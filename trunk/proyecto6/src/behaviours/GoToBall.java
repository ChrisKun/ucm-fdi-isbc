package behaviours;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class GoToBall extends Behaviour{

	private Vec2 oldBall;
	
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
		//r.setDisplayString("goToBallBehaviour");
		oldBall = new Vec2(0,0);
	}

	@Override
	public void onRelease(RobotAPI r) {
		// TODO Auto-generated method stub		
	}

	@Override
	public int takeStep() {
		if (!myRobotAPI.blocked()){
			//myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
			//myRobotAPI.avoidCollisions();
			Vec2 newBall = myRobotAPI.getBall();
			newBall.sub(oldBall);
			newBall.add(myRobotAPI.getBall());
			myRobotAPI.setBehindBall(newBall);
			oldBall = myRobotAPI.getBall();
			myRobotAPI.setDisplayString("NonBlocked");
		} else {
			
			//myRobotAPI.setSteerHeading(myRobotAPI.getPosition().t +0.75);
			myRobotAPI.avoidCollisions();
			myRobotAPI.setSpeed(100);
			myRobotAPI.setDisplayString("Blocked");
		}
		
		if (myRobotAPI.canKick())
			myRobotAPI.kick();
		return RobotAPI.ROBOT_OK;
	}
}