package Interfaz;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;

public class VentanaTerminos extends JFrame{
	
	public final static int W = 300;
	public final static int H = 200;
	private VentanaPerfil vP;
	
	
	
	public VentanaTerminos(VentanaPerfil p)
	{
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int width = pantalla.width;
		int height = pantalla.height;
		this.setBounds(width/2 - W/2, height/2 - H/2, W, H);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //TODO cerrar esta ventana debe activar la que la ha llamado.
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("Términos y condiciones");
		vP = p;
		
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
				  vP.setEnabled(true);
				  dispose();
			  }
			  });
	}

}
