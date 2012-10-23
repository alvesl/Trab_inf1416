package br.rio.puc.inf.model;

import java.security.PrivateKey;

import br.rio.puc.inf.control.instruments.Digest;

public class User {

	private Integer ID;
	
	private String fullName;
	
	private String username;
	
	private Integer groupID;
	
	private String password;
	
	private String publicKey;
	
	// Password to be inserted into Database
	private String dbPassword;
	
	// Number of times user loged in the system
	private Integer numLoged;
	
	private PrivateKey pkey;
	
	/***********
	 * Public Constructors
	 * 
	 */
	public User() {
		
	}
	
	public User(String username, String fullName, Integer groupID, String password, String publicKey) {
		
		this.username = username;
		this.fullName = fullName;
		this.groupID = groupID;
		this.password = password;
		this.publicKey = publicKey;
		this.numLoged = 0;
	}
	
	public User(String username, String fullName, Integer groupID, String password, String publicKey, Integer numLoged) {
		
		this.username = username;
		this.fullName = fullName;
		this.groupID = groupID;
		this.password = password;
		this.publicKey = publicKey;
		this.numLoged = 0;
	}
	
	public User(int ID, String username, String fullName, Integer groupID, String dbPassword, String publicKey, Integer numLoged) {
		
		this.ID = ID;
		this.username = username;
		this.fullName = fullName;
		this.groupID = groupID;
		this.dbPassword = dbPassword;
		this.publicKey = publicKey;
		this.numLoged = numLoged;
	}

	
	/***********
	 * User methods
	 * 
	 */
	
	// Check Password length and strength
	public static boolean checkpassword(String passwd)
	{
		// TODO Verificar tamanho e forca da senha
		return true;
	}
	
	// Generate passaword to be inserted in Db
	public static String generateDbPassword(User user, String algorithm) throws Exception
	{
		Digest digest = new Digest(algorithm);
		
		String temp = user.getPassword() + Integer.toString(user.getID()) + user.getUsername();
		//System.out.println(temp);
		
		return digest.getDigest(temp);
	}
	
	/***********
	 * Getters and Setters
	 * 
	 */
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String userName) {
		this.username = userName;
	}
	public Integer getGroupID() {
		return groupID;
	}
	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public Integer getNumLoged() {
		return numLoged;
	}
	public void setNumLoged(Integer numLoged) {
		this.numLoged = numLoged;
	}

	public PrivateKey getPkey() {
		return pkey;
	}

	public void setPkey(PrivateKey pkey) {
		this.pkey = pkey;
	}

	
}
