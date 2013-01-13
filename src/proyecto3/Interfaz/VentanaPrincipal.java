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
		
	}
	
	private Container getPanelPrincipal() {
		JPanel panelP = new JPanel();
		panelP.setLayout(new BorderLayout());		
		
		// Panel Superior: Con informaci�n
		panelP.add(getPanelSuperior(), BorderLayout.NORTH);
		
		// Panel Occidental: 
		panelP.add(getPanelIzquierdo(), BorderLayout.WEST);
		
		// Panel de inicio NOTA: Es el panel que variar�
		PanelInicio p = new PanelInicio();
		panelP.add(p);
		return panelP;		
	}

	private JPanel getPanelSuperior() {
		JPanel p = new JPanel();
		
		p.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton tmp;
		tmp = getBoton("Cesta");
		tmp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO X: La idea ser�a mirar como funciona JInternalFrame, pero la idea
				//		seria generar un frame, que no pudiese salirse de la aplicacion
				new VentanaCesta();
			}
		});
		p.add(tmp);
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
		JList<String> list = new JList<String>(categorias);
		JScrollPane scrollList = new JScrollPane(list);
		//p.add(list);
		return scrollList;
	}
	
	private JButton getBoton(String str)
	{
		JButton jb = new JButton();
		jb.setText(str);
		
		if (str.equals("Ayuda"))
		{
			jb.setText("");
			ImageIcon icon = new ImageIcon("src/proyecto3/images/help.png");
			jb.setIcon(icon);
			jb.setToolTipText(str);
		}
		//action Listener
		return jb;
	}

	public static void main (String[] args)
	{
		VentanaPrincipal p = new VentanaPrincipal();
		p.setVisible(true);
	}

}
