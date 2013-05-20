package behaviours;

import java.util.Queue;

import messages.PositionMessage;

import EDU.gatech.cc.is.util.Vec2;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.Message;
import teams.ucmTeam.RobotAPI;

public class GoToPosition extends Behaviour{
	
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
		r.setDisplayString("ToGoal");
	}

	@Override
	public void onRelease(RobotAPI r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int takeStep() {
		Vec2 pos = myRobotAPI.getBall();
		myRobotAPI.setSteerHeading(pos.t);
		myRobotAPI.setSpeed(1.0);
		
		if (myRobotAPI.blocked()){
			myRobotAPI.avoidCollisions();
			myRobotAPI.setSpeed(1.0);
		}
		
		// Miro cómo de lejos estoy
		Vec2 dist = pos;
		dist.sub(myRobotAPI.getPosition());
		if (dist.r <0.2) {
			myRobotAPI.setSpeed(0.0);
			myRobotAPI.setDisplayString("Arrive");
		}

		return RobotAPI.ROBOT_OK;
	}

}
