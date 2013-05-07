package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import clasificador.Main;

public class FotoActual extends JPanel implements ActionListener{

	private Controlador controlador;
	
	private String pathFotoActual;
	private JLabel fotoActual;
	
	private JTable tablaPropiedades;
	private DefaultTableModel modeloPropiedades;
	
	private JButton b_New;
	private JButton b_Add;
	
	private TablaIndividuos tab;
	
	private static final long serialVersionUID = 1L;
	
	public FotoActual() {
		// TODO Auto-generated constructor stub
		
	}
	
	public FotoActual(Controlador controlador){
		this.controlador = controlador;
		this.setLayout(new BorderLayout());
		
		modeloPropiedades = new DefaultTableModel();
		tablaPropiedades = new JTable(modeloPropiedades);
		tablaPropiedades.setLayout(new BorderLayout());
		this.add(tablaPropiedades,BorderLayout.CENTER);
		
		b_New = new JButton("Nuevo Individuo");
		b_New.addActionListener(this);
		b_Add = new JButton("Etiqueta Individuo");
		b_Add.addActionListener(this);
	}

	

	public void actualizarFoto(String nuevaFoto){
		pathFotoActual = nuevaFoto;
		actualizarPanel();
		actualizarTabla();
	}
	
	private void actualizarPanel(){
		Border blackline = BorderFactory.createEtchedBorder();
        ImageIcon foto = resizeFoto(pathFotoActual, 600, 350);
        
        if (fotoActual != null) remove(fotoActual);
        fotoActual = new JLabel(foto);
        fotoActual.setPreferredSize(new Dimension(600,300));
        fotoActual.setBorder(blackline);
        this.add(fotoActual, BorderLayout.WEST);
		this.add(new JLabel("Añadir información, etiquetar, etc."), BorderLayout.SOUTH);		
		
	}
	
	private void actualizarTabla(){
		
		tab = new TablaIndividuos(controlador);
		JTable t = new JTable(tab);
		/*
		 * Métodos interesantes a la hora de añadir el default table model a un JTable
		 */
		t.setCellSelectionEnabled(false);
		JScrollPane scrollPane = new JScrollPane(t);
		t.setFillsViewportHeight(true);
		tablaPropiedades.removeAll();
		
		tablaPropiedades.add(BorderLayout.PAGE_START, b_New);
		tablaPropiedades.add(BorderLayout.SOUTH, b_Add);
		tablaPropiedades.add(BorderLayout.CENTER, scrollPane);

		/*
		 * actualizar el contenido con una foto
		 */
		String nombreFoto = pathFotoActual.substring(pathFotoActual.lastIndexOf('\\')+1, pathFotoActual.lastIndexOf('.'));
		tab.ponerIndividuosPorContentidoDeFoto(nombreFoto,pathFotoActual);
	}
	
	private ImageIcon resizeFoto(String pathFoto, int ancho, int alto){
		ImageIcon foto = new ImageIcon(pathFoto);
        
        Image img = foto.getImage();
		int height = img.getHeight(null);
		int width = img.getWidth(null);
		float propHeight = height/((float)alto); 
		float propWidth = width/((float)ancho);
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
		
		return foto;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_New){
			VentanaEtiquetar vE = new VentanaEtiquetar(controlador);
			JDialog jD = new JDialog();
			jD.setContentPane(vE);
			jD.setSize(vE.getPreferredSize());
			jD.setLocation(200, 100);
			jD.setVisible(true);
			jD.setAlwaysOnTop(true);
			jD.setTitle("Crea un nuevo individuo");
		}
		if (e.getSource() == b_Add){
			//JOptionPane.showMessageDialog(this, controlador.getInstanciaActualSeleccionada());
			String nomFoto = pathFotoActual.substring(pathFotoActual.lastIndexOf('\\')+1, pathFotoActual.lastIndexOf('.'));
			JOptionPane.showMessageDialog(this,
					controlador.anadirIndividuoAFoto(controlador.getInstanciaActualSeleccionada(), nomFoto));
			//Añadir nueva fila a la tabla (solo si existe)
			tab.anadirIndividuoPorContenidoDeFoto(nomFoto,controlador.getInstanciaActualSeleccionada());
			
		}
	}
}
