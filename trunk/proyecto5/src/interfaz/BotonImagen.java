package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import clasificador.Main;

public class BotonImagen extends JButton {

	public String pathFile;
	
	public BotonImagen(String pathFile){
		super();
		this.pathFile = pathFile;
		
		this.setPreferredSize(new Dimension(300, 200));
		
		// Coger una imagen de la carpeta y ponerla de icono en el botón
		File dir = new File(pathFile);
		ImageIcon foto;
		if (dir.isDirectory()) {
			String[] fotos = dir.list();
			Random rand = new Random();
			String fotoRandom = fotos[rand.nextInt(fotos.length)];
			foto = new ImageIcon(pathFile + "\\" + fotoRandom);			
		} else if (dir.isFile()  /* Check file extension*/) {
			foto = new ImageIcon(pathFile);			
		} else {
			foto = new ImageIcon(Main.rootPath + "\\" + "nok.png");
		}		
		
		Image img = foto.getImage();
		int height = img.getHeight(null);
		int width = img.getWidth(null);
		float propHeight = height/200f; 
		float propWidth = width/300f;
		int newHeight, newWidth;
		if (propHeight > propWidth){
			newHeight = (int) (height/propHeight);
			newWidth = (int) (width/propHeight);
		} else {
			newHeight = (int) (height/propWidth);
			newWidth = (int) (width/propWidth);
		}
		
		Image newimg = img.getScaledInstance( newWidth, newHeight,  java.awt.Image.SCALE_DEFAULT ) ; 

		foto = new ImageIcon( newimg );
		
		this.setIcon(foto);
		
		Border compound = BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), 
				BorderFactory.createLoweredBevelBorder());
		this.setBorder(compound);
	

		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BotonImagen boton = (BotonImagen) arg0.getSource();
				File dir = new File(boton.pathFile);
				if (dir.isDirectory()){
					//Cambiar a vista Explorador(pathFile)
					Main.vista.activaPanelExplorador(boton.pathFile);
				}else if (dir.isFile()){
					//Cambiar a vista FotoActual(pathFile)
					Main.vista.activaPanelFoto(boton.pathFile);
				}
			}
		});
	}
}
