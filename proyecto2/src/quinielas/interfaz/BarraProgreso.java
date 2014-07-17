package src.quinielas.interfaz;

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
	private static int valorProgeso;
	private static int maxValorProgreso;
	
	public final static int MODOPARSER = 0;
	public final static int MODONFOLDEVALUATOR = 1;
	
	
	public BarraProgreso(int modo, int valorMax)
	{
		ventana = this;
		
		maxValorProgreso = valorMax; //modo consulta y actualizacion			
		valorProgeso = 0;
		
		this.setTitle("Por favor, espere");
		this.setContentPane(getPanelPrincipal(modo));
		this.setVisible(true);
		this.setResizable(false);
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	    int height = pantalla.height;
	    int width = pantalla.width;
	    this.setBounds(width/2 - 75, height/2 - 50, 150, 100);
	}
	
	private JPanel getPanelPrincipal(int modo)
	{
		String str = "Actualizando...";
		if (modo != MODOPARSER)
			str = "Consultando...";
		JPanel p = new JPanel();
		JLabel l = new JLabel(str);
		p.add(l, BorderLayout.NORTH);
		pb = new JProgressBar();
		/* Mostrar barra indeterminada sólo cuando se hace evualuación de NFOLD */
		if (modo == MODONFOLDEVALUATOR)
		{
			pb.setIndeterminate(true);
			pb.setStringPainted(false);
		}
		else
		{
			pb.setIndeterminate(false);
			pb.setMaximum(maxValorProgreso);
			pb.setStringPainted(true);
		}
		
		
		p.add(pb);
		return p;
	}
	
	public static void setBarraProgreso(int i)
	{
		pb.setValue(i);
	}
	
	public static void aumentarBarraProgreso()
	{
		pb.setValue(pb.getValue()+1);
	}
	
	public static void setValorMaxBarraProgreso(int i)
	{
		pb.setMaximum(i);
	}
	
	public void cerrarVentana()
	{
		ventana.dispose();
	}

}
