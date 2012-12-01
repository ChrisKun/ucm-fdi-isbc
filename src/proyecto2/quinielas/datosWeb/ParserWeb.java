package proyecto2.quinielas.datosWeb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import proyecto2.quinielas.interfaz.BarraProgreso;


public class ParserWeb {
	
	public static int TEMPORADAINICIAL = 2000; // TODO Añadido para usar en interfaz tambien... (RAUL)

	public int getUltimaTemporada() {
		return ultimaTemporada;
	}


	public int getUltimaJornadaPrimera() {
		return ultimaJornadaPrimera;
	}


	public int getUltimaJornadaSegunda() {
		return ultimaJornadaSegunda;
	}

	private final static String rutaDatos = 
			"."+File.separatorChar+
			"src"+File.separatorChar+
			"proyecto2"+File.separatorChar+
			"quinielas"+File.separatorChar+
			"datos";
	
	private ArrayList<String> jornadas;
	HashMap<String,ArrayList<Clasificacion>> clasPorJornPrim;
	HashMap<String,ArrayList<Clasificacion>> clasPorJornSeg;
	private ArrayList<String> paginasError;
	private ArrayList<String> partidosPorJugar;
	
	int anyoInicio = 2000; // TODO Si quieres, usa aqui la constante
	int ultimaTemporada = 2012;
	int inicioJornPrim=1;
	int inicioJornSeg=1;
	int ultimaJornadaPrimera=1;
	int ultimaJornadaSegunda=1;
	
	public void ejecutarParser(){		
	
		jornadas = new ArrayList<String>(); 
		clasPorJornPrim = new HashMap<String, ArrayList<Clasificacion>>();
		clasPorJornSeg = new HashMap<String, ArrayList<Clasificacion>>();
		paginasError = new ArrayList<String>();
		partidosPorJugar = new ArrayList<String>();
		
		
		File carpeta = new File(rutaDatos);
		if (!carpeta.exists()) {
			carpeta.mkdir();
		}else{
			try {
				cargarFicheros();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//1. Leer el fichero de configuracion
		leerConfiguracion();
		    
		//2. Coger nueva configuración y guardarla en fichero de texto
		actualizarConfiguracion();
		
		//3. Procesar todas las jornadas que toquen
		for (int i=anyoInicio;i<ultimaTemporada;i++){
			for (int j=1;j<=38;j++){
				anyadirJornada("primera",i,j);
				BarraProgreso.aumentarBarraProgreso(); //Para aumentar la barra de progreso
			}
			for (int j=1;j<=42;j++){
				anyadirJornada("segunda",i,j);
				BarraProgreso.aumentarBarraProgreso();
			}
		}
		for (int j=inicioJornPrim;j<=ultimaJornadaPrimera;j++){
			anyadirJornada("primera",ultimaTemporada,j);
			BarraProgreso.aumentarBarraProgreso();
		}
		for (int j=inicioJornSeg;j<=ultimaJornadaSegunda;j++){
			anyadirJornada("segunda",ultimaTemporada,j);
			BarraProgreso.aumentarBarraProgreso();
		}

		  
	    ultimaJornadaPrimera++;
	    ultimaJornadaSegunda++;
	    
		try {
			guardarFicheros();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void anyadirJornada(String categoria, int anyo, int jorn){

		
		//Paginas Web mal diseñadas
		if (anyo == 2003 && jorn == 1) return;
		if (anyo == 2011 && jorn == 7) return;
		if (anyo == 2011 && jorn == 33) return;

		int numEquipos  = categoria.equals("primera")? 20 : 22;
		int numPartidos = (numEquipos/2);
		Document doc;
		Elements tabla;
		ArrayList<String> info = new ArrayList<String>();

		String anyo2 = String.valueOf(anyo+1).substring(2);
		String dir = "http://www.marca.com/estadisticas/futbol/"+categoria+"/"+anyo+"_"+anyo2+"/jornada_"+jorn+"/";

		System.out.println(dir);
		
		try {
			doc = Jsoup.connect(dir).get();
			tabla = doc.getElementsByTag("td");
			for (Element linea: tabla){
				info.add(linea.text());
			}
		} catch (IOException e) {
			System.err.println(dir+"?");
			paginasError.add(dir);
			return;
		}
		ArrayList<Partido> partidos = new ArrayList<Partido>();
		String[] res; int res1 = 0, res2 = 0;
		//TODO cambiar esta guarreria!!!
		int n = 4;
    //numPartidos*n-1;
		//TODO Parsear uno por uno los resultados de los partidos
		for (int i=0;i<=numPartidos*n-1;i=i+n){
			//Procesar info de UN partido
			try{
				res = info.get(i+1).split("-");
				res1 = Integer.parseInt(res[0]);
				res2 = Integer.parseInt(res[1]);
			} catch (Exception e){
				partidosPorJugar.add(info.get(i+1) +","+info.get(i)+","+info.get(i+2));
				n = 3;
			}
			partidos.add(new Partido(info.get(i), info.get(i+2), res1, res2));
		}
		
		//TODO Parsear una por una la clasificacion de un equipo
		ArrayList<Clasificacion> clasificacion = new ArrayList<Clasificacion>();
		for (int i=numPartidos*n;i<=numEquipos*9+numPartidos*n-1;i=i+9){
			//Procesar clasificacion de UN equipo
			//procesarClasificacion(String clasificacion)
			clasificacion.add(new Clasificacion(
					Integer.parseInt(info.get(i)), 
					info.get(i+1), 
					Integer.parseInt(info.get(i+2)),
					Integer.parseInt(info.get(i+3)),
					Integer.parseInt(info.get(i+4)),
					Integer.parseInt(info.get(i+5)),
					Integer.parseInt(info.get(i+6)),
					Integer.parseInt(info.get(i+7)),
					Integer.parseInt(info.get(i+8))));
		}

		Jornada jornada = new Jornada(anyo, partidos, clasificacion);
		
		// Guardar clasificaciones en formato AaaaaJjj
		String s_jorn = String.valueOf(jorn);
		if (jorn<10) s_jorn = "0" + s_jorn;
		
		ArrayList<Clasificacion> clasPrimera = new ArrayList<Clasificacion>();
		ArrayList<Clasificacion> clasSegunda = new ArrayList<Clasificacion>();
		
		if (categoria == "primera"){
			for (Clasificacion c: clasificacion){
				clasPrimera.add(c);
			}
			clasPorJornPrim.put("A"+anyo+"J"+s_jorn, clasPrimera);
		}else{
			for (Clasificacion c: clasificacion){
				clasSegunda.add(c);
			}
			clasPorJornSeg.put("A"+anyo+"J"+s_jorn, clasSegunda);
		}
		
		jornadas.addAll(jornada.toFile());
		
	}

	public void leerConfiguracion(){
		try{
		    FileReader fstream = new FileReader(rutaDatos+File.separatorChar+"config.txt");
			BufferedReader out = new BufferedReader(fstream);
		    
			anyoInicio = Integer.parseInt(out.readLine());
		    inicioJornPrim = Integer.parseInt(out.readLine());
		    inicioJornSeg = Integer.parseInt(out.readLine());
		  
		    out.close();
		} catch (IOException e){
			//TODO Comprobar que hay un fichero llamado datos.txt
			System.err.println("No existe el fichero 'config.txt'");
		}
  }

	public void actualizarConfiguracion(){
		
		try {
			Document doc = Jsoup.connect("http://www.marca.com/estadisticas/futbol/primera/clasificacion.html").get();
			Elements tabla = doc.getElementsByTag("td");
			String[] title = doc.getElementsByTag("title").text().split(" ");
			ultimaTemporada = Integer.parseInt(title[3].split("-")[0]);			
			ultimaJornadaPrimera = Integer.parseInt(tabla.get(2).text());
		} catch (IOException e) {
			System.err.println("No se puede acceder a la pagina principal de Primera division");
		}
    
		try {
			Document doc = Jsoup.connect("http://www.marca.com/estadisticas/futbol/segunda/clasificacion.html").get();
			Elements tabla = doc.getElementsByTag("td");		
			ultimaJornadaSegunda = Integer.parseInt(tabla.get(2).text());

		} catch (IOException e) {
			System.err.println("No se puede acceder a la pagina principal de Segunda division");
		}

  }
	
	public void cargarFicheros() throws IOException, ClassNotFoundException{
		FileReader fstream;
		BufferedReader in;
		ObjectInputStream inputStream;
		inputStream = new ObjectInputStream(new FileInputStream(rutaDatos+File.separatorChar+"bd.qui"));
		clasPorJornPrim = (HashMap<String, ArrayList<Clasificacion>>) inputStream.readObject();
		clasPorJornSeg =  (HashMap<String, ArrayList<Clasificacion>>) inputStream.readObject();
		jornadas = (ArrayList<String>) inputStream.readObject();
		partidosPorJugar = (ArrayList<String>) inputStream.readObject();
		paginasError = (ArrayList<String>) inputStream.readObject();
		
		inputStream.close();
	}
	
	public void guardarFicheros() throws Exception{
		
		FileWriter fstream;
		BufferedWriter out;
		
		FileOutputStream fOutStream = new FileOutputStream(rutaDatos+File.separatorChar+"bd.qui");
		ObjectOutputStream outStream = new ObjectOutputStream(fOutStream);
	
		//Guardar 'config.txt'
		fstream = new FileWriter(rutaDatos+File.separatorChar+"config.txt");
		out = new BufferedWriter(fstream);
		out.write(ultimaTemporada+"\n");
		out.write(ultimaJornadaPrimera+"\n");
		out.write(ultimaJornadaSegunda+"\n");
		out.close();
		
		//Guardar clasificaciones por jornada de Primera
		//	Serializar clasPorJornPrim
		outStream.writeObject(clasPorJornPrim);		
		
		//Guardar clasificaciones por jornada de Segunda
		//	Serializar clasPorJornSeg
		outStream.writeObject(clasPorJornSeg);
		
		//
		//	Generar el fichero de 'infoMarca.txt'
		fstream = new FileWriter(rutaDatos+File.separatorChar+"infoMarca.txt");
		out = new BufferedWriter(fstream);
		for (String s: jornadas){
			out.write(s+"\n");
		}
		out.close();
		//	Serializar jornadas
		outStream.writeObject(jornadas);
		
		//
		//	Serializar partidosPorJugar
		outStream.writeObject(partidosPorJugar);
		
		//
		//	Serializar paginasError
		outStream.writeObject(paginasError);
		
		outStream.close();
	}


	public HashMap<String, ArrayList<Clasificacion>> getClasPorJornPrim() {
		return clasPorJornPrim;
	}

	public HashMap<String, ArrayList<Clasificacion>> getClasPorJornSeg() {
		return clasPorJornSeg;
	}


	public int getAnyoInicio() {
		return anyoInicio;
	}	
}
