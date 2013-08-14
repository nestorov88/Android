package com.melon.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.melon.dto.UserDTO;
import com.melon.utils.DBUtils;

public class UserService {
	
	private static final String TAG = UserService.class.getSimpleName();
	private static MySqlConnection mysql = new MySqlConnection();
	private static Logger Log = Logger.getLogger(GameService.class);
	private static final String TABLE_NAME = "users";
	
	private static final String USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
	private static final String SAVE_USER_QUERY = "INSERT INTO users VALUE (?, ?)";
	private static final String USER_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
	
	
	public static UserDTO getUserByEmail(String email) {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return getUserByEmail(conn,email);
		}
		
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Log.error("Error while clossing connection - getUserByEmail()", e);
			}
		}
		

	}
	
	public static UserDTO getUserByEmail(Connection conn, String email) {
		
		PreparedStatement pState = null;
		ResultSet rs = null;		
		UserDTO user = null;
		
		try {
			pState = conn.prepareStatement(USER_BY_EMAIL_QUERY);
			pState.setString(1, email);
			
			rs = pState.executeQuery();
			
			
			if (rs.first()) {
				user = loadFromResultSet(conn, rs);
			}
			
		} catch (SQLException e) {
			Log.error("Error during getUserByEmail()",e);
		}
		
		return user;
	}
	
	public static UserDTO saveUser(UserDTO user) {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return saveUser(conn,user);
		}
		
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Log.error("Error while clossing connection - saveWord()");
			}
		}
		

	}
	
	public static UserDTO saveUser(Connection conn, UserDTO user) {
		
		PreparedStatement pState = null;
		ResultSet rs = null;		
		
		try {
			user.setId(DBUtils.getId(TABLE_NAME));
			pState = conn.prepareStatement(SAVE_USER_QUERY);
			pState.setInt(1, user.getId());
			pState.setString(2, user.getEmail());
			pState.executeUpdate();
		} catch (SQLException e) {
			Log.error("Error during saveUser()",e);
		}
		
		return user;
	}
	
	public static UserDTO getUserById(Integer id) {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return getUserById(conn, id);
		}
		
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Log.error("Error while clossing connection - getUserById()");
			}
		}
		
	}
	
	public static UserDTO getUserById(Connection conn, Integer id) {
		
		UserDTO user = null;
		
		PreparedStatement pState = null;
		ResultSet rs = null;
		
		try {
			pState = conn.prepareStatement(USER_BY_ID_QUERY);
			pState.setInt(1, id);
			rs = pState.executeQuery();
			rs.first();
			
			user = loadFromResultSet(conn, rs);
		} catch (SQLException e) {
			Log.error("Error during getUserById()",e);
		}
		

		
		return user;
	}
	
	
	private static UserDTO loadFromResultSet(Connection conn, ResultSet rs) throws SQLException {
		UserDTO user = new UserDTO();
		
		user.setId(rs.getInt("id"));
		user.setEmail(rs.getString("email"));
		user.setGamesList(GameService.getAllGamesByUserId(user.getId()));
		
		return user;
	}
	
}
