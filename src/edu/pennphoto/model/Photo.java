package edu.pennphoto.model;

import java.util.ArrayList;

public class Photo {
	private Integer photoID;
	private String url;
	private boolean isPrivate;
	private int ownerId;
	
	private ArrayList<Integer> viewCircleIDs;
	private ArrayList<Integer> viewUserIDs;
	private ArrayList<Tag> tags;
	private ArrayList<Rating> ratings;
	
	public Photo(String url, boolean isPrivate, int ownerId){
		this(null, url, isPrivate, ownerId);
	}
	
	public Photo(Integer photoID, String url, boolean isPrivate, int ownerId) {
		super();
		this.photoID = photoID;
		this.url = url;
		this.isPrivate = isPrivate;
		this.ownerId = ownerId;
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
	
	public boolean addTag(Tag tag){
		return tags.add(tag);
	}
	
	public boolean removeTag(Tag tag){
		return tags.remove(tag);
	}
	
	public boolean addRating(Rating rating){
		return ratings.add(rating);
	}
	
	public boolean removeRating(Rating rating){
		return ratings.remove(rating);
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(ArrayList<Rating> ratings) {
		this.ratings = ratings;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	
}
