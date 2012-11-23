package proyecto2.quinielas.datosWeb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ParserWeb {

	private static FileWriter fstream;
	private static FileWriter fstream_err;
	private static FileWriter fstream_esp;
	private static FileWriter fstream_conf;
	private static BufferedWriter out;
	private static BufferedWriter out_err;
	private static BufferedWriter out_esp;
	private static BufferedWriter out_conf;

	public static void main(String[] args) throws IOException{

		fstream = 		new FileWriter("infoMarca.txt");
		fstream_err = 	new FileWriter("errores.txt");
		fstream_esp = 	new FileWriter("porJugar.txt");
		fstream_conf = 	new FileWriter("config.txt");
		out = 			new BufferedWriter(fstream);
		out_err = 		new BufferedWriter(fstream_err);
		out_esp = 		new BufferedWriter(fstream_esp);
		out_conf = 		new BufferedWriter(fstream_conf);

		int anyoIni = 2000;
		int anyoFin = 2012;
		int maxJornPrim=0, maxJornSeg=0;

		//http://www.lne.es/deportes/futbol/quiniela/resultados-quiniela-jornada-20.html
		Document doc1 = Jsoup.connect("http://www.lne.es/deportes/futbol/quiniela/resultados-quiniela-jornada-20.html").get();
		Elements tabla1 = doc1.getElementsByTag("td");
		for(Element e: tabla1){
			System.out.println(e.text());
		}
		System.in.read();
		
		try {
			Document doc = Jsoup.connect("http://www.marca.com/estadisticas/futbol/primera/clasificacion.html").get();
			Elements tabla = doc.getElementsByTag("td");
			String[] title = doc.getElementsByTag("title").text().split(" ");
			anyoFin = Integer.parseInt(title[3].split("-")[0]);			
			maxJornPrim = Integer.parseInt(tabla.get(2).text());

		} catch (IOException e) {
			System.err.println("No se puede acceder a la pagina principal de Primera division");
		}

		try {
			Document doc = Jsoup.connect("http://www.marca.com/estadisticas/futbol/segunda/clasificacion.html").get();
			Elements tabla = doc.getElementsByTag("td");		
			maxJornSeg = Integer.parseInt(tabla.get(2).text());

		} catch (IOException e) {
			System.err.println("No se puede acceder a la pagina principal de Segunda division");
		}

		out_conf.write(anyoFin+"\n");
		out_conf.write(maxJornPrim+"\n");
		out_conf.write(maxJornSeg+"\n");

		for (int i=anyoIni;i<anyoFin;i++){
			for (int j=1;j<=38;j++){
				anyadirJornada("primera",i,j);
			}
			for (int j=1;j<=42;j++){
				anyadirJornada("segunda",i,j);
			}
		}
		for (int j=1;j<=maxJornPrim;j++){
			anyadirJornada("primera",anyoFin,j);
		}
		for (int j=1;j<=maxJornSeg;j++){
			anyadirJornada("segunda",anyoFin,j);
		}

		out.close();
		out_err.close();
		out_esp.close();
		out_conf.close();

		/**
		 * Ficheros
		 * 	- Base de casos
		 *  - Clasificacion equipos segun año y jornada (1ª y 2ª)
		 *  ----- AaaaaJjj
		 *  --------- A2012J01
		 *  - Fichero de estado:
		 *  ----- Jornada
		 *  ----- Anyo
		 */
	}


	public static void anyadirJornada(String categoria, int anyo, int jorn) throws IOException{

		int numEquipos  = categoria.equals("primera")? 20 : 22;

		//Paginas Web mal diseñadas
		if (anyo == 2003 && jorn == 1) return;
		if (anyo == 2011 && jorn == 7) return;
		if (anyo == 2011 && jorn == 33) return;

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
			out_err.write(dir+"\n");
			return;
		}
		ArrayList<Partido> partidos = new ArrayList<Partido>();
		String[] res; int res1 = 0, res2 = 0;
		//TODO cambiar esta guarreria!!!
		int n = 4;
		for (int i=0;i<=numPartidos*n-1;i=i+n){
			//Procesar info de UN partido
			try{
				res = info.get(i+1).split("-");
				res1 = Integer.parseInt(res[0]);
				res2 = Integer.parseInt(res[1]);
			} catch (Exception e){
				out_esp.write(info.get(i+1) +","+info.get(i)+","+info.get(i+2)+ "\n");
				n = 3;
			}
			partidos.add(new Partido(info.get(i), info.get(i+2), res1, res2));
		}

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
		// Procesar jornada
		ArrayList<String> resultados = jornada.toFile();
		if (resultados.isEmpty()) return;
		for (String s: resultados){
			out.write(s+"\n");
		}
	}
}
