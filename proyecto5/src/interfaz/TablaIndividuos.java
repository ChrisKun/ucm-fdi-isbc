package interfaz;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import ontobridge.Ontologia;

public class TablaIndividuos extends DefaultTableModel {
	
	public Ontologia modelo;
	
	public TablaIndividuos(Ontologia modelo){
		this.modelo = modelo;
		String[] s = {"Componente", "Individuo"};
		this.setColumnIdentifiers(s);
		//this.addRow(s);
		

	}
	
	/**
	 * Con el nombre de una foto, busca en la ontologia y si existe, rellena la tabla
	 * con las propiedades y sus valores
	 * @param foto
	 */
	public void actualizarTabla(String foto){
		//1. Comprobar que existe la foto en la ontologia (como individuo)
		if (!modelo.getOb().existsInstance(foto))
			return;
		//2. Ver las propiedades de la foto y cargar esos valores para cada propiedad (cogiendolo de ontobridge)
		List<String> properties = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		modelo.getOb().listInstancePropertiesValues(foto, properties, values);
		//3. Ahora hay que procesar la informacion de los posibles individuos que puedan aparecer en los
		// valores de las propiedades y sacar a su vez el tipo de componente que son
		this.addColumn("Componente", properties.toArray());
		this.addColumn("Valor", values.toArray());
		//4. Añadir esta información a la tabla (actualizar)
		
		
	}
	
	 public boolean isCellEditable (int row, int column){
		 return false;
	 }


}
