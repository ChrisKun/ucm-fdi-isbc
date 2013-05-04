package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import clasificador.Main;

public class VentanaPrincipal extends JFrame{

	public static Controlador controlador;

	private JMenuBar menuBar;
	private JLabel l_Banner;
	private PanelIntercambiable panelIntercambiable;
	
	public final static int W = 1280;
	public final static int H = 720;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentanaPrincipal(Controlador controlador){
		this.setLocation(200, 150);
		this.setMinimumSize(new Dimension(W,H));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initMenuBar();
		this.setJMenuBar(menuBar);
		
		this.setLayout(new BorderLayout());
		l_Banner = new JLabel("Banner",SwingConstants.CENTER);
		this.add(l_Banner,BorderLayout.NORTH);
		this.add(controlador.getTree(),BorderLayout.WEST);
		
		panelIntercambiable = new PanelIntercambiable();
		this.add(panelIntercambiable, BorderLayout.CENTER);

	}
	
	public void initMenuBar(){
		menuBar = new JMenuBar();
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

}
