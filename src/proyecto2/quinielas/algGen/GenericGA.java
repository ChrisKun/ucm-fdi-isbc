package quinielas.algGen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Generic Genetic Algorithm
 * @author Juan A. Recio
 *
 */
public class GenericGA {

	int maxrepsnoimprovement; 
	int maxreps;
	int populationSize;
	int percentageToKill;
	int percentageToMute;
	EvaluationFunction evalFunction;
	
	ArrayList<IndividualInfo> individuals;
	
	boolean verbose;

	/**
	 * Constructor
	 * @param maxrepsnoimprovement Max number of repetitions without improvement to be waited before stoping the algorithm
	 * @param maxreps Max absolute number of repetitions
	 * @param percentageToKill Percentage of the population to kill every cycle
	 * @param percentageToMute Percentage of the population to mute every cycle
	 * @param evalFunction Evaluation function. Returns the goodness of every individual
	 */
	public GenericGA(int maxrepsnoimprovement, int maxreps, int percentageToKill, int percentageToMute, EvaluationFunction evalFunction) {
		super();
		this.maxrepsnoimprovement = maxrepsnoimprovement;
		this.maxreps = maxreps;
		this.percentageToMute = percentageToMute;
		this.evalFunction = evalFunction;
		this.percentageToKill = percentageToKill;
	}

	/**
	 * Runs the genetic algorimth
	 * @param initialPopulation is the initial population
	 * @return Returns an ordered list of IndividualInfo objects (individual+evaluation).
	 */
	public Collection<IndividualInfo> run(Collection<Individual> initialPopulation)
	{
		//initial population
		individuals = new ArrayList<IndividualInfo>();
		for(Individual ind: initialPopulation)
				individuals.add(new IndividualInfo(ind, 0f));
		this.populationSize = individuals.size();
		
		if(verbose)	System.out.println("Population size: "+populationSize);
		
		int rep=0;
		int lastrepimpro=-1;
		float bestEvaluation = 0f;
		
		while((rep<maxreps) && ((rep-lastrepimpro)<maxrepsnoimprovement))
		{
			if(verbose)	System.out.println("Repetition: "+rep+"  Best: "+individuals.get(0));
			
			kill();

			combine();

			mute();

			evaluate();
			
			IndividualInfo best = this.individuals.get(0);
			
			if(bestEvaluation < best.getEvaluation())
			{
				bestEvaluation = best.getEvaluation();
				lastrepimpro = rep;
			}
			
			rep++;
		}
		
		return individuals;
		
	}
	

	
	private void evaluate() {
		
		for(IndividualInfo ii: individuals)
		{
			float eval = this.evalFunction.evaluateIndividual(ii.getIndividual());
			ii.setEvaluation(eval);
		}
		
		Collections.sort(individuals);
	}

	private void mute() {
		int numtomute = this.percentageToMute * this.populationSize / 100;
		for(int i=0; i<numtomute; i++)
			individuals.get((int)(Math.random()*populationSize)).getIndividual().mutation();
	}

	private void combine() {

		int indToCreate = percentageToKill * populationSize / 100;

		ArrayList<IndividualInfo> newInds = new ArrayList<IndividualInfo>();
		
		for(int i=0; i<indToCreate; i++)
		{
			int posa = (int)Math.random()*individuals.size();
			int posb = (int)Math.random()*individuals.size();
			
			Individual a = individuals.get(posa).getIndividual();
			Individual b = individuals.get(posb).getIndividual();
			
			Individual newind = a.combination(b);
			
			newInds.add(new IndividualInfo(newind, 0f));
		}
		
		individuals.addAll(newInds);
	}

	private void kill() {
		
		int indToKill = percentageToKill * populationSize / 100;

		for(int i=0; i<indToKill; i++)
			individuals.remove(individuals.size()-1);
	}

	




	/**
	 * This class is a comparable pair of <individual, evaluation>
	 * @author Juan A. Recio
	 *
	 */
	public class IndividualInfo implements Comparable<IndividualInfo>{
		Individual individual;
		float evaluation;
		
		/**
		 * @param individual
		 * @param evaluation
		 */
		public IndividualInfo(Individual individual, float evaluation) {
			super();
			this.individual = individual;
			this.evaluation = evaluation;
		}
		public Individual getIndividual() {
			return individual;
		}
		public void setIndividual(Individual individual) {
			this.individual = individual;
		}
		public float getEvaluation() {
			return evaluation;
		}
		public void setEvaluation(float evaluation) {
			this.evaluation = evaluation;
		}

		@Override
		public int compareTo(IndividualInfo other) {
			if (evaluation<other.getEvaluation())
				return 1;
			else if (evaluation>other.getEvaluation())
				return -1;
			else
				return 0;
		}
		
		public String toString()
		{
			return evaluation +" -> "+ individual;
		}
		
	}


	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	

	
}
