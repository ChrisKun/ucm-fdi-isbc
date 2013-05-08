package utilidades;

import java.io.File;
import java.util.ArrayList;

public class Ficheros {

	public ArrayList<String> ficheros(String path){
		ArrayList<String> lista = new ArrayList<String>();
		ficherosRecursivo(lista, path);
		return lista;
	}
	
	private void ficherosRecursivo(ArrayList<String> lista, String path) {

        
		File root = new File(path);
        File[] list = root.listFiles();

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                ficherosRecursivo(lista, f.getAbsolutePath());
                System.out.println("Dir:" + f.getAbsoluteFile());
            }
            else {
                System.out.println("File:" + f.getAbsoluteFile());
                lista.add(f.getAbsolutePath());
            }
        }
    }
	
}
