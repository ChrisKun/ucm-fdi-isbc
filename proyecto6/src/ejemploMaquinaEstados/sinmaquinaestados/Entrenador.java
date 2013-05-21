package ejemploMaquinaEstados.sinmaquinaestados;

import teams.ucmTeam.Behaviour;
import teams.ucmTeam.TeamManager;


public class Entrenador extends TeamManager{

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
		Behaviour[] behav  =new Behaviour[1];
		behav[0]=new Comportamiento();
		return behav;
	}
	

}
