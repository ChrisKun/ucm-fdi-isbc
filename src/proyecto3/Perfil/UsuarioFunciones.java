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
	 * @param contrase�a
	 * @return
	 */
	public static Usuario creaUsuario(String nombre, String contrase�a) {
		return new Usuario(nombre,contrase�a);
	}
	
	/**
	 * Carga un usuario
	 * @param nombre
	 * @param contrase�a
	 * @return
	 * @throws Exception
	 */
	public static Usuario cargaUsuario(String nombre, String contrase�a) throws Exception {
		Usuario usuario = new Usuario(nombre,contrase�a);
		try {
			usuario.cargaUsuario();
		} catch (Exception e) {
			throw e;
		}
		return usuario;
	}
}
