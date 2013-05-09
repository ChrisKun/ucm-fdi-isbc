package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;

public class PanelCondicion extends JPanel implements ActionListener{

	private String titulo;
	
	private JComboBox cB_elementos;
	
	private JButton b_Union;
	private JButton b_Interseccion;
	private JButton b_Eliminar;
		
	private DefaultTableModel tM_propiedades;
	private JTable t_propiedades;
	
	public PanelCondicion(Controlador controlador, String titulo, ArrayList<String> elementos){
		super();
		this.add(createPanelPregunta(titulo, elementos));		
	}
	
	/**
	 * @param string
	 * @param nombre
	 * @param unaRespuesta
	 * @param elementos
	 * @return
	 */
	private JPanel createPanelPregunta(String titulo, ArrayList<String> elementos) {
		
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), titulo);		
		title.setTitleJustification(TitledBorder.CENTER);
		
		Vector v = new Vector(elementos);
		cB_elementos = new JComboBox(v); 
		p.add(cB_elementos);
				
		// agregamos ahora el combobox a la lista para mantener el puntero
		//comboBoxRespuestas.put(string,j);
		
		//paneles de botones
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(3,1));
		
		b_Union = new JButton("Union");
		b_Interseccion = new JButton("Interseccion");
		b_Eliminar = new JButton("Eliminar");
				
		b_Union.addActionListener(this);
		b_Interseccion.addActionListener(this);
		b_Eliminar.addActionListener(this);
		
		panelBotones.add(b_Union);
		panelBotones.add(b_Interseccion);
		panelBotones.add(b_Eliminar);
		
		p.add(panelBotones);
		
		tM_propiedades = new DefaultTableModel();
		
		tM_propiedades = new DefaultTableModel();
		tM_propiedades.addColumn("---");
		tM_propiedades.addColumn(titulo);
		t_propiedades = new JTable(tM_propiedades){
			 public boolean isCellEditable(int row, int column){  
				    return false;  
			 }
		};
		
		t_propiedades.getColumn("---").setMaxWidth(10);
		
		JScrollPane sP = new JScrollPane(t_propiedades);
		sP.setPreferredSize(new Dimension(300,140));
		p.add(sP);
		
		p.setBorder(title);
		return p;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_Union){
			addElementFromComboBox(true);
		}
		if (e.getSource() == b_Interseccion){
			addElementFromComboBox(false);
		}
		if (e.getSource() == b_Eliminar){
			removeElementFromComboBox();
		}
	}
	
	private void addElementFromComboBox(boolean esUnion){
		
		String[] s = new String[2];
		
		int rowNumber = cB_elementos.getItemCount(); 
		if (rowNumber != 0){
			if (tM_propiedades.getRowCount() != 0){
				if (esUnion) s[0] = "O"; else s[0] = "Y";
			}
			s[1] = (String) cB_elementos.getSelectedItem();
			cB_elementos.removeItem(s[1]);
			tM_propiedades.addRow(s);
		}
		
	}
	
	private void removeElementFromComboBox(){
		int pos = t_propiedades.getSelectedRow();
				
		if (pos >= 0){
			if (pos == 0 && tM_propiedades.getRowCount() > 1){
				tM_propiedades.setValueAt("", 1, 0);
			}
			String s = (String) tM_propiedades.getValueAt(pos, 1);
			cB_elementos.addItem(s);
			tM_propiedades.removeRow(pos);
		}
	}

	public ArrayList<String> getListaAtributos(){
		ArrayList<String> l = new ArrayList<String>();
		for (int i=0;i<tM_propiedades.getRowCount();i++){
			String s = (String) tM_propiedades.getValueAt(i, 1);
			l.add(s);
		}
		return l;
	}
}
