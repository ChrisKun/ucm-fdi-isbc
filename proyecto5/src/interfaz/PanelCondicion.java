package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PanelCondicion extends JPanel implements ActionListener{

	private String titulo;
	
	private JComboBox cB_elementos;
	
	private JButton b_Union;
	private JButton b_Interseccion;
	private JButton b_Eliminar;
		
	private JList l_propiedades;
	private DefaultListModel lM_propiedades;
	
	public PanelCondicion(Controlador controlador, String titulo, ArrayList<String> elementos){
		super();
		this.add(createPanelPregunta(titulo, controlador.getPropiedades()));		
	}
	
	/**
	 * @param string
	 * @param nombre
	 * @param unaRespuesta
	 * @param elementos
	 * @return
	 */
	private JPanel createPanelPregunta(String titulo, ArrayList<String> elementos) {
		
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), titulo);		
		title.setTitleJustification(TitledBorder.CENTER);
		
		Vector v = new Vector(elementos);
		cB_elementos = new JComboBox(v); 
		p.add(cB_elementos);
				
		// agregamos ahora el combobox a la lista para mantener el puntero
		//comboBoxRespuestas.put(string,j);
		
		//paneles de botones
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(3,1));
		
		b_Union = new JButton("Union");
		b_Interseccion = new JButton("Interseccion");
		b_Eliminar = new JButton("Eliminar");
		
		b_Union.addActionListener(this);
		b_Interseccion.addActionListener(this);
		b_Eliminar.addActionListener(this);
		
		panelBotones.add(b_Union);
		panelBotones.add(b_Interseccion);
		panelBotones.add(b_Eliminar);
		
		p.add(panelBotones);
		
		//lista con elementos añadidos (lo que se pasa al controlador)
		/*
		List l = new List();
		respuestasMultiRespuesta.put(titulo, l);
		p.add(l);
		 */	
		lM_propiedades = new DefaultListModel();
		l_propiedades = new JList(lM_propiedades);
		l_propiedades.setPreferredSize(new Dimension(300, 140));
		
		p.add(l_propiedades);
		
		p.setBorder(title);
		return p;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_Union){
			addElementFromComboBox();
		}
		if (e.getSource() == b_Interseccion){
			addElementFromComboBox();
		}
		if (e.getSource() == b_Eliminar){
			removeElementFromComboBox();
		}
	}
	
	private void addElementFromComboBox(){
		
		String s = (String) cB_elementos.getSelectedItem();
		lM_propiedades.addElement(s);
		
	}
	
	private void removeElementFromComboBox(){
		int pos = l_propiedades.getSelectedIndex();
		if (pos >= 0){
			lM_propiedades.remove(pos);
		}
	}

}
