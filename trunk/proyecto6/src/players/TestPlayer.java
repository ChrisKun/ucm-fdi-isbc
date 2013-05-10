package players;

import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;
import trainers.Coach;

public class TestPlayer extends UCMPlayer{

	@Override
	protected TeamManager getTeamManager() {
		return new Coach();
	}

}
