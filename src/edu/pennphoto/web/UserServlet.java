package edu.pennphoto.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;

import edu.pennphoto.db.DBHelper;
import edu.pennphoto.db.PhotoDAO;
import edu.pennphoto.db.UserDAO;
import edu.pennphoto.model.Circle;
import edu.pennphoto.model.Photo;
import edu.pennphoto.model.Professor;
import edu.pennphoto.model.Student;
import edu.pennphoto.model.User;
import edu.pennphoto.model.User.Gender;
import edu.pennphoto.model.User.Attendance;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

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

		if (msg == null) {
			// Error
			response.sendRedirect("login.jsp?error=bp");
		} else if (msg.equals("/logout")) {
			// Logout
			request.getSession().invalidate();
			response.sendRedirect(request.getServletContext().getContextPath()
					+ "/login.jsp");
		} else {
			// Any other
			response.getWriter().write(msg);
		}

		// String msg = "";
		// try {
		// // msg = DBHelper.getInstance().testConnection();
		// // UserDAO.testCreateUser();
		// // Circle circle = UserDAO.createCircle(17001, "test_circle2");
		// //ArrayList<Circle> circles = UserDAO.getUserCircles(17001);
		// //UserDAO.addFriendToCircle(17000, 17002);
		//
		// //UserDAO.removeFriendFromCircle(17000, 17002);
		// User user = UserDAO.login("test@penn.edu", "test");
		// msg = "" + user;
		// user = UserDAO.getUserById(17002);
		// msg += "\n\n" + user;
		//
		// PhotoDAO.testPostPhoto();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// msg = e.getMessage();
		// }
		// response.getWriter().write(msg);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		// TODO - Put is_logged in validation here.
		if (action.equals("login")) {
			handleLogin(request, response);
		} else if (action.equals("register")) {
			handleRegistration(request, response);
		} else if (action.equals("photo")) {
			handleSubmitPhoto(request, response);
		} else if (action.equals("create-circle")) {
			handleCreateCircle(request, response);
		}

	}

	protected void handleCreateCircle(HttpServletRequest request,
			HttpServletResponse response) {
		// Do some create circle stuff

		// TODO - Success or failure : redirect? AJAX?

	}

	protected void handleSubmitPhoto(HttpServletRequest request,
			HttpServletResponse response) {
		String privacy = request.getParameter("privacy");
		boolean isPrivate = privacy.equals("private") ? true : false;

		String url = request.getParameter("url");
		String rating = request.getParameter("rating");
		// TODO - Finish creating Photo

		// int ownerId = ;

		// Photo photo = new Photo(url, isPrivate, ownerId);
		// boolean photo_posted = PhotoDAO.postPhoto(photo);

		// TODO - Success or failure : redirect? AJAX?
	}

	protected void handleLogin(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String email = request.getParameter("email-login");
		String password = request.getParameter("pwd");
		User user = UserDAO.login(email, password);

		// TODO - Remove logged in validation form here in place of in doPost
		// method
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
		String firstName, lastName, email, password, address, city, state, zip;
		Date dob;
		Gender gender;

		boolean isProfessor = "T".equals(request.getParameter("is-professor"));
		User user = isProfessor ? new Professor() : new Student();
		user.setUserID(-1);
		user.setFirstName(request.getParameter("first-name"));
		user.setLastName(request.getParameter("last-name"));
		try {
			user.setDob(dateFormat.parse(request.getParameter("dob")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.sendRedirect("registration.jsp?error=1");
			return;
		}
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));

		user.setAddress(request.getParameter("street-address"));
		user.setStateId(Integer.parseInt(request.getParameter("state"), 10));
		user.setCity(request.getParameter("city"));
		user.setZip(request.getParameter("zip-code"));
		user.setGender("M".equalsIgnoreCase(request.getParameter("gender")) ? Gender.MALE
				: Gender.FEMALE);

		int i = 1;
		String institutionField = "institution-" + i;
		String institutionID;
		while ((institutionID = request.getParameter(institutionField)) != null){
			int attendedID = Integer.parseInt(institutionID);
			String institutionName;
			if (attendedID == 0) 
				institutionName = request.getParameter("other-" + institutionField);
			else
				institutionName = "";
			int startYear = Integer.parseInt(request.getParameter(institutionField + "-from-year"));
			int endYear = Integer.parseInt(request.getParameter(institutionField + "-to-year"));
				
			Attendance attended = new Attendance(attendedID, institutionName, startYear, endYear);
			user.addAttendance(attended);
			institutionField = "institution-" + ++i;
		}
		
		for(Attendance att : user.getAttendances())
			System.out.println(att.getInstitution() + ":" + att.getInstitutionId() + ":"  + att.getStartYear() + ":"  + att.getEndYear());
		
//		i = 1;
//		String interestField = "interest-" + i;
//		String interest;
//		while ((interest = request.getParameter(interestField)) != null){
//			user.addInterest(interest);
//			institutionField = "institution-" + ++i;
//		}
		
//		for(Attendance att : user.getAttendances())
//			System.out.println(att.getInstitution() + ":" + att.getInstitutionId() + ":"  + att.getStartYear() + ":"  + att.getEndYear());
		
		// TODO - Remove logged in validation form here in place of in doPost
		// method
		if (user == null) {
			response.sendRedirect("registration.jsp?error=1");
		} else {
			response.sendRedirect("login.jsp");
		}
	}

}
