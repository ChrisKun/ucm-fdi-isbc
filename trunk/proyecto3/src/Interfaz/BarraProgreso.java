package Interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class BarraProgreso extends JFrame{
	private static JProgressBar pb;
	private static JFrame ventana;
	
	
	
	public BarraProgreso()
	{
		ventana = this;

		this.setTitle("Por favor, espere");
		this.setContentPane(getPanelPrincipal());
		this.setVisible(true);
		this.setResizable(false);
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	    int height = pantalla.height;
	    int width = pantalla.width;
	    this.setBounds(width/2 - 75, height/2 - 50, 150, 100);
	}
	
	private JPanel getPanelPrincipal()
	{
		String str = "Cargando...";
		JPanel p = new JPanel();
		JLabel l = new JLabel(str);
		p.add(l, BorderLayout.NORTH);
		pb = new JProgressBar();
		/* Mostrar barra indeterminada sólo cuando se hace evualuación de NFOLD */
		pb.setIndeterminate(true);
		pb.setStringPainted(false);
		p.add(pb);
		return p;
	}
	
	public void cerrarVentana()
	{
		ventana.dispose();
	}

}
