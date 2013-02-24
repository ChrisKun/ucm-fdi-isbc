package sistema;

import java.util.ArrayList;

import Cbr.Recomendador;
import Perfil.Usuario;

public class SistemaTienda {

	public static Recomendador recomendador;
	public static Usuario usuarioActual;
	public static ArrayList<Integer> productosCesta;
	
	public SistemaTienda(){
		try {
			recomendador = new Recomendador();
			productosCesta = new ArrayList<Integer>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("El recomendador no se ha inicializado.");
		}
	}
}
