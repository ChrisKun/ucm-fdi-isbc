package proyecto2.quinielas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JOptionPane;

import proyecto2.quinielas.representacion.DescripcionQuinielas;
import proyecto2.quinielas.representacion.SolucionQuinielas;
import jcolibri.casebase.LinealCaseBase;
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


public class Quinielas implements StandardCBRApplication {

	// Numero de casos que recuperamos en el algoritmo KNN
	final static int K = 5;		
	
	Connector connector;
	CBRCaseBase caseBase;
	
	// ArrayList que contiene los resultados
	ArrayList<Prediccion> listaPredicciones;
	// Booleano que indica si estamos ejecutando una evaluación o no
	boolean esValidacion;
	
	/* ** CONSTRUCTORAS ** */
	public Quinielas () {
		super();
		listaPredicciones = new ArrayList<Prediccion>();
		esValidacion = false;
	}
	
	public Quinielas (boolean esEvaluacion) {
		super();
		// listaPredicciones = new ArrayList<Prediccion>();
		esValidacion = true;
	}
	
	/* ** GETTERS/SETTERS ** */
	public ArrayList<Prediccion> getListaPredicciones (){
		return this.listaPredicciones;
	}
	
	/* ** METODOS ** */	
	@Override
	public void configure() throws ExecutionException {
		try{
		// Crear el conector con la base de casos
		connector = new ConnectorQuinielas();
		// La organización en memoria será lineal
		caseBase = new LinealCaseBase();
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
		simConfig.setWeight(new Attribute("temporada", DescripcionQuinielas.class), 0.3);
		simConfig.setWeight(new Attribute("local", DescripcionQuinielas.class), 0.02);
		simConfig.setWeight(new Attribute("visitante", DescripcionQuinielas.class), 0.02);
		simConfig.setWeight(new Attribute("puntosLocal", DescripcionQuinielas.class), 0.2);
		simConfig.setWeight(new Attribute("pgLocal", DescripcionQuinielas.class), 0.1);
		simConfig.setWeight(new Attribute("peLocal", DescripcionQuinielas.class), 0.025);
		simConfig.setWeight(new Attribute("ppLocal", DescripcionQuinielas.class), 0.05);
		simConfig.setWeight(new Attribute("puntosVisitante", DescripcionQuinielas.class), 0.25);
		simConfig.setWeight(new Attribute("pgVisitante", DescripcionQuinielas.class), 0.15);
		simConfig.setWeight(new Attribute("peVisitante", DescripcionQuinielas.class), 0.025);
		simConfig.setWeight(new Attribute("ppVisitante", DescripcionQuinielas.class), 0.01);
		simConfig.setWeight(new Attribute("posLocal", DescripcionQuinielas.class), 0.3);
		simConfig.setWeight(new Attribute("posVisitante", DescripcionQuinielas.class), 0.3);
		simConfig.setWeight(new Attribute("gfavorLocal", DescripcionQuinielas.class), 0.2);
		simConfig.setWeight(new Attribute("gcontraLocal", DescripcionQuinielas.class), 0.4);
		simConfig.setWeight(new Attribute("gfavorVisitante", DescripcionQuinielas.class), 0.4);
		simConfig.setWeight(new Attribute("gcontraVisitante", DescripcionQuinielas.class), 0.2);
		
		// Ejecutamos la recuperación del vecino más próximo (usando el método en paralelo)
		// Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
		Collection<RetrievalResult> eval = ParallelNNScoringMethod.evaluateSimilarityParallel(caseBase.getCases(), query, simConfig);
		// Seleccionamos los K mejores casos
		eval = SelectCases.selectTopKRR(eval, K);
		
		for (RetrievalResult nse : eval) System.out.println(nse.toString());
		
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
        System.out.println("Resultado: "+sol.getSolucion().toString()+" - "+"Prediccion: "+prediccion.getResultado());
        Evaluator.getEvaluationReport().addDataToSeries("Aciertos", pre);
        Evaluator.getEvaluationReport().addDataToSeries("Confianza", prediccion.getConfianza());
			
		System.out.println(prediccion.toString());
	}
	
	// PODEMOS CAMBIAR ESTE MAIN Y PONER UNA CLASE QUE EJECUTE EL MAIN Y LANCE LA INTERFAZ,PARSEADOR... Y PERMITA EJECUTAR VALIDACION O NO
	public static void main(String[] args) {
		ValidacionCruzada validador = new ValidacionCruzada(); 
		if (JOptionPane.showConfirmDialog(null, "Hacer HoldOutEval?")==JOptionPane.OK_OPTION)
			validador.HoldOutEvaluation();
		if (JOptionPane.showConfirmDialog(null, "Hacer LeaveOneOutEval?")==JOptionPane.OK_OPTION)
				validador.LeaveOneOutEvaluation();
		if (JOptionPane.showConfirmDialog(null, "Hacer SameSplitEval?")==JOptionPane.OK_OPTION)
			validador.SameSplitEvaluation();
		/*
		Quinielas q = new Quinielas();
		try {
			//Configuración
			q.configure();
			// Preciclo
			q.preCycle();
			//Crear un objeto que almacena la consulta
			CBRQuery query = new CBRQuery();
			query.setDescription(new DescripcionQuinielas());
			// Rellenamos el HashMap con la información de los equipos
			HashMap<String, Integer[]> hash = rellenaHash(query);
			do {
				// Ejemplo de equipos
				String equipoLocal = "Osasuna";
				String equipoVisitante = "Getafe";
				rellenaQuery(query,hash,equipoLocal,equipoVisitante);		
				//Ejecutar el ciclo
				q.cycle(query);
			} while (JOptionPane.showConfirmDialog(null, "Continuar?")==JOptionPane.OK_OPTION);
		} catch (ExecutionException e) {			
			e.printStackTrace();
		}	*/
	}

	/*
	private static HashMap<String, Integer[]> rellenaHash (CBRQuery query){		
		try {
			// Rellenamos la información de los equipos de la jornada pedida
			BufferedReader br = new BufferedReader(new FileReader("jornada_actual.txt"));			
			String line = null;
			HashMap<String, Integer[]> hash = new HashMap<String, Integer[]>();
			while((line=br.readLine())!=null){					
				String[] tokens = line.split(",");
				Integer[] valores=new Integer[8];
				Integer i=0;
				while (i < 7) {
					valores[i] = Integer.valueOf(tokens[i+1]); 					
					i++;
				}
				hash.put(tokens[0],valores);				
			}
			br.close();
			return hash;
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return null;		
	}
	
	private static void rellenaQuery(CBRQuery query, HashMap<String, Integer[]> hash, String local, String visitante) {
		Integer[] clasifLocal = hash.get(local);
		Integer[] clasifVisitante = hash.get(visitante);
		Integer temporada = 2012;
		DescripcionQuinielas queryDesc = new DescripcionQuinielas (temporada,local,clasifLocal,visitante,clasifVisitante);		
		query.setDescription(queryDesc);		
	}
	*/
}
