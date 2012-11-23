package proyecto2.quinielas.cbr;

import proyecto2.quinielas.representacion.DescripcionQuinielas;
import proyecto2.quinielas.representacion.SolucionQuinielas;
import proyecto2.quinielas.representacion.SolucionQuinielas.UnoXDos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

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
			BufferedReader br = new BufferedReader(new FileReader(".\\src\\proyecto2\\quinielas\\datos\\infoMarca.txt"));
						
			String line = null;
			while((line=br.readLine())!=null){
				
				String[] tokens = line.split(",");
				// Medida de seguridad frente a posibles errores en el archivo de texto
			    if (tokens.length < 20) {
			    	System.err.println("Falta informacion en: " + line);
			    	continue;
			    }
			    
				
				DescripcionQuinielas desc = new DescripcionQuinielas();
				
				desc.setId(String.valueOf(i));
				desc.setTemporada(Integer.valueOf(tokens[0]));
				desc.setLocal(tokens[1]);
				desc.setVisitante(tokens[2]);
				desc.setPosLocal(Integer.valueOf(tokens[6]));
				desc.setPgLocal(Integer.valueOf(tokens[7]));
				desc.setPeLocal(Integer.valueOf(tokens[8]));
				desc.setPpLocal(Integer.valueOf(tokens[9]));
				desc.setGfavorLocal(Integer.valueOf(tokens[10]));
				desc.setGcontraLocal(Integer.valueOf(tokens[11]));
				desc.setPuntosLocal(Integer.valueOf(tokens[12]));
				desc.setPosVisitante(Integer.valueOf(tokens[14]));
				desc.setPgVisitante(Integer.valueOf(tokens[15]));
				desc.setPeVisitante(Integer.valueOf(tokens[16]));
				desc.setPpVisitante(Integer.valueOf(tokens[17]));
				desc.setGfavorVisitante(Integer.valueOf(tokens[18]));
				desc.setGcontraVisitante(Integer.valueOf(tokens[19]));
				desc.setPuntosVisitante(Integer.valueOf(tokens[20]));
					
				SolucionQuinielas sol = new SolucionQuinielas();
				
				sol.setId(String.valueOf(i));
				
				if(tokens[3].equals("1"))
					sol.setSolucion(UnoXDos.UNO);
				else if(tokens[3].equals("2"))
					sol.setSolucion(UnoXDos.DOS);
				else
					sol.setSolucion(UnoXDos.X);

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
