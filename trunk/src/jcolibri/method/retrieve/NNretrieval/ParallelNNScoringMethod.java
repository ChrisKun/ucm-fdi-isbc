/**
* ParallelNNScoringMethod
* jCOLIBRI2 framework (unofficial class file).
* @author Juan A. Recio-García.
* GAIA - Group for Artificial Intelligence Applications
* http://gaia.fdi.ucm.es
* NOTE: This is an experimental feature. Use at your own risk.
*/

package jcolibri.method.retrieve.NNretrieval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;

/**
* This class implements a parallel version of the NN scoring method. 
* Ideally, it splits the case base into several sets according to the number of cores of the processor.
* Then a new thread is created for every core that computes the similarity of each set of cases in a parallel way.
* Finally, a merge function joins the results. 
* The behaviour of this method is not guarantee and it may depend on the concrete hardware platform, OS and JVM. 
*/
public class ParallelNNScoringMethod {

	public static final int MIN_CASES_TO_PARALLELIZE = 80;
	
	public static Collection<RetrievalResult> evaluateSimilarityParallel(Collection<CBRCase> cases, CBRQuery query, NNConfig nnConfig)
	{
		int numCores = Runtime.getRuntime().availableProcessors();
		//int numCores = 4;
		if(numCores == 1)
		{
			org.apache.commons.logging.LogFactory.getLog(ParallelNNScoringMethod.class).info("Only 1 core detected. Using normal NNScoringMethod");
			return NNScoringMethod.evaluateSimilarity(cases, query, nnConfig);
		}
		
		if(cases.size()<MIN_CASES_TO_PARALLELIZE)
		{
			org.apache.commons.logging.LogFactory.getLog(ParallelNNScoringMethod.class).info("Number of cases too low. Using normal NNScoringMethod");
			return NNScoringMethod.evaluateSimilarity(cases, query, nnConfig);
		}
		
		
		Collection<Collection<CBRCase>> setOfCases = new ArrayList<Collection<CBRCase>>();
		
		split(cases, setOfCases, numCores);
		
		return evaluateSimilarity(setOfCases, query, nnConfig);
	}
	
	
	private static void split(Collection<CBRCase> cases, Collection<Collection<CBRCase>> setOfCases, int numCores) {
		
		int totalCases = cases.size();
		int casesPerSet = totalCases/numCores;
		casesPerSet++;
		
		Iterator<CBRCase> cIter = cases.iterator();
		for(int i=0; i<numCores; i++)
		{
			ArrayList<CBRCase> set = new ArrayList<CBRCase>(casesPerSet);
			for(int c=0; (c<casesPerSet) && cIter.hasNext(); c++)
				set.add(cIter.next());
			setOfCases.add(set);
		}
		
	}


	@SuppressWarnings("unchecked")
	public static Collection<RetrievalResult> evaluateSimilarity(Collection<Collection<CBRCase>> setOfCases, CBRQuery query, NNConfig nnConfig)
	{
		ArrayList<NNThread> threads = new ArrayList<NNThread>();
		
		int numCores = Runtime.getRuntime().availableProcessors();
		//org.apache.commons.logging.LogFactory.getLog(ParallelNNScoringMethod.class).info("Using "+numCores+" threads/processors");
		
        ExecutorService execSvc = Executors.newFixedThreadPool( numCores );
		
		for(Collection<CBRCase> caseSet: setOfCases)
		{
			NNThread thread = new NNThread(caseSet,query,nnConfig);
			threads.add(thread);
			execSvc.execute(thread);
		}
		
        execSvc.shutdown();

        try {
			execSvc.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		
		ArrayList<ArrayList<RetrievalResult>> result = new ArrayList<ArrayList<RetrievalResult>>();
		for(NNThread t: threads)
		{
			if(!t.getResult().isEmpty())
				result.add((ArrayList<RetrievalResult>)t.getResult());
		}
		
		
		return merge(result);
		
	}

	public static Collection<RetrievalResult> merge(ArrayList<ArrayList<RetrievalResult>> results)
	{
		ArrayList<RetrievalResult> merged = new ArrayList<RetrievalResult>();
		
		
		int remaining = results.size();
		boolean finish = false;
		
		while(!finish)
		{
			RetrievalResult max = null;
			ArrayList<RetrievalResult> maxList = null;
			
			if(remaining==1)
			{
				for(ArrayList<RetrievalResult> list : results)
					if(!list.isEmpty())
						merged.addAll(list);
				finish = true;
				continue;
			}
			
			
			for(ArrayList<RetrievalResult> list : results)
			{
				if(list.isEmpty())
					continue;
				
				if(max == null)
				{
					maxList = list;
					max = list.get(0);
				}
				else if(max.getEval()<list.get(0).getEval())
				{
					maxList = list;
					max = list.get(0);
				}
			}
			

			merged.add(maxList.remove(0));
			if(maxList.isEmpty())
				remaining--;

		}
		
		return merged;
		
	}	

}
