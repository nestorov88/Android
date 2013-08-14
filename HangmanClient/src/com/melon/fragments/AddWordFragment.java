package com.melon.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hangmanclient.R;
import com.melon.adapters.CategoriesExpandableAdapter;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddWordFragment extends Fragment{

	private FragmentListener mListener;
	private UserDTO user;
	private ArrayList<CategoryDTO> categoryList ;
	private EditText etDescription;
	private EditText etWord;
	private Button btnAddWord;
	private ArrayAdapter<CategoryDTO> categoryAdapter;
	
	private Spinner spinnerCategory;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_word_layout, container, false);
		
		spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
		
		etDescription = (EditText) view.findViewById(R.id.etDescription);
		
		etWord = (EditText) view.findViewById(R.id.etAddWord);
		
		btnAddWord = (Button) view.findViewById(R.id.btnAddWord);
		btnAddWord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addWord();
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
					// TODO Auto-generated method stub
					categoryAdapter = new ArrayAdapter<CategoryDTO>(getActivity(), android.R.layout.simple_spinner_item, categoryList);
					categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinnerCategory.setAdapter(categoryAdapter);
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
			categoryAdapter = new ArrayAdapter<CategoryDTO>(getActivity(), android.R.layout.simple_spinner_item, categoryList);
			categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerCategory.setAdapter(categoryAdapter);
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
		super.onAttach(activity);
	}
	
	private void addWord() {
		final String word = etWord.getText().toString();
		String description = etDescription.getText().toString();
		
		if(word != null && !word.equals("")) {
			final WordDTO wordToAdd = new WordDTO();
			CategoryDTO category = (CategoryDTO) spinnerCategory.getSelectedItem();
			wordToAdd.setUserDTO(user);
			wordToAdd.setWord(word);
			wordToAdd.setCategoryId(category.getId());
			if(description != null && !description.equals("")) {
				wordToAdd.setDescription(description);
			}
			
			new NNAsyncTask() {
				
				@Override
				public boolean onPostLoad() {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "Word '" + word + "' was added.", Toast.LENGTH_SHORT).show();
					getFragmentManager().popBackStack();
					return false;
				}
				
				@Override
				public boolean onLoad() {
					// TODO Auto-generated method stub
					Manager.getServiceClient().saveWord(wordToAdd);
					return false;
				}
			}.execute();
		}
	}
}
