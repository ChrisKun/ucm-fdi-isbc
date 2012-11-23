package proyecto2.quinielas;

import javax.swing.JOptionPane;

import proyecto2.quinielas.cbr.ValidacionCruzada;
import proyecto2.quinielas.representacion.DescripcionQuinielas;

public class Principal {
	
	public final static int TEMPORADA = 0;
	/* ATRIBUTOS */
	double[] listaPesos;
	
	/* CONSTRUCTORAS */ 
	public Principal () {		
		this.iniciaPesos();
	}
	/* GETTERS/SETTERS */
	public double[] getListaPesos() {
		return listaPesos;
	}
	public void setListaPesos(double[] listaPesos) {
		this.listaPesos = listaPesos;
	}
	/* AUXILIARES */
	private void iniciaPesos () {
		listaPesos = new double[DescripcionQuinielas.NUMCAMPOS];	
		listaPesos[TEMPORADA] = 0.3;
		listaPesos[1] = 0.02;
		listaPesos[2] = 0.02;
		listaPesos[3] = 0.2;
		listaPesos[4] = 0.1;
		listaPesos[5] = 0.025;
		listaPesos[6] = 0.05;
		listaPesos[7] = 0.25;
		listaPesos[8] = 0.15;
		listaPesos[9] = 0.025;
		listaPesos[10] = 0.01;
		listaPesos[11] =  0.3;
		listaPesos[12] = 0.3;
		listaPesos[13] = 0.2;
		listaPesos[14] = 0.4;
		listaPesos[15] = 0.4;
		listaPesos[16] = 0.2;	
	}
	
	public static void main (String args[]) {
		Principal principal = new Principal();
		
		ValidacionCruzada validador = new ValidacionCruzada(); 
		try {
			if (JOptionPane.showConfirmDialog(null, "Hacer HoldOutEval?")==JOptionPane.OK_OPTION)
				validador.HoldOutEvaluation(principal.getListaPesos());
			if (JOptionPane.showConfirmDialog(null, "Hacer LeaveOneOutEval?")==JOptionPane.OK_OPTION)
					validador.LeaveOneOutEvaluation(principal.getListaPesos());
			if (JOptionPane.showConfirmDialog(null, "Hacer SameSplitEval?")==JOptionPane.OK_OPTION)
				validador.SameSplitEvaluation(principal.getListaPesos());
		} catch	(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
