package behaviours;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Blocker extends Behaviour{

	@Override
	public void configure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInit(RobotAPI r) {
		r.setDisplayString("BlockerBehaviour");		
	}

	@Override
	public void onRelease(RobotAPI r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int takeStep() {
		myRobotAPI.blockGoalKeeper();
		return RobotAPI.ROBOT_OK;
	}


}
