package ejemploMaquinaEstados.maquinaestados;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.TeamManager;
import ejemploMaquinaEstados.maquinaestados.ComportamientoFSM;


/**
 * Este entrenador hace que todos los jugadores tengan un comportamiento basado en una máquina de estados (ComportamientoFSM)
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class Entrenador extends TeamManager {

	@Override
	public int onConfigure() {		
		return 0;
	}

	@Override
	public void onTakeStep() {

	}

	@Override
	public Behaviour getDefaultBehaviour(int id) {
		return this._behaviours[0];
	}

	@Override
	public Behaviour[] createBehaviours() {
		return new Behaviour[] {
				new ComportamientoFSM()
		};
	}


}
