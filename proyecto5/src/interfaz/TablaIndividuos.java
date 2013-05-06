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
	 * 
	 * 
	 * @param individuo
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
	 * del inviduo:
	 * Ejemplo:
	 *     Contenido | Valor
	 *     -----------------
	 *     Personaje | Link
	 *     Personaje | Zelda
	 *     Lugar     | Castillo
	 * @param foto
	 */
	public void verContenidoFoto(String foto){
		Iterator<String> it;
		ArrayList<String> tiposContenido;
		boolean enc = false;
		String clase;
		//1. Comprobar que existe la foto instanciada en la ontologia
		if (!modelo.getOb().existsInstance(foto))
			return;
		//2. Existe, por tanto vemos las propiedades (del estilo, Aparece)
		List<String> properties = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		modelo.getOb().listInstancePropertiesValues(foto, properties, values);
		String[] s = {"Componente", "Individuo"};
		this.setColumnIdentifiers(s);
		// Procesamos los valores de cada propiedad y vemos si son individuos... Si son individuos sacamos en ontobridge
		// a cual de las 4 clases pertenece
		for(int i = 0; i < properties.size(); i++){
			if (modelo.getOb().existsInstance(values.get(i))){
				// Existe esa instancia, por tanto vemos a que clases pertenece
				it = modelo.getOb().listBelongingClasses(values.get(i));
				//Vamos a ver a cual de las 4 clases pertenece
				while (it.hasNext() && !enc){
					clase = it.next();
					tiposContenido = c.getTiposDeContenido();
					for (int j = 0; j < tiposContenido.size(); j++ )
						if (clase.equals(tiposContenido.get(j))){
							enc = true;
							Vector<String> v = new Vector<String>();
							//CONTENIDO
							v.add(tiposContenido.get(j));
							//INSTANCIA
							v.add(modelo.getOb().getShortName(values.get(i)));
							//Lo añadimos como nueva fila
							this.addRow(v);
						}
				}
			}
		}
	}
	
	 public boolean isCellEditable (int row, int column){
		 return false;
	 }


}
