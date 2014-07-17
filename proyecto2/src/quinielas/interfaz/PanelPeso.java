package src.quinielas.interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PanelPeso extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 34967247656435954L;

	private String nombre;
	private JSlider slider;
	private JTextField tF_Peso;
	private double peso;
	
	public PanelPeso(String nombre){
		super();
		this.nombre = nombre;
		
		/* Creamos el panel */
		JPanel subPanel = new JPanel(); //subPanel con la información del peso y el JSlider
		this.setLayout(new BorderLayout());
		subPanel.setLayout(new GridLayout(1,2));
		
		/* Añadimos la etiqueta con el nombre del peso */
		this.add(new JLabel(nombre,JLabel.CENTER),BorderLayout.NORTH);
		
		/* Añadimos el JSlider con los datos correspondientes */
		//slider = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int)(datosPesos[quePeso]*1000));
		slider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
		slider.setPaintLabels(true); 	// Pintar las etiquetas
		slider.setPaintTrack(true); 	// Pintar por donde va el Slider
		
		/* Asociamos los valores 0 y 1 a los valores 0 y 1000 de la JSlider para darle más precisión */
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 0 ), new JLabel("0") );
		labelTable.put( new Integer( 1000 ), new JLabel("1") );
		slider.setLabelTable( labelTable );
		/* Listener */
		slider.addChangeListener(this);
		/* Añadimos el Slider */
		subPanel.add(slider);
		/* Creamos los campos que contienen el valor númerico del peso */
		//tF_Peso = new JTextField(""+datosPesos[quePeso]); // Datos iniciales
		tF_Peso = new JTextField(""+0); // Datos iniciales
		tF_Peso.setEditable(false);
		subPanel.add(tF_Peso);
		this.add(subPanel,BorderLayout.CENTER);
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		double d;
		if (source == slider){
			d = source.getValue();
			peso = d/1000;
			tF_Peso.setText(""+peso);
			
		}
	}
}
