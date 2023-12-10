import javax.swing.JPanel;

import MapObject.Block;
import MapObject.Door;
import MapObject.Item;
import MapObject.Obstacle;
import MapObject.Switch;
import MapObject.SwitchBlock;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePlayPanel extends JPanel implements Runnable{
	   
	private final int WIDTH = 713;
	private final int HEIGHT = 544;
	
	private final int IMG_WIDTH = 48;
	private final int IMG_HEIGHT = 59;
	private final int RUN_IMG_WIDTH = 68;
	private final int RUN_IMG_HEIGHT = 61;
	
	int myWidth, myHeight;
	int opponentWidth, opponentHeight;
	
	private Map map;
	private int stageNum = 1;
	private ArrayList<Block> blocks = null;
	public static ArrayList<Item> items = null;
	public static ArrayList<Obstacle> obstacles = null;
	public static ArrayList<Door> doors = null;
	public static ArrayList<SwitchBlock> switchBlocks = null;
	public static ArrayList<Switch> switchBtns = null;
	
	public static boolean isOpponentSwitchOn = false;
	public static int opponentlastSwitchIdx = -1;
	
	public static boolean isMySwitchOn = false;
	public static int mylastOnSwitchIdx = -1;
	
	public KeyAdapter testKey;
	
   //게임 제어를 위한 변수
   int status;	//게임의 상태
   int cnt;	//루프 제어용 컨트롤 변수
   int delay;	//루프 딜레이. 1/1000초 단위.
   long pretime;	//루프 간격을 조절하기 위한 시간 체크값
   int keyBuff;	//키 버퍼값
   Thread mainWork;
   MoveThread moveThread = new MoveThread();
   boolean isMovingRight = false;
   boolean isMovingLeft = false;
   boolean isJumping = false;
   boolean isFalling = false;
   boolean isDie = false;
   boolean isOpponentDie = false;
   boolean isArrive = false;
   boolean isOpponentArrive = false;
   boolean isGameClear = false;
   
   
   int resetTotalDistance = 90;
   int jumpingTotalDistance = resetTotalDistance;
   int jumpingDist = 6;
   int fallingDist = 6;
   int xmovingDist = 6;

    // 이미지 파일 불러오는 툴킷.
   Toolkit imageTool = Toolkit.getDefaultToolkit();
   PlayerInfo myInfo = new PlayerInfo();
   PlayerInfo opponentInfo = new PlayerInfo();
   
   Image character;
   Image opponent;
   Rectangle characterRec;
   
   Image openFireDoorImg = imageTool.getImage("src/static/image/character/fire boy_enter room.gif");
   Image openWaterDoorImg = imageTool.getImage("src/static/image/character/water girl_enter room.gif");
   Image mapImg = imageTool.getImage("src/static/image/background/game_play_background.png");
  
   // 이미지 버퍼
   Image buffImg;
   Graphics buffG;
   
   // 플레이어 위치값.
    int myXpos = 100;
    int myYpos = 100;
    
    int opponentXpos = 100;
    int opponentYpos = 100;

    boolean roof = true;

   // =================================================================================================
    public void gameControll() {
		playerItemGetCheck();
		playerObstacleCheck();
		if(items.size()==0) {
			playerArriveCheck();
		}
	}
    
    public static void removeItem(int i) {
		// 상대방이 먹은 item 없애기
    	items.remove(i);
    }
    
    public static void switchOn(int i) {
    	if (switchBtns != null) {
    		switchBtns.get(i).setSwitchState(true);
    		isOpponentSwitchOn = true;
    		opponentlastSwitchIdx = i;
    	}
    }
    
    public static void switchOff(int i) { 
    	if (switchBtns != null) {
    		switchBtns.get(i).setSwitchState(false);
    		isOpponentSwitchOn = false;
    		opponentlastSwitchIdx = -1;
    	}
    }
    
    public void playerItemGetCheck() {
			for(int i = 0; i < items.size(); i++) {
				
				Item m = items.get(i);
				if (!(m.getState()%2 == GameClientFrame.userNum%2)) continue;
				
				if(((m.getX() <= myXpos
						&& myXpos <= m.getX() + m.getWidth())
						|| (m.getX() <= myXpos + myWidth
						&& myXpos + myWidth <= m.getX() + m.getWidth()))
						&& ((myYpos <= m.getY()
						&& m.getY() <= myYpos + myHeight)
						|| (myYpos <= m.getY()+m.getHeight()
						&& m.getY() + m.getHeight() <= myYpos + myHeight))) {
					items.remove(m);
					ListenNetwork.sendObject(new Broker(GameClientFrame.roomId,
							"550"
							, i, "ITEM"));
					break;
				}
			}
	}
    
    public void playerOnSwitchCheck() { 
			
    	for(int i = 0; i < switchBtns.size(); i++) {//Item m : items
				Switch s = switchBtns.get(i);
				
				if (isOpponentSwitchOn && opponentlastSwitchIdx == i)
					continue;
				
				if(characterRec.intersects(s.getRectangle())) {
					s.setSwitchState(true);
					System.out.println("스위치가 눌렸음");
					isMySwitchOn = true;
					mylastOnSwitchIdx = i;
					ListenNetwork.sendObject(new Broker(GameClientFrame.roomId,
							"550", i, "SWITCH_ON"));
					break;
				}
				else if (isMySwitchOn && mylastOnSwitchIdx == i){
					System.out.println("스위치가 떼짐");
					isMySwitchOn = false;
					mylastOnSwitchIdx = -1;
					ListenNetwork.sendObject(new Broker(GameClientFrame.roomId,
							"550", i, "SWITCH_OFF"));
					s.setSwitchState(false);
				}
				
			}
	}
    
    public void playerObstacleCheck() {
        for (Obstacle o : obstacles) {

            if (o.getState() % 2 == GameClientFrame.userNum % 2) {
                continue;
            }

            if (((o.getX() + 20 <= myXpos + myWidth
					&& myXpos + myWidth <= o.getX() + o.getWidth() - 20)
					|| (o.getX() + 20 <= myXpos
					&& myXpos <= o.getX() + o.getWidth() - 20))
					&& (o.getY() <= myYpos + myHeight + 15
					&& myYpos + myHeight + 10 <= o.getY() + o.getHeight())) {
                System.out.println("1  Game Over!!!");
                isDie = true;
                GameClientFrame.net.isPlayingGame = false;
                character = imageTool.getImage(myInfo.getDieImgPath());
                ListenNetwork.sendObject(new Broker(GameClientFrame.userName,
						GameClientFrame.roomId, "600", "GameOver")); // GameOver 전송
                break;
            } else {
                continue;
            }
        }
	}
    
    public void playerArriveCheck() {
        for (Door door : doors) {
            if (door.getState() % 2 == GameClientFrame.userNum % 2) { // user가 도착했는지 판단
                if (myInfo.getType() == State.FRONT
						&& (door.getX() <= myXpos
						&& myXpos <= door.getX() + door.getW())
                        && (door.getX() <= myXpos + myWidth
						&& myXpos + myWidth <= door.getX() + door.getW())
                        && (door.getY() <= myYpos
						&& myYpos <= door.getY() + door.getH())) {
                    isArrive = true;
                } else {
                    isArrive = false;
                }
            } else { // opponent가 도착했는지 판단

                if (opponentInfo.getType() == State.FRONT
						&& (door.getX() <= opponentXpos
						&& opponentXpos <= door.getX() + door.getW())
                        && (door.getX() <= opponentXpos + opponentWidth
						&& opponentXpos + opponentWidth <= door.getX() + door.getW())
                        && (door.getY() <= opponentYpos
						&& opponentYpos <= door.getY() + door.getH())) {
                    isOpponentArrive = true;
                } else {
                    isOpponentArrive = false;
                }
            }


        }
    }
   
    
   // =======================================================================================
    // 스레드 파트
    public void run(){
       try
       {
          while(roof){
             pretime=System.currentTimeMillis();
             gameControll();
             if(isDie || isOpponentDie) { // 죽은 경우, 클리어한 경우 스레드 종료
            	 Thread.sleep(1000);
            	 break;
             }
             else if(isGameClear) {
            	 if(stageNum==1) {
            		Thread.sleep(1100);
            		stageNum=2;
            		settingMap();
					initState();
					switch(GameClientFrame.userNum) {
						case 1:
							myXpos = 35;
							myYpos = 40;

							opponentXpos = 35;
							opponentYpos = 130;
							break;
						case 2:
							myXpos = 35;
							myYpos = 130;

							opponentXpos = 35;
							opponentYpos = 40;
							break;
					}
            	 } else {
            		 Thread.sleep(700);
            		 break;
            	 }
             }
             
             repaint(); //화면 리페인트
          
             if(System.currentTimeMillis()-pretime<delay)
				 Thread.sleep(delay-System.currentTimeMillis()+pretime);
             if(status!=4) cnt++;
          }
          
          // GameOverPanel로 이동
          if(isDie || isOpponentDie) {
        	  GameClientFrame.isChanged = true; // 화면 변화가 필요함
        	  GameClientFrame.isGameOverScreen = true; // 게임 대기화면으로 변화
          }
          else if(isGameClear) {
        	  moveThread.interrupt();
        	  GameClientFrame.net.isPlayingGame = false;
        	  GameClientFrame.isChanged = true; // 화면 변화가 필요함
        	  GameClientFrame.isGameClearScreen = true; // 게임 클리어화면으로 변화
          }
       }
       catch (Exception e)
       {
          e.printStackTrace();
       }
    }
    
    public void settingMap() { // 맵 세팅하기
    	String mapPath = switch (stageNum) {
            case 1 -> "src/resource/map1.txt";
            case 2 -> "src/resource/map2.txt";
            default -> "";
        };

        map = new Map(mapPath);
        blocks = map.getBlocks();
        items = map.getItems();
        obstacles = map.getObstacles();
        doors = map.getDoors();
        switchBlocks = map.getSwitchBlocks();
        switchBtns = map.getSwitchBtns();
        
        for(Switch switchBtn : switchBtns) {
      	  if (switchBlocks.size() != 0) {
      		  switchBtn.setManageBlock(switchBlocks.get(0));
      	  }
        }
    }
    
    public void initState() {
    	isMovingRight = false;
        isMovingLeft = false;
        isJumping = false;
        isFalling = false;
        isDie = false;
        isOpponentDie = false;
        isArrive = false;
        isOpponentArrive = false;
        isGameClear = false;
    }
    
    public void systeminit(){//프로그램 초기화
      status=0;
      cnt=0;
      delay=17;// 17/1000초 = 58 (프레임/초)
      keyBuff =0;
      
      initState();
      
      //맵 설정
      settingMap();
      
      for(Switch switchBtn:switchBtns) {
    	  if (!switchBlocks.isEmpty()) {
    		  switchBtn.setManageBlock(switchBlocks.get(0));
    	  }
      }
      
      
      // 캐릭터 설정
      switch(GameClientFrame.userNum) {
      case 1:
    	  myInfo.setUserNum(1);
    	  myInfo.setCharacterImgPath("src/static/image/character/fire_boy_character.png");
    	  myInfo.setRunRightImgPath("src/static/image/character/fire boy_run right.gif");
    	  myInfo.setRunLeftImgPath("src/static/image/character/fire boy_run left.gif");
    	  myXpos = 384;
    	  myYpos = 452;
    	  
    	  opponentInfo.setUserNum(2);
    	  opponentInfo.setCharacterImgPath("src/static/image/character/water_girl_character.png");
    	  opponentInfo.setRunRightImgPath("src/static/image/character/water girl_run right.gif");
    	  opponentInfo.setRunLeftImgPath("src/static/image/character/water girl_run left.gif");
    	  opponentXpos = 288;
    	  opponentYpos = 452;
    	  break;
      case 2:
    	  myInfo.setUserNum(2);
    	  myInfo.setCharacterImgPath("src/static/image/character/water_girl_character.png");
    	  myInfo.setRunRightImgPath("src/static/image/character/water girl_run right.gif");
    	  myInfo.setRunLeftImgPath("src/static/image/character/water girl_run left.gif");
    	  myXpos = 288;
    	  myYpos = 452;
    	  
    	  opponentInfo.setUserNum(1);
    	  opponentInfo.setCharacterImgPath("src/static/image/character/fire_boy_character.png");
    	  opponentInfo.setRunRightImgPath("src/static/image/character/fire boy_run right.gif");
    	  opponentInfo.setRunLeftImgPath("src/static/image/character/fire boy_run left.gif");
    	  opponentXpos = 384;
    	  opponentYpos = 452;
    	  break;
      }
      character = imageTool.getImage(myInfo.getCharacterImgPath());
	  opponent =  imageTool.getImage(opponentInfo.getCharacterImgPath());
      mainWork =new Thread(this);
      mainWork.start();
   }
    
  
    public GamePlayPanel(){
        // 프레임의 대한 설정.
        setSize(WIDTH,HEIGHT);
        
        // 프레임의 x버튼 누르면 프로세스 종료.
        systeminit();
        
        MovingInfo obcm = new MovingInfo("400",
				GameClientFrame.roomId,
				myXpos, myYpos,
				GameClientFrame.userNum,
				State.FRONT); // gameRoom 입장 시도
		ListenNetwork.sendObject(obcm);
		
        moveThread.start();
        
        testKey = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    	if(!isJumping && !isFalling)
                    		isJumping = true;
                        break;
                    case KeyEvent.VK_LEFT:
                    	 if(isMovingRight)
                    		 isMovingRight=false;
                    	 isMovingLeft = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                    	if(!isMovingRight)
                    		myXpos -= 10;
                    	 if(isMovingLeft)
                    		 isMovingLeft=false;
                    	 isMovingRight = true;       	 
                        break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            	switch(e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
            		 myXpos += 10;
                	 isMovingRight = false;
                	 break;
                case KeyEvent.VK_LEFT:
               	 	isMovingLeft = false;
               	 	break;
            	}
	        }
	    };
    }

    @Override
    public void paint(Graphics g) {
    	buffImg = createImage(getWidth(),getHeight()); // 버퍼링용 이미지 ( 도화지 )
        buffG = buffImg.getGraphics(); // 버퍼링용 이미지에 그래픽 객체를 얻어야 그릴 수 있다고 한다. ( 붓? )
        
        update(g);
    }


    @Override
    public void update(Graphics g) {
        buffG.clearRect(0, 0, WIDTH, HEIGHT); // 백지화
        buffG.drawImage(mapImg,0,0, this);
        
        for (Block block : blocks)
        	buffG.drawImage(block.getImg(),
					block.getX(),
					block.getY(),this);
        
        for (Item item : items)
        	buffG.drawImage(item.getImg(),
					item.getX(),
					item.getY(),this);
        
        for (Obstacle obstacle : obstacles)
        	buffG.drawImage(obstacle.getImg(),
					obstacle.getX(),
					obstacle.getY(),this);
        
        
        for (Door door : doors)
        	buffG.drawImage(door.getImg(),door.getX(),door.getY(),this);
        
        for (SwitchBlock switchBlock:switchBlocks) {
        	if (switchBlock.getIsVisible())
        		buffG.drawImage(switchBlock.getImg(),switchBlock.getX(),switchBlock.getY(),this);
        }
       
        for (Switch switchBtn: switchBtns) {        	
        	buffG.drawImage(switchBtn.getImg(),switchBtn.getX(),switchBtn.getY(),this);
        }
        
        
        if(!(isArrive&&isOpponentArrive)) { // 모두 도착 X
        	buffG.drawImage(character, myXpos, myYpos, this);

          if(!isOpponentDie) {
          	switch(opponentInfo.getType()) {
              case LEFT:
              	opponent = imageTool.getImage(opponentInfo.getRunLeftImgPath());
              	break;
              case RIGHT:
              	opponent = imageTool.getImage(opponentInfo.getRunRightImgPath());
              	break;
              case FRONT:
              	opponent = imageTool.getImage(opponentInfo.getCharacterImgPath());
              	break;
              }
          }
          
          buffG.drawImage(opponent, opponentXpos, opponentYpos, this);
        }
        else { // 모두 도착한 경우
        	for(Door door:doors) {
        		if(door.getState()%2==0) {
        			buffG.drawImage(openWaterDoorImg,door.getX(),door.getY(),this);
        		}
        		else {
        			buffG.drawImage(openFireDoorImg,door.getX(),door.getY(),this);
        		}
        	}
        	isGameClear = true;

        }
       
        g.drawImage(buffImg,0,0,this);
		repaint();
    }
    
    private class MoveThread extends Thread{
    	public void run() {
    		while(true) {
    			if(isDie||isOpponentDie) // 죽은 경우, 게임 클리어한 경우 스레드 종료
    				break;
    			setCharacterImg();
    			if(isJumping)
    				jumping();
    			else {
    				falling();
    			}
    			if(isMovingLeft||isMovingRight)
    				xMoving();
    			playerOnSwitchCheck();
    			MovingInfo obcm = new MovingInfo("400",
						GameClientFrame.roomId,
						myXpos, myYpos,
						GameClientFrame.userNum,
						myInfo.getType());
    			ListenNetwork.sendObject(obcm);
    			try {
					sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
    public void setCharacterImg() {
    	if(isMovingLeft) {
    		character = imageTool.getImage(myInfo.getRunLeftImgPath());
    		myInfo.setType(State.LEFT);
    	}
    	else if(isMovingRight) {
    		character = imageTool.getImage(myInfo.getRunRightImgPath());
    		myInfo.setType(State.RIGHT);
    	}
    	else {
    		character = imageTool.getImage(myInfo.getCharacterImgPath());
    		myInfo.setType(State.FRONT);
    	}
    }
    
    public void resetTotalJumpDist() {
    	jumpingTotalDistance = resetTotalDistance;
    }
    
    public void jumping() {
    	if(jumpingTotalDistance <= 0) {
    		isJumping = false;
    		isFalling = true;
    		resetTotalJumpDist();
    	}
    	else {
    		if(canMove(myXpos,myYpos-jumpingDist/2)) {
    			myYpos -= jumpingDist;
    			jumpingTotalDistance -= jumpingDist;
    		}
    		else {
    			resetTotalJumpDist();
    			isJumping = false;
    			isFalling = true;
    		}
    	}
    }
    
    public void falling() {
    	if(canMove(myXpos, myYpos + fallingDist)) {
    		myYpos += fallingDist;
    	}
    	else {
    		isFalling = false;
    	}
    }
    
    public void xMoving() {
    	if(isMovingRight) {
    		myXpos+=xmovingDist;
    		if(!canMove(myXpos, myYpos)) {
    			myXpos -= xmovingDist;
    		}
    	}
    	else if(isMovingLeft) {
    		myXpos-=xmovingDist;
    		if(!canMove(myXpos, myYpos)) {
    			myXpos += xmovingDist;
    		}
    	}
    }
    
    public void setMoving(int x, int y, State type) {
    	opponentXpos = x;
    	opponentYpos = y;
    	opponentInfo.setType(type);
    }
    
    public void setDieImage() {
    	opponent = imageTool.getImage(opponentInfo.getDieImgPath());
    	isOpponentDie = true;
    }
    
    public boolean canMove(int x, int y) {
		switch(myInfo.getType()) {
			case LEFT:
				if(GameClientFrame.userNum==1) {
					y += 10;
					myWidth = RUN_IMG_WIDTH-45;
					myHeight = RUN_IMG_HEIGHT-17;
					opponentWidth = RUN_IMG_WIDTH-45;
					opponentHeight = RUN_IMG_HEIGHT-17;
				}
				else {
					y += 10;
					myWidth = RUN_IMG_WIDTH-45;
					myHeight = RUN_IMG_HEIGHT-17;
					opponentWidth = RUN_IMG_WIDTH-45;
					opponentHeight = RUN_IMG_HEIGHT-17;
				}
				break;
			case RIGHT:
				if(GameClientFrame.userNum==1) {
					x += 23;
					y += 10;
					myWidth = RUN_IMG_WIDTH-45;
					myHeight = RUN_IMG_HEIGHT-17;
					opponentWidth = RUN_IMG_WIDTH-45;
					opponentHeight = RUN_IMG_HEIGHT-17;
				}
				else {
					x += 30;
					y += 10;
					myWidth = RUN_IMG_WIDTH-45;
					myHeight = RUN_IMG_HEIGHT-17;
					opponentWidth = RUN_IMG_WIDTH-45;
					opponentHeight = RUN_IMG_HEIGHT-17;
				}
				break;
			case FRONT:
				if(GameClientFrame.userNum==1) {
					x += 8;
					y += 8;
					myWidth = IMG_WIDTH-30;
					myHeight = IMG_HEIGHT-14;
					opponentWidth = IMG_WIDTH-30;
					opponentHeight = IMG_HEIGHT-14;
				}
				else {
					x += 10;
					y += 8;
					myWidth = IMG_WIDTH-30;
					myHeight = IMG_HEIGHT-14;
					opponentWidth = IMG_WIDTH-30;
					opponentHeight = IMG_HEIGHT-14;
				}
				break;
    	}
    	
    	characterRec = new Rectangle(x,y,myWidth,myHeight);
        for (Block block : blocks) {
            if (characterRec.intersects(block.getBlockRec())) {
                return false;
            }
        }
        for (Obstacle obstacle : obstacles) {
            if (characterRec.intersects(obstacle.getRectangle())) {
                return false;
            }
        }

        for (SwitchBlock switchBlock : switchBlocks) {
            if (characterRec.intersects(switchBlock.getRectangle()) && switchBlock.getIsVisible()) {
                return false;
            }
        }
		
		return true;
	}
}