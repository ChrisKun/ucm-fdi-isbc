package proyecto2.quinielas;

import java.util.Collection;

import proyecto2.quinielas.representacion.SolucionQuinielas.UnoXDos;

import jcolibri.method.retrieve.RetrievalResult;

public class Votacion {

	// Metodo que implementa la media
	public Prediccion media (Collection<RetrievalResult> eval) {
		// resQuini[0] = 'X', resQuini[1] = '1', resQuini[2] = '2'
			int [] resQuini = new int[3];
			
			// Recorremos los casos recogidos y los vamos sumando segun su tipo 
			for (RetrievalResult nse: eval){
				String sol = nse.get_case().getSolution().toString();			
				if (sol.contains("UNO")) {
					resQuini[1] = resQuini[1]+1;
				} else if (sol.contains("DOS")) {
					resQuini[2] = resQuini[2]+1;
				} else
					resQuini[0] = resQuini[0]+1;
			}
			
			Prediccion prediccion = new Prediccion();
			int suma = 0;
			// Hacemos la media del caso mayoritario (En caso de empate se devuelve uno de los mayoritarios)
			for (int i = 0; i < resQuini.length; i++) {
				suma = suma + resQuini[i];
				if (resQuini[i] > prediccion.getConfianza()) {
					prediccion.setConfianza(resQuini[i]);
					switch (i) {
						case 0: prediccion.setResultado(UnoXDos.X); break;
						case 1: prediccion.setResultado(UnoXDos.UNO); break;
						case 2: prediccion.setResultado(UnoXDos.DOS); break;				
					}
				}
			}
			prediccion.setConfianza(prediccion.getConfianza()/suma);
			// Devolvemos el objeto con el resultado y su confianza
			return prediccion;
	}
	
	// Metodo que implementa la media ponderada
	public Prediccion mediaPonderada(Collection<RetrievalResult> eval) {
		// resQuini[0] = 'X', resQuini[1] = '1', resQuini[2] = '2'
		double [] resQuini = new double[3];
		
		// Recorremos los casos recogidos y los vamos sumando segun su tipo 
		for (RetrievalResult nse: eval){
			String sol = nse.get_case().getSolution().toString();			
			if (sol.contains("UNO")) {
				resQuini[1] = resQuini[1]+nse.getEval();
			} else if (sol.contains("DOS")) {
				resQuini[2] = resQuini[2]+nse.getEval();
			} else
				resQuini[0] = resQuini[0]+nse.getEval();
		}
		
		Prediccion prediccion = new Prediccion();
		double suma = 0;
		// Hacemos la media ponderada del caso cuya confianza sea mayor
		for (int i = 0; i < resQuini.length; i++) {
			suma = suma + resQuini[i];
			if (resQuini[i] > prediccion.getConfianza()) {
				prediccion.setConfianza(resQuini[i]);
				switch (i) {
					case 0: prediccion.setResultado(UnoXDos.X); break;
					case 1: prediccion.setResultado(UnoXDos.UNO); break;
					case 2: prediccion.setResultado(UnoXDos.DOS); break;				
				}
			}
		}
		prediccion.setConfianza(prediccion.getConfianza()/suma);
		// Devolvemos el objeto con el resultado y su confianza
		return prediccion;
	}
	
}
