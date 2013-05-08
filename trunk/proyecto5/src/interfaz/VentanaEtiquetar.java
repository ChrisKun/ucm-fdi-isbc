package interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
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
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
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
	
	private HashMap<String,JComboBox> comboBoxRespuestas;
	private HashMap<String,List> respuestasMultiRespuesta;
	private int cont;
	private TextField fieldNombreIndividuo;
	
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
		comboBoxRespuestas = new HashMap<String,JComboBox>();
		respuestasMultiRespuesta = new HashMap<String,List>();
		panelPreguntas.add(getPanelPregunta("Nombre", true, null));
		ArrayList<String> list_values = controlador.getPreguntasARellenar(contenido);
		for (String s: list_values){
			Vector<String> v = new Vector<String>(controlador.getIndividuosValidosRellenarPropiedad(s));
			panelPreguntas.add(getPanelPregunta(s,false,v));
		}
		panelPreguntas.add(getButtonsPanel());
	}

	private JPanel getPanelPregunta(final String string,boolean nombre, Vector<String> individuos) {
		JPanel p = new JPanel();
		if (nombre){
			p.setLayout(new FlowLayout());
			//p.add(new JLabel(string));
			fieldNombreIndividuo = new TextField(50);
			p.add(fieldNombreIndividuo);
		}
		else {
			p.setLayout(new FlowLayout());
			//p.add(new JLabel(string));
			//Parte de apareceEn (especial para restringir a la foto actual)
			if (string.equals(Config.apareceEn)){
				Vector<String> v = new Vector<String>();
				v.add(nomFoto); 
				JComboBox j = new JComboBox(v);
				p.add(j);
				comboBoxRespuestas.put(string,j);
			}
			else{
				JComboBox j = new JComboBox(individuos); 
				p.add(j);
				// agregamos ahora el combobox a la lista para mantener el puntero
				comboBoxRespuestas.put(string,j);
			}
			//paneles de botones
			JPanel panelBotones = new JPanel();
			panelBotones.setLayout(new GridLayout(2,1));
			JButton bMas = new JButton("+");
			bMas.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					//1. Obtenemos la respuesta seleccionada del ComboBox
					String sel = (String) comboBoxRespuestas.get(string).getSelectedItem();
					//2. Comprobamos que no esta en la lista
					List lis = respuestasMultiRespuesta.get(string);
					//2.b pasamos el contenido a una estructura
					String[] con = lis.getItems();
					//2.c comprobamos que no existe
					boolean enc = false;
					for (int i = 0; i < con.length && !enc; i++)
						enc = con[i].equals(sel);
					//3. lo incluimos si no estaba
					if (!enc)
						lis.add(sel);
				}
				
			});
			panelBotones.add(bMas);
			
			JButton bMenos = new JButton("-");
			bMenos.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					//Borramos el item seleccionado de la lista
					List lis = respuestasMultiRespuesta.get(string);
					int indexSel = lis.getSelectedIndex();
					if (indexSel>=0)
						lis.remove(indexSel);
				}
			});
			
			panelBotones.add(bMenos);
			
			p.add(panelBotones);
			//lista con elementos a�adidos (lo que se pasa al controlador)
			List l = new List();
			respuestasMultiRespuesta.put(string, l);
			p.add(l);
		}		
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), string);
		title.setTitleJustification(TitledBorder.CENTER);
		p.setBorder(title);
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
			//FIXME Actualizar a la nueva forma
			controlador.crearIndividuo(cont, recopilarRespuestas().get(0),recopilarRespuestas());
			tab.actualizarContenidoFoto(nomFoto);
		}
	}
}
