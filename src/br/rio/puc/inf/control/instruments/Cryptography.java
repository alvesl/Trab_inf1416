package br.rio.puc.inf.control.instruments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class Cryptography {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			while(true) {
				System.out.println("Chave:");
				String key = in.readLine();
				System.out.println("Passphrase:");
				String passphrase = in.readLine();
				
				byte[] b1 = encryptPrivateKey(key.getBytes("UTF8"), passphrase);
				System.out.println("Chave encriptada: " + toHex(b1));
				
				byte[] b2 = decryptPrivateKey(b1, passphrase);
				System.out.println("Chave decriptada: " + new String(b2));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**************
	 *  Cryptography helper
	 * 
	 */
	
	// Encrypt Private key
	public static byte[] encryptPrivateKey(byte[] key, String passphrase)
	{
		return encryptByteArray(key, "DES/ECB/PKCS5Padding", passphrase);
	}
	
	// Decrypt Private key
	public static byte[] decryptPrivateKey(byte[] encryptedKey, String passphrase)
	{
		return decryptByteArray(encryptedKey, "DES/ECB/PKCS5Padding", passphrase);
	}
	
	private static Key getSymmetricKey (String passphrase)
	{
		Key key = null;
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("DES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			
			random.setSeed(passphrase.getBytes("UTF8"));
			keyGen.init(56, random);
			
			key = keyGen.generateKey();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}
	
	// Encrypt an array of bytes
	private static byte[] encryptByteArray(byte[] bArray, String algorithm, String seed)
	{
		byte[] encryptedArray = null;
		try {
			Key symKey = getSymmetricKey(seed);
			Cipher cipher = Cipher.getInstance(algorithm);
			
			cipher.init(Cipher.ENCRYPT_MODE, symKey);
			encryptedArray = cipher.doFinal(bArray);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedArray;
	}
	
	// Decrypt an array of bytes
	private static byte[] decryptByteArray(byte[] bArray, String algorithm, String seed)
	{
		byte[] decryptedArray = null;
		try {
			Key symKey = getSymmetricKey(seed);
			Cipher cipher = Cipher.getInstance(algorithm);
			
			cipher.init(Cipher.DECRYPT_MODE, symKey);
			decryptedArray = cipher.doFinal(bArray);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedArray;
	}
	
	// Converts an array of bytes to a HEX represented string
	private static String toHex(byte[] array)
	{
		StringBuffer buf = new StringBuffer();
	    for(int i = 0; i < array.length; i++) {
	       String hex = Integer.toHexString(0x0100 + (array[i] & 0x00FF)).substring(1);
	       buf.append((hex.length() < 2 ? "0" : "") + hex);
	    }
	    return buf.toString();
	}

}
