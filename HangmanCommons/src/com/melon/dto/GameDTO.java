package com.melon.dto;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


public class GameDTO {

	private Integer id;
	private Boolean result;
	private Boolean wholeWordGuessed;
	private Integer userId;
	private Integer wordID;
	
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
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getWordID() {
		return wordID;
	}
	public void setWordID(Integer wordID) {
		this.wordID = wordID;
	}

	
	
	
}
