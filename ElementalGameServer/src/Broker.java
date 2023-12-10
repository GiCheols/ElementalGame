import lombok.Getter;

import java.io.Serializable;

@Getter
class Broker implements Serializable {
    private static final long serialVersionUID = 1L;
    public String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image, 500: Mouse Event
    public String UserName;
    public int roomId;
    public String data;
    public int objIdx;
    public String objType;

    public Broker(String UserName, int roomId, String code, String msg) {
        this.roomId = roomId;
        this.code = code;
        this.UserName = UserName;
        this.data = msg;
    }

    public Broker(int roomId, String code, int objIdx, String objType) {
        this.roomId = roomId;
        this.code = code;
        this.objIdx = objIdx;
        this.objType = objType;

    }
}