package br.rio.puc.inf.control.instruments;

import java.security.MessageDigest;

public class Digest {
	
	private MessageDigest messageDigest;
	
	/*************
	 *  Public Constructors
	 * 
	 */
	
	public Digest(String algorithm) throws Exception
	{
		messageDigest = MessageDigest.getInstance(algorithm);
	}
	

	/*************
	 *  Digest handling methods
	 * 
	 */
	
	public String getDigest (String text) throws Exception
	{	
		// Calculate Digest
		messageDigest.update(text.getBytes("UTF8"));
	    byte [] digestCalculated = messageDigest.digest();
		
	    // Converting to HEX
	    StringBuffer bufDigest = new StringBuffer();
	    for(int i = 0; i < digestCalculated.length; i++) {
	       String hex = Integer.toHexString(0x0100 + (digestCalculated[i] & 0x00FF)).substring(1);
	       bufDigest.append((hex.length() < 2 ? "0" : "") + hex);
	    }
	    
		return bufDigest.toString();
	}
	
	public static boolean compareDigest (String digest1, String digest2)
	{
		if(digest1.equals(digest2))
			return true;
		else
			return false;
	}

}
