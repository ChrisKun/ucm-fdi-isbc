package interfaz;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import controlador.Controlador;


public class PanelCondicionJuego extends PanelCondicion implements ActionListener{

	private JButton b_Add;
	private JButton b_Remove;
	
	public PanelCondicionJuego(Controlador controlador, String titulo,ArrayList<String> elementos) {
		super(controlador, titulo, elementos);
		
		b_Add = new JButton("+");
		b_Add.addActionListener(this);
		
		b_Remove = new JButton("-");
		b_Remove.addActionListener(this);
		b_Remove.setEnabled(false);
		
		panelBotones.setLayout(new GridLayout(2, 1));
		panelBotones.removeAll();
		panelBotones.add(b_Add);
		panelBotones.add(b_Remove);
		this.validate();
	}

	/* (non-Javadoc)
	 * @see interfaz.PanelCondicion#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_Add){
			addElementFromComboBox(true);
			b_Add.setEnabled(false);
			b_Remove.setEnabled(true);
		}
		if (e.getSource() == b_Remove){
			if (removeElementFromComboBox()){
				b_Add.setEnabled(true);
				b_Remove.setEnabled(false);
			}
			
		}
	}
}
