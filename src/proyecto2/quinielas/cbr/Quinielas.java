package proyecto2.quinielas.cbr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import proyecto2.quinielas.interfaz.BarraProgreso;
import proyecto2.quinielas.datosWeb.Clasificacion;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.evaluation.Evaluator;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.ParallelNNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.selection.SelectCases;

/**
 * 
 * Clase encargada de ejecutar todo el ciclo CBR
 *
 */

public class Quinielas implements StandardCBRApplication {

	/* CONSTANTES */
	// Numero de casos que recuperamos en el algoritmo KNN
	final static int K = 5;		
	
	Connector connector;
	CBRCaseBase caseBase;
	
	// HashMaps con las clasificaciones de ambas ligas
	HashMap<String,ArrayList<Clasificacion>> clasPorJornPrim;
	HashMap<String,ArrayList<Clasificacion>> clasPorJornSeg;
	// ArrayList que contiene los resultados
	ArrayList<Prediccion> listaPredicciones;
	// Array que contiene la lista de pesos
	double[] listaPesos;
	// Booleano que indica si estamos ejecutando una evaluación o no
	boolean esValidacion;
	// Booleano que indica el tipo de votación que ha pedido el usuario
	boolean media;
	
	/* CONSTRUCTORAS */
	public Quinielas (double[] listaPesos, HashMap<String,ArrayList<Clasificacion>> clasPorJornPrim, HashMap<String,ArrayList<Clasificacion>> clasPorJornSeg) {
		this.listaPesos = listaPesos;
		this.clasPorJornPrim = clasPorJornPrim;
		this.clasPorJornSeg = clasPorJornSeg;
		listaPredicciones = new ArrayList<Prediccion>();
		esValidacion = false;
		media = false;
	}
	
	public Quinielas (boolean esEvaluacion, double[] listaPesos) {
		// listaPredicciones = new ArrayList<Prediccion>();
		this.listaPesos = listaPesos;
		esValidacion = true;
	}
	
	/* GETTERS */
	public ArrayList<Prediccion> getListaPredicciones (){
		return this.listaPredicciones;
	}
	
	/**
	 * 
	 * @param liga Un '1' para primera y un '2' para segunda
	 * @param anyo El año del que quieras obtener la clasificación
	 * @param jornada La jornada de la que quieres la clasificación
	 * @return
	 */
	public ArrayList<Clasificacion> getClasificacion(int liga, int anyo, int jornada){
		String indice = "A"+anyo+"J"+jornada;
		if (liga == 1)
			return clasPorJornPrim.get(indice);
		else if (liga == 2)
			return clasPorJornSeg.get(indice);
		else
			return null;
	}
	
	/* METODOS */	
	@Override
	public void configure() {
		// Crear el conector con la base de casos
		connector = new ConnectorQuinielas();
		// La organización en memoria será lineal y cacheada (para que en la evaluación no esté
		// cambiando el medio de persistencia constantemente
		// caseBase = new LinealCaseBase();
		caseBase = new CachedLinealCaseBase();	
	}

	/**
	 * Ejecuta el preciclo
	 * @throws ExecutionException
	 */
	public CBRCaseBase preCycle() throws ExecutionException {		
		// Cargar los casos desde el conector a la base de casos
		try {
			caseBase.init(connector);
			if(caseBase.getCases().isEmpty())
				throw new ExecutionException("Fichero de clasificaciones vacio");
		} catch (ExecutionException e) {
			throw e;
		}
		return caseBase;
	}

	/**
	 * Ejecuta el configure() y el preciclo()
	 * @throws ExecutionException
	 */
	public void configCBR () throws ExecutionException {		
		try {
			//Configuración
			configure();		
			//Preciclo
			preCycle();
		} catch (ExecutionException e) {
			throw e;
		}
	}
	
	/**
	 * Ejecuta el ciclo
	 * @param query
	 * @throws ExecutionException 
	 */
	public void cycle(CBRQuery query) throws ExecutionException {
		// Para configurar el KNN se utiliza un objeto NNConfig
		NNConfig simConfig = new NNConfig();
		
		// Fijamos la función de similitud global
		simConfig.setDescriptionSimFunction(new Average());		
		
		// Fijamos las funciones de similitud locales	
		try {
			simConfig.addMapping(new Attribute("temporada", DescripcionQuinielas.class), new Interval(13));
			simConfig.addMapping(new Attribute("local", DescripcionQuinielas.class), new Equal());
			simConfig.addMapping(new Attribute("visitante", DescripcionQuinielas.class), new Equal());
			simConfig.addMapping(new Attribute("puntosLocal", DescripcionQuinielas.class), new Interval(126));
			simConfig.addMapping(new Attribute("pgLocal", DescripcionQuinielas.class), new Interval(42));
			simConfig.addMapping(new Attribute("peLocal", DescripcionQuinielas.class), new Interval(42));
			simConfig.addMapping(new Attribute("ppLocal", DescripcionQuinielas.class), new Interval(42));
			simConfig.addMapping(new Attribute("puntosVisitante", DescripcionQuinielas.class), new Interval(126));
			simConfig.addMapping(new Attribute("pgVisitante", DescripcionQuinielas.class), new Interval(42));
			simConfig.addMapping(new Attribute("peVisitante", DescripcionQuinielas.class), new Interval(42));
			simConfig.addMapping(new Attribute("ppVisitante", DescripcionQuinielas.class), new Interval(42));
			simConfig.addMapping(new Attribute("posLocal", DescripcionQuinielas.class), new Interval(22));
			simConfig.addMapping(new Attribute("posVisitante", DescripcionQuinielas.class),	new Interval(22));
			simConfig.addMapping(new Attribute("gfavorLocal", DescripcionQuinielas.class), new Interval(130));
			simConfig.addMapping(new Attribute("gcontraLocal", DescripcionQuinielas.class), new Interval(120));
			simConfig.addMapping(new Attribute("gfavorVisitante", DescripcionQuinielas.class), new Interval(130));
			simConfig.addMapping(new Attribute("gcontraVisitante", DescripcionQuinielas.class), new Interval(120));
		} catch (Exception e){
			ExecutionException ex = new ExecutionException("Fallo al fijar las funciones de similitud");
			throw ex;
		}
		
		// Asignamos pesos
		try {
			simConfig.setWeight(new Attribute("temporada", DescripcionQuinielas.class), listaPesos[0]);
			simConfig.setWeight(new Attribute("local", DescripcionQuinielas.class), listaPesos[1]);
			simConfig.setWeight(new Attribute("visitante", DescripcionQuinielas.class), listaPesos[2]);
			simConfig.setWeight(new Attribute("puntosLocal", DescripcionQuinielas.class), listaPesos[3]);
			simConfig.setWeight(new Attribute("pgLocal", DescripcionQuinielas.class), listaPesos[4]);
			simConfig.setWeight(new Attribute("peLocal", DescripcionQuinielas.class), listaPesos[5]);
			simConfig.setWeight(new Attribute("ppLocal", DescripcionQuinielas.class), listaPesos[6]);
			simConfig.setWeight(new Attribute("puntosVisitante", DescripcionQuinielas.class), listaPesos[7]);
			simConfig.setWeight(new Attribute("pgVisitante", DescripcionQuinielas.class), listaPesos[8]);
			simConfig.setWeight(new Attribute("peVisitante", DescripcionQuinielas.class), listaPesos[9]);
			simConfig.setWeight(new Attribute("ppVisitante", DescripcionQuinielas.class), listaPesos[10]);
			simConfig.setWeight(new Attribute("posLocal", DescripcionQuinielas.class), listaPesos[11]);
			simConfig.setWeight(new Attribute("posVisitante", DescripcionQuinielas.class), listaPesos[12]);
			simConfig.setWeight(new Attribute("gfavorLocal", DescripcionQuinielas.class), listaPesos[13]);
			simConfig.setWeight(new Attribute("gcontraLocal", DescripcionQuinielas.class), listaPesos[14]);
			simConfig.setWeight(new Attribute("gfavorVisitante", DescripcionQuinielas.class), listaPesos[15]);
			simConfig.setWeight(new Attribute("gcontraVisitante", DescripcionQuinielas.class), listaPesos[16]);
		} catch (Exception e){
			ExecutionException ex = new ExecutionException("Fallo al asignar pesos");
			throw ex;
		}
		
		try {
			// Ejecutamos la recuperación del vecino más próximo (usando el método en paralelo)
			Collection<RetrievalResult> eval = ParallelNNScoringMethod.evaluateSimilarityParallel(caseBase.getCases(), query, simConfig);
			// Seleccionamos los K mejores casos
			eval = SelectCases.selectTopKRR(eval, K);
					
			// Seleccionamos metodo de votacion (media vs. media ponderada)
			Votacion votacion = new Votacion();
			Prediccion prediccion;
			if (media == true)
				prediccion = votacion.mediaPonderada(eval);		
			else
				prediccion = votacion.media(eval);	
			
			// Si estamos haciendo una validación, NO añadimos la prediccion a la lista de predicciones
			if (esValidacion) validacion(query, prediccion);
			else listaPredicciones.add(prediccion);			
		} catch (Exception e) {
			ExecutionException ex = new ExecutionException("Fallo al ejecutar el algoritmo KNN / media");
			throw ex;
		}
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.caseBase.close();
	}
	
	/**
	 * Añade la información de cada consulta en los métodos de validación
	 * @param query
	 * @param prediccion
	 */
	public void validacion(CBRQuery query, Prediccion prediccion) {
        CBRCase caso = (CBRCase)query;
        SolucionQuinielas sol = (SolucionQuinielas)caso.getSolution();
        double pre;
        
        if(sol.getSolucion().equals(prediccion.getResultado()))
                pre = 1.0;
        else 
                pre = 0.0;
        /*
        System.out.print("********* ");
        System.out.print("Resultado: "+sol.getSolucion().toString()+" - "+"Prediccion: "+prediccion.getResultado() + " -> Confianza= " + "[ " + prediccion.getConfianza() + " ]");
        System.out.println(" *********");
        */
        Evaluator.getEvaluationReport().addDataToSeries("Aciertos", pre);
        Evaluator.getEvaluationReport().addDataToSeries("Confianza", prediccion.getConfianza());
	}
		
	/**
	 *  Ejecuta una consulta pedida por el usuario mediante la interfaz
	 * @param equipos
	 * @param temporada
	 * @param jornada
	 * @param listaPesos
	 * @param liga - 1 o 2
	 * @param media - True = media normal / False = media ponderada
	 * @return ArrayList<Prediccion> listaPredicciones
	 * @throws ExecutionException
	 */
	public ArrayList<Prediccion> querysCBR (ArrayList<String> equipos, int temporada, int jornada, double[] listaPesos, int liga, boolean media) throws ExecutionException {
		// Ponemos la media como la pida el usuario
		this.media = media;
		// Como las jornadas se almacenan en el array empezando en 0, tenemos que restar 1 para cuadrar con la jornada pedida por el usuario
		// Guardamos la jornada a consultar (una menos que la pasada por parámetro), como podemos tener jornadas mal parseadas, este valor puede variar
		int jornadaConsulta = jornada - 2;
		//Crear un objeto que almacena la consulta
		CBRQuery query = new CBRQuery();	
		// Estructuras para almacenar las clasificaciones
		ArrayList<Clasificacion> clasificaciones;
		Clasificacion clasifLocal = null;
		Clasificacion clasifVisitante = null;
		// Tokenizamos cada par de equipos
		String[] tokens = null;		
		// Buscamos para cada par de equipos su clasificacion
		try {
			for (String e: equipos) {	
				tokens = e.split(",");
				// Rellenamos la clasificacion en caso de que la jornada sea mayor que la primera	
				if (jornadaConsulta >  -1) {	
					// Buscamos la clasifiacion de cada uno en la temporada pedida, pero en la jornada ANTERIOR con información disponible
					while (clasifLocal == null && clasifVisitante == null) {
						if ((clasificaciones = getClasificacion(liga,temporada,jornadaConsulta)) == null && jornadaConsulta > -1) {
							jornadaConsulta--;
							continue;
						}
						for (Clasificacion i: clasificaciones) {		
							// Buscamos la clasificación del local
							if (i.getEq() == tokens[0]) {
								clasifLocal = i;
							}
							// Buscamos la clasificación del visitante
							if (i.getEq() == tokens[1]) {
								clasifVisitante = i;
							}
							// Si encontramos ambos, paramos. Si no seguimos buscando en jornadas anteriores
							if (clasifLocal != null && clasifVisitante != null) break;
							else jornadaConsulta--;
						}
					}
					// Si la jornada es la primera, los equipos tienen sus clasificaciones a 0
				} 
				// Si alguna de las clasificaciones está a null es porque, o bien no se ha encontrado o porque estamos pidiendo predicciones
				// de la primera jornada
				if (clasifLocal == null) 
					clasifLocal = new Clasificacion(0,tokens[0],0,0,0,0,0,0,0);
				if (clasifVisitante == null)
					clasifVisitante = new Clasificacion(0,tokens[1],0,0,0,0,0,0,0);
	
				// Rellenamos la query con los valores apropiados
				query.setDescription(new DescripcionQuinielas(Integer.valueOf(temporada),tokens[0],clasifLocal,tokens[1],clasifVisitante));
				
				//Ejecutar el ciclo
				cycle(query);			
				System.out.println("Ejecutado ciclo para el partido: "+tokens[0]+" vs. "+tokens[1]);
				
				BarraProgreso.aumentarBarraProgreso(); // Interfaz para la barra de progreso
			}
			// Copiamos los valores, ya que si no al llamar luego a los partidos de segunda, liamos la información
			ArrayList<Prediccion> listaDevolucion = new ArrayList<Prediccion>();
			for (Prediccion p: listaPredicciones) {
					listaDevolucion.add(p);
			}
			// Limpiamos la lista de predicciones
			listaPredicciones.clear();
			return listaDevolucion;
		} catch (ExecutionException e) {
			throw e;			
		}
	}	
}
