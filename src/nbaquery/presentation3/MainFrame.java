package nbaquery.presentation3;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import nbaquery.data.Row;
import nbaquery.logic.match.NewMatchService;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation3.match.MatchPanel;
import nbaquery.presentation3.player.PlayerPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements DetailedInfoContainer
{
	public static final int width = 800;
	public static final int height = 720;
	
	NewPlayerService newPlayerService;
	NewTeamService newTeamService;
	NewMatchService newMatchService;
	
	public DisplayButton closeButton, minimizeButton;
	
	PlayerPanel playerPanel;
	MatchPanel matchPanel;
	
	public MainFrame(NewPlayerService newPlayerService,
			NewTeamService newTeamService, NewMatchService newMatchService)
	{
		super();
		
		//XXX setting up business logic layer services.
		this.newPlayerService = newPlayerService;
		this.newTeamService = newTeamService;
		this.newMatchService = newMatchService;
		
		//XXX setting up basic parameters of the frame.
		super.setLayout(null);
		super.setSize(width, height);
		super.setUndecorated(true);
		Dimension screenSize = super.getToolkit().getScreenSize();
		super.setLocation((screenSize.width - super.getWidth()) / 2, (screenSize.height - super.getHeight()) / 2);
		super.setAlwaysOnTop(true);
		
		//XXX adding extended state button to the screen.
		closeButton = new DisplayButton("img3/exit_idle.png", "img3/exit_over.png")
		{
			@Override
			protected void activate()
			{
				System.exit(0);
			}
		};
		closeButton.setLocation(super.getWidth() - closeButton.getWidth() - 3, 3);
		super.add(closeButton);
		
		minimizeButton = new DisplayButton("img3/minimize_idle.png", "img3/minimize_over.png")
		{
			@Override
			protected void activate()
			{
				MainFrame.this.setExtendedState(JFrame.ICONIFIED);
			}
		};
		minimizeButton.setLocation(super.getWidth() - closeButton.getWidth() - minimizeButton.getWidth() - 6, 3);
		super.add(minimizeButton);
		
		//XXX adding functional panels.
		this.playerPanel = new PlayerPanel(this, newPlayerService);
		super.add(playerPanel);
		
		this.matchPanel = new MatchPanel(this, this.newMatchService);
		this.add(matchPanel);
		
		//XXX start refresh thread.
		this.refresh.start();
	}
	
	Thread refresh = new Thread()
	{
		public void run()
		{
			while(true) try
			{
				if(MainFrame.this.isVisible()) MainFrame.this.repaint();
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				
			}
		}
	};
	
	MouseAdapter windowMove = new MouseAdapter()
	{
		int renderThreshold = 30;
		
		int x, y;
		boolean shouldMove = false;
		public void mousePressed(MouseEvent me)
		{
			if(!shouldMove) if(me.getY() < renderThreshold)
			{
				x = me.getXOnScreen();
				y = me.getYOnScreen();
				shouldMove = true;
			}
		}
		
		public void mouseReleased(MouseEvent me)
		{
			if(shouldMove)
			{
				MainFrame.this.setLocation(MainFrame.this.getX() + me.getXOnScreen() - x,
						MainFrame.this.getY() + me.getYOnScreen() - y);
				shouldMove = false;
			}
		}
	};
	{
		this.addMouseListener(windowMove);
	}

	@Override
	public void displayPlayerInfo(Row player, boolean stacked)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTeamInfo(Row team, boolean stacked)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayMatchInfo(Row match, boolean stacked)
	{
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void displayTeamInfo(String teamNameOrAbbr, boolean isAbbr,
			boolean stacked)
	{
		Row[] rows = newTeamService.searchInfoByName(teamNameOrAbbr, isAbbr).getRows();
		if(rows.length > 0) this.displayTeamInfo(rows[0], stacked);
	}
}
