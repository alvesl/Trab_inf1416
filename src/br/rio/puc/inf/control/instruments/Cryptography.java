package br.rio.puc.inf.control.instruments;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class Cryptography {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//testSymmetricCrypto();
		
		//testAssymmetricCrypto();
		
		testCombinedCrypto();
	}

	// Test Symmetric Encryption
	private static void testSymmetricCrypto() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
				System.out.println("Chave:");
				String key = in.readLine();
				System.out.println("Passphrase:");
				String passphrase = in.readLine();
				
				byte[] b1 = encryptPrivateKey(key.getBytes("UTF8"), passphrase);
				System.out.println("Chave encriptada: " + toHex(b1));
				
				byte[] b2 = decryptPrivateKey(b1, passphrase);
				System.out.println("Chave decriptada: " + new String(b2, "UTF8"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Test Assymmetric Encryption
	private static void testAssymmetricCrypto() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
				System.out.println("Output file:");
				String output = in.readLine();
				System.out.println("Text to be encrypted: ");
				String text = in.readLine();
				String algorithm = "RSA";
				
				genKeyPair2(output, algorithm);
				PrivateKey privKey = getPrivateKeyFile2(output, algorithm);
				PublicKey pubKey = getPublicKeyFile(output, algorithm);
				
				// Sign with private
				Signature sig = Signature.getInstance("MD5WithRSA");
			    sig.initSign(privKey);
			    sig.update(text.getBytes("UTF8"));
			    byte[] signature = sig.sign();
			    
			    // Verify with public
			    sig.initVerify(pubKey);
			    sig.update(text.getBytes("UTF8"));
			    try {
			      if (sig.verify(signature)) {
			        System.out.println( "Signature verified" );
			      } else System.out.println( "Signature failed" );
			    } catch (SignatureException se) {
			      System.out.println( "Singature failed" );
			    }
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Test combined key cryptography
	private static void testCombinedCrypto()
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
				//System.out.println("Private key file:");
				//String privFile = in.readLine();
				//System.out.println("Public key file:");
				//String pubFile = in.readLine();
				System.out.println("Output: ");
				String output = in.readLine();
				System.out.println("Text to be encrypted: ");
				String text = in.readLine();
				System.out.println("Passphrase: ");
				String passphrase = in.readLine();
				String algorithm = "RSA";
				
				//genKeyPair(output, algorithm, passphrase);
				PublicKey pubKey = getPublicKeyFile(output, algorithm);
				PrivateKey privKey = getPrivateKeyFile2(output, algorithm);
				//PrivateKey privKey = getPrivateKeyFile(output, algorithm, passphrase);

				// Sign with private
				Signature sig = Signature.getInstance("MD5WithRSA");
			    sig.initSign(privKey);
			    sig.update(text.getBytes("UTF8"));
			    byte[] signature = sig.sign();
			    
			    // Verify with public
			    sig.initVerify(pubKey);
			    sig.update(text.getBytes("UTF8"));
			    try {
			      if (sig.verify(signature)) {
			        System.out.println( "Signature verified" );
			      } else System.out.println( "Signature failed" );
			    } catch (SignatureException se) {
			      System.out.println( "Singature failed" );
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
	
	// Generate pair of keys. Private key is encrypted using symmetric key
	public static void genKeyPair(String output, String algorithm, String passphrase)
	{
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
			keyGen.initialize(512);
			KeyPair keyPair = keyGen.generateKeyPair();
			PrivateKey privKey = keyPair.getPrivate();
			PublicKey pubKey = keyPair.getPublic();
			
			// Write public key in file
			String file = output+".pub";
		    FileOutputStream out = new FileOutputStream(file);
		    byte[] encPubKey = pubKey.getEncoded();
		    out.write(encPubKey);
		    out.close();
		    
		    // Encrypt private key with symmetric key
		    byte[] encPrivKey = encryptPrivateKey(privKey.getEncoded(), passphrase);
		    			
			// Write private key in file
			file = output+".pri";
		    out = new FileOutputStream(file);
		    out.write(encPrivKey);
		    out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Generate pair of keys
	public static void genKeyPair2(String output, String algorithm)
	{
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
			keyGen.initialize(512);
			KeyPair keyPair = keyGen.generateKeyPair();
			PrivateKey privKey = keyPair.getPrivate();
			PublicKey pubKey = keyPair.getPublic();
			
			// Write private key in file
			String file = output+".pri";
		    FileOutputStream out = new FileOutputStream(file);
		    byte[] encPrivKey = privKey.getEncoded();
		    out.write(encPrivKey);
		    out.close();
		    
		    file = output+".pub";
		    out = new FileOutputStream(file);
		    byte[] encPubKey = pubKey.getEncoded();
		    out.write(encPubKey);
		    out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//  Get Public key from file
	public static PublicKey getPublicKeyFile(String input, String algorithm)
	{
		PublicKey pubKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
			
			String pubKeyFile = input +".pub";
		    FileInputStream pubKeyStream = new FileInputStream(pubKeyFile);
		    int pubKeyLength = pubKeyStream.available();
		    byte[] pubKeyBytes = new byte[pubKeyLength];
		    pubKeyStream.read(pubKeyBytes);
		    pubKeyStream.close();
		    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
		    pubKey = keyFactory.generatePublic(pubKeySpec);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return pubKey;
	}
	
	// Get Private key from file
	public static PrivateKey getPrivateKeyFile(String input, String algorithm, String passphrase)
	{
		PrivateKey privKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
			
			String privKeyFile = input +".pri";
		    FileInputStream priKeyStream = new FileInputStream(privKeyFile);
		    int priKeyLength = priKeyStream.available();
		    byte[] encPrivKeyBytes = new byte[priKeyLength];
		    priKeyStream.read(encPrivKeyBytes);
		    priKeyStream.close();
		    //byte[] privKeyBytes = decryptPrivateKey(encPrivKeyBytes, passphrase);
		    PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encPrivKeyBytes);
		    privKey = keyFactory.generatePrivate(privKeySpec);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return privKey;
	}
	
	// Get Private key from file
	public static PrivateKey getPrivateKeyFile2(String input, String algorithm)
	{
		PrivateKey privKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
			
			String priKeyFile = input+".pri";
		    FileInputStream priKeyStream = new FileInputStream(priKeyFile);
		    int priKeyLength = priKeyStream.available();
		    byte[] priKeyBytes = new byte[priKeyLength];
		    priKeyStream.read(priKeyBytes);
		    priKeyStream.close();
		    PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
		    privKey = keyFactory.generatePrivate(priKeySpec);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return privKey;
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
	
	// Sign array of bytes
	private static byte[] signByteArray (byte[] bArray, PrivateKey privateKey)
	{
		byte[] signedBArray = null;
		try {
			Signature sig = Signature.getInstance("RSAwithMD5");
			
			sig.initSign(privateKey);
			sig.update(bArray);
			
			signedBArray = sig.sign();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return signedBArray;
	}
	
	// Verify Digital signature of an array of bytes
	private static boolean verifyDigitalSigByteArray (byte[] signature, byte[] bArray, PublicKey publicKey)
	{
		boolean verified = false;
		try {
			Signature sig = Signature.getInstance("RSAwithMD5");
			sig.initVerify(publicKey);
			sig.update(bArray);
			
			verified = sig.verify(signature);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return verified;
	}
	
	// Get symmetric key from passphrase
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
