package clasificador;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.sun.org.apache.bcel.internal.classfile.Field;

import ontobridge.Ontologia;
import interfaz.Controlador;
import interfaz.TablaIndividuos;
import interfaz.VentanaEtiquetar;
import interfaz.VentanaPrincipal;

public class Main {

	public static final String rootPath = "proyecto5";
	public static final String gamesPath = "fotos";// rootPath + "\\fotos"; //"fotos"; 
	private static final String pathOntologia = "file:src/ontologia/etiquetado.owl";// "file:proyecto5/src/ontologia/etiquetado.owl"; //"file:src/ontologia/etiquetado.owl";
	private static final String urlOntologia = "http://http://sentwittment.p.ht/";
	
	
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
