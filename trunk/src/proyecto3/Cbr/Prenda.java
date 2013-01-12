package Cbr;

import GAPDataBase.Product;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class Prenda implements CaseComponent {
	private Integer id;
	private String categoria;
	private String division;
	private Float precio;
	private String lavado;

	public Prenda (Product p) {
		this.id = p.getId();
		this.categoria = p.getCategory();
		this.division = p.getDivision();
		this.precio = Float.valueOf(p.getPrice().replaceAll("[a-z]|[A-Z]",""));
		this.lavado = p.getWashing();
	}
	
	public Attribute getIdAttribute() {
		return new Attribute("id",Prenda.class);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public Float getPrecio() {
		return precio;
	}
	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	public String getLavado() {
		return lavado;
	}
	public void setLavado(String lavado) {
		this.lavado = lavado;
	}
}
