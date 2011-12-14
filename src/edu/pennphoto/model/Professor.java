package edu.pennphoto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Professor extends User {
	String researchArea;
	String title;
	List<Integer> adviseeIds = new ArrayList<Integer>();
	
	public Professor(int userID, String email, String firstName, String lastName,
			Date dob, Gender gender, String address, String city, String state,
			String zip){
		super(userID, email, firstName, lastName, dob, gender, address, city, state, zip);
	}
	
	public Professor(){
		super();
	}
	
	public List<Integer> getAdviseeIds(){
		return adviseeIds;
	}
	
	public boolean addAdviseeId(Integer id){
		return adviseeIds.add(id);
	}
	
	public boolean removeAdviseeId(Integer id){
		return adviseeIds.remove(id);
	}
	
	public void setAdviseeIds(List<Integer> ids){
		adviseeIds = ids;
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

	@Override
	public String toString() {
		
			return "\nProfessor [userID=" + userID + ", password=" + password + ", email="
					+ email + ", firstName=" + firstName + ", lastName=" + lastName
					+ ", dob=" + DOB_FORMAT.format(dob) + ", gender=" + gender + ", address="
					+ address + ", city=" + city + ", state=" + state
					+ ", stateId=" + stateId + ", zip=" + zip + ", attendances="
					+ attendances + ", circles=" + circles + ", interestIds="
					+ interests + "researchArea=" + researchArea + ", title=" + title
							+ ", adviseeIds=" + adviseeIds + "]";
		
	}




}
