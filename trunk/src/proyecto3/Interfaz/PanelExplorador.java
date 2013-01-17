package Interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PanelExplorador extends JPanel implements ChangeListener {

	private static final int MAX = 500;
	private static final int MIN = 0;
	private double precioMinimo;
	private double precioMaximo;
	private JSlider barraPrecioMin;
	private JSlider barraPrecioMax;
	//2 slider -> el precio marcado por el slider del precio maximo acota el precio máximo que alcanza el precio min
	/*
	 *   precio max 0|-----|50-----|200
	 *   precio min 0|--|35--|50
	 *   rango de precios -> [35-50]
	 * 
	 * 
	 */
	
	public PanelExplorador(){
		precioMinimo = 1.04;
		precioMaximo = 200.35;
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
		JPanel panel = new JPanel(new GridLayout(2,1));
		barraPrecioMax = new JSlider(JSlider.HORIZONTAL, MIN, MAX, (int)precioMaximo);
		barraPrecioMin = new JSlider(JSlider.HORIZONTAL,MIN, (int)precioMaximo, (int) precioMinimo);
		barraPrecioMax.addChangeListener(this);
		barraPrecioMax.setMajorTickSpacing(50);
		barraPrecioMax.setMinorTickSpacing(10);
		barraPrecioMax.setPaintTicks(true);
		barraPrecioMax.setPaintLabels(true);
		barraPrecioMin.setMajorTickSpacing(50);
		barraPrecioMin.setMinorTickSpacing(10);
		barraPrecioMin.setPaintTicks(true);
		barraPrecioMin.setPaintLabels(true);
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
	
	@Override
	public void stateChanged(ChangeEvent e) {
		 JSlider source = (JSlider)e.getSource();
		  barraPrecioMin.setMaximum(barraPrecioMax.getValue());
		
	}
	
}
