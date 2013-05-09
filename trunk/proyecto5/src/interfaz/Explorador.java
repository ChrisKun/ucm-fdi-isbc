package interfaz;

import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Explorador extends JPanel{

	private static final long serialVersionUID = 1L;
	private String directoryPath;
	
	public Explorador(String directoryPath){
		this.directoryPath = directoryPath;
		this.setLayout(new FlowLayout());
		actualizarPanel();
	}
	
	public void setDirectoryPath(String directoryPath){
		this.directoryPath = directoryPath;
		actualizarPanel();
	}
	
	public void setFotosAMostrar(ArrayList<String> paths){
		this.removeAll();
		this.repaint();
		for (String s: paths){
			this.add(new BotonImagen(s));
		}		
	}
	
	private void actualizarPanel(){
		this.removeAll();
		this.repaint();
		File dir = new File(directoryPath);
		String[] carpetas = dir.list();
		
		for (int i=0;i<carpetas.length;i++){
			this.add(new BotonImagen(directoryPath + "\\" + carpetas[i]));
		}
	}

	public String getPathActual() {
		return directoryPath;		
	}
}
