package br.rio.puc.inf.view.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.rio.puc.inf.control.db.AccessJDBC;
import br.rio.puc.inf.model.User;

public class NewUserView {

	
	public static void main(String[] args) {
		
		//Instanciar conexão com DB
		AccessJDBC.init();
		
		String fullName = null;
		String username = null;
		int groupID = -1;
		String passwd = null;
		String publicKey = null;
		
		//Realizar leitura do login
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

		
		//Verificar se existe usuário no banco
		if(AccessJDBC.VerifyUser(username)) {
			System.out.println("Usuário existe!");
		} else {
			System.out.println("Usuário inexistente!");
		}
		
		AccessJDBC.attemptToClose();

	}

}
