import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


public class MenuPanel extends JPanel{
	/* Menu Panel에 들어갈 UI 요소*/
	private ImageIcon menuIcon;
	private Image resizeIntroBackground;
	
	private ImageIcon nameIcon;
	private ImageIcon resizenameIcon;
	
	private ImageIcon serverIcon;
	private ImageIcon resizeServerIcon;
	
	private ImageIcon startBtnIcon;
	private ImageIcon resizeStartBtnIcon;
	
	private ImageIcon startBtnClickedIcon;
	private ImageIcon resizeStartBtnClickedIcon;
	
	/* Color 및 Font 설정 */
	private Color contentColor =new Color(183,183,183);  
	private Color borderColor = new Color(23,23,23);
	private Font fieldFont = new Font("Arial", Font.PLAIN, 20);

	/* Server 정보 */
	private String[] optionsToChoose = {"Server1", "Server2", "Server3"};
	private final int BASE_PORT = 10000; // 기본 port number
	private final int PORT_GAP = 10000; // port 사이 간격

	private final int PORT = 9090;
	
	public MenuPanel() {
		
		/* MenuPanel 설정 - size, visibility, layout, opaque */
		setSize(500,287); 
		setVisible(true);
		setLayout(null);
		setOpaque(false);
		
		/* Panel Elements 이미지 설정 */
		setElementsImages();
		
		/* Name 글씨 이미지 UI & 위치 설정 */
		JLabel name = new JLabel(resizenameIcon);
		name.setBounds(55,85,resizenameIcon.getIconWidth(),resizenameIcon.getIconHeight());
		add(name);
		
		/* Name 입력창 UI & 위치 설정 */
		JTextField nameTextField = new JTextField();
		nameTextField.setBounds(190,85,240,34);
		nameTextField.setFont(fieldFont);
		nameTextField.setBackground(contentColor);
		nameTextField.setBorder(new LineBorder(borderColor,2));
		add(nameTextField);
		
		/* Server 글씨 이미지 UI & 위치 설정 */
		JLabel server = new JLabel(resizeServerIcon);
		server.setBounds(55,100+resizenameIcon.getIconHeight(),resizeServerIcon.getIconWidth(),resizeServerIcon.getIconHeight());
		add(server);
		
		/* Server 선택 콤보박스 UI & 위치 설정 */
		JComboBox<String> serverComboBox = new JComboBox<>(optionsToChoose);
		serverComboBox.setBounds(190,103+resizenameIcon.getIconHeight(),240,34);
		serverComboBox.setOpaque(false);
		serverComboBox.setFont(fieldFont);
		serverComboBox.setBackground(contentColor);
		serverComboBox.setBorder(new LineBorder(borderColor,2));
		add(serverComboBox);
				
		/* 게임 시작 버튼 UI & 위치 설정 */
		JButton startBtn = new JButton(resizeStartBtnIcon);
		startBtn.setContentAreaFilled(false);
		startBtn.setBorderPainted(false);
		startBtn.setFocusPainted(false);
		startBtn.setOpaque(false);
		startBtn.setBounds(500/2 - resizeStartBtnIcon.getIconWidth()/2,195,resizeStartBtnIcon.getIconWidth(),resizeStartBtnIcon.getIconHeight());
		startBtn.addMouseListener(new MouseAdapter() { // game start 버튼 눌렀을 때 동작
			
			public void mouseEntered(MouseEvent e) {
				startBtn.setIcon(resizeStartBtnClickedIcon);
			}
			
			public void mouseExited(MouseEvent e) {
				startBtn.setIcon(resizeStartBtnIcon);
			}
			
			public void mousePressed(MouseEvent e) {
				
				int index = serverComboBox.getSelectedIndex(); // 서버 넘버
				System.out.println("server "+optionsToChoose[index]+" selected!!");
				
				
				if (nameTextField.getText().equals("")) { // 이름 입력 안했을 시
					
					System.out.println("nameTextField is empty");
					JOptionPane.showMessageDialog(null,"이름을 입력해주세요");
				}
				else {									
//					int port = index * PORT_GAP + BASE_PORT;//선택된 서버에 따라 port번호 다르게 지정
					GameClientFrame.net = new ListenNetwork(nameTextField.getText(),PORT,index);
					GameClientFrame.net.start();
					GameClientFrame.roomId = index; // 플레이어가 입장한 room id -> 0,1,2
					GameClientFrame.userName = nameTextField.getText();
				}
			}
		});
		
		add(startBtn);
		
	} // end of MenuPanel Constructor..
	

		
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(resizeIntroBackground, 0, 0, null);
		repaint();
	}
	
	public ImageIcon resizeImage(ImageIcon icon, int width, int height) {
		
		 return new ImageIcon(icon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
	}
	
	private void setElementsImages() {
		
		 menuIcon = new ImageIcon("src/static/image/elements/Menu.png");
		 resizeIntroBackground = menuIcon.getImage().getScaledInstance(500, 287, Image.SCALE_SMOOTH);
		 
		 nameIcon = new ImageIcon("src/static/image/text/name.png");
		 resizenameIcon = resizeImage(nameIcon,110,40);//new ImageIcon(nameIcon.getImage().getScaledInstance(110,40,Image.SCALE_SMOOTH));
		 
		 serverIcon = new ImageIcon("src/static/image/text/server.png");
		 resizeServerIcon = resizeImage(serverIcon,110,40);//new ImageIcon(serverIcon.getImage().getScaledInstance(110,40,Image.SCALE_SMOOTH));
		 
		 startBtnIcon = new ImageIcon("src/static/image/elements/unclickedStartBtn.png");
		 resizeStartBtnIcon = resizeImage(startBtnIcon,150,55);//new ImageIcon(startBtnIcon.getImage().getScaledInstance(150,55,Image.SCALE_SMOOTH));
		 
		 startBtnClickedIcon = new ImageIcon("src/static/image/elements/clickedStartBtn.png");
		 resizeStartBtnClickedIcon = resizeImage(startBtnClickedIcon,150,55);//new ImageIcon(startBtnClickedIcon.getImage().getScaledInstance(150,55,Image.SCALE_SMOOTH));
	}
	
}
