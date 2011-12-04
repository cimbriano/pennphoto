package edu.pennphoto.model;

public class Student extends User {
	
	private String major;
	private double gpa;

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
