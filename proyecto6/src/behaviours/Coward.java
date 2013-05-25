package behaviours;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;

public class Coward extends Behaviour{

	private int heading = 0;
	
	@Override
	public void configure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInit(RobotAPI arg0) {
		arg0.setDisplayString("cowardBehaviour");
	}

	@Override
	public void onRelease(RobotAPI arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int takeStep() {
		//¡¡Siempre en su parte del campo!! Excepto si tiene el balón que avanza
		Vec2 ball = myRobotAPI.getBall();
		
		if (ball.r < 0.5){
			// Si es el que mas cerca esta de la pelota ir a ella, evitando colisiones
			myRobotAPI.setBehindBall(myRobotAPI.getBall());
			if (myRobotAPI.blocked())
				myRobotAPI.avoidCollisions();
			//Una vez tiene la pelota, avanzar hasta la mitad del 2º campo
			myRobotAPI.setSpeed(5); //avanza			
			//si ya ha pasado la segunda mitad del campo
			if (estaEnCampoEnemigo()){
				//hacemos que pase al jugador más cercano
				myRobotAPI.passBall(myRobotAPI.getClosestMate());
				//FIXME estaria bien comprobar que se encuentra libre...
			}
			//comprueba quien es el jugador mas cercano y se la pasa
			
			//o chuta si no hay nadie cerca
		
		// Si esta en el campo enemigo, bloquea a los enemigos
			if (estaEnCampoEnemigo())
				myRobotAPI.blockClosest();
		}
		else{
		// esperar
			myRobotAPI.setSpeed(2);
			heading+=myRobotAPI.normalizeZero(45);
			myRobotAPI.setSteerHeading(heading);
		//myRobotAPI.setSteerHeading(2);
		}
		
		
		//Mirar que aliado tiene mas cerca y pasar el balon
		
		// (Al pasar, como no tiene el balón, volverá a su posición)
		//myRobotAPI.setBehindBall(myRobotAPI.getOpponentsGoal());
		//if (myRobotAPI.canKick())
		//myRobotAPI.kick();
		return myRobotAPI.ROBOT_OK;
	}
	
	// devuelve en que campo se encuentran los oponentes 
	private int campoOponente(){
		//1. Cogemos las coordenadas de la porteria enemiga
		Vec2 v = myRobotAPI.toFieldCoordinates(myRobotAPI.getOpponentsGoal());
		//2. Comprobamos si es positivo o negativo
		//La porteria enemiga está a la izquierda
		if (v.x < 0)
			return myRobotAPI.WEST_FIELD;
		//en otro caso, está a la derecha (positivos)
		return myRobotAPI.EAST_FIELD;
	}
	
	private boolean estaEnCampoEnemigo(){
		// comprobamos si nuestro robot está en ese campo
		if (myRobotAPI.getFieldSide() == campoOponente())
			return true;
		return false;
	}

}
