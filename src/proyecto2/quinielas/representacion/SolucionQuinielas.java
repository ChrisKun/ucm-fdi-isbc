package proyecto2.quinielas.representacion;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/*
 * Clase que define nuestra solución
 */
public class SolucionQuinielas implements CaseComponent {

	public enum UnoXDos {UNO, X, DOS};
	
	private String id;
	private UnoXDos solucion;
	
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public UnoXDos getSolucion() {
		return solucion;
	}



	public void setSolucion(UnoXDos solucion) {
		this.solucion = solucion;
	}



	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", SolucionQuinielas.class);
	}



	@Override
	public String toString() {
		return id + "," + solucion;
	}

	
	
}
