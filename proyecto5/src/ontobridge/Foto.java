package ontobridge;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Nota: tal como esta esta clase no se deberia usar, ya que las fotos pasan a ser individuos ahora que tienen sus propias propiedades
 * Mirar los ejemplos de ontobridge para ver eso, que queda mucho mas claro.
 * @deprecated
 */
public class Foto {
	
	private String ruta;
	private String nombre;
	private HashMap<String, ArrayList<String>> listaEtiquetado;
	
	
	public Foto(String ruta, String nombre){
		this.ruta = ruta;
		this.nombre = nombre;
		listaEtiquetado = new HashMap<String, ArrayList<String>>();
	}


	public void setIndividuos(ArrayList<String> instancias, String categoria) {
		listaEtiquetado.put(categoria, instancias);
	}
	
	public void inicializaCategorias(ArrayList<String> categorias){
		for (int i = 0; i < categorias.size(); i++){
			listaEtiquetado.put(categorias.get(i), new ArrayList<String>());
		}
	}
	
	public void addIndividuo(String categoria, String individuo){
		ArrayList<String> l = listaEtiquetado.get(categoria);
		if (l != null)
			l.add(individuo);
	}


	public ArrayList<String> getIndividuos(String categoria) {
		return listaEtiquetado.get(categoria);
	}
	
	public String getRuta(){
		return this.ruta;
	}
	

}
