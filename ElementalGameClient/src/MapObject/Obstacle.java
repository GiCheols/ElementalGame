package MapObject;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Obstacle{ //4는 water obstacle, 5은 fire obstacle
	
	public static final int ITEM_WIDTH = 124;
	public static final int ITEM_HEIGHT = 32;
	private int state;
	
	Image img;
	Rectangle obstacleRec;
	
	public ImageIcon resizeImage(ImageIcon icon, int width, int height) {
		
		 return new ImageIcon(icon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
	}
	
	int x, y, w, h;
	
	
	public Obstacle() {}
	
	public Obstacle(int x, int y, int state) {
		this.x= x;
		this.y= y;
		this.w = ITEM_WIDTH;
		this.h = ITEM_HEIGHT;
		this.state = state;
		this.obstacleRec = new Rectangle(x,y,w,h);
		setImg();
	}
	public void setImg() {
		if (state < 0) {
			img = resizeImage(new ImageIcon("src/static/image/elements/poison_obstacle.png"),ITEM_WIDTH,ITEM_HEIGHT).getImage();
		}
		else if(state%2==0) { //water obstacle
			img = resizeImage(new ImageIcon("src/static/image/elements/water_obstacle.png"),ITEM_WIDTH,ITEM_HEIGHT).getImage();
		}
		else { // fire obstacle
			img = resizeImage(new ImageIcon("src/static/image/elements/fire_obstacle.png"),ITEM_WIDTH,ITEM_HEIGHT).getImage();
		}
	}
	public Image getImg() {return img;}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return w;}
	public int getHeight() {return h;}
	public int getState() {return state;}
	public Rectangle getRectangle() {return obstacleRec;}
	
}