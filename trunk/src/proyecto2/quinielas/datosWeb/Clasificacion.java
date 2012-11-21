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
		return "\nClasificacion [\n" +
				"Equipo (eq) = " + eq + ",\n" +
				"Partidos Jugados (pj) o Jornada = " + pj + ", \n" +
				"Posicion (pos) = " + pos + ",\n" +
				"Partidos Ganados (pg)   = " + pg + ",\n" +
				"Partidos Empatados (pe) = " + pe + ",\n" +
				"Partidos Perdidos (pp)  = " + pp + ",\n" +
				"Goles a Favor (gf)      = " + gf + ",\n" +
				"Goles en Contra (gc)    = " + gc + ",\n" +
				"Puntos (ptos) = " + ptos + "]\n";
	}

	public String toFile() {
		return eq +","+ pj +","+ pos +","+ pg +","+ pe +","+ pp +","+ gf +","+ gc +","+ ptos ;
	}

	public String getEq() {
		return eq;
	}
	
	
}
