package Interfaz;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class VentanaTerminos extends JFrame{
	
	public final static int W = 300;
	public final static int H = 400;
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
		this.setTitle("T�rminos y condiciones");
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
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Por favor, lea atentamente"));
		JTextArea j = new JTextArea();
		j.setPreferredSize(new Dimension(W-30,H-70));
		j.setText("1. Acepto que mirar� con buenos ojos la pr�ctica \n 2. Acepto que Ra�l Bueno Sevilla no es buena gente \n 3. Acepto que el punto 2 es mentira a medias \n" +
				"4. \n 5.\n 6.\n 7.\n 8.\n 9.\n 10.\n 11.\n 12.\n");
		j.setEditable(false);
		JScrollPane listScroller = new JScrollPane(j);
		p.add(listScroller);
		return p;
	}
	
	

}
