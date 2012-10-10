package br.rio.puc.inf.view.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;

import br.rio.puc.inf.control.db.AccessJDBC;



public class BasicView {

	/**
	 * @param args
	 */
	

	
	public static void main(String[] args) {
		
		//Instanciar conex�o com DB
		AccessJDBC.init(); 
		
		//Realizar leitura do login
		System.out.print("Login:");
		String username = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));  
		try {
			username = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		//Verificar se existe usu�rio no banco
		if(AccessJDBC.VerifyUser(username)) {
			System.out.println("Usu�rio existe!");
		} else {
			System.out.println("Usu�rio inexistente!");
		}
		
		AccessJDBC.AttemptToClose();

	}

}
