package interfaz;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import es.ucm.fdi.gaia.ontobridge.test.gui.PnlSelectInstance;

import ontobridge.Ontologia;

public class Controlador {

	Ontologia modelo; 
	VentanaPrincipal vista;
	String rutaOntologia;
	PnlSelectInstance tree; //arbol
	
	public Controlador(Ontologia modelo){
		this.modelo = modelo;
		rutaOntologia = modelo.getOb().getThingURI();
		System.out.println(rutaOntologia);
	}
	
	public void setVista(VentanaPrincipal vista){
		this.vista = vista;
	}
	
	/**
	 * Devuelve el árbol de Ontobridge correctamente configurado.
	 * @return
	 */
	public Component getTree(){
		tree = new PnlSelectInstance(modelo.getOb(),true);
		return tree;
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
		Iterator<String> it = modelo.getOb().listProperties(clasesContenido.get(contenido));
		
		while (it.hasNext()){
			aux = it.next();
			list.add(modelo.getOb().getShortName(aux));
			//TODO Falta comprobar si es obligatoria
		}
		return list;
	}
	
	/**
	 * Devuelve las propiedades de un individuo
	 * @param individuo
	 * @return
	 */
	public Iterator<String> getPropiedadesIndividuo(String individuo){
		return modelo.getOb().listInstanceProperties(individuo);
	}
	
	/**
	 * TODO
	 * Elimina un valor asociado a una propiedad.
	 * @param propiedad - propiedad a eliminar
	 * @param valor - valor de la propiedad
	 * @return si se ha podido hacer
	 */
	public boolean eliminarValorDePropiedad(String propiedad, String valor){
		return true;
	}
	
	/**
	 * TODO
	 * Elimina todos los valores asociados a una misma propiedad. Por ejemplo
	 * aparece Zelda, aparece Link y mandamos borrar aparece, borraria todas
	 * las propiedades asociadas a aparece (Link y Zelda)
	 * @param propiedad
	 * @return
	 */
	public boolean eliminarTodosValoresDePropiedad(String propiedad){
		return true;
	}
	
	/**
	 * Elimina un individuo de la ontologia
	 * @param individuo
	 * @return
	 */
	public boolean eliminarIndividuo(String individuo){
		String uriIndividuo = modelo.getOb().getURI(individuo);
		return true;
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
		boolean exito = this.anadirInvididuoAPropiedad(foto, individuo, Config.aparece);
		return exito;
	}
	
	/**
	 * Permite crear un individuo de una clase indicada y rellena sus
	 * propiedades. Tambien podrian ser fotos
	 * @param clasePertenencia - clase a la que pertenece la instancia a crear
	 * @param valoresPropiedades - valores de las propiedades que tiene que tener la instancia
	 */
	public void crearIndividuo(String clasePertenencia, String nombreInstancia, ArrayList<String> valoresPropiedades){
		// 1. Sacamos el nombre completo de la clase de pertenencia
		String uriClase = modelo.getOb().getURI(clasePertenencia);
		// 2. Creamos la instancia de esa clase
		modelo.getOb().createInstance(uriClase,nombreInstancia);
		// 3. Ahora tenemos que rellenar las propiedades de la instancia
		// Para ello recuperamos los punteros a propiedades y valores
		List<String> valores = new ArrayList();
		List<String> propiedades = new ArrayList();
		modelo.getOb().listInstancePropertiesValues(modelo.getOb().getURI(nombreInstancia), propiedades, valores);
		
		for (int i = 0; i < valoresPropiedades.size();i++){
			valores.add(modelo.getOb().getURI(valoresPropiedades.get(i)));
		}
	}
	
	/**
	 * Devuelve la instancia con la que estamos trabajando en este momento, no tiene por qué
	 * ser un String pero hay que hablar de que devolvemos aquí.
	 * @return la instancia actual seleccionada
	 */
	public String getInstanciaActualSeleccionada(){
		//TODO: Decide que hacer cuando no hay ninguna seleccionada porque peta
		String s;
		try{
			s = modelo.getOb().getShortName(tree.getSelectedInstance());
		}catch(NullPointerException e){
			s = "--???--";
		}		
		return s;
	}

	/**
	 * Devuelve la ruta de las fotos en las que aparece un individuo
	 * @param individuo
	 * @return lista de fotos en las que aparece
	 */
	public ArrayList<String> getFotosAparece(String individuo){
		ArrayList<String> listaFotos = new ArrayList<String>();
		//Primero vemos todas las clases a las que pertenece un individuo
		List<String> properties = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		modelo.getOb().listInstancePropertiesValues(individuo, properties, values);
		/* Ahora buscamos la propiedad de apareceEn para saber en qué fotos aparece y
		 * lo guardamos en un arrayList que es lo que devolvemos
		 */
		for (int i = 0; i < properties.size(); i++){
			String str = modelo.getOb().getShortName(properties.get(i));
			// Ahora comprobamos si es la propiedad que nos interesa
			if (str.equals(Config.apareceEn)){
				listaFotos.add(modelo.getOb().getShortName(values.get(i)));
			}
		}
		//En el caso de que este, lo añadimos al array que devolvemos
		return listaFotos;
	}
}
