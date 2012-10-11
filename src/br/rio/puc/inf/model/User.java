package br.rio.puc.inf.model;

public class User {

	private Integer ID;
	
	private String fullName;
	
	private String username;
	
	private Integer groupID;
	
	private String password;
	
	private String publicKey;
	
	// Password to be inserted into Database
	private String dbPassword;
	
	
	// Public Constructors
	public User() {
		
	}
	
	public User(String username, String fullName, int groupID, String password, String publicKey) {
		
		this.username = username;
		this.fullName = fullName;
		this.groupID = groupID;
		this.password = password;
		this.publicKey = publicKey;
	}
	
	// Check Password
	public static boolean checkpassword(String passwd)
	{
		// TODO Verificar forca da senha
		return true;
	}
	
	// Getters & Setters
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

	
}
