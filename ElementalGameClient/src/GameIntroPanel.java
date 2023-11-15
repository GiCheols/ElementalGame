import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class GameIntroPanel extends JPanel{
	
	private ImageIcon introImgIcon = new ImageIcon("src/static/image/background/TemplehallForest.jpg");
	private ImageIcon titleIcon = new ImageIcon("src/static/image/text/GameTitle.png");
	private ImageIcon menuIcon = new ImageIcon("src/static/image/elements/Menu.png");
	private ImageIcon waterGirlIcon = new ImageIcon("src/static/image/character/WaterGirl.png");
	private ImageIcon fireBoyIcon = new ImageIcon("src/static/image/character/FireBoy.png");
	
	private Image resizeIntroBackground = introImgIcon.getImage().getScaledInstance(GameClientFrame.SCREEN_WIDTH, GameClientFrame.SCREEN_HEIGHT, Image.SCALE_SMOOTH);
	private Image resizeWaterGirl = waterGirlIcon.getImage().getScaledInstance(GameClientFrame.SCREEN_WIDTH/4, GameClientFrame.SCREEN_HEIGHT/2, Image.SCALE_SMOOTH);
	private Image resizeFireBoy = fireBoyIcon.getImage().getScaledInstance(GameClientFrame.SCREEN_WIDTH/4, GameClientFrame.SCREEN_HEIGHT/2, Image.SCALE_SMOOTH);
	
	public static GameAudio audio;
	
	public GameIntroPanel() {
		
		setSize(GameClientFrame.SCREEN_WIDTH,GameClientFrame.SCREEN_HEIGHT);
		setBackground(Color.BLACK);
		setVisible(true);
		setLayout(null);
		setOpaque(false);
		
		//게임 타이틀 이미지 설정
		Image titleImage = titleIcon.getImage().getScaledInstance(650, 180, Image.SCALE_SMOOTH);
		titleIcon = new ImageIcon(titleImage);
		JLabel title = new JLabel(titleIcon);
		title.setBounds(GameClientFrame.SCREEN_WIDTH/2-titleIcon.getIconWidth()/2,15,titleIcon.getIconWidth(),titleIcon.getIconHeight());
		add(title);
		
		//메뉴 패널 설정
		MenuPanel menu = new MenuPanel();
		menu.setBounds(GameClientFrame.SCREEN_WIDTH/2-menu.getWidth()/2,10+titleIcon.getIconHeight(),menu.getWidth(),menu.getHeight());
		add(menu);

		
//		audio = new GameAudio("src/static/music/IntroMusic.wav");
//		audio.audioStart();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(resizeIntroBackground, 0, 0, null);
		g.drawImage(resizeWaterGirl,(GameClientFrame.SCREEN_WIDTH-GameClientFrame.SCREEN_WIDTH/4)-35,GameClientFrame.SCREEN_HEIGHT - GameClientFrame.SCREEN_HEIGHT/2 - 35,null);
		g.drawImage(resizeFireBoy,35,GameClientFrame.SCREEN_HEIGHT - GameClientFrame.SCREEN_HEIGHT/2 - 35,null);
		repaint();
	}
	
}


