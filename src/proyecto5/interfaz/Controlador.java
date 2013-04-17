package interfaz;

import java.awt.Component;


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
	
	public Component getTree(){
		PnlConceptsAndInstancesTree tree = new PnlConceptsAndInstancesTree(modelo.getOb(),true);
		return tree;
	}
}
