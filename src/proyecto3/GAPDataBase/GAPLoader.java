package GAPDataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GAPLoader {
	
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
	 * Funcion propia que solo devuelva la informacion mas relevante de los productos
	 * @return
	 */
	public static ArrayList<Product> extractProducts(){
		ConfigurableHSQLDBserver.initInMemory("GAP", false);
		ConfigurableHSQLDBserver.loadSQLFile("proyecto3/GAPDataBase/dump-v1.sql");
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
			//st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		ConfigurableHSQLDBserver.shutDown();
		return products;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigurableHSQLDBserver.initInMemory("GAP", false);
		ConfigurableHSQLDBserver.loadSQLFile("proyecto3/GAPDataBase/dump-v1.sql");
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
