package behaviours;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Goalkeeper extends Behaviour {

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
		r.setDisplayString("goalKeeper");	
	}

	@Override
	public void onRelease(RobotAPI arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public int takeStep() {
		Vec2 ball = myRobotAPI.getBall();
		Vec2 ourGoal = myRobotAPI.getOurGoal();
		//ball.sub(ourGoal);
		ourGoal.sub(ball);
		
		myRobotAPI.setSteerHeading(ourGoal.t);
		if (myRobotAPI.getPosition().y > 1||myRobotAPI.getPosition().y < -1){
			myRobotAPI.setSpeed(0);
		} else {
			myRobotAPI.setSpeed(10);
		}
		if (!myRobotAPI.behindEverybody()){
			//Opciones
			// · Que otro sea el portero
			// · Volver atras
		}
		return RobotAPI.ROBOT_OK;
	}

	
	
}
