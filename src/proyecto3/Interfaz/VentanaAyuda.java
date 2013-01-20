package Interfaz;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

public class VentanaAyuda extends JFrame {

	public final static int W = 350;
	public final static int H = 400;
	private VentanaPrincipal vP;
	
	public VentanaAyuda(VentanaPrincipal p)
	{
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int width = pantalla.width;
		int height = pantalla.height;
		this.setBounds(width/2 - W/2, height/2 - H/2, W, H);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //TODO cerrar esta ventana debe activar la que la ha llamado.
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("Ayuda");
		vP = p;
		
		this.setContentPane(getPanelPrincipal());
		
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
				  vP.setEnabled(true);
				  dispose();
			  }
			  });
		
	}



	private JPanel getPanelPrincipal() {
		JPanel p = new JPanel();
		char slash = File.separatorChar;
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Esperemos que le sea útil"));
		JLabel l = new JLabel();
		ImageIcon icon = new ImageIcon("src"+slash+"proyecto3"+slash+"images"+slash+"help_src.gif");
		l.setIcon(icon);
		p.add(l);
		return p;
	}
	
}
