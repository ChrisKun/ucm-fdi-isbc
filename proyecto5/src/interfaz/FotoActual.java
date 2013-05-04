package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class FotoActual extends JPanel {

	private Controlador controlador;
	
	private String pathFotoActual;
	private JLabel fotoActual;
	
	private static final long serialVersionUID = 1L;
	
	public FotoActual() {
		// TODO Auto-generated constructor stub
		
	}
	
	public FotoActual(Controlador controlador){
		this.controlador = controlador;
		this.setLayout(new BorderLayout());
		//fotoActual = new JLabel();
		setFotoActual("");
		
	}

	

	public void setFotoActual(String nuevaFoto){
		pathFotoActual = nuevaFoto;
		actualizarPanel();
	}
	
	private void actualizarPanel(){
		this.removeAll();
		
		Border blackline = BorderFactory.createEtchedBorder();
        ImageIcon foto = new ImageIcon(pathFotoActual);
        fotoActual = new JLabel(foto);
        fotoActual.setPreferredSize(new Dimension(700,850));
        fotoActual.setBorder(blackline);
        this.add(fotoActual, BorderLayout.CENTER);
		this.add(new JLabel("Añadir información, etiquetar, etc."), BorderLayout.SOUTH);
	}
}
