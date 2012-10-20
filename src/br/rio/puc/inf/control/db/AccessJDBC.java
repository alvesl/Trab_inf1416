package br.rio.puc.inf.control.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.rio.puc.inf.model.User;

public class AccessJDBC {

	static private Connection theConn;

	
	/***********************
	 * Database Control
	 *
	 */
	
	public static void init() {
		try {
			theConn = getNewConnection();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void attemptToClose() {
	      try {  
	        if (theConn != null) theConn.close();  
	      }  
	      catch (Exception e) {  
	      }  
	}

	private static Connection getNewConnection() throws Exception {
		Driver d = (Driver) Class.forName("sun.jdbc.odbc.JdbcOdbcDriver")
				.newInstance();
		Connection c = DriverManager.getConnection("jdbc:odbc:Group3DB");
		return c;
	}

	/***********************
	 * Database Inserts, Updates and Querys
	 *
	 */
	
	// Verify if username exists
	public static boolean VerifyUser(String username) {

		ResultSet rs;
		Statement stmt;
		String sqlQuery;
		Boolean returnType = false;

		sqlQuery = "select * from Users WHERE Username='" + username+ "'";
		try {
			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			if (rs.next())
				returnType = true;
			rs.close();
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		return returnType;
	}
	
	// Insert user in DB
	public static void insertUser(User user)
	{
		Statement stmt = null;
		String sqlInsert;
		
		sqlInsert = "INSERT INTO Users (Full_Name, Username, Group_ID, Public_Key, Num_loged) VALUES('" + user.getFullName() + "', '" + 
					user.getUsername() + "', '" + user.getGroupID() + "', '" + user.getPublicKey() + "', '" + user.getNumLoged() + "')";


		
		System.out.println(sqlInsert);
		try {

			stmt = theConn.createStatement();

			
			stmt.execute(sqlInsert);
			
			// Updates the Db password
			user.setID(getLastUserIDInserted());			
			updateDbPasswd(user);
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Returns the last user ID inserted into Users
	public static User getUser(String username) {
		ResultSet rs;
		Statement stmt;
		String sqlQuery;
		int ID = -1;
		String fullName = null;
		int groupID = -1;
		String dbPassword = null;
		String publicKey = null;
		int numLoged = -1;
		

		sqlQuery = "SELECT * FROM Users WHERE Username = '" + username + "'";
		try {
			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			
			rs.next();
			ID = rs.getInt(1);
			fullName = rs.getString(2);
			groupID = rs.getInt(4);
			dbPassword = rs.getString(5);
			publicKey = rs.getString(6) ;
			numLoged = rs.getInt(7);
			
			rs.close();
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		return new User(ID, username, fullName, groupID, dbPassword, publicKey, numLoged);
	}
	
	// Returns the last user ID inserted into Users
	private static int getLastUserIDInserted() {
		ResultSet rs;
		Statement stmt;
		String sqlQuery;
		int ID = -1;

		sqlQuery = "select MAX(ID) from Users";
		try {
			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			
			rs.next();
			ID = rs.getInt(1);

			rs.close();
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		return ID;
	}
	
	// Returns the last user ID inserted into Users
	public static int getUserID(String username) {
		ResultSet rs;
		Statement stmt;
		String sqlQuery;
		int ID = -1;

		sqlQuery = "SELECT ID FROM Users WHERE Username = '" + username + "'";
		try {
			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			
			rs.next();
			ID = rs.getInt(1);

			rs.close();
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		return ID;
	}
	
	// Insert the Database user password, which is HEX(HASH_MD5(password + ID + username))
	private static void updateDbPasswd(User user)
	{
		Statement stmt = null;
		String sqlUpdate;
		
		try {
			user.setDbPassword(User.generateDbPassword(user, "MD5"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		sqlUpdate = "UPDATE Users SET Password = '"+ user.getDbPassword() + "' WHERE ID = " + user.getID() + "";
		try {

			stmt = theConn.createStatement();
			stmt.executeUpdate(sqlUpdate);
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/***********************
	 * Getters and Setters
	 *
	 */
	
	public static Connection getTheConn() {
		return theConn;
	}

	public static void setTheConn(Connection theConn) {
		AccessJDBC.theConn = theConn;
	}
	
}
