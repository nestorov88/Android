package com.melon.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MySqlConnection {
	
    private String dbUrl = "jdbc:mysql://87.120.137.9:3306/magic_table";
	private String dbUser = "nestor";
	private String dbPass = "hail";
/*    private String dbUrl = "jdbc:mysql://127.0.0.1/PingPongDB";
	private String dbUser = "root";
	private String dbPass = "5282";*/
	private Connection con;
	private static int nextPlayerID = -1;
	
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
}
