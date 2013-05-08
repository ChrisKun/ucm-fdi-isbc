package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controlador.Controlador;

public class PanelConsulta extends JPanel implements ActionListener{

	private Controlador controlador;
	
	private JPanel panelIzquierda;
	private JPanel panelDerecha;
	private JLabel l_fotosDe;
	private JTextField tF_consulta;
	private JButton b_consulta;
	private JList modelos;
	public PanelConsulta(Controlador controlador){
		super();
		this.controlador = controlador;
		//this.setLayout(new GridLayout(1,2));
		this.setLayout(new BorderLayout());
		panelIzquierda = new JPanel();
		panelDerecha = new JPanel();
		
		panelIzquierda.setLayout(new BorderLayout());
		
		this.add(panelIzquierda, BorderLayout.WEST);
		this.add(panelDerecha, BorderLayout.EAST);
		
		JPanel p_consultaTexto = new JPanel();
		JPanel p_consultaBotones = new PanelCondiciones(controlador);
		
		panelIzquierda.add(p_consultaTexto, BorderLayout.NORTH);
		panelIzquierda.add(p_consultaBotones, BorderLayout.CENTER);
		
		l_fotosDe = new JLabel("FOTOS DE: ");
		tF_consulta = new JTextField(30);
		b_consulta = new JButton("Consulta");
		
		p_consultaTexto.add(l_fotosDe);
		p_consultaTexto.add(tF_consulta);
		p_consultaTexto.add(b_consulta);
				
		modelos = new JList(/*controlador.getModelos()*/);
		panelDerecha.add(modelos);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_consulta){
			
		}
		
	}
}
