package proyecto2.quinielas;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;

public class Quinielas implements StandardCBRApplication {

	Connector connector;
	CBRCaseBase caseBase;
	
	@Override
	public void configure() throws ExecutionException {
		connector = new ConnectorQuinielas();
		caseBase = new LinealCaseBase();

	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		
		// Aquí se calcularía datos sobre los casos como los puntos acumulados, etc
		// caseBase.getCases();
		
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postCycle() throws ExecutionException {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Quinielas q = new Quinielas();
		CBRCaseBase casebase;
		try {
			q.configure();
			casebase = q.preCycle();
			for(CBRCase c : casebase.getCases())
				System.out.println(c);
		
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
