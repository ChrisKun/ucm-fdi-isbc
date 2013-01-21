package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GAPDataBase.GAPLoader;

public class PanelExplorador extends JPanel implements ChangeListener {

	/*
	 * - Boton para hacer consulta aplicando filtros...
	 * - Si no hay resultados, no mostrar nada
	 * - Los botones avanzar y retroceder solo estaran disponibles cuando los resultados sean mayores que X elementos 
	 *   (de momento X = 8)
	 */
	private static final int MAX = 25000;
	private static final int MIN = 0;
	private static int precioMinimo;
	private static int precioMaximo;
	private static String divisionActual;
	private static String categoriaActual;
	private static ArrayList<Integer> listaArticulos;
	private static int numArticulos = 0;
	private static int pagActual = 1;
	private static int numPaginas = 1;
	
	private JSlider barraPrecioMin;
	private JSlider barraPrecioMax;
	private JTextField etiquetaPrecioMax;
	private JTextField etiquetaPrecioMin;
	
	private JLabel mostrarPagina;
	
	private VentanaPrincipal vP; //para hacer cambios en la interfaz
	
	// A la hora de hacer consulta, se comprobara el boton seleccionado y el rango de precios
	JRadioButton botonMano;
	JRadioButton botonMaquina;
	
	JButton avanzar;
	JButton retroceder;
	
	
	
	public PanelExplorador(VentanaPrincipal vP, String division){
		this.vP = vP;
		if (divisionActual != division){
			divisionActual = division;
			pagActual = 1;
			listaArticulos = GAPLoader.extractPIdsByDivision(divisionActual);
		}
		
		precioMinimo = 10000;
		precioMaximo = 20000;
		this.setLayout(new BorderLayout());
		this.add(getPanelFiltrado(), BorderLayout.NORTH);
		this.add(getPanelArticulos(), BorderLayout.CENTER);
		this.add(getPanelAvanzarRetroceder(),BorderLayout.SOUTH);
	}
	
	private Component getPanelAvanzarRetroceder() {
		JPanel p = new JPanel();
		char slash = File.separatorChar;
		ImageIcon icon = null;
		
		p.setLayout(new GridBagLayout());
		
		avanzar = new JButton("");
		icon = new ImageIcon("src"+slash+"proyecto3"+slash+"images"+slash+"forward.png");	
		avanzar.setIcon(icon);
		avanzar.setPreferredSize(new Dimension(32,32));
		
		retroceder = new JButton();
		icon = new ImageIcon("src"+slash+"proyecto3"+slash+"images"+slash+"backward.png");	
		retroceder = new JButton("");
		retroceder.setIcon(icon);
		retroceder.setPreferredSize(new Dimension(32,32));
		
		
		
		if (pagActual == numPaginas) avanzar.setEnabled(false);
		if (pagActual == 1) retroceder.setEnabled(false);
		
		mostrarPagina = new JLabel("Página "+pagActual+" de "+numPaginas);
		
		
		avanzar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pagActual++;
				retroceder.setEnabled(true);
				vP.cambiarPanel(new PanelExplorador(vP, divisionActual));
				mostrarPagina.setText("Página "+pagActual+" de "+numPaginas);
			}
		});
		
		retroceder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pagActual--;
				avanzar.setEnabled(true);
				vP.cambiarPanel(new PanelExplorador(vP, divisionActual));
				mostrarPagina.setText("Página "+pagActual+" de "+numPaginas);
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		
		p.add(retroceder,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		
		p.add(avanzar,c);
		
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets (0,10,0,0);
		
		p.add(mostrarPagina,c);
		return p;
	}

	private JPanel getPanelFiltrado() {
		JPanel p = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		 gbc.gridx = 2;
		 gbc.gridy = 0;
		 
		 //gbc.gridwidth = 4;
		 p.add(getSubPanelFiltradoLavado(),gbc);
		
		 gbc.gridx = 0;
		 gbc.gridy = 0;
		 gbc.gridheight = 2;
		 gbc.gridwidth = 1;
		// gbc.gridwidth = 1;
		//p.add(getSubPanelFiltradoLavado());
		p.add(getSubPanelFiltradoPrecio(),gbc);
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Opciones de filtrado",TitledBorder.CENTER,TitledBorder.DEFAULT_JUSTIFICATION,new Font("Arial", Font.BOLD, 14)));
		
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		JButton j = new JButton("Aplicar Filtros");
		j.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ArrayList<Integer> filtroLavado;
				if (botonMano.isSelected()){
					filtroLavado = GAPLoader.extractPIdsByWashing("Hand wash");	
				} else {
					filtroLavado = GAPLoader.extractPIdsByWashing("Machine wash");
				}
				ArrayList<Integer> filtroPrecio = GAPLoader.extractPIdsByPriceRange(precioMinimo/100, precioMaximo/100);
				
				listaArticulos.retainAll(filtroPrecio);
				listaArticulos.retainAll(filtroLavado);
				vP.cambiarPanel(new PanelExplorador(vP, divisionActual));
			}
		});
		p.add(j,gbc);
		
		return p;
	}
	
	private JPanel getPanelArticulos() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,4));
		anyadirArticulos(p,8);
		return p;
	}
			
	private void anyadirArticulos(JPanel p, int numElem) {
		JComponent j = null;
		numArticulos = listaArticulos.size();
		numPaginas = listaArticulos.size()/numElem +1;
		//j.setIcon()
		for (int i = (pagActual-1)*numElem; i < pagActual*numElem; i++) //TODO falta saber el número de elementos que queremos mostrar y sus imagenes
		{
			if (i >= numArticulos){ //se acabaron los elementos, rellenamos con Jlabel
				j = new JLabel();
			} else {
				Integer pId = listaArticulos.get(i);
				j = new JButton(pId.toString());
				j.setName(pId.toString());
				Icon icon = new ImageIcon(GAPLoader.extractImagePathByPId(pId));
				
				Image img = ((ImageIcon) icon).getImage() ; 
				Image newimg = img.getScaledInstance( 180, 130,  java.awt.Image.SCALE_SMOOTH ) ;  
				icon = new ImageIcon( newimg );
				
				((JButton) j).setIcon(icon);
				((JButton) j).addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						JButton b = (JButton) arg0.getSource();
						//podemos coger el nombre del boton (no el texto que se muestra)
						divisionActual = "";
						vP.cambiarPanel(new PanelArticulo(vP, Integer.parseInt(b.getName())));						
					}
				});
			}
			
			j.setPreferredSize(new Dimension(200,250));
			j.setBorder(BorderFactory.createBevelBorder(0));
			p.add(j);
		}
	}

	private JPanel getSubPanelFiltradoPrecio(){
		JPanel panel = new JPanel(new GridLayout(2,2));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Filtrado de precio"));
		barraPrecioMax = new JSlider(JSlider.HORIZONTAL, MIN, MAX, precioMaximo);
		barraPrecioMin = new JSlider(JSlider.HORIZONTAL,MIN, precioMaximo, precioMinimo);
		barraPrecioMax.addChangeListener(this);
		barraPrecioMax.setPaintTicks(true);
		barraPrecioMax.setMajorTickSpacing(5000);
		barraPrecioMin.setMajorTickSpacing(5000);
		barraPrecioMax.setPaintLabels(true);
		barraPrecioMin.setPaintTicks(true);
		barraPrecioMin.setPaintLabels(true);
		barraPrecioMin.addChangeListener(this);
		panel.add(getSubPanelFiltradoPrecioMax());
		panel.add(barraPrecioMax);
		panel.add(getSubPanelFiltradoPrecioMin());
		panel.add(barraPrecioMin);
		//JSlider precio = new JSlider(JSlider.HORIZONTAL,FPS_MIN, FPS_MAX, FPS_INIT);
		//JSlider
		

		//Create the label table
		Hashtable labelTable = new Hashtable();
		labelTable.put( MIN, new JLabel(""+MIN/100));
		labelTable.put( MAX, new JLabel(""+MAX/100));
		//labelTable.put( new Integer( FPS_MAX ), new JLabel("Fast") );
		barraPrecioMax.setLabelTable( labelTable );
		
		Hashtable labelTable2 = new Hashtable();
		labelTable2.put( MIN, new JLabel(""+MIN/100));
		labelTable2.put( precioMaximo, new JLabel(""+precioMaximo/100));
		//labelTable.put( new Integer( FPS_MAX ), new JLabel("Fast") );
		barraPrecioMin.setLabelTable( labelTable2 );
		
		
		return panel;
	}
	
	private JPanel getSubPanelFiltradoLavado(){
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Filtrado por lavado"));
		
		//creacion de los botones
		botonMano = new JRadioButton("Lavado a Mano");
		botonMano.setSelected(true);
		botonMaquina = new JRadioButton("Lavado a Máquina");
		
		//grupo para los botones
		ButtonGroup group = new ButtonGroup();
		group.add(botonMano);
		group.add(botonMaquina);
		
		panel.add(botonMano);
		panel.add(botonMaquina);
		
		
		return panel;
	}
	
	private JPanel getSubPanelFiltradoPrecioMax()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JLabel l = new JLabel("Precio máx.: ");
		etiquetaPrecioMax = new JTextField(((double) precioMaximo/100)+"   ");
		etiquetaPrecioMax.setEditable(false);
		JLabel l2 = new JLabel("€");
		
		p.add(l);
		p.add(etiquetaPrecioMax);
		p.add(l2);
		return p;
	}
	
	private JPanel getSubPanelFiltradoPrecioMin()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JLabel l = new JLabel("Precio mín.: ");
		etiquetaPrecioMin = new JTextField((double) precioMinimo/100+"   ");
		etiquetaPrecioMin.setEditable(false);
		JLabel l2 = new JLabel("€");
		
		p.add(l);
		p.add(etiquetaPrecioMin);
		p.add(l2);
		return p;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		 JSlider source = (JSlider)e.getSource();
		 
		 if (source == barraPrecioMax)
		 {
			 precioMaximo = barraPrecioMax.getValue();
			 barraPrecioMin.setMaximum(precioMaximo);
			 etiquetaPrecioMax.setText(""+((double) precioMaximo/100));
			 
			 Hashtable labelTable2 = new Hashtable();
				labelTable2.put( MIN, new JLabel(""+MIN/100));
				labelTable2.put( precioMaximo, new JLabel(""+((double)precioMaximo/100)));
				//labelTable.put( new Integer( FPS_MAX ), new JLabel("Fast") );
				barraPrecioMin.setLabelTable( labelTable2 );
			 
			 if (precioMaximo < precioMinimo)
			 {
				 precioMinimo = precioMaximo;
				 etiquetaPrecioMin.setText(""+((double) precioMinimo/100));
			 }
		 }
		 else if (source == barraPrecioMin)
		 {
			precioMinimo = barraPrecioMin.getValue();
			//barraPrecioMin.setMaximum(precioMinimo);
			etiquetaPrecioMin.setText(""+((double) precioMinimo/100));
			
			Hashtable labelTable2 = new Hashtable();
			labelTable2.put( MIN, new JLabel(""+MIN/100));
			labelTable2.put( precioMaximo, new JLabel(""+((double)precioMaximo/100)));
			//labelTable.put( new Integer( FPS_MAX ), new JLabel("Fast") );
			barraPrecioMin.setLabelTable( labelTable2 );
		 }
	}
	
}
