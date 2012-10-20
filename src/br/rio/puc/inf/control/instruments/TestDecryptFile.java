package br.rio.puc.inf.control.instruments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.PrivateKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class TestDecryptFile {

	/**
	 * This class is reponsible to use the Cryptography class to test de decryption of a file.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.println("Privatekey file:");
			String privFile = in.readLine();
			System.out.println("Passphrase: ");
			String passphrase = in.readLine();
			
			System.out.println("Env file:");
			String envelope = in.readLine();
			
			System.out.println("Enc file:");
			String encFile = in.readLine();
			
			byte[] envelopeBytes = Cryptography.getEncFile(envelope);
			byte[] encFileBytes = Cryptography.getEncFile(encFile);
			
			// Decriptar o envelope digital
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			PrivateKey privKey = Cryptography.getPrivateKeyFile(privFile, passphrase);
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			
			byte[] decryptedEnvelope = cipher.doFinal(envelopeBytes);
			System.out.println("Envelope: "+new String(decryptedEnvelope));
			
			// gerar chave DES utilizando a semente do envelope
			Key key = Cryptography.generateDESKey(decryptedEnvelope);
			Cipher cipherB= Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipherB.init(Cipher.DECRYPT_MODE, key);
			
			// Descriptar o arquivo codificado
			byte[] decFileBytes = cipherB.doFinal(encFileBytes);
			
			System.out.println(new String (decFileBytes));
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		

	}

}
