package edu.pennphoto.model;

import java.util.ArrayList;
import java.util.Date;

public abstract class User {

	public enum Gender{ MALE, FEMALE}
	
	private int userID;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private Date dob;
	private Gender gender;
	private String address;
	private String city;
	private String state;
	private int stateId;
	private String zip;

	private ArrayList<Attendance> attendances;
	private ArrayList<Circle> circles;
	private ArrayList<Integer> interestIds;
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(userID).append("\n").
		append(email).append("\n").
		append(password).append("\n").
		append(firstName).append("\n").
		append(lastName).append("\n").
		append(dob).append("\n").
		append(gender).append("\n").
		append(address).append("\n").
		append(city).append("\n").
		append(stateId).append("\n").
		append(zip).append("\n");
		
		return sb.toString();
	}
	public boolean addInterestID(Integer id){
		return interestIds.add(id);
	}
	
	public boolean removeInterestID(Integer id){
		return interestIds.remove(id);
	}
	
	public ArrayList<Integer> getInterestIDs() {
		return interestIds;
	}

	public void setInterestIDs(ArrayList<Integer> interestIDs) {
		this.interestIds = interestIDs;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public ArrayList<Attendance> getAttendances(){
		return attendances;
	}
	
	public ArrayList<Circle> getCircles(){
		return circles;
	}
	
	public boolean addAttendance(Attendance a){
		return attendances.add(a);
	}
	
	public boolean removeAttendance(Attendance a){
		return attendances.remove(a);
	}

	public void setAttendances(ArrayList<Attendance> attendances){
		this.attendances = attendances;
	}
	
	public boolean addCircle(Circle c){
		return circles.add(c);
	}
	
	public boolean removeCircle(Circle c){
		return circles.remove(c);
	}

	public void setCircles(ArrayList<Circle> circles){
		this.circles = circles;
	}	
	
	public User(){
		super();
	}
	
	public User(int userID, String email, String firstName, String lastName,
			Date dob, Gender gender, String address, String city, String state,
			String zip) {
		super();
		this.userID = userID;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.gender = gender;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		attendances = new ArrayList<Attendance>();
		circles = new ArrayList<Circle>();
		interestIds = new ArrayList<Integer>();
	}
	
	public class Attendance{
		private String institution;
		private int startYear;
		private int endYear;
		
		public String getInstitution() {
			return institution;
		}

		public void setInstitution(String institution) {
			this.institution = institution;
		}

		public int getStartYear() {
			return startYear;
		}

		public void setStartYear(int startYear) {
			this.startYear = startYear;
		}

		public int getEndYear() {
			return endYear;
		}

		public void setEndYear(int endYear) {
			this.endYear = endYear;
		}

		public Attendance(String institution, int startYear, int endYear) {
			super();
			this.institution = institution;
			this.startYear = startYear;
			this.endYear = endYear;
		}
		
		
		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	
	
}
