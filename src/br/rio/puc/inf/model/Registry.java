package br.rio.puc.inf.model;

import java.util.Date;

public class Registry {
	
	private Integer ID;
	
	private Integer messageID;
	
	private Date time;
	
	// Public Constructor
	public Registry() {
		
	}

	
	// Getters & Setters
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Integer getMessageID() {
		return messageID;
	}

	public void setMessageID(Integer messageID) {
		this.messageID = messageID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
