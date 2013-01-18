package Interfaz;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class VentanaCesta extends JFrame{//JInternalFrame{

	public final static int W = 300;
	public final static int H = 200;
	
	public VentanaCesta(){
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int width = pantalla.width;
		int height = pantalla.height;
		this.setBounds(width/2 - W/2, height/2 - H/2, W, H);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //TODO cerrar esta ventana debe activar la que la ha llamado.
		this.setVisible(true);
		this.setResizable(false);
		
		this.setContentPane(getPanelPrincipalCesta());
	}

	private Container getPanelPrincipalCesta() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		return p;
	}
}
