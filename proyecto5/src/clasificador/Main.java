package clasificador;

import java.io.File;
import javax.swing.JOptionPane;
import ontobridge.Ontologia;
import interfaz.Controlador;
import interfaz.VentanaPrincipal;

public class Main {

	// Rutas accesibles desde toda la aplicacion
	public static final String rootPath = "proyecto5";
	public static final String gamesPath = "fotos";// rootPath + "\\fotos"; //"fotos"; 
	public static final String pathOntoLimpio = "src/ontologia/etiquetado.owl";
	public static final String pathOntologia = "file:src/ontologia/etiquetado.owl";// "file:proyecto5/src/ontologia/etiquetado.owl"; //"file:src/ontologia/etiquetado.owl";
	public static final String urlOntologia = "http://http://sentwittment.p.ht/";	
	
	public static VentanaPrincipal vista;
	
	public static void main(String[] args){
		Ontologia modelo = new Ontologia(urlOntologia, pathOntologia);
		Controlador controlador = new Controlador(modelo);
		vista = new VentanaPrincipal(controlador);
		controlador.setVista(vista);
		controlador.getTiposDeContenido();
		
		checkFiles();
		
		vista.setVisible(true);
		vista.setTitle("CLASIFICATOR 3000");						
	}

	private static void checkFiles() {
		File dir = new File(gamesPath);
		JOptionPane.showConfirmDialog(null, "Estamos comprobando /fotos");
	}
}
