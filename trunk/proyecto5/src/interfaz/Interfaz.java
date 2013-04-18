package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Interfaz extends JFrame {

	private Controlador controlador;

	private JMenuBar menuBar;
	private JLabel l_Banner;
	
	public final static int W = 1280;
	public final static int H = 720;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Interfaz(Controlador controlador){
		this.setLocation(200, 150);
		this.setMinimumSize(new Dimension(W,H));
		
		initMenuBar();
		this.setJMenuBar(menuBar);
		
		this.setLayout(new BorderLayout());
		l_Banner = new JLabel("Banner",SwingConstants.CENTER);
		this.add(l_Banner,BorderLayout.NORTH);
		this.add(controlador.getTree(),BorderLayout.WEST);
	}
	
	public void initMenuBar(){
		menuBar = new JMenuBar();
	}
}
