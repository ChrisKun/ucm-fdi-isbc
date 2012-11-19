package proyecto2.quinielas.datosWeb;

public class Clasificacion {

	private int pos;
	private String eq;
	private int pj,pg,pe,pp;
	private int gf,gc;
	private int ptos;
	
	public Clasificacion(int pos, String eq, int pj, int pg, int pe, int pp, int gf, int gc, int ptos) {
		this.eq = eq;
		this.pos = pos;
		this.pj = pj;
		this.pg = pg;
		this.pe = pe;
		this.pp = pp;
		this.gf = gf;
		this.gc = gc;
		this.ptos = ptos;
	}
	
	public String toString() {
		return "Clasificacion [eq=" + eq + ", pj=" + pj + ", pos=" + pos
				+ ", pg=" + pg + ", pe=" + pe + ", pp=" + pp + ", gf=" + gf
				+ ", gc=" + gc + ", ptos=" + ptos + "]\n";
	}

	public String toFile() {
		return eq +","+ pj +","+ pos +","+ pg +","+ pe +","+ pp +","+ gf +","+ gc +","+ ptos ;
	}

	public String getEq() {
		return eq;
	}
	
	
}
