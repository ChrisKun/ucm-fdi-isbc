package Interfaz;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Este Panel va a ser la vista en la que ofrecemos la información de un articulo
 * en particular, junto a sus opciones y las opiniones de otros usuarios.
 * Tambien viene acompañado de un recomendador en el que mostramos prendas relacionadas
 * @author Xavi
 *
 */
public class PanelArticulo extends JPanel{

	public PanelArticulo(){
		this.setLayout(new BorderLayout());
		this.add(getPanelRecomendador(), BorderLayout.SOUTH);
		this.add(getPanelReviews(), BorderLayout.EAST);
	}

	/**
	 * En este panel vamos a mostrar los productos que recomendamos.
	 * En caso de no caber en la pantalla usariamos un scroll horizontal
	 * @return
	 */
	private JPanel getPanelRecomendador() {
		// TODO Panel con los articulos que recomendamos
		JPanel p = new JPanel();

		return p;
	}
	
	private JPanel getPanelReviews() {
		// TODO Panel con las opiniones y valoraciones
		JPanel p = new JPanel();
		
		return p;
	}
}
