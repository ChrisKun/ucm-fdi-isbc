package trainers;

import behaviours.Blocker;
import behaviours.GoToBall;
import behaviours.Goalkeeper;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;

public class Coach extends TeamManager {

	@Override
	public Behaviour[] createBehaviours() {
		return new Behaviour[] {
				new GoToBall(),
				new Blocker(),
				new Goalkeeper()
				};
	}

	@Override
	public Behaviour getDefaultBehaviour(int arg0) {
		return _behaviours[0];
	}

	@Override
	public int onConfigure() {
		return RobotAPI.ROBOT_OK;
	}

	@Override
	protected void onTakeStep() {
		// Si el jugador esta en su campo --> GoToBall
		RobotAPI robot = _players[0].getRobotAPI();
		_players[0].setBehaviour(_behaviours[2]);
		_players[1].setBehaviour(_behaviours[1]);
		
	}
}


