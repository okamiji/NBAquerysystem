package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.apache.batik.swing.svg.JSVGComponent;

import nbaquery.logic.match.MatchService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.addon.GoodLookingScrollBar;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.info.Match;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.panel.PanelSet;
import nbaquery.presentation2.util.Button;
import nbaquery.presentation2.util.CardType;

@SuppressWarnings("serial")
public class DetailedPanel extends JPanel{
	JPanel info_panel, data_panel;
	JPanel host_panel, guest_panel;
	JScrollPane data_scr;
	JScrollPane host_scr, guest_scr;
	
	JButton exit_button;
	Button info_button, data_button;
	Button team1_button, team2_button;
	
	Player player = null;
	Team team = null;
	Match match = null;
	
	String[] player_detailed_info = null;
	String[] team_detailed_info = null;
	String[][] match_detailed_info = null;
	
	int scr_height;
	
	JLabel direction_label;
	
	JLabel info_label, team_label;
	JLabel portrait_label;
	ImageIcon portrait, action;
	
	String team_string, match_string;
	
	public DetailedPanel(Player get_player){		
		player = get_player;
		initialize_player();
	}
	public DetailedPanel(Team get_team){	
		team = get_team;
		initialize_team();
	}
	public DetailedPanel(Match get_match){
		match = get_match;
		initialize_match();
	}
	
	private void initialize_player(){
		initialize();
		set_player_info();
	}
	private void initialize_team(){
		initialize();
		set_team_info();
	}	
	private void initialize_match(){
		initialize();
		set_match_info();
	}
	private void initialize(){
		this.setBackground(new Color(0,0,0,0.0f));
		this.setLayout(null);
		this.setSize(784, 545);
		this.setLocation(197, 93);//+70, +90
		
		exit_button = new Button("Img2/detail_exit.png", "Img2/detail_exit_c.png", this);
		exit_button.setBounds(567, 3, 20, 30);
		
		exit_button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.detailed_exit();
			}
		});
	}
	
	private void set_player_info(){
		info_button = new Button("Img2/primary_info.png", "Img2/primary_info_c.png", this);
		info_button.setBounds(174, 3, 195, 30);
		
		data_button = new Button("Img2/match_data.png", "Img2/match_data_c.png", this);
		data_button.setBounds(370, 3, 195, 30);
		
		direction_label = new JLabel();
		direction_label.setText("球员 < " + player.get_name());
		direction_label.setFont(new Font("微软雅黑",Font.BOLD, 12));
		direction_label.setForeground(new Color(191, 211, 200));
		direction_label.setBounds(0, 0, 160, 30);
		this.add(direction_label);
		
		info_panel = new JPanel();
		info_panel.setBackground(new Color(0, 0, 0, 0.0f));
		info_panel.setLayout(null);
		info_panel.setSize(590, 530);
		info_panel.setLocation(-5, -25);
		
		data_panel = new JPanel();
		data_panel.setBackground(new Color(0, 0, 0, 0.0f));
		data_panel.setLayout(null);
		//data_panel.setSize(575, 530);
		//data_panel.setLocation(-5, 5);
		//this.add(data_panel);+
		
		data_scr = new JScrollPane(data_panel, 
    		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		data_scr.setSize(590, 400);
		data_scr.setLocation(-5, 35);
		data_scr.setBorder(null);
		data_scr.setBackground(new Color(0, 0, 0, 0));
		data_scr.setOpaque(true);
		data_scr.setVerticalScrollBar(new GoodLookingScrollBar());
		this.add(data_scr);
		data_scr.setVisible(false);

		//data panel
		MatchService ms = PanelSet.ms;
		String[][] str = ms.searchForMatchsByPlayer(player.get_name());
		CardCreator creator = new CardCreator();
		ArrayList<Card> card_list = creator.create_needed_cards(CardType.MATCH_of_PLAYER, str, true);
		CardLocation location = new CardLocation(CardType.MATCH_of_PLAYER);
		scr_height = location.get_total_height(card_list.size());
		for(int i=0; i<card_list.size(); i++){
			Card card = card_list.get(i);
			data_panel.add(card);
			card.setLocation(card.width, card.height);
		}
		data_panel.repaint();
		

	    data_panel.setPreferredSize(new Dimension(data_scr.getWidth() - 50, scr_height));
		
		//info panel
		player_detailed_info = PanelSet.get_player_service().searchForOnePlayer(player.get_name());
		team_label = new JLabel();
		String player_team_name = "所属球队： " + player.get_team();
		team_label.setText(player_team_name);
		
		JLabel background_label=new JLabel(new ImageIcon("Img2/detail_background1.png"));
		info_panel.add(background_label, new Integer(Integer.MIN_VALUE));
		background_label.setBounds(-15, 5, 610, 545); 
		
		team_label.setSize(130, 100);
		team_label.setLocation(215, 185);
		team_label.setForeground(new Color(191, 211, 200));
		info_panel.add(team_label);
		
		info_label = new JLabel();

		info_label.setText(get_player_text());
		info_label.setForeground(new Color(191, 211, 200));
		info_label.setFont(info_label.getFont().deriveFont(Font.PLAIN));
		info_label.setBounds(215, 5, 490, 290);
		info_panel.add(info_label);
		
		portrait = new ImageIcon(player.get_portrait_path());
		portrait.setImage(portrait.getImage().getScaledInstance(154, 132, Image.SCALE_DEFAULT));
		
		portrait_label = new JLabel(portrait);
		portrait_label.setSize(154, 132);
		portrait_label.setLocation(10, 95);
		info_panel.add(portrait_label);
		
		//initialize with primary information panel
		add(info_panel);
		info_button.setIcon(new ImageIcon("Img2/primary_info_c.png"));
		
		team_label.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				String team_name = player.get_team();
				Team created_team = new Team(PanelSet.get_team_service().searchForOneTeam(team_name));
				PanelSet.create_detailed_panel(created_team);
				
			}
		});
		
		//button
		info_button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				info_button.setIcon(new ImageIcon("Img2/primary_info_c.png"));
				data_button.setIcon(new ImageIcon("Img2/match_data.png"));
				
				add(info_panel);
				info_panel.setVisible(true);
				data_scr.setVisible(false);
				remove(data_scr);
				validate();
				repaint();
			}
		});
		data_button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				data_button.setIcon(new ImageIcon("Img2/match_data_c.png"));
				info_button.setIcon(new ImageIcon("Img2/primary_info.png"));
				
				info_panel.setVisible(false);
				remove(info_panel);
				add(data_scr);
				data_scr.setVisible(true);
				validate();
				repaint();
			}
		});
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void set_team_info(){
		direction_label = new JLabel();
		direction_label.setText("球队 < " + team.get_name());
		direction_label.setFont(new Font("微软雅黑",Font.BOLD, 12));
		direction_label.setForeground(new Color(191, 211, 200));
		direction_label.setBounds(0, 0, 160, 30);
		this.add(direction_label);
		
		info_panel = new JPanel();
		info_panel.setBackground(new Color(0, 0, 0, 0.0f));
		info_panel.setLayout(null);
		info_panel.setSize(590, 530);
		info_panel.setLocation(-5, -25);
		this.add(info_panel);
		
		//info panel
		team_detailed_info = PanelSet.get_team_service().searchForOneTeam(team.get_name());

		JLabel background_label=new JLabel(new ImageIcon("Img2/detail_background2.png"));
		info_panel.add(background_label, new Integer(Integer.MIN_VALUE));
		background_label.setBounds(-15, 5, 610, 545); 
		
		info_label = new JLabel();

		info_label.setText(get_team_text());
		info_label.setForeground(new Color(191, 211, 200));
		info_label.setFont(info_label.getFont().deriveFont(Font.PLAIN));
		info_label.setBounds(215, 16, 490, 490);
		info_panel.add(info_label);
		
		JSVGComponent svgComponent = new JSVGComponent(null, false, false);
		String path = team.get_team_info()[29];
		if(path != null){
			File f = new File(path);
			try {
	            svgComponent.loadSVGDocument(f.toURL().toString());
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
			svgComponent.setBounds(18, 55, 130, 130);
			this.add(svgComponent);
			this.repaint();
		}
		
		JLabel team_name_label = new JLabel();
		team_name_label.setForeground(new Color(191, 211, 200));
		team_name_label.setText(team.get_name());
		team_name_label.setHorizontalAlignment(JLabel.CENTER);
		team_name_label.setBounds(20, 205, 130, 50);
		info_panel.add(team_name_label);
		
		JLabel primary_info = new JLabel();
		primary_info.setText(get_team_primary_text());
		primary_info.setForeground(new Color(191, 211, 200));
		primary_info.setFont(info_label.getFont().deriveFont(Font.PLAIN));
		primary_info.setBounds(32, 265, 120, 180);
		info_panel.add(primary_info);
		
	}
	private void set_match_info(){
		team1_button = new Button("Img2/host.png", "Img2/host_c.png", this);
		team1_button.setBounds(174, 3, 195, 30);
		
		team2_button = new Button("Img2/guest.png", "Img2/guest_c.png", this);
		team2_button.setBounds(370, 3, 195, 30);
		
		
		direction_label = new JLabel();
		direction_label.setText("比赛 < " + match.get_season() + " " + match.get_team()[0] + " : " + match.get_team()[1]);
		direction_label.setFont(new Font("微软雅黑",Font.BOLD, 12));
		direction_label.setForeground(new Color(191, 211, 200));
		direction_label.setBounds(0, 0, 160, 30);
		this.add(direction_label);
		
		host_panel = new JPanel();
		host_panel.setBackground(new Color(0, 0, 0, 0.0f));
		host_panel.setLayout(null);
		host_panel.setSize(590, 530);
		host_panel.setLocation(-5, -25);
		
		host_scr = new JScrollPane(data_panel, 
	    		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
	    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		host_scr.setSize(590, 400);
		host_scr.setLocation(-5, 35);
		host_scr.setBorder(null);
		host_scr.setBackground(new Color(0, 0, 0, 0));
		host_scr.setOpaque(true);
		host_scr.setVerticalScrollBar(new GoodLookingScrollBar());
		this.add(host_scr);
		host_scr.setVisible(false);
		
		guest_panel = new JPanel();
		guest_panel.setBackground(new Color(0, 0, 0, 0.0f));
		guest_panel.setLayout(null);
		guest_panel.setSize(590, 530);
		guest_panel.setLocation(-5, -25);
		
		guest_scr = new JScrollPane(data_panel, 
    		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		guest_scr.setSize(590, 400);
		guest_scr.setLocation(-5, 35);
		guest_scr.setBorder(null);
		guest_scr.setBackground(new Color(0, 0, 0, 0));
		guest_scr.setOpaque(true);
		guest_scr.setVerticalScrollBar(new GoodLookingScrollBar());
		this.add(guest_scr);
		guest_scr.setVisible(false);


		MatchService ms = PanelSet.ms;
		
		int match_id = Integer.parseInt(match.get_id());
		System.out.println(match_id);
		String[][] str = ms.searchForOneMatchById(match_id);
		String[][] team1_players;
		String[][] team2_players;
		
	/*	for(int i=0; i<str.length; i++){
			if()
		}8*/
		
		/*CardCreator creator = new CardCreator();
		ArrayList<Card> card_list = creator.create_needed_cards(CardType.MATCH_of_PLAYER, str, true);
		CardLocation location = new CardLocation(CardType.MATCH_of_PLAYER);
		scr_height = location.get_total_height(card_list.size());
		for(int i=0; i<card_list.size(); i++){
			Card card = card_list.get(i);
			data_panel.add(card);
			card.setLocation(card.width, card.height);
		}
		data_panel.repaint();
		

	    data_panel.setPreferredSize(new Dimension(data_scr.getWidth() - 50, scr_height));
		*/
		//button
		team1_button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				team1_button.setIcon(new ImageIcon("Img2/host_c.png"));
				team2_button.setIcon(new ImageIcon("Img2/guest.png"));
				
				add(host_panel);
				host_panel.setVisible(true);
				guest_scr.setVisible(false);
				remove(guest_scr);
				remove(guest_panel);
				validate();
				repaint();
			}
		});
		team2_button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				team1_button.setIcon(new ImageIcon("Img2/host.png"));
				team2_button.setIcon(new ImageIcon("Img2/guest_c.png"));
				
				add(guest_panel);
				guest_panel.setVisible(true);
				host_scr.setVisible(false);
				remove(host_scr);
				remove(host_panel);
				validate();
				repaint();
			}
		});
	}

	private String get_player_text(){
		String player_string = "<html>";
		player_string += "<b>球员姓名：</b> " + player_detailed_info[0];
		player_string += "<br/>";
		player_string += "<b>球衣编号： </b>" + player_detailed_info[1];
		player_string += "<br/>";
		player_string += "<b>球员位置：</b> " + player_detailed_info[2];
		player_string += "<br/>";
		player_string += "<b>球员身高： </b>" + player_detailed_info[3];
		player_string += "<br/>";
		player_string += "<b>球员体重： </b>" + player_detailed_info[4];
		player_string += "<br/>";
		player_string += "<b>出生日期： </b>" + player_detailed_info[5];
		player_string += "<br/>";
		player_string += "<b>年龄： </b>" + player_detailed_info[6];
		player_string += "<br/>";
		player_string += "<b>球龄：</b> " + player_detailed_info[7];
		player_string += "<br/>";
		player_string += "<b>毕业学校： </b>" + player_detailed_info[8];
		player_string += "</html>";
		return player_string;
	}
	private String get_team_text(){
		System.out.println(" ");
		String team_string = "<html>";
		team_string += "<b>赛季：</b> " + team_detailed_info[0];
		team_string += "<br/>";
		team_string += "<b>比赛场数：</b> " + team_detailed_info[2];
		team_string += "<br/>";
		team_string += "<b>投篮命中数： </b>" + team_detailed_info[3];
		team_string += "<br/>";
		team_string += "<b>投篮出手次数： </b>" + team_detailed_info[4];
		team_string += "<br/>";
		team_string += "<b>三分命中数： </b>" + team_detailed_info[5] + " ";
		team_string += "<b>三分出手数： </b>" + team_detailed_info[6];
		team_string += "<br/>";
		team_string += "<b>罚球命中数： </b>" + team_detailed_info[7] + " ";
		team_string += "<b>罚球出手数： </b>" + team_detailed_info[8];
		team_string += "<br/>";
		team_string += "<b>进攻篮板数：</b> " + team_detailed_info[9] + " ";
		team_string += "<b>防守篮板数： </b>" + team_detailed_info[10] + " ";
		team_string += "<b>篮板数： </b>" + team_detailed_info[11];
		team_string += "<br/>";
		team_string += "<b>助攻数： </b>" + team_detailed_info[12];
		team_string += "<br/>";
		team_string += "<b>抢断数： </b>" + team_detailed_info[13];
		team_string += "<br/>";
		team_string += "<b>盖帽数： </b>" + team_detailed_info[14];
		team_string += "<br/>";
		team_string += "<b>失误数： </b>" + team_detailed_info[15];
		team_string += "<br/>";
		team_string += "<b>犯规数： </b>" + team_detailed_info[16];
		team_string += "<br/>";
		team_string += "<b>比赛得分： </b>" + team_detailed_info[17];
		team_string += "<br/>";
		team_string += "<b>投篮命中率： </b>" + team_detailed_info[18];
		team_string += "<br/>";
		team_string += "<b>三分命中率： </b>" + team_detailed_info[19];
		team_string += "<br/>";
		team_string += "<b>罚球命中率： </b>" + team_detailed_info[20];
		team_string += "<br/>";
		team_string += "<b>胜率： </b>" + team_detailed_info[21];
		team_string += "<br/>";
		team_string += "<b>进攻回合： </b>" + team_detailed_info[22];
		team_string += "<br/>";
		team_string += "<b>进攻效率： </b>" + team_detailed_info[23] + " ";
		team_string += "<b>防守效率： </b>" + team_detailed_info[24];
		team_string += "<br/>";
		team_string += "<b>进攻篮板效率： </b>" + team_detailed_info[25] + " ";
		team_string += "<b>防守篮板效率： </b>" + team_detailed_info[26];
		team_string += "<br/>";
		team_string += "<b>抢断效率： </b>" + team_detailed_info[27];
		team_string += "<br/>";
		team_string += "<b>助攻效率： </b>" + team_detailed_info[28];
		team_string += "</html>";
		return team_string;
	}
	private String get_team_primary_text(){
		String text = "<html>";
		text += "<b>队伍所在地： </b>" + team_detailed_info[31];
		text += "<br/>";
		text += "<b>赛区： </b>" + team_detailed_info[32];
		text += "<br/>";
		text += "<b>分区： </b>" + team_detailed_info[33];
		text += "<br/>";
		text += "<b>主场： </b>" + team_detailed_info[34];
		text += "<br/>";
		text += "<b>创建时间： </b>" + team_detailed_info[35];
		text += "</html>";
		return text;
	}
	private String get_match_text(){
		String match_string = "<html>";
		match_string += "<b>球员姓名：</b> " + player_detailed_info[0];
		match_string += "<br/>";
		match_string += "<b>球衣编号： </b>" + player_detailed_info[1];
		match_string += "<br/>";
		match_string += "<b>球员位置：</b> " + player_detailed_info[2];
		match_string += "<br/>";
		match_string += "<b>球员身高： </b>" + player_detailed_info[3];
		match_string += "<br/>";
		match_string += "<b>球员体重： </b>" + player_detailed_info[4];
		match_string += "<br/>";
		match_string += "<b>出生日期： </b>" + player_detailed_info[5];
		match_string += "<br/>";
		match_string += "<b>年龄： </b>" + player_detailed_info[6];
		match_string += "<br/>";
		match_string += "<b>球龄：</b> " + player_detailed_info[7];
		match_string += "<br/>";
		match_string += "<b>毕业学校： </b>" + player_detailed_info[8];
		match_string += "</html>";
		return match_string;
	}
}

