package proyecto2.quinielas.datosWeb;

import java.util.ArrayList;


public class Jornada {

	private int anyo;
	private ArrayList<Partido> partidos;
	private ArrayList<Clasificacion> clasificacion;
	
	public Jornada(int anyo, ArrayList<Partido> partidos, ArrayList<Clasificacion> clasificacion) {
		this.anyo = anyo;
		this.partidos = partidos;
		this.clasificacion = clasificacion;
	}

	public String toString() {
		return "Jornada [anyo=" + anyo + ", partidos=" + partidos
				+ ", clasificacion=" + clasificacion + "]";
	}
	
	public ArrayList<String> toFile() {
		ArrayList<String> resultados = new ArrayList<String>();
		String temp_s = "";
		for (Partido partido: partidos){
			temp_s = anyo + "," + partido.toFile();
			for (Clasificacion clas: clasificacion){
				if (clas.getEq().equals(partido.getEquipoLocal()))
					temp_s = temp_s +','+ clas.toFile();
			}
			for (Clasificacion clas: clasificacion){
				if (clas.getEq().equals(partido.getEquipoVisitante()))
					temp_s = temp_s +','+ clas.toFile();
			}
			resultados.add(temp_s);
		}
		
	    return resultados;
	}
}
