package com.melon.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.melon.dto.WordDTO;
import com.melon.utils.DBUtils;

public class WordService {

	private static final String TAG = WordService.class.getSimpleName();
	private static MySqlConnection mysql = new MySqlConnection();
	private static Logger Log = Logger.getLogger(WordService.class);
	private static final String TABLE_NAME = "words";
	
	private static final String ALL_WORDS_BY_CATEGORY_ID_QUERY = "SELECT * FROM words WHERE category_id = ?";
	private static final String WORD_BY_ID_QUERY = "SELECT * FROM words WHERE id = ?";
	private static final String ADD_WORD_QUERY = "INSERT INTO words VALUE (?,?,?,?,?)";
	private static final String DELETE_WORD_BY_ID_QUERY = "DELETE FROM words WHERE id = ?";
	
	public static void deleteWordById(Integer id) {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			deleteWordById(conn,id);
		} 
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Log.error("Error while clossing connection - saveWord()");
			}
		}
		

	}
	
	public static void deleteWordById(Connection conn, Integer id) {

		
		PreparedStatement pState = null;
		ResultSet rs = null;
			
		try {
			pState = conn.prepareStatement(DELETE_WORD_BY_ID_QUERY);
			pState.setInt(1, id);
			
			pState.executeUpdate();
		} catch (SQLException e) {
			Log.error("Error during - deleteWordById()", e);
		}
;
		

	}
			
	public static WordDTO saveWord(WordDTO word) {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return saveWord(conn,word);
		} catch(SQLException e) {
			Log.error("Error during saveWord()",e);
		}
		
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Log.error("Error while clossing connection - saveWord()");
			}
		}
		
		return null;
	}
	
	public static WordDTO saveWord(Connection conn, WordDTO word) throws SQLException {
		
		PreparedStatement pState = conn.prepareStatement(WORD_BY_ID_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		if(word.getId() == null) {
			pState.setNull(1, Types.INTEGER);
		} else {
			pState.setInt(1, word.getId());
		}
			
		
		ResultSet rs = null;
		boolean insert = false;
		
		rs = pState.executeQuery();
		if(!rs.next()) {
			rs.moveToInsertRow();
			rs.updateInt("id", DBUtils.getId(TABLE_NAME));
			insert = true;
		}
		rs.updateString("word", word.getWord());
		rs.updateInt("category_id", word.getCategoryId());
		rs.updateInt("added_by_user_id", word.getUserDTO().getId());
		rs.updateString("description", word.getDescription());
		
		if(insert) {
			rs.insertRow();
		} else {
			rs.updateRow();
		}
/*		pState = conn.prepareStatement(ADD_WORD_QUERY);
		pState.setInt(1, word.getId());
		pState.setString(2, word.getWord());
		pState.setInt(3, word.getCategoryId());
		pState.setInt(4, word.getUserDTO().getId());
		pState.setString(5, word.getDescription());
		

		pState.executeUpdate();*/
		
		return word;
	}
	
	public static ArrayList<WordDTO> getAllWordsByCategory(Integer categoryId) {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return getAllWordsByCategory(conn, categoryId);
		} catch(SQLException e) {
			Log.error("Error during findAllWordsByCategory()",e);
		}
		
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Log.error("Error while clossing connection - findAllWordsByCategory()");
			}
		}
		
		return null;
	}
	
	public static ArrayList<WordDTO> getAllWordsByCategory(Connection conn, Integer categoryId) throws SQLException {
		
		ArrayList<WordDTO> wordsList = new ArrayList<WordDTO>();
		
		PreparedStatement pState = null;
		ResultSet rs = null;
		
		
		pState = conn.prepareStatement(ALL_WORDS_BY_CATEGORY_ID_QUERY);
		pState.setInt(1, categoryId);
		rs = pState.executeQuery();
		
		while(rs.next()) {
			WordDTO word = loadFromResultSet(conn, rs);
			wordsList.add(word);
		}
		
		return wordsList;
	}
	
	
	public static WordDTO getWordById(Integer id) {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return getWordById(conn, id);
		}
		
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Log.error("Error while clossing connection - getWordById()");
			}
		}
		
	}
	
	public static WordDTO getWordById(Connection conn, Integer id)  {
		
		WordDTO word = null;
		
		PreparedStatement pState = null;
		ResultSet rs = null;
		
		try {
			pState = conn.prepareStatement(WORD_BY_ID_QUERY);
			
			rs.first();
			
			word = loadFromResultSet(conn, rs);
		} catch (SQLException e) {
			Log.error("Error during getWordById()",e);
		}
		

		
		return word;
		
		
	}
	
	private static WordDTO loadFromResultSet(Connection conn, ResultSet rs) throws SQLException {
		
		WordDTO word = new WordDTO();
		word.setId(rs.getInt("id"));
		word.setWord(rs.getString("word"));
		word.setCategoryId(rs.getInt("category_id"));
		word.setUserDTO(UserService.getUserById(rs.getInt("added_by_user_id")));
		word.setDescription(rs.getString("description"));
		
		return word;
	}
	
}
