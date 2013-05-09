package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import clasificador.Main;

import Controlador.Controlador;

public class DialogAsociarJuego extends JDialog implements ActionListener, FocusListener{
	
	private Controlador controlador;
	private File fichero;
	
	private JPanel p_existe;
	private JRadioButton rB_existe;
	private JComboBox cB_existe;
	
	private JPanel p_nuevo;
	private JRadioButton rB_nuevo;
	private JTextField tF_nuevo;
	
	private ButtonGroup bG_botones;
	
	private JButton b_Seleccionar;
	
	public DialogAsociarJuego(Controlador controlador, File fichero){
		super();
		this.fichero = fichero;
		this.controlador = controlador;
		this.setBounds(500, 250, 320, 150);
		this.setTitle("Asociar la foto a un juego");
		JPanel p_asociarJuego = new JPanel();
		this.setContentPane(p_asociarJuego);
		p_asociarJuego.setLayout(new GridLayout(3,1));
		p_existe = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rB_existe = new JRadioButton("Existe");
		rB_existe.setSelected(true);
		bG_botones = new ButtonGroup();
		
		p_existe.add(rB_existe);
		
		ArrayList<String> aL_juegos = controlador.getJuegos();
		Vector<String> v_juegos = new Vector<String>(aL_juegos);
		cB_existe = new JComboBox(v_juegos);
		cB_existe.addFocusListener(this);
		
		p_existe.add(cB_existe);
		
		p_nuevo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rB_nuevo = new JRadioButton("Nuevo");
		p_nuevo.add(rB_nuevo);
		tF_nuevo = new JTextField();
		tF_nuevo.setColumns(20);
		tF_nuevo.addFocusListener(this);
		
		p_nuevo.add(tF_nuevo);
		
		bG_botones.add(rB_existe);
		bG_botones.add(rB_nuevo);
		
		p_asociarJuego.add(p_existe);
		p_asociarJuego.add(p_nuevo);
		this.validate();
		
		b_Seleccionar = new JButton("Seleccionar");
		b_Seleccionar.addActionListener(this);
		
		JPanel p_Seleccionar = new JPanel();
		p_Seleccionar.add(b_Seleccionar);
		p_asociarJuego.add(p_Seleccionar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_Seleccionar){
			String juegoSeleccionado;
			String pathGame = "";
			File dir = new File("");
			if (rB_existe.isSelected()){
				juegoSeleccionado = (String) cB_existe.getSelectedItem();
				pathGame = Main.gamesPath + "\\" + juegoSeleccionado;
				dir = new File(pathGame);
			}
			if (rB_nuevo.isSelected()){
				juegoSeleccionado = tF_nuevo.getText();
				pathGame = Main.gamesPath + "\\" + juegoSeleccionado;
				dir = new File(pathGame);
				dir.mkdir();				
				controlador.addJuego(juegoSeleccionado);
			}
			dir = new File(dir.getAbsolutePath() + "\\" + fichero.getName());
			try {
				Files.copy(fichero.toPath(), dir.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Main.checkFiles(controlador);
			this.dispose();
		}		
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == cB_existe){
			rB_existe.setSelected(true);
		}
		if (e.getSource() == tF_nuevo){
			rB_nuevo.setSelected(true);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

}
