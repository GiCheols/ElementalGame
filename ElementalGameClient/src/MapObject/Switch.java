package MapObject;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Switch{ // state 9
	Image img = new ImageIcon("src/static/image/elements/switch.png").getImage();
	int x, y, w, h;
	Rectangle switchkRec;
	private Boolean isSwitchOn = false;//스위치가 눌리지 않은 상태
	
	public static final int SWITHCH_WIDTH = 30;
	public static final int SWITHCH_HEIGHT = 12;
	
	private SwitchBlock manageBlock;
	
	
	public Switch() {}
	public ImageIcon resizeImage(ImageIcon icon, int width, int height) {
		 return new ImageIcon(icon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
	}
	
	public Switch(int x, int y) {
		this.x= x;
		this.y= y;
		this.w = SWITHCH_WIDTH;
		this.h = SWITHCH_HEIGHT;
		this.switchkRec = new Rectangle(x,y,w,h);
		setImg();
		//this.setBounds(x,y, w, h);
	}
	
	public void setImg() {
			img = resizeImage(new ImageIcon("src/static/image/elements/switch.png"),SWITHCH_WIDTH,SWITHCH_HEIGHT).getImage();
	}
	
	public void setManageBlock(SwitchBlock manageBlock) {
		this.manageBlock = manageBlock;
	}
	
	public void setSwitchState(Boolean value) {
		this.isSwitchOn = value;
		manage();
	}
	
	public void manage() {
		if (isSwitchOn) {
			manageBlock.setVisible(false);
		}
		else {
			manageBlock.setVisible(true);
		}
	}
	
	public Image getImg() {return img;}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return w;}
	public int getHeight() {return h;}
	public Rectangle getRectangle() {return switchkRec;}
	
}