package proyecto2.quinielas.representacion;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class DescripcionQuinielas implements CaseComponent {

	private int id;
	private String temporada;
	private String liga;
	private int jornada;
	private String local;
	private String visitante;
	
	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getTemporada() {
		return temporada;
	}



	public void setTemporada(String temporada) {
		this.temporada = temporada;
	}



	public String getLiga() {
		return liga;
	}



	public void setLiga(String liga) {
		this.liga = liga;
	}



	public int getJornada() {
		return jornada;
	}



	public void setJornada(int jornada) {
		this.jornada = jornada;
	}



	public String getLocal() {
		return local;
	}



	public void setLocal(String local) {
		this.local = local;
	}



	public String getVisitante() {
		return visitante;
	}



	public void setVisitante(String visitante) {
		this.visitante = visitante;
	}



	public int getGolesLocal() {
		return golesLocal;
	}



	public void setGolesLocal(int golesLocal) {
		this.golesLocal = golesLocal;
	}



	public int getGolesVisitante() {
		return golesVisitante;
	}



	public void setGolesVisitante(int golesVisitante) {
		this.golesVisitante = golesVisitante;
	}



	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id",DescripcionQuinielas.class);
	}



	@Override
	public String toString() {
		return id + "," + temporada + "," + liga + "," + jornada + "," + local
				+ "," + visitante + "," + golesLocal + "," + golesVisitante;
	}
	
	

}
