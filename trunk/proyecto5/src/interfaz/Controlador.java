package interfaz;

import java.awt.Component;
import java.util.ArrayList;


import es.ucm.fdi.gaia.ontobridge.test.gui.PnlConceptsAndInstancesTree;

import ontobridge.Ontologia;

public class Controlador {

	Ontologia modelo;
	Interfaz vista;
	
	public Controlador(Ontologia modelo){
		this.modelo = modelo;
	}
	
	public void setVista(Interfaz vista){
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
		
		return list;
	}
	
	/**
	 * Devuelve en una lista las preguntas que debe responder el usuario para etiquetar la foto,
	 * indicando con un asterisco cuales son obligatorias.
	 * @param contenido indice en la lista devuelta por 'getTiposDeContenido()'
	 * @return
	 */
	public ArrayList<String> getPreguntasARellenar(int contenido){
		ArrayList<String> list = new ArrayList<String>();
		
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
