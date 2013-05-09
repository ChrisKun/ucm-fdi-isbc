package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clasificador.Main;

import Controlador.Controlador;

public class PanelCondiciones extends JPanel implements ActionListener{

	private PanelCondicion pC_propiedades;
	private PanelCondicion pC_instancias;
	private PanelCondicion pC_juego;
	
	private JButton b_consulta;
	private Controlador controlador;
	
	public PanelCondiciones(Controlador controlador){
		super();
		this.controlador = controlador;
		this.setLayout(new BorderLayout());
		
		JPanel panelPreguntas = new JPanel();
		panelPreguntas.setLayout(new GridLayout(3,1));
		
		pC_propiedades = new PanelCondicion(controlador, "Propiedades", controlador.getPropiedades());
		pC_instancias = new PanelCondicion(controlador, "Clases/Instancias", controlador.getIndividuosYClases());
		pC_juego = new PanelCondicion(controlador, "Juego", controlador.getJuegos());
			
		panelPreguntas.add(pC_propiedades);
		panelPreguntas.add(pC_instancias);
		panelPreguntas.add(pC_juego);
		
		this.add(panelPreguntas, BorderLayout.CENTER);
		
		
		b_consulta = new JButton("Consulta");
		b_consulta.addActionListener(this);
		this.add(b_consulta, BorderLayout.SOUTH);
		
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
			try {
				ArrayList<String> res = controlador.ejecutaConsulta(consulta);
				ArrayList<String> res_ = new ArrayList<String>();
				for (String s: res){
					s = s.substring(s.indexOf("\\fotos")+1);
					res_.add(s);
				}
				Main.vista.activaPanelExplorador(res_);
			}catch (Exception e1) {
				JOptionPane.showMessageDialog(
						null, 
						e1.getMessage(), 
						"Error en la consulta", 
						JOptionPane.NO_OPTION
						);
			}
		}
	}

}
