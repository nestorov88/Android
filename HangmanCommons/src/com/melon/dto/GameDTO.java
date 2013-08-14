package com.melon.dto;

import java.util.ArrayList;

public class GameDTO {

	private Integer id;
	private Boolean result;
	private Boolean wholeWordGuessed;
	private WordDTO wordDTO;
	private UserDTO userDTO;
	
	private ArrayList<String> guestedLetters;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	public Boolean getWholeWordGuessed() {
		return wholeWordGuessed;
	}
	public void setWholeWordGuessed(Boolean wholeWordGuessed) {
		this.wholeWordGuessed = wholeWordGuessed;
	}
	public ArrayList<String> getGuestedLetters() {
		return guestedLetters;
	}
	public void setGuestedLetters(ArrayList<String> guestedLetters) {
		this.guestedLetters = guestedLetters;
	}
	public WordDTO getWordDTO() {
		return wordDTO;
	}
	public void setWordDTO(WordDTO wordDTO) {
		this.wordDTO = wordDTO;
	}
	public UserDTO getUserDTO() {
		return userDTO;
	}
	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
	
	
}
