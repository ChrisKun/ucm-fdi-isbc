package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import controlador.Controlador;


import clasificador.Main;

public class VentanaPrincipal extends JFrame implements ActionListener{

	private Controlador controlador;

	private JMenuBar menuBar;
	private JMenu menuArchivo;
	private JMenuItem itemNuevo;
	private JMenuItem itemGuardarOnt;
	private JMenuItem itemCargarOnt;
	private JMenuItem itemActualizarOnt;
	
	private JTabbedPane panelIntercambiable;
	
	private PanelConsulta panelConsulta;
	private PanelExploracion panelExploracion;
	private PanelEtiquetar panelEdicion;
	
		
	public final static int W = 1280;
	public final static int H = 720;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentanaPrincipal(Controlador controlador){
		this.controlador = controlador;
		this.setLocation(200, 150);
		this.setMinimumSize(new Dimension(W,H));
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                		null, 
                		"Estas seguro de que te quieres ir? " +
                		"\nTodos los cambios no guardados se perderan", 
                		"Cerrar el programa", 
                		JOptionPane.YES_NO_OPTION, 
                		JOptionPane.QUESTION_MESSAGE, 
                		null, null, null
                		);
                if (confirm == 0) {
                   System.exit(0);
                }
            }
		});
		initMenuBar();
		this.setJMenuBar(menuBar);
		
		this.setLayout(new BorderLayout());
		
		panelIntercambiable = new JTabbedPane();

		panelConsulta = new PanelConsulta(controlador);
		panelIntercambiable.add("Consulta",panelConsulta);
		
		panelExploracion = new PanelExploracion(controlador);
		panelIntercambiable.add("Explorador",panelExploracion);
	
		
		panelEdicion = new PanelEtiquetar(controlador);
		panelIntercambiable.add("Edicion",panelEdicion);
		
		this.add(panelIntercambiable, BorderLayout.CENTER);
		
		controlador.getIndividuosValidosRellenarPropiedad("amigo_de");
		
	}
	
	private void initMenuBar(){
		menuBar = new JMenuBar();
		menuArchivo = new JMenu("Archivo");
		menuBar.add(menuArchivo);
		itemNuevo = new JMenuItem("Nuevo");
		itemNuevo.addActionListener(this);
		menuArchivo.add(itemNuevo);
		itemGuardarOnt = new JMenuItem("Salvar Ontologia");
		itemGuardarOnt.addActionListener(this);
		menuArchivo.add(itemGuardarOnt);
		itemCargarOnt = new JMenuItem("Cargar Ontologia");
		itemCargarOnt.addActionListener(this);
		menuArchivo.add(itemCargarOnt);
		itemActualizarOnt = new JMenuItem("Actualizar Ontologia");
		itemActualizarOnt.addActionListener(this);
		menuArchivo.add(itemActualizarOnt);
	}

	public void activaPanelExplorador(String pathFile){
		panelExploracion.setPathExplorador(pathFile);
		panelExploracion.cambiarPanel(PanelExploracion.panelExplorador);
		this.validate();
	}

	public void activaPanelExplorador(ArrayList<String> res) {
		panelExploracion.setPathExplorador(res);
		panelExploracion.cambiarPanel(PanelExploracion.panelExplorador);
		panelIntercambiable.setSelectedIndex(1);
		this.validate();
	}
	
	public void activaPanelFoto(String pathFile){
		panelExploracion.setPathFoto(pathFile);
		panelExploracion.cambiarPanel(PanelExploracion.panelFoto);
		this.validate();
	}

	public void activaPanelEdicion(String pathFile){
		panelIntercambiable.setSelectedIndex(2);
		panelEdicion.setFotoActual(pathFile);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == itemNuevo) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Tipos de Imagen", "jpg", "jpeg", "gif", "png");
			fileChooser.setFileFilter(filter);
			int seleccion = fileChooser.showOpenDialog(this);
			if (seleccion == JFileChooser.APPROVE_OPTION)
			{
			   File fichero = fileChooser.getSelectedFile();
			   // Aquí debemos abrir y leer el fichero.
			   activaPanelFoto(fichero.getAbsolutePath());
			   DialogAsociarJuego p_AsociarJuego = new DialogAsociarJuego(controlador, fichero);
			   p_AsociarJuego.setVisible(true);
			}
		}
		
		if (e.getSource() == itemGuardarOnt){
			controlador.guardarOntologia();
			JOptionPane.showMessageDialog(this, "Se ha guardado la ontologia'");
		}
		
		if (e.getSource() == itemCargarOnt){
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Ontologia", "owl");
			fileChooser.setFileFilter(filter);
			int seleccion = fileChooser.showOpenDialog(this);
			if (seleccion == JFileChooser.APPROVE_OPTION)
			{
			   File fichero = fileChooser.getSelectedFile();
			   controlador.cargarOntologia(fichero.getAbsolutePath());
			}
		}

		if (e.getSource() == itemActualizarOnt){
			Main.checkFiles(controlador);
		}
	}

}
