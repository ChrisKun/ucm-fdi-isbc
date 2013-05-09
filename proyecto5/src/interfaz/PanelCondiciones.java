package interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import Controlador.Controlador;

public class PanelCondiciones extends JPanel implements ActionListener{

	private PanelCondicion pC_propiedades;
	private PanelCondicion pC_instancias;
	private PanelCondicion pC_juego;
	
	private JButton b_consulta;
	
	public PanelCondiciones(Controlador controlador){
		super();
		this.setLayout(new GridLayout(3,1));
		this.add(new PanelCondicion(controlador, "Propiedades", controlador.getPropiedades()));
		this.add(new PanelCondicion(controlador, "Clases/Instancias", controlador.getIndividuosYClases()));
		this.add(new PanelCondicion(controlador, "Juego", controlador.getJuegos()));
		
		b_consulta = new JButton("Consulta");
		b_consulta.addActionListener(this);
		//this.add(b_consulta);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_consulta){
			ArrayList<String> propiedades = pC_propiedades.getListaAtributos();
			ArrayList<String> instancias = pC_instancias.getListaAtributos();
			ArrayList<String> juego = pC_juego.getListaAtributos();
			
			String consulta = "";
			for (String s: propiedades){
				consulta += s;
				consulta += " ";
			}
			
			for (String s: instancias){
				consulta += s;
				consulta += " ";
			}
			
			for (String s: juego){
				consulta += "en ";
				consulta += s;
				consulta += " ";
			}
		}
	}

}
