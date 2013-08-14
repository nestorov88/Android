package com.melon.services;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.melon.dto.GameDTO;
import com.melon.dto.UserDTO;
import com.melon.dto.WordDTO;
import com.melon.interfaces.IHangmanServices;

@Path("/Melon2")
public class HangmanService {
	
	private static final String TAG = HangmanService.class.getSimpleName();
	
    ObjectMapper mapper = new ObjectMapper();
    Logger log = Logger.getLogger(HangmanService.class);
    //Logger log = Logger.getLogger(getClass());

    
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html><body><h1>" + "Hello " + "</h1></body>";
	}
	
	private String processException(Exception e) {
		try {
			return mapper.writeValueAsString(e);
	        } catch (Throwable t) {
	            log.fatal("Failed to map exception!", t);
	            return "";
	        }
	    }


	@POST
	@Path("/saveUser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String saveUser(@FormParam("user") String user) {
		
		try {
			String result = mapper.writeValueAsString(UserService.saveUser(mapper.readValue(user, UserDTO.class)));
			log.info("saveUser result: "+ result);
			return result;
		} catch (Exception e) {
			return processException(e);
		} catch (Throwable t) {
			return "";
		}

	}
	
	@POST
	@Path("/saveWord")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String saveWord(@FormParam("word") String word) {
		
		try {
			String result = mapper.writeValueAsString(WordService.saveWord(mapper.readValue(word, WordDTO.class)));
			log.info("saveWord result: "+ result);
			return result;
		} catch (Exception e) {
			return processException(e);
		} catch (Throwable t) {
			return "";
		}

	}
	
	@POST
	@Path("/saveGame")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String saveGame(@FormParam("game") String game) {
		
		try {
			String result = mapper.writeValueAsString(GameService.saveGame(mapper.readValue(game, GameDTO.class)));
			log.info("saveWord result: "+ result);
			return result;
		} catch (Exception e) {
			return processException(e);
		} catch (Throwable t) {
			return "";
		}

	}
	
	@POST
	@Path("/deleteWordById")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteWordById(@FormParam("wordId") String wordId) {
		
		try {
			WordService.deleteWordById(Integer.valueOf(wordId));
			return "success";
		} catch (Exception e) {
			return processException(e);
		} catch (Throwable t) {
			return "";
		}

	}
	
	@POST
	@Path("/getUserByEmail")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String getUserByEmail(@FormParam("email") String email) {
		
		try {
			String result = mapper.writeValueAsString(UserService.getUserByEmail(email));
			log.info(result);
			return result;
		} catch (Exception e) {
			return processException(e);
		} catch (Throwable t) {
			return "";
		}

	}
	
	@POST
	@Path("/getAllGussedWordByUserId")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String getAllGussedWordByUserId(@FormParam("userId") String userId) {
		
		try {
			String result = mapper.writeValueAsString(WordService.getAllGussedWordByUserId(Integer.valueOf(userId)));
			log.info(result);
			return result;
		} catch (Exception e) {
			return processException(e);
		} catch (Throwable t) {
			return "";
		}

	}
	
	@POST
	@Path("/getAllCetogiries")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String getUserByEmail() {
		
		try {
			String result = mapper.writeValueAsString(CategoryService.getAllCetogiries());
			log.info(result);
			return result;
		} catch (Exception e) {
			return processException(e);
		} catch (Throwable t) {
			return "";
		}

	}
}
