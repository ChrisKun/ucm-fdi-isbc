package proyecto2.quinielas;

/**
 * 
 * Clase que contiene la última información disponible en la Web y la información que solicita el usuario.
 * También contiene la estructura de datos que almacena las clasificaciones de cada equipo por temporada y jornada
 *
 */

public class Config {
	
	private Integer ultimaTemporada;
	private Integer ultimaJornadaPrimera;
	private Integer ultimaJornadaSegunda;
	
	private Integer seleccionTemporada;
	private Integer seleccionJornadaPrimera;
	private Integer seleccionJornadaSegunda;
	
	public Integer getUltimaTemporada() {
		return ultimaTemporada;
	}
	public void setUltimaTemporada(Integer ultimaTemporada) {
		this.ultimaTemporada = ultimaTemporada;
	}
	public Integer getUltimaJornadaPrimera() {
		return ultimaJornadaPrimera;
	}
	public void setUltimaJornadaPrimera(Integer ultimaJornadaPrimera) {
		this.ultimaJornadaPrimera = ultimaJornadaPrimera;
	}
	public Integer getUltimaJornadaSegunda() {
		return ultimaJornadaSegunda;
	}
	public void setUltimaJornadaSegunda(Integer ultimaJornadaSegunda) {
		this.ultimaJornadaSegunda = ultimaJornadaSegunda;
	}
	public Integer getSeleccionTemporada() {
		return seleccionTemporada;
	}
	public void setSeleccionTemporada(Integer seleccionTemporada) {
		this.seleccionTemporada = seleccionTemporada;
	}
	public Integer getSeleccionJornadaPrimera() {
		return seleccionJornadaPrimera;
	}
	public void setSeleccionJornadaPrimera(Integer seleccionJornadaPrimera) {
		this.seleccionJornadaPrimera = seleccionJornadaPrimera;
	}
	public Integer getSeleccionJornadaSegunda() {
		return seleccionJornadaSegunda;
	}
	public void setSeleccionJornadaSegunda(Integer seleccionJornadaSegunda) {
		this.seleccionJornadaSegunda = seleccionJornadaSegunda;
	}

}
