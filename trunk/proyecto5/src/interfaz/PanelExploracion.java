package interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;

import clasificador.Main;

public class PanelExploracion extends JPanel{

	private static final long serialVersionUID = 1L;
	public static final String panelIntro = "Introduccion";
	public static final String panelExplorador = "Explorador de Imagenes";
	public static final String panelFoto = "Informacion de la foto actual";
	
	private JPanel panelIntercambiable;
	private PanelOntoTree panelOntoTree;
	
	private Explorador explorador;
	private FotoActual fotoActual;
	
	public PanelExploracion(Controlador controlador){
		explorador = new Explorador(Main.gamesPath);
		fotoActual = new FotoActual(controlador);
		
		panelIntercambiable = new JPanel();
		panelIntercambiable.setLayout(new CardLayout(10,10));
		panelIntercambiable.add(explorador, panelExplorador);
		panelIntercambiable.add(fotoActual, panelFoto);
		
		this.setLayout(new BorderLayout());
		this.add(panelIntercambiable,BorderLayout.CENTER);
		
		panelOntoTree = new PanelOntoTree(controlador);
		this.add(panelOntoTree, BorderLayout.WEST);
	}
	
	public void cambiarPanel(String nuevoPanel){
		CardLayout cl = (CardLayout) panelIntercambiable.getLayout();		
		cl.show(panelIntercambiable, nuevoPanel);
		panelIntercambiable.validate();
	}

	public void setPathExplorador(String pathFile){
		explorador.setDirectoryPath(pathFile);
	}

	public void setPathFoto(String pathFile) {
		fotoActual.actualizarFoto(pathFile);		
	}
}
