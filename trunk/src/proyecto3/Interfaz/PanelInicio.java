package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import sistema.SistemaTienda;

import GAPDataBase.GAPLoader;
import GAPDataBase.ImageInfo;
import GAPDataBase.Product;

public class PanelInicio extends JPanel{
	
	private static final long serialVersionUID = 1L;
	//private JScrollPane panelReview;
	private JTextArea textoReview; // Texto de la parte de la review. Atributo porque ha de ser modificable.
	private JLabel imagen; //imagen que se muestra en el inicio. Atributo porque ha de ser modificable.
	private static Integer pIdActual;
	private static int posPIdActual = 0;
	private static ArrayList<Integer> pIdActuales;
	private VentanaPrincipal vP; //referencia a la ventana principal para modificar el panel
	
	public PanelInicio(VentanaPrincipal vP)
	{
		this.vP = vP;
		
		pIdActual = 794756;
		pIdActuales = new ArrayList<Integer>();
		try {
			if (SistemaTienda.usuarioActual == null){
				pIdActuales = SistemaTienda.recomendador.recomendadosPorMasComprados();
			} else {
				pIdActuales = SistemaTienda.recomendador.recomendadosPorUsuario(SistemaTienda.usuarioActual);
			}
			if (pIdActuales.size() == 0){
				pIdActuales = SistemaTienda.recomendador.recomendadosPorProducto(pIdActual);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pIdActuales.add(pIdActual);
		}
		pIdActual = pIdActuales.get(0);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(); // para las dimensiones de las celdas del gridBag
		gbc.weightx = 0.5; //aspecto que debe mantener el objeto con respecto a la columna en la que se encuentra
		gbc.weighty = 0.5; //aspecto que debe mantener el objeto con respecto a la fila en la que se encuentra
		gbc.gridwidth = 3; // ancho en celdas que ocupará el componente
		gbc.gridheight = 1; // largo en celdas que ocupara el componente
		gbc.gridx = 0; //celda en la que se colocará el componente
		gbc.gridy = 2;
		gbc.insets = new Insets(1,1,1,1); //bordes de cada componente new Insets (arriba,izquierda,abajo,derecha);
		//this.add(getZonaImagen() , gbc );
		this.add(getZonaReview(),gbc);
		
		gbc.weightx = 0.9; //aspecto que debe mantener el objeto con respecto a la columna en la que se encuentra
		gbc.weighty = 0.9; //aspecto que debe mantener el objeto con respecto a la fila en la que se encuentra
		gbc.gridwidth = 1; // ancho en celdas que ocupará el componente
		gbc.gridheight = 2; // largo en celdas que ocupara el componente
		gbc.gridx = 2; //celda en la que se colocará el componente
		gbc.gridy = 0;
		gbc.insets = new Insets(1,1,1,1); //bordes de cada componente new Insets (arriba,izquierda,abajo,derecha);
		this.add(getZonaImagen() , gbc );
		
		ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	posPIdActual++;
            	if (posPIdActual == pIdActuales.size()) posPIdActual = 0;
            	pIdActual = pIdActuales.get(posPIdActual);
            	//TODO validate()
            }
		};
        Timer timer = new Timer( 3000 , taskPerformer);
        timer.setRepeats(true);
        timer.start();

		
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
		String productName = GAPLoader.extractInfoProductById(pIdActual).getName();
		String productOverview = GAPLoader.extractInfoProductById(pIdActual).getOverview();
		textoReview.setText(productName+": "+productOverview);
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
		String pathFile = GAPLoader.extractImagePathByPId(pIdActual);
		imagen = new JLabel(new ImageIcon(pathFile));
		imagen.setPreferredSize(new Dimension(200,250));
		pImagen.add(imagen);
		JButton botonIrArticulo = new JButton("IrArticulo");
		botonIrArticulo.setText("Ir al Articulo");
		botonIrArticulo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				vP.cambiarPanel(new PanelArticulo(vP, pIdActual));
			}
		});
		pImagen.add(botonIrArticulo, BorderLayout.EAST);
		return pImagen;
	}
}
