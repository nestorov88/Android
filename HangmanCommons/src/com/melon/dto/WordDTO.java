package com.melon.dto;

public class WordDTO {

	private Integer id;
	private String word;
	private String description;
	private Integer categoryId;
	private UserDTO userDTO;
	
	
	public String getWord() {
		return word;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
		
	public UserDTO getUserDTO() {
		return userDTO;
	}
	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
	@Override
	public String toString() {
		return word;
	}
	
}
