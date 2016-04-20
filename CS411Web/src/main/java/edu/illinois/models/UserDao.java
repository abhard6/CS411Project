package edu.illinois.models;

import org.springframework.stereotype.Component;

import edu.illinois.Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component("UserDao")
public class UserDao extends BasicDao<Login>{

	public void insertUserQuery(String username, Query t) {
		mySql.executeUpdate("INSERT INTO user_query(username, query_id) VALUES(\"" + username + "\"," + t.id + ")");
	}

	public void removeUserQuery(String username, Query t) {
		mySql.executeUpdate("DELETE FROM user_query WHERE username=\"" + username + "\" AND query_id=" + t.id + "");
	}

	public boolean foundUserQuery(String username, Query t) {
		try {
			return mySql.executeQuery("SELECT * FROM user_query WHERE username=\"" + username + "\" AND query_id=" + t.id).first();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void insert(Registration greeting) {
		Connection conn = mySql.connection;
		String query = "INSERT INTO  Users" + " (username,password) " +  " values (?, ?);";

		PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(query);

			preparedStmt.setString (1, greeting.getUsername());
			preparedStmt.setString (2, greeting.getPassword());

			// execute the preparedstatement
			preparedStmt.execute();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean validateUser(Login loginObj) {
		Connection conn = mySql.connection;
		String query = "select 1 from Users where username = ? and password = ?";
		
		PreparedStatement preparedStmt;
		ResultSet rs;
		try {
			preparedStmt = conn.prepareStatement(query);
			
			preparedStmt.setString (1, loginObj.getUserName());
			preparedStmt.setString (2, loginObj.getPassword());
			
			// execute the preparedstatement
			preparedStmt.execute();
			rs = preparedStmt.getResultSet();
			
			if(rs.first())
				return true;
			
			return false;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	@Override
	Login singleResult(ResultSet r) {
			try {
				return new Login(r.getString("username"), r.getString("password"));
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return null;
	}

}
