package com.melon.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.melon.dto.GameDTO;
import com.melon.dto.WordDTO;
import com.melon.utils.DBUtils;

public class GameService {

	private static final String TAG = GameService.class.getSimpleName();
	private static MySqlConnection mysql = new MySqlConnection();
	private static Logger Log = Logger.getLogger(GameService.class);
	private static ObjectMapper mapper = new ObjectMapper();
	
	private static final String TABLE_NAME = "games";
	private static final String ALL_GAMES_BY_USER_ID_QUERY = "SELECT * FROM games WHERE user_id = ?";
	private static final String GAME_BY_ID_QUERY = "SELECT * FROM games WHERE id = ?";
	
	public static ArrayList<GameDTO> getAllGamesByUserId(Integer id) {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return getAllGamesByUserId(conn, id);
		}
		
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Log.error("Error while clossing connection - getAllGamesByUserId()");
			}
		}
		
	}
	
	public static ArrayList<GameDTO> getAllGamesByUserId(Connection conn, Integer id) {
		
		ArrayList<GameDTO> gamesList = new ArrayList<GameDTO>();
		
		PreparedStatement pState = null;
		ResultSet rs = null;
		
		
		try {
			pState = conn.prepareStatement(ALL_GAMES_BY_USER_ID_QUERY);
			pState.setInt(1, id);
			rs = pState.executeQuery();
			
			while(rs.next()) {
				GameDTO game = loadFromResultSet(conn, rs);
				gamesList.add(game);
			}
		} catch (Exception e) {
			Log.error("Error during getAllGamesByUserId()",e);
		} 
		return gamesList;
	}
	
	public static GameDTO saveGame(GameDTO game) {
		Connection conn = null;
		
		try {
			conn = mysql.getCon();
			return saveGame(conn,game);
		} catch(SQLException e) {
			Log.error("Error during saveGame()",e);
		}
		
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Log.error("Error while clossing connection - saveGame()");
			}
		}
		
		return null;
	}
	
	public static GameDTO saveGame(Connection conn, GameDTO game) throws SQLException {
		
		PreparedStatement pState = conn.prepareStatement(GAME_BY_ID_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		if(game.getId() == null) {
			pState.setNull(1, Types.INTEGER);
		} else {
			pState.setInt(1, game.getId());
		}
			
		
		ResultSet rs = null;
		boolean insert = false;
		
		rs = pState.executeQuery();
		if(!rs.next()) {
			rs.moveToInsertRow();
			rs.updateInt("id", DBUtils.getId(TABLE_NAME));
			insert = true;
		}

		rs.updateInt("user_id", game.getUserId());
		rs.updateInt("word_id", game.getWordID());
		rs.updateBoolean("result", game.getResult());
		rs.updateBoolean("whole_word_guess", game.getWholeWordGuessed());
		
		if(insert) {
			rs.insertRow();
		} else {
			rs.updateRow();
		}

		return game;
	}
	
	private static GameDTO loadFromResultSet(Connection conn,ResultSet rs) throws SQLException, JsonParseException, JsonMappingException, IOException {
		GameDTO game = new GameDTO();
		game.setId(rs.getInt("id"));
		game.setUserId(rs.getInt("user_id"));
		game.setWordID(rs.getInt("word_id"));
		game.setResult(rs.getBoolean("result"));
		game.setWholeWordGuessed(rs.getBoolean("whole_word_guess"));
//		game.setGuestedLetters((ArrayList<String>)mapper.readValue(rs.getString("guested_letters"), new TypeReference<ArrayList<String>>() {}));
		return game;
	}
	
}
