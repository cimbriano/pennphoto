package edu.pennphoto.etl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import edu.pennphoto.model.Professor;
import edu.pennphoto.model.Student;
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		parseXML("DataExchange/pennphoto-8.xml");
		parseXML("DataExchange/pennphoto-11.xml");
		parseXML("DataExchange/pennphoto-12.xml");
		
	}
	
	private static Document parseXML(String filename){
		System.out.println("Parsing: " + filename);
		Document doc = null;
		try {
			File file = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nodes = doc.getElementsByTagName("student");
			if(nodes.getLength() == 0)	nodes = doc.getElementsByTagName("tns:student");
			System.out.println(nodes.getLength());
			System.out.println("students-----------------------");
			for(int i = 0; i < nodes.getLength(); i++){
				Node student = nodes.item(i);
				createUser(student);
			}
			nodes = doc.getElementsByTagName("professor");
			if(nodes.getLength() == 0)	nodes = doc.getElementsByTagName("tns:professor");
			System.out.println(nodes.getLength());
			System.out.println("professors-----------------------");
			for(int i = 0; i < nodes.getLength(); i++){
				Node professor = nodes.item(i);
				createUser(professor);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
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
				//TODO how to add interests
			} else if (nodeNameMatches(field, MAJOR)){
				((Student) user).setMajor(getTextValue(field));
			} else if (nodeNameMatches(field, GPA)){
				((Student) user).setGpa(getDoubleValue(field));
			} else if (nodeNameMatches(field, RESEARCH_AREA)){
				((Professor) user).setResearchArea(getTextValue(field));
			} else if (nodeNameMatches(field, TITLE)){
				((Professor) user).setTitle(getTextValue(field));
			}
		}
		System.out.println(user);
		return user;
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
		return node.getChildNodes().item(0).getNodeValue();
		
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


}
