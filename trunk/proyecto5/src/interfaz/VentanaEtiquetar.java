package interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import clasificador.Main;

public class VentanaEtiquetar extends JPanel implements ListSelectionListener, ActionListener{

	private Controlador controlador;
	private static final long serialVersionUID = 1L;
	private JList list; // JAVA 6
	private DefaultListModel listModel;
	//private JList<String> list; FIXME JAVA 7
	private JButton b_Back;
	private JButton b_Send;
	
	public static final String s_Tipos = "Tipos de Contenido";
	public static final String s_Preguntas = "Preguntas a contestar";
	
	private JPanel panelTipos;
	private JPanel panelPreguntas;
	
	String nomFoto;
	TablaIndividuos tab;
	
	private ArrayList<JComboBox> comboBoxRespuestas;
	private int cont;
	
	public VentanaEtiquetar(Controlador controlador, String nomFoto, TablaIndividuos tab) {
		this.controlador = controlador;
		this.setLayout(new CardLayout(10, 10));
		
		panelTipos = new JPanel();		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setBackground(this.getBackground());
		list.addListSelectionListener(this);
		
		panelPreguntas = new JPanel();
		panelPreguntas.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
		
		
		actualizarPanelTipo();
		this.add(panelTipos, s_Tipos);
		this.add(panelPreguntas, s_Preguntas);
		
		this.setPreferredSize(new Dimension(550,400));
		
		this.nomFoto = nomFoto;
		this.tab = tab;
	}
	
	public void cambiarPanel(String nuevoPanel){
		CardLayout cl = (CardLayout) this.getLayout();		
		cl.show(this, nuevoPanel);
		this.validate();
	}
	
	private JPanel getButtonsPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		
		b_Back = new JButton("Atras");
		b_Back.addActionListener(this);
		b_Send = new JButton("Crear");
		b_Send.addActionListener(this);
		
		panel.add(b_Back);
		panel.add(b_Send);
		
		return panel;
	}
	
	private void actualizarPanelTipo(){
		listModel.removeAllElements();		
		ArrayList<String> list_values = controlador.getTiposDeContenido();
		for (String s: list_values){
			listModel.addElement(s);
		}
		panelTipos.add(list);
	}
	
	private void actualizarPanelPreguntas(int contenido){
		panelPreguntas.removeAll();
		cont = contenido;
		comboBoxRespuestas = new ArrayList<JComboBox>();
		panelPreguntas.add(getPanelPregunta("Nombre", true, null));
		ArrayList<String> list_values = controlador.getPreguntasARellenar(contenido);
		for (String s: list_values){
			Vector<String> v = new Vector<String>(controlador.getIndividuosValidosRellenarPropiedad(s));
			panelPreguntas.add(getPanelPregunta(s,false,v));
		}
		panelPreguntas.add(getButtonsPanel());
	}

	private JPanel getPanelPregunta(String string,boolean nombre, Vector<String> individuos) {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout(20,15));
		p.add(new JLabel(string),BorderLayout.WEST);
		if (nombre)
			p.add(new TextField(50),BorderLayout.EAST);
		else if (string.equals(Config.apareceEn)){
			Vector<String> v = new Vector<String>();
			v.add(nomFoto); //FIXME Chapuza xD
			JComboBox j = new JComboBox(v);
			p.add(j);
			comboBoxRespuestas.add(j);
		}
		else {
			individuos.add(""); //para que se pueda dejar en blanco
			JComboBox j = new JComboBox(individuos); 
			p.add(j);
			// agregamos ahora el combobox a la lista para mantener el puntero
			comboBoxRespuestas.add(j);
		}
		return p;
	}

	public ArrayList<String> recopilarRespuestas(){
		ArrayList<String> respuestas = new ArrayList<String>();
		Component[] components = panelPreguntas.getComponents();
		TextField tF;
		JPanel p;
		// Nombre
		p = (JPanel) components[0];
		tF = (TextField) p.getComponent(1);
		respuestas.add(tF.getText());
		// Resto de respuestas
		for (int i = 0; i < comboBoxRespuestas.size(); i++){
			respuestas.add((String)comboBoxRespuestas.get(i).getSelectedItem());
		}
		
		/*for (int i=0;i<components.length-1;i++){
			p = (JPanel) components[i];
			tF = (TextField) p.getComponent(1);
			respuestas.add(tF.getText());
		}*/
		return respuestas;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList) e.getSource();
		if (e.getValueIsAdjusting()){
			System.out.println(list.getSelectedValue());
			actualizarPanelPreguntas(list.getSelectedIndex());
			cambiarPanel(s_Preguntas);
			list.setSelectedIndex(-1);
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_Back){
			cambiarPanel(s_Tipos);
		}
		if (e.getSource() == b_Send){
			//JOptionPane.showMessageDialog(this, "Habilitame un metodo para pasarte todo esto");
			controlador.crearIndividuo(cont, recopilarRespuestas().get(0),recopilarRespuestas());
			tab.actualizarContenidoFoto(nomFoto);
		}
	}
}
