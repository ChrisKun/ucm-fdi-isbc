package interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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

import Controlador.Controlador;

import clasificador.Main;

public class PanelEtiquetar extends JPanel implements ActionListener{

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
	
	public PanelEtiquetar(Controlador controlador) {
		this.controlador = controlador;
		this.setLayout(new CardLayout(10, 10));
		
		panelTipos = new JPanel();
		panelTipos.setLayout(new BorderLayout());
		JLabel l = new JLabel("Por favor, selecciona un tipo de individuo a crear: ");
		l.setHorizontalAlignment((int) l.CENTER_ALIGNMENT);
		l.setFont(new Font(l.getFont().getFontName(), Font.PLAIN, 20));
		panelTipos.add(BorderLayout.NORTH,l);
		
		panelPreguntas = new JPanel();
		panelPreguntas.setLayout(new GridLayout(0,2));
		
		actualizarPanelTipo();
		this.add(panelTipos, s_Tipos);
		this.add(panelPreguntas, s_Preguntas);
		
		this.setPreferredSize(new Dimension(550,400));
		
		setFotoActual("");
	}
	
	public void cambiarPanel(String nuevoPanel){
		CardLayout cl = (CardLayout) this.getLayout();		
		cl.show(this, nuevoPanel);
		this.validate();
	}
	
	private JPanel getButtonsPanel(){
		JPanel panel = new JPanel();
		
		b_Back = new JButton("Atras");
		b_Back.addActionListener(this);
		b_Send = new JButton("Crear");
		b_Send.addActionListener(this);
		
		panel.add(b_Back);
		panel.add(b_Send);
		
		return panel;
	}
	
	private void actualizarPanelTipo(){
		
		JPanel sobrePanel = new JPanel();
		sobrePanel.setLayout(new GridLayout(0,3));
		
		JPanel p = new JPanel();
		GridLayout f = new GridLayout(0,1);
		f.setVgap(50);
		f.setHgap(200);
		//f.setAlignment(FlowLayout.CENTER);
		p.setLayout(f);
		
		ArrayList<String> list_values = controlador.getTiposDeContenido();
		
		for (final String s: list_values){
			JButton b = new JButton(s);
			b.setSize(250, 100);
			b.setFont(new Font(b.getFont().getFontName(), Font.BOLD, 22));
			b.setBorder(BorderFactory.createBevelBorder(0));
			b.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					actualizarPanelPreguntas(controlador.dameIndiceContenido(s));
					cambiarPanel(s_Preguntas);
				}
				
			});
			p.add(b);
		}
		sobrePanel.add(new JLabel());
		sobrePanel.add(p);
		panelTipos.add(BorderLayout.CENTER,sobrePanel);
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

	/**
	 * @param string
	 * @param nombre
	 * @param unaRespuesta
	 * @param individuos
	 * @return
	 */
	private JPanel getPanelPregunta(final String string,boolean nombre, Vector<String> individuos) {
		JPanel p = new JPanel();
		if (nombre){
			p.setLayout(new FlowLayout());
			fieldNombreIndividuo = new TextField(50);
			p.add(fieldNombreIndividuo);
		}
		else {
			p.setLayout(new FlowLayout());
			//Parte de apareceEn (especial para restringir a la foto actual)
			if (string.equals("OTRA cosA")){ //XXX Config.apareceEn
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
			//lista con elementos añadidos (lo que se pasa al controlador)
			List l = new List();
			respuestasMultiRespuesta.put(string, l);
			p.add(l);
		}		
		
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), string);
		title.setTitleJustification(TitledBorder.CENTER);
		p.setBorder(title);
		return p;
	}

	public HashMap<String,ArrayList<String>> recopilarRespuestas(){
		HashMap<String,ArrayList<String>> respuestas = new HashMap<String,ArrayList<String>>();
		
		ArrayList<String> a = new ArrayList<String>();
		a.add(fieldNombreIndividuo.getText());
		
		respuestas.put("Nombre", a);
		
		//Para el resto hay que recopilar acorde a la categoria
		ArrayList<String> propiedades = controlador.getPreguntasARellenar(cont);
		
		// Ahora vamos recorriendo todas las propiedades y sacamos la lista
		// asociada a la propiedad y lo pasamos a un arrayList
		List lista;
		String[] l;
		ArrayList<String> resp;
		for (int i = 0; i < propiedades.size(); i++){
			lista = respuestasMultiRespuesta.get(propiedades.get(i));
			//Ahora lo añadimos al hashmap a devolver
			l = lista.getItems();
			resp = new ArrayList<String>();
			for (int j = 0; j < l.length; j++){
				resp.add(l[j]);
			}
			respuestas.put(propiedades.get(i),resp);
		}
		return respuestas;
	}
	
	public void setFotoActual(String nomFoto){
		this.nomFoto = nomFoto;
		cambiarPanel(s_Tipos);
		actualizarPanelTipo();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_Back){
			cambiarPanel(s_Tipos);
		}
		if (e.getSource() == b_Send){
			if (!fieldNombreIndividuo.getText().equals("")){
				controlador.crearIndividuo(cont, recopilarRespuestas());
				cambiarPanel(s_Tipos);
			}	
			else
				JOptionPane.showMessageDialog(null, "Por favor, introduce un nombre de instancia");
		}
	}
}
