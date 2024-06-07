package rorvirus;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Projectile {
	Random rand = new Random();
	float x, y;
	float speedX, speedY;
	float MovementSpeed = 10;
	public float radius = 5;
	private Color color = Color.RED;
	
	
	public Projectile(float x,float y, int angleInDegree) {
		this.x = x;
		this.y = y;
		this.speedX = (float)(MovementSpeed *  Math.cos(Math.toRadians(angleInDegree)));
		this.speedY = (float)(-MovementSpeed * (float) Math.sin(Math.toRadians(angleInDegree)));
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)(x - radius), (int)(y - radius), (int)(2 * radius), (int)(2 * radius));
	}
	
	public void movement(BattleArea map) {
		float chrMinX = map.minX + radius;
		float chrMinY = map.minY + radius;
		float chrMaxX = map.maxX - radius;
		float chrMaxY = map.maxY - radius;
		
		x += speedX;
		y += speedY;
		
		if (x < chrMinX) {
			radius=0;
		} else if (x > chrMaxX) {
			radius=0;
		}
		
		if (y < chrMinY) {
			radius=0;
		} else if (y > chrMaxY) {
			radius=0;
		}
	}
}
