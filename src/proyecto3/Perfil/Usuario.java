package Perfil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import Cbr.RecomendadorPerfil;
import GAPDataBase.GAPLoader;
import GAPDataBase.Product;

public class Usuario {
	String nombre;
	String contraseña;
	ArrayList<Product> productosComprados;
	
	public String getNombre() {
		return nombre;
	}

	public ArrayList<Product> getProductosComprados() {
		return productosComprados;
	}

	public void añadeProductoComprado(Product producto) {
		if (!productosComprados.contains(producto))
			productosComprados.add(producto);
	}
	
	public Usuario(String nombre, String contraseña) throws Exception {
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.productosComprados = new ArrayList<Product>();
	}
	
	private void cargaUsuario(String nombre, String contraseña) throws Exception {
		  File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;

	      try {
	         archivo = new File("Usuarios"+File.separatorChar+nombre+File.separatorChar+"Datos.txt");
	         if (!archivo.exists()) {	     		
		     	throw new Exception("No existe el fichero de datos");
	         } else {
		         fr = new FileReader(archivo);
		         br = new BufferedReader(fr);	
		         if ((br.readLine()).equals(contraseña)) {
		        	cargaProductos(); 
		         } else {
		        	 throw new Exception("No concuerda la contraseña con la almacenada");
		         }	       
	         } 
	      }
	      catch(Exception e){
	        throw e;
	      }finally{
               fr.close(); 
	      }
	}
	
	private void cargaProductos() throws Exception {
		 File archivo = null;
	     FileReader fr = null;
	     BufferedReader br = null;
	     String linea;
	     try {
	         archivo = new File("Usuarios"+File.separatorChar+nombre+File.separatorChar+"Compras.txt");
	         if (!archivo.exists()) {	     
	        	 throw new Exception("No existe el fichero de compras");
	         } else {
	             fr = new FileReader (archivo);
	             br = new BufferedReader(fr);
	        	 while((linea = br.readLine()) != null) {
	        		 Product producto = new Product();
	        		 producto.setId(Integer.valueOf(linea));
	        		 productosComprados.add(producto);
	        	 }
	             fr.close();  
	         }	
	         archivo = new File("Usuarios"+File.separatorChar+nombre+File.separatorChar+"Favoritos.txt");
	         if (!archivo.exists()) {	     
	        	 throw new Exception("No existe el fichero de compras");
	         } else {
	             fr = new FileReader (archivo);
	             br = new BufferedReader(fr);
	        	 while((linea = br.readLine()) != null) {
	        		 Product producto = new Product();
	        		 producto.setId(Integer.valueOf(linea));
	        		 productosComprados.add(producto);
	        	 }
	         }	
	     } catch (Exception e) {
	    	 throw e;
	     } finally {
	    	 fr.close();
	     }
	}
	
	/**
	 * Guarda la información del usuario
	 * @throws Exception
	 */
	public void guardaUsuario() throws Exception{
		// Creamos el directorio en caso de no existir
		File directorio = new File("Usuarios"+File.separatorChar+nombre);
		directorio.mkdirs();
		String direccion;
		try {
			// Guardamos la informacion personal
			direccion = directorio.getPath()+File.separatorChar+"Datos.txt";
			guardaDatos(direccion);
			// Guardamos las compras
			direccion = directorio.getPath()+File.separatorChar+"Compras.txt";
			guardaProductos(direccion, this.productosComprados);			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
		
	private void guardaDatos(String direccion) throws Exception {
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(direccion);
            pw = new PrintWriter(fichero);            
            
            pw.println(contraseña);
            
        } catch (Exception e) {
           throw new Exception("Error al guardar los datos personales");
        } finally {
              fichero.close();
        }

	}

	private void guardaProductos(String direccion, ArrayList<Product> productos) throws Exception {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(direccion);
            pw = new PrintWriter(fichero);
            for (Product p: productos) {
            	pw.println(p.getId());
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
			Usuario usuario = new Usuario("Pedro Gomez Serrano","holaquease123");
			int i = 0;
			ArrayList<Product> productos = GAPLoader.extractProducts();
			for (Product p: productos) {
				if (i>6) usuario.añadeProductoComprado(p);
				if (i>20)break;
				i++;
			}
			RecomendadorPerfil recomendador = new RecomendadorPerfil();
			recomendador.configure();
			recomendador.preCycle();
			recomendador.recomendados(usuario);
			recomendador.postCycle();
			usuario.guardaUsuario();
		} catch (Exception e) {
				e.printStackTrace();
		}
		
	}
}
