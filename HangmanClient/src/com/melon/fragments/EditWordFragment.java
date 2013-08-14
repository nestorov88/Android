package com.melon.fragments;

import java.util.ArrayList;

import com.example.hangmanclient.R;
import com.melon.dto.CategoryDTO;
import com.melon.dto.UserDTO;
import com.melon.dto.WordDTO;
import com.melon.interfaces.FragmentListener;
import com.melon.utils.Manager;
import com.melon.utils.NNAsyncTask;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditWordFragment extends Fragment{
	
	private FragmentListener mListener;
	private UserDTO user;
	private ArrayList<CategoryDTO> categoryList ;
	private EditText etDescription;
	private EditText etWord;
	private Button btnEditWord;
	private Spinner spinnerCategory;
	private WordDTO wordToEdit;
	private ArrayAdapter<CategoryDTO> categoryAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_edit_word_layout, container, false);
		spinnerCategory = (Spinner) view.findViewById(R.id.spinnerEditCategory);
		
		if(wordToEdit != null) {
			etDescription = (EditText) view.findViewById(R.id.etDescription);
			etDescription.setText(wordToEdit.getDescription());
			
			etWord = (EditText) view.findViewById(R.id.etWord);
			etWord.setText(wordToEdit.getWord());
		}
		
		btnEditWord = (Button) view.findViewById(R.id.btnEditWord);
		btnEditWord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editWord(wordToEdit);
				WordsListFragment fr = (WordsListFragment) getFragmentManager().findFragmentByTag("words_list");
				if (fr != null) {
					fr.refreshWordExpandableList(true);
				}
			}
		});
		
		
		if(categoryList == null || (categoryList.size() == 0)) {
			new NNAsyncTask() {
				
				@Override
				public boolean onPostLoad() {
					prepareEditLayout();
					return false;
				}
				
				@Override
				public boolean onLoad() {
					// TODO Auto-generated method stub
					categoryList = Manager.getServiceClient().getAllCetogiries();
					return false;
				}
			}.execute();
		} else {
			// TODO Auto-generated method stub
			prepareEditLayout();
		}

		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setRetainInstance(true);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		setHasOptionsMenu(true);
        mListener = (FragmentListener) activity;
        if(mListener.getUser() == null) {
        	mListener.setUser(user);
        } else {
        	user = mListener.getUser();
        }
        
        if(mListener.getCategoriesList() == null) {
        	mListener.setCategoriesList(categoryList);
        } else {
        	categoryList = mListener.getCategoriesList();
        }
        
        if(mListener.getWordToEdit() == null) {
        	mListener.setWordToEdit(wordToEdit);
        } else {
        	wordToEdit = mListener.getWordToEdit();
        }

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		inflater.inflate(R.menu.edit_word_menu, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.addWord:
			mListener.changeFragment(new AddWordFragment(), "add_word", true);
			return true;
		default:
			return false;
		}
	}
	private void editWord(final WordDTO wordToEdit) {
		final String word = etWord.getText().toString();
		String description = etDescription.getText().toString();
		
		if(word != null && !word.equals("")) {
			
			CategoryDTO category = (CategoryDTO) spinnerCategory.getSelectedItem();
			wordToEdit.setUserDTO(user);
			wordToEdit.setWord(word);
			wordToEdit.setCategoryId(category.getId());
			if(description != null && !description.equals("")) {
				wordToEdit.setDescription(description);
			}
			
			new NNAsyncTask() {
				
				@Override
				public boolean onPostLoad() {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "Word '" + word + "' was edited.", Toast.LENGTH_SHORT).show();
					mListener.setWordToEdit(null);
					getFragmentManager().popBackStack();
					return false;
				}
				
				@Override
				public boolean onLoad() {
					// TODO Auto-generated method stub
					Manager.getServiceClient().saveWord(wordToEdit);
					return false;
				}
			}.execute();
		}
	}
	
	public void prepareEditLayout() {
		CategoryDTO categoryToSelect = null;
		if(wordToEdit != null) {
			for(CategoryDTO category : categoryList) {
				if(category.getId().equals(wordToEdit.getCategoryId().intValue())) {
					categoryToSelect = category;
				}
			}
		}
		categoryAdapter = new ArrayAdapter<CategoryDTO>(getActivity(), android.R.layout.simple_spinner_item, categoryList);
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCategory.setAdapter(categoryAdapter);
		if(categoryToSelect != null) {
			spinnerCategory.setSelection(categoryAdapter.getPosition(categoryToSelect));
		}	
	}
}
