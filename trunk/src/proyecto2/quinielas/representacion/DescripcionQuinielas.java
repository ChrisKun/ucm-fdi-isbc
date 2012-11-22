package proyecto2.quinielas.representacion;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class DescripcionQuinielas implements CaseComponent {	

	// Numero de campos que tenemos
	//public final static Integer NUMCAMPOS = 18;
	
	private String id;
	private Integer temporada;
	private String local;
	private String visitante;
	private Integer puntosLocal;
	private Integer pgLocal;
	private Integer peLocal;
	private Integer ppLocal;
	private Integer puntosVisitante;
	private Integer pgVisitante;
	private Integer peVisitante;
	private Integer ppVisitante;
	private Integer posLocal;
	private Integer posVisitante;
	private Integer gfavorLocal;
	private Integer gcontraLocal;
	private Integer gfavorVisitante;
	private Integer gcontraVisitante;

	// Constructor que inicializa desde dos String que contienen la información de ambos equipos
	public DescripcionQuinielas(Integer temporada,String local, Integer[] clasifLocal, String visitante, Integer[] clasifVisitante) {
		this.temporada = temporada;
		this.local = local;
		this.visitante = visitante;
		this.posLocal = clasifLocal[1];		
		this.pgLocal = clasifLocal[2];
		this.peLocal = clasifLocal[3];
		this.ppLocal = clasifLocal[4];
		this.gfavorLocal = clasifLocal[5];
		this.gcontraLocal = clasifLocal[6];
		this.puntosLocal = clasifLocal[7];
		this.posVisitante = clasifVisitante[1];		
		this.pgVisitante = clasifVisitante[2];
		this.peVisitante = clasifVisitante[3];
		this.ppVisitante = clasifVisitante[4];
		this.gfavorVisitante = clasifVisitante[5];
		this.gcontraVisitante = clasifVisitante[6];
		this.puntosVisitante = clasifVisitante[7];
	}
	
	@Override
	public String toString() {
		return "DescripcionQuinielas [id=" + id + ", temporada=" + temporada
				+ ", local=" + local + ", visitante=" + visitante
				+ ", puntosLocal=" + puntosLocal + ", pgLocal=" + pgLocal
				+ ", peLocal=" + peLocal + ", ppLocal=" + ppLocal
				+ ", puntosVisitante=" + puntosVisitante + ", pgVisitante="
				+ pgVisitante + ", peVisitante=" + peVisitante
				+ ", ppVisitante=" + ppVisitante + ", posLocal=" + posLocal
				+ ", posVisitante=" + posVisitante + ", gfavorLocal="
				+ gfavorLocal + ", gcontraLocal=" + gcontraLocal
				+ ", gfavorVisitante=" + gfavorVisitante
				+ ", gcontraVisitante=" + gcontraVisitante + "]";
	}


	public DescripcionQuinielas() {
		super();
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public Integer getTemporada() {
		return temporada;
	}



	public void setTemporada(Integer temporada) {
		this.temporada = temporada;
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



	public Integer getPuntosLocal() {
		return puntosLocal;
	}



	public void setPuntosLocal(Integer puntosLocal) {
		this.puntosLocal = puntosLocal;
	}



	public Integer getPgLocal() {
		return pgLocal;
	}



	public void setPgLocal(Integer pgLocal) {
		this.pgLocal = pgLocal;
	}



	public Integer getPeLocal() {
		return peLocal;
	}



	public void setPeLocal(Integer peLocal) {
		this.peLocal = peLocal;
	}



	public Integer getPpLocal() {
		return ppLocal;
	}



	public void setPpLocal(Integer ppLocal) {
		this.ppLocal = ppLocal;
	}



	public Integer getPuntosVisitante() {
		return puntosVisitante;
	}



	public void setPuntosVisitante(Integer puntosVisitante) {
		this.puntosVisitante = puntosVisitante;
	}



	public Integer getPgVisitante() {
		return pgVisitante;
	}



	public void setPgVisitante(Integer pgVisitante) {
		this.pgVisitante = pgVisitante;
	}



	public Integer getPeVisitante() {
		return peVisitante;
	}



	public void setPeVisitante(Integer peVisitante) {
		this.peVisitante = peVisitante;
	}



	public Integer getPpVisitante() {
		return ppVisitante;
	}



	public void setPpVisitante(Integer ppVisitante) {
		this.ppVisitante = ppVisitante;
	}



	public Integer getPosLocal() {
		return posLocal;
	}



	public void setPosLocal(Integer posLocal) {
		this.posLocal = posLocal;
	}



	public Integer getPosVisitante() {
		return posVisitante;
	}



	public void setPosVisitante(Integer posVisitante) {
		this.posVisitante = posVisitante;
	}



	public Integer getGfavorLocal() {
		return gfavorLocal;
	}



	public void setGfavorLocal(Integer gfavorLocal) {
		this.gfavorLocal = gfavorLocal;
	}



	public Integer getGcontraLocal() {
		return gcontraLocal;
	}



	public void setGcontraLocal(Integer gcontraLocal) {
		this.gcontraLocal = gcontraLocal;
	}



	public Integer getGfavorVisitante() {
		return gfavorVisitante;
	}



	public void setGfavorVisitante(Integer gfavorVisitante) {
		this.gfavorVisitante = gfavorVisitante;
	}



	public Integer getGcontraVisitante() {
		return gcontraVisitante;
	}



	public void setGcontraVisitante(Integer gcontraVisitante) {
		this.gcontraVisitante = gcontraVisitante;
	}



	public Attribute getIdAttribute() {
		return new Attribute("id",DescripcionQuinielas.class);
	}
	

}
