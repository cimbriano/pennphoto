package edu.pennphoto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.pennphoto.model.Photo;
import edu.pennphoto.model.Rating;
import edu.pennphoto.model.Tag;

public class PhotoDAO {

	private static String RELEVANCE_SCORE_QUERY = "select pp.id as photo_id, IF(avg(rating) IS NOT NULL, avg(rating), 0 ) "+
			"+ IF((select 1 from Friendship_View where user_id=? and friend_id=pp.owner_id), 1, 0) +IF(pp.owner_id=?, 1, 0) "+
			"+ IF(favg.favg is null, 0, favg.favg) as rel_score from Photo pp left join Rating r on pp.id=r.photo_id "+ 
			"left join (select photo_id, avg(rating) favg from Rating where user_id IN (select friend_id from Friendship_View where user_id=?) group by photo_id) favg on pp.id = favg.photo_id " +
			"where pp.id IN ("+
			"select distinct p.id from Photo p left join Photo_Visible_To_Circle vcr on p.id=vcr.photo_id "+ 
			"left join Photo_Visible_To_User vu on p.id=vu.photo_id left join In_Circle ic on vcr.circle_id = ic.circle_id where is_private = 0 or vu.user_id = ? or ic.friend_id=? or p.owner_id=?) group by pp.id"; 
	
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

	public static boolean createTag(Tag tag) {
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

	public static boolean createRating(Rating rating) {
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

	public static List<Photo> searchPhotosByTag(String keyword, int userId) {
		String query = "select * from Photo p left join Tag t on p.id=t.photo_id inner join ("+RELEVANCE_SCORE_QUERY+") "+
	" rel on p.id=rel.photo_id where t.tag=? order by rel.rel_score";
//		String query = "select * from Photo p left join Tag t on p.id=t.photo_id where t.tag=? and"
//				+ " p.id IN (select distinct p.id from Photo p left join Photo_Visible_To_Circle vcr on p.id=vcr.photo_id left join Photo_Visible_To_User vu on p.id=vu.photo_id"
//				+ " left join In_Circle ic on vcr.circle_id = ic.circle_id where is_private = 0 or vu.user_id = ? or ic.friend_id=? or p.owner_id=?)";

		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.prepareStatement(query);
//			stmt.setString(1, keyword);
//			stmt.setInt(2, userId);
//			stmt.setInt(3, userId);
//			stmt.setInt(4, userId);
			
			stmt.setInt(1, userId);
			stmt.setInt(2, userId);
			stmt.setInt(3, userId);
			stmt.setInt(4, userId);
			stmt.setInt(5, userId);
			stmt.setInt(6, userId);
			stmt.setString(7, keyword);
			ResultSet rs = stmt.executeQuery();
			return generatePhotoList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Photo> getUserPhoto(int userId) {
		String query = "select * from Photo where owner_id=" + userId;
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return generatePhotoList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Integer> getPhotoVisibleToCircles(int photoId) {
		String query = "select distinct(circle_id) from Photo_Visible_To_Circle where photo_id="
				+ photoId;
		return getIntList(query);
	}

	public static List<Integer> getPhotoVisibleToUsers(int photoId) {
		String query = "select distinct(user_id) from Photo_Visible_To_User where photo_id="
				+ photoId;
		return getIntList(query);
	}

	public static List<Tag> getPhotoTags(int photoId) {
		String query = "select * from tag where photo_id=" + photoId;
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ArrayList<Tag> tags = new ArrayList<Tag>();
			Tag tag = null;
			while (rs.next()) {
				// int photoID, int userID, String tagText
				tag = new Tag(rs.getInt("photo_id"), rs.getInt("user_id"),
						rs.getString("tag"));
				tags.add(tag);
			}
			return tags;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Rating> getPhotoRatings(int photoId) {
		String query = "select * from Rating where photo_id=" + photoId;
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ArrayList<Rating> ratings = new ArrayList<Rating>();
			Rating rating = null;
			while (rs.next()) {
				// int photoID, int userID, String tagText
				rating = new Rating(rs.getInt("photo_id"),
						rs.getInt("user_id"), rs.getInt("rating"));
				ratings.add(rating);
			}
			return ratings;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static List<Integer> getIntList(String query) {
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = DBHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ArrayList<Integer> result = new ArrayList<Integer>();
			while (rs.next()) {
				result.add(rs.getInt(1));
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static List<Photo> generatePhotoList(ResultSet rs)
			throws SQLException {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		Photo photo = null;
		while (rs.next()) {
			// photoId, String url, boolean isPrivate, int ownerId, Date
			// uploadDate
			photo = new Photo(rs.getInt("id"), rs.getString("url"),
					rs.getBoolean("is_private"), rs.getInt("owner_id"),
					rs.getDate("upload_date"));
			photos.add(photo);
		}
		return photos;
	}

	public static void testSearchPhoto() {
		List<Photo> photos = searchPhotosByTag("tag from 17009", 17002);
		for (Photo photo : photos) {
			System.out.println(photo.getPhotoId() + " " + photo.getUrl());
		}
	}

	public static void testPostPhoto() {
		try {
			Photo photo = new Photo(
					"http://www.discoverlife.org/IM/I_JP/0005/320/Dichelostemma_capitatum,_flowers,I_JP523.jpg",
					true, 17001);
			photo.addViewCircleID(17000);
			photo.addViewCircleID(17001);
			photo.addViewUserID(17001);
			photo.addViewUserID(17006);
			photo.addViewUserID(17009);
			if (postPhoto(photo)) {
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
			}
			;

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
