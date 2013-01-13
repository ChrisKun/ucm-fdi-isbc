package Perfil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import GAPDataBase.GAPLoader;
import GAPDataBase.Product;

public class Usuario {
	String nombre;
	String contraseña;
	ArrayList<Product> productosComprados;
	ArrayList<Product> productosFavoritos;
	
	
	public String getNombre() {
		return nombre;
	}

	public ArrayList<Product> getProductosComprados() {
		return productosComprados;
	}

	public ArrayList<Product> getProductosFavoritos() {
		return productosFavoritos;
	}

	public Usuario(String nombre, String contraseña) {
		this.nombre = nombre;
		this.contraseña = contraseña;
		productosComprados = new ArrayList<Product>();
		productosFavoritos = new ArrayList<Product>();
	}
	
	public void añadeProductoComprado(Product producto) {
		productosComprados.add(producto);
	}
	
	public void añadeProductoFavorito(Product producto) {
		productosFavoritos.add(producto);
	}
	
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
			// Guardamos los favoritos
			direccion = directorio.getPath()+File.separatorChar+"Favoritos.txt";
			guardaProductos(direccion, this.productosFavoritos);
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
            
            pw.println(nombre);
            pw.println(contraseña);
            
        } catch (Exception e) {
           throw new Exception("Error al guardar los datos personales");
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              throw new Exception("Error al cerrar el fichero de datos personales");
           }
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
	
	public void cargaUsuario() {
		
	}
	
	public static void main(String args[]) {
		Usuario usuario = new Usuario("Pedro Gomez Serrano","holaquease123");
		try{
			int i = 0;
			ArrayList<Product> productos = GAPLoader.extractProducts();
			for (Product p: productos) {
				if (i<6) usuario.añadeProductoComprado(p);
				else usuario.añadeProductoFavorito(p);
				if (i>13) break;
				i++;
			}
			usuario.guardaUsuario();
		} catch (Exception e) {
				e.printStackTrace();
		}
		
	}
}
