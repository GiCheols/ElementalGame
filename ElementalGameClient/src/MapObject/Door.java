package MapObject;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Door { //6는 water door, 7은 fire door
	
	public static final int DOOR_WIDTH = 56;
	public static final int DOOR_HEIGHT = 64;
	private int state;
//	private String openFireDoorImgPath = "src/static/image/character/fire boy_enter room.gif";
//	private String openWaterDoorImgPath = "src/static/image/character/water girl_enter room.gif";
	
	Image img;
	
	public ImageIcon resizeImage(ImageIcon icon, int width, int height) {
		
		 return new ImageIcon(icon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
	}
	
	int x, y, w, h;
	
	
	public Door() {}
	
	public Door(int x, int y, int state) {
		this.x= x;
		this.y= y;
		this.w = DOOR_WIDTH;
		this.h = DOOR_HEIGHT;
		this.state = state;
		setImg();
	}
	public void setImg() {
		if(state%2==0) { //water door
			img = resizeImage(new ImageIcon("src/static/image/elements/water_door.png"),DOOR_WIDTH,DOOR_HEIGHT).getImage();
		}
		else { // fire door
			img = resizeImage(new ImageIcon("src/static/image/elements/fire_door.png"),DOOR_WIDTH,DOOR_HEIGHT).getImage();
		}
	}
	
	public Image getImg() {return img;}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return w;}
	public int getHeight() {return h;}
	public int getState() {return state;}
//	public String getOpenFireDoorImgPath() {return openFireDoorImgPath;}
//	public String getOpenWaterDoorImgPath() {return openWaterDoorImgPath;}
}