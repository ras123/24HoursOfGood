package com.timeline.project.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.james.mime4j.field.datetime.DateTime;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Event {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private int userId;
	@Persistent
	private DateTime startDateTime;
	@Persistent
	private DateTime endDateTime;
	@Persistent
	private String notes;
	@Persistent
	private String colourCode;
	@Persistent 
	private String postSecondaryName;
	
	public Event(int userId, DateTime startDateTime, DateTime endDateTime) {
		this.userId = userId;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}
	
	public Event(int userId, DateTime startDateTime) {
		this.userId = userId;
		this.startDateTime = startDateTime;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public DateTime getstartDateTime() {
		return startDateTime;
	}
	
	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	public DateTime getendDateTime() {
		return endDateTime;
	}
	
	public void setEndDateTime(DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getColorCode() {
		return colourCode;
	}
	
	public void setColorCode(String colourCode) {
		this.colourCode = colourCode;
	}
}
