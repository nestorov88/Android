package com.melon.dto;

import java.util.ArrayList;

public class CategoryDTO {

	private Integer id;
	private String name;
	private ArrayList<WordDTO> wordList;
	
	public CategoryDTO(String name) {
		this.name = name;
		wordList = new ArrayList<WordDTO>();
	}
	
	public CategoryDTO() {
		wordList = new ArrayList<WordDTO>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<WordDTO> getWordList() {
		return wordList;
	}
	public void setWordList(ArrayList<WordDTO> wordList) {
		this.wordList = wordList;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}
}
