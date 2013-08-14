package com.melon.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.example.hangmanclient.R;
import com.melon.adapters.CategoriesExpandableAdapter;
import com.melon.dto.CategoryDTO;
import com.melon.dto.UserDTO;
import com.melon.dto.WordDTO;
import com.melon.interfaces.FragmentListener;
import com.melon.rest.RestClient;
import com.melon.utils.Manager;
import com.melon.utils.NNAsyncTask;

public class WordsListFragment extends Fragment{

	private ExpandableListView expList;
	private CategoriesExpandableAdapter adapter;
	private FragmentListener mListener;
	private UserDTO user;
	
	private ArrayList<WordDTO> wordsList;
	private HashMap<CategoryDTO, List<WordDTO>> expandableList;
	private ArrayList<CategoryDTO> categoryList ;
	
	private static final String TAG = WordsListFragment.class.getSimpleName();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_words_list_layout, container, false);
		expList = (ExpandableListView) view.findViewById(R.id.expWordsList);
		expList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
//				
 
				getActivity().registerForContextMenu(v);
				getActivity().openContextMenu(v);
				getActivity().unregisterForContextMenu(v);


				return false;
			}
		});


		if(savedInstanceState == null) {
			refreshWordExpandableList(true);
		}
	
		
		return view;
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setRetainInstance(true);
		super.onActivityCreated(savedInstanceState);
	}

	public CategoriesExpandableAdapter getAdapter() {
		return adapter;
	}
	
	
	public void refreshWordExpandableList(final boolean refresh) {
		new NNAsyncTask(getActivity(), false) {
			
			@Override
			public boolean onPostLoad() {
				adapter = new CategoriesExpandableAdapter(getActivity(), categoryList, expandableList);
				expList.setAdapter(adapter);
				return false;
			}
			
			@Override
			public boolean onLoad() {
				// TODO Auto-generated method stub
//				Log.i(TAG, "refreshWordExpandableList");
				if(mListener.getCategoriesList() == null || refresh) {
//					Log.i(TAG,"Get all categories again");
					categoryList = Manager.getServiceClient().getAllCetogiries();
					mListener.setCategoriesList(categoryList);
				} else {
					categoryList = mListener.getCategoriesList();
				}
				
				wordsList = new ArrayList<WordDTO>();
				expandableList = new HashMap<CategoryDTO, List<WordDTO>>();
				
				for (int i = 0; i < categoryList.size(); i++) {
					CategoryDTO category = categoryList.get(i);
//					Log.i(TAG, "Category Name: " + category.getName() + " word list size: " + category.getWordList().size());
					ArrayList<WordDTO> wordsAddedByUserList = new ArrayList<WordDTO>();
					
					for(int j = 0; j < category.getWordList().size(); j++) {
						WordDTO word = category.getWordList().get(j);
//						Log.i(TAG, "User: " + user);
						
//						Log.i(TAG, "Word User ID: " + word.getUserDTO().getId() + " Saved User ID: " + user.getId());
						if(word.getUserDTO().getId().equals(user.getId())) {
							wordsAddedByUserList.add(word);
						}
						
					}
//					Log.i(TAG, "Category Name: " + category.getName() + " word list size: " + wordsAddedByUserList.size());
					expandableList.put(category, wordsAddedByUserList);
					wordsList.addAll(category.getWordList());
				}
				
				return false;
			}
		}.execute();
	}
	
}
