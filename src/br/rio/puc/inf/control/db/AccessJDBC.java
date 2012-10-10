package br.rio.puc.inf.control.db;

import java.sql.*;

public class AccessJDBC {
	
	  static Connection theConn;  
	  
	  public static void main (String args[]) {  
	    try {  
	      // connection to an ACCESS MDB  
	      theConn = MyConnection.getConnection();  
	  
	      ResultSet rs;  
	      Statement stmt;  
	      String sql;  
	  
	      sql =  "select * from Messages";  
	      stmt = theConn.createStatement();  
	      rs = stmt.executeQuery(sql);  
	  
	      while (rs.next()) {  
	         System.out.println(rs.getString("Message"));  
	      }  
	      rs.close();  
	      stmt.close();  
	    }  
	    catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    finally {  
	      try {  
	        if (theConn != null) theConn.close();  
	      }  
	      catch (Exception e) {  
	      }  
	    }  
	  
	    }  
	  }  
	  
	class MyConnection {  
	  public static Connection getConnection() throws Exception {  
	     Driver d = (Driver)Class.forName  
	         ("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();  
	     String filename = "db.accdb";
	     Connection c = DriverManager.getConnection(
	    		 "jdbc:odbc:Group3DB"
	    		 );
	     return c;        
	    } 

}
