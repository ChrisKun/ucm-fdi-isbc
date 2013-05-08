package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
	private JList l_modelos;
	private DefaultListModel lM_modelos;
	
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
		b_consulta.addActionListener(this);
		
		p_consultaTexto.add(l_fotosDe);
		p_consultaTexto.add(tF_consulta);
		p_consultaTexto.add(b_consulta);
		
		lM_modelos = new DefaultListModel();
		l_modelos = new JList(lM_modelos);
		panelDerecha.add(l_modelos);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_consulta){
			String s_consulta = tF_consulta.getText();
			try {
				ArrayList<String> res = controlador.ejecutaConsulta(s_consulta);
				lM_modelos.removeAllElements();
				for (String s: res){  
					lM_modelos.addElement(s);
				}
				this.validate();
			} catch (Exception e1) {
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
