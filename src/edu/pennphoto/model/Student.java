package edu.pennphoto.model;

import java.util.Date;

public class Student extends User {
	
	private String major;
	private double gpa;
	private int advisorId;
	
	public Student(int userID, String email, String firstName, String lastName,
			Date dob, Gender gender, String address, String city, String state,
			String zip){
		super(userID, email, firstName, lastName, dob, gender, address, city, state, zip);
	}
	
	public Student(){
		super();
	}
	
	

	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
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
