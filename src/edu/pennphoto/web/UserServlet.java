package edu.pennphoto.web;

import java.io.IOException;
import java.util.ArrayList;

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
import edu.pennphoto.model.User;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		
		if(msg == null) {
			response.sendRedirect("login.jsp?error=bp");
		} else if (msg.equals("/logout")){
			request.getSession().invalidate();
			response.sendRedirect(request.getServletContext().getContextPath()+ "/login.jsp");
		} else {
			response.getWriter().write(msg);			
		}
		
//		String msg = "";
//		try {
//			// msg = DBHelper.getInstance().testConnection();
//			// UserDAO.testCreateUser();
//			// Circle circle = UserDAO.createCircle(17001, "test_circle2");
//			//ArrayList<Circle> circles = UserDAO.getUserCircles(17001);
//			//UserDAO.addFriendToCircle(17000, 17002);
//			
//			//UserDAO.removeFriendFromCircle(17000, 17002);
//			User user = UserDAO.login("test@penn.edu", "test");
//			msg = "" + user;
//			user = UserDAO.getUserById(17002);
//			msg += "\n\n" + user;
//			
//			PhotoDAO.testPostPhoto();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			msg = e.getMessage();
//		}
//		response.getWriter().write(msg);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		if (action.equals("login")) {
			handleLogin(request, response);
		} else if (action.equals("register")) {
			handleRegistration(request, response);
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
		String email = request.getParameter("email-login");
		String password = request.getParameter("pwd");
		User user = UserDAO.login(email, password);

		if (user == null) {
			response.sendRedirect("registration.jsp?error=1");
		} else {
			response.sendRedirect("login.jsp");
		}		
	}

}
