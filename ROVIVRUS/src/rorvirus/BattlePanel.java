package rorvirus;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class BattlePanel extends JPanel implements MouseListener, KeyListener {
	private static final int REFRESH_RATE = 60;
	private Character chr;
	private BattleArea map;
	private Enemy en0;
	private Projectile pro;
	ArrayList<Enemy> enemies = new ArrayList<Enemy>(5);
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>(5);

	private int areaWidth = 1140;
	private int areaHeight = 621;
	private int score = 0;
	private String text;
	private String textdeath;
	private String textscore;
	boolean mouseIsClicked;
	
	private SimpleDateFormat format;
	private long startDate, endDate;
	
	private BufferedImage myImage = null;

	public BattlePanel() {

		this.setPreferredSize(new Dimension(areaWidth, areaHeight));
		int x = areaWidth / 2;
		int y = areaHeight / 2;

		this.addMouseListener(this);
		this.setFocusable(true);

		this.addKeyListener(this);
		this.setFocusable(true);

		try {
			myImage = ImageIO.read(new File("battleback1.png"));
		} catch (IOException e1) {}
		map = new BattleArea(0, 0, areaWidth, areaHeight, Color.BLACK, Color.WHITE, myImage);

		try {
			myImage = ImageIO.read(new File("wizard.png"));
		} catch (IOException e1) {}
		chr = new Character(x, y, myImage);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Component c = (Component) e.getSource();
				Dimension dim = c.getSize();
				areaWidth = dim.width;
				areaHeight = dim.height;
				map.set(0, 0, areaWidth, areaHeight);
			}
		});
		format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		startDate = generateDate();
		startThread();
	}

	public void startThread() {
		Thread gameThread = new Thread() {
			public void run() {
				while (true) {
					chr.movement(map);
					chr.death();
					death();
					score();
					stats();
					for (Enemy en : enemies) {
						en.movement(map);
						en.attack(chr, pro);
						if(mouseIsClicked) {
							if(en.getattacked(pro, chr)) {
								score++;
							}
						}
					}
					autoSpawn();
					repaint();
					if(mouseIsClicked) {
						pro.movement(map);
					}
					try {
						Thread.sleep(1000 / REFRESH_RATE);
					} catch (InterruptedException ex) {
					}
				}
			}
		};
		gameThread.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		map.draw(g);
		chr.draw(g);

		for (Enemy en : this.enemies) {
			en.draw(g);
		}

		g.setColor(Color.GREEN);
		g.setFont(new Font("Poppins", Font.BOLD, 10));
		g.drawString(this.text, 10, 15);

		if (chr.HealthPoint == 0) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, areaWidth, areaHeight);
			g.setColor(Color.RED);
			g.setFont(new Font("Poppins", Font.BOLD, 30));
			g.drawString(this.textdeath, areaWidth / 2 - 100, areaHeight / 2 + 20);
			g.drawString(this.textscore, areaWidth / 2 - 80, areaHeight / 2 + 50);
		}
		
		if (mouseIsClicked) {
			pro.draw(g);
		}
	}

	public void stats() {
		this.text = "HP : " + String.valueOf(chr.HealthPoint) 
					+ "\r\n   Stamina : " + String.valueOf(chr.Stamina)
					+ "\r\n   Attack Point : " + String.valueOf(chr.AttackPoint)
					+ "\r\n   Score : " + String.valueOf(this.score);
		repaint();
	}
	
	public void death() {
		this.textdeath = "GAME OVER";
		repaint();
	}
	
	public void score() {
		this.textscore = "Score : " + String.valueOf(this.score);
		repaint();
	}
	
	private void autoSpawn() {  
	    endDate = generateDate();
		long diff = (endDate - startDate) / 1000;
		if(diff >= 3 && !chr.death()) {
			this.score++;
			addenemy();
			startDate = endDate;
		}
	}
	
//	private void addScore() {  
//	    endDate = generateDate();
//		long diff = (endDate - startDate) / 1000;
//		if(diff >= 0.5) {
//			this.score++;
//			startDate = endDate;
//		}
//	}
	
	private long generateDate() {
		Date date = new Date();
		String newDate = format.format(date);
		try {
		//konfersi tipe data
			date = format.parse(newDate);
		} catch (ParseException e) {}
	    return date.getTime();
	}

	public void addenemy() {
		this.setPreferredSize(new Dimension(areaWidth, areaHeight));

		Random rand = new Random();
		float radius = 10;
		int x = (int) (rand.nextInt((int) (areaWidth - radius * 2 - 20)) + radius + 10);
		int y = (int) (rand.nextInt((int) (areaHeight - radius * 2 - 20)) + radius + 10);

		int max = 2;
		int min = 1;
		int range = max - min + 1;
		// random pirate 1/2
		int randImage = (int) (Math.random() * range) + min;

		if (randImage == 1) {
			try {
				myImage = ImageIO.read(new File("pirate1.png"));
			} catch (IOException e1) {}
		} 
		
		else {
			try {
				myImage = ImageIO.read(new File("pirate2.png"));
			} catch (IOException e1) {}
		}
		
		en0 = new Enemy(x, y, myImage);
		enemies.add(en0);
	}
	
	
	public void chrat() {
		int ang;
		if (chr.speedX == 0 && chr.speedY == 0) {
			ang = 0;
			pro = new Projectile(chr.x + 50, chr.y + 50, ang);
		}
		if (chr.speedX == chr.MovementSpeed && chr.speedY == 0) {
			ang = 0;
			pro = new Projectile(chr.x + 50, chr.y + 50, ang);
		}
		if (chr.speedX == chr.MovementSpeed && chr.speedY == -chr.MovementSpeed) {
			ang = 45;
			pro = new Projectile(chr.x + 50, chr.y + 50, ang);
		}
		if (chr.speedX == 0 && chr.speedY == -chr.MovementSpeed) {
			ang = 90;
			pro = new Projectile(chr.x + 50, chr.y + 50, ang);
		}
		if (chr.speedX == -chr.MovementSpeed && chr.speedY == -chr.MovementSpeed) {
			ang = 135;
			pro = new Projectile(chr.x + 50, chr.y + 50, ang);
		}
		if (chr.speedX == -chr.MovementSpeed && chr.speedY == 0) {
			ang = 180;
			pro = new Projectile(chr.x + 50, chr.y + 50, ang);
		}
		if (chr.speedX == -chr.MovementSpeed && chr.speedY == chr.MovementSpeed) {
			ang = 225;
			pro = new Projectile(chr.x + 50, chr.y + 50, ang);
		}
		if (chr.speedX == 0 && chr.speedY == chr.MovementSpeed) {
			ang = 270;
			pro = new Projectile(chr.x + 50, chr.y + 50, ang);
		}
		if (chr.speedX == chr.MovementSpeed && chr.speedY == chr.MovementSpeed) {
			ang = 315;
			pro = new Projectile(chr.x + 50, chr.y + 50, ang);
		}
	}

	
	public void mouseClicked(MouseEvent e) {
		mouseIsClicked = true;
		chrat();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		// free the ball
	}

	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	public void mouseExited(MouseEvent e) {
		// do nothing
	}

	public void keyTyped(KeyEvent e) {
		// do nothing
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_W) {
			if (chr.speedY > -chr.MovementSpeed) {
				chr.speedY -= chr.MovementSpeed;
			}
		}

		if (key == KeyEvent.VK_A) {
			if (chr.speedX > -chr.MovementSpeed) {
				chr.speedX -= chr.MovementSpeed;
			}
		}

		if (key == KeyEvent.VK_S) {
			if (chr.speedY < chr.MovementSpeed) {
				chr.speedY += chr.MovementSpeed;
			}
		}

		if (key == KeyEvent.VK_D) {
			if (chr.speedX < chr.MovementSpeed) {
				chr.speedX += chr.MovementSpeed;
			}
		}

		if (key == KeyEvent.VK_SPACE) {
			if (chr.speedY == -chr.MovementSpeed && chr.Stamina > 0) {
				chr.speedY = -(chr.MovementSpeed * 2);
				chr.Stamina -= 10;
			}
			if (chr.speedX == -chr.MovementSpeed && chr.Stamina > 0) {
				chr.speedX = -(chr.MovementSpeed * 2);
				chr.Stamina -= 10;
			}
			if (chr.speedY == chr.MovementSpeed && chr.Stamina > 0) {
				chr.speedY = (chr.MovementSpeed * 2);
				chr.Stamina -= 10;
			}
			if (chr.speedX == chr.MovementSpeed && chr.Stamina > 0) {
				chr.speedX = (chr.MovementSpeed * 2);
				chr.Stamina -= 10;
			}
		}

		if (key == KeyEvent.VK_E) {
			addenemy();
		}
		if (key == KeyEvent.VK_R) {
			if (!enemies.isEmpty()) {
				enemies.get(0).attack(chr, pro);
				enemies.remove(0);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_W) {
			chr.speedY += chr.MovementSpeed;
		}

		if (key == KeyEvent.VK_A) {
			chr.speedX += chr.MovementSpeed;
		}

		if (key == KeyEvent.VK_S) {
			chr.speedY -= chr.MovementSpeed;
		}

		if (key == KeyEvent.VK_D) {
			chr.speedX -= chr.MovementSpeed;
		}

		if (key == KeyEvent.VK_SPACE) {
			if (chr.speedY == -(chr.MovementSpeed * 2)) {
				chr.speedY = -chr.MovementSpeed;
			}

			if (chr.speedX == -(chr.MovementSpeed * 2)) {
				chr.speedX = -chr.MovementSpeed;
			}

			if (chr.speedY == (chr.MovementSpeed * 2)) {
				chr.speedY = chr.MovementSpeed;
			}

			if (chr.speedY == (chr.MovementSpeed * 2)) {
				chr.speedY = chr.MovementSpeed;
			}
		}
	}

}
