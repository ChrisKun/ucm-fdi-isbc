package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

public class VentanaEtiquetar extends JDialog {

	private Controlador controlador;
	private static final long serialVersionUID = 1L;
	private JList list; // JAVA 6
	//private JList<String> list; FIXME JAVA 7
	private JButton b_Back;
	private JButton b_Next;
	
	
	public VentanaEtiquetar(Controlador controlador) {
		this.controlador = controlador;
		this.setLayout(new BorderLayout(5, 7));
		
		this.add(getButtonsPanel(), BorderLayout.SOUTH);
		this.add(getTipoDeContenido());		
		
		this.setSize(150, 200);
	}
	
	private JPanel getButtonsPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		
		b_Back = new JButton("Back");
		b_Next = new JButton("Next");
		
		panel.add(b_Back);
		panel.add(b_Next);
		
		return panel;
	}
	
	private JPanel getTipoDeContenido(){
		JPanel panel = new JPanel();
		ArrayList<String> list_values = controlador.getTiposDeContenido();
		String[] values = new String[list_values.size()];
		list_values.toArray(values);
		list = new JList(values); //JAVA 6
		//list = new JList<String>(values); FIXME JAVA 7
		panel.add(list);
		return panel;
	}
	
	private JPanel getPreguntasARellenar(int contenido){
		JPanel panel = new JPanel();
		ArrayList<String> list_values = controlador.getPreguntasARellenar(contenido);
		String[] preguntas = new String[list_values.size()];
		list_values.toArray(preguntas);
		//list = new JList<String>(preguntas); FIXME JAVA 7
		list = new JList(preguntas); // JAVA 6
		panel.add(list);
		return panel;
	}
}
