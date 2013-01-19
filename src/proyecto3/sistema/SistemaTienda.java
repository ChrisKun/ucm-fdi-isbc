package sistema;

import Cbr.Recomendador;
import Perfil.Usuario;

public class SistemaTienda {

	public static Recomendador recomendador;
	public static Usuario usuarioActual;
	
	public SistemaTienda(){
		try {
			recomendador = new Recomendador();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("El recomendador no se ha inicializado.");
		}
	}
	
	public static void init(){
		try {
			recomendador = new Recomendador();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("El recomendador no se ha inicializado.");
		}
	}
}
