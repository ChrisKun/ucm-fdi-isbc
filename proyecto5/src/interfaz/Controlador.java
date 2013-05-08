package interfaz;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;


import ontobridge.Ontologia;

public class Controlador {

	Ontologia modelo; 
	VentanaPrincipal vista;
	ArbolPersonalizado treeContenido; //arbol con raiz: CONTENIDO
	ArbolPersonalizado treeFoto; //arbol con raiz: Foto
	ArrayList<Component> trees;
	
	public Controlador(Ontologia modelo){
		this.modelo = modelo;
		treeContenido = new ArbolPersonalizado(modelo.getOb(),true, Config.SeleccionArbol.Contenido.toString());
		treeFoto = new ArbolPersonalizado(modelo.getOb(),true, Config.SeleccionArbol.Foto.toString());
		// Incialmente activamos el arbol de contenido
		
		trees = new ArrayList<Component>();
		trees.add(treeContenido);
		trees.add(treeFoto);
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
	 * FIXME Da un nullPointer??
	 * Permite asociar una instancia de una foto a una imagen poniendo su ruta
	 * @param rutaFoto
	 * @param instanciaFoto
	 */
	public void setRutaFoto(String rutaFoto, String instanciaFoto){
		//modelo.getOb().createDataTypeProperty(modelo.getOb().getURI(instanciaFoto), Config.urlfoto, "file://"+rutaFoto);
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
		Iterator<String> it = modelo.getOb().listProperties(clasesContenido.get(contenido));
		
		while (it.hasNext()){
			aux = it.next();
			list.add(modelo.getOb().getShortName(aux));
		}
		return list;
	}
	
	/**
	 * FIXME Quiza venga bien filtrar esto...
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
	 * TODO
	 * Elimina un individuo de la ontologia
	 * @param individuo
	 * @return
	 */
	public boolean eliminarIndividuo(String individuo){
		String uriIndividuo = modelo.getOb().getURI(individuo);
		return true;
	}
	
	/**
	 * FIXME Falla y no se xq
	 * Devuelve un String con la ruta de la instancia de la foto actual
	 * @param foto
	 * @return
	 */
	public String getRutaInstanciaActual(String instanciaFoto){
		//Iterator<String> it = modelo.getOb().listPropertyValue(modelo.getOb().getURI(instanciaFoto), Config.urlfoto);
		//return it.next();
		return "FIXME";
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
	public void crearIndividuo(int cont, String nombreInstancia, ArrayList<String> valoresPropiedades){
		// 1. Sacamos el nombre completo de la clase de pertenencia
		String uriClase = modelo.getOb().getURI(getTiposDeContenido().get(cont));
		// 2. Comprobamos que existe
		if (!modelo.getOb().existsClass(uriClase))
			return;
		// 3. Creamos la instancia de esa clase
		modelo.getOb().createInstance(uriClase,nombreInstancia);
		ArrayList<String> propiedades = getPreguntasARellenar(cont);
		// 4. Ahora tenemos que rellenar las propiedades de la instancia
		// Para ello recuperamos los punteros a propiedades y valores
		for (int i = 1; i < valoresPropiedades.size(); i++){
			if (!valoresPropiedades.get(i).equals(""))
				modelo.getOb().createOntProperty(nombreInstancia, propiedades.get(i-1), valoresPropiedades.get(i));
		}
		
		/*List<String> valores = new ArrayList();
		List<String> propiedades = new ArrayList();
		modelo.getOb().listInstancePropertiesValues(modelo.getOb().getURI(nombreInstancia), propiedades, valores);
		
		for (int i = 0; i < valoresPropiedades.size();i++){
			valores.add(modelo.getOb().getURI(valoresPropiedades.get(i)));
		}*/
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
	 * XXX Sin testear
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

	public void guardarOntologia() {
		// TODO Auto-generated method stub
		
	}

	public void cargarOntologia() {
		// TODO Auto-generated method stub
		
	}
}