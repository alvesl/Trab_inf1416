package br.rio.puc.inf.control.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sun.jmx.snmp.Timestamp;

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
	
	// Update the Database user password, which is HEX(HASH_MD5(password + ID + username))
	public static void updateDbPasswd(User user)
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
	
	// Update the Database user public key
	public static void updatePublicKey(User user)
	{
		Statement stmt = null;
		String sqlUpdate;
		
		sqlUpdate = "UPDATE Users SET Public_key = '" + user.getPublicKey() + "' WHERE ID = " + user.getID() + "";
		try {

			stmt = theConn.createStatement();
			stmt.executeUpdate(sqlUpdate);
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Get Group Name given ID
	public static String getGroupName(int groupID)
	{
		ResultSet rs;
		Statement stmt = null;
		String sqlQuery;
		String group = null;
		
		sqlQuery = "Select Name FROM Groups WHERE ID = " + Integer.toString(groupID);
		try {

			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
						
			rs.next();
			group = rs.getString(1);
			
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return group;
	}
	
	// Get Group Name given ID
	public static int getNumRegisteredUsers()
	{
		ResultSet rs;
		Statement stmt = null;
		String sqlQuery;
		int num = -1;
		
		sqlQuery = "SELECT COUNT(*) FROM Users";
		try {

			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
						
			rs.next();
			num = rs.getInt(1);
			
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	// Get Group Name given ID
	public static java.sql.Timestamp getLastBlockByUser(String username)
	{
		ResultSet rs;
		Statement stmt = null;
		String sqlQuery;
		java.sql.Timestamp time = null;
		
		sqlQuery = "SELECT TIME_INSERTED FROM Registries WHERE Username = '" + username + "' And (Message_ID=3008 OR Message_ID=4007) AND  ID = (SELECT MAX(ID)  FROM Registries WHERE Message_ID=3008 OR Message_ID = 4007);";
		try {

			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
						

			if (rs.next())
				time = rs.getTimestamp(1);
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	
	// Insert log message in DB
	public static void registerMessage(int ID, String username)
	{
		PreparedStatement stmt = null;
		String sqlInsert;
		Date currentDatetime = new Date(System.currentTimeMillis());
		java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDatetime.getTime()); 
        
		if(username != null)
			sqlInsert = "INSERT INTO Registries (Message_ID, Time_inserted, Username) VALUES(" + Integer.toString(ID) + ", " + 
					"?, '" + username + "')";
		else
			sqlInsert = "INSERT INTO Registries (Message_ID, Time_inserted) VALUES('"+ Integer.toString(ID) + "', ?)";
		
		try {

			stmt = theConn.prepareStatement(sqlInsert);
			stmt.setTimestamp(1, timestamp);

			
			stmt.execute();
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Insert log message in DB
	public static void registerMessage(int ID, String username, String filename)
	{
		PreparedStatement stmt = null;
		String sqlInsert;
		Date currentDatetime = new Date(System.currentTimeMillis());
		java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDatetime.getTime()); 
        
		sqlInsert = "INSERT INTO Registries (Message_ID, Time_inserted, Username, Filename) VALUES(" + Integer.toString(ID) + ", " + 
					"?, '" + username + "', '" + filename +"')";
		
		try {

			stmt = theConn.prepareStatement(sqlInsert);
			stmt.setTimestamp(1, timestamp);

			
			stmt.execute();
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean verifyUserBlocked(String username)
	{
		// TODO
		return false;
	}
	
	// Get number of loged in times
	public static int getNumLogedPasswd(String username)
	{
		ResultSet rs;
		Statement stmt = null;
		String sqlQuery;
		int num = -1;
		
		sqlQuery = "SELECT Num_loged FROM Users WHERE Username = '" + username + "'";
		try {

			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
						
			rs.next();
			num = rs.getInt(1);
			
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	// Update number of times loged using current password
	public static void updateNumLogedPasswd(String username, int numLoged)
	{
		Statement stmt = null;
		String sqlUpdate;
		
		sqlUpdate = "UPDATE Users SET Num_loged = " + numLoged + " WHERE Username = '" + username + "'";
		try {

			stmt = theConn.createStatement();
			stmt.executeUpdate(sqlUpdate);
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Get number of loged in times
	public static int getNumLoged(String username)
	{
		ResultSet rs;
		Statement stmt = null;
		String sqlQuery;
		int num = 0;
		
		sqlQuery = "SELECT COUNT(*) FROM Registries WHERE ((Message_ID = 4003) AND (Username = '" + username + "'))";
		try {

			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
						
			if(rs.next())
				num = rs.getInt(1);
			
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	// Get number of times passwd or public key were changed
	public static int getNumChanges(String username)
	{
		ResultSet rs;
		Statement stmt = null;
		String sqlQuery;
		int num = 0;
		
		sqlQuery = "SELECT COUNT(*) FROM Registries WHERE ((Message_ID = 7002) AND (Username = '" + username + "'))";
		try {

			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
						
			if(rs.next())
				num = rs.getInt(1);
			
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	// Get number of times passwd or public key were changed
	public static int getNumQueries(String username)
	{
		ResultSet rs;
		Statement stmt = null;
		String sqlQuery;
		int num = 0;
		
		sqlQuery = "SELECT COUNT(*) FROM Registries WHERE ((Message_ID = 8003) AND (Username = '" + username + "'))";
		try {

			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
						
			if(rs.next())
				num = rs.getInt(1);
			
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	// Write log into a file
	public static List<String> getLog()
	{
		ResultSet rs;
		Statement stmt = null;
		String sqlQuery;
		java.sql.Timestamp date = null;
		String username = null;
		String message = null;
		String filename = null;
		List<String> array = new ArrayList<>();
		
		
		sqlQuery = "SELECT Registries.ID, Registries.Time_inserted, Registries.Username, Messages.Message, Registries.Filename FROM Registries INNER JOIN Messages " +
				"ON Registries.Message_ID = Messages.ID " + 
				"ORDER BY Registries.ID ASC";
		try {

			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
						
			while(rs.next())
			{
				String str;
				date = rs.getTimestamp(2);
				username = rs.getString(3);
				message = rs.getString(4);
				filename = rs.getString(5);
				if(username != null)
				{
					String temp = message.replace("<login_name>", username);
					if(filename != null)
					{
						str = date.toString() + ": " + temp.replace("<arq_name>", filename);
					}
					else
						str = date.toString() + ": " + temp;
				}
				else
					str = date.toString() + ": " + message;
				array.add(str);
			}
			
			stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
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
