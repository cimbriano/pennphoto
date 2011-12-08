package edu.pennphoto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.pennphoto.model.Photo;

public class PhotoDAO {
	
	public static boolean postPhoto(Photo photo) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement subStmt1 = null;
		PreparedStatement subStmt2 = null;
		try {
			String userQuery = "insert into Photo values (null, ?, ?, ?, null)";
			conn = DBHelper.getInstance().getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(userQuery,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, photo.getOwnerId());
			stmt.setString(2, photo.getUrl());
			stmt.setBoolean(3, photo.isPrivate());
			stmt.execute();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			int photoId = rs.getInt(1);
			photo.setPhotoID(photoId);
			if (photo.isPrivate()) {
				ArrayList<Integer> viewCircleIDs = photo.getViewCircleIDs();
				if(viewCircleIDs.size() > 0){
					subStmt1 = conn
							.prepareStatement("insert into Photo_Visible_To_Circle values(?,?)");
					for (Integer circleId : viewCircleIDs) {
						subStmt1.setInt(1, photoId);
						subStmt1.setInt(2, circleId);
						subStmt1.execute();
					}
				}
				ArrayList<Integer> viewUserIDs = photo.getViewUserIDs();
				if(viewUserIDs.size() > 0){
					subStmt2 = conn
							.prepareStatement("insert into Photo_Visible_To_User values(?,?)");
					for (Integer userId : viewUserIDs) {
						subStmt2.setInt(1, photoId);
						subStmt2.setInt(2, userId);
						subStmt2.execute();
					}
				}
			} 
			return true;
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
			return false;
		} finally {
			conn.setAutoCommit(true);
			if (stmt != null) {
				stmt.close();
			}
			if (subStmt1 != null) {
				subStmt1.close();
			}
			if (subStmt2 != null) {
				subStmt2.close();
			}
		}
	}
	
	public static void testPostPhoto() throws SQLException {
		Photo photo = new Photo("http://www.discoverlife.org/IM/I_JP/0005/320/Dichelostemma_capitatum,_flowers,I_JP523.jpg", true, 17001);
		photo.addViewCircleID(17000);
		photo.addViewCircleID(17001);
		photo.addViewUserID(17001);
		photo.addViewUserID(17002);
		postPhoto(photo);
	}

}
