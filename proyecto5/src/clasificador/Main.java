package clasificador;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import ontobridge.Ontologia;
import utilidades.Ficheros;
import interfaz.Controlador;
import interfaz.VentanaPrincipal;

public class Main {

	// Rutas accesibles desde toda la aplicacion
	public static final String rootPath = "proyecto5";
	public static final String gamesPath = "fotos";// rootPath + "\\fotos"; //"fotos"; 
	// XXX: AHORA MISMO APUNTAMOS A LA ONTOLOGIA VACIA, debemos trabajar sobre esta y llamarla etiquetado
	// Hay que mantener siempre una limpia para que Belen vea como hacemos la Maxi Carga de la muerte
	public static final String pathOntoSinFile = "src/ontologia/etiquetado_limpio.owl";
	public static final String pathOntologia = "file:src/ontologia/etiquetado_limpio.owl";// "file:proyecto5/src/ontologia/etiquetado.owl"; //"file:src/ontologia/etiquetado.owl";
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
		JOptionPane.showMessageDialog(
				null, 
				"Estamos comprobando /fotos", 
				"Comprobando las fotos", 
				JOptionPane.NO_OPTION
				);
		Ficheros f = new Ficheros();
		ArrayList<String> lista = f.ficheros(gamesPath);
		for (String s: lista){
			System.out.println(s);
		}
		
	}
	
}
