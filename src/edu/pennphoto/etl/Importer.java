package edu.pennphoto.etl;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import edu.pennphoto.db.UserDAO;
import edu.pennphoto.model.AdvisingMap;
import edu.pennphoto.model.Circle;
import edu.pennphoto.model.Photo;
import edu.pennphoto.model.Professor;
import edu.pennphoto.model.Rating;
import edu.pennphoto.model.Student;
import edu.pennphoto.model.Tag;
import edu.pennphoto.model.User;
import edu.pennphoto.model.User.Gender;



/**Import data from groups 8, 11 and 12
 * @author Mark
 *
 */
public class Importer {
	
	private static final String STUDENT = "student";
	private static final String[] USERID = {"userID", "userId", "tns:accountID"};
	private static final String[] EMAIL = {"email", "tns:email"};
	private static final String[] FIRST_NAME = {"tns:fname", "firstName"};
	private static final String[] LAST_NAME = {"tns:lname", "lastName"};
	private static final String[] COMBINED_NAME = {"name"};
	private static final String[] DOB = {"birthday", "age", "tns:birthDate"};
	private static final SimpleDateFormat DOB_FORMAT1 = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat DOB_FORMAT2 = new SimpleDateFormat("MM/dd/yyyy");
	private static final String[] GENDER = {"gender", "tns:gender"};
	private static final String[] ADDRESS = {"address", "tns:address"};
	private static final String[] MAJOR = {"major"};
	private static final String[] GPA = {"GPA"};
	private static final String[] RESEARCH_AREA = {"researchArea"};
	private static final String[] TITLE = {"title"};
	private static final String[] INTEREST = {"interest", "tns:interest"};
	private static final String[] ATTENDANCE = {"school", "tns:schoolsAttended"};
	private static final String[] SCHOOL_NAME = {"tns:name"};
	private static final String[] PHOTO = {"photo", "tns:photo"};
	private static final String[] PHOTO_ID = {"photoId", "photoID", "tns:photoID"};
	private static final String[] URL = {"url", "tns:url"};
	private static final String[] RATING = {"tns:rating", "rating"};
	private static final String[] RATER_ID ={"tns:raterID", "userId"};
	private static final String[] RATING_VALUE = {"score", "tns:score"};
	private static final String[] TAG = {"tag", "tns:tag"};
	private static final String[] TAG_TEXT = {"tns:text"};
	private static final String[] PHOTO_OWNER_ID = {"ownerID"};
	private static final String[] CIRCLE = {"circle", "tns:circle"};
	private static final String[] CIRCLE_ID = {"circleID", "circleId", "tns:circleID"};
	private static final String[] CIRCLE_NAME = {"name", "tns:name"};
	private static final String[] OWNER_ID = {"ownerID"};
	private static final String[] CONTAINS_FRIEND = {"containsFriend", "friendID", "tns:friend"};
	private static final String[] ADVISOR = {"tns:advisor", "advisor"};
	private static final String PHOTO_11 = "photo";
	private static final String CIRCLE_11 = "circle";
	private static final String FRIEND_11 = "belongsTo";
	
	private static final int[] GROUP_IDS = {8, 11, 12};
	
	private static HashMap<Integer, User> _users;
	private static ArrayList<Photo> _photos;
	private static HashMap<Integer, Circle> _circles;
	private static Document _doc;
	private static boolean _photosLoaded;
	private static boolean _circlesLoaded;
	private static boolean _friendsLoaded;
	private static int _current_group_id;
	private static AdvisingMap _advisingMap;
	


	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i : GROUP_IDS){
			_current_group_id = i;
			String filename = "DataExchange/pennphoto-" + _current_group_id + ".xml";
			System.out.println("\n\n\nImporting group " + _current_group_id);
			parseXML(filename);
		}
	}
	
	private static void initializeVariables(){
		_users = new HashMap<Integer, User>();
		_photos = new ArrayList<Photo>();
		_circles = new HashMap<Integer, Circle>();
		_advisingMap = new AdvisingMap();
		_photosLoaded = false;
		_circlesLoaded = false;
		_friendsLoaded = false;
	}
	
	private static void parseXML(String filename){
		System.out.println("Parsing: " + filename + "*******************************");
		initializeVariables();
		try {
			File file = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			_doc = dBuilder.parse(file);
			_doc.getDocumentElement().normalize();
			
			loadUsersFromDoc();
			if(!_photosLoaded) loadPhotosFromDoc();
			if(!_circlesLoaded) loadCirclesFromDoc();
			if(!_friendsLoaded) loadFriendsFromDoc();
			
			System.out.println(_users.values());
			System.out.println(_photos);
			System.out.println(_circles.values());
			//System.out.println( _advisingMap);

	/*		storeUsers(users);
			storeCircles(users);
			storePhotos();
			storeTags();
			storeRatings();
			storeAdvises();
	*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void storeUsers(List<User> users){
		for(User user : users){
			try {
				UserDAO.createUser(user);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void storeCircles(List<User> users){
		//TODO implement createCircles
//		UserDAO.createCircle(userId, name);
	}
	

	
	private static User createUser(Node userNode){
		User user;
		boolean isProfessor = !userNode.getNodeName().contains(STUDENT);
		if(isProfessor){
			user = new Professor();
		} else {
			user = new Student();
		}
		NodeList fields = userNode.getChildNodes();
		for(int j = 0; j < fields.getLength(); j++){
			Node field = fields.item(j);
			if(nodeNameMatches(field, USERID)){
				user.setUserID(getIntValue(field));
			} else if(nodeNameMatches(field, EMAIL)){
				user.setEmail(getTextValue(field));
			} else if (nodeNameMatches(field, COMBINED_NAME)){
				setCombinedName(field, user);
			} else if (nodeNameMatches(field, FIRST_NAME)){
				user.setFirstName(getTextValue(field));
			} else if (nodeNameMatches(field, LAST_NAME)){
				user.setLastName(getTextValue(field));
			} else if  (nodeNameMatches(field, DOB)){
				user.setDob(getDobDateValue(field));
			} else if (nodeNameMatches(field, GENDER)){
				user.setGender(getGenderValue(field));
			} else if (nodeNameMatches(field, ADDRESS)){
				setAddress(field, user);
			} else if (nodeNameMatches(field, INTEREST)) {
				user.addInterest(new User.Interest(getTextValue(field)));
			} else if (nodeNameMatches(field, ATTENDANCE)){
				addAttendance(field, user);
			} else if (nodeNameMatches(field, MAJOR)){
				((Student) user).setMajor(getTextValue(field));
			} else if (nodeNameMatches(field, GPA)){
				((Student) user).setGpa(getDoubleValue(field));
			} else if (nodeNameMatches(field, ADVISOR)){
				((Student) user).setAdvisorId(getUniqueId(getIntValue(field)));
			} else if (nodeNameMatches(field, RESEARCH_AREA)){
				((Professor) user).setResearchArea(getTextValue(field));
			} else if (nodeNameMatches(field, TITLE)){
				((Professor) user).setTitle(getTextValue(field));
			} else if (nodeNameMatches(field, PHOTO)){
				_photosLoaded = true;
				loadPhoto(field, user);
			} else if (nodeNameMatches(field, CIRCLE)){
				loadCircle(field, user);
			}
		}
		return user;
	}
	
	private static void loadCircle(Node node, User user){
		NodeList circleChildren = node.getChildNodes();
		Circle circle = new Circle();
		for(int i = 0; i < circleChildren.getLength(); i++){
			Node circleChild = circleChildren.item(i);
			if(nodeNameMatches(circleChild, CIRCLE_ID)){
				circle.setCircleID(getUniqueId(getIntValue(circleChild)));
			} else if (nodeNameMatches(circleChild, CIRCLE_NAME)){
				circle.setName(getTextValue(circleChild));
			} else if (nodeNameMatches(circleChild, OWNER_ID)){
				user = _users.get(getUniqueId(getIntValue(circleChild)));
			}
		}
		_circlesLoaded = true;
		_circles.put(circle.getCircleID(), circle);
		user.addCircle(circle);
		loadFriend(node, circle);
	}
	
	private static void loadFriend(Node node, Circle circle){
		NodeList friends = node.getChildNodes();
		ArrayList<Integer> friendIds = new ArrayList<Integer>();
		int circleId = (circle != null) ? circle.getCircleID() : 0;
		for(int i = 0; i < friends.getLength(); i++){
			Node friend = friends.item(i);
			if(nodeNameMatches(friend, CONTAINS_FRIEND)){
				_friendsLoaded = true;
				friendIds.add(getUniqueId(getIntValue(friend)));
			} else if (nodeNameMatches(friend, CIRCLE_ID)){
				circleId = getUniqueId(getIntValue(friend));
			}
		}
		_circles.get(circleId).addFriendIDs(friendIds);
	}

	


	private static boolean nodeNameMatches(Node node, String[] tags){
		String name = node.getNodeName();
		for(String element : tags){
			if(element.equals(name)) return true;
		}
		return false;
	}

	private static int getIntValue(Node node){
		int retVal = 0;
		retVal = Integer.parseInt(node.getChildNodes().item(0).getNodeValue());
		return retVal;
	}
	private static String getTextValue(Node node){
		
		String retString = null;
		try{
			retString = node.getChildNodes().item(0).getNodeValue().trim();
		} catch (NullPointerException n) {

		}
		return retString;
	}
	
	private static Date getDobDateValue(Node node) {
		String textValue = getTextValue(node);
		Date date;
		try {
			date = DOB_FORMAT1.parse(textValue);
		} catch (ParseException e) {
			try {
				date = DOB_FORMAT2.parse(textValue);
			} catch (ParseException e1) {
				int offset = getIntValue(node);
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - offset);
				date = cal.getTime();
			}
		}
		return date;
	}
	
	private static void setCombinedName(Node node, User user){
		String[] names = getTextValue(node).split(" ");
		user.setFirstName(names[0]);
		if(names.length > 1){
			user.setLastName(names[1]);
		} else user.setLastName(names[0]);
	}
	
	private static Gender getGenderValue(Node node){
		if(getTextValue(node).toLowerCase().contains("m")){
			return Gender.MALE;
		} else {
			return Gender.FEMALE;
		}
	}
	
	private static void setAddress(Node node, User user){
		String[] address = getTextValue(node).split(",");
		if(address.length == 2){ // just [city, state]
			user.setCity(address[0]);
			user.setState(address[1]);
		} else if (address.length == 3){ // [street, city, state zip]
			user.setAddress(address[0].trim());
			user.setCity(address[1].trim());
			String[] stateZip = address[2].trim().split(" ");
			user.setState(stateZip[0]);
			if(stateZip.length > 1) user.setZip(stateZip[1]);
		}
	}
	
	private static double getDoubleValue(Node node){
		return Double.parseDouble(getTextValue(node));
	}

	private static void loadUsersFromDoc(){
		System.out.println("Root element :" + _doc.getDocumentElement().getNodeName());
		NodeList nodes = _doc.getElementsByTagName("student");
		if(nodes.getLength() == 0)	nodes = _doc.getElementsByTagName("tns:student");
		for(int i = 0; i < nodes.getLength(); i++){
			Node student = nodes.item(i);
			User user = (createUser(student));
			_users.put(user.getUserID(), user);
		}
		nodes = _doc.getElementsByTagName("professor");
		if(nodes.getLength() == 0)	nodes = _doc.getElementsByTagName("tns:professor");
		for(int i = 0; i < nodes.getLength(); i++){
			Node professor = nodes.item(i);
			User user = (createUser(professor));
			_users.put(user.getUserID(), user);
		}
	}
	
	private static void addAttendance(Node node, User user){
		String nodeName = node.getNodeName();
		if(nodeName.equals(ATTENDANCE[0])){ // team 8's <school> tag
			user.addAttendance(new User.Attendance(getTextValue(node), 0, 0));
		} else if (nodeName.equals(ATTENDANCE[1])){
			NodeList nl = node.getChildNodes();
			for(int i = 0; i < nl.getLength(); i++){
				Node n = nl.item(i);
				if(nodeNameMatches(n, SCHOOL_NAME)){
					user.addAttendance(new User.Attendance(getTextValue(n), 0, 0));
				}
			}
		}
	}
	
	private static void loadPhotosFromDoc(){
		NodeList nodes = _doc.getElementsByTagName(PHOTO_11);
		for(int i = 0; i < nodes.getLength(); i++){
			loadPhoto(nodes.item(i), null);
		}
	}
	
	private static void loadCirclesFromDoc(){
		NodeList nodes = _doc.getElementsByTagName(CIRCLE_11);
		for(int i = 0; i < nodes.getLength(); i++){
			loadCircle(nodes.item(i), null);
		}
	}
	
	
	private static void loadFriendsFromDoc(){
		NodeList nodes = _doc.getElementsByTagName(FRIEND_11);
		for(int i = 0; i < nodes.getLength(); i++){
			loadFriend(nodes.item(i), null);
		}
	}
	
	private static void loadPhoto(Node node, User user){
		NodeList nl = node.getChildNodes();
		String url = null;
		int userId = (user != null) ? user.getUserID() : 0;
		int photoId = 0;
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(nodeNameMatches(n, PHOTO_ID)){
				photoId = getUniqueId(getIntValue(n));
			} else if (nodeNameMatches(n, URL)){
				url = getTextValue(n);
			} else if (nodeNameMatches(n, PHOTO_OWNER_ID)){
				userId = getUniqueId(getIntValue(n));
			}
		}
		//public Photo(Integer photoId, String url, boolean isPrivate, int ownerId, Date uploadDate)
		Photo photo = new Photo(photoId, url, false, userId, new Date()); 
		_photos.add(photo);
		loadTagsAndRatingsFromPhoto(node, photo);		
	}
	
	private static void loadTagsAndRatingsFromPhoto(Node node, Photo photo){
		NodeList nl = node.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(nodeNameMatches(n, RATING)){
				Rating rating = new Rating();
				rating.setPhotoID(photo.getPhotoId());
				NodeList ratingChildren = n.getChildNodes();
				for(int j = 0; j < ratingChildren.getLength(); j++){
					Node ratingChild = ratingChildren.item(j);
					if(nodeNameMatches(ratingChild, RATER_ID)){
						rating.setUserID(getUniqueId(getIntValue(ratingChild)));
					} else if (nodeNameMatches(ratingChild, RATING_VALUE)){
						rating.setValue(getIntValue(ratingChild));
					}
				}
				photo.addRating(rating);
			} else if (nodeNameMatches(n, TAG)){
				Tag tag = new Tag();
				tag.setPhotoID(photo.getPhotoId());
				if(_current_group_id != 8){
					NodeList tagChildren = n.getChildNodes();
					for(int j = 0; j < tagChildren.getLength(); j++){
						Node tagChild = tagChildren.item(j);
						if(nodeNameMatches(tagChild, TAG_TEXT)){
							tag.setTagText(getTextValue(tagChild));
						}
					}
				} else {
					tag.setTagText(getTextValue(n));
				}
				photo.addTag(tag);
			}
		}
	}
	
	
	private static int getUniqueId(int id){
		int base = _current_group_id * 1000;	//get the base value for this particular group
		int uniqueId = (id >= base) ? id : id + base;
		return uniqueId;
	}
}
