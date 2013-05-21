package ejemploMaquinaEstados.maquinaestados;

import ejemploMaquinaEstados.maquinaestados.fsm.Estado;
import ejemploMaquinaEstados.maquinaestados.fsm.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

/**
 * Estado que hace que el robot deambule por el campo sin dirección fija (cada segundo modifica su dirección de movimiento)
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class Wander extends Estado {
	private long initTime;

	public Wander(MaquinaEstados fsm) {
		super(fsm);
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		robot.setDisplayString("");
	}

	@Override
	protected void onInit(RobotAPI robot) {
		initTime = robot.getTimeStamp();
		robot.setDisplayString("Wander");
		robot.setSteerHeading(Math.random()*2*3.141598);
		robot.setSpeed(1.0);
	}

	@Override
	protected void onTakeStep(RobotAPI robot) {
		if (robot.getTimeStamp()-initTime>1000) {
			// Si ha pasado un segundo cambio de dirección
			robot.setSteerHeading(Math.random()*2*3.141598);
			robot.setSpeed(1.0);
			initTime = robot.getTimeStamp();
		}
	}

}
