package interfaz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import ontobridge.Ontologia;

public class TablaIndividuos extends DefaultTableModel {
	
	Ontologia modelo;
	Controlador c;
	
	public TablaIndividuos(Controlador c){
		modelo = c.modelo;
		String[] s = {"Componente", "Individuo"};
		this.setColumnIdentifiers(s);
		this.c = c;
	}
	
	/**
	 * Igual que poner IndividuosPorContenidoDeFoto pero solo para poner uno nuevo
	 * @param foto
	 */
	public void anadirIndividuoPorContenidoDeFoto(String foto,String individuo){
		String foto2 = modelo.getOb().getURI(foto);
		String uriIndividuo = modelo.getOb().getURI(individuo);
		
		if (!modelo.getOb().existsInstance(foto2))
			return;

		//CONTENIDO - Comprobamos la propiedad
		Vector<String> v = new Vector<String>();
		
		for (int j = 0; j < c.getTiposDeContenido().size(); j++){
		//Comprobar si es distinto de personaje
			if (modelo.getOb().isInstanceOf(uriIndividuo,modelo.getOb().getURI(c.getTiposDeContenido().get(j)))){
				v.add(c.getTiposDeContenido().get(j));
				v.add(individuo);
				//Lo añadimos como nueva fila
				this.addRow(v);
			}
					
		}
	}
	
	/**
	 * Le pasamos el nombre del individuo de una foto y lo que tiene que hacer es ir comprobando los valores de las propiedades
	 * de este individuo. Una vez consultado eso, deberia sacar a que tipo de clase pertenece (Personaje, Planta, etc) y el nombre
	 * del inviduo
	 */
	public void ponerIndividuosPorContentidoDeFoto(String foto, String rutaImagen){
		//Asociamos la ruta de la imagen a una instancia
		c.setRutaFoto(rutaImagen, foto); //FIXME
		Vector <String> v = new Vector<String>();
		String uriAparece = modelo.getOb().getURI(Config.aparece);
		ArrayList<String> tiposContenido = c.getTiposDeContenido();
		
		String foto2 = modelo.getOb().getURI(foto);
		String[] s = {"Componente", "Individuo"};
		this.setColumnIdentifiers(s);
		//Ver qué tipo de foto es para saber que propiedades podemos pedirle...
		if (!modelo.getOb().existsInstance(foto2))
			return;
		//recogemos sus propiedades
		//List<String> properties = new ArrayList<String>();
		//List<String> values = new ArrayList<String>();
		//modelo.getOb().listInstancePropertiesValues(foto2, properties, values);
		
		// Sacamos los valores de la propiedad aparece
		Iterator<String> it = modelo.getOb().listPropertyValue(foto2, uriAparece);
		
		String str;
		
		while (it.hasNext()){
			str = it.next();
			v = new Vector<String>();
			
				for (int j = 0; j < tiposContenido.size() ; j++){
					//Comprobar si es distinto de personaje
					if (modelo.getOb().isInstanceOf(str,modelo.getOb().getURI(tiposContenido.get(j)))){
						v.add(tiposContenido.get(j));
						v.add(modelo.getOb().getShortName(str));
						//Lo añadimos como nueva fila
						this.addRow(v);
					}
			}
		}
		
		/* y ahora vemos sus propiedades
		 * aparece y aparecePersonaje son propiedades del individuo foto
		 * aparecePersonaje ademas cuenta con que tienen que ser personajes..
		 */
		/*for (int i = 0; i < properties.size(); i++){
			//CONTENIDO - Comprobamos la propiedad
			if (uriAparece.equals(properties.get(i))){
				v = new Vector<String>();
				// Sacamos que a que tipo de contenido pertenece
				for (int j = 0; j < tiposContenido.size() ; j++){
					//Comprobar si es distinto de personaje
					if (modelo.getOb().isInstanceOf(values.get(i),modelo.getOb().getURI(tiposContenido.get(j)))){
						v.add(tiposContenido.get(j));
						v.add(modelo.getOb().getShortName(values.get(i)));
						//Lo añadimos como nueva fila
						this.addRow(v);
					}
				}
			}
		}*/
	}
	
	/**
	 * impide que se puedan modificar las celdas
	 */
	 public boolean isCellEditable (int row, int column){
		 return false;
	 }


}
