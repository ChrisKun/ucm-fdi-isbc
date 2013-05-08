package Controlador;

import interfaz.VentanaPrincipal;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import clasificador.Config;
import clasificador.Main;
import ontobridge.Ontologia;
import recuperadorSemantico.Recuperador;
import utilidades.Ficheros;

public class Controlador {

	Ontologia modelo; 
	VentanaPrincipal vista;
	ArbolPersonalizado treeContenido; //arbol con raiz: CONTENIDO
	ArbolPersonalizado treeFoto; //arbol con raiz: Foto
	ArrayList<Component> trees;
	Recuperador recuperador;
	
	public Controlador(Ontologia modelo){
		this.modelo = modelo;
		treeContenido = new ArbolPersonalizado(modelo.getOb(),true, Config.SeleccionArbol.Contenido.toString());
		treeFoto = new ArbolPersonalizado(modelo.getOb(),true, Config.SeleccionArbol.Foto.toString());
		// Incialmente activamos el arbol de contenido
		trees = new ArrayList<Component>();
		trees.add(treeContenido);
		trees.add(treeFoto);
		// Creamos el recuperador
		recuperador = new Recuperador(modelo);
	}
	
	public void setVista(VentanaPrincipal vista){
		this.vista = vista;
	}
	
	/**
	 * Devuelve el árbol de Ontobridge ACTIVO correctamente configurado.
	 * @return
	 */
	public ArrayList<Component> getTrees(){
		return trees;
	}
	
	/**
	 * Devuelve el modelo
	 * @return Ontologia (modelo)
	 */
	public Ontologia getModelo() {
		return modelo;
	}
	
	/**
	 * Devuelve todas las propiedades de la ontologia aplicables a Contenido y que no son "aparece" y "aparece_en"
	 * @return ArrayList<String> con las propiedades
	 */
	public ArrayList<String> getPropiedades() {
		ArrayList<String> lista = new ArrayList<String>();
		String aux, aux1;
		Iterator<String> iterador, iterador1;
		iterador = modelo.getOb().listSubClasses(modelo.getOb().getURI("Contenido"),true);
		while (iterador.hasNext()) {
			aux = iterador.next();
			iterador1 = modelo.getOb().listProperties(aux);
			while (iterador1.hasNext()) {
				aux1 = iterador1.next();
				if (!aux1.contains("aparece") && !aux1.contains("tiene"))
					lista.add(modelo.getOb().getShortName(aux1));
			}
		}
		return lista;
	}
	
	/**
	 * Devuelve todos los individuos y clases de la ontologia
	 * @return ArrayList<String> con las clases e instancias
	 */
	public ArrayList<String> getIndividuosYClases() {
		ArrayList<String> lista = new ArrayList<String>();
		Iterator<String> iterador = modelo.getOb().listInstances(modelo.getOb().getURI("Contenido"));
		while (iterador.hasNext()) {
			lista.add(modelo.getOb().getShortName(iterador.next()));
		}
		iterador = modelo.getOb().listSubClasses(modelo.getOb().getURI("Contenido"), true);
		while (iterador.hasNext()) {
			lista.add(modelo.getOb().getShortName(iterador.next()));
		}
		return lista;
	}
	
	/**
	 * Devuelve todas las instancias de Juego
	 * @return ArrayList<String> con las instancias de Juego
	 */
	public ArrayList<String> getJuegos() {
		ArrayList<String> lista = new ArrayList<String>();
		Iterator<String> iterador = modelo.getOb().listInstances(modelo.getOb().getURI("Juego"));
		while (iterador.hasNext()) {
			lista.add(modelo.getOb().getShortName(iterador.next()));
		}
		return lista;
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
	 * Devuelve en una lista los individuos que pueden asociarse a una propiedad
	 * pasada por parámetro
	 * @param propiedad
	 * @return rango de instancias que pueden rellenar la propiedad
	 */
	public ArrayList<String> getIndividuosValidosRellenarPropiedad(String propiedad)
	{
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> rango = new ArrayList<String>();
		String uriPropiedad = modelo.getOb().getURI(propiedad);
		Iterator<String> itInd;
		// Si la propiedad no existe, salimos
		
		if (!modelo.getOb().existsProperty(uriPropiedad))
			return list;
		
		// Si existe, continuamos
		Iterator<String> it = modelo.getOb().listPropertyRange(uriPropiedad);
		while (it.hasNext())
			rango.add(it.next());
		
		//Hay que devolver todos los individuos que cumplan el rango
		for (int i = 0; i < rango.size(); i++){
			//Obtenemos todos los individuos que satisfacen uno de los rangos
			itInd = modelo.getOb().listInstances(rango.get(i));
			
			while (itInd.hasNext())
				list.add(modelo.getOb().getShortName(itInd.next()));
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
		Iterator<String> it = modelo.getOb().listSpecificProperties((clasesContenido.get(contenido)));
		
		while (it.hasNext()){
			aux = it.next();
			list.add(modelo.getOb().getShortName(aux));
		}
		return list;
	}
	
	/**
	 * Devuelve las propiedades que puede tener un individuo
	 * @param individuo
	 * @return
	 */
	public ArrayList<String> getPropiedadesIndividuo(String individuo){
		ArrayList<String> str = new ArrayList<String>();
		String uriIndividuo = modelo.getOb().getURI(individuo);
		Iterator<String> it = modelo.getOb().listInstanceProperties(uriIndividuo);
		
		while (it.hasNext())
			str.add(it.next());
		
		return str;
	}
	
	/**
	 * Elimina todos los valores asociados a una misma propiedad. Por ejemplo
	 * aparece Zelda, aparece Link y mandamos borrar aparece, borraria todas
	 * las propiedades asociadas a aparece (Link y Zelda)
	 * @param propiedad
	 * @return
	 */
	public boolean eliminarTodosValoresDePropiedad(String individuo, String propiedad){
		boolean existe = false;
		String uriPropiedad = modelo.getOb().getURI(propiedad);
		String uriIndividuo = modelo.getOb().getURI(individuo);
		existe = modelo.getOb().existsProperty(uriPropiedad) &&
				modelo.getOb().existsInstance(uriIndividuo);
		if (!existe)
			return existe;
		//2 Existe, por tanto eliminamos todas los valores asociados a una misma propiedad
		modelo.getOb().deleteProperties(uriIndividuo, uriPropiedad);
		return existe;
	}
	
	/**
	 * Elimina un individuo de la ontologia
	 * @param individuo
	 * @return
	 */
	public boolean eliminarIndividuo(String individuo){
		modelo.getOb().delete(modelo.getOb().getURI(individuo));
		return true;
	}
	
	/**
	 * Elimina todos los individuos etiquetados en una foto
	 * @param foto
	 * @return
	 */
	public boolean limpiarFoto(String foto){
		return eliminarTodosValoresDePropiedad(foto, Config.aparece);
	}
	
	/**
	 * Permite aniadir un individuo EXISTENTE a una propiedad EXISTENTE de otro
	 * individuo EXISTENTE
	 * @param invididuo - individuo que contiene la propiedad
	 * @param invididuoP - individuo que queremos incluir en la propiedad
	 * @param propiedad - propiedad del individuo donde incluiremos el individuoP
	 * @return (TRUE o FALSE) Si la operacion ha tenido exito
	 */
	public boolean anadirInvididuoAPropiedad(String individuo, String individuoP, String propiedad){
		boolean exito = false;
		//1. Pasar todo a URIs de la ontologia
		String uriIndividuo = modelo.getOb().getURI(individuo);
		String uriIndividuoP = modelo.getOb().getURI(individuoP);
		String uriPropiedad = modelo.getOb().getURI(propiedad);
		//2. Comprobar que las 3 existen en la ontologia
		exito = (modelo.getOb().existsInstance(uriIndividuo) &&
				modelo.getOb().existsInstance(uriIndividuoP) &&
				modelo.getOb().existsProperty(uriPropiedad));
		//3. Si alguna no exite, no continuamos y devolvemos FALSE
		if (!exito)
			return exito;
		//4. Comprobar además que el individuo a etiquetar pertenece a alguno
		// de los tipos de contenido
		exito = comprobarSiEsTipoContenido(uriIndividuoP);
		if (!exito)
			return exito;
		//6. añadimos
		modelo.getOb().createOntProperty(uriIndividuo, uriPropiedad, uriIndividuoP);
		return exito;
	}
	
	/**
	 * Elimina un individuo EXISTENTE asociado a una propiedad de otro individuo
	 * @param individuo del que se quiere eliminar
	 * @param individuoP que se va a borrar
	 * @see Solo funciona para individuos aniadidos desde ontobridge
	 * @param propiedad que los asocia
	 * @return TRUE o FALSE
	 */
	public boolean eliminarIndividuoDePropiedad(String individuo, String individuoP, String propiedad){
		boolean exito = false;
		//1. Pasar todo a URIs de la ontologia
		String uriIndividuo = modelo.getOb().getURI(individuo);
		String uriIndividuoP = modelo.getOb().getURI(individuoP);
		String uriPropiedad = modelo.getOb().getURI(propiedad);
		//2. Comprobar que las 3 existen en la ontologia
		exito = (modelo.getOb().existsInstance(uriIndividuo) &&
				modelo.getOb().existsInstance(uriIndividuoP) &&
				modelo.getOb().existsProperty(uriPropiedad));
		//3. Si alguna no exite, no continuamos y devolvemos FALSE
		if (!exito)
			return exito;
		//4. eliminamos
		modelo.getOb().deleteOntProperty(uriIndividuo, uriPropiedad, uriIndividuoP);
		return exito;
	}
	
	
	
	/**
	 * Comprueba si un individuo pertenece a una de las clases de Tipo de Contenido
	 * @param individuo
	 * @return
	 */
	private boolean comprobarSiEsTipoContenido(String individuo){
		boolean esDeTipoContenido = false;
		ArrayList<String> tiposContenido = getTiposDeContenido();
		for (int i = 0; i < tiposContenido.size(); i++){
				if (modelo.getOb().isInstanceOf(individuo, modelo.getOb().getURI(tiposContenido.get(i))))
					esDeTipoContenido = true;
		}
		return esDeTipoContenido;
	}
	
	/**
	 * Permite incluir un individuo en una foto
	 * @param individuo - el que queremos anadir en la foto
	 * @param foto - la foto que queremos que se incluya el individuo
	 * @return
	 */
	public boolean anadirIndividuoAFoto(String individuo, String foto){
		return this.anadirInvididuoAPropiedad(foto, individuo, Config.aparece);
	}
	
	/**
	 * Permite eliminar un individuo de una foto
	 * @param individuo que queremos borrar de la foto
	 * @see Solo funciona para individuos aniadidos desde ontobridge
	 * @param foto de la que queremos quitar el individuo
	 * @return
	 */
	public boolean eliminarIndividuoDeFoto(String individuo, String foto){
		return this.eliminarIndividuoDePropiedad(foto, individuo, Config.aparece);
	}
	
	/**
	 * Permite crear un individuo de una clase indicada y rellena sus
	 * propiedades. Tambien podrian ser fotos
	 * @param cont - clase a la que pertenece la instancia a crear
	 * @param valoresPropiedades - valores de las propiedades que tiene que tener la instancia
	 */
	public void crearIndividuo(int cont, HashMap<String,ArrayList<String>> valoresPropiedades){
		// 1. Sacamos el nombre completo de la clase de pertenencia
		String uriClase = modelo.getOb().getURI(getTiposDeContenido().get(cont));
		// 2. Comprobamos que existe
		if (!modelo.getOb().existsClass(uriClase))
			return;
		// 3. Creamos la instancia de esa clase
		String nombreInstancia = valoresPropiedades.get("Nombre").get(0);
		modelo.getOb().createInstance(uriClase,valoresPropiedades.get("Nombre").get(0));
		ArrayList<String> propiedades = getPreguntasARellenar(cont);
		// 4. Ahora tenemos que rellenar las propiedades de la instancia
		// Para ello recuperamos los punteros a propiedades y valores
		for (int i = 0; i < propiedades.size(); i++){
			ArrayList<String> valorPropiedad = valoresPropiedades.get(propiedades.get(i));
			//Obtenemos la lista de valores de esa propiedad
			//Rellenamos con toda la lista de valores esa propiedad
			for (int j = 0; j < valorPropiedad.size(); j++){
				if(!valorPropiedad.get(j).equals(""))
					modelo.getOb().createOntProperty(nombreInstancia, propiedades.get(i), valorPropiedad.get(j));
			}
		}
	}
	
	/**
	 * Devuelve la instancia con la que estamos trabajando en este momento, no tiene por qué
	 * ser un String pero hay que hablar de que devolvemos aquí.
	 * @return la instancia actual seleccionada
	 */
	public String getInstanciaActualSeleccionada(){
		String s;
		try{
			s = modelo.getOb().getShortName(treeContenido.getSelectedInstance());
		}catch(NullPointerException e){
			s = "--???--";
		}		
		return s;
	}

	/** 
	 * Funcion de guardado de la ontologia
	 */
	public void guardarOntologia() {
		modelo.getOb().save(Main.pathOntoSinFile);
	}

	/**
	 * Funcion de carga de una nueva ontologia
	 * @param string 
	 */
	public void cargarOntologia(String string) {
		modelo = new Ontologia(Main.urlOntologia, Main.pathOntologia);
	}
	
	public int dameIndiceContenido(String contenido){
		int index = -1;
		boolean enc = false;
		ArrayList<String> s = this.getTiposDeContenido();
		for (int i = 0; i < s.size() && !enc; i++){
			if (s.get(i).equals(contenido)){
				enc = true;
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Añade las fotos al modelo 
	 * @param urlFotos - Recibe unas url's y añade al modelo los nombres de las url's
	 * @return Entero con el numero de fotos añadidas
	 */
	public int addFotosModelo(ArrayList<String> urlFotos) {
		int numeroAñadido = 0;
		String nombre;
		for (String urlFoto: urlFotos) {
			nombre = nombreFoto(urlFoto);
			if (!modelo.getOb().existsInstance(modelo.getOb().getURI(nombre))) {
				if (tieneFormatoCorrecto(urlFoto)) {
					modelo.getOb().createInstance("Foto", nombreFoto(nombre));
					modelo.getOb().createDataTypeProperty(nombre, modelo.getOb().getURI("urlfoto"), urlFoto);					
					numeroAñadido++;
				}
			}
		}	
		return numeroAñadido;
	}
	
	/**
	 * Ejecuta una consulta en la ontologia
	 * @param consulta - String con la consulta
	 * @return Devuelve un ArrayList<String> con los individuos
	 * @throws Exception si la consulta no funciona
	 */
	public ArrayList<String> ejecutaConsulta(String consulta) throws Exception {
		return recuperador.consulta(consulta);
	}
	
	/**
	 * Muestra el uso del recuperador
	 * @return ArrayList<String> para mostrar el uso
	 */
	public ArrayList<String> usoRecuperador() {
		return recuperador.uso();
	}
	
	/******************** METODOS PRIVADOS ***************************/
	/**
	 * Extrae el nombre de una foto de una url
	 * @param urlFoto - url de la foto
	 * @return String con el nombre de la foto
	 */
	private String nombreFoto(String urlFoto) {
		String separador = String.valueOf(File.separatorChar) + String.valueOf(File.separatorChar);
		String[] tokens = urlFoto.split(separador);
		return tokens[tokens.length-1];
	}
	
	/**
	 * Funcion para chequear el formato de una foto
	 * @param foto a chequear
	 * @return true - formato valido, falso en caso contrario
	 */
	private boolean tieneFormatoCorrecto(String foto) {
		if (foto.endsWith(".jpg") || foto.endsWith(".jpeg") ||
				foto.endsWith(".gif") || foto.endsWith(".png"))
			return true;
		else 
			return false;
	}	
	
	// TODO: Main para pruebas, eliminar cuando no se necesite
	public static void main(String[] args) throws Exception{
		String pathOntologia = "file:src/ontologia/etiquetado_limpio.owl";
		String urlOntologia = "http://http://sentwittment.p.ht/";
		Ontologia ontologia = new Ontologia(urlOntologia, pathOntologia);
		Controlador r = new Controlador(ontologia);
		Ficheros f = new Ficheros();
		ArrayList<String> lista = f.ficheros(Main.gamesPath);
		r.addFotosModelo(lista);
		/*
		ArrayList<String> lista = new ArrayList<String>();
		lista.add("\\fotos\\starfox\\images.jpg");
		lista.add("\\fotos\\starfox\\sfa108.jpg");
		r.añadeFotosModelo(lista);	
		*/
		r.guardarOntologia();
	}
}