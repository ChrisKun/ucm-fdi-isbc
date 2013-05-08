package interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
		this.setLayout(new GridLayout(1,2));
		
		panelIzquierda = new JPanel();
		this.add(panelIzquierda);
		panelDerecha = new JPanel();
		this.add(panelDerecha);
		
		l_fotosDe = new JLabel("FOTOS DE: ");
		tF_consulta = new JTextField(30);
		b_consulta = new JButton("Consulta");
		panelIzquierda.add(l_fotosDe);
		panelIzquierda.add(tF_consulta);
		panelIzquierda.add(b_consulta);
		
		modelos = new JList(/*controlador.getModelos()*/);
		panelDerecha.add(modelos);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
