import lombok.Getter;

@Getter
public class PlayerInfo {
	int userNum;
	String characterImgPath;
	String runRightImgPath;
	String runLeftImgPath;
	String dieImgPath = "src/image/character/die.gif";
	@Getter
	State type;
	
	public PlayerInfo() {
		this.type = State.FRONT;
	}
	
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
   
	public void setCharacterImgPath(String characterImgPath) {
	   this.characterImgPath = characterImgPath;
	}
   
	public void setRunRightImgPath(String runRightImgPath) {
	   this.runRightImgPath = runRightImgPath;
	}
   
	public void setRunLeftImgPath(String runLeftImgPath) {
	   this.runLeftImgPath = runLeftImgPath;
	}
	
	public void setType(State type) {
		this.type = type;
	}
}
