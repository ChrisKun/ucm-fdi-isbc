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
	 * @param contraseña
	 * @return
	 */
	public static Usuario creaUsuario(String nombre, String contraseña) {
		return new Usuario(nombre,contraseña);
	}
	
	/**
	 * Carga un usuario
	 * @param nombre
	 * @param contraseña
	 * @return
	 * @throws Exception
	 */
	public static Usuario cargaUsuario(String nombre, String contraseña) throws Exception {
		Usuario usuario = new Usuario(nombre,contraseña);
		try {
			usuario.cargaUsuario();
		} catch (Exception e) {
			throw e;
		}
		return usuario;
	}
}
