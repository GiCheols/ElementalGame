import java.io.Serializable;

enum State {LEFT, RIGHT, FRONT}

public class MovingInfo implements Serializable{
    private static final long serialVersionUID = 2L;
    private String code;
    private int roomId;
    private int posX;
    private int posY;
    private int characterNum;
    private State type;

    public MovingInfo(String code, int roomId, int posX, int posY, int characterNum, State type) {
        this.code = code;
        this.roomId = roomId;
        this.posX = posX;
        this.posY = posY;
        this.characterNum = characterNum;
        this.type = type;
    }

    public String getCode() {
        return code;
    }


    public int getRoomId() {
        return roomId;
    }


    public int getPosX() {
        return posX;
    }


    public int getPosY() {
        return posY;
    }

    public int getCharacterNum() {
        return characterNum;
    }

    public State getType() {
        return type;
    }

}
