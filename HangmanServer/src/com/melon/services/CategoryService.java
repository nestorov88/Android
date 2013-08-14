package com.melon.services;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.melon.dto.CategoryDTO;
import com.melon.services.MySqlConnection;
import com.melon.services.WordService;


public class CategoryService {

	private static final String TAG = "CategoriesService";
	
	private static final String QUERY_ALL_CATEGORIES = "SELECT * FROM categories";
	private static final String QUERY_CATEGORY_BY_IDS = "SELECT * FROM categories WHERE id = ?";
	private static MySqlConnection mysql = new MySqlConnection();
	private static Logger Log = Logger.getLogger(CategoryService.class);
	
	public static ArrayList<CategoryDTO> getAllCetogiries() throws SQLException {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return getAllCetogiries(conn);
		} catch(SQLException e) {
			Log.error("Error during getAllCetogiries() ", e);
		}
		
		finally {
			conn.close();
		}
		
		return null;

	}
	
	public static ArrayList<CategoryDTO> getAllCetogiries(Connection conn) throws SQLException {
		ArrayList<CategoryDTO> categoriestList = new ArrayList<CategoryDTO>();
		
		PreparedStatement pState = null;
		ResultSet rs = null;
		
		
		pState = conn.prepareStatement(QUERY_ALL_CATEGORIES);
		rs = pState.executeQuery();
		while(rs.next()){
			CategoryDTO categoryDTO = loadFromResultSet(conn, rs);
			categoriestList.add(categoryDTO);
		}

		return categoriestList;
	}
	
	public static CategoryDTO getCategoryById(int id) throws SQLException {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return getCategoryById(id,conn);
		} catch(SQLException e) {
			Log.error("Error during getCategoryById(int id)");
		}
		
		finally {
			conn.close();
		}
		
		return null;

	}
	
	public static CategoryDTO getCategoryById(int id,Connection conn) throws SQLException {
		
		
		PreparedStatement pState = null;
		ResultSet rs = null;
		
		
		pState = conn.prepareStatement(QUERY_CATEGORY_BY_IDS);
		pState.setInt(1, id);
		rs = pState.executeQuery();
		CategoryDTO categoryDTO = null;
		
		while(rs.next()){
			categoryDTO = loadFromResultSet(conn, rs);
			
		}

		return categoryDTO;
	}
	
	public static CategoryDTO loadFromResultSet(Connection conn, ResultSet rs) throws SQLException {
		CategoryDTO categoryDTO = new CategoryDTO();
		
		categoryDTO.setId(rs.getInt("id"));
		categoryDTO.setName(rs.getString("category_name"));
		categoryDTO.setWordList(WordService.getAllWordsByCategory(conn, categoryDTO.getId()));
		
		return categoryDTO;
	}
	
}

