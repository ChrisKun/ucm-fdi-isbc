package proyecto2.quinielas;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import proyecto2.quinielas.cbr.DescripcionQuinielas;
import proyecto2.quinielas.cbr.Prediccion;
import proyecto2.quinielas.cbr.Quinielas;
import proyecto2.quinielas.cbr.ValidacionCruzada;
import proyecto2.quinielas.datosWeb.ParserWeb;
import proyecto2.quinielas.interfaz.BarraProgreso;
import proyecto2.quinielas.interfaz.Interfaz;

public class Principal {
	
	//TODO prueba
	private static BarraProgreso barra; //Ventana auxiliar con la ProgressBar
	
	// Constantes para que sea más comodo operar
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
	
	// Pesos por defecto
	public final static double PESOTEMPORADA = 0.4;
	public final static double PESOLOCAL = 0.02;
	public final static double PESOVISITANTE = 0.02;
	public final static double PESOPUNTOSLOCAL = 0.2;
	public final static double PESOPGLOCAL = 0.1;
	public final static double PESOPELOCAL = 0.025;
	public final static double PESOPPLOCAL = 0.05;
	public final static double PESOPUNTOSVISITANTE = 0.25;
	public final static double PESOPGVISITANTE = 0.15;
	public final static double PESOPEVISITANTE = 0.025;
	public final static double PESOPPVISITANTE = 0.01;
	public final static double PESOPOSLOCAL = 0.3;
	public final static double PESOPOSVISITANTE = 0.3;
	public final static double PESOGFAVORLOCAL = 0.35;
	public final static double PESOGCONTRALOCAL = 0.4;
	public final static double PESOGFAVORVISITANTE = 0.4;
	public final static double PESOGCONTRAVISITANTE = 0.2;
	
	
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
		listaPesos[TEMPORADA] = PESOTEMPORADA;
		listaPesos[LOCAL] = PESOLOCAL;
		listaPesos[VISITANTE] = PESOVISITANTE;
		listaPesos[PUNTOSLOCAL] = PESOPUNTOSLOCAL;
		listaPesos[PGLOCAL] = PESOPGLOCAL;
		listaPesos[PELOCAL] = PESOPELOCAL;
		listaPesos[PPLOCAL] = PESOPPLOCAL;
		listaPesos[PUNTOSVISITANTE] = PESOPUNTOSVISITANTE;
		listaPesos[PGVISITANTE] = PESOPGVISITANTE;
		listaPesos[PEVISITANTE] = PESOPEVISITANTE;
		listaPesos[PPVISITANTE] = PESOPPVISITANTE;
		listaPesos[POSLOCAL] =  PESOPOSLOCAL;
		listaPesos[POSVISITANTE] = PESOPOSVISITANTE;
		listaPesos[GFAVORLOCAL] = PESOGFAVORLOCAL;
		listaPesos[GCONTRALOCAL] = PESOGCONTRALOCAL;
		listaPesos[GFAVORVISITANTE] = PESOGFAVORVISITANTE;
		listaPesos[GCONTRAVISITANTE] = PESOGCONTRAVISITANTE;	
	}
	
	public static void main (String args[]) {
		// Configuración de pesos
		Principal principal = new Principal();
		Interfaz i;
		principal.iniciaPesos();
		
		// PASO 1. LANZAR EL PARSER
		ParserWeb parser = new ParserWeb();
		barra = new BarraProgreso(BarraProgreso.MODOPARSER,0);
		try {
			//Interfaz.mensajeEsperaParser();
	
			parser.main(args);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// PASO 2. CONFIGURAR 
		Config c = new Config();
		Quinielas q = new Quinielas(principal.getListaPesos());
		q.configCBR();
		
		// PASO 3. LLAMAR A LA INTERFAZ
		barra.cerrarVentana(); //cerramos la ventana
		i = new Interfaz(principal.getListaPesos(),c,q);
		
		
		//q.querysCBR(a,2012,4,principal.listaPesos,c.getClasificacionesPrimera(),c.getClasificacionesSegunda());
		//ValidacionCruzada validador = new ValidacionCruzada(); 
	}
	
	public static BarraProgreso getBarra() {
		return barra;
	}
	
	public static void setBarra(BarraProgreso barra) {
		Principal.barra = barra;
	}

}
