package rorvirus;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Enemy extends JComponent{
	Random rand = new Random();
	float HealthPoint = 10;
	float MovementSpeed = 2;
	float AttackPoint = 1;

	float x, y;
	float speedX, speedY;
	int angleInDegree = rand.nextInt(360);
	public int radius = 15;
	private Image image;

	public Enemy(float x, float y, Image image) {
		this.x = x;
		this.y = y;
		this.speedX = (float) (MovementSpeed * Math.cos(Math.toRadians(angleInDegree)));
		this.speedY = (float) (-MovementSpeed * (float) Math.sin(Math.toRadians(angleInDegree)));
		this.image = image;
	}

	public void draw(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(image, (int)x-35, (int)y-40, 75, 75, this);
	}

	public void movement(BattleArea map) {
		float chrMinX = map.minX + radius;
		float chrMinY = map.minY + radius;
		float chrMaxX = map.maxX - radius;
		float chrMaxY = map.maxY - radius;

		x += speedX;
		y += speedY;

		if (x < chrMinX) {
			speedX = -speedX;
			x = chrMinX;
		} else if (x > chrMaxX) {
			speedX = -speedX;
			x = chrMaxX;
		}

		if (y < chrMinY) {
			speedY = -speedY;
			y = chrMinY;
		} else if (y > chrMaxY) {
			speedY = -speedY;
			y = chrMaxY;
		}
	}
	public boolean death() {
		if(this.HealthPoint <= 0) {
			this.MovementSpeed = 0;
			this.AttackPoint = 0;
			this.radius = 0;
			return true;
		}
		return false;
	}
	
	public void attack(Character chr, Projectile pro) {
		
		double xDif = this.x - (chr.x + 65);
		double yDif = this.y - (chr.y + 65);
		double distanceSquared = xDif * xDif + yDif * yDif;
		boolean chrcollision = distanceSquared < (this.radius + chr.radius) * (this.radius + chr.radius);
		
		if ((chrcollision && chr.HealthPoint > 0)) {
			chr.HealthPoint -= this.AttackPoint;
			speedX = -speedX;
			speedY = -speedY;
		}
	}
	
	public boolean getattacked(Projectile pro, Character chr) {
		double xDif = this.x - (pro.x);
		double yDif = this.y - (pro.y);
		double distanceSquared = xDif * xDif + yDif * yDif;
		boolean procollision = distanceSquared < (this.radius + pro.radius) * (this.radius + pro.radius);
		
		if (procollision) {
			return true;
		}
		return false;
	}
	
	
}