package ejemploMaquinaEstados.sinmaquinaestados;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

/**
 * Comportamiento que simula el uso de una máquina de estados (si estoy cerca de la pelota voy hacia ella y, si no, deambulo por el campo)
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class Comportamiento extends Behaviour{
	long initTime;
	@Override
	public void configure() {
		// TODO Auto-generated method stub

	}

	@Override
	public int takeStep() {
		Vec2 ball= myRobotAPI.getBall();
		
		if (ball.r<0.5) {
			this.myRobotAPI.setDisplayString("GoToBall");
			myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
			if(myRobotAPI.canKick())
			myRobotAPI.kick();
			return RobotAPI.ROBOT_OK;			
		}
		else{
			this.myRobotAPI.setDisplayString("Wander");
			if (this.myRobotAPI.getTimeStamp()-initTime>1000) {
				myRobotAPI.setSteerHeading(Math.random()*2*3.141598);
				myRobotAPI.setSpeed(1.0);
				initTime = this.myRobotAPI.getTimeStamp();
			}
		}
		return 0;
	}

	@Override
	public void onInit(RobotAPI r) {
		initTime = this.myRobotAPI.getTimeStamp();	
		myRobotAPI.setSteerHeading(Math.random()*2*3.141598);
		myRobotAPI.setSpeed(1.0);
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub

	}



	@Override
	public void onRelease(RobotAPI r) {
		// TODO Auto-generated method stub

	}

}
