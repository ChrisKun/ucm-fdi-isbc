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

	public static void main(String[] args){
		
		// Create file 
		FileWriter fstream;
		try {
			fstream = new FileWriter("infoMarca.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			//infoJornada("primera",2011,11);
			for (int i=2000;i<=2012;i++){
				for (int j=1;j<=42;j++){
					//System.out.print(infoJornada("primera",i,j));
					out.write(infoJornada("primera",i,j));
				}
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	
	public static String infoJornada(String categoria, int anyo, int jorn){
		
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
			System.out.println("Pero que le pasa a "+dir+"?");
			return dir + "\n";
		}
		ArrayList<Partido> partidos = new ArrayList<Partido>();
		String[] res; int res1, res2;
		for (int i=0;i<=numPartidos*4-1;i=i+4){
			//Procesar info de UN partido
			try{
				res = info.get(i+1).split("-");
				res1 = Integer.parseInt(res[0]);
				res2 = Integer.parseInt(res[1]);
			} catch (Exception e){
				return info.get(i+1) + "\n";
			}
			partidos.add(new Partido(info.get(i), info.get(i+2), res1, res2));
		}
																							
		ArrayList<Clasificacion> clasificacion = new ArrayList<Clasificacion>();
		for (int i=numPartidos*4;i<=numEquipos*9+40-1;i=i+9){
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
		return jornada.toFile();
	}
}
