package proyecto2.quinielas;

import proyecto2.quinielas.representacion.DescripcionQuinielas;
import proyecto2.quinielas.representacion.SolucionQuinielas;
import proyecto2.quinielas.representacion.SolucionQuinielas.UnoXDos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseBaseFilter;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.InitializingException;

/**
 * Carga los casos desde el fichero obtenido en http://www.terra.es/personal3/olbapordep/
 * @author juanan
 *
 */
public class ConnectorQuinielas implements Connector {


	
	@Override
	public void initFromXMLfile(URL file) throws InitializingException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeCases(Collection<CBRCase> cases) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCases(Collection<CBRCase> cases) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<CBRCase> retrieveAllCases() {
	
		ArrayList<CBRCase> list = new ArrayList<CBRCase>();
		int i=0;
		
		try {
			//TODO Cambiar la ruta por una donde tengamos localizados archivos media
			BufferedReader br = new BufferedReader(new FileReader("src/proyecto2/quinielas/datos.txt"));
			
			
			String line = null;
			while((line=br.readLine())!=null){
				
				if(line.contains("Promo")||line.contains("Descenso"))
					continue;
				
				String[] tokens = line.split(" {2,}");
				
				DescripcionQuinielas desc = new DescripcionQuinielas();
				desc.setId(i);
				desc.setTemporada(tokens[0]);
				desc.setLiga(tokens[1]);
				desc.setJornada(Integer.valueOf(tokens[2]));
				desc.setLocal(tokens[3]);
				desc.setVisitante(tokens[4]);
				String[] goles = tokens[5].split("-");
				desc.setGolesLocal(Integer.valueOf(goles[0]));
				desc.setGolesVisitante(Integer.valueOf(goles[1]));
				
				int diff = desc.getGolesLocal()-desc.getGolesVisitante(); 
				
				SolucionQuinielas sol = new SolucionQuinielas();
				sol.setId(i);
				
				if(diff>0)
					sol.setSolucion(UnoXDos.UNO);
				else if(diff==0)
					sol.setSolucion(UnoXDos.X);
				else
					sol.setSolucion(UnoXDos.DOS);
				
				CBRCase _case = new CBRCase();
				_case.setDescription(desc);
				_case.setSolution(sol);
				
				
				list.add(_case);
				
				i++;
				
			}
			br.close();
		} catch (Exception e) {
			System.err.println("Error caso: "+i);
			e.printStackTrace();
		}
		System.out.println("Conector. Casos cargados: "+i);
		return list;
	}

	@Override
	public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args)
	{
		ConnectorQuinielas cq = new ConnectorQuinielas();
		cq.retrieveAllCases();
	}

}
