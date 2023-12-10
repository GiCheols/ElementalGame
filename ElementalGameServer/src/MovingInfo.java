import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class MovingInfo implements Serializable{
    private static final long serialVersionUID = 2L;
    private String code;
    private int roomId;
    private int posX;
    private int posY;
    private int characterNum;
    private State type;
}
