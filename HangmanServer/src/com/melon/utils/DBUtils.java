package com.melon.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.melon.services.MySqlConnection;

public class DBUtils {

	public static synchronized Integer getId(String tableName) throws SQLException {
		
		MySqlConnection mysqlconn = new MySqlConnection();
		Integer id = null;
		
		PreparedStatement pState = null;
		ResultSet rs = null;
		
		String query = "SELECT MAX(id) FROM " + tableName;
		pState = mysqlconn.getCon().prepareStatement(query);
		
		rs = pState.executeQuery();
		
		rs.first();
		
		id = rs.getInt(1);
		id++;
		return id;
		
	}
	
}