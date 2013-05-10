package interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controlador.Controlador;


import clasificador.Main;

public class PanelExploracion extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	public static final String panelExplorador = "Explorador de Imagenes";
	public static final String panelFoto = "Informacion de la foto actual";
	
	private String panelActual = panelExplorador;
	
	private JPanel panelIntercambiable;
	private PanelOntoTree panelOntoTree;
	
	private Explorador explorador;
	private FotoActual fotoActual;
	
	private JPanel panelBotones;
	private JButton b_Atras;
	private JButton b_Inicio;
	
	public PanelExploracion(Controlador controlador){
		explorador = new Explorador(Main.gamesPath);
		explorador.setLayout(new GridLayout(0,3));
		//explorador.setLayout(new )
		JScrollPane exploradorScroll = new JScrollPane(explorador,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		fotoActual = new FotoActual(controlador);
		
		panelIntercambiable = new JPanel();
		panelIntercambiable.setLayout(new CardLayout(10,10));
		//panelIntercambiable.add(explorador, panelExplorador);
		panelIntercambiable.add(exploradorScroll, panelExplorador);
		panelIntercambiable.add(fotoActual, panelFoto);
		
		this.setLayout(new BorderLayout());
		this.add(panelIntercambiable,BorderLayout.CENTER);
		
		panelOntoTree = new PanelOntoTree(controlador);
		this.add(panelOntoTree, BorderLayout.WEST);
		
		panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
		b_Atras = new JButton("Atras");
		b_Atras.addActionListener(this);
		panelBotones.add(b_Atras);
		b_Inicio = new JButton("Inicio");
		b_Inicio.addActionListener(this);
		panelBotones.add(b_Inicio);		
		this.add(panelBotones, BorderLayout.SOUTH);
	}
	
	public void cambiarPanel(String nuevoPanel){
		CardLayout cl = (CardLayout) panelIntercambiable.getLayout();
		panelActual = nuevoPanel;
		cl.show(panelIntercambiable, nuevoPanel);
		panelIntercambiable.validate();
	}

	public void setPathExplorador(String pathFile){
		explorador.setDirectoryPath(pathFile);
	}

	public void setPathExplorador(ArrayList<String> paths){
		explorador.setFotosAMostrar(paths);
	}
	
	public void setPathFoto(String pathFile) {
		fotoActual.actualizarFoto(pathFile);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_Atras) {
			if (panelActual.equals(panelExplorador)){
				String newPath = explorador.getPathActual();
				if (newPath.equals(Main.gamesPath)) return;
				newPath = newPath.substring(0, newPath.lastIndexOf('\\'));
				setPathExplorador(newPath);
				cambiarPanel(panelExplorador);
			} else if (panelActual.equals(panelFoto)){
				String newPath = fotoActual.getPathActual();
				newPath = newPath.substring(0, newPath.lastIndexOf('\\'));
				setPathExplorador(newPath);
				cambiarPanel(panelExplorador);
			}
		}
		
		if (e.getSource() == b_Inicio) {
			Main.vista.activaPanelExplorador(Main.gamesPath);
		}
	}
}
