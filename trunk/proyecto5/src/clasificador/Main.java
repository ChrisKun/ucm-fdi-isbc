package clasificador;

import ontobridge.Ontologia;
import interfaz.Controlador;
import interfaz.VentanaPrincipal;

public class Main {

	public static final String pathGames = "proyecto5\\fotos";
	private static final String pathOntologia = "file:proyecto5/src/ontologia/etiquetado.owl";
	private static final String urlOntologia = "http://http://sentwittment.p.ht/";
	
	public static void main(String[] args){
		Ontologia modelo = new Ontologia(urlOntologia, pathOntologia);
		Controlador controlador = new Controlador(modelo);
		VentanaPrincipal vista = new VentanaPrincipal(controlador);
		controlador.setVista(vista);
		
		
		
		vista.setVisible(true);
	}
}
