package br.rio.puc.inf.control.instruments;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
				
				byte[] b1 = encryptPrivateKeySymmetric(key.getBytes("UTF8"), passphrase);
				System.out.println("Chave encriptada: " + toHex(b1));
				
				byte[] b2 = decryptPrivateKeySymmetric(b1, passphrase);
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
				System.out.println("Public file:");
				String pubFile = in.readLine();
				System.out.println("Private file:");
				String privFile = in.readLine();
				System.out.println("Text to be encrypted: ");
				String text = in.readLine();
				String algorithm = "RSA";
				
				genKeyPair2(pubFile, privFile, algorithm);
				PrivateKey privKey = getPrivateKeyFile2(privFile, algorithm);
				PublicKey pubKey = getPublicKeyFile(pubFile, algorithm);
				
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
				System.out.println("Private File: ");
				String privFile = in.readLine();
				System.out.println("Public File: ");
				String pubFile = in.readLine();
				System.out.println("Text to be encrypted: ");
				String text = in.readLine();
				System.out.println("Passphrase: ");
				String passphrase = in.readLine();
				String algorithm = "RSA";
				
				//genKeyPairAssymmetric(pubFile, privFile, algorithm, passphrase);
				PublicKey pubKey = getPublicKeyFile(pubFile, algorithm);
				PrivateKey privKey = getPrivateKeyFile(privFile, algorithm, passphrase);

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
			if(e instanceof FileNotFoundException)
				System.out.println("File doesnt exist");
			else
				System.out.println("Verificação negativa");
		}
	}
	
	/**************
	 *  Cryptography helper
	 * 
	 */
	
	// Generate pair of keys. Private key is encrypted using symmetric key
	public static void genKeyPairAssymmetric(String pubFile, String privFile, String algorithm, String passphrase)
	{
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
			keyGen.initialize(512);
			KeyPair keyPair = keyGen.generateKeyPair();
			PrivateKey privKey = keyPair.getPrivate();
			PublicKey pubKey = keyPair.getPublic();
			
			// Write public key in file
			String file = pubFile;
		    FileOutputStream out = new FileOutputStream(file);
		    byte[] encPubKey = pubKey.getEncoded();
		    out.write(encPubKey);
		    out.close();
		    
		    // Encrypt private key with symmetric key
		    byte[] encPrivKey = encryptPrivateKeySymmetric(privKey.getEncoded(), passphrase);
		    			
			// Write private key in file
			file = privFile;
		    out = new FileOutputStream(file);
		    out.write(encPrivKey);
		    out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	//  Get Public key from file
	public static PublicKey getPublicKeyFile(String inputFile, String algorithm) throws Exception
	{
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		
		String pubKeyFile = inputFile;
	    FileInputStream pubKeyStream = new FileInputStream(pubKeyFile);
	    int pubKeyLength = pubKeyStream.available();
	    byte[] pubKeyBytes = new byte[pubKeyLength];
	    pubKeyStream.read(pubKeyBytes);
	    pubKeyStream.close();
	    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
	    PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

	    return pubKey;
	}
	
	// Get Private key from file. Private key must be encrypted by symmetric key
	public static PrivateKey getPrivateKeyFile(String inputFile, String algorithm, String passphrase) throws Exception
	{
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		
		String privKeyFile = inputFile;
	    FileInputStream priKeyStream = new FileInputStream(privKeyFile);
	    int priKeyLength = priKeyStream.available();
	    byte[] encPrivKeyBytes = new byte[priKeyLength];
	    priKeyStream.read(encPrivKeyBytes);
	    priKeyStream.close();
	    byte[] privKeyBytes = decryptPrivateKeySymmetric(encPrivKeyBytes, passphrase);
	    PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKeyBytes);
	    PrivateKey privKey = keyFactory.generatePrivate(privKeySpec);
	    
		return privKey;
	}	
	
	// Encrypt Private key
	public static byte[] encryptPrivateKeySymmetric(byte[] key, String passphrase) throws Exception
	{
		return encryptByteArray(key, "DES/ECB/PKCS5Padding", passphrase);
	}
	
	// Decrypt Private key
	public static byte[] decryptPrivateKeySymmetric(byte[] encryptedKey, String passphrase) throws Exception
	{
		return decryptByteArray(encryptedKey, "DES/ECB/PKCS5Padding", passphrase);
	}
	
	// Encrypt an array of bytes
	private static byte[] encryptByteArray(byte[] bArray, String algorithm, String seed) throws Exception
	{
		Key symKey = getSymmetricKey(seed);
		Cipher cipher = Cipher.getInstance(algorithm);
		
		cipher.init(Cipher.ENCRYPT_MODE, symKey);
		byte[] encryptedArray = cipher.doFinal(bArray);
		
		return encryptedArray;

	}
	
	// Decrypt an array of bytes
	private static byte[] decryptByteArray(byte[] bArray, String algorithm, String seed) throws Exception
	{
		Key symKey = getSymmetricKey(seed);
		Cipher cipher = Cipher.getInstance(algorithm);
		
		cipher.init(Cipher.DECRYPT_MODE, symKey);
		byte[] decryptedArray = cipher.doFinal(bArray);

		return decryptedArray;
	}
	
	// Sign array of bytes
	private static byte[] signByteArraySymmetric (byte[] bArray, PrivateKey privateKey)
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
	private static Key getSymmetricKey (String passphrase) throws Exception
	{
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		SecureRandom random = new SecureRandom();
		
		random.setSeed(passphrase.getBytes("UTF8"));
		keyGen.init(56, random);
		
		Key key = keyGen.generateKey();

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

	
	
	/*********************************************************
	*
	*Obsolete Methods 
	*/
	
	// Generate pair of keys
	private static void genKeyPair2(String outputFilePub, String outputFilePriv, String algorithm)
	{
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
			keyGen.initialize(512);
			KeyPair keyPair = keyGen.generateKeyPair();
			PrivateKey privKey = keyPair.getPrivate();
			PublicKey pubKey = keyPair.getPublic();
			
			// Write private key in file
			String file = outputFilePriv;
		    FileOutputStream out = new FileOutputStream(file);
		    byte[] encPrivKey = privKey.getEncoded();
		    out.write(encPrivKey);
		    out.close();
		    
		    // Write public key in a file
		    file = outputFilePub;
		    out = new FileOutputStream(file);
		    byte[] encPubKey = pubKey.getEncoded();
		    out.write(encPubKey);
		    out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
}
