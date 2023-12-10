import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

//Server Message를 수신해서 화면에 표시
public class ListenNetwork extends Thread {
	String ipAddr = "127.0.0.1";
	int portNo;
	int roomId;
	int playerCharacter = 0;
	
	private static final String ALLOW_LOGIN_MSG = "ALLOW";
	private static final String DENY_LOGIN_MSG = "DENY";

	public static Socket socket;  // 연결소켓
	public static InputStream is;
	public static OutputStream os;
	public static ObjectInputStream ois;
	public static ObjectOutputStream oos;
	
	public static boolean isPlayingGame = false;
	private String userName;
	
	public ListenNetwork(String userName, int portNo, int roomId) {
		this.userName = userName;
		this.portNo = portNo;
		System.out.println(ipAddr);
		System.out.println(portNo);
		this.roomId = roomId;
		try {
			socket = new Socket(ipAddr, portNo);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			Broker broker = new Broker(userName, roomId, "100", ""); // gameRoom 입장 시도
			sendObject(broker);
		} catch (NumberFormatException | IOException error) {
			JOptionPane.showMessageDialog(null,"Server Can't connect");
		}
	}
	
	public void run() {
		while (true) {
			try {
				Object obcm = null;
				Broker broker = null;
				MovingInfo mi = null;
				try {
					obcm = ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				if (obcm == null) {
					break;
				}
				if (obcm instanceof Broker) {
					broker = (Broker) obcm;
				}else if (obcm instanceof MovingInfo) {
					mi = (MovingInfo) obcm;
				}
				else
					continue;
				
				if(broker != null) {
					switch (broker.getCode()) {
					case "100": // 서버 접속 결과 - allow,deny
						System.out.println(broker.getData());
						String loginResult = broker.getData().split(" ")[0];
						System.out.println("loginResult = "+loginResult);
						if(loginResult.equals(ALLOW_LOGIN_MSG)) {
							GameClientFrame.isWaitingScreen = true;
							/* 화면 전환 */
							switch(broker.getData().split(" ")[1]) {
							case "1":
								if(playerCharacter == 0) // 처음 입장한 플레이어인 경우
									playerCharacter = 1;
								GameClientFrame.waitingPlayerNum = 1;
								GameClientFrame.playerNames.add(userName);
								break;
							case "2":
								String[] playerNames = broker.getData().split(" ")[2].split("//");
								if(playerCharacter == 0) {// 2번째로 입장한 플레이어인 경우
									playerCharacter = 2;
									for(int i = 0; i < playerNames.length; i++)
										GameClientFrame.playerNames.add(playerNames[i]);
								}
								else { // 대기하고 있던 플레이어인 경우
									GameClientFrame.playerNames.add(playerNames[1]);
								}
								GameClientFrame.waitingPlayerNum = 2;
								break;
							}
							
							System.out.println(userName+" : "+playerCharacter+"번 캐릭터");
							GameClientFrame.userNum = playerCharacter;
	
							GameClientFrame.isChanged = true; // 화면 변화가 필요함
							GameClientFrame.isGameScreen = true; // 게임 대기화면으로 변화
						}
						else if(loginResult.equals(DENY_LOGIN_MSG)) {
							GameClientFrame.isWaitingScreen = false;
							JOptionPane.showMessageDialog(null,"해당 서버는 가득 찼습니다. 다른 서버를 선택해주세요.");
							return;
						}
						break;
					case "200":
						System.out.println("채팅");
						ChattingPanel.appendText(broker.getUserName(),broker.getData());
						break;
					case "300": // 게임 시작에 대한 응답 -> 게임 플레이화면으로 전환
						System.out.println("게임 스타트에 대한 응답");
						GameClientFrame.isWaitingScreen = false;
						GameClientFrame.isChanged = true;
						GameClientFrame.isPlayingScreen = true;
						isPlayingGame = true;
						break;
					case "550":
						if(broker.getObjType().equals("ITEM"))
							GamePlayPanel.removeItem(broker.getObjIdx());
						else if (broker.getObjType().equals("SWITCH_ON")) {
							GamePlayPanel.switchOn(broker.getObjIdx());
						}
						else if (broker.getObjType().equals("SWITCH_OFF")) {
							GamePlayPanel.switchOff(broker.getObjIdx());
						}
						break;
					case "600":
						if(broker.getData().equals("GameOver")) {
							GameClientFrame.gameScreenPane.setDieImage();
							isPlayingGame = false;
						}
						break;
					case "999":
						System.out.println("GameClientFrame.isWaitingScreen => "+GameClientFrame.isWaitingScreen);
						System.out.println("isPlayingGame = "+isPlayingGame);
						if(GameClientFrame.isWaitingScreen) { // 대기화면에서 상대방이 나간 경우
							System.out.println("999 받음 => "+broker.getData());
							if(broker.getData().split(" ")[0].equals("1")) {
								playerCharacter = 1;
								GameClientFrame.waitingPlayerNum = 1;
								String exitPlayerName = broker.getData().split(" ")[1];
								System.out.println(GameClientFrame.playerNames.size());
								GameClientFrame.gameScreenPane.removePlayerList(exitPlayerName);
								GameClientFrame.playerNames.remove(exitPlayerName);
							}
							GameClientFrame.isChanged = true; // 화면 변화가 필요함
							GameClientFrame.isGameScreen = true; // 게임 대기화면으로 변화
						}
						else if(isPlayingGame){ // 게임 중에 상대방이 나간 경우
							if(broker.getData().split(" ")[0].equals("1")) {
								playerCharacter = 1;
								GameClientFrame.waitingPlayerNum = 1;
								String exitPlayerName = broker.getData().split(" ")[1];
								System.out.println(GameClientFrame.playerNames.size());
								GameClientFrame.gameScreenPane.removePlayerList(exitPlayerName);
								GameClientFrame.playerNames.remove(exitPlayerName);
							}
							JOptionPane.showMessageDialog(null,"상대방이 게임방을 나갔습니다.");
							exitRoom();
							GameClientFrame.isChanged = true;
							GameClientFrame.isHomeScreen = true;
						}
						break;
					}
				}
				else if(mi != null) {
					if(GameClientFrame.gameScreenPane!=null)
						GameClientFrame.gameScreenPane.setMovingInfo(mi.getPosX(), mi.getPosY(), mi.getType());
				}
			} catch (IOException e) {
				try {
					System.out.println("e1");
					e.printStackTrace();
					ois.close();
					oos.close();
					socket.close();
					GameClientFrame.net = null;
					break;
				} catch (Exception ee) {
					System.out.println("e2");
					break;
				} // catch문 끝
			} // 바깥 catch문끝
		}
	}
	
	public void exitRoom() {
		Broker broker = new Broker(this.userName, roomId, "999");
		System.out.println(broker.getUserName()+", "+broker.getRoomId()+", "+broker.getCode()+", "+broker.getData());
		sendObject(broker);
		try {
			ois.close();
			oos.close();
			socket.close();
			GameClientFrame.net = null;
		} catch (Exception ee) {
			ee.printStackTrace();
		} // catch문 끝
		this.interrupt();
	}
	
	public static void sendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}