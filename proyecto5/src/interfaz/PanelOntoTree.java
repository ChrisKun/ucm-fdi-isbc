package interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controlador.ArbolPersonalizado;
import controlador.Controlador;


public class PanelOntoTree extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	private JComboBox cB_Seleccion;
	private String[] valoresSeleccion = {"Contenidos","Fotos"};
	private JPanel panelTrees;
	private ArrayList<ArbolPersonalizado> trees;
	
	private Controlador controlador;
	
	public PanelOntoTree(Controlador controlador){
		super();
		this.controlador = controlador;
		
		this.setLayout(new BorderLayout(10,10));
		panelTrees = new JPanel();
		panelTrees.setLayout(new CardLayout());
		trees = controlador.getTrees();
		for (int i=0;i<trees.size();i++){
			panelTrees.add(trees.get(i),valoresSeleccion[i]);
		}
		this.add(panelTrees,BorderLayout.CENTER);
		
		cB_Seleccion = new JComboBox(valoresSeleccion);
		cB_Seleccion.addActionListener(this);
		this.add(cB_Seleccion, BorderLayout.NORTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cB = (JComboBox) e.getSource();
		CardLayout cl = (CardLayout) panelTrees.getLayout();		
		
		if (cB.getSelectedItem() == valoresSeleccion[0]){
			cl.show(panelTrees, valoresSeleccion[0]);
		}
		if (cB.getSelectedItem() == valoresSeleccion[1]){
			cl.show(panelTrees, valoresSeleccion[1]);
		}
		panelTrees.validate();
	}
	 
	public void actualizar(){
		for (ArbolPersonalizado a: trees){
			a.actualizar();
		}
	}
	
}
