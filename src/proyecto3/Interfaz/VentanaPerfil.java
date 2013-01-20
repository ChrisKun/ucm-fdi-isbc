package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import Perfil.Usuario;

import sistema.SistemaTienda;

public class VentanaPerfil extends JFrame implements ActionListener{

	public final static int W = 450;
	public final static int H = 200;
	private static boolean logeado = false;
	
	private VentanaPrincipal vP;
	private VentanaPerfil vPerf;
	
	private JPanel panelSinUsuario;
	private JPanel panelConUsuario;
	
	private JTextField loginNombre;
	private JPasswordField loginPass;
	private JTextField regNombre;
	private JPasswordField regPass;
	private JButton botonLogin;
	private JButton botonReg;
	private JButton botonSalir;
	
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
		vPerf = this;
		
		if (!logeado){
			panelSinUsuario = getPanelPrincipalPerfil();
			this.add(panelSinUsuario);
		} else {
			panelConUsuario = getPanelLogeado();
			this.add(panelConUsuario);
		}
		
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
		
		JPanel subPanelCondiciones = new JPanel();
		subPanelCondiciones.setLayout(new FlowLayout());
		
		JLabel s = new JLabel("NOTA: Registrarse conlleva aceptar ");
		JLabel r = new JLabel("los términos y condiciones.");
		r.setFont(new Font(s.getFont().getFontName(), Font.BOLD, s.getFont().getSize()));
		r.setForeground(Color.BLUE);
		
		r.addMouseListener(new ListernerMouse());
		
		subPanelCondiciones.add(s);
		subPanelCondiciones.add(r);
		
		
		p.add(j, BorderLayout.NORTH);
		p.add(getSubPanelRegistrar(), BorderLayout.CENTER);
		p.add(subPanelCondiciones,BorderLayout.SOUTH);
		
		
		return p;
	}

	private JPanel getPanelLogeado(){
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Has iniciado sesion"));
		JLabel nombreUsuario = new JLabel("Usuario: "+ SistemaTienda.usuarioActual.getNombre());
		p.add(nombreUsuario, BorderLayout.NORTH);
		botonSalir = new JButton("Salir");
		botonSalir.setText("Salir");
		botonSalir.addActionListener(this);
		p.add(botonSalir, BorderLayout.SOUTH);
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
			//TODO comprobar en la base de datos
			try {
				Usuario usuario = Usuario.cargaUsuario(loginNombre.getText(), String.copyValueOf(loginPass.getPassword()));
				SistemaTienda.usuarioActual = usuario;
				this.remove(panelSinUsuario);
				panelConUsuario = getPanelLogeado();
				this.add(panelConUsuario);
				this.validate();
				logeado = true;
			} catch (Exception e) {
				// Error al hacer login
				JOptionPane.showMessageDialog(null,"Inserta un nombre de usuario y una pass validos"); 
			}
		}
		else if (b == botonReg)
		{
			// TODO añadir a la base de datos
			try {
				SistemaTienda.usuarioActual = Usuario.creaUsuario(regNombre.getText(), String.copyValueOf(regPass.getPassword()));
				SistemaTienda.usuarioActual.guardaUsuario();
				this.remove(panelSinUsuario);
				panelConUsuario = getPanelLogeado();
				this.add(panelConUsuario);
				this.validate();
				logeado = true;
			} catch (Exception e) {
				// Error al registrar
				if (regNombre.getText().compareTo("") == 0){
					JOptionPane.showMessageDialog(null,"Inserta un nombre");
				} else {
					JOptionPane.showMessageDialog(null,"Este usuario ya esta registrado");	
				}
				 
			}
		}
		else if (b == botonSalir){			
			try {
				JOptionPane.showMessageDialog(null,"Gracias por su compra");
				SistemaTienda.usuarioActual.guardaUsuario();
				this.remove(panelConUsuario);
				panelSinUsuario = getPanelPrincipalPerfil();
				this.add(panelSinUsuario);
				this.validate();
				logeado = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SistemaTienda.usuarioActual = null;
		}
		
	}
	
	private class ListernerMouse implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			new VentanaTerminos(vPerf);
			vPerf.setEnabled(false);
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
