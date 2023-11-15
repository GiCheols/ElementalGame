package MapObject;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Block{
	Image img = new ImageIcon("src/static/image/elements/block.png").getImage();
	int x, y, w, h;
	Rectangle blockRec;
	
	public static final int BLOCK_WIDTH = 31;
	public static final int BLOCK_HEIGHT = 32;
	
	public Block() {}
	
	public Block(int x, int y) {
		this.x= x;
		this.y= y;
		this.w = BLOCK_WIDTH;
		this.h = BLOCK_HEIGHT;
		this.blockRec = new Rectangle(x,y,w,h);
		//this.setBounds(x,y, w, h);
	}
	
	public Image getImg() {return img;}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return w;}
	public int getHeight() {return h;}
	public Rectangle getRectangle() {return blockRec;}
	
//	public void paintComponent(Graphics g) {
//		setLocation(x,y);
//		g.drawImage(img, 0, 0, w,  h, this);
//	}
}