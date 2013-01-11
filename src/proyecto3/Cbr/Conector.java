package Cbr;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import GAPDataBase.*;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseBaseFilter;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.InitializingException;

public class Conector implements Connector {

	@Override
	public void close() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void deleteCases(Collection<CBRCase> arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void initFromXMLfile(URL arg0) throws InitializingException {
		// TODO Auto-generated method stub		
	}

	@Override
	public Collection<CBRCase> retrieveAllCases() {		
		ArrayList<CBRCase> casos = new ArrayList<CBRCase>();
		ArrayList<Product> productos = GAPLoader.extractProducts();
		for (Product p: productos) {
			CBRCase caso = new CBRCase();
			caso.setDescription((CaseComponent) p);
			casos.add(caso);
		}
		return casos;
	}

	@Override
	public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeCases(Collection<CBRCase> arg0) {
		// TODO Auto-generated method stub
		
	}
}
