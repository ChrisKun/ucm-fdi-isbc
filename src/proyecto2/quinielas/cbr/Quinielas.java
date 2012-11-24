package proyecto2.quinielas.cbr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JOptionPane;

import proyecto1.viajes.TravelDescription;
import proyecto1.viajes.TravelRecommender;
import proyecto2.quinielas.Config;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.evaluation.Evaluator;
import jcolibri.exception.ExecutionException;
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
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
	
	// ArrayList que contiene los resultados
	ArrayList<Prediccion> listaPredicciones;
	// Array que contiene la lista de pesos
	double[] listaPesos;
	// Booleano que indica si estamos ejecutando una evaluación o no
	boolean esValidacion;
	
	/* CONSTRUCTORAS */
	public Quinielas (double[] listaPesos) {
		this.listaPesos = listaPesos;
		listaPredicciones = new ArrayList<Prediccion>();
		esValidacion = false;
	}
	
	public Quinielas (boolean esEvaluacion, double[] listaPesos) {
		// listaPredicciones = new ArrayList<Prediccion>();
		this.listaPesos = listaPesos;
		esValidacion = true;
	}
	
	/* GETTERS/SETTERS */
	public ArrayList<Prediccion> getListaPredicciones (){
		return this.listaPredicciones;
	}
	
	/* METODOS */	
	@Override
	public void configure() throws ExecutionException {
		try{
		// Crear el conector con la base de casos
		connector = new ConnectorQuinielas();
		// La organización en memoria será lineal y cacheada (para que en la evaluación no esté
		// cambiando el medio de persistencia constantemente
		// caseBase = new LinealCaseBase();
		caseBase = new CachedLinealCaseBase();
		} catch (Exception e){
			throw new ExecutionException(e);
		}	
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {		
		// Cargar los casos desde el conector a la base de casos
		caseBase.init(connector);		
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		// Para configurar el KNN se utiliza un objeto NNConfig
		NNConfig simConfig = new NNConfig();
		
		// Fijamos la función de similitud global
		simConfig.setDescriptionSimFunction(new Average());		
		
		// Fijamos las funciones de similitud locales		
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
		
		// Asignamos pesos
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
		
		// Ejecutamos la recuperación del vecino más próximo (usando el método en paralelo)
		// Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
		Collection<RetrievalResult> eval = ParallelNNScoringMethod.evaluateSimilarityParallel(caseBase.getCases(), query, simConfig);
		// Seleccionamos los K mejores casos
		eval = SelectCases.selectTopKRR(eval, K);
		
		// for (RetrievalResult nse : eval) System.out.println(nse.toString());
		
		// Seleccionamos metodo de votacion
		Votacion votacion = new Votacion();
		Prediccion prediccion = votacion.mediaPonderada(eval);		
		
		// Si estamos haciendo una validación no añadimos la prediccion a la lista de predicciones
		if (esValidacion) validacion(query, prediccion);
		else listaPredicciones.add(prediccion);
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.caseBase.close();
	}
	
	public void validacion(CBRQuery query, Prediccion prediccion) {
        CBRCase caso = (CBRCase)query;
        SolucionQuinielas sol = (SolucionQuinielas)caso.getSolution();
        double pre;
        
        if(sol.getSolucion().equals(prediccion.getResultado()))
                pre = 1.0;
        else 
                pre = 0.0;
        // Los println realentizan mucho la ejecución
    /*  System.out.print("********* ");
        System.out.print("Resultado: "+sol.getSolucion().toString()+" - "+"Prediccion: "+prediccion.getResultado() + " -> Confianza= " + "[ " + prediccion.getConfianza() + " ]");
        System.out.println(" *********");
     */
        Evaluator.getEvaluationReport().addDataToSeries("Aciertos", pre);
        Evaluator.getEvaluationReport().addDataToSeries("Confianza", prediccion.getConfianza());
	}
	
	// Este método ejecuta una consulta pedida por el usuario mediante la interfaz
	public ArrayList<Prediccion> querysCBR (ArrayList<String> equipos, int temporada, int jornada, double[] listaPesos,
			String[][][] clasificacionesPrimera, String[][][] clasificacionesSegunda) {
		try{
			//Configuración
			configure();
			
			//Preciclo
			preCycle();
	
			//Crear un objeto que almacena la consulta
			CBRQuery query = new CBRQuery();	
			// Si nos piden la jornada 1, entonces los equipos están con todos sus valores a 0
			Integer[] clasifLocal = {0,0,0,0,0,0,0};
			Integer[] clasifVisitante = {0,0,0,0,0,0,0};			
			// Iteramos los partidos pedidos
			Iterator<String> iterador = equipos.iterator();
			String[] tokens = null;
			String[] tokensLocal = null;
			String[] tokensVisitante = null;
			// Buscamos para cada par de equipos del arrayList su clasificacion
			while (iterador.hasNext()) {
				tokens = iterador.next().split(",");
				tokensLocal = null;
				// Buscamos la clasifiacion de cada uno
				for (String i: clasificacionesPrimera[temporada-2000][jornada]) {					
					if ((i != null)	&& i.startsWith(tokens[0])) {
						tokensLocal = i.split((","));
					}
					if ((i != null)	&& i.startsWith(tokens[1])) {
						tokensVisitante = i.split((","));
					}
					if (tokensLocal != null && tokensVisitante != null) break;
				}			
				// Rellenamos la clasificacion
				for (int i = 0;i<tokensLocal.length-2;i++) {
					clasifLocal[i] = Integer.valueOf(tokensLocal[i+1]);
					clasifVisitante[i] = Integer.valueOf(tokensVisitante[i+1]);
				}
				// Rellenamos la query con los valores apropiados
				query.setDescription(new DescripcionQuinielas(Integer.valueOf(temporada),tokens[0],clasifLocal,tokens[1],clasifVisitante));
				//Ejecutar el ciclo
				cycle(query);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return listaPredicciones;
	}	
}
