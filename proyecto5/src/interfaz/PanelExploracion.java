package interfaz;

import java.awt.CardLayout;

import javax.swing.JPanel;

import clasificador.Main;

public class PanelExploracion extends JPanel{

	private static final long serialVersionUID = 1L;
	public static final String panelIntro = "Introduccion";
	public static final String panelExplorador = "Explorador de Imagenes";
	public static final String panelFoto = "Informacion de la foto actual";
	private Explorador explorador;
	private FotoActual fotoActual;
	
	public PanelExploracion(Controlador controlador){
		explorador = new Explorador(Main.gamesPath);
		fotoActual = new FotoActual(controlador);
		this.setLayout(new CardLayout(10,10));
		this.add(explorador, panelExplorador);
		this.add(fotoActual, panelFoto);		
	}
	
	public void cambiarPanel(String nuevoPanel){
		CardLayout cl = (CardLayout) this.getLayout();		
		cl.show(this, nuevoPanel);
		this.validate();
	}

	public void setPathExplorador(String pathFile){
		explorador.setDirectoryPath(pathFile);
	}

	public void setPathFoto(String pathFile) {
		fotoActual.actualizarFoto(pathFile);		
	}
}
