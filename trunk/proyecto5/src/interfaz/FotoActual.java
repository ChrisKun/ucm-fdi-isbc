package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class FotoActual extends JPanel {

	private Controlador controlador;
	
	private String pathFotoActual;
	private JLabel fotoActual;
	
	private JTable tablaPropiedades;
	private DefaultTableModel modeloPropiedades;
	
	private static final long serialVersionUID = 1L;
	
	public FotoActual() {
		// TODO Auto-generated constructor stub
		
	}
	
	public FotoActual(Controlador controlador){
		this.controlador = controlador;
		this.setLayout(new BorderLayout());
		//fotoActual = new JLabel();
		//actualizarFoto("");
		
	}

	

	public void actualizarFoto(String nuevaFoto){
		this.removeAll();
		pathFotoActual = nuevaFoto;
		actualizarPanel();
		actualizarTabla();
	}
	
	private void actualizarPanel(){
				
		Border blackline = BorderFactory.createEtchedBorder();
        ImageIcon foto = resizeFoto(pathFotoActual, 600, 350);
        
        fotoActual = new JLabel(foto);
        fotoActual.setPreferredSize(new Dimension(600,300));
        fotoActual.setBorder(blackline);
        this.add(fotoActual, BorderLayout.WEST);
		this.add(new JLabel("Añadir información, etiquetar, etc."), BorderLayout.SOUTH);
		
		modeloPropiedades = new DefaultTableModel();
		String[] a = {"a","b"};
		modeloPropiedades.addRow(a);
		tablaPropiedades = new JTable(modeloPropiedades);
		
		
	}
	
	private void actualizarTabla(){
		tablaPropiedades.setLayout(new BorderLayout());
		this.add(tablaPropiedades,BorderLayout.CENTER);
		TablaIndividuos tab = new TablaIndividuos(controlador.modelo, controlador);
		JTable t = new JTable(tab);
		/*
		 * Métodos interesantes a la hora de añadir el default table model a un JTable
		 */
		t.setCellSelectionEnabled(false);
		JScrollPane scrollPane = new JScrollPane(t);
		t.setFillsViewportHeight(true);
		tablaPropiedades.add(BorderLayout.CENTER, scrollPane);

		/*
		 * actualizar el contenido con una foto
		 */
		String nombreFoto = pathFotoActual.substring(pathFotoActual.lastIndexOf('\\')+1, pathFotoActual.lastIndexOf('.'));
		System.out.println(nombreFoto);
		tab.ponerIndividuosPorContentidoDeFoto(nombreFoto);
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
}
