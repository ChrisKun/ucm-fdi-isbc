package ejemploMaquinaEstados.maquinaestados.fsm;

import teams.ucmTeam.RobotAPI;

/**
 * Clases abstracta que define un estado de una máquina de estados finita. Sirve para manipular un RobotAPI.
 * Para implementar un estado solo será necesario implementar el comportamiento de los métodos abstractos:
 * <ul>
 * <li>onInit(RobotAPI): Se ejecuta justo antes de que el estado pase a ser el estado activo de la máquina de estados.</li>
 * <li>onTakeStep(RobotAPI): Se ejecuta en cada takeStep cuando este estado es el estado actual de la máquina de estados.</li>
 * <li>onEnd(RobotAPI): Se ejecuta cuando este estado deja de ser el estado actual de la máquina de estados.</li>
 * </ul>
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public abstract class Estado {
	
	/**
	 * Robot que se va a manipular
	 */
	private RobotAPI robot;
	
	/**
	 * Máquina de estados que contiene a este estado.
	 * Se guarda por si se implementa una FSM en la que los estados son responsables de realizar las transiciones entre estados 
	 * (usando el método getMaquinaEstados()("nombre")
	 */
	protected MaquinaEstados maquina;
	
	/**
	 * Crea el estado
	 * @param miMaquina Referencia a la máquina de estados contenedora de este estado 
	 */
	public Estado(MaquinaEstados miMaquina) {
		maquina = miMaquina;		
	}
	
	/**
	 * Da acceso a la máquina de estados desde las subclases
	 * @return Referencia a la máquina de estados contenedora de este estado
	 */
	protected MaquinaEstados getMaquinaEstados() {
		return maquina;
	}

	/**
	 * Inicializa el estado. Es llamado cuando el estado pasa a estar activo 
	 * @param robot RobotAPI que será controlada por el estado
	 */
	public final void init(RobotAPI robot) {
		this.robot = robot;
		onInit(this.robot);
	}

	/**
	 * TakeStep del estado. Manipula el robot
	 * @param robot RobotAPI controlada por el estado
	 */
	public final void takeStep() {
		this.onTakeStep(robot);
	}
	
	/**
	 * Llamado cuando el estado se desactiva en la máquina de estados.
	 * @param robot  RobotAPI controlada por el estado
	 */
	public final void end() {
		onEnd(this.robot);
	}
	
	//-------------- MÉTODOS A IMPLEMENTAR POR LAS SUBCLASES --------------//
	/**
	 * Método llamado desde el init()que deberá ser implementado por las subclases. 
	 * @param robot RobotAPI controlada por el estado
	 */
	protected abstract void onInit(RobotAPI robot);
	
	/**
	 * Método llamado desde el init()que deberá ser implementado por las subclases. 
	 * @param robot RobotAPI controlada por el estado
	 */
	protected abstract void onEnd(RobotAPI robot);

	/**
	 * Método llamado desde el init()que deberá ser implementado por las subclases. 
	 * @param robot RobotAPI controlada por el estado
	 */
	protected abstract void onTakeStep(RobotAPI robot);
	
	
	

}
