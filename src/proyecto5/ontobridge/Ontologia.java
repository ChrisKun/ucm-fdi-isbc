package ontobridge;

import java.util.ArrayList;
import java.util.Iterator;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;
import es.ucm.fdi.gaia.ontobridge.test.gui.PnlConceptsAndInstancesTree;

public class Ontologia {
	
	// Necesitamos un objeto de ontobridge para cargar el razonador y la propia ontologia
	private OntoBridge ob;
	// Necesitamos un objeto de OntologyDocument para cargar la ontologia principal
	private OntologyDocument mainOnto;
	
	/**
	 * Constructora que carga la ontologia pasada por parámetro como String
	 * @param url 	- Direccion url de la ontologia
	 * @param file 	- Direccion de la ontologia (archivo .owl)
	 */
	public Ontologia(String url, String file) {
		// Cargamos Ontobridge
		ob = new OntoBridge();
		// Usamos Pellet como Razonador
		ob.initWithPelletReasoner();		
		// Cargamos la Ontologia
		mainOnto = new OntologyDocument(url,file);
		ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
		ob.loadOntology(mainOnto, subOntologies, false);
	}
	
		
	// Getters
	
	public OntoBridge getOb() {
		return ob;
	}

	public OntologyDocument getMainOnto() {
		return mainOnto;
	}
	
	
	
	// MAIN PARA PRUEBAS
	public static void main(String args[]) 
	{
		//XXX: IMPORTANTE -> Poner "file:" delante de la ruta de la ontologia
		Ontologia ontologia = new Ontologia("http://http://sentwittment.p.ht/","file:src/proyecto5/ontologia/etiquetado.owl");
		
		Iterator<String> fotos = ontologia.getOb().listInstances("Lugar");
		while(fotos.hasNext())
		{
			String instance = fotos.next();
			System.out.println(instance);
			ArrayList<String> properties = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			ontologia.getOb().listInstancePropertiesValues(instance, properties, values);
			for(int i=0; i<properties.size(); i++)
				System.out.println(properties.get(i)+" --> "+ values.get(i));
		}
		
		
		javax.swing.JFrame window = new javax.swing.JFrame(ontologia.getMainOnto().getURL());
		PnlConceptsAndInstancesTree tree = new PnlConceptsAndInstancesTree(ontologia.getOb(),true);
		window.getContentPane().add(tree);
		window.pack();
		window.setSize(300, 600);
		window.setVisible(true);
		
	}
}
