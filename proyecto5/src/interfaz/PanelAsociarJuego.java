package interfaz;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PanelAsociarJuego extends JPanel{
	
	private Controlador controlador;
	
	private JPanel p_existe;
	private JRadioButton rB_existe;
	private JComboBox cB_existe;
	
	private JPanel p_nuevo;
	private JRadioButton rB_nuevo;
	private JComboBox cB_nuevo;
	
	public PanelAsociarJuego(Controlador controlador){
		super();
		this.controlador = controlador;
		p_existe = new JPanel();
		rB_existe = new JRadioButton("Existe");
		p_existe.add(rB_existe);
		cB_existe = new JComboBox();
		p_existe.add(cB_existe);
		
		p_nuevo = new JPanel();
		rB_nuevo = new JRadioButton("Nuevo");
		p_nuevo.add(rB_nuevo);
		cB_nuevo = new JComboBox();
		p_existe.add(cB_nuevo);
	}

}
