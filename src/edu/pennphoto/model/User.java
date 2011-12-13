package edu.pennphoto.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class User {

	public enum Gender{ 
		MALE{
			public String toString(){ return "m";}
		},
		FEMALE{
			public String toString(){ return "f"; }
		}
		
	}
	
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
	private ArrayList<Interest> interests;
	private static final SimpleDateFormat DOB_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String toString() {
		return "User [userID=" + userID + ", password=" + password + ", email="
				+ email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", dob=" + DOB_FORMAT.format(dob) + ", gender=" + gender + ", address="
				+ address + ", city=" + city + ", state=" + state
				+ ", stateId=" + stateId + ", zip=" + zip + ", attendances="
				+ attendances + ", circles=" + circles + ", interestIds="
				+ interests + "]";
	}

	public boolean addInterest(Interest interest){
		return interests.add(interest);
	}
	
	public boolean removeInterest(Integer interest){
		return interests.remove(interest);
	}
	
	public ArrayList<Interest> getInterests() {
		return interests;
	}

	public void setInterests(ArrayList<Interest> interests) {
		this.interests = interests;
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
		attendances = new ArrayList<Attendance>();
		circles = new ArrayList<Circle>();
		interests = new ArrayList<Interest>();
	}
	
	public User(int userID, String email, String firstName, String lastName,
			Date dob, Gender gender, String address, String city, String state,
			String zip) {
		this();
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
	}
	public static class Interest{
		int id;
		String label;
		
		public Interest(String label){
			this(0, label);
		}
		public Interest(int id, String label){
			this.id = id;
			this.label = label;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
	}
	public static class Attendance{
		private int institutionId;
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

		public int getInstitutionId() {
			return institutionId;
		}

		public void setInstitutionId(int institutionId) {
			this.institutionId = institutionId;
		}

		public Attendance(String institution, int startYear, int endYear) {
			super();
			this.institution = institution;
			this.startYear = startYear;
			this.endYear = endYear;
		}
		
		public Attendance(int institutionId, String institution, int startYear, int endYear) {
			this(institution,startYear, endYear);
			this.institutionId = institutionId;
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
