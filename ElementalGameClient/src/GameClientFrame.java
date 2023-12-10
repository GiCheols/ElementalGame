import javax.swing.*;

import java.util.ArrayList;

public class GameClientFrame extends JFrame{
		
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 600;
	
	public static ListenNetwork net = null;
	
	
	public static boolean isHomeScreen;
	public static boolean isGameScreen;
	public static boolean isChanged;
	public static boolean isNextStage;
	public static boolean isWaitingScreen;
	public static boolean isPlayingScreen;
	public static boolean isGameOverScreen;
	public static boolean isGameClearScreen;
	public static int roomId;
	public static String userName;

	public static int userNum; // 첫번째 유저인지, 두번째 유저인지
	public static int waitingPlayerNum;


	public static GameScreenPanel gameScreenPane = null;
	public static ArrayList<String> playerNames = new ArrayList<>();
	

	public GameClientFrame() {
		
		init();
		setTitle("Elemental Game");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		setContentPane(new GameIntroPanel());
		GameThread gth = new GameThread(); // 게임 화면 전환 스레드(게임 전반적 관리)
		gth.start();
		setVisible(true);

		this.requestFocus();
		this.setFocusable(true);
	}
	
	class GameThread extends Thread{ // 게임 전반적 관리
		
		public void run() {
			while(true) {
				if(isChanged) { // 화면에 변화가 필요할 때
					System.out.println("CHANGE AT SCREEN");
					isChanged = false;	
					selectScreen(); // 변경될 화면 선택
				}
				try {
					Thread.sleep(10);
				}catch(InterruptedException e) {
					return;
				}
			}
		}	
	}
	

	static void init() {
		isHomeScreen = false; // 홈화면
		isGameScreen = false; // 서버 선택 후 입장한 화면
		isNextStage = false;
		isChanged = false; // 화면의 변화가 필요한지 여부
		isWaitingScreen = false; // 대기 화면
		isPlayingScreen = false; // 게임 중인 화면
		gameScreenPane = null;
		playerNames.clear();
	}

	public void selectScreen() {
		if (isHomeScreen) { // 홈화면
			isHomeScreen = false;
			init();
			System.out.println("HOME");
			setContentPane(new GameIntroPanel());
		}
		else if (isGameScreen) { // 대기화면
			isGameScreen = false;
			System.out.println("WAITING SCREEN");
			if(gameScreenPane == null) {
				System.out.println("gameScreenPane == null");
				gameScreenPane = new GameScreenPanel(roomId,userName);
				setContentPane(gameScreenPane);
				gameScreenPane.requestFocus();
				gameScreenPane.setFocusable(true);
			}
			else {
				System.out.println("gameScreenPane != null");
				gameScreenPane.changeWaitPlayerNum();
				gameScreenPane.changePlayerList();
			}
		}
		else if (isNextStage) {
			isNextStage = false;
		}
		else if (isPlayingScreen) {
			System.out.println("GAME SCREEN");
			isPlayingScreen = false;
			gameScreenPane.changeToPlayPanel();
		}
		else if (isGameOverScreen) {
			System.out.println("GAME OVER...");
			isGameOverScreen = false;
			gameScreenPane.changeToGameOverPanel();
		}
		else if (isGameClearScreen) {
			System.out.println("GAME CLEAR");
			isGameClearScreen = false;
			gameScreenPane.changeToGameClearPanel();
		}
	}
}




