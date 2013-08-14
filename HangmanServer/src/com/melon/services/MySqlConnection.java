package com.melon.services;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class MySqlConnection {
	
    private String dbUrl = "jdbc:mysql://127.0.0.1/Hangman";
	private String dbUser = "hangman";
	private String dbPass = "hail";
	private Connection con;
	private static int nextPlayerID = -1;
	private PreparedStatement pState = null;
	private ResultSet rs = null;
	
	public MySqlConnection()  {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public Connection getCon()  {
		try {
			con = DriverManager.getConnection(dbUrl,dbUser,dbPass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	
	public int getNextPlayerID() {
		PreparedStatement pState = null;
		ResultSet rs = null;
		int max = -2;
		try {
			pState = getCon().prepareStatement("SELECT max(ID) FROM players");
			rs = pState.executeQuery();
			rs.first();
			max = rs.getInt(1);
		} catch (SQLException e) {
			
		}
		
		if(nextPlayerID <= max) {
			nextPlayerID = max + 1;
		} else {
			nextPlayerID += 1;
		}
		
		return nextPlayerID;
	}
	
	private String getCountryNameById(Integer id) {
			String name = null;
			try {
				pState = getCon().prepareStatement("select * from country where id=1");
				rs = pState.executeQuery();
				rs.first();
				name = rs.getString("name");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return name;
	}
}
