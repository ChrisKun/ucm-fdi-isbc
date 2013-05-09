package clasificador;

/**
 * Sirve para poder modificar facilmente los nombres de las propiedades
 * que necesitamos comprobar
 */

public class Config {
	
	/* Atributos */
	public static final String comportamiento = "Comportamiento";
	public static final String daño = "Daño";
	
	public static final String aparece = "aparece";
	public static final String apareceEn = "aparece_en";
	public static final String urlfoto = "urlfoto";
	public static final String juego = "Juego";
	public static final String amigoDe = "amigo_de";
	public static final String enemigoDe = "enemigo_de";
	public static final String esUsado = "es_usado";
	public static final String saleElJuego = "sale_el_juego";
	public static final String tiene = "tiene";
	public static final String usa = "usa";
	public static final String urlFoto = "urlfoto";
	public static final String esquemaUrl = "http://www.w3.org/2001/XMLSchema#string";
	public static final String claseFoto = "Foto";
	public static enum SeleccionArbol {Contenido, Foto}
	
	private static String[] simetricas = {Config.amigoDe, Config.enemigoDe};
	
	private static String[][] inversas = {{Config.usa,Config.esUsado},{Config.aparece, Config.apareceEn}};
	
	public static boolean comprobarPropiedadEsSimetrica(String string){
		boolean sim = false;
		for (int i = 0; i < simetricas.length && !sim; i++){
			sim = simetricas[i].equals(string);
		}
		return sim;
	}
	
	/**
	 * ¡Devolvera null si no tiene, cuidado!
	 * @return
	 */
	public static String getPropiedadInversa(String str){
		for (int i = 0; i < inversas.length; i++){
			if (inversas[i][0].equals(str))
				return inversas[i][1];
			else if (inversas[i][1].equals(str))
				return inversas[i][0];
		}
		return null;
	}
	
	/**
	 * recibe un texto que es un nombre y se encarga de eliminar espacios
	 * al principio y sustituir los espacios intermedios por barras bajas
	 * @param text
	 * @return
	 */
	public static String limpiarNombre(String text) {
		// 1. Quitar espacios iniciales
		String t = text.trim();
		// 2. Sustituir los espacios en blanco por "_"
		String ret = t.replaceAll(" ", "_");
		return ret;
	}
}
