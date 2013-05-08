package interfaz;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class PanelCondiciones extends JPanel{

	public PanelCondiciones(Controlador controlador){
		super();
		this.setLayout(new GridLayout(3,1));
		this.add(new PanelCondicion(controlador, "Propiedades", controlador.getPropiedades()));
		this.add(new PanelCondicion(controlador, "Clases/Instancias", controlador.getIndividuosYClases()));
		this.add(new PanelCondicion(controlador, "Juego", controlador.getJuegos()));
		
	}

}
