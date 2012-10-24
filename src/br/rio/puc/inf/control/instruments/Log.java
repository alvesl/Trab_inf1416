package br.rio.puc.inf.control.instruments;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

import br.rio.puc.inf.control.db.AccessJDBC;

public class Log {

	// Log Info into the DB and retreives it
	
	public static void main(String[] args) 
	{
		AccessJDBC.init();
		List<String> list = AccessJDBC.getLog();
		
		try {
			FileWriter fStream = new FileWriter("log.txt");
			BufferedWriter out = new BufferedWriter(fStream);
			Iterator iter = list.iterator();
			while(iter.hasNext())
			{
				out.write((String)iter.next());
				out.newLine();
			}
			out.close();
		}
		catch (Exception e) {
				//System.out.println(e.printStackTrace());
		}
	}
	
	// Register Log into DB
	public static void registerMessage(int ID, String username)
	{
		AccessJDBC.registerMessage(ID, username);
	}
	
	
	
}
