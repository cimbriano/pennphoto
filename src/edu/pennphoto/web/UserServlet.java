package edu.pennphoto.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.apache.catalina.Session;
import org.json.JSONArray;

import edu.pennphoto.db.DBHelper;
import edu.pennphoto.db.PhotoDAO;
import edu.pennphoto.db.UserDAO;
import edu.pennphoto.model.Circle;
import edu.pennphoto.model.Photo;
import edu.pennphoto.model.Professor;
import edu.pennphoto.model.Rating;
import edu.pennphoto.model.Student;
import edu.pennphoto.model.Tag;
import edu.pennphoto.model.User;
import edu.pennphoto.model.User.Gender;
import edu.pennphoto.model.User.Attendance;
import edu.pennphoto.model.User.Interest;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String msg = request.getPathInfo();
		// Checks for /logout
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if (msg == null || user == null) {
			// Error
			response.sendRedirect("login.jsp?error=bp");
		} else if (msg.equals("/logout")) {
			// Logout
			request.getSession().invalidate();
			response.sendRedirect(request.getServletContext().getContextPath()
					+ "/login.jsp");
		} else  if (msg.equals("/profile")) {
			response.sendRedirect(request.getServletContext().getContextPath()
					+ "/profile.jsp");
		}else{
			// Any other
			response.getWriter().write(msg);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user == null){
			if (action.equals("login")) {
				handleLogin(request, response);
			} else if (action.equals("register")) {
				handleRegistration(request, response);
			} else {
				response.sendRedirect("login.jsp");
			}		
		} else {
			if (action.equals("photo")) {
				handleSubmitPhoto(request, response);
			} else if (action.equals("create-circle")) {
				handleCreateCircle(request, response);
			} else if(action.equals("add-friend")){
				handleAddFriendToCircle(request, response);
			}else if (action.equals("rating")){
				handleSubmitRating(request, response);
			} else if (action.equals("init-friends")) {
				handleFriendBrowserInit(request, response);
			} else if (action.equals("browser-photos")) {
				handleGetBrowserPhotos(request, response);
			} else if (action.equals("tag-photo")){
				handleTagPhoto(request, response);
			} else {
				response.sendRedirect("homepage.jsp");
			}
		}

	}

	private void handleSubmitRating(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {

			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			int photoID = Integer.parseInt(request.getParameter("photo"));
			int rateValue = Integer.parseInt(request.getParameter("rating"));
			
			Rating rating = new Rating(photoID, user.getUserID(), rateValue);
			
			boolean success = PhotoDAO.createRating(rating);
			if(success){
				response.sendRedirect("homepage.jsp?" + ResponseCode.PARAMETER_NAME + "=" + ResponseCode.RATING_SUCCESS);
			}	else {
				response.sendRedirect("homepage.jsp?" + ResponseCode.PARAMETER_NAME + "=" + ResponseCode.RATING_FAILURE);
			}
		} catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("homepage.jsp?" + ResponseCode.PARAMETER_NAME + "=" + ResponseCode.RATING_FAILURE);
		}finally{

		}
		
	}

	private void handleAddFriendToCircle(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		
		try {
			
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			String circleIDString = request.getParameter("circleID");
			int circleID = Integer.parseInt(circleIDString);
			
			
			String friendIDString = request.getParameter("friendID");
			int friendID = -1;
			
			try{
				friendID = Integer.parseInt(friendIDString);
				
				
			} catch (Exception e){
				friendID = UserDAO.getUserIdByEmail(friendIDString);
				
				
			}
					
			UserDAO.addFriendToCircle(circleID, friendID);
			//toDO - loop through circles
			for(Circle circle : user.getCircles()){
				if(circle.getCircleID() == circleID){
					circle.addFriendID(friendID);
				}
			}
			
			response.sendRedirect("confirmation.jsp");
			
			
		} catch(Exception e) {
			response.sendRedirect("error.jsp");
		}finally{
			
		}
		
		
	}
	
	protected void handleTagPhoto(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		int userId = user.getUserID();
		int photoId = Integer.parseInt(request.getParameter("photo"));
		Photo photo = PhotoDAO.getPhotoById(photoId);
		String tagText = request.getParameter("tag");
		boolean success = false;
		if(tagText !=  null && !"".equals(tagText.trim())){
			Tag tag = new Tag();
			tag.setUserID(userId);
			tag.setTagText(tagText.trim());
			tag.setPhotoID(photoId);
			success = PhotoDAO.createTag(tag);
			photo.addTag(tag);
		}
		if(success){
			response.sendRedirect("homepage.jsp?" + ResponseCode.PARAMETER_NAME + "=" + ResponseCode.TAGGING_SUCCESS);
		}	else {
			response.sendRedirect("homepage.jsp?" + ResponseCode.PARAMETER_NAME + "=" + ResponseCode.TAGGING_FAILURE);
		}
		
	}
	
	protected void handleGetBrowserPhotos(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		int userId = user.getUserID();
		Integer friendId = null;
		if (request.getParameter("friend-id") != null) {
			try {
				friendId = Integer.parseInt(request.getParameter("friend-id"));
			} catch (NumberFormatException nfe) {
				return;
			}
		}
		
		JSONArray json = PhotoDAO.getFriendsPhotos(userId, friendId);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
		out.flush();
	}

	protected void handleFriendBrowserInit(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		int userId = user.getUserID();
		String userName = user.getFirstName() + " " + user.getLastName(); 
		JSONArray json = UserDAO.getInitialBrowserFriends(userId, userName);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
		out.flush();
	}
	
	protected void handleCreateCircle(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		int userId = user.getUserID();
		String name = request.getParameter("circle-name");
		
		Circle circle = UserDAO.createCircle(userId, name);
		
		user.addCircle(circle);
		
		response.sendRedirect("confirmation.jsp");

	}

	protected void handleSubmitPhoto(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String privacy = request.getParameter("privacy");
		System.out.println("Privacy param =" + privacy);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		int userId = user.getUserID();
		boolean isPrivate = privacy.equals("private");
		boolean photo_posted = false;
		String url = request.getParameter("url");

		Photo photo = new Photo(url, isPrivate, userId);
		String rating = request.getParameter("rating");
		String[] circleIds = request.getParameterValues("circleIds");
		String[] viewerIds = request.getParameterValues("friendIds");
		System.out.println("cricleIds:" + circleIds);
		System.out.println("viewerIds:" + viewerIds);
		String tagText = request.getParameter("tag");
		
		if(circleIds != null){
			for(String circleId : circleIds){
				photo.addViewCircleID(Integer.parseInt(circleId));
			}
		}
		if(viewerIds != null){
			for(String viewerId : viewerIds){
				photo.addViewUserID(Integer.parseInt(viewerId));
			}
		}

		
		try {
			photo_posted = PhotoDAO.postPhoto(photo);
			if(rating != null){
				Rating r = new Rating();
				r.setUserID(userId);
				r.setValue(Integer.parseInt(rating));
				r.setPhotoID(photo.getPhotoId());
				PhotoDAO.createRating(r);
				photo.addRating(r);
			}			
			if(tagText !=  null && !"".equals(tagText.trim())){
				Tag tag = new Tag();
				tag.setUserID(userId);
				tag.setTagText(tagText);
				tag.setPhotoID(photo.getPhotoId());
				PhotoDAO.createTag(tag);
				photo.addTag(tag);
			}
		} catch (SQLException e) {
			
		}
		if(photo_posted){
			response.sendRedirect("confirmation.jsp");
		} else {
			response.sendRedirect("error.jsp");
		}
		
	}

	protected void handleLogin(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String email = request.getParameter("email-login");
		String password = request.getParameter("pwd");
		User user = UserDAO.login(email, password);
		if (user == null) {
			response.sendRedirect("login.jsp?error=1");
		} else {			
			HttpSession session = request.getSession(true);
			session.setAttribute("user", user);
			response.sendRedirect("homepage.jsp");
		}
	}

	protected void handleRegistration(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean isProfessor = "T".equals(request.getParameter("is-professor"));
		User user = isProfessor ? new Professor() : new Student();

		if (!setUserFields(request, response, user))
			return;

		if (isProfessor) {
			if (!setProfessorFields(request, response, user))
				return;
		} else {
			if (!setStudentFields(request, response, user))
				return;
		}

		if (!setUserAttendances(request, response, user))
			return;

		if (!setUserInterests(request, response, user))
			return;

		try {
			if (UserDAO.createUser(user)) {
				response.sendRedirect("login.jsp");
			} else {
				response.sendRedirect("registration.jsp?error=1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Please bear with us as we restore service.");
		}
		// TODO - Remove logged in validation form here in place of in doPost
		// method
	}

	private static boolean setUserFields(HttpServletRequest request,
			HttpServletResponse response, User user) throws IOException {
		user.setUserID(-1);
		user.setFirstName(request.getParameter("first-name"));
		user.setLastName(request.getParameter("last-name"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));
		user.setGender("M".equalsIgnoreCase(request.getParameter("gender")) ? Gender.MALE
				: Gender.FEMALE);
		user.setAddress(request.getParameter("street-address"));
		user.setStateId(Integer.parseInt(request.getParameter("state"), 10));
		user.setCity(request.getParameter("city"));
		user.setZip(request.getParameter("zip-code"));

		if (!areAllUserStringsSet(user)) {
			response.sendRedirect("registration.jsp?error=1");
			return false;
		}

		try {
			user.setDob(dateFormat.parse(request.getParameter("dob")));
		} catch (ParseException e) {
			sendFieldErrorRedirect(response, "dob",
					"Please enter Date of Birth in MM/DD/YYYY format.");
			return false;
		}

		if (!isValidEmail(user.getEmail())) {
			sendFieldErrorRedirect(response, "email",
					"Please enter a valid Email Address.");
			return false;
		}

		if (!isValidZipCode(user.getZip())) {
			sendFieldErrorRedirect(response, "zip-code",
					"Please enter a valid Zip Code.");
			return false;
		}

		return true;
	}

	private static boolean setProfessorFields(HttpServletRequest request,
			HttpServletResponse response, User user) {
		Professor prof = (Professor) user;
		prof.setResearchArea(request.getParameter("research-area"));
		prof.setTitle(request.getParameter("title"));

		return true;
	}

	private static boolean setStudentFields(HttpServletRequest request,
			HttpServletResponse response, User user) throws IOException {
		Student student = (Student) user;
		student.setMajor(request.getParameter("major"));
		try {
			student.setGpa(Double.parseDouble(request.getParameter("gpa")));
		} catch (NumberFormatException nfe) {
			sendFieldErrorRedirect(response, "gpa", "Please enter a valid GPA.");
			return false;
		}
		try {
			student.setAdvisorId(Integer.parseInt(request
					.getParameter("advisor")));
		} catch (NumberFormatException nfe) {
			sendFieldErrorRedirect(response, "advisor",
					"Please choose a valid Advisor.");
			return false;
		}

		return true;
	}

	private static boolean setUserAttendances(HttpServletRequest request,
			HttpServletResponse response, User user) throws IOException {
		int num = 1;
		String institutionField = "institution-" + num;
		String institutionID;
		while ((institutionID = request.getParameter(institutionField)) != null) {
			int attendedID = Integer.parseInt(institutionID);
			String institutionName;
			if (attendedID == 0) {
				institutionName = request.getParameter("other-"
						+ institutionField);
				if (institutionName == null
						|| institutionName.trim().length() == 0) {
					sendFieldErrorRedirect(response, "other-"
							+ institutionField,
							"Please enter an Other Institution name.");
					return false;
				}
			} else {
				institutionName = "";
			}

			int startYear;
			try {
				String yearParam = request.getParameter(institutionField
						+ "-from-year");
				if (!yearParam.matches("^[12]\\d{3}$"))
					throw new NumberFormatException();
				startYear = Integer.parseInt(yearParam);
			} catch (NumberFormatException nfe) {
				sendFieldErrorRedirect(response, institutionField
						+ "-from-year",
						"Please enter Year Attended From in YYYY format.");
				return false;
			}

			int endYear;
			try {
				String param = request.getParameter(institutionField
						+ "-to-year");
				if (!param.matches("^[12]\\d{3}$"))
					throw new NumberFormatException();
				endYear = Integer.parseInt(param);
			} catch (NumberFormatException nfe) {
				sendFieldErrorRedirect(response, institutionField + "-to-year",
						"Please enter Year Attended To in YYYY format.");
				return false;
			}

			Attendance attended = new Attendance(attendedID, institutionName,
					startYear, endYear);
			user.addAttendance(attended);
			institutionField = "institution-" + ++num;
		}

		return true;
	}

	private static boolean setUserInterests(HttpServletRequest request,
			HttpServletResponse response, User user) {
		int num = 1;
		String interestField = "interest-" + num;
		String interest;
		while ((interest = request.getParameter(interestField)) != null
				&& interest.trim().length() != 0) {
			user.addInterest(new Interest(interest));
			interestField = "interest-" + ++num;
		}

		return true;
	}

	private static void sendFieldErrorRedirect(HttpServletResponse response,
			String field, String message) throws IOException {
		message = URLEncoder.encode(message);
		response.sendRedirect("registration.jsp?error=1&field=" + field
				+ "&message=" + message);
	}

	private static boolean isValidZipCode(String zip) {
		boolean isValid = false;
		String zipCodeExpression = "^\\d{5}(-\\d{4})?$";
		Pattern pattern = Pattern.compile(zipCodeExpression,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(zip);

		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	private static boolean isValidEmail(String email) {
		boolean isValid = false;
		String emailExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern pattern = Pattern.compile(emailExpression,
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);

		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	private static boolean areAllUserStringsSet(User user) {
		boolean areSet = false;

		try {
			if (user.getFirstName().length() != 0
					&& user.getLastName().length() != 0
					&& user.getEmail().length() != 0
					&& user.getPassword().length() != 0
					&& user.getAddress().length() != 0
					&& user.getCity().length() != 0
					&& user.getZip().length() != 0) {
				areSet = true;
			}
		} catch (NullPointerException npe) {
			areSet = false;
		}

		return areSet;
	}
	
	private static String[] parseCSV(String string){
		String[] rawValues = string.split(",");
		ArrayList<String> values = new ArrayList<String>();
		for(String rawValue : rawValues){
			String trimmed = rawValue.trim();
			if(!"".equals(trimmed)) values.add(trimmed);
		}
		if(values.size() == 0){
			return null;
		} else {
			String[] retValues = new String[values.size()];
			return values.toArray(retValues);
		}
	}
}
