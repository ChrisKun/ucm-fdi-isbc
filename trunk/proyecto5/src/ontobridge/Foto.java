package ontobridge;

import java.util.ArrayList;

public class Foto {
	
	private String ruta;
	private String nombre;
	private ArrayList<String> lugar;
	private ArrayList<String> objeto;
	private ArrayList<String> personaje;
	
	
	public Foto(String ruta, String nombre){
		this.ruta = ruta;
		this.nombre = nombre;
		lugar = new ArrayList<String>();
		objeto = new ArrayList<String>();
		personaje = new ArrayList<String>();
	}


	public void setInstancias(ArrayList<String> instancias, String string) {
		//TODO Hacer enumerado
		if (string.equals("Objeto")){
			this.objeto = instancias;
		}
		else if (string.equals("Personaje")){
			this.personaje = instancias;
		}
		else if (string.equals("Lugar")){
			this.lugar = instancias;
		}
	}


	public ArrayList<String> getInstancias(String string) {
		ArrayList<String> l = null;
		//TODO Enumerado!!!
		if (string.equals("Objeto")){
			l = this.objeto;
		}
		else if (string.equals("Personaje")){
			l = this.personaje;
		}
		else if (string.equals("Lugar")){
			l = this.lugar;
		}
		return l;
	}
	
	public String getRuta(){
		return this.ruta;
	}
	

}
