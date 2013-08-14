package com.melon.interfaces;

import java.util.ArrayList;

import android.app.Fragment;

import com.melon.dto.CategoryDTO;
import com.melon.dto.UserDTO;
import com.melon.dto.WordDTO;

public interface FragmentListener {

	public UserDTO getUser();
	
	public void setUser(UserDTO user);
	
	public void changeFragment(Fragment fr, String fragmentTag);
	public void changeFragment(Fragment fr, String fragmentTag, Boolean addToBackstack);
	
	public void setCategoriesList(ArrayList<CategoryDTO> list);
	public ArrayList<CategoryDTO> getCategoriesList();
	
	public void setWordToEdit(WordDTO word);
	public WordDTO getWordToEdit();
	
}
