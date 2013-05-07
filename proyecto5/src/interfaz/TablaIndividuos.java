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
	 * @deprecated
	 */
	public void anadirIndividuoPorContenidoDeFoto(String foto,String individuo){
		String foto2 = modelo.getOb().getURI(foto);
		String uriIndividuo = modelo.getOb().getURI(individuo);
		ArrayList<String> list = c.getTiposDeContenido();
		
		if (!modelo.getOb().existsInstance(foto2))
			return;

		//CONTENIDO - Comprobamos la propiedad
		Vector<String> v = new Vector<String>();
		boolean enc = false;
		
		for (int j = 0; j < list.size() && !enc; j++){
		//Comprobar si es distinto de personaje
			if (modelo.getOb().isInstanceOf(uriIndividuo,modelo.getOb().getURI(list.get(j)))){
				v.add(list.get(j));
				v.add(individuo);
				//Lo añadimos como nueva fila
				this.addRow(v);
				enc = true;
			}
					
		}
	}
	
	/**
	 * FIXME
	 * @param foto
	 */
	public void actualizarContenidoFoto(String foto){
		String[] s = {"Componente", "Individuo"};
		this.setNumRows(0);
		this.setColumnIdentifiers(s);
		this.ponerIndividuosPorContentidoDeFoto(foto, null);
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
	}
	
	/**
	 * impide que se puedan modificar las celdas
	 */
	 public boolean isCellEditable (int row, int column){
		 return false;
	 }


}
