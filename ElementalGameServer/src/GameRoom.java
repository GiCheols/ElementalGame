import java.net.ServerSocket;
import java.util.Vector;
public class GameRoom extends Thread{
    @SuppressWarnings("unchecked")

    private ServerSocket socket;
    private int roomId; // 게임방 id
    private Vector UserVec; // UserService 벡터
    private Vector UserNameVec; // 참여자 이름 벡터

    // 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
    public GameRoom(ServerSocket socket, int roomId) {
        this.socket = socket;
        this.roomId = roomId;
        UserVec = new Vector();
        UserNameVec = new Vector();
        System.out.println("["+roomId+"] UserVec.size()="+UserVec.size());
    }

    public int getRoomId() {
        return roomId;
    }

    public ServerSocket getSocket() {
        return this.socket;
    }

    public Vector getUserVec() {
        return this.UserVec;
    }

    public Vector getUserNameVec() {
        return this.UserNameVec;
    }

    public boolean enterRoom(String userName) {
        System.out.println("enterRoom");
        if(UserVec.size()==2) {
            System.out.println("[방 "+roomId+"] 2명이 이미 참가한 방입니다.");
            return false;
        }
        else {
            System.out.println("[방 "+roomId+"] 방을 참가합니다.");
            UserNameVec.add(userName);
            return true;
        }
    }
}
