package ejemploMaquinaEstados.maquinaestados.fsm;

import java.util.HashMap;
import java.util.Map;

import teams.ucmTeam.RobotAPI;

/**
 * <p>Clase abstracta utilizada para crear una máquina de estados finita (FSM) utilizadas para controlar una RobotAPI.
 * Una FSM contiene un conjunto de estados accesibles por un nombre (String). En cada TakeStep se ejecuta el takeStep del
 * estado que se encuentra actualmente activo. La FSM se encarga de llamar a los métodos init y end de los estados cuando estos
 * son activados y desactivados, respectivamente.
 * 
 * <p>Para crear una máquina de estados usando esta clase hay que implementar los siguientes métodos:
 * <ul>
 * <li>crearEstados(): Crea los estados de la FSM y los añade a la misma usando el método crearNuevoEstado(nombre, estado)</li>
 * <li>nombreEstadoInicial(): Devuelve el nombre del estado inicial. Si no es correcto entonces fallará la inicialización de la máquina de estados</li>
 * </ul>
 * 
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public abstract class MaquinaEstados {
	
	/**
	 * Si no está a null indica cuál será el nuevo estado al que queremos cambiar 
	 */
	private Estado estadoSiguiente;
	
	/**
	 * Estado activo que gobierna la máquina de estados
	 */
	private Estado estadoActual;
	
	
	/**
	 * Contenedor de estados, accesibles por su nombre 
	 */
	private Map<String, Estado> estados;
	
	/**
	 * Robot controlado por esta máquina de estados
	 */
	private RobotAPI robot;
	
	/**
	 * Crea una nueva máquina de estados
	 * @param robot Robot que será gobrenado por esta máquina de estados
	 */
	public MaquinaEstados(RobotAPI robot) {
		estados = new HashMap<String, Estado>();
		this.robot = robot;		
	}
	
	/**
	 * Inicializa la máquina de estados. Puede fallar si no se establece correctamente el estado inicial.
	 * @return <code>true</code> si la inicialización de produjo correctamente, o <code>false</code> si
	 * el estado inicial no se ha podido activar
	 */
	public boolean init() {
		// Crear los estados
		crearEstados();
		if (estadoInicial(nombreEstadoInicial()))
			return true;
		else return false;
	}

	/**
	 * Cambia de estado si se ha solicitado un cambio de estado antes de ejecutar este takeStep y ejecuta el takeStep del estado actual
	 */
	public void takeStep() {
		if (estadoSiguiente!=null) {
			if (estadoActual!=estadoSiguiente) {
				estadoActual.end();
				estadoActual = estadoSiguiente;
				estadoActual.init(robot);
				estadoSiguiente = null;
			}
			estadoActual.takeStep();
		}
	}
	
	/**
	 * Solicita un cambio de estado en la máquina. El cambio será efectivo antes de realizar el siguiente takeStep.
	 * @param nombre Nombre del estado al que queremos cambiar
	 * @return <code>false</code> si no existe ningún estado con el nombre dado, o <code>true</code> e.o.c.
	 */
	public boolean cambiarEstado(String nombre){
		Estado nuevoEstado = estados.get(nombre);
		if (nuevoEstado!=null){
			estadoSiguiente = nuevoEstado;
			return true;
		}
		return false;
	}
	
	/**
	 * Finaliza la ejecución de la máquina de estados 
	 */
	public void end() {
		if (estadoActual!=null)
			estadoActual.end();
	}
	
	/**
	 * Establece el estado inicial de la máquina y lo activa. Falla si no existe el estado inicial
	 * @param nombreEstadoInicial Nombre del estado inicial
	 * @return <code>false</code> si el estado inicial no existe o <code>true</code> e.o.c.
	 */
	private boolean estadoInicial(String nombreEstadoInicial) {
		Estado nuevoEstado = estados.get(nombreEstadoInicial);
		if (nuevoEstado!=null){
			estadoActual = nuevoEstado;
			estadoActual.init(robot);
			return true;
		}
		else return false;
	}
	
	/**
	 * Método empleado por las subclases para incluir nuevos estados en esta máquina de estados.
	 * @param nombre Nombre del nuevos estado
	 * @param nuevoEstado Objeto que representa el nuevo estado a incluir en la máquina
	 */
	protected void crearNuevoEstado(String nombre, Estado nuevoEstado) {
		if (nombre!=null)
			estados.put(nombre, nuevoEstado);		
	}
	
	//-------------- MÉTODOS A IMPLEMENTAR POR LAS SUBCLASES --------------//
	
	/**
	 * Crea los estados de la máquina de estado. Este método es llamado desde el init() y insertará 
	 * los distintos estados de la máquina concreta usando el método crearNuevoEstado(nombre, estado) 
	 */
	protected abstract void crearEstados() ;

	/**
	 * Devuelve el nombre del estado inicial. Si el nombre que devuelve no es correcto entonces provocará
	 * un error en la inicialización de la máquina de estados.
	 * @return Nombre del estado inicial
	 */
	protected abstract String nombreEstadoInicial();

	
}
