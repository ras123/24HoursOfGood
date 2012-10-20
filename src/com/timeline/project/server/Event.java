package com.timeline.project.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.apphosting.api.DatastorePb.GetResponse.Entity;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Event extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2395577088378612431L;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private Long userId;
	@Persistent
	private Date startDateTime;
	@Persistent
	private Date endDateTime;
	@Persistent
	private String notes;
	@Persistent
	private String colourCode;
	@Persistent 
	private String postSecondaryName;
	@Persistent
	private String title;
	@Persistent
	private String eventType;
	
	public Event(Key createKey) {
		super();
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getstartDateTime() {
		return startDateTime;
	}
	
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	public Date getendDateTime() {
		return endDateTime;
	}
	
	public void setEndDateTime(Date endDateTime) {
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
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getEventType() {
		return eventType;
	}
	
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String getPostSecondaryName() {
		return postSecondaryName;
	}
	
	public void setPostSecondaryName(String postSecondaryName) {
		this.postSecondaryName = postSecondaryName;
	}
}
