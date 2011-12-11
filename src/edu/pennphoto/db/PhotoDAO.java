package edu.pennphoto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.pennphoto.model.Circle;
import edu.pennphoto.model.Photo;
import edu.pennphoto.model.Rating;
import edu.pennphoto.model.Tag;

public class PhotoDAO {

	public static boolean postPhoto(Photo photo) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
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
			photo.setPhotoId(photoId);
			storeVisibilityInfo(photo, conn);
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
			photo.setPhotoId(0);
			ex.printStackTrace();
			return false;
		} finally {
			conn.setAutoCommit(true);
			if (stmt != null) {
				stmt.close();
			}

		}
	}

	private static void storeVisibilityInfo(Photo photo, Connection conn)
			throws SQLException {
		PreparedStatement subStmt1 = null;
		PreparedStatement subStmt2 = null;
		try {
			if (photo.isPrivate()) {
				ArrayList<Integer> viewCircleIDs = photo.getViewCircleIDs();
				if (viewCircleIDs.size() > 0) {
					subStmt1 = conn
							.prepareStatement("insert into Photo_Visible_To_Circle values(?,?)");
					for (Integer circleId : viewCircleIDs) {
						subStmt1.setInt(1, photo.getPhotoId());
						subStmt1.setInt(2, circleId);
						subStmt1.execute();
					}
				}
				ArrayList<Integer> viewUserIDs = photo.getViewUserIDs();
				if (viewUserIDs.size() > 0) {
					subStmt2 = conn
							.prepareStatement("insert into Photo_Visible_To_User values(?,?)");
					for (Integer userId : viewUserIDs) {
						subStmt2.setInt(1, photo.getPhotoId());
						subStmt2.setInt(2, userId);
						subStmt2.execute();
					}
				}
			}
		} finally {
			if (subStmt1 != null) {
				subStmt1.close();
			}
			if (subStmt2 != null) {
				subStmt2.close();
			}
		}
	}

	public static boolean createTag(Tag tag){
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			String query = "insert into Tag values (?, ?, ?)";
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, tag.getPhotoID());
			stmt.setInt(2, tag.getUserID());
			stmt.setString(3, tag.getTagText());
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
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException excep) {
					excep.printStackTrace();
				}
			}
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean createRating(Rating rating){
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			String query = "insert into Rating values (?, ?, ?)";
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, rating.getPhotoID());
			stmt.setInt(2, rating.getUserID());
			stmt.setInt(3, rating.getValue());
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
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException excep) {
					excep.printStackTrace();
				}
			}
			ex.printStackTrace();
			return false;
		}
	}
	public static void testPostPhoto() {
		try{
			Photo photo = new Photo(
					"http://www.discoverlife.org/IM/I_JP/0005/320/Dichelostemma_capitatum,_flowers,I_JP523.jpg",
					true, 17001);
			photo.addViewCircleID(17000);
			photo.addViewCircleID(17001);
			photo.addViewUserID(17001);
			photo.addViewUserID(17006);
			photo.addViewUserID(17009);
			if(postPhoto(photo)){
				System.out.println("posting some tags");
				Tag tag = new Tag(photo.getPhotoId(), 17009, "tag from 17009");
				createTag(tag);
				tag = new Tag(photo.getPhotoId(), 17010, "tag from 17010");
				createTag(tag);
				tag = new Tag(photo.getPhotoId(), 17011, "tag from 17011");
				createTag(tag);
						
				System.out.println("rate photo");
				Rating rating = new Rating(photo.getPhotoId(), 17009, 1);
				createRating(rating);
				rating = new Rating(photo.getPhotoId(), 17010, 2);
				createRating(rating);
				rating = new Rating(photo.getPhotoId(), 17011, 3);
				createRating(rating);
			};
			
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
}
