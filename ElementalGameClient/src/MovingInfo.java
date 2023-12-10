import lombok.Getter;

import java.io.Serializable;

@Getter
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

	public State getType(){
		return type;
	}
}
