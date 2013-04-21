package interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import clasificador.Main;

public class NavegadorJuegos extends JPanel {

	private static final long serialVersionUID = 1L;

	public NavegadorJuegos(){
		//this.setLayout(new FlowLayout());
		
		//Buscar en la carpeta donde estan los juegos y obtener la lista de
		// los juegos que hay (Uno por carpeta).
		File dir = new File(Main.pathGames);
		String[] carpetas = dir.list();
		//Preparar un botón para cada una de las carpetas
		
		JPanel tmp;
		for (int i=0;i<carpetas.length;i++){
			tmp = getBotonCarpeta(carpetas[i]);
			this.add(tmp);
		}
	}
	
	private JPanel getBotonCarpeta(String nombreCarpeta){
		JPanel panel = new JPanel();
		//panel.setLayout(new GridLayout(2, 1));
		panel.setLayout(new BorderLayout(10,10));
		panel.setPreferredSize(new Dimension(300, 200));
		JButton b_carpeta = new JButton();
		b_carpeta.setText(nombreCarpeta);
		b_carpeta.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton boton = (JButton) e.getSource();
				String s = Main.pathGames + "\\" + boton.getText();
				JOptionPane.showMessageDialog(null, s);				
			}
		});
		
		// Coger una imagen de la carpeta y ponerla de icono en el botón
		File dir = new File(Main.pathGames + "\\" + nombreCarpeta);
		String[] fotos = dir.list();
		
		Random rand = new Random();
		String fotoRandom = fotos[rand.nextInt(fotos.length)];
		ImageIcon foto = new ImageIcon(Main.pathGames + "\\" + nombreCarpeta + "\\" + fotoRandom);
		
		b_carpeta.setIcon(foto);
		// Añadir debajo el nombre de la carpeta
		
		panel.add(b_carpeta,BorderLayout.CENTER);
		
		panel.add(new JLabel(nombreCarpeta, SwingConstants.CENTER), BorderLayout.SOUTH);
		return panel;
	}
	
}
