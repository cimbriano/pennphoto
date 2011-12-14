package edu.pennphoto.model;

public class Rating {
	private int photoID;
	private int userID;
	private int value;
	public Rating(int photoID, int userID, int value) {
		super();
		this.photoID = photoID;
		this.userID = userID;
		this.value = value;
	}
	
	public Rating(){
		
	}
	public int getPhotoID() {
		return photoID;
	}
	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Rating [photoID=" + photoID + ", userID=" + userID + ", value="
				+ value + "]";
	}
	
	
}
