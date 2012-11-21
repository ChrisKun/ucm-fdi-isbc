package proyecto2.quinielas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JOptionPane;

import proyecto2.quinielas.representacion.DescripcionQuinielas;
import proyecto2.quinielas.representacion.SolucionQuinielas.UnoXDos;
import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.classification.ClassificationSolution;
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.selection.SelectCases;

public class Quinielas implements StandardCBRApplication {

	Connector connector;
	CBRCaseBase caseBase;
	
	@Override
	public void configure() throws ExecutionException {
		try{
		//Crear el conector con la base de casos
		connector = new ConnectorQuinielas();
		//La organización en memoria será lineal
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
		
		// Ejecutamos la recuperación del vecino más próximo
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
		
		// Seleccionamos los k mejores casos
		eval = SelectCases.selectTopKRR(eval, 5);
		
		Collection<CBRCase> casos = new ArrayList<CBRCase>();
		System.out.println("Casos Recuperados:");
		// 0 = X, 1 = 1, 2 = 2
		double [] resQuini = new double[3];
		for (RetrievalResult nse: eval){
			String sol = nse.get_case().getSolution().toString();			
			if (sol.contains("UNO")) {
				resQuini[1] = resQuini[1]+nse.getEval();
			} else if (sol.contains("DOS")) {
				resQuini[2] = resQuini[2]+nse.getEval();
			} else
				resQuini[0] = resQuini[0]+nse.getEval();
			System.out.println(nse);
			casos.add(nse.get_case());
		}
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.caseBase.close();
	}
	
	public static void main(String[] args) {
		Quinielas q = new Quinielas();
		try {
			//Configuración
			q.configure();
			// Preciclo
			q.preCycle();
			//Crear un objeto que almacena la consulta
			CBRQuery query = new CBRQuery();
			// Rellenamos el HashMap con la información de los equipos
			HashMap<String, int[]> hash = rellenaHash(query);
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
		}	
	}

	private static HashMap<String, int[]> rellenaHash (CBRQuery query){		
		try {
			// Rellenamos la información de los equipos de la jornada pedida
			BufferedReader br = new BufferedReader(new FileReader("jornada_actual.txt"));			
			String line = null;
			HashMap<String, int[]> hash = new HashMap<String, int[]>();
			while((line=br.readLine())!=null){					
				String[] tokens = line.split(",");
				int[] valores=new int[8];
				int i=0;
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
	
	private static void rellenaQuery(CBRQuery query, HashMap<String, int[]> hash, String local, String visitante) {
		int [] clasifLocal = hash.get(local);
		int [] clasifVisitante = hash.get(visitante);
		int temporada = 2012;
		DescripcionQuinielas queryDesc = new DescripcionQuinielas (temporada,local,clasifLocal,visitante,clasifVisitante);		
		query.setDescription(queryDesc);		
	}
}
