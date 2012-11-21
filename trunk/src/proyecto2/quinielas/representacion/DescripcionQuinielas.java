package proyecto2.quinielas.representacion;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class DescripcionQuinielas implements CaseComponent {	

	// Numero de campos que tenemos
	public final static int NUMCAMPOS = 20;
	private int id;
	private int temporada;
	private String local;
	private String visitante;
	private int puntosLocal;
	private int pgLocal;
	private int peLocal;
	private int ppLocal;
	private int puntosVisitante;
	private int pgVisitante;
	private int peVisitante;
	private int ppVisitante;
	private int posLocal;
	private int posVisitante;
	private int gfavorLocal;
	private int gcontraLocal;
	private int gfavorVisitante;
	private int gcontraVisitante;

	public DescripcionQuinielas(int temporada,String local, int[] clasifLocal, String visitante, int[] clasifVisitante) {
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



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getTemporada() {
		return temporada;
	}



	public void setTemporada(int temporada) {
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



	public int getPuntosLocal() {
		return puntosLocal;
	}



	public void setPuntosLocal(int puntosLocal) {
		this.puntosLocal = puntosLocal;
	}



	public int getPgLocal() {
		return pgLocal;
	}



	public void setPgLocal(int pgLocal) {
		this.pgLocal = pgLocal;
	}



	public int getPeLocal() {
		return peLocal;
	}



	public void setPeLocal(int peLocal) {
		this.peLocal = peLocal;
	}



	public int getPpLocal() {
		return ppLocal;
	}



	public void setPpLocal(int ppLocal) {
		this.ppLocal = ppLocal;
	}



	public int getPuntosVisitante() {
		return puntosVisitante;
	}



	public void setPuntosVisitante(int puntosVisitante) {
		this.puntosVisitante = puntosVisitante;
	}



	public int getPgVisitante() {
		return pgVisitante;
	}



	public void setPgVisitante(int pgVisitante) {
		this.pgVisitante = pgVisitante;
	}



	public int getPeVisitante() {
		return peVisitante;
	}



	public void setPeVisitante(int peVisitante) {
		this.peVisitante = peVisitante;
	}



	public int getPpVisitante() {
		return ppVisitante;
	}



	public void setPpVisitante(int ppVisitante) {
		this.ppVisitante = ppVisitante;
	}



	public int getPosLocal() {
		return posLocal;
	}



	public void setPosLocal(int posLocal) {
		this.posLocal = posLocal;
	}



	public int getPosVisitante() {
		return posVisitante;
	}



	public void setPosVisitante(int posVisitante) {
		this.posVisitante = posVisitante;
	}



	public int getGfavorLocal() {
		return gfavorLocal;
	}



	public void setGfavorLocal(int gfavorLocal) {
		this.gfavorLocal = gfavorLocal;
	}



	public int getGcontraLocal() {
		return gcontraLocal;
	}



	public void setGcontraLocal(int gcontraLocal) {
		this.gcontraLocal = gcontraLocal;
	}



	public int getGfavorVisitante() {
		return gfavorVisitante;
	}



	public void setGfavorVisitante(int gfavorVisitante) {
		this.gfavorVisitante = gfavorVisitante;
	}



	public int getGcontraVisitante() {
		return gcontraVisitante;
	}



	public void setGcontraVisitante(int gcontraVisitante) {
		this.gcontraVisitante = gcontraVisitante;
	}



	public Attribute getIdAttribute() {
		return new Attribute("id",DescripcionQuinielas.class);
	}
	

}
