package rorvirus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class BattleArea extends JComponent {
	int minX;
	int maxX;
	int minY;
	int maxY;
	public Color colorFilled;
	private Color colorBorder;
	private Image image;
	public BattleArea(int x, int y, int width, int height, Color colorFilled, Color colorBorder, Image image) {
		this.minX = x;
		this.minY = y;
		this.maxX = x + width - 1;
		this.maxY = y + height - 1;
		this.colorFilled = colorFilled;
		this.colorBorder = colorBorder;
		this.image = image;
	}
	
	public void set(int x, int y, int width, int height) {
		minX = x;
		minY = y;
		maxX = x + width - 1;
		maxY = y + height - 1;
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, 0, 0, 1140, 621, this);
		g.setColor(colorBorder);
		g.drawRect(minX, minY, maxX - minX - 1, maxY - minY - 1);
	}
}
