package interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelOntoTree extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	private JComboBox cB_Seleccion;
	private String[] valoresSeleccion = {"Contenidos","Fotos"};
	private Component ontoTree;
	
	
	private Controlador controlador;
	
	public PanelOntoTree(Controlador controlador){
		super();
		this.controlador = controlador;
		
		this.setLayout(new BorderLayout(10,10));
		
		ontoTree = controlador.getTree();
		this.add(ontoTree,BorderLayout.CENTER);
		
		cB_Seleccion = new JComboBox(valoresSeleccion);
		cB_Seleccion.addActionListener(this);
		this.add(cB_Seleccion, BorderLayout.NORTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cB = (JComboBox) e.getSource();
		if (cB.getSelectedItem() == valoresSeleccion[0]){
			controlador.setTree(Config.SeleccionArbol.Contenido);
		}
		if (cB.getSelectedItem() == valoresSeleccion[1]){
			controlador.setTree(Config.SeleccionArbol.Foto);
		}
		ontoTree = controlador.getTree();
	}
}
