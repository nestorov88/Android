package com.melon.activities;

import java.util.ArrayList;

import com.example.hangmanclient.R;
import com.melon.dto.CategoryDTO;
import com.melon.dto.GameDTO;
import com.melon.dto.UserDTO;
import com.melon.dto.WordDTO;
import com.melon.fragments.EditWordFragment;
import com.melon.fragments.WordsListFragment;
import com.melon.interfaces.FragmentListener;
import com.melon.views.ExpandableItemView.ExpandableViewContextMenuInfo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public class EditWordActivity extends Activity implements FragmentListener{
	
	private UserDTO user;
	private WordDTO wordToEdit;
	private ArrayList<CategoryDTO> categoriesList;
	private static final String TAG = EditWordActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if(user == null) {
			user = (UserDTO) getIntent().getSerializableExtra("user");
			Log.i(TAG, "User: " + user);			
		}

		setContentView(R.layout.activity_edit_word_layout);
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
		ft.replace(R.id.wordListFragment, fr, fragmentTag);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		if(addToBackStack) {
			ft.addToBackStack(null);
		}
		
		ft.commit();
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
			WordsListFragment wordListFragment = (WordsListFragment)getFragmentManager().findFragmentById(R.id.wordListFragment);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGame(GameDTO game) {
		// TODO Auto-generated method stub
		
	}
}
