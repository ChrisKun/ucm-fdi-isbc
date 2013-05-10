package behaviours;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class GoToBall extends Behaviour{

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
		r.setDisplayString("goToBallBehaviour");
	}

	@Override
	public void onRelease(RobotAPI r) {
		// TODO Auto-generated method stub		
	}

	@Override
	public int takeStep() {
		myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
		if (myRobotAPI.canKick())
			myRobotAPI.kick();
		return myRobotAPI.ROBOT_OK;
	}

}
