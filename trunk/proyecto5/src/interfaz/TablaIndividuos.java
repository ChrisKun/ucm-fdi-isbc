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
	
	Ontologia modelo;
	Controlador c;
	
	public TablaIndividuos(Ontologia modelo, Controlador c){
		this.modelo = modelo;
		String[] s = {"Componente", "Individuo"};
		this.setColumnIdentifiers(s);
		this.c = c;
		//this.addRow(s);
		

	}
	
	
	/**
	 * Con el nombre de un individuo, busca en la ontologia y si existe, rellena la tabla
	 * con las propiedades de ese individuo (foto) y los valores para cada propiedad.
	 * @param individuo
	 * @deprecated
	 */
	public void actualizarPropiedadesIndividuo(String individuo){
		//1. Comprobar que existe el individuo en la ontologia (como individuo)
		if (!modelo.getOb().existsInstance(individuo))
			return;
		//2. Ver las propiedades del invividuo y cargar esos valores para cada propiedad (cogiendolo de ontobridge)
		List<String> properties = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		modelo.getOb().listInstancePropertiesValues(individuo, properties, values);
		//3. Ahora hay que procesar la informacion de los posibles individuos que puedan aparecer en los
		// valores de las propiedades y sacar a su vez el tipo de componente que son
		this.setColumnCount(0);
		// Hay que procesar esto para que las propiedades salgan bien y no con toda la direccion de la ontologia
		// XXX NOTA: En Componente deberia ir la clase superior a la que pertenece el individuo (Personaje, Planta, etc) y el
		// Nombre del individuo
		this.addColumn("Componente", properties.toArray());
		this.addColumn("Valor", values.toArray());
		//4. Añadir esta información a la tabla (actualizar)
		
		
	}
	
	/**
	 * Le pasamos el nombre del individuo de una foto y lo que tiene que hacer es ir comprobando los valores de las propiedades
	 * de este individuo. Una vez consultado eso, deberia sacar a que tipo de clase pertenece (Personaje, Planta, etc) y el nombre
	 * del inviduo
	 */
	public void ponerIndividuosPorContentidoDeFoto(String foto){
		String[] s = {"Componente", "Individuo"};
		this.setColumnIdentifiers(s);
		//Ver qué tipo de foto es para saber que propiedades podemos pedirle...
		if (!modelo.getOb().existsInstance(modelo.getOb().getURI(foto)))
			return;
		//recogemos sus propiedades
		List<String> properties = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		modelo.getOb().listInstancePropertiesValues(foto, properties, values);
		/* y ahora vemos sus propiedades
		 * aparece y aparecePersonaje son propiedades del individuo foto
		 * aparecePersonaje ademas cuenta con que tienen que ser personajes..
		 */
		for (int i = 0; i < properties.size(); i++){
			Vector <String> v = new Vector<String>();
			//CONTENIDO - Comprobamos la propiedad
			String str = modelo.getOb().getShortName(properties.get(i));
			if (str.equals("aparece")){
				// Sacamos que a que tipo de contenido pertenece
				for (int j = 0; j < c.getTiposDeContenido().size(); j++){
					//Comprobar si es distinto de personaje
					if (modelo.getOb().isInstanceOf(values.get(i),modelo.getOb().getURI(c.getTiposDeContenido().get(j)))){
						v.add(c.getTiposDeContenido().get(j));
						v.add(modelo.getOb().getShortName(values.get(i)));
						//Lo añadimos como nueva fila
						this.addRow(v);
					}
				}
			}
		}
		
	}
	
	/**
	 * impide que se puedan modificar las celdas
	 */
	 public boolean isCellEditable (int row, int column){
		 return false;
	 }


}
