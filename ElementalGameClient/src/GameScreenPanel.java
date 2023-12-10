import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

// 서버 입장 이후 게임하는 화면 (대기, 게임, 채팅...)
public class GameScreenPanel extends JPanel{

	private GameWaitPanel gameWaitPanel;
	private GameInfoPanel gameInfoPanel;
	private GamePlayPanel gamePlayPanel = null;
	private GameOverPanel gameOverPanel = null;
	private GameClearPanel gameClearPanel = null;

	public GameScreenPanel(int roomId, String userName) {
		setSize(GameClientFrame.SCREEN_WIDTH,
				GameClientFrame.SCREEN_HEIGHT);
		setLayout(null);
		setVisible(true);

		// 게임 대기 화면 (왼쪽)
		gameWaitPanel = new GameWaitPanel();
		add(gameWaitPanel);
		
		gameWaitPanel.setBackground(Color.BLACK);
		
		changeWaitPlayerNum();
		
		// 게임 정보 화면(오른쪽) (서버 정보, 홈버튼, 채팅...)
		gameInfoPanel = new GameInfoPanel();
		gameInfoPanel.setBounds(gameWaitPanel.getWidth(),
				0, gameInfoPanel.getWidth(),
				gameInfoPanel.getHeight());
		gameInfoPanel.setServerName(roomId);
		gameInfoPanel.setUserName(userName);
		add(gameInfoPanel);
		changePlayerList();
	}
	
	public void changeWaitPlayerNum() {
		gameWaitPanel.changePlayerNum(GameClientFrame.waitingPlayerNum); // gameRoom에 입장한 플레이어 수
		if (GameClientFrame.waitingPlayerNum == 2) {
			gameWaitPanel.addGameStartBtn();
		}
		this.repaint();
	}
	
	public void changeToPlayPanel() {
		remove(gameWaitPanel);
		if(gamePlayPanel == null) {
			gamePlayPanel = new GamePlayPanel();
			gamePlayPanel.setBounds(0, 0, gamePlayPanel.getWidth(), gamePlayPanel.getHeight());
			add(gamePlayPanel);
			addKeyListener(gamePlayPanel.testKey);
			gamePlayPanel.addMouseListener(new MouseAdapter() {
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
		remove(gamePlayPanel);
		if(gameOverPanel == null) {
			gameOverPanel = new GameOverPanel();
			gameOverPanel.setBounds(0, 0, gameOverPanel.getWidth(), gameOverPanel.getHeight());
			add(gameOverPanel);
		}
		this.repaint();
	}
	
	public void changeToGameClearPanel() {
		remove(gamePlayPanel);
		if(gameClearPanel == null) {
			gameClearPanel = new GameClearPanel();
			gameClearPanel.setBounds(0, 0, gameClearPanel.getWidth(), gameClearPanel.getHeight());
			add(gameClearPanel);
		}
		this.repaint();
	}
	
	public void changePlayerList() {
		gameInfoPanel.changePlayerList(GameClientFrame.playerNames);
		this.repaint();
	}
	
	public void removePlayerList(String name) {
		gameInfoPanel.removePlayerList(name);
		this.repaint();
	}
	
	public void setMovingInfo(int x, int y, State type) {
		if(gamePlayPanel !=null)
			gamePlayPanel.setMoving(x, y, type);
	}
	
	public void setDieImage() {
		gamePlayPanel.setDieImage();
	}
	
	

}
