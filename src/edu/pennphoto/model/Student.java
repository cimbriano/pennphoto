package edu.pennphoto.model;

import java.util.Date;

import edu.pennphoto.model.User.Gender;

public class Student extends User {
	
	private String major;
	private double gpa;
	
	Student(int userID, String email, String firstName, String lastName,
			Date dob, Gender gender, String address, String city, String state,
			String zip){
		super(userID, email, firstName, lastName, dob, gender, address, city, state, zip);
	}
	
	Student(){
		super();
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}
	
	public String toString(){
		return super.toString() + "\n" + major + "\n"+gpa;
	}
	
}
