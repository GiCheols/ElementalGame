package MapObject;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class SwitchBlock{ //state 8
	
	public static final int ITEM_WIDTH = 93;
	public static final int ITEM_HEIGHT = 16;
	private Boolean isVisible = true;
	Image img;
	Rectangle obstacleRec;
	int x, y, w, h;

	public SwitchBlock() {}
	
	public SwitchBlock(int x, int y) {
		this.x= x;
		this.y= y;
		this.w = ITEM_WIDTH;
		this.h = ITEM_HEIGHT;
		this.obstacleRec = new Rectangle(x,y,w,h);
		setImg();
	}
	
	public ImageIcon resizeImage(ImageIcon icon, int width, int height) {
		 return new ImageIcon(icon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
	}
	
	public void setImg() {
			img = resizeImage(new ImageIcon("src/static/image/elements/switch_block2.png"),ITEM_WIDTH,ITEM_HEIGHT).getImage();
	}
	public void setVisible(Boolean value) {isVisible = value;}
	public Boolean getIsVisible() {return isVisible;}
	public Image getImg() {return img;}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return w;}
	public int getHeight() {return h;}
	public Rectangle getRectangle() {return obstacleRec;}
	
	
}