package br.rio.puc.inf.control.db;

import java.sql.*;

public class AccessJDBC {

	static private Connection theConn;

	public static void init() {
		try {
			theConn = getNewConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Connection getNewConnection() throws Exception {
		Driver d = (Driver) Class.forName("sun.jdbc.odbc.JdbcOdbcDriver")
				.newInstance();
		Connection c = DriverManager.getConnection("jdbc:odbc:Group3DB");
		return c;
	}

	public static boolean VerifyUser(String username) {

		ResultSet rs;
		Statement stmt;
		String sql;
		Boolean returnType = false;

		sql = "select * from Users WHERE Username='" + username+ "'";
		try {
			stmt = theConn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
				returnType = true;
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnType;
	}

	public static Connection getTheConn() {
		return theConn;
	}

	public static void setTheConn(Connection theConn) {
		AccessJDBC.theConn = theConn;
	}
	
	public static void AttemptToClose() {
	      try {  
		        if (theConn != null) theConn.close();  
		      }  
		      catch (Exception e) {  
		      }  
		
	}

}
