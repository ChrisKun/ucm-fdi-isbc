package trainers;

import behaviours.Blocker;
import behaviours.GoToBall;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;

public class Coach extends TeamManager {

	@Override
	public Behaviour[] createBehaviours() {
		return new Behaviour[] {
				new GoToBall(), 
				new Blocker()
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
		RobotAPI robot = _players[2].getRobotAPI();
		if (robot.getPosition().x * robot.getFieldSide()>=0)
		_players[2].setBehaviour(_behaviours[0]);
		else
		// E.o.c. --> Blocker
		_players[2].setBehaviour(_behaviours[1]);
	}


}


