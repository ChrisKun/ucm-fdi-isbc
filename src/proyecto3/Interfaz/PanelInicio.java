package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelInicio extends JPanel{
	
	//private JScrollPane panelReview;
	private JTextArea textoReview; // Texto de la parte de la review. Atributo porque ha de ser modificable.
	private JLabel imagen; //imagen que se muestra en el inicio. Atributo porque ha de ser modificable.
	
	public PanelInicio()
	{
		this.setLayout(new BorderLayout());
		this.add(getZonaImagen() , BorderLayout.CENTER);
		this.add(getZonaReview(), BorderLayout.SOUTH);
		/*
		 * NOTA: No debería ser un lugar donde mostrar información del producto?
		JButton jb = new JButton();
		jb.setText("Review");
		jb.setSize(20, 10);
		this.add(jb, BorderLayout.SOUTH);
		*/
	}
	/**
	 * Construye el panel en donde estará el texto de la review. Tiene Scroll por si es un texto largo.
	 * @return JScrollPane
	 */
	private JScrollPane getZonaReview()
	{
		textoReview = new JTextArea("",5,50);
		textoReview.setLineWrap(true);
		JScrollPane panelReview = new JScrollPane(textoReview);
		panelReview.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textoReview.setText("Coger de la base de datos...");
		textoReview.setEditable(false);
		return panelReview;
	}
	
	private JPanel getZonaImagen() 
	{
		imagen = new JLabel();
	    File file = new File("src/proyecto3/images/help.png");
        BufferedImage read;
		try {
			read = ImageIO.read(file);
		
		
        Image scaledInstance = read.getScaledInstance(200, 100, Image.SCALE_DEFAULT);
        imagen.setIcon(new ImageIcon(scaledInstance));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		JPanel panelImagen = new JPanel();
		panelImagen.setBackground(Color.white);
		panelImagen.setPreferredSize(new Dimension(20,30));
		panelImagen.setSize(new Dimension(20,30));
		
		panelImagen.add(imagen, BorderLayout.CENTER);
		return panelImagen;
	}
}
