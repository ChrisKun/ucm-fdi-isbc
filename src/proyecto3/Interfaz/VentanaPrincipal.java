package Interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

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

		return null;
	}

	public static void main (String[] args)
	{
		VentanaPrincipal p = new VentanaPrincipal();
		p.setVisible(true);
	}

}
