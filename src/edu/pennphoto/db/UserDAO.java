package edu.pennphoto.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import edu.pennphoto.model.Professor;
import edu.pennphoto.model.Student;
import edu.pennphoto.model.User;
import edu.pennphoto.model.User.Gender;

public class UserDAO {
	
	public static void createUser(User user) throws SQLException{
		Connection conn = null;
		PreparedStatement userStmt = null;
		PreparedStatement spStmt = null;
		try{
			boolean isProfessor = user instanceof Professor;
		String userQuery = "insert into User values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		conn = DBHelper.getInstance().getConnection();
		conn.setAutoCommit(false);
		userStmt = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
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
		userStmt.setString(11, user.getGender() == Gender.MALE?"m":"f");
		
		userStmt.execute();
		ResultSet rs = userStmt.getGeneratedKeys();
		rs.next();
		int userId = rs.getInt(1);
		if(isProfessor){
			Professor professor = (Professor)user;
			spStmt = conn.prepareStatement("insert into Professor values(?,?,?)");
			spStmt.setInt(1, userId);
			spStmt.setString(2, professor.getResearchArea());
			spStmt.setString(3, professor.getTitle());
		}else{
			Student student = (Student)user;
			spStmt = conn.prepareStatement("insert into Student values(?,?,?)");
			spStmt.setInt(1, userId);
			spStmt.setString(2, student.getMajor());
			spStmt.setDouble(3, student.getGpa());
		}
		spStmt.execute();
		user.setUserID(userId);
		}catch(Exception ex){
			 if (conn != null) {
			        try {
			          System.err.print("Transaction is being rolled back");
			          conn.rollback();
			        } catch(SQLException excep) {
			          excep.printStackTrace();
			        }
			      }
			 ex.printStackTrace();
		} finally {
		      if (userStmt != null) { userStmt.close(); }
		      if (spStmt != null) { spStmt.close(); }
		      conn.setAutoCommit(true);
		}	
	}
	
	public static void testCreateUser() throws SQLException{
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
}
