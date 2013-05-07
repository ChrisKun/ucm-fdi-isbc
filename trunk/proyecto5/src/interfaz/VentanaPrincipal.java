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
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
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

import clasificador.Main;

public class VentanaPrincipal extends JFrame implements ActionListener{

	private Controlador controlador;

	private JMenuBar menuBar;
	private JMenu menuArchivo;
	private JMenuItem itemNuevo;
	private JMenuItem itemGuardarOnt;
	private JMenuItem itemCargarOnt;
	
	private JLabel l_Banner;
	
	private JTabbedPane panelIntercambiable;
	
	private JPanel panelConsulta;
	private PanelExploracion panelExploracion;
	
	private JPanel panelBotones;
	private JButton botonAtras;
	private JButton botonInicio;
	
	private Component ontoTree;

	
	
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
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initMenuBar();
		this.setJMenuBar(menuBar);
		
		this.setLayout(new BorderLayout());
		l_Banner = new JLabel("Banner",SwingConstants.CENTER);
		this.add(l_Banner,BorderLayout.NORTH);
		ontoTree = controlador.getTree();
		this.add(ontoTree,BorderLayout.WEST);
		
		panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
		botonAtras = new JButton("Atras");
		botonAtras.addActionListener(this);
		panelBotones.add(botonAtras);
		botonInicio = new JButton("Inicio");
		botonInicio.addActionListener(this);
		panelBotones.add(botonInicio);		
		this.add(panelBotones, BorderLayout.SOUTH);
		
		panelIntercambiable = new JTabbedPane();

		panelConsulta = new JPanel();
		panelIntercambiable.add("Consulta",panelConsulta);
		
		panelExploracion = new PanelExploracion(controlador);
		panelIntercambiable.add("Explorador",panelExploracion);
		
		this.add(panelIntercambiable, BorderLayout.CENTER);
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
	}

	public void activaPanelExplorador(String pathFile){
		panelExploracion.setPathExplorador(pathFile);
		panelExploracion.cambiarPanel(PanelExploracion.panelExplorador);
		this.validate();
	}
	
	public void activaPanelFoto(String pathFile){
		panelExploracion.setPathFoto(pathFile);
		panelExploracion.cambiarPanel(PanelExploracion.panelFoto);
		this.validate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == itemNuevo) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Tipos de Imagen", "jpg", "gif", "png");
			fileChooser.setFileFilter(filter);
			int seleccion = fileChooser.showOpenDialog(this);
			if (seleccion == JFileChooser.APPROVE_OPTION)
			{
			   File fichero = fileChooser.getSelectedFile();
			   // Aquí debemos abrir y leer el fichero.
			   activaPanelFoto(fichero.getAbsolutePath());
			}
		}
		
		if (e.getSource() == itemGuardarOnt){
			controlador.guardarOntologia();
		}
		
		if (e.getSource() == itemCargarOnt){
			controlador.cargarOntologia();
		}

		/////////////
		
		if (e.getSource() == botonAtras) {
			JOptionPane.showMessageDialog(null, "You shall not pass!!");
		}
		
		if (e.getSource() == botonInicio) {
			activaPanelExplorador(Main.gamesPath);
		}
	}

}
