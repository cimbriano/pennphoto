package edu.pennphoto.etl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.pennphoto.db.PhotoDAO;
import edu.pennphoto.db.UserDAO;
import edu.pennphoto.model.*;
import edu.pennphoto.model.User.Gender;
import edu.pennphoto.model.User.Interest;

public class Exporter {

	public static final String TAB = "    ";
	public static final SimpleDateFormat DOB_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat UPLOAD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	private static final String TAB2 = TAB + TAB;
	private static final String TAB3 = TAB2 + TAB;
	private static final String TAB4 = TAB3 + TAB;
	private static final String START = "<tns:";
	private static final String END = "</tns:";
	private static final String PROF = "professor>\n";
	private static final String STUD = "student>\n";
	private static final String ID = "id>";
	private static final String EMAIL = "email>";
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
	private static final String PHOTO = "photo>\n";
	private static final String URL = "url>";
	private static final String PRIVATE = "is_private>";
	private static final String UPLOAD_DATE = "upload_date>";
	private static final String RATING = "rating>\n";
	private static final String USER_ID = "user_id>";
	private static final String VALUE = "value>";
	private static final String TAG = "tag>\n";
	private static final String TAG_TEXT = "tag_text>";
	private static final String CIRCLE_ID = "circle_id>";
	private static final String VIEWABLE_TO = "visible_to>";
	
	private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
						"<tns:photodb xmlns:tns=\"pennphoto17ns\"\n" +
						TAB + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
						TAB + "xsi:schemaLocation=\"pennphoto17ns pennphoto17.xsd\">\n";	
	private static final String FOOTER = "</tns:photodb>\n";
	
	public static void main(String[] args){
		
	}
	
	public static String getExportXML(){
		return export(UserDAO.getUsersList());
	}
	
	/**Returns an XML export document for a list of users
	 * 
	 * @param users
	 * @return XML export document
	 */
	public static String export(List<User> users){
		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append(HEADER);
		for(User user : users){
			boolean isProfessor = user instanceof Professor;
			xmlBuilder.append(TAB + START + (isProfessor ? PROF : STUD));
			xmlBuilder.append(TAB2 + START + ID + user.getUserID() + END + ID + "\n");
			xmlBuilder.append(TAB2 + START + EMAIL + user.getEmail() + END + EMAIL + "\n");
			xmlBuilder.append(TAB2 + START + FNAME + user.getFirstName() + END + FNAME + "\n");
			xmlBuilder.append(TAB2 + START + LNAME + user.getLastName() + END + LNAME + "\n");
			xmlBuilder.append(TAB2 + START + DOB + DOB_FORMAT.format(user.getDob()) + END + DOB + "\n");
			xmlBuilder.append(TAB2 + START + STADD + user.getAddress() + END + STADD + "\n");
			xmlBuilder.append(TAB2 + START + CITY + user.getCity() + END + CITY + "\n");
			xmlBuilder.append(TAB2 + START + STATE + user.getState() + END + STATE + "\n");
			xmlBuilder.append(TAB2 + START + ZIP + user.getZip() + END + ZIP + "\n");
			xmlBuilder.append(TAB2 + START + ISPROF + isProfessor + END + ISPROF + "\n");
			xmlBuilder.append(TAB2 + START + GENDER + user.getGender() + END + GENDER + "\n");
			appendInterests(xmlBuilder, user);
			appendAttendances(xmlBuilder, user);				
			appendPhotos(xmlBuilder, PhotoDAO.getUserPhoto(user.getUserID()));			
			appendCircles(xmlBuilder, user);
			if(isProfessor){
				appendProfAttributes(xmlBuilder, (Professor) user);
			} else {
				appendStudAttributes(xmlBuilder, (Student) user);
			}

			xmlBuilder.append(TAB + END + (isProfessor ? PROF : STUD));
		}
		xmlBuilder.append(FOOTER);
		return xmlBuilder.toString();
	}

	private static void appendInterests(StringBuilder xmlBuilder, User user) {
		for(Interest interest : user.getInterests()){
			xmlBuilder.append(TAB + START + INTEREST + interest.getLabel() + END + INTEREST + "\n");
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
	
	private static void appendPhotos(StringBuilder xmlBuilder, List<Photo> photos){
		for(Photo photo : photos){
			boolean isPrivate = photo.isPrivate();
			xmlBuilder.append(TAB2 + START + PHOTO);
			xmlBuilder.append(TAB3 + START + ID + photo.getPhotoId() + END + ID + "\n");
			xmlBuilder.append(TAB3 + START + URL + photo.getUrl() + END + URL + "\n");
			xmlBuilder.append(TAB3 + START + PRIVATE + isPrivate + END + PRIVATE + "\n");
			xmlBuilder.append(TAB3 + START + UPLOAD_DATE + 
					UPLOAD_DATE_FORMAT.format(photo.getUploadDate()) + END + ID + "\n");
			for(Rating rating : photo.getRatings()){
				xmlBuilder.append(TAB3 + START + RATING);
				xmlBuilder.append(TAB4 + START + USER_ID + rating.getUserID() + END + USER_ID + "\n");
				xmlBuilder.append(TAB4 + START + VALUE + rating.getValue() + END + VALUE + "\n");
				xmlBuilder.append(TAB3 + END + RATING);
			}
			for(Tag tag : photo.getTags()){
				xmlBuilder.append(TAB3 + START + TAG);
				xmlBuilder.append(TAB4 + START + USER_ID + tag.getUserID() + END + USER_ID + "\n");
				xmlBuilder.append(TAB4 + START + TAG_TEXT + tag.getTagText() + END + TAG_TEXT + "\n");
				xmlBuilder.append(TAB3 + END + TAG);
			}
			List<Integer> viewCircleIds = photo.getViewCircleIDs();
			List<Integer> viewUserIds = photo.getViewUserIDs();
			if((viewCircleIds.size() + viewUserIds.size()) > 0){
				xmlBuilder.append(TAB3 + START + VIEWABLE_TO);
				for(Integer circleId : viewCircleIds){
					xmlBuilder.append(TAB4 + START + CIRCLE_ID + circleId + END + CIRCLE_ID + "\n");
				}
				for(Integer userId: viewUserIds){
					xmlBuilder.append(TAB4 + START + USER_ID + userId + END + USER_ID + "\n");
				}
				xmlBuilder.append(TAB3 + END + VIEWABLE_TO);
			}
			xmlBuilder.append(TAB2 + END + PHOTO);
		}
	}

	public static String export(User user){
		ArrayList<User> list = new ArrayList<User>();
		list.add(user);
		return export(list);
	}
}
