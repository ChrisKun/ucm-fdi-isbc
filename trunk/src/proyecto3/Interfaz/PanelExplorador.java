package Interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;

public class PanelExplorador extends JPanel{

	private double precioMinimo;
	private double precioMaximo;
	//2 slider -> el precio marcado por el slider del precio maximo acota el precio máximo que alcanza el precio min
	/*
	 *   precio max 0|-----|50-----|200
	 *   precio min 0|--|35--|50
	 *   rango de precios -> [35-50]
	 * 
	 * 
	 */
	
	public PanelExplorador(){
		
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
		JPanel panel = new JPanel();
		//JSlider precio = new JSlider(JSlider.HORIZONTAL,FPS_MIN, FPS_MAX, FPS_INIT);
		//JSlider
		return panel;
	}
	
	private JPanel getSubPanelFiltradoLavado(){
		JPanel panel = new JPanel();
		return panel;
	}
	
	
}
