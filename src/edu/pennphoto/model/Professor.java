package edu.pennphoto.model;

import java.util.Date;

import edu.pennphoto.model.User.Gender;

public class Professor extends User {
	String researchArea;
	String title;
	
	public Professor(int userID, String email, String firstName, String lastName,
			Date dob, Gender gender, String address, String city, String state,
			String zip){
		super(userID, email, firstName, lastName, dob, gender, address, city, state, zip);
	}
	
	public Professor(){
		super();
	}

	public String getResearchArea() {
		return researchArea;
	}

	public void setResearchArea(String researchArea) {
		this.researchArea = researchArea;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString(){
		return super.toString() + "\n" + title + "\n"+researchArea;
	}
}
