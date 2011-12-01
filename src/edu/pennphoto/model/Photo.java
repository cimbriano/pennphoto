package edu.pennphoto.model;

import java.util.ArrayList;

public class Photo {
	private int photoID;
	private String url;
	private boolean isPrivate;
	
	private ArrayList<Integer> viewCircleIDs;
	private ArrayList<Integer> viewUserIDs;
	
	public Photo(int photoID, String url, boolean isPrivate) {
		super();
		this.photoID = photoID;
		this.url = url;
		this.isPrivate = isPrivate;
		viewCircleIDs = new ArrayList<Integer>();
		viewUserIDs = new ArrayList<Integer>();
	}

	public int getPhotoID() {
		return photoID;
	}

	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public ArrayList<Integer> getViewCircleIDs() {
		return viewCircleIDs;
	}

	public void setViewCircleIDs(ArrayList<Integer> viewCircleIDs) {
		this.viewCircleIDs = viewCircleIDs;
	}

	public ArrayList<Integer> getViewUserIDs() {
		return viewUserIDs;
	}

	public void setViewUserIDs(ArrayList<Integer> viewUserIDs) {
		this.viewUserIDs = viewUserIDs;
	}
	
	public boolean addViewUserID(int userID){
		return viewUserIDs.add(userID);
	}
	
	public boolean addViewCircleID(int circleID){
		return viewCircleIDs.add(circleID);
	}
	public boolean removeViewUserID(Integer userID){
		return viewUserIDs.remove(userID);
	}
	
	public boolean removeViewCircleID(Integer circleID){
		return viewCircleIDs.remove(circleID);
	}	
	
}
