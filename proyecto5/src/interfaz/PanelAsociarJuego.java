package interfaz;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PanelAsociarJuego extends JPanel{
	
	private Controlador controlador;
	
	private JPanel p_existe;
	private JRadioButton rB_existe;
	private JComboBox cB_existe;
	
	private JPanel p_nuevo;
	private JRadioButton rB_nuevo;
	private JTextField tF_nuevo;
	
	public PanelAsociarJuego(Controlador controlador){
		super();
		this.controlador = controlador;
		this.setLayout(new GridLayout(2, 2));
		p_existe = new JPanel();
		rB_existe = new JRadioButton("Existe");
		ButtonGroup bG = new ButtonGroup();
		
		p_existe.add(rB_existe);
		cB_existe = new JComboBox();
		p_existe.add(cB_existe);
		
		p_nuevo = new JPanel();
		rB_nuevo = new JRadioButton("Nuevo");
		p_nuevo.add(rB_nuevo);
		tF_nuevo = new JTextField();
		p_existe.add(tF_nuevo);
		
		bG.add(rB_existe);
		bG.add(rB_nuevo);
		
		this.add(p_existe);
		this.add(p_nuevo);
	}

}
