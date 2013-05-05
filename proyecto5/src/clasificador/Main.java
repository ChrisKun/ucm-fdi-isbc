package clasificador;

import java.awt.BorderLayout;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ontobridge.Ontologia;
import interfaz.Controlador;
import interfaz.TablaIndividuos;
import interfaz.VentanaPrincipal;

public class Main {

	public static final String rootPath = "proyecto5";
	public static final String gamesPath = rootPath + "\\fotos"; //"fotos"; 
	private static final String pathOntologia =  "file:proyecto5/src/ontologia/etiquetado.owl"; //"file:src/ontologia/etiquetado.owl";
	private static final String urlOntologia = "http://http://sentwittment.p.ht/";
	
	
	public static VentanaPrincipal vista;
	
	public static void main(String[] args){
		Ontologia modelo = new Ontologia(urlOntologia, pathOntologia);
		Controlador controlador = new Controlador(modelo);
		vista = new VentanaPrincipal(controlador);
		controlador.setVista(vista);
		controlador.getTiposDeContenido();
		
		vista.setVisible(true);
		
		/* SOLO PARA PRUEBAS */
		JFrame f = new JFrame();
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		f.setContentPane(p);
		TablaIndividuos tab = new TablaIndividuos(modelo);
		JTable t = new JTable(tab);
		/*
		 * Métodos interesantes a la hora de añadir el default table model a un JTable
		 */
		t.setCellSelectionEnabled(false);
		JScrollPane scrollPane = new JScrollPane(t);
		t.setFillsViewportHeight(true);
		p.add(BorderLayout.CENTER, scrollPane);
		f.setVisible(true);
		boolean para = false;
		Iterator<String> it = modelo.getOb().listAllClasses();
		while (it.hasNext() && !para){
			String clase = it.next();
			Iterator<String> l = modelo.getOb().listInstances(clase);
			while (l.hasNext() && !para)
			{
				String la = l.next();
				if (la != null)
					para = true;
				tab.actualizarTabla(la);
			}
		}
	}
}
