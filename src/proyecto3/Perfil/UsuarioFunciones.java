package Perfil;

/**
 * Clase que crea o carga un usuario
 * @author Alvaro
 *
 */
public class UsuarioFunciones {
	
	/**
	 * Crea un usuario
	 * @param nombre
	 * @param contraseņa
	 * @return
	 */
	public static Usuario creaUsuario(String nombre, String contraseņa) {
		return new Usuario(nombre,contraseņa);
	}
	
	/**
	 * Carga un usuario
	 * @param nombre
	 * @param contraseņa
	 * @return
	 * @throws Exception
	 */
	public static Usuario cargaUsuario(String nombre, String contraseņa) throws Exception {
		Usuario usuario = new Usuario(nombre,contraseņa);
		try {
			usuario.cargaUsuario();
		} catch (Exception e) {
			throw e;
		}
		return usuario;
	}
}
