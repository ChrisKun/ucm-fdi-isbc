package Interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

public class VentanaCesta extends JFrame implements ActionListener{//JInternalFrame{

	public final static int W = 300;
	public final static int H = 200;
	private DefaultListModel listModel;
	private JList listaProductos;
	private JButton eliminar;
	private JButton cancelar;
	private VentanaPrincipal vP;
	
	
	
	public VentanaCesta(VentanaPrincipal ventana){
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int width = pantalla.width;
		int height = pantalla.height;
		this.setBounds(width/2 - W/2, height/2 - H/2, W, H);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //TODO cerrar esta ventana debe activar la que la ha llamado.
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("Cesta");
		
		this.setContentPane(getPanelPrincipalCesta());
		vP = ventana;
		
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
				  vP.setEnabled(true);
				  dispose();
			  }
			  });
	}

	private JPanel getPanelPrincipalCesta() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(getSubPanelLista(), BorderLayout.CENTER );
		p.add(getBotones(), BorderLayout.EAST);
		p.add(getPanelPrecioTotal(), BorderLayout.SOUTH);
		return p;
	}

	private JPanel getPanelPrecioTotal() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Precio total: "));
		JLabel l = new JLabel("247,23"+" €"); //TODO calcular precio
		p.add(l);
		return p;
	}

	private JPanel getBotones() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3,1));
		JButton pagar = new JButton("Pagar");
		pagar.setToolTipText("Procede con la compra de sus productos");
		
		cancelar = new JButton("Cancelar");
		cancelar.setToolTipText("Cancela la compra actual");
		cancelar.addActionListener(this);
		
		eliminar = new JButton("Eliminar");
		eliminar.addActionListener(this);
		eliminar.setToolTipText("Elimina un producto seleccionado");
		
		if (listModel.getSize() == 0)
			eliminar.setEnabled(false);
		
		p.add(eliminar);
		p.add(cancelar);
		p.add(pagar);
		
		return p;
	}

	private JPanel getSubPanelLista() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Mi carrito"));
		
		listModel = new DefaultListModel();
	
		listModel.addElement("Elemento 1");
		listModel.addElement("Elemento 2");
		listModel.addElement("Elemento 3");
		listModel.addElement("Elemento 1");
		listModel.addElement("Elemento 2");
		listModel.addElement("Elemento 3");
		listModel.addElement("Elemento 1");
		listModel.addElement("Elemento 2");
		listModel.addElement("Elemento 3");
		listModel.addElement("Elemento 1");
		listModel.addElement("Elemento 2");
		listModel.addElement("Elemento 3");
		listModel.addElement("Elemento 1");
		listModel.addElement("Elemento 2");
		listModel.addElement("Elemento 3");

		listaProductos = new JList(listModel);
		listaProductos.setLayoutOrientation(JList.VERTICAL);
		// Solo se puede seleccionar un producto para eliminar
		listaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane listScroller = new JScrollPane(listaProductos);
		//listScroller.setPreferredSize(new Dimension(250, 80));
		
		p.add(listScroller, BorderLayout.CENTER);
		
		if (listModel.size()>0)
			listaProductos.setSelectedIndex(0);
			
		return p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		
		if (b == eliminar)
		{
			
			 int index = listaProductos.getSelectedIndex();
			 listModel.remove(index);
			 

			 if (listModel.getSize() <= 0) 
				 eliminar.setEnabled(false);
			 
			 else 
			 { 
				 if (index == listModel.getSize()) 
					 index--;
				 
			     listaProductos.setSelectedIndex(index);
			     listaProductos.ensureIndexIsVisible(index);
			 }
		}
		else if (b == cancelar)
		{
			vP.setEnabled(true);
			this.dispose();
		}
		
	}
}
