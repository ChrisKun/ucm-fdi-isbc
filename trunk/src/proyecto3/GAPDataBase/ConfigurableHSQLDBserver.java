/**
 * HSQLDBserver.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 04/07/2007
 */
package GAPDataBase;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;

import jcolibri.util.FileIO;

import org.hsqldb.Server;
import org.hsqldb.util.SqlFile;

/**
 * Creates a data base server.<br>
 * Server can be persistent (saved into the file system) or not (just in memory).<br>
 * User can load several sql files to import the data.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class ConfigurableHSQLDBserver
{
    static boolean initialized = false;

    private static Server server;
    
    private static PrintStream outStream = new PrintStream(System.out);
    private static PrintWriter out = new PrintWriter(outStream);
    private static String dataBaseName;

    /**
     * Initialize the server using a non persistent data base in memory 
     * @param databaseName is the name of the data base
     * @param showLog indicates if log comments must be shown
     */
    public static void initInMemory(String databaseName, boolean showLog)
    {
	if (initialized) return;
	
    org.apache.commons.logging.LogFactory.getLog(ConfigurableHSQLDBserver.class).info("Creating data base ...");

    dataBaseName = databaseName;
        
	server = new Server();
	server.setDatabaseName(0, databaseName);
	server.setDatabasePath(0, "mem:"+databaseName+";sql.enforce_strict_size=true");

	if(showLog)
	{
	    server.setLogWriter(out);
	    server.setErrWriter(out);
	}
	else
	{
	    server.setLogWriter(null);
	    server.setErrWriter(null);
	}
	server.setSilent(!showLog);
	server.setTrace(showLog);
	
	server.start();
	
	initialized = true;
    }
    
    /**
     * Initialize the server using a persistent data base in the file system <p>
     * This persistent data base contains HSQLDB MEMORY tables.
     * Memory tables are the default type when the CREATE TABLE command is used. 
     * Their data is held entirely in memory but any change to their structure or contents is written to the <dbname>.script file. 
     * The script file is read the next time the database is opened, and the MEMORY tables are recreated with all their contents. 
     * So MEMORY tables are persistent.
     * <p>
     * More info: <a href="http://hsqldb.org/doc/guide/ch01.html">http://hsqldb.org/doc/guide/ch01.html</a>
     * 
     * @param databaseName is the name of the data base
     * @param showLog indicates if log comments must be shown
     * @param directory where the data base 
     * must be created or loaded from. This directory must exist.
     */
    public static void initInFileSystem(String databaseName, boolean showLog, String directory)
    {
	if (initialized)
	    return;
	
        org.apache.commons.logging.LogFactory.getLog(ConfigurableHSQLDBserver.class).info("Creating data base in file system...");

        dataBaseName = databaseName;
        
	server = new Server();
	
	server.setDatabaseName(0, databaseName);
	server.setDatabasePath(0, "file:"+directory+File.separator+databaseName+";sql.enforce_strict_size=true");

	if(showLog)
	{
	    server.setLogWriter(out);
	    server.setErrWriter(out);
	}
	else
	{
	    server.setLogWriter(null);
	    server.setErrWriter(null);
	}
	server.setSilent(!showLog);
	server.setTrace(showLog);
	
	server.start();
	
	initialized = true;
    }
    
    /**
     * Loads the given sql file.
     * @param sqlfile to be loaded
     */
    public static void loadSQLFile(String sqlfile)
    {
	if (!initialized)
	{
	    org.apache.commons.logging.LogFactory.getLog(ConfigurableHSQLDBserver.class).warn("Server must be initialized before loading SQL files.");
	    return;
	}
	
	try
	{
	    Class.forName("org.hsqldb.jdbcDriver");

	    Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/"+dataBaseName, "sa", "");
	    File f = new File( FileIO.findFile(sqlfile).getFile() );
	    SqlFile file = new SqlFile(f,false,new HashMap());
	    file.execute(conn,outStream,outStream, true);

	    org.apache.commons.logging.LogFactory.getLog(ConfigurableHSQLDBserver.class).info("SQL file: "+sqlfile+" loaded");
	    
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(ConfigurableHSQLDBserver.class).error(e);
	}

    }

    /**
     * Shutdown the server
     */
    public static void shutDown()
    {
	if (!initialized)
	    return;
	
	try
	{
	    Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/"+dataBaseName, "sa", "");
	    Statement st = conn.createStatement();
	    st.execute("SHUTDOWN");
	    server.stop();
	    initialized = false;
	} catch (Exception e)
	{
	    org.apache.commons.logging.LogFactory.getLog(ConfigurableHSQLDBserver.class).error(e);
	    
	}

	
    }

    /**
     * Testing method
     */
    public static void main(String[] args)
    {
	ConfigurableHSQLDBserver.initInMemory("travel", true);
	ConfigurableHSQLDBserver.loadSQLFile("jcolibri/test/database/travel.sql");
	ConfigurableHSQLDBserver.shutDown();
	System.exit(0);
	
    }

}
