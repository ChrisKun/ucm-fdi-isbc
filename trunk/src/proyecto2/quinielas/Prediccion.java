package proyecto2.quinielas;

import proyecto2.quinielas.representacion.SolucionQuinielas.UnoXDos;

/*
 * Clase que devuelve el resultado con una medida de confianza entre [0,1] 
 */

public class Prediccion {
	
	private UnoXDos resultado;	
	private double confianza;

	@Override
	public String toString() {
		return "Prediccion [resultado=" + resultado + ", confianza="
				+ confianza + "]";
	}

	public UnoXDos getResultado() {
		return resultado;
	}

	public void setResultado(UnoXDos resultado) {
		this.resultado = resultado;
	}

	public double getConfianza() {
		return confianza;
	}

	public void setConfianza(double confianza) {
		this.confianza = confianza;
	}
}
