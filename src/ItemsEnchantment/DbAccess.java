package ItemsEnchantment;

import java.sql.*;
/**
 * Classe che gestisce l'interfacciamento col db.
 * @author Savio S. Pastore
 */
public class DbAccess {
	static String DRIVER_CLASS_NAME="org.gjt.mm.mysql.Driver";
	private static final String DBMS="jdbc:mysql";
	private static final String SERVER="localhost";
	static final String DATABASE="spotify_mpd";
	private static final int PORT=3306;
	private static String USER_ID="";
	private static final String PASSWORD = "";
	private static Connection conn;
    /**
     * Provvede a caricare il driver per stabilire la connessione con la base di dati e inizializza tale connessione.
     * @throws DatabaseConnectionException
     */
	public static void initConnection() throws ClassNotFoundException{
		String connectionString=DBMS+"://"+SERVER+":"+PORT+"/"+DATABASE;
		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try{
			conn= DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
   /**
    *
    * @return  connessione corrente.
    */
	public static Connection getConnection(){return conn;}
	/**
	 * Chiude la connessione.
	 * @throws SQLException
	 */
	public static void closeConnection() throws SQLException{
		conn.close();
	}
}
