package Cbr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;

import Perfil.Usuario;
import jcolibri.casebase.*;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.selection.SelectCases;

/**
 * Clase que recomienda ropa
 * @author Alvaro
 *
 */
public class Recomendador implements StandardCBRApplication {
	
    /** Connector object */
	Connector connector;
    /** CaseBase object */
	CBRCaseBase caseBase;
	
    /** KNN config */
    NNConfig simConfig;
    
    /** Productos similares */
    ArrayList<Integer> productosSimilares;
    
    /**
     * Crea el recomendador y hace los metodos configure() y preCycle()
     * @throws ExecutionException
     */
    public Recomendador() throws Exception {
    	super();
    	try {
			this.inicio();
		} catch (ExecutionException e) {
			throw e;
		}
    }
    
	public void configure() throws ExecutionException {
		try {
		// Crear el conector con la base de casos
		connector = new Conector();
		// La organizacion en memoria sera lineal
		caseBase = new LinealCaseBase();	
		} catch (Exception e) {
			throw new ExecutionException(e);
		} 		
		// Añadimos las funciones de similitud
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());		
		simConfig.addMapping(new Attribute("categoria", Prenda.class), new Equal());
		simConfig.addMapping(new Attribute("division", Prenda.class), new Equal());
		simConfig.addMapping(new Attribute("precio", Prenda.class), new Interval(500));
		// TODO: Asignar pesos si hace falta
	}

	public CBRCaseBase preCycle() throws ExecutionException {
		// Cargar los casos desde el conector a la base de casos
		try {
			caseBase.init(connector);
			if(caseBase.getCases().isEmpty())
				throw new ExecutionException("Base de datos vacia");
		} catch (ExecutionException e) {
			throw e;
		}
		return caseBase;
	}
		
	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		// Ejecutamos la recuperación del vecino más próximo
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
		// Cogemos los 3 mejores casos
		eval = SelectCases.selectTopKRR(eval, 4);
		Integer id;
		for (RetrievalResult r: eval) {
			// Añadimos la id del producto en caso de NO estar ya añadido y NO ser el mismo que pedimos
			id = Integer.valueOf(r.get_case().getID().toString());
			if (!productosSimilares.contains(id) && !query.getID().equals(id))
				productosSimilares.add(id);			
		}		
	}
	
	@Override
	public void postCycle() throws ExecutionException {
		this.caseBase.close();
		this.connector.close();
	}
	
	/**
	 * Configura el sistema y ejecuta el preciclo
	 * @throws ExecutionException
	 */
	private void inicio() throws ExecutionException {
		try {
			this.configure();
			this.preCycle();
		} catch (ExecutionException e) {
			throw e;
		}
	}
	
	/**
	 * Devuelve la lista de los ID's de los productos similares al producto pedido
	 * @param producto
	 * @return Lista ID's 
	 * @throws Exception
	 */
	public ArrayList<Integer> recomendadosPorProducto(Integer idProducto) throws Exception {	
		productosSimilares = new ArrayList<Integer>();	
	    CBRQuery query = new CBRQuery();
	    query.setDescription(new Prenda(idProducto));
	    try {
			this.cycle(query);
		} catch (ExecutionException e) {
			throw e;
		}
		return productosSimilares;		
	}
	
	/**
	 * Devuelve la lista de los ID's de los productos similares a los que ha comprado el usuario
	 * @param usuario
	 * @return Lista ID's
	 * @throws Exception
	 */
	public ArrayList<Integer> recomendadosPorUsuario(Usuario usuario) throws Exception {	
		productosSimilares = new ArrayList<Integer>();	
		for (Integer idProducto: usuario.getProductosComprados()) {
		    CBRQuery query = new CBRQuery();
		    query.setDescription(new Prenda(idProducto));
		    try {
				this.cycle(query);
			} catch (ExecutionException e) {
				throw e;
			}
		}
		// Comprobamos que ninguno de los productos similares sea alguno que haya comprado el usuario
		for (Integer id: productosSimilares) {
			if (usuario.getProductosComprados().contains(id)) 
				productosSimilares.remove((Object)id);
		}
		return productosSimilares;		
	}	
	
	/**
	 * Devuelve la lista de los ID's de los productos mas comprados
	 * @param usuario
	 * @return Lista ID's
	 * @throws Exception
	 */
	public ArrayList<Integer> recomendadosPorMasComprados() throws Exception {	
		ArrayList<Integer> productosSimilares = new ArrayList<Integer>();	
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		String linea;
		// Comprobamos si hay o no carpeta de usuarios
		File directorio = new File(Usuario.DIR);
		if (!directorio.exists()) 
			throw new Exception("No hay carpeta de usuarios");
		// Listamos todos los usuarios
		File[] usuarios = directorio.listFiles();
		for (File usuario: usuarios) {
			try {
				archivo = new File(Usuario.DIR+usuario.getName()+File.separatorChar+"Compras.txt");	
				// Si no existe el fichero de compras no podemos leer y lo notificamos
				if (!archivo.exists())
					throw new Exception("El usuario "+usuario.getName()+" No tiene fichero de compras");
				fr = new FileReader (archivo);
	            br = new BufferedReader(fr);
	            while((linea = br.readLine()) != null) {
	            	try {
	            	if (!productosSimilares.contains(Integer.valueOf(linea))) 
	            			productosSimilares.add(Integer.valueOf(linea));
	            	// Si hay problema de lectura del fichero lo notificamos
	            	} catch (Exception e) {
	            		throw new Exception("El usuario "+usuario.getName()+" tiene el fichero de compras corrupto");
	            	}
	            }
			} catch (Exception e) {
				throw e;
			} finally {
				br.close();
			}
		}
		return productosSimilares;		
	}		
}
