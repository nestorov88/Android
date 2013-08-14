package com.melon.fragments;

import com.example.hangmanclient.R;
import com.melon.activities.EditWordActivity;
import com.melon.dto.UserDTO;
import com.melon.interfaces.FragmentListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class UserDetailsFragment extends Fragment{
	
	private FragmentListener mListener;
	private TextView txtUserName;
	private TextView txtUserId;
	private UserDTO user;
	private static final String TAG = UserDetailsFragment.class.getSimpleName();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_user_details_layout, container, false);
		
        InputMethodManager in = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        
		txtUserName = (TextView) view.findViewById(R.id.txtUserName);
		txtUserId = (TextView) view.findViewById(R.id.txtUserId);
		
		if(user == null) {
			user = mListener.getUser();
		}
		
		txtUserId.setText(String.valueOf(user.getId()));
		txtUserName.setText(user.getEmail());

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		setHasOptionsMenu(true);
        try {
            mListener = (FragmentListener) activity;
            if(mListener.getUser() == null) {
            	mListener.setUser(user);
            	Log.i(TAG,"Setting1 user: " + user);
            } else {
            	Log.i(TAG,"Setting2 user: " + user);
            	user = mListener.getUser();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentListener");
        }
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setRetainInstance(true);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		inflater.inflate(R.menu.user_details_menu, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.editMyWords:
/*			Intent intent = new Intent(getActivity(), EditWordActivity.class);
			intent.putExtra("user", user);
			getActivity().startActivity(intent);*/
			mListener.changeFragment(new WordsListFragment(), "words_list", true);
			return true;
		case R.id.startGame:

			mListener.changeFragment(new HangmanFragment(), "hangman", true);
		default:
			return false;
		}
	}
	
	
	
	

}
