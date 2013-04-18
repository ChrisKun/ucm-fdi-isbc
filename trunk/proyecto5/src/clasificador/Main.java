package clasificador;

import ontobridge.Ontologia;
import interfaz.Controlador;
import interfaz.Interfaz;

public class Main {

	public static void main(String[] args){
		Ontologia modelo = new Ontologia("http://http://sentwittment.p.ht/","file:src/proyecto5/ontologia/etiquetado.owl");
		Controlador controlador = new Controlador(modelo);
		Interfaz vista = new Interfaz(controlador);
		controlador.setVista(vista);
		
		
		
		vista.setVisible(true);
	}
}
