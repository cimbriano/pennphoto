package edu.pennphoto.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import edu.pennphoto.model.Circle;
import edu.pennphoto.model.Professor;
import edu.pennphoto.model.Student;
import edu.pennphoto.model.User;
import edu.pennphoto.model.User.Gender;

public class UserDAO {

	public static void createUser(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement userStmt = null;
		PreparedStatement spStmt = null;
		try {
			boolean isProfessor = user instanceof Professor;
			String userQuery = "insert into User values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
			if (isProfessor) {
				Professor professor = (Professor) user;
				spStmt = conn
						.prepareStatement("insert into Professor values(?,?,?)");
				spStmt.setInt(1, userId);
				spStmt.setString(2, professor.getResearchArea());
				spStmt.setString(3, professor.getTitle());
			} else {
				Student student = (Student) user;
				spStmt = conn
						.prepareStatement("insert into Student values(?,?,?)");
				spStmt.setInt(1, userId);
				spStmt.setString(2, student.getMajor());
				spStmt.setDouble(3, student.getGpa());
			}
			spStmt.execute();
			user.setUserID(userId);
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
		} finally {
			if (userStmt != null) {
				userStmt.close();
			}
			if (spStmt != null) {
				spStmt.close();
			}
			conn.setAutoCommit(true);
		}
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
			return getUser(-1, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
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
				user = isProfessor ? new Professor() : new Student();
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

				spStmt = conn.createStatement();
				if (isProfessor) {
					Professor professor = (Professor) user;
					ResultSet prs = spStmt
							.executeQuery("select * from Professor where user_id="
									+ user.getUserID());
					if (prs.next()) {
						professor.setTitle(prs.getString("title"));
						professor.setResearchArea(prs
								.getString("research_area"));
					}

				} else {
					Student student = (Student) user;
					ResultSet prs = spStmt
							.executeQuery("select * from Student where user_id="
									+ user.getUserID());
					if (prs.next()) {
						student.setMajor(prs.getString("major"));
						student.setGpa(prs.getDouble("gpa"));
					}
				}
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
		Connection conn = null;
		PreparedStatement circleStmt = null;
		Circle circle = null;
		try {
			String userQuery = "insert into Circle values (null, ?, ?)";
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
			if (circleStmt != null) {
				try {
					circleStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ex.printStackTrace();
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
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			ex.printStackTrace();
		}
		return false;
	}
	
	public static int removeFriendFromCircle(int circleId, int friendId) {
		Connection conn = null;
		Statement stmt = null;
		try {
			String query = "delete from In_Circle where circle_id="+circleId+" and friend_id="+friendId;
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			int rowsNum = stmt.executeUpdate(query);
			return rowsNum;
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
		return 0;
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

	public static void testCreateUser1() throws SQLException {
		Professor professor = new Professor();
		professor.setEmail("proftest@penn.edu");
		professor.setPassword("proftest");
		professor.setFirstName("profTestFN");
		professor.setLastName("proftestLN");
		professor.setDob(new java.util.Date());
		professor.setAddress("proftest address");
		professor.setGender(Gender.MALE);
		professor.setTitle("Prof");
		professor.setResearchArea("researchArea");
		createUser(professor);
	}
}
