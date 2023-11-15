import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerListPanel extends JPanel{
	private Color backgroundColor = new Color(79,135,85);
	private JLabel playerListLabel = new JLabel("참여자 목록");
	private JLabel fireBoyLabel = new JLabel("FireBoy : ");
	private JLabel waterGirlLabel = new JLabel("WaterGirl : ");
	private JLabel player1NameLabel;
	private JLabel player2NameLabel;
	
	public PlayerListPanel() {
		setSize(245,86);
		setLayout(null);
		setBackground(backgroundColor);
		
		// 제목 label "참여자 목록"
		Font playerListTitleFont = new Font(playerListLabel.getFont().getName(),Font.BOLD,17);
		playerListLabel.setFont(playerListTitleFont);
		playerListLabel.setForeground(Color.WHITE);
		playerListLabel.setBounds(5,10,188,17);
		add(playerListLabel);
		
		fireBoyLabel.setForeground(Color.WHITE);
		fireBoyLabel.setBounds(7,40,58,15);
		add(fireBoyLabel);
		waterGirlLabel.setForeground(Color.WHITE);
		waterGirlLabel.setBounds(7,60,68,15);
		add(waterGirlLabel);
	}
	
	public void addPlayerLabel(int idx, String name) {
		switch(idx) {
		case 1:
			if(player1NameLabel==null) {
				player1NameLabel = new JLabel(name);
				player1NameLabel.setForeground(Color.WHITE);
				player1NameLabel.setBounds(65,40,161,15);
				add(player1NameLabel);
			}
			break;
		case 2:
			player2NameLabel = new JLabel(name);
			player2NameLabel.setForeground(Color.WHITE);
			player2NameLabel.setBounds(72,60,154,15);
			add(player2NameLabel);
			break;
		}
		
	}
	
	public void removePlayerLabel(String name) {
		System.out.println(name);
		if(GameClientFrame.playerNames.indexOf(name)==0) {
			remove(player1NameLabel);
			remove(player2NameLabel);
			player1NameLabel = null;
			player2NameLabel = null;
			
//			player1NameLabel = new JLabel(name);
//			player1NameLabel.setForeground(Color.WHITE);
//			player1NameLabel.setBounds(65,40,161,15);
//			add(player1NameLabel);
		}
		else if(GameClientFrame.playerNames.indexOf(name)==1) {
			remove(player2NameLabel);
			player2NameLabel = null;
		}
	}
}
