package Interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class PanelInicio extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private JScrollPane panelReview;
	private JTextArea textoReview; // Texto de la parte de la review. Atributo porque ha de ser modificable.
	private JLabel imagen; //imagen que se muestra en el inicio. Atributo porque ha de ser modificable.
	
	public PanelInicio()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(); // para las dimensiones de las celdas del gridBag
		gbc.weightx = 0.5; //aspecto que debe mantener el objeto con respecto a la columna en la que se encuentra
		gbc.weighty = 0.5; //aspecto que debe mantener el objeto con respecto a la fila en la que se encuentra
		gbc.gridwidth = 3; // ancho en celdas que ocupar� el componente
		gbc.gridheight = 1; // largo en celdas que ocupara el componente
		gbc.gridx = 0; //celda en la que se colocar� el componente
		gbc.gridy = 2;
		gbc.insets = new Insets(1,1,1,1); //bordes de cada componente new Insets (arriba,izquierda,abajo,derecha);
		//this.add(getZonaImagen() , gbc );
		this.add(getZonaReview(),gbc);
		
		gbc.weightx = 0.9; //aspecto que debe mantener el objeto con respecto a la columna en la que se encuentra
		gbc.weighty = 0.9; //aspecto que debe mantener el objeto con respecto a la fila en la que se encuentra
		gbc.gridwidth = 1; // ancho en celdas que ocupar� el componente
		gbc.gridheight = 2; // largo en celdas que ocupara el componente
		gbc.gridx = 2; //celda en la que se colocar� el componente
		gbc.gridy = 0;
		gbc.insets = new Insets(1,1,1,1); //bordes de cada componente new Insets (arriba,izquierda,abajo,derecha);
		this.add(getZonaImagen() , gbc );
		/*
		 * NOTA: No deber�a ser un lugar donde mostrar informaci�n del producto?
		JButton jb = new JButton();
		jb.setText("Review");
		jb.setSize(20, 10);
		this.add(jb, BorderLayout.SOUTH);
		*/
	}
	/**
	 * Construye el panel en donde estar� el texto de la review. Tiene Scroll por si es un texto largo.
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
		JPanel pImagen = new JPanel();
		Border blackline = BorderFactory.createEtchedBorder();
		pImagen.setBorder(blackline);
		pImagen.getBorder();
		pImagen.setBackground(Color.white);
		
		imagen = new JLabel("imagen");
		imagen.setPreferredSize(new Dimension(200,250));
		pImagen.add(imagen);
		return pImagen;
	}
}
