package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import sistema.SistemaTienda;

import GAPDataBase.GAPLoader;
import GAPDataBase.Product;

/**
 * Este Panel va a ser la vista en la que ofrecemos la información de un articulo
 * en particular, junto a sus opciones y las opiniones de otros usuarios.
 * Tambien viene acompañado de un recomendador en el que mostramos prendas relacionadas
 * @author Xavi
 *
 */
public class PanelArticulo extends JPanel {

	private Integer pIdActual;
	private JLabel imagen; //imagen que se muestra en el inicio. Atributo porque ha de ser modificable.
	private VentanaPrincipal vP; //referencia a la ventana principal para poder modificar el panel
	
	/**
	 * En esta clase vamos a construir un panel que corresponde a la distribucion
	 * seguida en el archivo "VentanaArticulo.png" de la memoria.
	 */
	public PanelArticulo(Integer pIdActual, VentanaPrincipal vP){
		//Al crear este panel necesitamos llamar al recomendador automaticamente
		//	y mostrar los articulos recuperados
		this.vP = vP;
		this.pIdActual = pIdActual;
		this.setLayout(new BorderLayout());
		this.add(getPanelRecomendador(), BorderLayout.SOUTH);
		//this.add(getPanelReviews(), BorderLayout.EAST); // TODO Pendiente de aprobación
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
		gbc.gridwidth = 3; // ancho en celdas que ocupará el componente
		gbc.gridheight = 1; // largo en celdas que ocupara el componente
		gbc.gridx = 2; //celda en la que se colocará el componente
		gbc.gridy = 0;
		gbc.insets = new Insets(1,1,1,1); //bordes de cada componente new Insets (arriba,izquierda,abajo,derecha);
		p.add(getZonaPropiedades(),gbc);
		
		gbc.weightx = 0.9; //aspecto que debe mantener el objeto con respecto a la columna en la que se encuentra
		gbc.weighty = 0.9; //aspecto que debe mantener el objeto con respecto a la fila en la que se encuentra
		gbc.gridwidth = 1; // ancho en celdas que ocupará el componente
		gbc.gridheight = 2; // largo en celdas que ocupara el componente
		gbc.gridx = 0; //celda en la que se colocará el componente
		gbc.gridy = 0;
		gbc.insets = new Insets(1,1,1,1); //bordes de cada componente new Insets (arriba,izquierda,abajo,derecha);
		p.add(getZonaImagen() , gbc );
		
		return p;
	}

	/**
	 * En este panel mostramos las propiedades del articulo que estamos mostrando
	 * @return El panel con las propiedades
	 */
	private JPanel getZonaPropiedades() {
		
		JPanel panel = new JPanel();
		String propiedades = "";
		JTextArea textoReview = new JTextArea("",6,20);
		textoReview.setLineWrap(true);
		JScrollPane panelReview = new JScrollPane(textoReview);
		panelReview.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Detalles del producto",TitledBorder.CENTER,TitledBorder.DEFAULT_JUSTIFICATION,new Font("Arial", Font.BOLD, 14)));
		panel.add(textoReview);
		Product p = GAPLoader.extractInfoProductById(pIdActual);
		propiedades = propiedades + "Nombre: " + p.getName()+"\n";
		propiedades = propiedades + "Materiales: " + p.getComposition()+"\n";
		propiedades = propiedades + "Lavado: " + p.getWashing()+"\n";
		propiedades = propiedades + "Precio: " + p.getPrice()+"\n";
		textoReview.setText(propiedades);
		textoReview.setEditable(false);
		return panel;
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
		String pathFile = GAPLoader.extractImagePathByPId(pIdActual);
		imagen = new JLabel(new ImageIcon(pathFile));
		imagen.setPreferredSize(new Dimension(200,250));
		pImagen.add(imagen);
		return pImagen;
	}

	/**
	 * En este panel vamos a mostrar los productos que recomendamos.
	 * En caso de no caber en la pantalla usariamos un scroll horizontal
	 * @return El panel del recomendador de prendas
	 */
	private JPanel getPanelRecomendador() {
		// TODO Panel con los articulos que recomendamos
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		ArrayList<Integer> recomendados = new ArrayList<Integer>();
		try {
			recomendados = SistemaTienda.recomendador.recomendadosPorProducto(pIdActual);
		} catch (Exception e) {
			// TODO Que hacer si no recomienda nada?
			e.printStackTrace();		
		}
		
		for (Integer n: recomendados){
			p.add(getBoton(n));
		}
		/*
		JTextArea textoReview = new JTextArea("",5,50);
		textoReview.setLineWrap(true);
		JScrollPane panelReview = new JScrollPane(textoReview);
		panelReview.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textoReview.setText("Casos rescatados de la Base de Casos");
		textoReview.setEditable(false);
		*/
		return p;
	}
	
	/**
	 * Devuelve un botón para la interfaz, ya con el action listener incluido
	 * @param str
	 * @return
	 */
	private JButton getBoton(Integer pId)
	{
		JButton jb = new JButton();
		jb.setName(pId.toString());
		//jb.setText(pId.toString()); // Cambiado por la imagen
		//cambiamos el nombre, que es lo que se hara en el action listener
		jb.setName(pId.toString());
		Icon icon = new ImageIcon(GAPLoader.extractImagePathByPId(pId));
		
		Image img = ((ImageIcon) icon).getImage() ;  
		Image newimg = img.getScaledInstance( 75, 75,  java.awt.Image.SCALE_SMOOTH ) ;  
		icon = new ImageIcon( newimg );
		
		jb.setIcon(icon);
		jb.setMaximumSize(new Dimension(20, 20));
		//action Listener
		jb.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JButton b = (JButton) arg0.getSource();
				//podemos coger el nombre del boton (no el texto que se muestra)
				vP.cambiarPanel(new PanelArticulo(Integer.parseInt(b.getName()),vP));
			}
			
		}); //cambiado para que pille el pId
		return jb;
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
