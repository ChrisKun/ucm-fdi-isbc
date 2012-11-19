package proyecto2.quinielas.datosWeb;

public class Partido {
	
	//Atributos
	private String equipoLocal;
	private String equipoVisitante;
	private int		resEqLocal;
	private int		resEqVisitante;
	private char resultado;
	
	public Partido(String equipoLocal, String equipoVisitante, int resEqLocal, int resEqVisitante){
		this.equipoLocal = equipoLocal;
		this.equipoVisitante = equipoVisitante;
		this.resEqLocal = resEqLocal;
		this.resEqVisitante = resEqVisitante;
		if (resEqLocal < resEqVisitante)
			resultado = '1';
		else if (resEqLocal == resEqVisitante)
			resultado = 'X';
		else
			resultado = '2';
	}
	
	public String toString() {
		return "Partido [equipoLocal=" + equipoLocal + ", equipoVisitante="
				+ equipoVisitante + ", resEqLocal=" + resEqLocal
				+ ", resEqVisitante=" + resEqVisitante + ", resultado="
				+ resultado + "]\n";
	}

	public String toFile() {
		return equipoLocal +","+ equipoVisitante +","+ resultado;
	}

	public String getEquipoLocal() {
		return equipoLocal;
	}

	public String getEquipoVisitante() {
		return equipoVisitante;
	}
	
	
}
