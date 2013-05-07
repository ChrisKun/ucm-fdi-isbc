package recuperadorSemantico;

import java.util.ArrayList;
import java.util.Iterator;
import ontobridge.Ontologia;

/**
 * Las consultas admiten este formato:
 * FOTOS DE: ((propiedad*)? ((clase|instancia)(conector)...(conector)(clase|instancia)))|(clase|instancia)(en clase-juego)?
 */
public class Recuperador {
	// Lista de propiedades pedidas
	ArrayList<InfoCadena> propiedades;
	// Lista de clases pedidas
	ArrayList<InfoCadena> clases;
	// Lista de instancias pedidas
	ArrayList<InfoCadena> instancias;
	// Estructuras auxiliares
	String juego;
	Ontologia ontologia;
	
	/**
	 * Clase interna para saber en que fase del procesamiento estamos 
	 */
	private class InfoFase {
		boolean existe;
		int fase;
		InfoFase (boolean existe, int fase) {
			this.existe = existe;
			this.fase = fase;
		}
	}
	
	/**
	 * Clase interna para sacar informacion de cada elemento de la cadena
	 */
	private class InfoCadena {
		// Union representa si un elemento se añade a la cadena si ya esta o no
		// True - Se añade siempre | False - Se añade solo si ya esta
		boolean union;
		String cadena;
		InfoCadena (boolean union, String cadena) {
			this.union = union;
			this.cadena = cadena;
		}
	}
	
	/**
	 * Constructora que carga la ontologia
	 * @param ontologia
	 */
	public Recuperador(Ontologia ontologia) {
		this.ontologia = ontologia;
	}
	
	/**
	 * Funcion para comprobar si un conector es de Interseccion
	 */
	private boolean esInterseccion(String argumento) {
		if (argumento.matches("y|con|\\,"))
			return true;
		else
			return false;
	}
	
	/**
	 * Funcion para comprobar si un conector es de Union
	 */
	private boolean esUnion(String argumento) {
		if (argumento.matches("o"))
			return true;
		else
			return false;
	}
	
	/**
	 * Funcion de Analisis Semantico 
	 * @param argumentos - Array de argumentos a analizar
	 * @throws Exception - Excepcion para notificar que la consulta no se ciñe a los parámetros
	 */
	private void analisisSemantico(String[] argumentos) throws Exception {
		InfoFase token;
		int fase = 0;
		// Una clase se tiene
		boolean union = true;
		// Inicialmente el argumento anterior es vacio, asi que es valido
		boolean argumentoAnteriorValido = true;
		for (String argumento : argumentos) {
			// Comprobamos si el argumento es de union
			if (esUnion(argumento) && argumentoAnteriorValido) {
				union = true;
				argumentoAnteriorValido = false;
				continue;
			// Comprobamos si el argumento es de interseccion
			} else if (esInterseccion(argumento) && argumentoAnteriorValido) {
				union = false;
				argumentoAnteriorValido = false;
				continue;
			// Comprobamos si ya llegamos a la parte de que se nos pide la foto concreta
			} else if (argumento.matches("en")) {
				// Pasamos a la fase de chequear la foto
				fase = 2;
				continue;
			}
			// Comprobamos si el argumento es valido 
			token = analisisCategoria(argumento, fase, union);
			if (token.existe) {
				// Si el argumento pertenece a la ontologia, lo marcamos como valido
				argumentoAnteriorValido = true;
				fase = token.fase;
			} else {
				throw new Exception("El argumento " + argumento + " no se encuentra");
			}
		}
	}
	
	private InfoFase analisisCategoria(String argumento, int fase, boolean union) {
		boolean existe = true;
		/*
		 *  Dependiendo de la fase se analiza un tipo de elemento u otro
		 *  Fase 0: Analizamos propiedades
		 *  Fase 1: Analizamos clases o individuos
		 *  Fase 2: Se analiza el nombre del juego
		 *  Fase 3+: Ya hemos acabado de analizar si hay algo mas, esta mal la cadena
		 */		
		switch (fase) {
		case 0:
			if (ontologia.getOb().existsProperty(argumento)) {
				propiedades.add(new InfoCadena(union, argumento));
				fase = 0;
				break;
			}
		case 1:
			if (ontologia.getOb().existsClass(argumento)) {
				clases.add(new InfoCadena(union, argumento));
				fase = 1;				
				break;
			}
			else if (ontologia.getOb().existsInstance(argumento)) {	
				instancias.add(new InfoCadena(union, argumento));
				fase = 1;
				break;
			}			
		case 2:
			if (ontologia.getOb().existsInstance(argumento, "Juego")) {
				juego = new String(argumento);
				fase = 3;
				break;
			}
		default:
			existe = false;
		}
		return new InfoFase(existe, fase);
	}
	
	/**
	 * Con toda la informacion de instancias, clases y propiedades
	 * Ejecutamos las consultas en OntoBridge
	 * @return ArrayList<String> con los nombres de los individuos que cumplen lo pedido
	 */
	private ArrayList<String> ejecutarConsultas() {		
		ArrayList<String> resultado = new ArrayList<String>();
		ArrayList<String> listaInterseccion = new ArrayList<String>();
		ArrayList<String> individuosFotos = new ArrayList<String>();
		Iterator<String> iterador, iterador2;
		String aux;
		// Sacamos las iteradorancias de todas las clases
		for(InfoCadena clase: clases) {
			iterador = ontologia.getOb().listInstances(clase.cadena);
			while (iterador.hasNext()) {
				// A cada instancia le ponemos el valor de union correspondiente a la clase
				instancias.add(new InfoCadena(clase.union, iterador.next()));
			}
		}
		// Sacamos los individuos que cumplen las propiedades pedidas
		for (InfoCadena propiedad: propiedades) {
			// Por cada individuo sacamos los valores de la propiedad
			for (InfoCadena instancia: instancias) {
				iterador = ontologia.getOb().listPropertyValue(instancia.cadena, propiedad.cadena);
				if (instancia.union) {
					// En caso de union si no esta lo unimos 
					while (iterador.hasNext()) {
						aux = iterador.next();
						if (!resultado.contains(aux)) {						
							resultado.add(aux);
						}
					}
				} else {
					// En caso de interseccion solo dejamos los comunes 
					while (iterador.hasNext()) {
						aux = iterador.next();
						if (resultado.contains(aux)) {
							listaInterseccion.add(aux);
						}
					}
					resultado.clear();
					resultado.addAll(listaInterseccion);
				}
			}			
		}
		// Sacamos los individuos que estan catalogados en fotos del juego pedido
		// Si no se pide juego, no hacemos nada
		if (juego != null) {
			// Fotos del juego
			iterador = ontologia.getOb().listPropertyValue(juego, "sale_en_foto");
			while (iterador.hasNext()) {
				// Sacamos los individuos de cada foto
				iterador2 = ontologia.getOb().listPropertyValue(iterador.next(), "aparece");
				while(iterador2.hasNext()) {
					aux = iterador2.next();
					// Si no estaba el individuo añadido, lo añadimos
					if (!individuosFotos.contains(aux)) {
						individuosFotos.add(aux);
					}
				}
				
			}
			// Intersecamos ambos conjuntos (resultado y los individuos de las fotos)
			for(String individuo: resultado) {
				if (!individuosFotos.contains(individuo)) {
					resultado.remove(individuo);
				}
			}	
		}
		return resultado;
	}
	
	/**
	 * Funcion de consulta de cadenas
	 * @param consulta 		- String pasado como consulta 
	 * @throws Exception	- Excepcion para notificar que la consulta no se ciñe a los parámetros
	 */
	public void consulta(String consulta) throws Exception {
		// Creamos las estructuras
		propiedades = new ArrayList<InfoCadena>();
		clases = new ArrayList<InfoCadena>();
		instancias = new ArrayList<InfoCadena>();
		juego = null;
		// Separamos las comas
		consulta = consulta.replaceAll("\\, ", " \\, ");
		String argumentos[] = consulta.split(" ");
		// Hacemos el analisis semantico
		analisisSemantico(argumentos);
		// Ejecutamos las consultas en OntoBridge
		ejecutarConsultas();
		// Limpiamos las estructuras para futuros usos
		clases.clear();
		instancias.clear();
		propiedades.clear();
		juego = null;
	}

	public static void main(String[] args) throws Exception{
		String pathOntologia = "file:src/ontologia/etiquetado.owl";// "file:proyecto5/src/ontologia/etiquetado.owl"; //"file:src/ontologia/etiquetado.owl";
		String urlOntologia = "http://http://sentwittment.p.ht/";
		Ontologia ontologia = new Ontologia(urlOntologia, pathOntologia);
		Recuperador r = new Recuperador(ontologia);
		r.consulta("enemigo_de Link y Zelda");	
		//r.consulta("enemigo_de Link, Ganondorf");	
	}
}