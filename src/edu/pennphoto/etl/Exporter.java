package edu.pennphoto.etl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import edu.pennphoto.model.*;
import edu.pennphoto.model.User.Gender;

public class Exporter {

	public static final String TAB = "    ";
	public static final String TAB2 = TAB + TAB;
	public static final String TAB3 = TAB2 + TAB;
	private static final String START = "<tns:";
	private static final String END = "</tns:";
	private static final String PROF = "professor>\n";
	private static final String STUD = "student>\n";
	private static final String ID = "id>";
	private static final String FNAME = "first_name>";
	private static final String LNAME = "last_name>";
	private static final String DOB = "dob>";
	private static final String STADD = "street_address>";
	private static final String CITY = "city>";
	private static final String STATE = "state>";
	private static final String ZIP = "zip_code>";
	private static final String ISPROF = "is_professor>";
	private static final String GENDER = "gender>";
	private static final String INTEREST = "interest>";
	private static final String ATTEND = "attended>";
	private static final String NAME = "name>";
	private static final String ATTEND_START = "start_year>";
	private static final String ATTEND_END = "end_year>";
	private static final String CIRCLE = "circle>";
	private static final String FRIEND_ID = "friend_id>";
	private static final String RESEARCH = "research_area>";
	private static final String TITLE = "title>";
	private static final String ADVISEE_ID = "advisee_id>";
	private static final String MAJOR = "major>";
	private static final String GPA = "gpa>";
	private static final String ADVISOR_ID = "advisor_id>";
	
	private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
						"<tns:photodb xmlns:tns=\"pennphoto17ns\"\n" +
						TAB + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
						TAB + "xsi:schemaLocation=\"pennphoto17ns pennphoto17.xsd \">\n";	
	private static final String FOOTER = "</tns:photodb>\n";
	
	public static void main(String[] args){
		User user1 = new Professor(1, "user1@penn.edu", "First 1", "Last 1", new Date(), 
				Gender.MALE, "Primero Street", "Isa City", "NY", "10001");
		System.out.println(export(user1));
	}
	
	public static String export(List<User> users){
		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append(HEADER);
		for(User user : users){
			boolean isProfessor = user instanceof Professor;
			xmlBuilder.append(START + (isProfessor ? PROF : STUD));
			xmlBuilder.append(TAB + START + ID + user.getUserID() + END + ID + "\n");
			xmlBuilder.append(TAB + START + FNAME + user.getFirstName() + END + FNAME + "\n");
			xmlBuilder.append(TAB + START + LNAME + user.getLastName() + END + LNAME + "\n");
			xmlBuilder.append(TAB + START + DOB + user.getDob() + END + DOB + "\n");
			xmlBuilder.append(TAB + START + STADD + user.getAddress() + END + STADD + "\n");
			xmlBuilder.append(TAB + START + CITY + user.getCity() + END + CITY + "\n");
			xmlBuilder.append(TAB + START + STATE + user.getState() + END + STATE + "\n");
			xmlBuilder.append(TAB + START + ZIP + user.getZip() + END + ZIP + "\n");
			xmlBuilder.append(TAB + START + ISPROF + isProfessor + END + ISPROF + "\n");
			xmlBuilder.append(TAB + START + GENDER + user.getGender() + END + GENDER + "\n");
			appendInterests(xmlBuilder, user);
			appendAttendances(xmlBuilder, user);
			appendPhotos(xmlBuilder, user);
			appendCircles(xmlBuilder, user);
			if(isProfessor){
				appendProfAttributes(xmlBuilder, (Professor) user);
			} else {
				appendStudAttributes(xmlBuilder, (Student) user);
			}

			xmlBuilder.append(END + (isProfessor ? PROF : STUD));
		}
		xmlBuilder.append(FOOTER);
		return xmlBuilder.toString();
	}

	private static void appendInterests(StringBuilder xmlBuilder, User user) {
		for(Integer intId : user.getInterestIDs()){
			//TODO replace intId with actual Interest name
			xmlBuilder.append(TAB + START + INTEREST + intId + END + INTEREST + "\n");
		}
	}
	
	private static void appendAttendances(StringBuilder xmlBuilder, User user){
		for(User.Attendance att: user.getAttendances()){
			xmlBuilder.append(TAB2 + START + ATTEND + "\n");
			xmlBuilder.append(TAB3 + START + NAME + att.getInstitution() + END + NAME + "\n");
			xmlBuilder.append(TAB3 + START + ATTEND_START + att.getStartYear() + END + ATTEND_START + "\n");
			xmlBuilder.append(TAB3 + START + ATTEND_END + att.getEndYear() + END + ATTEND_END + "\n");
			xmlBuilder.append(TAB2 + TAB + END + ATTEND + "\n");
		}
	}
	
	private static void appendPhotos(StringBuilder xmlBuilder, User user){
		//TODO implement appendPhotos()
	}
	
	private static void appendCircles(StringBuilder xmlBuilder, User user){
		for(Circle circle : user.getCircles()){
			xmlBuilder.append(TAB2 + START + CIRCLE +"\n");
			xmlBuilder.append(TAB3 + START + ID + circle.getCircleID() + END + ID + "\n");
			xmlBuilder.append(TAB3 + START + NAME + circle.getName() + END + NAME + "\n");
			for(Integer friendId : circle.getFriendIDs()){
				xmlBuilder.append(TAB3 + START + FRIEND_ID + friendId + END + FRIEND_ID + "\n");
			}
			xmlBuilder.append(TAB2 + END + CIRCLE +"\n");
		}
	}
	
	private static void appendStudAttributes(StringBuilder xmlBuilder, Student student) {
		xmlBuilder.append(TAB2 + START + MAJOR + student.getMajor() + END + MAJOR + "\n");
		xmlBuilder.append(TAB2 + START + GPA + student.getGpa() + END + GPA + "\n");
		xmlBuilder.append(TAB2 + START + ADVISOR_ID + student.getAdvisorId() + END + ADVISOR_ID + "\n");
	}

	private static void appendProfAttributes(StringBuilder xmlBuilder, Professor professor) {
		xmlBuilder.append(TAB2 + START + RESEARCH + professor.getResearchArea() + END + RESEARCH + "\n");
		xmlBuilder.append(TAB2 + START + TITLE + professor.getTitle() + END + TITLE + "\n");
		for(Integer id : professor.getAdviseeIds()){
			xmlBuilder.append(TAB2 + START + ADVISEE_ID + id + END + ADVISEE_ID + "\n");
		}
	}

	public static String export(User user){
		ArrayList<User> list = new ArrayList<User>();
		list.add(user);
		return export(list);
	}
}
