package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import sistema.SistemaTienda;

import GAPDataBase.GAPLoader;

public class VentanaPrincipal extends JFrame {
	
	/* Configuracion de pantalla */
	public final static int W = 720;
	public final static int H = 480;
	private VentanaPrincipal vP;
	private JPanel panelCambia;
	private JPanel panelPrincipal;
	private JLabel labelInicio;
	private JButton homeCat;
	private JButton retCat;
	
	
	public VentanaPrincipal()
	{
		GAPLoader.initDataBase();
		System.out.println(GAPLoader.recopilaCategorias());
		new SistemaTienda();
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int width = pantalla.width;
		int height = pantalla.height;
		this.setBounds(width/2 - W/2, height/2 - H/2, W, H);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setContentPane(getPanelPrincipal());
		this.setMinimumSize(new Dimension(W,H));
		vP = this;
	}
	
	private Container getPanelPrincipal() {
		panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());		
		
		// Panel Superior: Con información
		panelPrincipal.add(getPanelSuperior(), BorderLayout.NORTH);
		
		// Panel Occidental: 
		panelPrincipal.add(getPanelIzquierdo(), BorderLayout.WEST);
		
		// Panel de inicio NOTA: Es el panel que variará
		panelCambia = new PanelInicio(this);
		
		panelPrincipal.add(panelCambia);
		
		return panelPrincipal;		
	}

	private JPanel getPanelSuperior() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBorder(BorderFactory.createBevelBorder(0));
		p.add(getSubPanelMenu(),BorderLayout.EAST);
		p.add(getSubPanelTextoInicio(), BorderLayout.WEST);
		
		return p;
	}
	
	
	
	private JPanel getSubPanelTextoInicio() {
		JPanel p = new JPanel();
		labelInicio = new JLabel("¡Bienvenido invitado! Inicie sesión o regístrese para comprar");
		p.add(labelInicio);
		return p;
	}
	
	public void setLabelInicio(String str)
	{
		labelInicio.setText(str);
	}

	private JPanel getSubPanelMenu() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.RIGHT));
		p.add(getBoton("Inicio"));
		p.add(getBoton("Cesta"));
		p.add(getBoton("Perfil"));
		p.add(getBoton("Ayuda"));
		return p;
	}
	
	private JPanel getPanelIzquierdo()
	{
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createBevelBorder(0));
		p.setLayout(new BorderLayout());
		p.add(getPanelCategoria(), BorderLayout.CENTER);
		p.add(getSubPanelBotonCategoria(), BorderLayout.SOUTH);
		return p;
	}
	
	private JPanel getSubPanelBotonCategoria()
	{
		ImageIcon icon = null;
		char slash = File.separatorChar;
		
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		
		homeCat = new JButton();
		homeCat.setToolTipText("Volver a todas las categorías");
		homeCat.setPreferredSize(new Dimension(32,32));
		retCat = new JButton();
		retCat.setToolTipText("Retroceder una categoría");
		retCat.setPreferredSize(new Dimension(32,32));
		
		icon = new ImageIcon("src"+slash+"proyecto3"+slash+"images"+slash+"home_cat.png");
		homeCat.setIcon(icon);
		
		icon = new ImageIcon("src"+slash+"proyecto3"+slash+"images"+slash+"back_cat.png");
		retCat.setIcon(icon);
		
		retCat.setEnabled(false);
		
		p.add(homeCat);
		p.add(retCat);
		return p;
	}

	private JScrollPane getPanelCategoria() {
		// X: Fijate que he cambiado el JPanel por un JScrollPane, ya que podemos tener el 
		//		efecto de un panel entero con un scroll. Se aceptan sugerencias jeje
		
		//JPanel p = new JPanel();
		String[] divisiones = GAPLoader.recopilaDivisiones();
		//GAPLoader.recopilaCategorias();
		// X: Tal como esta en la carpeta de imagenes, luego hay subcarpetas. Habria que 
		//		ver como hacemos los submenus.
		JList list = new JList(divisiones);
		JScrollPane scrollList = new JScrollPane(list);
		//p.add(list);
		return scrollList;
	}
	
	/**
	 * Devuelve un botón para la interfaz, ya con el action listener incluido
	 * @param str
	 * @return
	 */
	private JButton getBoton(String str)
	{
		char slash = File.separatorChar;
		ImageIcon icon = null;
		
		JButton jb = new JButton();
		jb.setName(str);
		//jb.setText(str);
		
		if (str.equals("Ayuda"))
			icon = new ImageIcon("src"+slash+"proyecto3"+slash+"images"+slash+"help.png");
		else if (str.equals("Perfil"))
			icon = new ImageIcon("src"+slash+"proyecto3"+slash+"images"+slash+"profile.png");
		else if (str.equals("Cesta"))
			icon = new ImageIcon("src"+slash+"proyecto3"+slash+"images"+slash+"cart.png");
		else
			icon = new ImageIcon("src"+slash+"proyecto3"+slash+"images"+slash+"home.png");
		
		jb.setIcon(icon);
		jb.setToolTipText(str);
		jb.setPreferredSize(new Dimension(48,48));
		//action Listener
		jb.addActionListener(new ListenerVentana());
		return jb;
	}
	
	public void cambiarPanel(JPanel p)
	{
		panelPrincipal.remove(panelCambia);
		panelCambia = p;
		panelPrincipal.add(panelCambia);
		panelPrincipal.validate();
	}

	public static void main (String[] args)
	{
		VentanaPrincipal p = new VentanaPrincipal();
		p.setVisible(true);
	}
	
	private class ListenerVentana implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			
			//Opciones
			if (b.getName().equals("Cesta"))
			{
				new VentanaCesta(vP);
				vP.setEnabled(false);
				
			}
			else if (b.getName().equals("Perfil"))
			{
				new VentanaPerfil(vP);
				vP.setEnabled(false);
			}
			else if (b.getName().equals("Ayuda"))
			{
				new VentanaAyuda(vP);
				vP.setEnabled(false);
			}
			else if (b.getName().equals("Inicio"))
			{
				vP.cambiarPanel(new PanelInicio(vP));
			}
		}		
	}
	

}
