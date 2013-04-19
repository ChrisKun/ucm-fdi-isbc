package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class PanelVentana extends JPanel {

	private Controlador controlador;
	
	private String nombreFotoActual;
	private JLabel fotoActual;
	
	private static final long serialVersionUID = 1L;
	
	public PanelVentana(Controlador controlador){
		this.controlador = controlador;
		this.setLayout(new BorderLayout());
		//fotoActual = new JLabel();
		setFotoActual("");
		this.add(fotoActual, BorderLayout.CENTER);
		this.add(new JLabel("Añadir información, etiquetar, etc."), BorderLayout.SOUTH);
	}

	public void setFotoActual(String nuevaFoto){
		nombreFotoActual = nuevaFoto;		
		
        Border blackline = BorderFactory.createEtchedBorder();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String pathFile = "proyecto5\\fotos\\zelda\\Battle-for-the-Triforce-III-2.jpg";
        ImageIcon foto = new ImageIcon(pathFile);
        fotoActual = new JLabel(new ImageIcon(pathFile));
        fotoActual.setPreferredSize(new Dimension(700,850));
        fotoActual.setBorder(blackline);
		
		this.validate();
	}
}
