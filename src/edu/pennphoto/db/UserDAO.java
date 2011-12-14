package edu.pennphoto.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import edu.pennphoto.model.Circle;
import edu.pennphoto.model.Professor;
import edu.pennphoto.model.Student;
import edu.pennphoto.model.User;
import edu.pennphoto.model.User.Attendance;
import edu.pennphoto.model.User.Gender;
import edu.pennphoto.model.User.Interest;

public class UserDAO {

	public static boolean createUser(User user) throws SQLException {
			Connection conn = null;
			boolean success = false;
			try{
				success = createBaseUser(user);
				if(success){
					conn = DBHelper.getInstance().getConnection();
					storeAttendances(user, conn);
					storeInterests(user, conn);
				}
			}catch(NamingException ex){
				//TODO: throw it too?
				ex.printStackTrace();
			}finally{
				if(conn != null){
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		return success;
	}
	
	private static boolean createBaseUser(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement userStmt = null;
		PreparedStatement spStmt = null;
		boolean success = true;
		try {
			boolean isProfessor = user instanceof Professor;
			String userQuery = "insert into User values ("+(user.getUserID() > 0?user.getUserID():"null")+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			conn = DBHelper.getInstance().getConnection();
			conn.setAutoCommit(false);
			userStmt = conn.prepareStatement(userQuery,
					Statement.RETURN_GENERATED_KEYS);
			userStmt.setString(1, user.getPassword());
			userStmt.setString(2, user.getEmail());
			userStmt.setString(3, user.getFirstName());
			userStmt.setString(4, user.getLastName());
			userStmt.setDate(5, new Date(user.getDob().getTime()));
			userStmt.setString(6, user.getAddress());
			userStmt.setString(7, user.getCity());
			userStmt.setInt(8, user.getStateId());
			userStmt.setString(9, user.getZip());
			userStmt.setBoolean(10, isProfessor);
			userStmt.setString(11, user.getGender() == Gender.MALE ? "m" : "f");

			userStmt.execute();
			ResultSet rs = userStmt.getGeneratedKeys();
			rs.next();
			int userId = rs.getInt(1);
			user.setUserID(userId);
			if (isProfessor) {
				Professor professor = (Professor) user;
				spStmt = conn
						.prepareStatement("insert into Professor values(?,?,?)");
				spStmt.setInt(1, userId);
				spStmt.setString(2, professor.getResearchArea());
				spStmt.setString(3, professor.getTitle());
				spStmt.execute();
			} else {
				Student student = (Student) user;
				spStmt = conn
						.prepareStatement("insert into Student values(?,?,?)");
				spStmt.setInt(1, userId);
				spStmt.setString(2, student.getMajor());
				spStmt.setDouble(3, student.getGpa());
				spStmt.execute();
				addAdvisorAdvisee(student.getUserID(), student.getAdvisorId(), conn);
			}
		} catch (Exception ex) {
			if (conn != null) {
				try {
					System.err.print("Transaction is being rolled back");
					conn.rollback();
				} catch (SQLException excep) {
					excep.printStackTrace();
				}
			}
			ex.printStackTrace();
			success = false;
		} finally {
			conn.setAutoCommit(true);
			try{
				userStmt.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			try{
				spStmt.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			try{
				conn.close();
			}catch(SQLException ex){
				ex.printStackTrace();
			}
		}
		return success;
	}
	
	private static void addAdvisorAdvisee(int studentId, int professorId, Connection conn) throws SQLException{
		Statement stmt = null;
		try {
			String query = "insert into Advises values("+studentId+", "+professorId+")";
			stmt = conn.createStatement();
			stmt.execute(query);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static boolean storeAttendances(User user, Connection conn){
		//Connection conn = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("insert into Attended values(?, ?, ?, ?)");
			ArrayList<Attendance> attendances = user.getAttendances();
			for (Attendance attendance : attendances) {
				int institutionId = attendance.getInstitutionId();
				if(attendance.getInstitutionId() <= 0){
					institutionId = getInstitutionIdByName(attendance.getInstitution(), conn);
					if(institutionId <= 0){
						institutionId = createInstitution(attendance.getInstitution(), conn);
					}
				}
				stmt.setInt(1, user.getUserID());
				stmt.setInt(2, institutionId);
				stmt.setInt(3, attendance.getStartYear());
				stmt.setInt(4, attendance.getEndYear());
				stmt.execute();
			}
			return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
//				if (conn != null) {
//					conn.close();
//				}
				if (stmt != null) {
					stmt.close();
				}
			}catch (Exception ex) {
			}
		}
	}
	
	private static boolean storeInterests(User user, Connection conn){
		//Connection conn = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("insert into Interested_In values(?, ?)");
			ArrayList<Interest> interests = user.getInterests();
			for (Interest interest : interests) {
				int id = interest.getId();
				if(id <= 0){
					id = getInterestIdByLabel(interest.getLabel(), conn);
					if(id <= 0){
						id = createInterest(interest.getLabel(), conn);
					}
				}
				stmt.setInt(1, user.getUserID());
				stmt.setInt(2, id);
				stmt.execute();
			}
			return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
//				if (conn != null) {
//					conn.close();
//				}
				if (stmt != null) {
					stmt.close();
				}
			}catch (Exception ex) {
			}
		}
	}
	
	private static int createInterest(String interest, Connection conn){
		return createIdValuePair("insert into Interest_Desc values (null, ?)", interest, conn);
	}
	
	private static int createInstitution(String institution, Connection conn){
		return createIdValuePair("insert into Institution values (null, ?)", institution, conn);
//		PreparedStatement stmt = null;
//		int institutionId = 0;
//		try {
//			String query = "insert into Institution values (null, ?)";
//			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//			stmt.setString(1, institution);
//			stmt.execute();
//			ResultSet rs = stmt.getGeneratedKeys();
//			rs.next();
//			institutionId = rs.getInt(1);
//		} catch (Exception ex) {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			ex.printStackTrace();
//		}
//		return institutionId;
	}
	
	private static int createIdValuePair(String query, String value, Connection conn){
		PreparedStatement stmt = null;
		int id = 0;
		try {
			//String query = "insert into Institution values (null, ?)";
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, value);
			stmt.execute();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
		} catch (Exception ex) {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ex.printStackTrace();
		}
		return id;
	}
	
	public static User getUserById(int userId) {
		try {
			return getUser(userId, null, null);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static User login(String username, String password) {
		try {
			User user =  getUser(-1, username, password);
			if(user != null){
				UserDAO.getUserCircles(user.getUserID());
				
			}
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	public static List<User> getUsersList(){
//		List<User> users = new ArrayList<User>();
//		loadProfessor
//		String query = "select * from User"
//	}
	
	private static void loadProfessors(List<User> container, Connection conn){
		String query = "select * from User u inner join Professor p on u.id=p.user_id";
		Statement stmt = null;
		try {
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
				Professor professor = (Professor)createUserDataObject(rs, true);
				populateProfessorData(rs, professor);
				container.add(professor);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void loadStudents(List<User> container, Connection conn){
		String query = "select * from User u inner join Student s on u.id=s.user_id";
		Statement stmt = null;
		try {
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
				Student student = (Student)createUserDataObject(rs, false);
				populateStudentData(rs, student);
				container.add(student);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static User createUserDataObject(ResultSet rs, boolean isProfessor) throws SQLException{
		User user = isProfessor ? new Professor() : new Student();
		user.setEmail(rs.getString("email"));
		user.setUserID(rs.getInt("id"));
		user.setFirstName(rs.getString("first_name"));
		user.setLastName(rs.getString("last_name"));
		user.setDob(rs.getDate("dob"));
		user.setAddress(rs.getString("street_address"));
		user.setStateId(rs.getInt("state_id"));
		user.setCity(rs.getString("city"));
		user.setZip(rs.getString("zip_code"));
		user.setGender("m".equalsIgnoreCase(rs.getString("gender")) ? Gender.MALE
				: Gender.FEMALE);
		return user;
	}

	private static void populateProfessorData(ResultSet rs, Professor professor) throws SQLException{
		professor.setTitle(rs.getString("title"));
		professor.setResearchArea(rs
				.getString("research_area"));
	}
	
	private static void populateStudentData(ResultSet rs, Student student) throws SQLException{
		student.setMajor(rs.getString("major"));
		student.setGpa(rs.getDouble("gpa"));
	}
	
	private static User getUser(int userId, String username, String password)
			throws SQLException {
		Connection conn = null;
		PreparedStatement userStmt = null;
		Statement spStmt = null;
		User user = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			if (userId == -1) {
				String userQuery = "select * from User where email=? and password=?";
				userStmt = conn.prepareStatement(userQuery);
				userStmt.setString(1, username);
				userStmt.setString(2, password);
			} else {
				String userQuery = "select * from User where id=?";
				userStmt = conn.prepareStatement(userQuery);
				userStmt.setInt(1, userId);
			}

			ResultSet rs = userStmt.executeQuery();
			if (rs.next()) {
				boolean isProfessor = rs.getBoolean("is_professor");
				user = createUserDataObject(rs, isProfessor);
//				user = isProfessor ? new Professor() : new Student();
//				user.setEmail(rs.getString("email"));
//				user.setUserID(rs.getInt("id"));
//				user.setFirstName(rs.getString("first_name"));
//				user.setLastName(rs.getString("last_name"));
//				user.setDob(rs.getDate("dob"));
//				user.setAddress(rs.getString("street_address"));
//				user.setStateId(rs.getInt("state_id"));
//				user.setCity(rs.getString("city"));
//				user.setZip(rs.getString("zip_code"));
//				user.setGender("m".equalsIgnoreCase(rs.getString("gender")) ? Gender.MALE
//						: Gender.FEMALE);

				spStmt = conn.createStatement();
				if (isProfessor) {
					Professor professor = (Professor) user;
					ResultSet prs = spStmt
							.executeQuery("select * from Professor where user_id="
									+ user.getUserID());
					if (prs.next()) {
						populateProfessorData(prs, professor);
//						professor.setTitle(prs.getString("title"));
//						professor.setResearchArea(prs
//								.getString("research_area"));
					}

				} else {
					Student student = (Student) user;
					ResultSet prs = spStmt
							.executeQuery("select * from Student where user_id="
									+ user.getUserID());
					if (prs.next()) {
						populateStudentData(prs, student);
//						student.setMajor(prs.getString("major"));
//						student.setGpa(prs.getDouble("gpa"));
					}
				}
				user.setState(getStateNameById(user.getStateId()));
			}
		} catch (Exception ex) {
			user = null;
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (userStmt != null) {
				userStmt.close();
			}
			if (spStmt != null) {
				spStmt.close();
			}
		}
		return user;
	}
	
	public static Circle createCircle(int userId, String name) {
		return createCircle(userId, name, -1);
	}
	
	public static Circle createCircle(int userId, String name, int knownCircleId) {
		Connection conn = null;
		PreparedStatement circleStmt = null;
		Circle circle = null;
		try {
			String userQuery = "insert into Circle values ("+(knownCircleId >0?knownCircleId:"null")+", ?, ?)";
			conn = DBHelper.getInstance().getConnection();
			circleStmt = conn.prepareStatement(userQuery,
					Statement.RETURN_GENERATED_KEYS);
			circleStmt.setString(1, name);
			circleStmt.setInt(2, userId);
			circleStmt.execute();
			ResultSet rs = circleStmt.getGeneratedKeys();
			rs.next();
			int circleId = rs.getInt(1);
			circle = new Circle(circleId, name);
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
				try {
					circleStmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return circle;
	}

	public static boolean addFriendToCircle(int circleId, int friendId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			String query = "insert into In_Circle values (?, ?)";
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, circleId);
			stmt.setInt(2, friendId);
			stmt.execute();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static int removeFriendFromCircle(int circleId, int friendId) {
		String query = "delete from In_Circle where circle_id="+circleId+" and friend_id="+friendId;
		return executeDeleteStatement(query);
	}

	private static int executeDeleteStatement(String query){
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			int rowsNum = stmt.executeUpdate(query);
			return rowsNum;
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public static int deleteCircle(int circleId){
		return executeDeleteStatement("delete from Circle where id="+circleId);
	}
	
	public static ArrayList<Circle> getUserCircles(int userId) {
		String query = "select * from Circle c left join In_Circle ic on c.id = ic.circle_id where c.owner_id="
				+ userId;
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ArrayList<Circle> circles = new ArrayList<Circle>();
			Circle circle = null;
			while (rs.next()) {
				if (circle == null || circle.getCircleID() != rs.getInt("id")) {
					circle = new Circle(rs.getInt("id"), rs.getString("name"));
					circles.add(circle);
				}
				circle.addFriendID(rs.getInt("friend_id"));
			}
			return circles;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Map<Integer, String> getProfessors() {
		return loadKeyValuePairs("select id, CONCAT(first_name, ' ', last_name)  from Professor P join User U where P.user_id = U.id order by last_name");
	}
	
	public static Map<Integer, String> getInstitutions(){
		return loadKeyValuePairs("select * from Institution order by name");
	}
	
	public static Map<Integer, String> getStates(){
		return loadKeyValuePairs("select id, name from State order by name");
	}
	
	private static int getInstitutionIdByName(String name, Connection conn) throws SQLException{
		return getIdByValue("select id from Institution where name=?", name, conn);
	}
	private static int getInterestIdByLabel(String label, Connection conn) throws SQLException{
		return getIdByValue("select id from Interest_Desc where label=?", label, conn);
	}
	private static int getIdByValue(String query, String value, Connection conn) throws SQLException{
		PreparedStatement stmt = null;
		int id = 0;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, value);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} finally{
			try{
			if(stmt != null) stmt.close();
			//if(conn != null) conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	private static String getStateNameById(int stateId){
		try {
			return loadValueById("select name from State where id="+stateId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static String loadValueById(String query) throws SQLException, NamingException {
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				return rs.getString(1);
			}
			return null;
		} finally{
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Map<Integer, String> loadKeyValuePairs(String query) {
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			Map<Integer, String> result = new LinkedHashMap<Integer, String>();
			while (rs.next()) {
				result.put(rs.getInt(1), rs.getString(2));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void testCreateUser() throws SQLException {
		Student student = new Student();
		student.setEmail("test@penn.edu");
		student.setPassword("test");
		student.setFirstName("TestFN");
		student.setLastName("testLN");
		student.setDob(new java.util.Date());
		student.setAddress("test address");
		student.setGender(Gender.FEMALE);
		student.setMajor("eng");
		student.setGpa(3.5);
		createUser(student);
	}
	
	public static User testCreateUser2() {
		Student student = new Student();
		student.setEmail("test6");
		student.setPassword("test6");
		student.setFirstName("TestFNStud6");
		student.setLastName("testLNStud6");
		student.setDob(new java.util.Date());
		student.setAddress("test address2");
		student.setGender(Gender.FEMALE);
		student.setMajor("eng2");
		student.setGpa(3.5);
		student.addAttendance(new Attendance(1, "some name", 2000, 2001));
		student.addAttendance(new Attendance(0, "MIT", 2001, 2002));
		student.addAttendance(new Attendance(0, "Princeton", 2002, 2003));
		student.addAttendance(new Attendance(0, "Brown", 2002, 2003));
		student.addInterest(new Interest(1, "Football"));
		student.addInterest(new Interest(0, "Music"));
		student.addInterest(new Interest(0, "Chess"));
		try {
		createUser(student);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return student;
	}

	public static void testCreateUser1(){
		Professor professor = new Professor();
		professor.setUserID(111);
		professor.setEmail("proftest2@penn.edu");
		professor.setPassword("proftest2");
		professor.setFirstName("David");
		professor.setLastName("Muller");
		professor.setDob(new java.util.Date());
		professor.setAddress("proftest address");
		professor.setGender(Gender.MALE);
		professor.setTitle("Prof");
		professor.setResearchArea("researchArea");
		try {
			createUser(professor);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
