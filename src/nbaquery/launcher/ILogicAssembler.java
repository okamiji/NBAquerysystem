package nbaquery.launcher;

import nbaquery.data.TableHost;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;

public interface ILogicAssembler
{
	public void assemble(TableHost tableHost);
	
	public TeamService getTeamService();
	
	public PlayerService getPlayerService();
}