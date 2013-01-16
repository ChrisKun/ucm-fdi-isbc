package Interfaz;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class VentanaPrincipal extends JFrame {
	
	/* Configuracion de pantalla */
	public final static int W = 720;
	public final static int H = 480;
	
	public VentanaPrincipal()
	{
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int width = pantalla.width;
		int height = pantalla.height;
		this.setBounds(width/2 - W/2, height/2 - H/2, W, H);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setContentPane(getPanelPrincipal());
		this.setMinimumSize(new Dimension(720,480));
	}
	
	private Container getPanelPrincipal() {
		JPanel panelP = new JPanel();
		panelP.setLayout(new BorderLayout());		
		
		// Panel Superior: Con información
		panelP.add(getPanelSuperior(), BorderLayout.NORTH);
		
		// Panel Occidental: 
		panelP.add(getPanelIzquierdo(), BorderLayout.WEST);
		
		// Panel de inicio NOTA: Es el panel que variará
		JPanel p = new PanelExplorador();//PanelInicio();
		panelP.add(p);
		return panelP;		
	}

	private JPanel getPanelSuperior() {
		JPanel p = new JPanel();
		
		p.setLayout(new FlowLayout(FlowLayout.RIGHT));
		p.add(getBoton("Cesta"));
		p.add(getBoton("Perfil"));
		p.add(getBoton("Ayuda"));
		return p;
	}
	
	private JScrollPane getPanelIzquierdo() {
		// X: Fijate que he cambiado el JPanel por un JScrollPane, ya que podemos tener el 
		//		efecto de un panel entero con un scroll. Se aceptan sugerencias jeje
		
		//JPanel p = new JPanel();
		String[] categorias = {"Body", "Boys", "GapFit", "Girls", "InfantBoy", "InfantGirl", "Maternity", 
				"Men", "ToddlerBoy", "ToddlerGirl", "Women"};
		// X: Tal como esta en la carpeta de imagenes, luego hay subcarpetas. Habria que 
		//		ver como hacemos los submenus.
		JList list = new JList(categorias);
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
		JButton jb = new JButton();
		jb.setName(str);
		jb.setText(str);
		
		if (str.equals("Ayuda"))
		{
			jb.setText("");
			ImageIcon icon = new ImageIcon("src/proyecto3/images/help.png");
			jb.setIcon(icon);
			jb.setToolTipText(str);
		}
		//action Listener
		jb.addActionListener(new ListenerVentana());
		return jb;
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
				new VentanaCesta();
			}
			else if (b.getName().equals("Perfil"))
			{
				
			}
			else if (b.getName().equals("Ayuda"))
			{
				
			}
		}		
	}

}
