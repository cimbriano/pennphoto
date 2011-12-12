package edu.pennphoto.model;

public class Event {
	
	public enum EventType{ 
		PHOTO, TAG
	}
	
	private int photoId;
	private int userId;
	
	/**
	 * Hold photo url if event type is PHOTO, otherwise holds tag text
	 */
	private String eventValue;
	private EventType type;
	
	
	public Event(){
		super();
	}


	public Event(int photoId, int userId, String eventValue, EventType type) {
		super();
		this.photoId = photoId;
		this.userId = userId;
		this.eventValue = eventValue;
		this.type = type;
	}

	public int getPhotoId() {
		return photoId;
	}


	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getEventValue() {
		return eventValue;
	}


	public void setEventValue(String eventValue) {
		this.eventValue = eventValue;
	}


	public EventType getType() {
		return type;
	}


	public void setType(EventType type) {
		this.type = type;
	}
}
