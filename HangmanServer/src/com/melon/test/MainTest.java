package com.melon.test;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

import com.melon.dto.CategoryDTO;
import com.melon.dto.UserDTO;
import com.melon.dto.WordDTO;
import com.melon.services.CategoryService;
import com.melon.services.UserService;
import com.melon.services.WordService;


public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws SQLException {
		
		
		ArrayList<WordDTO> wordsList = WordService.getAllWordsByCategory(1);
		System.out.println(wordsList.size());
		
		WordDTO word = new WordDTO();
		UserDTO user = new UserDTO();
		user.setId(13);
		word.setCategoryId(1);
		word.setId(36);
		word.setWord("Plamen");
		word.setUserDTO(user);
		word.setDescription("KUFTEEE");
		
		WordService.saveWord(word);
		
/*		ArrayList<CategoryDTO> categoriesList = CategoryService.getAllCetogiries();
		for(int i = 0; i < categoriesList.get(0).getWordList().size(); i++) {
			System.out.println("Word: " + categoriesList.get(0).getWordList().get(i).getWord());
			System.out.println("Description: " + categoriesList.get(0).getWordList().get(i).getDescription());
		}*/
		
/*		UserDTO user = new UserDTO();
		
		user.setEmail("nestorov88@gmail.com");
		UserService.saveUser(user);*/
		
/*		System.out.println(UserService.getUserByEmail("hi").getId());
		WordService.deleteWordById(6);*/
	}

}
