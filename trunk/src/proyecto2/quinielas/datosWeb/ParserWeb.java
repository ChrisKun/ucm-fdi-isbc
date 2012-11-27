package proyecto2.quinielas.datosWeb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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
	private static FileWriter fstream_conf_out;
	private static FileReader fstream_conf_in;
	private static FileWriter fstream_clasP;
	private static FileWriter fstream_clasS;
	private static BufferedWriter out;
	private static BufferedWriter out_err;
	private static BufferedWriter out_esp;
	private static BufferedWriter out_conf_out;
	private static BufferedReader out_conf_in;
	private static BufferedWriter out_clasP;
	private static BufferedWriter out_clasS;
	
	public static void main(String[] args) throws IOException{
		
		File carpeta = new File(".\\src\\proyecto2\\quinielas\\datos");
		if (!carpeta.exists()) {
			carpeta.mkdir();
		}
		
		fstream = 		new FileWriter(".\\src\\proyecto2\\quinielas\\datos\\infoMarca.txt",true);
		fstream_err = 	new FileWriter(".\\src\\proyecto2\\quinielas\\datos\\errores.txt");
		fstream_esp = 	new FileWriter(".\\src\\proyecto2\\quinielas\\datos\\porJugar.txt");
		fstream_clasP =	new FileWriter(".\\src\\proyecto2\\quinielas\\datos\\clasPrimera.txt",true);
		fstream_clasS =	new FileWriter(".\\src\\proyecto2\\quinielas\\datos\\clasSegunda.txt",true);
		
		out = 			new BufferedWriter(fstream);
		out_err = 		new BufferedWriter(fstream_err);
		out_esp = 		new BufferedWriter(fstream_esp);
		out_clasP = 	new BufferedWriter(fstream_clasP);
		out_clasS =		new BufferedWriter(fstream_clasS);
		
		int anyoIni = 2000;
		//TODO añadir el año en el que estamos
		int anyoFin = 2012;
		int ultJornPrim=1, ultJornSeg=1;
		int maxJornPrim=1, maxJornSeg=1;
		
		//TODO Comprobar que hay un fichero llamado datos.txt
		
		
		try{
			fstream_conf_in  = new FileReader(".\\src\\proyecto2\\quinielas\\datos\\config.txt");
			out_conf_in =	new BufferedReader(fstream_conf_in);
			
			anyoIni = Integer.parseInt(out_conf_in.readLine());
			ultJornPrim = Integer.parseInt(out_conf_in.readLine());
			ultJornSeg = Integer.parseInt(out_conf_in.readLine());
		
			out_conf_in.close();
		} catch (IOException e){
			
		}
		
		fstream_conf_out = new FileWriter(".\\src\\proyecto2\\quinielas\\datos\\config.txt");
		out_conf_out =	new BufferedWriter(fstream_conf_out);
		
		
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

		for (int i=anyoIni;i<anyoFin;i++){
			for (int j=1;j<=38;j++){
				anyadirJornada("primera",i,j);
			}
			for (int j=1;j<=42;j++){
				anyadirJornada("segunda",i,j);
			}
		}
		for (int j=ultJornPrim;j<=maxJornPrim;j++){
			anyadirJornada("primera",anyoFin,j);
		}
		for (int j=ultJornSeg;j<=maxJornSeg;j++){
			anyadirJornada("segunda",anyoFin,j);
		}

		maxJornPrim++; maxJornSeg++;
		out_conf_out.write(anyoFin+"\n");
		out_conf_out.write(maxJornPrim+"\n");
		out_conf_out.write(maxJornSeg+"\n");
		
		out.close();
		out_err.close();
		out_esp.close();
		out_conf_out.close();
		out_clasP.close();
		out_clasS.close();
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
		
		// Guardar clasificaciones en formato AaaaaJjj
		String s_jorn;
		if (jorn<10) 
			s_jorn = "0" + String.valueOf(jorn);
		else
			s_jorn = String.valueOf(jorn);
		if (categoria == "primera"){
			out_clasP.write("A"+anyo+"J"+s_jorn+"\n");
			for (Clasificacion c: clasificacion){
				out_clasP.write(c.toFile()+"\n");
			}
		}else{
			out_clasS.write("A"+anyo+"J"+s_jorn+"\n");
			for (Clasificacion c: clasificacion){
				out_clasS.write(c.toFile()+"\n");
			}
		}
		
		// Procesar jornada
		ArrayList<String> resultados = jornada.toFile();
		if (resultados.isEmpty()) return;
		for (String s: resultados){
			out.write(s+"\n");
		}
	}

	public void inicializarDatos(){
		
	}
	
	public void inicializarFicheros(){
		
		
	}

}
