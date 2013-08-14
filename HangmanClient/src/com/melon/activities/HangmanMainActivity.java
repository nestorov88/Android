package com.melon.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.hangmanclient.R;
import com.melon.dto.CategoryDTO;
import com.melon.dto.GameDTO;
import com.melon.dto.UserDTO;
import com.melon.dto.WordDTO;
import com.melon.fragments.EditWordFragment;
import com.melon.fragments.LoginFrament;
import com.melon.fragments.WordsListFragment;
import com.melon.interfaces.FragmentListener;
import com.melon.views.ExpandableItemView.ExpandableViewContextMenuInfo;

public class HangmanMainActivity extends Activity implements FragmentListener {

	private UserDTO user;
	private WordDTO wordToEdit;
	private ArrayList<CategoryDTO> categoriesList;
	private GameDTO gameToPlay;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hangman_main);

		if(savedInstanceState == null) {
			getFragmentManager().beginTransaction().replace(R.id.layout, new LoginFrament()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hangman_main, menu);
		return true;
	}

	@Override
	public UserDTO getUser() {
		return user;
	}

	@Override
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	public void changeFragment(Fragment fr, String fragmentTag) {
		changeFragment(fr, fragmentTag, false);
	}
	
	public void changeFragment(Fragment fr, String fragmentTag, Boolean addToBackStack) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.layout, fr, fragmentTag);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		if(addToBackStack) {
			ft.addToBackStack(null);
		}
		
		ft.commit();
	}


	@Override
	public void setCategoriesList(ArrayList<CategoryDTO> list) {
		// TODO Auto-generated method stub
		this.categoriesList = list;
	}

	@Override
	public ArrayList<CategoryDTO> getCategoriesList() {
		// TODO Auto-generated method stub
		return categoriesList;
	}

	@Override
	public void setWordToEdit(WordDTO word) {
		// TODO Auto-generated method stub
		this.wordToEdit = word;
	}

	@Override
	public WordDTO getWordToEdit() {
		return wordToEdit;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		Log.i("", "onCreateContextMenu");
		menu.setHeaderTitle("Context Menu");  
	    menu.add(0, v.getId(), 0, "Edit");  
	    menu.add(0, v.getId(), 0, "Delete"); 
	    super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		final ExpandableViewContextMenuInfo info = (ExpandableViewContextMenuInfo) item.getMenuInfo();
		
		if(item.getTitle().equals("Delete")) {
			WordsListFragment wordListFragment = (WordsListFragment)getFragmentManager().findFragmentByTag("words_list");
			wordListFragment.getAdapter().deleteItem(info.groupPosition, info.childPosition);
			wordListFragment.refreshWordExpandableList(true);
		} else if(item.getTitle().equals("Edit")) {
			setWordToEdit(info.word);
			changeFragment(new EditWordFragment(), "edit_word", true);
		
		}
		return true;
	}

	@Override
	public GameDTO getGame() {
		return gameToPlay;
	}

	@Override
	public void setGame(GameDTO game) {
		this.gameToPlay = game;
	}
	
}
