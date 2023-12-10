//JavaObjServer.java ObjectStream 기반 채팅 Server

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class GameServerFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private int portNumber;
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;

    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓

    private ArrayList<GameRoom> gameRooms = new ArrayList<GameRoom>();

    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private static final String ALLOW_LOGIN_MSG = "ALLOW";
    private static final String DENY_LOGIN_MSG = "DENY";

    public GameServerFrame(int portNumber) {
        this.portNumber = portNumber;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(338, 440);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 10, 300, 298);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JLabel lblNewLabel = new JLabel("Port Number");
        lblNewLabel.setBounds(13, 318, 87, 26);
        contentPane.add(lblNewLabel);

        txtPortNumber = new JTextField();
        txtPortNumber.setEditable(false);
        txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPortNumber.setText(Integer.toString(portNumber));
        txtPortNumber.setBounds(112, 318, 199, 26);
        contentPane.add(txtPortNumber);
        txtPortNumber.setColumns(10);

        JButton btnServerStart = new JButton("Server Start");
        btnServerStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    socket = new ServerSocket(portNumber);
                } catch (NumberFormatException | IOException e1) {
                    e1.printStackTrace();
                }
                btnServerStart.setText("Chat Server Running..");
                btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
                txtPortNumber.setEnabled(false); // 더이상 포트번호 수정 못하게 막는다

                // 게임룸 생성
                GameRoom gameRoom_server1 = new GameRoom(socket,1);
                gameRoom_server1.start();

                GameRoom gameRoom_server2 = new GameRoom(socket,2);
                gameRoom_server2.start();

                GameRoom gameRoom_server3 = new GameRoom(socket,3);
                gameRoom_server3.start();
                gameRooms.add(gameRoom_server1);
                gameRooms.add(gameRoom_server2);
                gameRooms.add(gameRoom_server3);

                AcceptServer accept_server = new AcceptServer(socket);
                accept_server.start();
            }
        });
        btnServerStart.setBounds(12, 356, 300, 35);
        contentPane.add(btnServerStart);
        setVisible(true);
    }

    // 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
    class AcceptServer extends Thread {
        ServerSocket socket;
        private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터

        public AcceptServer(ServerSocket socket) {
            this.socket = socket;
        }

        public ServerSocket getSocket() {
            return socket;
        }

        public Vector getUserVec() {
            return UserVec;
        }

        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문
                try {
                    AppendText("Waiting new clients ..." + socket.getLocalPort());
                    client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
                    AppendText("새로운 참가자 from " + client_socket);
                    // User 당 하나씩 Thread 생성
                    UserService new_user = new UserService(client_socket, this);
                    UserVec.add(new_user); // 새로운 참가자 배열에 추가
                    new_user.start(); // 만든 객체의 스레드 실행
                    System.out.println("현재 참가자 수 " + UserVec.size());
                } catch (IOException e) {
                    AppendText("accept() error");
                }
            }
        }
    }

    public void AppendText(String str) {
        textArea.append(str+"\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

    // User 당 생성되는 Thread
    // Read One 에서 대기 -> Write All
    class UserService extends Thread {
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        private Socket client_socket;
        private ServerSocket socket;
        private Vector user_vc;
        public String UserName = "";
        public String UserStatus;

        private int roomId;

        public UserService(Socket client_socket, AcceptServer acceptServer) {
            // 매개변수로 넘어온 자료 저장
            this.client_socket = client_socket;
            this.socket = acceptServer.getSocket();
            this.user_vc = acceptServer.getUserVec();
            try {
                oos = new ObjectOutputStream(client_socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(client_socket.getInputStream());
            } catch (Exception e) {
                AppendText("userService error");
            }
        }

        public boolean Login(int roomId) {
            GameRoom gameRoom = gameRooms.get(roomId);
            if(gameRoom.enterRoom(UserName)) {
                gameRoom.getUserVec().add(this);
                AppendText("새로운 참가자 " + UserName + " 입장.");
                AppendText("[방"+gameRoom.getRoomId()+"] 참가자 "+gameRoom.getUserVec().size()+"/2");
                return true;
            }else {
                AppendText("새로운 참가자 " + UserName + " 입장 거절 당함.");
                return false;
            }
        }

        public int getPlayerNum(int roomId) { // gameRoom에 입장한 플레이어 수
            return gameRooms.get(roomId).getUserVec().size();
        }

        public String getUserNames(int roomId) {
            Vector userNames = gameRooms.get(roomId).getUserNameVec();
            return userNames.get(0)+"//"+userNames.get(1);
        }

        public void Logout() {
            System.out.println("LOGOUT 중");
            GameRoom gameRoom = gameRooms.get(roomId);
            gameRoom.getUserNameVec().remove(UserName);
            gameRoom.getUserVec().remove(this);
            System.out.println(gameRoom.getUserVec().size());
            String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
            user_vc.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
            Broker broker = new Broker("SERVER",roomId, "999", getPlayerNum(roomId) +" "+UserName);
            WriteOtherObject(roomId, broker); // 나를 제외한 다른 User들에게 전송
            AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + user_vc.size());

        }

        // 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
        public void WriteAllObject(int roomId, Object ob) {
            GameRoom gameRoom = gameRooms.get(roomId);
            Vector gameRoomUserVec = gameRoom.getUserVec();
            int userVecSize = gameRoomUserVec.size();
            for (int i = 0; i < userVecSize; i++) {
                UserService user = (UserService) gameRoomUserVec.elementAt(i);
                if (user.UserStatus == "O")
                    user.WriteOneObject(ob);
            }
        }

        public void WriteOtherObject(int roomId, Object ob) {
            GameRoom gameRoom = gameRooms.get(roomId);
            Vector gameRoomUserVec = gameRoom.getUserVec();
            int userVecSize = gameRoomUserVec.size();
            for (int i = 0; i < userVecSize; i++) {
                UserService user = (UserService) gameRoomUserVec.elementAt(i);
                if (user != this) {
                    user.WriteOneObject(ob);
                }
            }
        }

        public void WriteOneObject(Object ob) {
            try {
                oos.writeObject(ob);
            }
            catch (IOException e) {
                AppendText("oos.writeObject(ob) error");
                try {
                    ois.close();
                    oos.close();
                    client_socket.close();
                    client_socket = null;
                    ois = null;
                    oos = null;
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Logout();
            }
        }

        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문
                try {

                    Object obcm = null;
                    String msg = null;
                    Broker broker = null;
                    MovingInfo mi = null;

                    if (socket == null)
                        break;
                    try {
                        obcm = ois.readObject();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return;
                    }
                    if (obcm == null) {
                        break;
                    }
                    if (obcm instanceof Broker) {
                        broker = (Broker) obcm;
                    }
                    else if (obcm instanceof MovingInfo) {
                        mi = (MovingInfo)obcm;
                    }
                    else {
                        continue;
                    }
                    if(broker != null) {
                        if (broker.code.matches("100")) { // login
                            System.out.println(broker);
                            AppendText("로그인 요청 받음");
                            UserName = broker.UserName;
                            UserStatus = "O"; // Online 상태
                            if(Login(broker.roomId)) { // 로그인 성공시
                                this.roomId = broker.roomId;
                                int waitingPlayerNum = getPlayerNum(broker.roomId);
                                switch(waitingPlayerNum) {
                                    case 1:
                                        obcm = new Broker("SERVER", broker.roomId, "100", ALLOW_LOGIN_MSG+" "+waitingPlayerNum);
                                        WriteAllObject(broker.roomId, obcm);
                                        break;
                                    case 2:
                                        obcm = new Broker("SERVER", broker.roomId, "100", ALLOW_LOGIN_MSG+" "+waitingPlayerNum+" "+getUserNames(broker.roomId));
                                        WriteAllObject(broker.roomId, obcm);
                                        break;
                                }

                            }
                            else { // 로그인 실패 시
                                obcm = new Broker("SERVER", broker.roomId, "100", DENY_LOGIN_MSG);
                                oos.writeObject(obcm);
                                break; //스레드 종료
                            }
                        }
                        else if (broker.code.matches("200")) {
                            obcm = new Broker(broker.getUserName(), broker.roomId, "200", broker.getData());
                            WriteOtherObject(broker.roomId, obcm);
                        }
                        else if (broker.code.matches("300")) {
                            obcm = new Broker("[SERVER]", broker.roomId, "300","게임을 시작합니다.");
                            WriteAllObject(broker.roomId, obcm);
                        }
                        else if (broker.code.matches("550")) {
                            obcm = new Broker(broker.roomId, "550", broker.objIdx,broker.objType);
                            WriteOtherObject(broker.roomId, obcm);
                        }
                        else if (broker.code.matches("600")) {
                            System.out.println("broker.getData = "+broker.getData());
                            obcm = new Broker(broker.getUserName(), broker.roomId, "600", broker.getData());
                            WriteOtherObject(broker.roomId, obcm);
                        }
                        else if (broker.code.matches("999")) { // logout message 처리
                            System.out.println("999 받음");
                            Logout();
                            break;
                        }

                    } // end of broker != null..
                    else if (mi != null) {
                        WriteOtherObject(mi.getRoomId(),obcm);
                    }

                } catch (IOException e) {
                    AppendText("ois.readObject() error");
                    try {
                        ois.close();
                        oos.close();
                        client_socket.close();
                        Logout(); // 에러가난 현재 객체를 벡터에서 지운다
                        break;
                    } catch (Exception ee) {
                        break;
                    } // catch문 끝
                } // 바깥 catch문끝
            } // while
        } // run
    }
}
