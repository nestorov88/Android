package com.melon.fragments;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hangmanclient.R;
import com.melon.dto.UserDTO;
import com.melon.interfaces.FragmentListener;
import com.melon.rest.RestClient;
import com.melon.utils.NNAsyncTask;

public class LoginFrament extends Fragment {

	private FragmentListener mListener;
	private EditText etEmail;
	private Button btnLogin;
	private RestClient rc = new RestClient();
	private static final String TAG = LoginFrament.class.getSimpleName();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login_layout, container, false);
		
		etEmail = (EditText) view.findViewById(R.id.edTxtUserEmail);
		
		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = etEmail.getText().toString();
				btnLogin.setEnabled(false);
				login(email);
				
			}
		});
		
		return view;
	}

	
	private void login(final String email) {
		if(email != null && !email.equals("") && validEmail(email)) {
			new NNAsyncTask(getActivity(), true) {
				
				UserDTO user;
				
				@Override
				public boolean onPostLoad() {
					// TODO Auto-generated method stub
					
					mListener.setUser(user);
	/*				FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.replace(R.id.layout,new UserDetailsFragment(),"user_detail");
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					ft.addToBackStack(null);
					ft.commit();*/
					mListener.changeFragment(new UserDetailsFragment(), "user_detail", true);
					return false;
				}
				
				@Override
				public boolean onLoad() {

					user = rc.getUserByEmail(email);
					
					if(user == null) {
//						Log.i(TAG, "User is: "+user);
						user = new UserDTO();
						user.setEmail(email);
						user = rc.saveUser(user);
					}
				
					return true;							
				}
			}.execute();
		} else {
			btnLogin.setEnabled(true);
			Toast.makeText(getActivity(), "Email is no valid", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
        try {
            mListener = (FragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentListener");
        }
	}
	
	private boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
}
