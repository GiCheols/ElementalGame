import lombok.Getter;

import java.io.Serializable;
import javax.swing.ImageIcon;

@Getter
class Broker implements Serializable {
	private static final long serialVersionUID = 1L;
	private String UserName;
	private int roomId;
	private String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image
	private String data;
	private int objIdx;
	private String objType;
	public ImageIcon img;

	public Broker(String UserName, int roomId, String code, String msg) {
		this.UserName = UserName;
		this.roomId = roomId;
		this.code = code;
		this.data = msg;
	}
	
	@Override
	public String toString() {
		return "ChatMsg [UserName=" + UserName + ", roomId=" + roomId + ", code=" + code + ", data=" + data + "]";
	}

	public Broker(int roomId, String code, int itemIdx, String objType) {
		this.roomId = roomId;
		this.code = code;
		this.objIdx = itemIdx;
		this.objType = objType;
	}
	
	public Broker(String UserName, int roomId, String code) {
		this.UserName = UserName;
		this.roomId = roomId;
		this.code = code;
	}
}