package com.melon.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String email;
	private ArrayList<GameDTO> gamesList;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ArrayList<GameDTO> getGamesList() {
		return gamesList;
	}
	public void setGamesList(ArrayList<GameDTO> gamesList) {
		this.gamesList = gamesList;
	}
	
	
}
