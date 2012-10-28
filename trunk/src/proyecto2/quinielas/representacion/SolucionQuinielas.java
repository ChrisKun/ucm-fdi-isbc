package proyecto2.quinielas.representacion;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class SolucionQuinielas implements CaseComponent {

	public enum UnoXDos {UNO, X, DOS};
	
	private int id;
	private UnoXDos solucion;
	
	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
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
