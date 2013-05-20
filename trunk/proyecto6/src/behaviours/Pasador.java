package behaviours;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Pasador extends Behaviour{

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
		r.setDisplayString("Pasador");
	}

	@Override
	public void onRelease(RobotAPI r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int takeStep() {
		return RobotAPI.ROBOT_OK;
	}

}
