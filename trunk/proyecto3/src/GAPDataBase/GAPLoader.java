package GAPDataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GAPLoader {

	private static boolean loaded = false;
	
	private static void extractReviews(Product p, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("select \"ID_Rw\" from \"Prenda_Reviews\" where \"PID\"="+p.getId());
		ResultSet rs = st.getResultSet();
		if(!rs.next())
		{
			st.close();
			return;
		}
		Integer id = rs.getInt(1);
		st.close();
		
		st = conn.createStatement();
		st.execute("select * from \"Reviews\" where \"ID_Rw\"="+id);
		rs = st.getResultSet();
		while(rs.next())
		{
			p.getReviews().add(rs.getString(1));
		}
		st.close();
	}
	
	private static void extractRatings(Product p, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("select \"ID_R\" from \"Prenda_Rating\" where \"PID\"="+p.getId());
		ResultSet rs = st.getResultSet();
		rs.next();
		Integer id = rs.getInt(1);
		st.close();
		
		st = conn.createStatement();
		st.execute("select * from \"Rating\" where \"ID_R\"="+id);
		rs = st.getResultSet();
		while(rs.next())
		{
			Rating r = new Rating();
			r.setId(id);
			r.setOverall(rs.getString(2));
			r.setLength(rs.getString(3));
			r.setChest(rs.getString(4));
			r.setWaist(rs.getString(5));
			r.setSleeves(rs.getString(6));
			r.setHr(rs.getString(7));
			r.setRise(rs.getString(8));
			r.setGeneral(rs.getString(9));
			r.setOcasions(rs.getString(10));
			r.setCupsize(rs.getString(11));
			r.setSupport(rs.getString(12));
			r.setCoverage(rs.getString(13));
			r.setHighlights(rs.getString(14));
			r.setShrinkage(rs.getString(15));
			
			
			p.getRatings().add(r);
		}
		st.close();
	}

	private static void extractImages(Product p, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("select \"ID_Im\" from \"Prenda_imagen\" where \"PID\"="+p.getId());
		ResultSet rs = st.getResultSet();
		rs.next();
		Integer idImg = rs.getInt(1);
		st.close();
		
		st = conn.createStatement();
		st.execute("select * from \"Imagen\" where \"Im_ID\"="+idImg);
		rs = st.getResultSet();
		while(rs.next())
		{
			String file = rs.getString(1);
			String url = rs.getString(2);
			String color = rs.getString(4);
			ImageInfo ii = new ImageInfo(file,url,color,idImg);
			p.getImageInfo().add(ii);
		}
		st.close();
	}
	
	private static void extractDescription(Product p, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("select \"ID_Des\" from \"Prenda_Description\" where \"PID\"="+p.getId());
		ResultSet rs = st.getResultSet();
		rs.next();
		Integer idDes = rs.getInt(1);
		st.close();
		
		st = conn.createStatement();
		st.execute("select * from \"Description\" where \"ID_Des\"="+idDes);
		rs = st.getResultSet();
		rs.next();
		
		p.setOverview(rs.getString(1));
		p.setSizeAndFiting(rs.getString(2));
		p.setWashing(rs.getString(3));
		p.setImportation(rs.getString(4));
		p.setComposition(rs.getString(5));
		st.close();		
	}
	
	/**
	 * Devuelve la informacion de los productos
	 * @return
	 */
	public static ArrayList<Product> extractProducts(){
		ArrayList<Product> products = new ArrayList<Product>();
	    try {	   
	    	Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/GAP", "sa", ""); 	
			Statement st = conn.createStatement();
			st.execute("select * from \"Prenda\"");
			ResultSet rs = st.getResultSet();
			while(rs.next())
			{			
				Product p = new Product();
				p.setName(rs.getString(1));
				p.setUrl(rs.getString(2));
				p.setCategory(rs.getString(3));
				p.setDivision(rs.getString(4));
				p.setId(rs.getInt(5));
				p.setPrice(rs.getString(6));
				
				extractDescription(p, conn);
				products.add(p);
				//System.out.println(p);
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		//ConfigurableHSQLDBserver.shutDown();
		return products;
	}
	
	/**
	 * Devuelve la informacion del producto que tenga la id pasado como parametro
	 * @param id
	 * @return
	 */
	public static Product extractInfoProductById(Integer id){
		Product p = new Product();
	    try {	    	
	    	Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/GAP", "sa", "");
			Statement st = conn.createStatement();
			st.execute("select * from \"Prenda\" where \"PID\"=" + id);
			ResultSet rs = st.getResultSet();	
			rs.next();
			p.setName(rs.getString(1));
			p.setUrl(rs.getString(2));
			p.setCategory(rs.getString(3));
			p.setDivision(rs.getString(4));
			p.setId(rs.getInt(5));
			p.setPrice(rs.getString(6));
			extractDescription(p, conn);

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		//ConfigurableHSQLDBserver.shutDown();
		return p;
	}
	
	/**
	 * Dado <code>pId</code> devuelve la imagen correspondiente al producto de la base de datos
	 * @param pId Codigo Id del producto
	 * @return
	 */
	public static String extractImagePathByPId(Integer pId){
		String pathFile = "";
	    try {	    	
	    	Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/GAP", "sa", "");
			Statement st = conn.createStatement();
			st.execute("select \"ID_Im\" from \"Prenda_imagen\" where \"PID\"="+pId);
			ResultSet rs = st.getResultSet();	
			rs.next();
			int Im_Id = rs.getInt(1);
			st.execute("select file from \"Imagen\" where \"Im_ID\"=" + Im_Id);
			rs = st.getResultSet();
			rs.next();
			pathFile = rs.getString(1);

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		//ConfigurableHSQLDBserver.shutDown();
		return pathFile;
	}
	
	public static ArrayList<Integer> extractPIdsByPriceRange(Integer min, Integer max){
	
		ArrayList<Integer> products = new ArrayList<Integer>();
	    try {	    	
	    	Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/GAP", "sa", "");
			Statement st = conn.createStatement();
			st.execute("select \"PID\", precio from \"Prenda\"");
			ResultSet rs = st.getResultSet();	
			String precio_str;
			Float precio_tmp;
			Integer pId_tmp;
			while(rs.next()){
				pId_tmp = rs.getInt(1);
				precio_str = rs.getString(2);
				precio_tmp = Float.valueOf(precio_str.replaceAll("[a-z]|[A-Z]",""));
				if (min <= precio_tmp && precio_tmp <= max){
					products.add(pId_tmp);
				}
			}			
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;

	}
	
	public static ArrayList<Integer> extractPIdsByDivision(String division){
		ArrayList<Integer> productos = new ArrayList<Integer>();
		
		try {	    	
	    	Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/GAP", "sa", "");
			Statement st = conn.createStatement();
			st.execute("select \"PID\" from \"Prenda\" where division='"+division+"'");
			ResultSet rs = st.getResultSet();	
			while(rs.next()){
				productos.add(rs.getInt(1));
			}			
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return productos;
	}
	
	public static ArrayList<Integer> extractPIdsByWashing(String washing){
		ArrayList<Integer> productos = new ArrayList<Integer>();
		
		try {	    	
	    	Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/GAP", "sa", "");
			Statement st = conn.createStatement();
			st.execute("select \"PID\" from \"Prenda_Description\" where \"ID_Des\" IN "+
						"(select \"ID_Des\" from \"Description\" where washing like '%"+washing+"%')");
			ResultSet rs = st.getResultSet();	
			while(rs.next()){
				productos.add(rs.getInt(1));
			}			
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return productos;
	}
	
	public static String[] recopilaDivisiones(){
		ArrayList<String> divisiones = new ArrayList<String>();

		try{
			Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/GAP", "sa", "");
			Statement st = conn.createStatement();
			st.execute("select distinct division from \"Prenda\"");
			ResultSet rs = st.getResultSet();	
			while(rs.next()){
				divisiones.add(rs.getString(1));	
			}	
			st.close();
		} catch(Exception e) {
			
		}
    	
		String[] ret = divisiones.toArray(new String[divisiones.size()]);
		return ret;
	
	}
	
	/**
	 * Devuelve para cada una de las divisiones sus categorias.
	 * @param divisiones Las divisiones en las que esta subdividido el catalogo
	 * @return Una matriz con las categorias correspondientes a las divisiones por fila
	 */
	private static String[][] recopilaCategorias(String[] divisiones){
		ArrayList<String[]> categorias = new ArrayList<String[]>();
		Set<String> categoriasPorDiv = new HashSet<String>();
		String tmp;
		int maxCat = 0;
		for (int i=0;i<divisiones.length;i++){
		try{
			categoriasPorDiv.clear();
			Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/GAP", "sa", "");
			Statement st = conn.createStatement();
			st.execute("select distinct categoria from \"Prenda\" where division ='"+divisiones[i]+"'");
			ResultSet rs = st.getResultSet();	
			
			while(rs.next()){
				tmp = rs.getString(1);
				String[] cats = tmp.split(";");
				for(int j=0;j<cats.length;j++){
					categoriasPorDiv.add(cats[j]);
				}
			}
			categorias.add(categoriasPorDiv.toArray(new String[categoriasPorDiv.size()]));
			if (categoriasPorDiv.size() > maxCat) maxCat = categoriasPorDiv.size();
			st.close();
		} catch(Exception e) {
			
		}
		}
		String[][] ret = new String[divisiones.length][maxCat];
		for (int i=0;i<divisiones.length;i++){
			for(int j=0;j<categorias.get(i).length;j++){
				ret[i][j] = categorias.get(i)[j];
			}
		}
		
		return ret;	
	}	
	
	public static String[][] recopilaCategorias(){
		return recopilaCategorias(recopilaDivisiones());
	}
	
	public static void initDataBase(){
		ConfigurableHSQLDBserver.initInMemory("GAP", false);
		ConfigurableHSQLDBserver.loadSQLFile("src/GAPDataBase/dump-v1.sql");		
	}
	
	public static void shutDownDataBase() {
		ConfigurableHSQLDBserver.shutDown();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigurableHSQLDBserver.initInMemory("GAP", false);
		ConfigurableHSQLDBserver.loadSQLFile("src/GAPDataBase/dump-v1.sql");
		try {	    	
			Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/GAP", "sa", "");
			Statement st = conn.createStatement();
			st.execute("select * from \"Prenda\"");
			ResultSet rs = st.getResultSet();
			while(rs.next())
			{
				Product p = new Product();
				p.setName(rs.getString(1));
				p.setUrl(rs.getString(2));
				p.setCategory(rs.getString(3));
				p.setDivision(rs.getString(4));
				p.setId(rs.getInt(5));
				p.setPrice(rs.getString(6));	
				
				extractDescription(p, conn);
				extractImages(p, conn);
				extractRatings(p, conn);
				extractReviews(p, conn);
				//System.out.println(p);
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		ConfigurableHSQLDBserver.shutDown();
		System.exit(0);
	}

}
