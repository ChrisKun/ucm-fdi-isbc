package proyecto2.quinielas;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import proyecto2.quinielas.cbr.DescripcionQuinielas;
import proyecto2.quinielas.cbr.Prediccion;
import proyecto2.quinielas.cbr.Quinielas;
import proyecto2.quinielas.cbr.ValidacionCruzada;

/**
 * 
 * @author Xavi
 *
 * Jornada:
 * 		AaaaaJjj
 * 		Ejemplo: A2007J03
 * 
 * Clasificaciones:
 * 		20x Clasificacion (en primera)
 * 		22x Clasificacion (en segunda)
 * 
 * Formato fichero:
 * 		Para todas las jornadas{
 * 			Jornada
 * 			Clasificaciones
 * 		}
 */

public class Principal {
	
	// TODO Raul, aqui te dejo las constantes para que te sea más comodo operar
	public final static int TEMPORADA = 0;
	public final static int LOCAL = 1;
	public final static int VISITANTE = 2;
	public final static int PUNTOSLOCAL = 3;
	public final static int PGLOCAL = 4;
	public final static int PELOCAL = 5;
	public final static int PPLOCAL = 6;
	public final static int PUNTOSVISITANTE = 7;
	public final static int PGVISITANTE = 8;
	public final static int PEVISITANTE = 9;
	public final static int PPVISITANTE = 10;
	public final static int POSLOCAL = 11;
	public final static int POSVISITANTE = 12;
	public final static int GFAVORLOCAL = 13;
	public final static int GCONTRALOCAL = 14;
	public final static int GFAVORVISITANTE = 15;
	public final static int GCONTRAVISITANTE = 16;
	
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
		principal.iniciaPesos();
		Config c = new Config();
		c.setUltimaTemporada(2012);
		c.rellenaClasificacionesPrimera();
		c.rellenaClasificacionesSegunda();
		Quinielas q = new Quinielas(principal.getListaPesos());
		
		// 	public ArrayList<Prediccion> querysCBR (ArrayList<String> equipos, int temporada, int jornada, double[] listaPesos,
		// String[][][] clasificacionesPrimera, String[][][] clasificacionesSegunda) {
		ArrayList<String> a = new ArrayList<String>();
		a.add("Valencia,Málaga");
		a.add("Espanyol,Rayo");
		
		q.querysCBR(a,2012,4,principal.listaPesos,c.getClasificacionesPrimera(),c.getClasificacionesSegunda());
	}

}
