package ejemploMaquinaEstados.sinmaquinaestados;

import teams.ucmTeam.UCMPlayer;
import teams.ucmTeam.TeamManager;

public class EquipoSinFSM extends UCMPlayer{

	@Override
	protected TeamManager getTeamManager() {
		TeamManager t=new Entrenador();
		return t;
	}

}
