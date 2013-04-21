package interfaz;

import java.awt.Component;
import java.util.ArrayList;


import es.ucm.fdi.gaia.ontobridge.test.gui.PnlConceptsAndInstancesTree;

import ontobridge.Ontologia;

public class Controlador {

	Ontologia modelo;
	VentanaPrincipal vista;
	
	public Controlador(Ontologia modelo){
		this.modelo = modelo;
	}
	
	public void setVista(VentanaPrincipal vista){
		this.vista = vista;
	}
	
	/**
	 * Devuelve el árbol de Ontobridge correctamente configurado.
	 * @return
	 */
	public Component getTree(){
		PnlConceptsAndInstancesTree tree = new PnlConceptsAndInstancesTree(modelo.getOb(),true);
		return tree;
	}
	
	/**
	 * Devuelve en una lista los tipos de contenidos que etiquetamos
	 * @return
	 */
	public ArrayList<String> getTiposDeContenido(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("Objeto");
		list.add("Personaje");
		list.add("Lugar");
		return list;
	}
	
	/**
	 * Devuelve en una lista las preguntas que debe responder el usuario para etiquetar la foto,
	 * indicando con un asterisco cuales son obligatorias.
	 * @param contenido indice en la lista devuelta por 'getTiposDeContenido()'
	 * @return
	 */
	public ArrayList<String> getPreguntasARellenar(int contenido){
		ArrayList<String> clasesContenido = getTiposDeContenido();
		ArrayList<String> list = new ArrayList<String>();
		String aux = null;
		//Obtenemos las propiedades de una clase y la guardamos en un iterador
		Iterator<String> it = modelo.getOb().listProperties(clasesContenido.get(contenido));
		
		while (it.hasNext()){
			aux = it.next();
			list.add(aux.substring(aux.lastIndexOf("#")+1));
			//TODO Falta comprobar si es obligatoria y mejorar el aspecto :P
		}
		return list;
	}
	
	/**
	 * Devuelve la instancia con la que estamos trabajando en este momento, no tiene por qué
	 * ser un String pero hay que hablar de que devolvemos aquí.
	 * @return
	 */
	public String getInstanciaActualSeleccionada(){
		String s = "";
		
		return s;
	}
}
