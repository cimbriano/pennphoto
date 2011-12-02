package edu.pennphoto.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBHelper {
	
	private static DBHelper dbHelper;
	private DataSource dataSource;
	
	private DBHelper() throws NamingException{
		Context initContext = new InitialContext();
		Context envContext  = (Context)initContext.lookup("java:/comp/env");
		dataSource = (DataSource)envContext.lookup("jdbc/pennphotoDB");
	}
	
	public static DBHelper getInstance() throws NamingException{
		if(dbHelper == null){
			dbHelper = new DBHelper();
		}
		return dbHelper;
	}
	
	public Connection getConnection() throws SQLException{
		return dataSource.getConnection();
		
	}
	
	public String testConnection(){
		try {
			Connection conn = getConnection();
			Statement statement = conn.createStatement();
			
			if (statement.execute("select * from user")) {
			
				ResultSet rs = statement.getResultSet();
				
				return "Success!!!";
			}else{
				return "False after exec";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		
	}
}
