package Cbr;

import viajes.TravelRecommender;
import jcolibri.casebase.*;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;
import GAPDataBase.ConfigurableHSQLDBserver;

public class Recomendador implements StandardCBRApplication {
	
	Connector connector;
	CBRCaseBase caseBase;
	
	@Override
	public void configure() throws ExecutionException {
		try {
		// Crear el conector con la base de casos
		connector = new DataBaseConnector();
		// Inicializar el conector con su archivo xml de configuración
		connector.initFromXMLfile(jcolibri.util.FileIO.findFile("proyecto3/Cbr/databaseconfig.xml"));
		// La organizacion en memoria sera lineal
		caseBase = new LinealCaseBase();	
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	@Override
	public void cycle(CBRQuery arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postCycle() throws ExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args){
		//Lanzar el SGBD
		ConfigurableHSQLDBserver.initInMemory("GAP", false);
		ConfigurableHSQLDBserver.loadSQLFile("proyecto3/GAPDataBase/dump-v1.sql");
		
		Recomendador rec = new Recomendador();
		
		try {
			rec.configure();
		} catch (ExecutionException e) {
			org.apache.commons.logging.LogFactory.getLog(TravelRecommender.class).error(e);
		}
	}
			
}
