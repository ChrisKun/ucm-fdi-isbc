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
	private String contrase�a;
	// Lista con las ID's de los productos comprados
	private ArrayList<Integer> productosComprados;
	// Seguridad usada en la encriptacion
	private final String SEGURIDAD = "MD5";
	// Directorio de la carpeta usuarios
	public static final String DIR = "Usuarios"+File.separatorChar;
	
	public Usuario (String nombre, String contrase�a) {
		this.nombre = nombre;
		this.contrase�a = StringMD.getStringMessageDigest(contrase�a, SEGURIDAD);
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
	 * A�ade un ID de producto nuevo
	 * @param producto
	 */
	public void a�adeProductoComprado(Integer producto) {
		if (!productosComprados.contains(producto))
			productosComprados.add(producto);
	}
	
	/**
	 * Carga los datos del usuario en caso de estar almacenados
	 * @return
	 * @throws Exception
	 */
	public void cargaUsuario() throws Exception {
		  File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;
	      try {
	         archivo = new File(DIR+nombre+File.separatorChar+"Datos.txt");
	         if (!archivo.exists()) {	     		
		     	throw new Exception("No existe el fichero de datos");
	         } else {
		         fr = new FileReader(archivo);
		         br = new BufferedReader(fr);	
		         if ((br.readLine()).equals(contrase�a)) {
		        	cargaProductos(); 
		         } else {
		        	 throw new Exception("No concuerda la contrase�a con la almacenada");
		         }	       
	         } 
	      }
	      catch(Exception e){
	        throw e;
	      }finally{
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
	     try {
	         archivo = new File(DIR+nombre+File.separatorChar+"Compras.txt");
	         if (!archivo.exists()) {	     
	        	 throw new Exception("No existe el fichero de compras");
	         } else {
	             fr = new FileReader (archivo);
	             br = new BufferedReader(fr);
	        	 while((linea = br.readLine()) != null) {
	        		 productosComprados.add(Integer.valueOf(linea));
	        	 }
	         }		        
	     } catch (Exception e) {
	    	 throw e;
	     } finally {
	    	 fr.close();
	     }
	}
	
	/**
	 * Guarda la informaci�n del usuario
	 * @throws Exception
	 */
	public void guardaUsuario() throws Exception{
		// Creamos el directorio en caso de no existir
		File directorio = new File(DIR);
		directorio.mkdirs();
		String direccion;
		try {
			// Guardamos la informacion personal
			direccion = DIR+File.separatorChar+this.getNombre()+File.separatorChar+"Datos.txt";
			guardaDatos(direccion);
			// Guardamos las compras
			direccion = DIR+this.getNombre()+File.separatorChar+"Compras.txt";
			guardaProductos(direccion, this.productosComprados);			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
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
            pw.println(this.contrase�a);            
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
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              throw new Exception("Error al cerrar el fichero de productos");
           }
        }
    }
	
	public static void main(String args[]) {		
		try{
		
			//Usuario usuario = UsuarioFunciones.cargaUsuario("Pedro","112");	
			Usuario usuario = UsuarioFunciones.creaUsuario("Pedro", "112");
			ArrayList<Product> productos = GAPLoader.extractProducts();
			int i = 0;
			for(Product p: productos) {
				if (i<9) usuario.a�adeProductoComprado(p.getId());
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
			usuario.guardaUsuario();
		} catch (Exception e) {
				e.printStackTrace();
		} finally {
			GAPLoader.shutDownDataBase();
		}
		
	}
}
