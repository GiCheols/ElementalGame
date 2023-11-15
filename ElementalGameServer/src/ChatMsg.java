// ChatMsg.java 채팅 메시지 ObjectStream 용.

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;

class ChatMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    public String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
    public String UserName;
    public int roomId;
    public String data;
    public ImageIcon img;
    public MouseEvent mouse_e;
    public int objIdx;
    public String objType;

    public ChatMsg(String UserName, int roomId, String code, String msg) {
        this.roomId = roomId;
        this.code = code;
        this.UserName = UserName;
        this.data = msg;
    }

    public ChatMsg(int roomId, String code, int objIdx,String objType) {
        this.roomId = roomId;
        this.code = code;
        this.objIdx = objIdx;
        this.objType = objType;

    }

    public String getUserName() {
        return UserName;
    }

    public String getData() {
        return data;
    }
}