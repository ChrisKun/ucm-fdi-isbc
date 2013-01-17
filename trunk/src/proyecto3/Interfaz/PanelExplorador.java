package Interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PanelExplorador extends JPanel implements ChangeListener {

	private static final int MAX = 25000;
	private static final int MIN = 0;
	private int precioMinimo;
	private int precioMaximo;
	private JSlider barraPrecioMin;
	private JSlider barraPrecioMax;
	private JTextField etiquetaPrecioMax;
	private JTextField etiquetaPrecioMin;
	
	//2 slider -> el precio marcado por el slider del precio maximo acota el precio máximo que alcanza el precio min
	/*
	 *   precio max 0|-----|50-----|200
	 *   precio min 0|--|35--|50
	 *   rango de precios -> [35-50]
	 * 
	 * 
	 */
	
	public PanelExplorador(){
		precioMinimo = 10000;
		precioMaximo = 20000;
		this.setLayout(new BorderLayout());
		this.add(getPanelFiltrado(), BorderLayout.NORTH);
		this.add(getPanelArticulos(), BorderLayout.CENTER);
	}

	private JPanel getPanelFiltrado() {
		JPanel p = new JPanel(new GridLayout(1,2));
		p.add(getSubPanelFiltradoLavado());
		p.add(getSubPanelFiltradoPrecio());
		return p;
	}
	
	private JPanel getPanelArticulos() {
		JPanel p = new JPanel();
		return p;
	}
			
	private JPanel getSubPanelFiltradoPrecio(){
		JPanel panel = new JPanel(new GridLayout(2,2));
		barraPrecioMax = new JSlider(JSlider.HORIZONTAL, MIN, MAX, precioMaximo);
		barraPrecioMin = new JSlider(JSlider.HORIZONTAL,MIN, precioMaximo, precioMinimo);
		barraPrecioMax.addChangeListener(this);
		barraPrecioMax.setPaintTicks(true);
		barraPrecioMax.setPaintLabels(true);
		barraPrecioMin.setPaintTicks(true);
		barraPrecioMin.setPaintLabels(true);
		panel.add(getSubPanelFiltradoPrecioMax());
		panel.add(barraPrecioMax);
		panel.add(barraPrecioMin);
		//JSlider precio = new JSlider(JSlider.HORIZONTAL,FPS_MIN, FPS_MAX, FPS_INIT);
		//JSlider
		return panel;
	}
	
	private JPanel getSubPanelFiltradoLavado(){
		JPanel panel = new JPanel();
		return panel;
	}
	
	private JPanel getSubPanelFiltradoPrecioMax()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JLabel l = new JLabel("Precio máximo: ");
		etiquetaPrecioMax = new JTextField(""+precioMaximo);
		etiquetaPrecioMax.setEditable(false);
		JLabel l2 = new JLabel("€");
		
		p.add(l);
		p.add(etiquetaPrecioMax);
		p.add(l2);
		return p;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		 JSlider source = (JSlider)e.getSource();
		 precioMaximo = barraPrecioMax.getValue();
		  barraPrecioMin.setMaximum(precioMaximo);
		  etiquetaPrecioMax.setText(""+((double) precioMaximo/100));
		
	}
	
}
