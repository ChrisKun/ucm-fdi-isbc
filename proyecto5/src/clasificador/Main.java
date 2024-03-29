package clasificador;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import controlador.Controlador;

import ontobridge.Ontologia;
import utilidades.Ficheros;
import interfaz.VentanaPrincipal;

public class Main {

	// Rutas accesibles desde toda la aplicacion
	public static final String rootPath = "proyecto5";
	public static final String gamesPath = "fotos";// rootPath + "\\fotos"; //"fotos"; 

	public static final String pathOntoSinFile = "src/ontologia/etiquetado.owl";
	public static final String pathOntologia = "file:src/ontologia/etiquetado.owl";// "file:proyecto5/src/ontologia/etiquetado.owl"; //"file:src/ontologia/etiquetado.owl";
	public static final String urlOntologia = "http://http://sentwittment.p.ht/";	
	
	public static VentanaPrincipal vista;
	
	public static void main(String[] args){
		
		File dir = new File("fotos");
		if (!dir.exists()){
			dir.mkdir();
			System.out.println(dir.getAbsolutePath());
		}
		
		Ontologia modelo = new Ontologia(urlOntologia, pathOntologia);
		Controlador controlador = new Controlador(modelo);
		vista = new VentanaPrincipal(controlador);
		controlador.setVista(vista);
		controlador.getTiposDeContenido();
		
		
		
		checkFiles(controlador);
		
		vista.setVisible(true);
		vista.setTitle("Videogame Tagger");					
	}

	/**
	 * Comprueba que todas las fotos de la carpeta \fotos estan en la ontologia y si no las a�ade 
	 * @param controlador
	 */
	public static void checkFiles(Controlador controlador) {
		File dir = new File(gamesPath);
		Ficheros f = new Ficheros();
		ArrayList<String> lista = f.ficheros(gamesPath);
		int numElems = controlador.addFotosModelo(lista);
		if (numElems > 0){
			JOptionPane.showMessageDialog(
					null, 
					"Se han a�adido "+numElems+" elementos",
					"Nuevas fotos!",
					JOptionPane.NO_OPTION
					);
			
		}
	}
	
}
