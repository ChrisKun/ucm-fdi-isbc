package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import clasificador.Main;

public class VentanaPrincipal extends JFrame implements ActionListener{

	public static Controlador controlador;

	private JMenuBar menuBar;
	private JMenu menuArchivo;
	private JMenuItem itemNuevo;
	
	private JLabel l_Banner;
	
	private PanelIntercambiable panelIntercambiable;
	
	private JPanel panelBotones;
	private JButton botonAtras;
	private JButton botonInicio;
	
	
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
		this.add(controlador.getTree(),BorderLayout.WEST);
		
		panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
		botonAtras = new JButton("Atras");
		botonAtras.addActionListener(this);
		panelBotones.add(botonAtras);
		botonInicio = new JButton("Inicio");
		botonInicio.addActionListener(this);
		panelBotones.add(botonInicio);		
		this.add(panelBotones, BorderLayout.SOUTH);
		
		panelIntercambiable = new PanelIntercambiable();
		this.add(panelIntercambiable, BorderLayout.CENTER);
	}
	
	private void initMenuBar(){
		menuBar = new JMenuBar();
		menuArchivo = new JMenu("Archivo");
		menuBar.add(menuArchivo);
		itemNuevo = new JMenuItem("Nuevo");
		itemNuevo.addActionListener(this);
		menuArchivo.add(itemNuevo);
	}

	public void activaPanelExplorador(String pathFile){
		panelIntercambiable.setPathExplorador(pathFile);
		panelIntercambiable.cambiarPanel(PanelIntercambiable.panelExplorador);
		this.validate();
	}
	
	public void activaPanelFoto(String pathFile){
		panelIntercambiable.setPathFoto(pathFile);
		panelIntercambiable.cambiarPanel(PanelIntercambiable.panelFoto);
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
		
		if (e.getSource() == botonAtras) {
			JOptionPane.showMessageDialog(null, "You shall not pass!!");
		}
		
		if (e.getSource() == botonInicio) {
			activaPanelExplorador(Main.gamesPath);
		}
	}

}
