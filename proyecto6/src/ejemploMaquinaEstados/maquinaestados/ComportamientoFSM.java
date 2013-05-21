package ejemploMaquinaEstados.maquinaestados;

import ejemploMaquinaEstados.maquinaestados.fsm.MaquinaEstados;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

/**
 * Comportamiento de ejemplo que hace uso de una máquina de estados. En este caso empleamos una máquina de estados
 * con dos estados (Wander y GoToBall) y el comportamiento se responsabiliza de camabir entre estos dos estados
 * (si la pelota está lo suficientemente cerca voy hacia ella (estado "gotoball"), si no entonces deambulo (estado "wander")
 * 
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class ComportamientoFSM extends Behaviour{
	
	/**
	 * Máquina de estados que gobierna el comportamiento
	 */
	private MaquinaEstados maquina;

	@Override
	public void configure() {
		
	}

	@Override
	public void end() {
		maquina.end();
	}

	@Override
	public void onInit(RobotAPI robot) {
		// Creamos la máquina en el onInit porque necesita tener acceso a la robotAPI
		maquina = new MiMaquinaEstados(robot);
		if (!maquina.init()) {
			// Se ha producido un error en la inicialización de la máquina. Podemos mostrar mensaje de error
			;
		}
	}

	@Override
	public void onRelease(RobotAPI arg0) {
		// Do nothing		
	}

	@Override
	public int takeStep() {
		Vec2 ball= myRobotAPI.getBall();		
		if (ball.r<0.5) {
			// Si estoy lo suficientemente cerca cambio a GoToBall
			maquina.cambiarEstado("gotoball");			
		}
		else
			// En otro caso paso a Wander
			maquina.cambiarEstado("wander");
		// delego siempre en la máquina de estados para ejecutar el takeStep
		maquina.takeStep();
		return RobotAPI.ROBOT_OK;
	}

}
