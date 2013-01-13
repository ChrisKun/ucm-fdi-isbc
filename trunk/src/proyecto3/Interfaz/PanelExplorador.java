package Interfaz;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class PanelExplorador extends JPanel{

	public PanelExplorador(){
		this.setLayout(new BorderLayout());
		this.add(getPanelFiltrado(), BorderLayout.NORTH);
		this.add(getPanelArticulos(), BorderLayout.CENTER);
	}

	private JPanel getPanelFiltrado() {
		// TODO Auto-generated method stub
		JPanel p = new JPanel();
		
		return p;
	}
	
	private JPanel getPanelArticulos() {
		// TODO Auto-generated method stub
		JPanel p = new JPanel();
		
		return p;
	}
}
