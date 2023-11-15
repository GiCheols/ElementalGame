import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import MapObject.Block;
import MapObject.Door;
import MapObject.Item;
import MapObject.Obstacle;
import MapObject.Switch;
import MapObject.SwitchBlock;


public class Map {

	final static public int BLOCK_WIDTH_LENGTH = 23; //한 줄에 21개
	
	public ArrayList<Block> blocks =new ArrayList<>();
	public ArrayList<Item> items =new ArrayList<>();
	public ArrayList<Obstacle> obstacles = new ArrayList<>();
	public ArrayList<Door> doors = new ArrayList<>();
	public ArrayList<SwitchBlock> switchBlocks = new ArrayList<>();
	public ArrayList<Switch> switchBtns = new ArrayList<>();
	
	public String path;
	ArrayList<String> packet = new ArrayList<>();

	
	public Map(String path) {
		String[] map_arr  = setMapArr(path);
		setBlockObjects(map_arr);
		
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}
	
	public void setItmes(ArrayList<Item> items) {
		this.items = items;
	}
	
	public void setSwitchBlocks(ArrayList<SwitchBlock> switchBlocks) {
		this.switchBlocks = switchBlocks;
	}
	
	public void setSwitchBtns(ArrayList<Switch> switchBtns) {
		this.switchBtns = switchBtns;
	}

	//map.txt 파일을 읽어 내용을 담은 문자열 배열 생성
	//map_arr[0] = "1 1 1 1 1 1 ... 1 1"
	public String[] setMapArr(String path) {
		String[] map_arr = new String[packet.size()];
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(path)));
			String s;			
			while (( s = br.readLine()) != null) {
				String sc[] = s.split(" ");
				for(int i=0; i< sc.length;i++)
					packet.add(sc[i]);
			}
			map_arr = packet.toArray(map_arr);
			
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if( br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}
		return map_arr;	
	}
	
	//배열 내용으로 Block 객체 생성
	public void setBlockObjects(String [] map_arr) {
		
		for(int i=0; i< map_arr.length; i++) {
			int state = Integer.parseInt(map_arr[i]);
			if(state==1) { // 기본 블럭일 때
				int x = (i%BLOCK_WIDTH_LENGTH)* Block.BLOCK_WIDTH;
				int y = (i/BLOCK_WIDTH_LENGTH)* Block.BLOCK_HEIGHT;
				Block block = new Block(x,y);
				blocks.add(block);
			}
			if(state == 2 || state == 3) { // 아이템일 때
				int x = (i%BLOCK_WIDTH_LENGTH)* Block.BLOCK_WIDTH;
				int y = (i/BLOCK_WIDTH_LENGTH)* Block.BLOCK_HEIGHT;
				Item Item = new Item(x,y,state);
				items.add(Item);
			}
			if (state == 4 || state == 5 || state < 0) { // 장애물일 때
				int x = (i%BLOCK_WIDTH_LENGTH)* Block.BLOCK_WIDTH;
				int y = (i/BLOCK_WIDTH_LENGTH)* Block.BLOCK_HEIGHT;
				Obstacle obstacle = new Obstacle(x,y,state);
				obstacles.add(obstacle);
			}
			if (state == 6 || state == 7) { // 문일 때
				int x = (i%BLOCK_WIDTH_LENGTH)* Block.BLOCK_WIDTH;
				int y = (i/BLOCK_WIDTH_LENGTH)* Block.BLOCK_HEIGHT;
				Door door = new Door(x,y,state);
				doors.add(door);
			}
			if (state == 8) {
				int x = (i%BLOCK_WIDTH_LENGTH)* Block.BLOCK_WIDTH;
				int y = (i/BLOCK_WIDTH_LENGTH)* Block.BLOCK_HEIGHT;
				SwitchBlock switchBlock = new SwitchBlock(x,y);
				switchBlocks.add(switchBlock);
			}
			if (state == 9) {
				int heightDiff = Block.BLOCK_HEIGHT - Switch.SWITHCH_HEIGHT;
				int x = (i%BLOCK_WIDTH_LENGTH)* Block.BLOCK_WIDTH;
				int y = (i/BLOCK_WIDTH_LENGTH)* Block.BLOCK_HEIGHT;
				Switch switchBtn = new Switch(x,y+heightDiff);
				switchBtns.add(switchBtn);
			}
		}
	}
	
	public ArrayList getBlocks() {
		return blocks;
	}
	
	public ArrayList getItems() {
		return items;
	}
	
	public ArrayList getObstacles() {
		return obstacles;
	}
	
	public ArrayList getDoors() {
		return doors;
	}
	
	public ArrayList getSwitchBlocks() {
		return switchBlocks;
	}
	
	public ArrayList getSwitchBtns() {
		return switchBtns;
	}
}