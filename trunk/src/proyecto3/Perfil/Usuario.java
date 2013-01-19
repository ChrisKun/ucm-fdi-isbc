package Perfil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import Cbr.Recomendador;
import Encriptacion.StringMD;
import GAPDataBase.GAPLoader;
import GAPDataBase.Product;

/**
 * Clase que almacena la informacion de cada usuario
 * @author Alvaro
 *
 */
public class Usuario {
	
	private String nombre;
	private String contraseña;
	// Lista con las ID's de los productos comprados
	private ArrayList<Integer> productosComprados;
	// Seguridad usada en la encriptacion
	private final String SEGURIDAD = "MD5";
	// Directorio de la carpeta usuarios
	public static String DIR = "Usuarios";
	
	private Usuario (String nombre, String contraseña) {
		this.nombre = nombre;
		this.contraseña = StringMD.getStringMessageDigest(contraseña, SEGURIDAD);
		productosComprados = new ArrayList<Integer>();
	}
	
	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve los ID's de los productos del usuario
	 * @return
	 */
	public ArrayList<Integer> getProductosComprados() {
		return productosComprados;
	}

	/**
	 * Añade un ID de producto nuevo y guarda la informacion del usuario
	 * @param producto
	 */
	public void añadeProductoComprado(Integer producto) {
		if (!productosComprados.contains(producto)) {
			productosComprados.add(producto);
			try {
				// Guardamos el usuario para no preocuparnos de poder perder la informacion
				this.guardaUsuario();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Elimina el producto de la lista de productos comprados (siempre y cuando exista)
	 * @param producto
	 */
	public void eliminaProductoComprado(Integer producto) {
		if (productosComprados.contains(producto)) {
			int pos = 0;
			for (Integer i: productosComprados) {
				if (i.equals(producto)) {
					productosComprados.remove(pos);
					break;
				} else {
					pos++;
				}
			}
		}
	}
	
	/**
	 * Crea un usuario y su directorio
	 * @param nombre
	 * @param contraseña
	 * @return
	 * @throws Exception
	 */
	public static Usuario creaUsuario(String nombre, String contraseña) throws Exception {
		File directorio = new File(DIR+File.separatorChar+nombre);
		if (directorio.exists())
			throw new Exception("El nombre de usuario ya existe");
		else 
			directorio.mkdirs();
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
		usuario.cargaUsuario();
		return usuario;
	}	
		
	/**
	 * Carga los datos del usuario en caso de estar almacenados
	 * @return
	 * @throws Exception
	 */
	private void cargaUsuario() throws Exception {
		 File archivo = null;
	     FileReader fr = null;
	     BufferedReader br = null;
	     archivo = new File(DIR+File.separatorChar+nombre+File.separatorChar+"Datos.txt");
         if (!archivo.exists()) {        	
	     	throw new Exception("No existe el fichero de datos");
         } else {
	        fr = new FileReader(archivo);
	        br = new BufferedReader(fr);	
	        if ((br.readLine()).equals(contraseña)) {
	        	cargaProductos(); 
	        } else {
	        	fr.close(); 
	        	throw new Exception("No concuerda la contraseña con la almacenada");
	        }
        	fr.close(); 
         }    
	}
	
	/**
	 * Carga los productos del usuario en caso de estar almacenados
	 * @throws Exception
	 */
	private void cargaProductos() throws Exception {
		 File archivo = null;
	     FileReader fr = null;
	     BufferedReader br = null;
	     String linea;
	     
         archivo = new File(DIR+File.separatorChar+nombre+File.separatorChar+"Compras.txt");
         if (!archivo.exists()) {	     
        	 throw new Exception("No existe el fichero de compras");
         } else {
	             fr = new FileReader (archivo);
	             br = new BufferedReader(fr);
	        	 while((linea = br.readLine()) != null) {
	        		 productosComprados.add(Integer.valueOf(linea));
	        	 }
        		 fr.close();
        	 }  
	}	

	/**
	 * Guarda la información del usuario
	 * @throws Exception
	 */
	public void guardaUsuario() throws Exception{
		File directorio = new File(DIR+File.separatorChar+this.getNombre());
		String direccion;
		// Guardamos la informacion personal
		direccion = directorio.getPath()+File.separatorChar+"Datos.txt";
		guardaDatos(direccion);
		// Guardamos las compras
		direccion = directorio.getPath()+File.separatorChar+"Compras.txt";
		guardaProductos(direccion, this.productosComprados);			
	}
	
	/**
	 * Guarda los datos personales del usuario
	 * @param direccion
	 * @throws Exception
	 */
	private void guardaDatos(String direccion) throws Exception {
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(direccion);
            pw = new PrintWriter(fichero);            
            pw.println(this.contraseña);            
        } catch (Exception e) {
           throw new Exception("Error al guardar los datos personales");
        } finally {
              fichero.close();
        }
	}

	/**
	 * Guarda los productos comprados del usuario
	 * @param direccion
	 * @param productos
	 * @throws Exception
	 */
	private void guardaProductos(String direccion, ArrayList<Integer> productos) throws Exception {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(direccion);
            pw = new PrintWriter(fichero);
            for (Integer p: productos) {
            	pw.println(p);
            }
        } catch (Exception e) {
           throw new Exception("Error al guardar los productos");
        } finally {
              fichero.close();
        }
    }
	
	public static void main(String args[]) {		
		try{
			GAPLoader.initDataBase();
			Usuario usuario = Usuario.cargaUsuario("Pedro","112");	
			//Usuario usuari1o = Usuario.cargaUsuario("Pedaro","112");	
			//Usuario usuario2 = Usuario.creaUsuario("Pedro", "112");/*
			usuario.eliminaProductoComprado(317265);
			ArrayList<Product> productos = GAPLoader.extractProducts();
			int i = 0;
			for(Product p: productos) {
				if (i<9) usuario.añadeProductoComprado(p.getId());
				else break;
				i++;
			}
			//usuario.guardaUsuario();
			ArrayList<Integer> lista;
			Recomendador recomendador = new Recomendador();
			lista = recomendador.recomendadosPorUsuario(usuario);
			lista = recomendador.recomendadosPorMasComprados();
			lista = recomendador.recomendadosPorProducto(245996);
			recomendador.postCycle();			
		} catch (Exception e) {
			e.getLocalizedMessage();
			e.printStackTrace();
		} finally {
			GAPLoader.shutDownDataBase();
		}
		
	}
}
