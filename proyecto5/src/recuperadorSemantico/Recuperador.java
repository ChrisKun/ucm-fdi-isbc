package recuperadorSemantico;

import interfaz.Config;

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
	
	/********************************************** FUNCIONES ******************************************************/
	
	/**
	 * Constructora que carga la ontologia
	 * @param ontologia
	 */
	public Recuperador(Ontologia ontologia) {
		this.ontologia = ontologia;
	}
	
	/**
	 * Funcion para obtener el nombre completo del parametro en la ontologia
	 * @param parametro - String cuyo nombre complemeto queremos saber
	 * @return String con el nombre completo
	 */
	private String uri(String parametro) {
		return ontologia.getOb().getURI(parametro);
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
			if (ontologia.getOb().existsProperty(uri(argumento))) {
				propiedades.add(new InfoCadena(union, argumento));
				fase = 0;
				break;
			}
		case 1:
			if (ontologia.getOb().existsClass(uri(argumento))) {
				clases.add(new InfoCadena(union, argumento));
				fase = 1;				
				break;
			}
			else if (ontologia.getOb().existsInstance(uri(argumento))) {
				instancias.add(new InfoCadena(union, argumento));
				fase = 1;
				break;
			}			
		case 2:
			if (ontologia.getOb().existsInstance(uri(argumento), uri(Config.juego))) {
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
		ArrayList<String> lista = new ArrayList<String>();
		ArrayList<String> listaInterseccion = new ArrayList<String>();
		ArrayList<String> fotografias = new ArrayList<String>();
		Iterator<String> iterador, iterador2;
		String aux, aux1;
		// Sacamos las instancias de todas las clases
		for(InfoCadena clase: clases) {
			iterador = ontologia.getOb().listInstances(uri(clase.cadena));
			while (iterador.hasNext()) {
				// A cada instancia le ponemos el valor de union correspondiente a la clase
				instancias.add(new InfoCadena(clase.union, iterador.next()));
			}
		}
		// Comprobamos si hay propiedades
		if (propiedades.size() > 0) {
			// Sacamos los individuos que cumplen las propiedades pedidas
			for (InfoCadena propiedad: propiedades) {
				// Por cada individuo sacamos los valores de la propiedad
				for (InfoCadena instancia: instancias) {
					iterador = ontologia.getOb().listPropertyValue(uri(instancia.cadena), uri(propiedad.cadena));
					if (instancia.union) {
						// En caso de union si no esta lo unimos 
						while (iterador.hasNext()) {
							aux = iterador.next();
							if (!lista.contains(aux)) {						
								lista.add(aux);
							}
						}
					} else {
						// En caso de interseccion solo dejamos los comunes 
						while (iterador.hasNext()) {
							aux = iterador.next();
							if (lista.contains(aux)) {
								listaInterseccion.add(aux);
							}
						}
						lista.clear();
						lista.addAll(listaInterseccion);
					}
				}			
			}
		// Si no hay propiedades, entonces (por ahora) la lista son las instancias
		} else {
			for (InfoCadena instancia: instancias) {
				lista.add(uri(instancia.cadena));
			}
		}
		// Sacamos las fotos del juego pedido
		if (juego != null) {
			// Fotos del juego
			iterador = ontologia.getOb().listPropertyValue(uri(juego), uri(Config.saleEnFoto));
			while (iterador.hasNext()) {
				aux = iterador.next();
				// Sacamos los individuos de cada foto
				iterador2 = ontologia.getOb().listPropertyValue(uri(aux), uri(Config.aparece));
				while (iterador2.hasNext()) {
					aux1 = iterador2.next();
					// Si el individuo estaba en la lista y la foto no esta añadida, la añadimos
					if (lista.contains(aux1) && !fotografias.contains(aux)) {
						fotografias.add(aux);
					}
				}
				
			}
		// Si no se nos restringe a un juego concreto, cogemos todas las fotos	
		} else {
			// Sacamos las fotos finalmente con la informacion de los individuos que queremos
			for (String individuo: lista) {
				iterador = ontologia.getOb().listPropertyValue(individuo, uri(Config.apareceEn));
				while (iterador.hasNext()) {
					aux = iterador.next();
					if (!fotografias.contains(aux)) {
						fotografias.add(aux);
					}
				}
			}		
		}
		// Dejamos la cadena lista con los nombres cortos
		for (String foto: fotografias) {
			foto = ontologia.getOb().getShortName(foto);
			System.out.println(foto);
		}
		return fotografias;
	}
	
	
	private String transformaConsulta(String consulta) {
		consulta = consulta.replaceFirst("(amigos|amigo) de", "amigo_de");
		consulta = consulta.replaceFirst("(enemigos|enemigo) de", "enemigo_de");
		consulta = consulta.replaceFirst("(objetos usados|objeto usado) por", "usa");
		consulta = consulta.replaceFirst("(personajes que usan|personaje que usa)", "es_usado");
		consulta = consulta.replaceFirst("personajes buenos", "comportamiento A_Bueno");
		consulta = consulta.replaceFirst("personajes (malvados|malos)", "comportamiento A_Malo");
		return consulta;
	}
	
	/**
	 * Funcion de consulta de cadenas
	 * @param consulta 		- String pasado como consulta 
	 * @throws Exception	- Excepcion para notificar que la consulta no se ciñe a los parámetros
	 */
	public ArrayList<String> consulta(String consulta) throws Exception {
		// Creamos las estructuras
		propiedades = new ArrayList<InfoCadena>();
		clases = new ArrayList<InfoCadena>();
		instancias = new ArrayList<InfoCadena>();
		juego = null;
		// Separamos las comas
		consulta = consulta.replaceAll("\\, ", " \\, ");
		consulta = transformaConsulta(consulta);

		String argumentos[] = consulta.split(" ");
		// Hacemos el analisis semantico
		analisisSemantico(argumentos);
		// Ejecutamos las consultas en OntoBridge
		ArrayList<String> resultados = ejecutarConsultas();
		// Limpiamos las estructuras para futuros usos
		clases.clear();
		instancias.clear();
		propiedades.clear();
		juego = null;
		// Devolvemos los resultados
		return resultados;
	}
	
	public static void main(String[] args) throws Exception{
		String pathOntologia = "file:src/ontologia/etiquetado.owl";
		String urlOntologia = "http://http://sentwittment.p.ht/";
		Ontologia ontologia = new Ontologia(urlOntologia, pathOntologia);
		Recuperador r = new Recuperador(ontologia);
		r.consulta("amigos de Link o Mario");	
		//r.consulta("enemigo_de Link, Ganondorf");	
	}
	}