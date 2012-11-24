package proyecto2.quinielas.cbr;

import java.util.Vector;

import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.evaluators.*;

/**
 * 
 * Clase que contiene los tres métodos de validación cruzada: Hold Out, Leave One Out y Same SPlit
 *
 */

public class ValidacionCruzada {
	
	// Métod que realiza la media de los aciertos y añada la infomación para mostrarla
	public void MediaAciertos() 
	{
	    Vector<Double> vectorAciertos = Evaluator.getEvaluationReport().getSeries("Aciertos");
	    
	    double media = 0.0;
	    for (Double acierto: vectorAciertos)
	    		media += acierto;
	    media = media / (double)Evaluator.getEvaluationReport().getNumberOfCycles();
	    Evaluator.getEvaluationReport().putOtherData("Media aciertos", Double.toString(media));
	}
	
    public void HoldOutEvaluation(double[] listaPesos)
    {
	    //SwingProgressBar shows the progress
	    jcolibri.util.ProgressController.clear();
	    jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), HoldOutEvaluator.class);
	       
	    HoldOutEvaluator eval = new HoldOutEvaluator();
	    eval.init(new Quinielas(true, listaPesos));
	    // Configurar % de casos que cogemos y nº de vueltas
	    eval.HoldOut(25, 1);
	    
	    this.MediaAciertos();
	    
	    System.out.println(Evaluator.getEvaluationReport());
	    jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - HoldOutEvaluation", false);
    }
    
    public void LeaveOneOutEvaluation(double[] listaPesos)
    {
		//SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), LeaveOneOutEvaluator.class);
		
		LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
		eval.init(new Quinielas(true, listaPesos));
		eval.LeaveOneOut();		

	    this.MediaAciertos();
		
		System.out.println(Evaluator.getEvaluationReport());
		jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - LeaveOneOutEvaluation", false);
    }
    
    public void SameSplitEvaluation(double[] listaPesos)
    {
		//SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), SameSplitEvaluator.class);
		            
		SameSplitEvaluator eval = new SameSplitEvaluator();
		eval.init(new Quinielas(true, listaPesos));
		// Configurar el % de testeo y el nombre del fichero de salida
		eval.generateSplit(10, "split1.txt");
		eval.HoldOutfromFile("split1.txt");
			    
	    this.MediaAciertos();
		
		System.out.println(Evaluator.getEvaluationReport());
		jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - SameSplitEvaluation", false);
    }

}
