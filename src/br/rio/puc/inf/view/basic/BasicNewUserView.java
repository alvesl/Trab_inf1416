package br.rio.puc.inf.view.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.rio.puc.inf.control.db.AccessJDBC;
import br.rio.puc.inf.control.instruments.Digest;
import br.rio.puc.inf.model.User;

public class BasicNewUserView {

	
	public static void main(String[] args) {
		
		//Instanciar conexão com DB
		AccessJDBC.init();
		
		String fullName = null;
		String username = null;
		int groupID = -1;
		String passwd = null;
		String publicKey = null;
		
		// Read user info
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));  
		try {
			System.out.print("Login:");
			username = in.readLine();
			while(AccessJDBC.VerifyUser(username))
			{
				System.out.println("Login ja existe. Tente novamente.\nLogin:");
				username = in.readLine();
			}
			
			System.out.print("Nome Completo:");
			fullName = in.readLine();
			
			System.out.print("Grupo:");
			groupID  = Integer.parseInt(in.readLine());
			
			System.out.print("Senha:");
			passwd = in.readLine();
			while(!User.checkpassword(passwd))
			{
				System.out.println("Senha fraca. Tente de novamente.\nSenha");
				passwd = in.readLine();
			}
			
			System.out.print("Chave Publica:");
			publicKey = in.readLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
		User user = new User(username, fullName, groupID, passwd, publicKey);
		
		// Insert user in DB
		AccessJDBC.insertUser(user);
		
		// Check if user is correct (login and password)
		boolean ok = false;
		User user2, user3;
		while (!ok)
		{
			try {
				System.out.print("Login Verificacao:");
				username = in.readLine();
				if(!AccessJDBC.VerifyUser(username))
				{
					System.out.println("Usuario inexistente");
					continue;
				}
				user2 = AccessJDBC.getUser(username);
				System.out.print("Senha Verificacao:");
				passwd = in.readLine();
				user3 = new User(username, null, -1, passwd, null);
				user3.setID(AccessJDBC.getUserID(username));
				String s1 = user2.getDbPassword();
				String s2 = User.generateDbPassword(user3, "MD5");
				
				System.out.println(s1);
				System.out.println(s2);
				
				if(!Digest.compareDigest(s1, s2))
				{
					System.out.println("Senha incorreta");
					continue;
				}
				System.out.println("Login e senha Verificadas!");
				break;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		AccessJDBC.attemptToClose();

	}

}
