package rorvirus;

import java.awt.*;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Character extends JComponent{
	float HealthPoint = 100;
	float MovementSpeed = 2;
	float Stamina = 100;
	float AttackPoint = 5;
	
	float x, y;
	float speedX, speedY;
	public float radius = 10;
	private Image image;
	
	public Character(float x, float y, Image image) {
		this.x = x;
		this.y = y;
		this.speedX = 0;
		this.speedY = 0;
		this.image = image;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(image, (int)x, (int)y, 120, 120, this);
	}
	
	public boolean death() {
		if(HealthPoint <= 0) {
			this.MovementSpeed = 0;
			this.Stamina = 0;
			this.AttackPoint = 0;
			this.radius = 0;
			return true;
		}
		return false;
	}
	
	public void movement(BattleArea map) {
		float chrMinX = map.minX + radius;
		float chrMinY = map.minY + radius;
		float chrMaxX = map.maxX - radius;
		float chrMaxY = map.maxY - radius;
		
		x += speedX;
		y += speedY;
		
		if (x < chrMinX) {
			x = chrMinX;
		} else if (x > chrMaxX) {
			x = chrMaxX;
		}
		
		if (y < chrMinY) {
			y = chrMinY;
		} else if (y > chrMaxY) {
			y = chrMaxY;
		}
	}
}
