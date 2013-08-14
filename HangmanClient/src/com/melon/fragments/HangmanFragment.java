package com.melon.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.hangmanclient.R;
import com.melon.dto.CategoryDTO;
import com.melon.dto.UserDTO;
import com.melon.dto.WordDTO;
import com.melon.interfaces.FragmentListener;
import com.melon.utils.Manager;
import com.melon.utils.NNAsyncTask;

public class HangmanFragment extends Fragment{

	private FragmentListener mListener;
	private ArrayList<WordDTO> wordsList;
	private ArrayList<CategoryDTO> categoryList;
	private UserDTO user;
	private WordDTO wordToGuess;
	
	private static final String TAG = HangmanFragment.class.getSimpleName();
	
	private TextView txtWordToGuess;
	private TextView txtTriesLeft;
	private TextView txtLettersYouTried;
	private TextView txtLettersToGuess;
	
	private ImageView imgHangmanPicture;
	
	private ArrayList<Character> triedLetters;
	
	private char[] word;
	private char[] wordWorkCopy;
	
	private int[] hangmanErrorsPictures;
	
	private int triesLeft = 5;
	private int lettersToGuess;
	
	private EditText etLetterToGuess;
	private Button btnGuessLetter;
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		hangmanErrorsPictures = new int[]{R.drawable.error5, R.drawable.error4, R.drawable.error3, R.drawable.error2, R.drawable.error1, R.drawable.error0};
		
		View view = inflater.inflate(R.layout.fragment_hangman, container, false);
		
		triedLetters = new ArrayList<Character>();

		imgHangmanPicture = (ImageView) view.findViewById(R.id.imgHangmanImage);
		
		
		txtTriesLeft = (TextView) view.findViewById(R.id.txtTriesLeft);
		
		
		txtLettersToGuess = (TextView) view.findViewById(R.id.txtLettersToGuess);
		
		txtLettersYouTried = (TextView) view.findViewById(R.id.txtLettersYouTried);
		
		etLetterToGuess = (EditText) view.findViewById(R.id.etLetterForGuess);
		etLetterToGuess.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE) {
					String letter = etLetterToGuess.getText().toString();
					if(letter != null && !letter.equals("")) {
						
		                InputMethodManager in = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);


		                in.hideSoftInputFromWindow(etLetterToGuess.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
						guessLetter(letter.toCharArray()[0]);
					}
				}
				return true;
			}
		});
		btnGuessLetter = (Button) view.findViewById(R.id.btnGuessLetter);
		
		btnGuessLetter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String letter = etLetterToGuess.getText().toString();
				if(letter != null && !letter.equals("")) {
					guessLetter(letter.toCharArray()[0]);
				}
			}
		});
		txtWordToGuess = (TextView) view.findViewById(R.id.txtWordToGuess);
		
		getAllWords();
				
		return view;
	}
	
	public void getAllWords() {
		wordsList = new ArrayList<WordDTO>();
		
		if(categoryList == null || (categoryList.size() == 0)) {
			new NNAsyncTask() {
				
				@Override
				public boolean onPostLoad() {
					// TODO Auto-generated method stub
					
					for(CategoryDTO category : categoryList) {
						wordsList.addAll(category.getWordList());
					}
					if(wordToGuess == null) {
						wordToGuess = getRandomWord();
					}
					
					setWordToGuess();
					updateScreenData();
					return false;
				}
				
				@Override
				public boolean onLoad() {
					categoryList = Manager.getServiceClient().getAllCetogiries();
					return false;
				}
			}.execute();
		} else {
			for(CategoryDTO category : categoryList) {
				wordsList.addAll(category.getWordList());
			}
			if(wordToGuess == null) {
				wordToGuess = getRandomWord();
			}
			setWordToGuess();
			updateScreenData();
		}
		
		

	}
	
	public WordDTO getRandomWord() {
		int rd = (int)(Math.random() * wordsList.size());
		return wordsList.get(rd);
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
	
	public char[] maskWord(char[] word) {
		char[] maskedWord = word;
		for(int i = 1; i < word.length-1; i++) {
			maskedWord[i] = '_';
		}
		return maskedWord;
	}
	
	public void guessLetter(char letter) {
		Log.i(TAG, "guessLetter");
		boolean correctGuess = false;
		if(!triedLetters.contains(letter)) {
			for(int i = 0; i < wordWorkCopy.length; i++) {
				
				if(word[i] == letter) {			
					wordWorkCopy[i] = letter;
					correctGuess = true;
					
					lettersToGuess--;
					Log.i(TAG, "Word letter: " + word[i] + "Letter: " + letter + "count: " + lettersToGuess);
				}
			}
			if(correctGuess) {
				txtWordToGuess.setText(String.valueOf(wordWorkCopy));
				triedLetters.add(letter);
			} else {
				triedLetters.add(letter);
				triesLeft--;
			}
			checkWirOrLose();
		} else {
			Toast.makeText(getActivity(), "You already tried this letter",  Toast.LENGTH_LONG).show();
		}
	}
	
	public void checkWirOrLose() {
		Log.i(TAG, "Tries left:" + triesLeft + " Letters to guess: " + lettersToGuess);
		if(triesLeft == 0) {
			Toast.makeText(getActivity(), "You lose",  Toast.LENGTH_LONG).show();
		} else if( lettersToGuess == 0) {
			Toast.makeText(getActivity(), "You won",  Toast.LENGTH_LONG).show();
		}
		updateScreenData();
	}
	
	public void setWordToGuess() {
		
		if(word == null) {
			word = wordToGuess.getWord().toCharArray();
			wordWorkCopy = maskWord(wordToGuess.getWord().toCharArray());
			lettersToGuess = word.length - 2;
		} 
		
		txtWordToGuess.setText(String.valueOf(wordWorkCopy));

	}
	
	public void updateScreenData() {
		imgHangmanPicture.setImageResource(hangmanErrorsPictures[triesLeft]);
		String trl = getResources().getString(R.string.tries_left) + String.valueOf(triesLeft);
		txtTriesLeft.setText(trl);
		String ltg = getResources().getString(R.string.letters_to_guess) + String.valueOf(lettersToGuess);
		txtLettersToGuess.setText(ltg);
		StringBuilder sb = new StringBuilder();
		for(char letter : triedLetters ) {
			sb.append(letter + ", ");
		}
		String lyt = getResources().getString(R.string.letters_you_have_tried) + sb.toString();
		txtLettersYouTried.setText(lyt);
		Log.i(TAG, "TRL: " + trl + " LTG: " + ltg + " LYT: " + lyt);
	}
}
