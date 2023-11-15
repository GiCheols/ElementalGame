import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameOverPanel extends JPanel{
	private JLabel gameOverLabel = new JLabel();
	private Icon gameoverIcon;
	private String gameOverImgPath = "src/static/image/text/gameover.png";
	 
	private ImageIcon exitBtnIcon;
	private ImageIcon resizeExitBtnIcon;
	
	private ImageIcon exitBtnClickedIcon;
	private ImageIcon resizeExitBtnClickedIcon;
	
	public final int WIDTH = 713;
	public final int HEIGHT = 544;
	
	public GameOverPanel() {
		setSize(WIDTH, HEIGHT); 
		setLayout(null);
		setVisible(true);
		setBackground(Color.BLACK);
//		setElementsImages();
		
		gameOverLabel.setBounds(214,189,284,100);
		gameoverIcon = new ImageIcon(new ImageIcon(gameOverImgPath).getImage().getScaledInstance(gameOverLabel.getWidth(),gameOverLabel.getHeight(),Image.SCALE_DEFAULT));
		gameOverLabel.setIcon(gameoverIcon);
		this.add(gameOverLabel);
		
		exitBtnIcon = new ImageIcon("src/static/image/elements/unclickedExitBtn.png");
		resizeExitBtnIcon = resizeImage(exitBtnIcon,150,55);
	 
		exitBtnClickedIcon = new ImageIcon("src/static/image/elements/clickedExitBtn.png");
		resizeExitBtnClickedIcon = resizeImage(exitBtnClickedIcon,150,55);
		
		JButton exitBtn = new JButton(resizeExitBtnIcon);
		exitBtn.setContentAreaFilled(false);
		exitBtn.setBorderPainted(false);
		exitBtn.setFocusPainted(false);
		exitBtn.setOpaque(false);
		exitBtn.setBounds(WIDTH/2 - resizeExitBtnIcon.getIconWidth()/2,HEIGHT/2+80- resizeExitBtnIcon.getIconHeight()/2,resizeExitBtnIcon.getIconWidth(),resizeExitBtnIcon.getIconHeight());
		add(exitBtn);
		exitBtn.addMouseListener(new MouseAdapter() { // exit 버튼 눌렀을 때 동작
			public void mouseEntered(MouseEvent e) {
				exitBtn.setIcon(resizeExitBtnClickedIcon);
			}
			
			public void mouseExited(MouseEvent e) {
				exitBtn.setIcon(resizeExitBtnIcon);
			}
			
			public void mousePressed(MouseEvent e) {
				GameClientFrame.isChanged = true;
				GameClientFrame.isHomeScreen = true;
				GameClientFrame.net.exitRoom();
			}
		});
	}
		
	public ImageIcon resizeImage(ImageIcon icon, int width, int height) {
		 return new ImageIcon(icon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
	}
}
