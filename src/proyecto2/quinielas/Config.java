package proyecto2.quinielas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * Clase que contiene la �ltima informaci�n disponible en la Web y la informaci�n que solicita el usuario.
 * Tambi�n contiene un array que contiene la clasificaci�n de primera y segunda divisi�n se�n temporada y jornada.
 * clasificacionXXX[temporada,jornada,pos_equipo] = Hashmap. Si un campo es NULL es porque no se cuenta con esa informaci�n en la base de casos
 * 
 */

public class Config {
	
	/* CONSTANTES */ 
	public static final int JORNADASPRIMERA = 38;
	public static int JORNADASSEGUNDA = 42;
	public static int NUMEROEQUIPOSPRIMERA = 20;
	public static int NUMEROEQUIPOSSEGUNDA = 22;
	
	/* ATRIBUTOS */
	private Integer ultimaTemporada;
	private Integer ultimaJornadaPrimera;
	private Integer ultimaJornadaSegunda;
	
	private Integer seleccionTemporada;
	private Integer seleccionJornadaPrimera;
	private Integer seleccionJornadaSegunda;
	
	// Estructuras que almacenan las clasificaciones de primera y segunda divisi�n seg�n temporada y jornada
	private String[][][] clasificacionesPrimera;
	private String[][][] clasificacionesSegunda;
	
	/* CONSTRUCTORAS */
	public Config() {
		// Leer de fichero
		cargarDatosTemporadaJornadaActual("config.txt");
		//ultimaTemporada = 0;
		//ultimaJornadaPrimera = 0;
		//ultimaJornadaSegunda = 0;
		seleccionTemporada = 2000;
		seleccionJornadaPrimera = 0;
		seleccionJornadaSegunda = 0;
		rellenaClasificacionesPrimera();
		rellenaClasificacionesSegunda();		
	}
		
	/* FUNCIONES */ 
	public void rellenaClasificacionesPrimera() {
		if (ultimaTemporada < 2000) return;
		int numeroTemporadas = (ultimaTemporada - 2000) + 1;
		clasificacionesPrimera = new String[numeroTemporadas][JORNADASPRIMERA][NUMEROEQUIPOSPRIMERA];
		rellenaEstructura(clasificacionesPrimera, 1);
	}
	
	public void rellenaClasificacionesSegunda() {
		if (ultimaTemporada < 2000) return;
		int numeroTemporadas = (ultimaTemporada - 2000) + 1;
		clasificacionesSegunda = new String[numeroTemporadas][JORNADASSEGUNDA][NUMEROEQUIPOSSEGUNDA];
		rellenaEstructura(clasificacionesSegunda, 2);
	}
	private void rellenaEstructura (String[][][] clasificaciones, int liga) {
		int temporada = 0;
		int jornada = 0;
		int i = 0;
		
		try {
			String file;
			// Seleccion del archivo
			if (liga == 1) file = "clasPrimera.txt";
			else file = "clasSegunda.txt";
			
			BufferedReader br = new BufferedReader(new FileReader(".\\src\\proyecto2\\quinielas\\datos\\" + file));
			String line = null;
			// Rellenamos la tabla cogiendo la informacion de la temporada y jornada de las filas con formato AaaaaJjj
			while((line=br.readLine())!=null){				
				if (line.startsWith("A") && line.length() == 8) {
					i=0;
					String[] tokens = line.split("A|J");		
					temporada = Integer.valueOf(tokens[1]) - 2000;
					jornada = Integer.valueOf(tokens[2]);
					if (temporada == 12) {
						i=0;
					}
				} else {
					// La jornada en el array se almacena siempre empezando en el 0, asi que la jornada tiene que ser: jornada-1
					if (liga == 1) clasificacionesPrimera[temporada][jornada-1][i] = line;
					else clasificacionesSegunda[temporada][jornada-1][i] = line;
					i++;
				}
			}
			br.close();
		} catch (Exception e) {
			System.err.println("Error lectura en temporada: "+temporada+" jornada: "+ jornada + " posicion de equipo: " + i);
			e.printStackTrace();
		}
	}
	
	/* GETTERS/SETTERS */
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
	public String[][][] getClasificacionesSegunda() {
		return clasificacionesSegunda;
	}
	public String[][][] getClasificacionesPrimera() {
		return clasificacionesPrimera;
	}
	
	public void cargarDatosTemporadaJornadaActual(String file)
	{		
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(".\\src\\proyecto2\\quinielas\\datos\\" + file));
			ultimaTemporada = Integer.parseInt(br.readLine());
			ultimaJornadaPrimera = Integer.parseInt(br.readLine());
			ultimaJornadaSegunda = Integer.parseInt(br.readLine());
			br.close();	
		} 	
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
		
}
