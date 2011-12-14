package edu.pennphoto.model;

public class Tag {
	private int photoID;
	private int userID;
	private String tagText;
	
	
	public Tag(int photoID, int userID, String tagText) {
		super();
		this.photoID = photoID;
		this.userID = userID;
		this.tagText = tagText;
	}
	
	public Tag(){}
	
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
	public String getTagText() {
		return tagText;
	}
	public void setTagText(String tagText) {
		this.tagText = tagText;
	}

	@Override
	public String toString() {
		return "Tag [photoID=" + photoID + ", userID=" + userID + ", tagText="
				+ tagText + "]";
	}
	
}
