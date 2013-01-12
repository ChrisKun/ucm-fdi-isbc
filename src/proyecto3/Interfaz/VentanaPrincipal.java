package Interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaPrincipal extends JFrame {
	
	
	public VentanaPrincipal()
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setContentPane(getPanelPrincipal());
		
	}
	
	private Container getPanelPrincipal() {
		JPanel panelP = new JPanel();
		panelP.setLayout(new BorderLayout());
		
		
		// Panel de inicio NOTA: Es el panel que variará
		PanelInicio p = new PanelInicio();
		panelP.add(p, BorderLayout.CENTER);
		
		// Panel Superior: Con información
		panelP.add(getPanelSuperior());
		
		// Panel Occidental: 
		
		
		return panelP;
	}

	private JPanel getPanelSuperior() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(getBoton("Cesta"));
		p.add(getBoton("Perfil"));
		p.add(getBoton("Ayuda"));
		return p;
	}
	
	private JButton getBoton(String str)
	{
		JButton jb = new JButton();
		jb.setText(str);
		
		if (str.equals("Ayuda"))
		{
			jb.setText("");
			ImageIcon icon = new ImageIcon("src/proyecto3/images/help.png");
			jb.setIcon(icon);
			jb.setToolTipText(str);
		}
		//action Listener
		return jb;
	}

	public static void main (String[] args)
	{
		VentanaPrincipal p = new VentanaPrincipal();
		p.setVisible(true);
	}

}
