package Interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class VentanaPerfil extends JFrame implements ActionListener{

	public final static int W = 450;
	public final static int H = 200;
	private VentanaPrincipal vP;
	
	private JTextField loginNombre;
	private JPasswordField loginPass;
	private JTextField regNombre;
	private JPasswordField regPass;
	private JButton botonLogin;
	private JButton botonReg;
	
	public VentanaPerfil(VentanaPrincipal ventana){
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int width = pantalla.width;
		int height = pantalla.height;
		this.setBounds(width/2 - W/2, height/2 - H/2, W, H);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //TODO cerrar esta ventana debe activar la que la ha llamado.
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("Perfil");
		vP = ventana;
		
		this.setContentPane(getPanelPrincipalPerfil());
		
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
				  vP.setEnabled(true);
				  dispose();
			  }
			  });
	}

	private JPanel getPanelPrincipalPerfil() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(getPanelLogin(),BorderLayout.NORTH);
		p.add(getPanelRegistrar(), BorderLayout.SOUTH);
		return p;
	}

	private Component getPanelLogin() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Login"));
		
		JLabel l = new JLabel("Nombre: ");
		
		loginNombre = new JTextField();
		loginNombre.setEditable(true);
		loginNombre.setPreferredSize(new Dimension(80,20));
		
		JLabel l2 = new JLabel("Contraseña: ");
		
		loginPass = new JPasswordField();
		loginPass.setEditable(true);
		loginPass.setPreferredSize(new Dimension(80,20));
		
		botonLogin = new JButton("Entrar");
		botonLogin.addActionListener(this);
		
		p.add(l);
		p.add(loginNombre);
		p.add(l2);
		p.add(loginPass);
		p.add(botonLogin);
		
		return p;
		
	}

	private Component getPanelRegistrar() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Registrar"));
		
		JLabel j = new JLabel("¿No tienes cuenta? Registrate a continuacion: ");
		JLabel s = new JLabel("NOTA: Registrarse conlleva aceptar los términos y condiciones");
		
		p.add(j, BorderLayout.NORTH);
		p.add(getSubPanelRegistrar(), BorderLayout.CENTER);
		p.add(s,BorderLayout.SOUTH);
		
		
		return p;
	}

	private Component getSubPanelRegistrar() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());

		JLabel l = new JLabel("Nombre: ");
		
		regNombre = new JTextField();
		regNombre.setEditable(true);
		regNombre.setPreferredSize(new Dimension(80,20));
		
		JLabel l2 = new JLabel("Contraseña: ");
		
		regPass = new JPasswordField();
		regPass.setEditable(true);
		regPass.setPreferredSize(new Dimension(80,20));
		
		botonReg = new JButton("Registrarse");
		botonReg.addActionListener(this);
		
		p.add(l);
		p.add(regNombre);
		p.add(l2);
		p.add(regPass);
		p.add(botonReg);
		return p;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton b = (JButton) arg0.getSource();
		
		if (b == botonLogin)
		{
			
		}
		else if (b == botonReg)
		{
			
		}
		
	}
}
