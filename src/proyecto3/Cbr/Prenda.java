package Cbr;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class Prenda implements CaseComponent {
	private String id;
	private String categoria;
	private String division;
	private String precio;
	private String lavado;

	public Attribute getIdAttribute() {
		return new Attribute("id",Prenda.class);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public String getLavado() {
		return lavado;
	}
	public void setLavado(String lavado) {
		this.lavado = lavado;
	}
}
