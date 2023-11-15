import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// 서버 입장 이후 게임하는 화면 (대기, 게임, 채팅...)
public class GameScreenPanel extends JPanel{

	private GameWaitPanel gameWaitPane;
	private GameInfoPanel gameInfoPane;
	private GamePlayPanel gamePlayPane = null;
	private GameOverPanel gameOverPane = null;
	private GameClearPanel gameClearPane = null;

	public GameScreenPanel(int roomId, String userName) {
		setSize(GameClientFrame.SCREEN_WIDTH,GameClientFrame.SCREEN_HEIGHT);
		System.out.println(GameClientFrame.SCREEN_WIDTH+","+GameClientFrame.SCREEN_HEIGHT);
		setLayout(null);
		setVisible(true);

		// 게임 대기 화면 (왼쪽)
		gameWaitPane = new GameWaitPanel();
		add(gameWaitPane);
		
		gameWaitPane.setBackground(Color.BLACK);
		
		changeWaitPlayerNum();
		
		// 게임 정보 화면(오른쪽) (서버 정보, 홈버튼, 채팅...)
		gameInfoPane = new GameInfoPanel();
		gameInfoPane.setBounds(gameWaitPane.getWidth(), 0, gameInfoPane.getWidth(), gameInfoPane.getHeight());
		gameInfoPane.setServerName(roomId);
		gameInfoPane.setUserName(userName);
		add(gameInfoPane);
		changePlayerList();
	}
	
	public void changeWaitPlayerNum() {
		gameWaitPane.changePlayerNum(GameClientFrame.waitingPlayerNum); // gameRoom에 입장한 플레이어 수
		if (GameClientFrame.waitingPlayerNum == 2) {
			gameWaitPane.addGameStartBtn();
		}
		this.repaint();
	}
	
	public void changeToPlaypanel() {
		remove(gameWaitPane);
		if(gamePlayPane == null) {
			gamePlayPane = new GamePlayPanel();
			gamePlayPane.setBounds(0, 0, gamePlayPane.getWidth(), gamePlayPane.getHeight());
			add(gamePlayPane);
			addKeyListener(gamePlayPane.testKey);
			gamePlayPane.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("mouse click");
					requestFocus();
					setFocusable(true);
				}
			});
		}
		this.repaint();
	}
	
	public void changeToGameOverPanel() {
		remove(gamePlayPane);
		if(gameOverPane == null) {
			gameOverPane = new GameOverPanel();
			gameOverPane.setBounds(0, 0, gameOverPane.getWidth(), gameOverPane.getHeight());
			add(gameOverPane);
		}
		this.repaint();
	}
	
	public void changeToGameClearPanel() {
		remove(gamePlayPane);
		if(gameClearPane == null) {
			gameClearPane = new GameClearPanel();
			gameClearPane.setBounds(0, 0, gameClearPane.getWidth(), gameClearPane.getHeight());
			add(gameClearPane);
		}
		this.repaint();
	}
	
	public void changePlayerList() {
		gameInfoPane.changePlayerList(GameClientFrame.playerNames);
		this.repaint();
	}
	
	public void removePlayerList(String name) {
		gameInfoPane.removePlayerList(name);
		this.repaint();
	}
	
	public void setMovingInfo(int x, int y, State type) {
		if(gamePlayPane!=null)
			gamePlayPane.setMoving(x, y, type);
	}
	
	public void setDieImage() {
		gamePlayPane.setDieImage();
	}
	
	

}
