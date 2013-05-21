package ejemploMaquinaEstados.maquinaestados;

import ejemploMaquinaEstados.maquinaestados.fsm.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

/**
 * Ejemplo de máquina de estados con 2 estados: Wander y GoToBall. Inicialmente estaremos en el estado Wander.
 * El comportamiento que usa esta máquina de estados será el responsable de realiazr los cambios de estado.
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class MiMaquinaEstados extends MaquinaEstados {

	public MiMaquinaEstados(RobotAPI entidad) {
		super(entidad);
	}

	@Override
	protected void crearEstados() {
		this.crearNuevoEstado("wander", new Wander(this));
		this.crearNuevoEstado("gotoball", new GoToBall(this));
	}

	@Override
	protected String nombreEstadoInicial() {
		return "wander";
	}

}
