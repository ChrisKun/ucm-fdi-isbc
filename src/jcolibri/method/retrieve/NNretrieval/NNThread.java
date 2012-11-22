/**
* NNThread
* jCOLIBRI2 framework (unofficial class file). 
* @author Juan A. Recio-García.
* GAIA - Group for Artificial Intelligence Applications
* http://gaia.fdi.ucm.es
* NOTE: This is an experimental feature. Use at your own risk.
*/

package jcolibri.method.retrieve.NNretrieval;

import java.util.Collection;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;

/**
* Thread that computes NN scoring for a given set of cases.
*/
public class NNThread implements Runnable {

	Collection<CBRCase> cases;
	CBRQuery query;
	NNConfig nnConfig;
	Collection<RetrievalResult> result;

	/**
	 * @param cases
	 * @param query
	 * @param nnConfig
	 */
	public NNThread(Collection<CBRCase> cases, CBRQuery query, NNConfig nnConfig) {
		super();
		this.cases = cases;
		this.query = query;
		this.nnConfig = nnConfig;
	}



	public void run() {
		result = NNScoringMethod.evaluateSimilarity(cases, query, nnConfig);
	}
	
	public Collection<RetrievalResult> getResult()
	{
		return result;
	}

}
