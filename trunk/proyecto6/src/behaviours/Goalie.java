package behaviours;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Goalie extends Behaviour{

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRelease(RobotAPI r) {
		// TODO Auto-generated method stub
	}

	@Override
	public int takeStep() {
		// TODO Auto-generated method stub
		if (myRobotAPI.canKick()){
			myRobotAPI.kick();
		} else {
			myRobotAPI.setBehindBall(myRobotAPI.getBall());
		}
		
		return 0;
	}

}
