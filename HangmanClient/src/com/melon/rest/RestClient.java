package com.melon.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.melon.dto.CategoryDTO;
import com.melon.dto.GameDTO;
import com.melon.dto.UserDTO;
import com.melon.dto.WordDTO;

import android.util.Log;





public class RestClient extends RestClientBase{

	   private static final String TAG = "RestClient";
	   private ObjectMapper mapper = new ObjectMapper();
	   
	   
	    public RestClient() {
//	        super("172.16.1.214", 18080);
	    	super("212.25.61.159", 18080);
	    }
	    

	    public UserDTO saveUser(UserDTO user) {
	    	
	    	Map<String, String> params = new HashMap<String, String>();
	    	
	    	try {
	    		params.put("user", mapper.writeValueAsString(user));
				return post("/HangmanServer/rest/Melon2/saveUser", new TypeReference<UserDTO>() { },params);
			} catch (Exception e) {
				Log.e(TAG, "Error during saveUser", e);
				return null;
			}
	    }
	    
	    public GameDTO saveGame(GameDTO game) {
	    	
	    	Map<String, String> params = new HashMap<String, String>();
	    	Log.i(TAG, "Save game: " + game);
	    	try {
	    		params.put("game", mapper.writeValueAsString(game));
				return post("/HangmanServer/rest/Melon2/saveGame", new TypeReference<GameDTO>() { },params);
			} catch (Exception e) {
				Log.e(TAG, "Error during saveGame", e);
				return null;
			}
	    }	
	    
	    public WordDTO saveWord(WordDTO word) {
	    	
	    	Map<String, String> params = new HashMap<String, String>();
	    	
	    	try {
	    		params.put("word", mapper.writeValueAsString(word));
				return post("/HangmanServer/rest/Melon2/saveWord", new TypeReference<WordDTO>() { },params);
			} catch (Exception e) {
				Log.e(TAG, "Error during saveWord", e);
				return null;
			}
	    }
	    
	    public void deleteWordById(Integer id) {
	    	Map<String, String> params = new HashMap<String, String>();
	    	
	    	try {
	    		params.put("wordId", mapper.writeValueAsString(id));
	    		post("/HangmanServer/rest/Melon2/deleteWordById",params);
			} catch (Exception e) {
				Log.e(TAG, "Error during saveUser", e);
			}
	    }
	    
	    public UserDTO getUserByEmail(String email) {
	    	
	    	Map<String, String> params = new HashMap<String, String>();
	    	
	    	try {
	    		params.put("email", email);
				return post("/HangmanServer/rest/Melon2/getUserByEmail", new TypeReference<UserDTO>() { },params);
			} catch (Exception e) {
				Log.e(TAG, "Error during getUserByEmail", e);
				return null;
			}
	    }
	    
	    public ArrayList<CategoryDTO> getAllCetogiries() {
	    	
	    	Map<String, String> params = new HashMap<String, String>();
	    	
	    	try {

				return post("/HangmanServer/rest/Melon2/getAllCetogiries", new TypeReference<ArrayList<CategoryDTO>>() { },params);
			} catch (Exception e) {
				Log.e(TAG, "Error during getUserByEmail", e);
				return null;
			}
	    }	  
	    public ArrayList<WordDTO> getAllGussedWordByUserId(Integer userId) {
	    	
	    	Map<String, String> params = new HashMap<String, String>();
	    	params.put("userId", String.valueOf(userId));
	    	try {

				return post("/HangmanServer/rest/Melon2/getAllGussedWordByUserId", new TypeReference<ArrayList<WordDTO>>() { },params);
			} catch (Exception e) {
				Log.e(TAG, "Error during getAllGussedWordByUserId", e);
				return null;
			}
	    }
}
