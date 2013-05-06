package interfaz;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;



import es.ucm.fdi.gaia.ontobridge.test.gui.PnlConceptsAndInstancesTree;
import es.ucm.fdi.gaia.ontobridge.test.gui.PnlSelectInstance;

import ontobridge.Foto;
import ontobridge.Ontologia;

public class Controlador {

	Ontologia modelo; 
	VentanaPrincipal vista;
	String rutaOntologia;
	PnlSelectInstance tree; //arbol
	
	public Controlador(Ontologia modelo){
		this.modelo = modelo;
		rutaOntologia = modelo.getOb().getThingURI();
		System.out.println(rutaOntologia);
	}
	
	public void setVista(VentanaPrincipal vista){
		this.vista = vista;
	}
	
	/**
	 * Devuelve el árbol de Ontobridge correctamente configurado.
	 * @return
	 */
	public Component getTree(){
		tree = new PnlSelectInstance(modelo.getOb(),true);
		return tree;
	}
	
	/**
	 * Devuelve en una lista los tipos de contenidos que se pueden etiquetar
	 * (Personajes, Plantas, Lugares y Objetos)
	 * @return
	 */
	public ArrayList<String> getTiposDeContenido(){
		
		String s;
		Iterator<String> it = modelo.getOb().listSubClasses(modelo.getOb().getURI("Contenido"), true);
		ArrayList<String> list = new ArrayList<String>();
		
		while (it.hasNext()){
			s = it.next();
			list.add(modelo.getOb().getShortName(s));
		}
		return list;
	}
	
	/**
	 * Devuelve en una lista las preguntas que debe responder el usuario cuando crea un individuo
	 * de un contenido en concreto (relleno de atributos)
	 * @param contenido indice en la lista devuelta por 'getTiposDeContenido()'
	 * @return lista con todas las preguntas a contestar
	 */
	public ArrayList<String> getPreguntasARellenar(int contenido){
		ArrayList<String> clasesContenido = getTiposDeContenido();
		ArrayList<String> list = new ArrayList<String>();
		String aux = null;
		//Obtenemos las propiedades de una clase y la guardamos en un iterador
		Iterator<String> it = modelo.getOb().listProperties(clasesContenido.get(contenido));
		
		while (it.hasNext()){
			aux = it.next();
			list.add(modelo.getOb().getShortName(aux));
			//TODO Falta comprobar si es obligatoria
		}
		return list;
	}
	
	/**
	 * Devuelve los individuos de la foto asociada a un tipo de contenido
	 * en concreto
	 * @param nombreFoto nombre de la foto que se quiere sacar las instancias
	 * @param contenido tipo de contenido a sacar
	 * @deprecated
	 * @return
	 */
	public ArrayList<String> getIndividuosEtiquetados(String nombreFoto, int contenido){
		//Foto f = fotos.get(nombreFoto);
		//return f.getIndividuos(getTiposDeContenido().get(contenido));
		return null;
	}
	
	/**
	 * Devuelve las propiedades de un individuo
	 * @param individuo
	 * @return
	 */
	public Iterator<String> getPropiedadesIndividuo(String individuo){
		return modelo.getOb().listInstanceProperties(individuo);
	}
	/**
	 * Devuelve los individuos asociados a una propiedad de un individuo
	 * @param individuo
	 * @return properties y values serán rellenados con las propiedades y los valores de esas propiedades
	 */
	//public void getValoresDePropiedadesDeIndividuo(String individuo, List<String> properties, List<String> values){
	//	modelo.getOb().listInstancePropertiesValues(individuo, properties, values);
	//}
	
	/**
	 * Permite rellenar las propiedades de un individuo
	 */
	public void creaIndividuo(String clase, String individuo){
		modelo.getOb().createInstance(clase, individuo);
	}
	
	/**
	 * Permite añadir individuos a la foto, pasandole los nuevos
	 * que sobreescribiran las que habian y el tipo de contenido que son
	 * @param nombreFoto nombre de la foto a sobreescribir instancias
	 * @param individuos nuevos que sustituiran a las viejas
	 * @param contenido tipo de contenido 
	 * @deprecated
	 */
	public void setEtiquetarInstancias(String nombreFoto, ArrayList<String> individuos, int contenido){
		//Foto f = fotos.get(nombreFoto);
		//if (f != null)
			//f.setIndividuos(individuos,getTiposDeContenido().get(contenido));
	}
	
	/**
	 * Devuelve la instancia con la que estamos trabajando en este momento, no tiene por qué
	 * ser un String pero hay que hablar de que devolvemos aquí.
	 * @return la instancia actual seleccionada
	 */
	public String getInstanciaActualSeleccionada(){
		return modelo.getOb().getShortName(tree.getSelectedInstance());
	}

	/**
	 * FIXME Forma de añadir un nuevo individuo. En el caso de las fotos
	 * El nombre sera el nombre del archivo quitando el jpg
	 */
	public void crearInvididuo(String nombre){
		
	}
	
	/**
	 * Devuelve la ruta de las fotos en las que aparece un individuo
	 * @param individuo
	 * @return lista de fotos en las que aparece
	 */
	public ArrayList<String> getFotosAparece(String individuo){
		ArrayList<String> listaFotos = new ArrayList<String>();
		//Primero vemos todas las clases a las que pertenece un individuo
		List<String> properties = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		modelo.getOb().listInstancePropertiesValues(individuo, properties, values);
		/* Ahora buscamos la propiedad de apareceEn para saber en qué fotos aparece y
		 * lo guardamos en un arrayList que es lo que devolvemos
		 */
		for (int i = 0; i < properties.size(); i++){
			String str = modelo.getOb().getShortName(properties.get(i));
			// Ahora comprobamos si es la propiedad que nos interesa
			if (str.equals("apareceEn")){
				listaFotos.add(modelo.getOb().getShortName(values.get(i)));
			}
		}
		//En el caso de que este, lo añadimos al array que devolvemos
		return listaFotos;
	}
}
