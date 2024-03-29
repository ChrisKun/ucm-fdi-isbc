package src.quinielas.cbr;

import java.util.Vector;



import src.quinielas.Principal;
import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.evaluators.*;

/**
 * 
 * Clase que contiene los tres m�todos de validaci�n cruzada: Hold Out, Leave One Out y Same SPlit
 *
 */

public class ValidacionCruzada {
	
	// M�tod que realiza la media de los aciertos y a�ada la infomaci�n para mostrarla
	public void MediaAciertos() 
	{
	    Vector<Double> vectorAciertos = Evaluator.getEvaluationReport().getSeries("Aciertos");
	    
	    double media = 0.0;
	    for (Double acierto: vectorAciertos)
	    		media += acierto;
	    media = media / (double)Evaluator.getEvaluationReport().getNumberOfCycles();
	    Evaluator.getEvaluationReport().putOtherData("Media aciertos", Double.toString(media));
	}
	
	//TODO
	/**
	 * 
	 * @param listaPesos
	 * @param numeroCasos
	 * @param numeroVueltas
	 * @param media - True = media ponderada / False = media normal
	 */
    public void HoldOutEvaluation(double[] listaPesos, int numeroCasos, int numeroVueltas, boolean media)
    {
	    //SwingProgressBar shows the progress
	    jcolibri.util.ProgressController.clear();
	    jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), HoldOutEvaluator.class);
	       
	    HoldOutEvaluator eval = new HoldOutEvaluator();
	    eval.init(new Quinielas(true, listaPesos, media));
	    // Configurar % de casos que cogemos y n� de vueltas
	    eval.HoldOut(numeroCasos,numeroVueltas);
	    
	    this.MediaAciertos();
	    
	    System.out.println(Evaluator.getEvaluationReport());
	    jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - HoldOutEvaluation", false);
    }
    
    //TODO
    /**
     * 
     * @param listaPesos
     * @param media - True = media ponderada / False = media normal
     */
    public void LeaveOneOutEvaluation(double[] listaPesos, boolean media)
    {
		//SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), LeaveOneOutEvaluator.class);
		
		LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
		eval.init(new Quinielas(true, listaPesos, media));
		eval.LeaveOneOut();		

	    this.MediaAciertos();
		
		System.out.println(Evaluator.getEvaluationReport());
		jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - LeaveOneOutEvaluation", false);
    }
    
    //TODO
    /**
     * 
     * @param listaPesos
     * @param numeroCasos
     * @param media - True = media ponderada / False = media normal
     */
    public void SameSplitEvaluation(double[] listaPesos, int numeroCasos, boolean media)
    {
		//SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), SameSplitEvaluator.class);
		            
		SameSplitEvaluator eval = new SameSplitEvaluator();
		eval.init(new Quinielas(true, listaPesos, media));
		// Configurar el % de testeo y el nombre del fichero de salida
		eval.generateSplit(numeroCasos, "split1.txt");
		eval.HoldOutfromFile("split1.txt");
			    
	    this.MediaAciertos();
		
		System.out.println(Evaluator.getEvaluationReport());
		jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - SameSplitEvaluation", false);
    }
    
    //TODO
    /**
     * 
     * @param listaPesos
     * @param numeroConjuntos
     * @param numeroVueltas
     * @param media - True = media ponderada / False = media normal
     */
    public void NFoldEvaluation(double[] listaPesos, int numeroConjuntos, int numeroVueltas, boolean media)
    {
		//SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), NFoldEvaluator.class);
		            
		NFoldEvaluator eval = new NFoldEvaluator();
		eval.init(new Quinielas(true, listaPesos, media));
		// Configurar % de conjuntos que cogemos y n� de vueltas
		eval.NFoldEvaluation(numeroConjuntos, numeroVueltas);
			    
	    this.MediaAciertos();
		
		System.out.println(Evaluator.getEvaluationReport());
		jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - NFoldEvaluation", false);
    }
    
    public static void main (String[] args) {
    	ValidacionCruzada v = new ValidacionCruzada();
    	Principal p = new Principal();
    	// Poner el valor que queramos para hacer media ponderada o normal (true = ponderada)
    	boolean media = false;
    	//v.HoldOutEvaluation(p.getListaPesos(),15,1, media);
    	//v.LeaveOneOutEvaluation(p.getListaPesos(), media);
    	//v.SameSplitEvaluation(p.getListaPesos(),10, media);
    	v.NFoldEvaluation(p.getListaPesos(),10,1, media);
    }

}
