package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 * Este Panel va a ser la vista en la que ofrecemos la informaci�n de un articulo
 * en particular, junto a sus opciones y las opiniones de otros usuarios.
 * Tambien viene acompa�ado de un recomendador en el que mostramos prendas relacionadas
 * @author Xavi
 *
 */
public class PanelArticulo extends JPanel{

	private JLabel imagen; //imagen que se muestra en el inicio. Atributo porque ha de ser modificable.
	
	/**
	 * En esta clase vamos a construir un panel que corresponde a la distribucion
	 * seguida en el archivo "VentanaArticulo.png" de la memoria.
	 */
	public PanelArticulo(){
		this.setLayout(new BorderLayout());
		this.add(getPanelRecomendador(), BorderLayout.SOUTH);
		this.add(getPanelReviews(), BorderLayout.EAST); // TODO Pendiente de aprobaci�n
		
		this.add(getPanelCentral());
	}

	/**
	 * En este panel vamos a mostrar la imagen y las propiedades del producto.
	 * Es un JPanel organizado con un GridBagLayout
	 * @return
	 */
	private JPanel getPanelCentral() {
		// TODO Auto-generated method stub
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints(); // para las dimensiones de las celdas del gridBag
				
		gbc.weightx = 0.5; //aspecto que debe mantener el objeto con respecto a la columna en la que se encuentra
		gbc.weighty = 0.5; //aspecto que debe mantener el objeto con respecto a la fila en la que se encuentra
		gbc.gridwidth = 3; // ancho en celdas que ocupar� el componente
		gbc.gridheight = 1; // largo en celdas que ocupara el componente
		gbc.gridx = 2; //celda en la que se colocar� el componente
		gbc.gridy = 0;
		gbc.insets = new Insets(1,1,1,1); //bordes de cada componente new Insets (arriba,izquierda,abajo,derecha);
		p.add(getZonaPropiedades(),gbc);
		
		gbc.weightx = 0.9; //aspecto que debe mantener el objeto con respecto a la columna en la que se encuentra
		gbc.weighty = 0.9; //aspecto que debe mantener el objeto con respecto a la fila en la que se encuentra
		gbc.gridwidth = 1; // ancho en celdas que ocupar� el componente
		gbc.gridheight = 2; // largo en celdas que ocupara el componente
		gbc.gridx = 0; //celda en la que se colocar� el componente
		gbc.gridy = 0;
		gbc.insets = new Insets(1,1,1,1); //bordes de cada componente new Insets (arriba,izquierda,abajo,derecha);
		p.add(getZonaImagen() , gbc );
		
		return p;
	}

	/**
	 * En este panel mostramos las propiedades del articulo que estamos mostrando
	 * @return El panel con las propiedades
	 */
	private JScrollPane getZonaPropiedades() {
		JTextArea textoReview = new JTextArea("");
		textoReview.setLineWrap(true);
		JScrollPane panelReview = new JScrollPane(textoReview);
		panelReview.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textoReview.setText("Propiedades");
		textoReview.setEditable(false);
		return panelReview;
	}

	/**
	 * En este panel mostraremos la imagen del articulo seleccionado
	 * @return El panel con la imagen
	 */
	private JPanel getZonaImagen() {
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

	/**
	 * En este panel vamos a mostrar los productos que recomendamos.
	 * En caso de no caber en la pantalla usariamos un scroll horizontal
	 * @return El panel del recomendador de prendas
	 */
	private JScrollPane getPanelRecomendador() {
		// TODO Panel con los articulos que recomendamos
		JTextArea textoReview = new JTextArea("",5,50);
		textoReview.setLineWrap(true);
		JScrollPane panelReview = new JScrollPane(textoReview);
		panelReview.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textoReview.setText("Casos rescatados de la Base de Casos");
		textoReview.setEditable(false);
		return panelReview;
	}
	
	/**
	 * En este panel, pendiente de revision, mostrariamos los reviews de la gente, y la
	 * media de ratings que tiene la prenda.
	 * @return El panel con los ratings y reviews
	 */
	private JScrollPane getPanelReviews() {
		// TODO Panel con las opiniones y valoraciones
		JTextArea textoReview = new JTextArea("");
		textoReview.setLineWrap(true);
		JScrollPane panelReview = new JScrollPane(textoReview);
		panelReview.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textoReview.setText("Reviews, pendiente de revision");
		textoReview.setEditable(false);
		return panelReview;
	}
}
