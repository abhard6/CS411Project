package edu.illinois.models;

import org.springframework.stereotype.Component;

import edu.illinois.Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component("UserDao")
public class UserDao extends BasicDao<Post>{

	public void insert(Registration greeting) {
		Connection conn = mySql.connection;
		String query = "INSERT INTO  users" + " (username,password) " +  " values (?, ?);";

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
		String query = "select 1 from users where username = ? and password = ?";
		
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
	Post singleResult(ResultSet r) {
		// TODO Auto-generated method stub
		return null;
	}

}
