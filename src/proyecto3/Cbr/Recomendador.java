package Cbr;

import jcolibri.casebase.*;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;

public class Recomendador implements StandardCBRApplication {
	
    /** Connector object */
	Connector connector;
    /** CaseBase object */
	CBRCaseBase caseBase;
	
    /** KNN config */
    NNConfig simConfig;
    
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
		simConfig.addMapping(new Attribute("categoria", Prenda.class), new Interval(13));
		simConfig.addMapping(new Attribute("division", Prenda.class), new Equal());
		simConfig.addMapping(new Attribute("precio", Prenda.class), new Interval(2000));
		simConfig.addMapping(new Attribute("lavado", Prenda.class), new Equal());
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
	public void cycle(CBRQuery arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.caseBase.close();
		this.connector.close();
	}


	public static void main(String[] args){
		Recomendador rec = new Recomendador();
		
		try {
			rec.configure();
			rec.preCycle();
		} catch (ExecutionException e) {
			org.apache.commons.logging.LogFactory.getLog(Recomendador.class).error(e);
		}
	}
			
}
