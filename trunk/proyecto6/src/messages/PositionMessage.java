package messages;

import teams.ucmTeam.Message;
import EDU.gatech.cc.is.util.Vec2;

/**
 * Mensaje que devuelve una posicion 
 */
public class PositionMessage extends Message {
	private Vec2 position;

	public PositionMessage(Vec2 position) {
		super();
		this.position = position;
	}
	
	public Vec2 getPosicion() {
		return position;
	}
	
	public void setPosicion(Vec2 position) {
		this.position = position;
	}
}