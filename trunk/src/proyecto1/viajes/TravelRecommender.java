package proyecto1.viajes;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod;
import jcolibri.method.gui.formFilling.ObtainQueryWithFormMethod;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.selection.SelectCases;


public class TravelRecommender implements StandardCBRApplication{

	/** Connector object */
	Connector _connector;
	/** CaseBase object */
	CBRCaseBase _caseBase;
	
	public void configure() throws ExecutionException {
		try{
			//Crear el conector con la base de casos
			_connector = new DataBaseConnector();
			//Inicializar el conector con su archivo xml de configuración
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile(
					"proyecto1/viajes/databaseconfig.xml"));
			//La organización en memoria será lineal
			_caseBase = new LinealCaseBase();
		} catch (Exception e){
			throw new ExecutionException(e);
		}		
	}

	public CBRCaseBase preCycle() throws ExecutionException {
		// Cargar los casos desde el conector a la base de casos
		_caseBase.init(_connector);
		// Imprimir los casos leidos
		// Puedes comentar las siguientes líneas una vez que funciones
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		/*for(CBRCase c: cases)
			System.out.println(c);*/
		return _caseBase;
	}

	public void cycle(CBRQuery query) throws ExecutionException {
		// Para configurar el KNN se utiliza un objeto NNConfig
		NNConfig simConfig = new NNConfig();
		// Fijamos la función de similitud global
		simConfig.setDescriptionSimFunction(new Average());
		
		// Fijamos las funciones de similitud locales
		simConfig.addMapping(
				new Attribute("HolidayType", TravelDescription.class),
				new Equal()
				);
		simConfig.addMapping(
				new Attribute("NumberOfPersons", TravelDescription.class),
				new Interval(12)
				);
		simConfig.addMapping(
				new Attribute("Region", TravelDescription.class),
				new Equal()
				);
		simConfig.addMapping(
				new Attribute("Transportation", TravelDescription.class),
				new Equal()
				);
		simConfig.addMapping(
				new Attribute("Duration", TravelDescription.class),
				new Interval(21)
				);
		simConfig.addMapping(
				new Attribute("Season", TravelDescription.class),
				new Equal()
				);
		simConfig.addMapping(
				new Attribute("Accommodation", TravelDescription.class),
				new Equal()
				);
		
		// Es posible modificar el peso de cada atributo en la media ponderada.
		// Por defecto el peso es 1
		// simConfig.setWeight(new Attribute("Season", TravelDescription.class, 0.5);
		
		// Ejecutamos la recuperación del vecino más próximo
		Collection<RetrievalResult> eval =
				NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
		
		// Seleccionamos los k mejores casos
		eval = SelectCases.selectTopKRR(eval, 5);
		
		// Imprimimos el resultado del k-NN y obtenemos la lista de casos recuperados:
		Collection<CBRCase> casos = new ArrayList<CBRCase>();
		System.out.println("Casos Recuperados:");
		for (RetrievalResult nse: eval){
			System.out.println(nse);
			casos.add(nse.get_case());
		}
		
		// Aqui se incluiría el código para adaptar la solución
		
		// Solamente mostramos el resultado
		DisplayCasesTableMethod.displayCasesInTableBasic(casos);
	}

	public void postCycle() throws ExecutionException {
		this._caseBase.close();
		
	}

	public static void main(String[] args){
		
		//Lanzar el SGBD
		jcolibri.test.database.HSQLDBserver.init();
		
		//Crear el objeto que implementa la aplicación CBR
		TravelRecommender trApp = new TravelRecommender();
		try{
			//Configuración
			trApp.configure();
			
			//Preciclo
			trApp.preCycle();
	
			//Crear un objeto que almacena la consulta
			CBRQuery query = new CBRQuery();
			query.setDescription(new TravelDescription());
			
			//Mientras que el usuario quiera (se muestra ventana de continuar)
			do{
				//Obtener los valores de la consulta
				ObtainQueryWithFormMethod.obtainQueryWithoutInitialValues(query, null, null);
				//Ejecutar el ciclo
				trApp.cycle(query);
			}while(JOptionPane.showConfirmDialog(null, "Continuar?")==JOptionPane.OK_OPTION);
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(TravelRecommender.class).error(e);
		}
		//Apagar el SGBD
		jcolibri.test.database.HSQLDBserver.shutDown();
	}
}
