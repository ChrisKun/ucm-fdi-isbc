package clasificador;

import ontobridge.Ontologia;
import interfaz.Controlador;
import interfaz.VentanaPrincipal;

public class Main {

	public static final String rootPath = "proyecto5";
	public static final String gamesPath = rootPath + "\\fotos";
	private static final String pathOntologia = "file:proyecto5/src/ontologia/etiquetado.owl";
	private static final String urlOntologia = "http://http://sentwittment.p.ht/";
	
	public static VentanaPrincipal vista;
	
	public static void main(String[] args){
		Ontologia modelo = new Ontologia(urlOntologia, pathOntologia);
		Controlador controlador = new Controlador(modelo);
		vista = new VentanaPrincipal(controlador);
		controlador.setVista(vista);
		controlador.getTiposDeContenido();
		
		vista.setVisible(true);
	}
}
