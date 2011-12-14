package edu.pennphoto.model;
import java.util.ArrayList;
import java.util.List;

public class Circle {
	private int circleID;
	private String name;
	private List<Integer> friendIDs;
	public Circle(int circleID, String name) {
		super();
		this.circleID = circleID;
		this.name = name;
		friendIDs = new ArrayList<Integer>();
	}
	
	public Circle(){
		this(0, null);
	}
	public int getCircleID() {
		return circleID;
	}
	public void setCircleID(int circleID) {
		this.circleID = circleID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getFriendIDs() {
		return friendIDs;
	}
	public void setFriendID(List<Integer> friendIDs) {
		this.friendIDs = friendIDs;
	}
	
	public boolean addFriendID(Integer friendID){
		return friendIDs.add(friendID);
	}
	
	public boolean addFriendIDs(List<Integer> friendIds){
		return this.friendIDs.addAll(friendIds);
	}
	
	public boolean removeFriendID(Integer friendID){
		return friendIDs.remove(friendID);
	}

	@Override
	public String toString() {
		return "Circle [circleID=" + circleID + ", name=" + name
				+ ", friendIDs=" + friendIDs + "]";
	}
	
	
	
}
