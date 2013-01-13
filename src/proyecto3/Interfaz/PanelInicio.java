package Interfaz;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelInicio extends JPanel{
	
	public PanelInicio()
	{
		this.setLayout(new BorderLayout());
		
		JLabel p = new JLabel("Imagen");
		this.add(p , BorderLayout.CENTER);
		
		JButton jb = new JButton();
		jb.setText("Review");
		jb.setSize(20, 10);
		this.add(jb, BorderLayout.SOUTH);
	}
}
