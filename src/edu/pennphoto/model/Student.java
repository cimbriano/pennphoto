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

	@Override
	public String toString() {
		return "\nStudent [userID=" + userID + ", password=" + password + ", email="
				+ email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", dob=" + DOB_FORMAT.format(dob) + ", gender=" + gender + ", address="
				+ address + ", city=" + city + ", state=" + state
				+ ", stateId=" + stateId + ", zip=" + zip + ", attendances="
				+ attendances + ", circles=" + circles + ", interestIds="
				+ interests + " major=" + major + ", gpa=" + gpa + ", advisorId="
				+ advisorId + "]";
	}

	
}
