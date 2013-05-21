package ejemploMaquinaEstados.maquinaestados;

import teams.ucmTeam.UCMPlayer;
import teams.ucmTeam.TeamManager;

/**
 * Equipo en el que todos los jugadores emplean un comportamiento gobernado por una máquina de estados
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class EquipoConFSM extends UCMPlayer{

	@Override
	protected TeamManager getTeamManager() {
		TeamManager t=new Entrenador();
		return t;
	}

}
