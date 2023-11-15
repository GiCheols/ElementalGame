import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

//Server Message를 수신해서 화면에 표시
public class ListenNetwork extends Thread {
	String ip_addr = "127.0.0.1";
	//String ip_addr = "218.159.204.53";
//	String ip_addr = "192.168.35.87";
	int port_no;
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
	
//	public boolean isLogin = false;
	private String userName = "";
	
	public ListenNetwork(String userName,int port_no, int roomId) {
		this.userName = userName;
		this.port_no = port_no;
		System.out.println(ip_addr);
		System.out.println(port_no);
		this.roomId = roomId;
		try {
			socket = new Socket(ip_addr, port_no);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			ChatMsg obcm = new ChatMsg(userName, roomId, "100", ""); // gameRoom 입장 시도
			SendObject(obcm);
		} catch (NumberFormatException | IOException error) {
			// TODO Auto-generated catch block
			//error.printStackTrace();
			JOptionPane.showMessageDialog(null,"Server Can't connect");
		}
	}
	
	public void run() {
		
		while (true) {
			System.out.println("메시지 대기중...");
			try {
				Object obcm = null;
				String msg = null;
				ChatMsg cm = null;
				MovingInfo mi = null;
				try {
					obcm = ois.readObject();
//					System.out.println("obcm read success");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				if (obcm == null) {
//					System.out.println("obcm is null!");
					break;
				}
				if (obcm instanceof ChatMsg) {
					cm = (ChatMsg) obcm;
					msg = String.format("[%s] %s", cm.getUserName(), cm.getData());
					System.out.println(msg);
				}else if (obcm instanceof MovingInfo) {
//					System.out.println("obcm을 제대로 받음");
					mi = (MovingInfo) obcm;
				}
				else
					continue;
				
				if(cm != null) {
					switch (cm.getCode()) {
					case "100": // 서버 접속 결과 - allow,deny
						System.out.println(cm.getData());
						String loginResult = cm.getData().split(" ")[0];
						System.out.println("loginResult = "+loginResult);
						if(loginResult.equals(ALLOW_LOGIN_MSG)) {
							GameClientFrame.isWaitingScreen = true;
	//						isLogin = true;
							/* 화면 전환 */
							switch(cm.getData().split(" ")[1]) {
							case "1":
								if(playerCharacter == 0) // 처음 입장한 플레이어인 경우
									playerCharacter = 1;
								GameClientFrame.waitingPlayerNum = 1;
								GameClientFrame.playerNames.add(userName);
								break;
							case "2":
								String[] playerNames = cm.getData().split(" ")[2].split("//");
								if(playerCharacter == 0) {// 2번째로 입장한 플레이어인 경우
									playerCharacter = 2;
									for(int i=0;i<playerNames.length;i++)
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
	//						isLogin = false;
							JOptionPane.showMessageDialog(null,"해당 서버는 가득 찼습니다. 다른 서버를 선택해주세요.");
							return;
						}
						break;
					case "200":
						System.out.println("채팅을 받았다.");
						ChattingPanel.appendText(cm.getUserName(),cm.getData());
						break;
					case "300": // 게임 시작에 대한 응답 -> 게임 플레이화면으로 전환
						System.out.println("게임 스타트에 대한 응답을 받았다!!!!");
						GameClientFrame.isWaitingScreen = false;
						GameClientFrame.isChanged = true;
						GameClientFrame.isPlayingScreen = true;
						isPlayingGame = true;
						break;
					case "550":
						if(cm.getObjType().equals("ITEM"))
							GamePlayPanel.removeItem(cm.getObjIdx());
						else if (cm.getObjType().equals("SWITCH_ON")) {
							GamePlayPanel.switchOn(cm.getObjIdx());
						}
						else if (cm.getObjType().equals("SWITCH_OFF")) {
							GamePlayPanel.switchOff(cm.getObjIdx());
						}
						break;
					case "600":
						if(cm.getData().equals("GameOver")) {
							GameClientFrame.gameScreenPane.setDieImage();
							isPlayingGame = false;
						}
						break;
					case "999":
						System.out.println("GameClientFrame.isWaitingScreen => "+GameClientFrame.isWaitingScreen);
						System.out.println("isPlayingGame = "+isPlayingGame);
						if(GameClientFrame.isWaitingScreen) { // 대기화면에서 상대방이 나간 경우
							System.out.println("999 받음 => "+cm.getData());
							if(cm.getData().split(" ")[0].equals("1")) {
								playerCharacter = 1;
								GameClientFrame.waitingPlayerNum = 1;
								String exitPlayerName = cm.getData().split(" ")[1];
								System.out.println(GameClientFrame.playerNames.size());
								GameClientFrame.gameScreenPane.removePlayerList(exitPlayerName);
								GameClientFrame.playerNames.remove(exitPlayerName);
							}
							GameClientFrame.isChanged = true; // 화면 변화가 필요함
							GameClientFrame.isGameScreen = true; // 게임 대기화면으로 변화
						}
						else if(isPlayingGame){ // 게임 중에 상대방이 나간 경우
							if(cm.getData().split(" ")[0].equals("1")) {
								playerCharacter = 1;
								GameClientFrame.waitingPlayerNum = 1;
								String exitPlayerName = cm.getData().split(" ")[1];
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
//					System.out.println(mi);
//					System.out.println("받은 데이터: "+mi.getPosX()+ mi.getPosY()+ mi.getType());
					if(GameClientFrame.gameScreenPane!=null)
						GameClientFrame.gameScreenPane.setMovingInfo(mi.getPosX(), mi.getPosY(), mi.getType());
					//break;
				}
			} catch (IOException e) {
				//AppendText("ois.readObject() error");
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
		ChatMsg obcm = new ChatMsg(this.userName, roomId, "999");
		System.out.println(obcm.getUserName()+", "+obcm.getRoomId()+", "+obcm.getCode()+", "+obcm.getData());
		SendObject(obcm);
		try {
			ois.close();
			oos.close();
			socket.close();
			GameClientFrame.net = null;
		} catch (Exception ee) {
			System.out.println("exitRoom ee");
		} // catch문 끝
		this.interrupt();
	}
	
	public static void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
		}
	}
}