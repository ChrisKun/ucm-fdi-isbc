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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
	
	public VentanaEtiquetar(Controlador controlador) {
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
		panelPreguntas.add(getPanelPregunta("Nombre"));
		ArrayList<String> list_values = controlador.getPreguntasARellenar(contenido);
		for (String s: list_values){
			panelPreguntas.add(getPanelPregunta(s));
		}
		panelPreguntas.add(getButtonsPanel());
	}

	private JPanel getPanelPregunta(String string) {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout(20,15));
		p.add(new JLabel(string),BorderLayout.WEST);
		p.add(new TextField(50),BorderLayout.EAST);
		return p;
	}

	public ArrayList<String> recopilarRespuestas(){
		ArrayList<String> respuestas = new ArrayList<String>();
		Component[] components = panelPreguntas.getComponents();
		TextField tF;
		JPanel p;
		for (int i=0;i<components.length-1;i++){
			p = (JPanel) components[i];
			tF = (TextField) p.getComponent(1);
			respuestas.add(tF.getText());
		}
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
			JOptionPane.showMessageDialog(this, "Habilitame un metodo para pasarte todo esto");
			recopilarRespuestas();
		}
	}
}
