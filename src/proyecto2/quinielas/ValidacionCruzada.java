package proyecto2.quinielas;

import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.evaluators.*;

public class ValidacionCruzada {
	
    public void HoldOutEvaluation()
    {
	    //SwingProgressBar shows the progress
	    jcolibri.util.ProgressController.clear();
	    jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), HoldOutEvaluator.class);
	       
	    HoldOutEvaluator eval = new HoldOutEvaluator();
	    eval.init(new Quinielas(true));
	    // Configurar % de casos que cogemos y nº de vueltas
	    eval.HoldOut(5, 1);
	    
	    
	    System.out.println(Evaluator.getEvaluationReport());
	    jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - Evaluation", false);
    }
    
    public void LeaveOneOutEvaluation()
    {
		//SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), LeaveOneOutEvaluator.class);
		
		LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
		eval.init(new Quinielas(true));
		eval.LeaveOneOut();
		    
		System.out.println(Evaluator.getEvaluationReport());
		jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - Evaluation", false);
    }
    
    public void SameSplitEvaluation()
    {
		//SwingProgressBar shows the progress
		jcolibri.util.ProgressController.clear();
		jcolibri.util.ProgressController.register(new jcolibri.test.main.SwingProgressBar(), SameSplitEvaluator.class);
		            
		SameSplitEvaluator eval = new SameSplitEvaluator();
		eval.init(new Quinielas(true));
		// Configurar el % de testeo y el nombre del fichero de salida
		eval.generateSplit(10, "split1.txt");
		eval.HoldOutfromFile("split1.txt");
		            
		System.out.println(Evaluator.getEvaluationReport());
		jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Quinielas - Evaluation", false);
    }

}
