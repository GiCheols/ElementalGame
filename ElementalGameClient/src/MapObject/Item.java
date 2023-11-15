package MapObject;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Item{ //2는 watergirl gem, 3은 fireboy gem
	
	public static final int ITEM_WIDTH = 21;
	public static final int ITEM_HEIGHT = 21;
	private int state;
	
	Image img;
	
	public ImageIcon resizeImage(ImageIcon icon, int width, int height) {
		
		 return new ImageIcon(icon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
	}
	
	int x, y, w, h;
	
	
	public Item() {}
	
	public Item(int x, int y, int state) {
		this.x= x;
		this.y= y;
		this.w = ITEM_WIDTH;
		this.h = ITEM_HEIGHT;
		this.state = state;
		setImg();
	}
	public void setImg() {
		if(state%2==0) { //water gem
			img = resizeImage(new ImageIcon("src/static/image/elements/water_gem.png"),ITEM_WIDTH,ITEM_HEIGHT).getImage();
		}
		else { // fire gem
			img = resizeImage(new ImageIcon("src/static/image/elements/fire_gem.png"),ITEM_WIDTH,ITEM_HEIGHT).getImage();
		}
	}
	public Image getImg() {return img;}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return w;}
	public int getHeight() {return h;}
	public int getState() {return state;}
	
}