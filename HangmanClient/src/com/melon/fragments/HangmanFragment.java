package com.melon.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.melon.dto.GameDTO;
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
	private TextView txtWordDescription;
	
	private ImageView imgHangmanPicture;
	
	private ArrayList<Character> triedLetters;
	
	private char[] word;
	private char[] wordWorkCopy;
	
	private int[] hangmanErrorsPictures;
	
	private int triesLeft = 5;
	private int lettersToGuess = 0;
	
	private EditText etLetterToGuess;
	private Button btnGuessLetter;
	
	private GameDTO gameToPlay;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		hangmanErrorsPictures = new int[]{R.drawable.error5, R.drawable.error4, R.drawable.error3, R.drawable.error2, R.drawable.error1, R.drawable.error0};
		
		View view = inflater.inflate(R.layout.fragment_hangman, container, false);
		
		if(triedLetters == null) {
			triedLetters = new ArrayList<Character>();
		}
		
		imgHangmanPicture = (ImageView) view.findViewById(R.id.imgHangmanImage);
		
		
		txtTriesLeft = (TextView) view.findViewById(R.id.txtTriesLeft);
		
		txtWordDescription = (TextView) view.findViewById(R.id.txtWordDescription);
		
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
						etLetterToGuess.setText("");
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
					setUpGame();
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

			setUpGame();
			setWordToGuess();
			updateScreenData();
		}
		
		

	}
	
	public WordDTO getRandomWord() {
		int rd = (int)(Math.random() * wordsList.size());
		Log.i(TAG, "Word List size: " + wordsList.size() + " rd: " + rd);
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
        if(mListener.getGame() == null) {
        	mListener.setGame(gameToPlay);
        } else {
        	gameToPlay = mListener.getGame();
        }
		super.onAttach(activity);
	}
	
	public char[] maskWord(char[] word) {
		char[] maskedWord = word;
		for(int i = 1; i < word.length-1; i++) {
			if(maskedWord[0] != maskedWord[i] && maskedWord[maskedWord.length -1] != maskedWord[i]) {
				maskedWord[i] = '_';
				lettersToGuess++;
			}

		}
		triedLetters.add(maskedWord[0]);
		triedLetters.add(maskedWord[word.length-1]);
		maskedWord[0] = Character.toUpperCase(maskedWord[0]);
		maskedWord[word.length-1] = Character.toUpperCase(maskedWord[word.length-1]);
		
		return maskedWord;
	}
	
	public void guessLetter(char letter) {
		Log.i(TAG, "guessLetter");
		char workChar = Character.toLowerCase(letter);
		boolean correctGuess = false;
		if(!triedLetters.contains(workChar)) {
			for(int i = 0; i < wordWorkCopy.length; i++) {
				
				if(word[i] == workChar) {			
					wordWorkCopy[i] = workChar;
					correctGuess = true;
					
					lettersToGuess--;
					Log.i(TAG, "Word letter: " + word[i] + "Letter: " + workChar + "count: " + lettersToGuess);
				}
			}
			if(correctGuess) {
				txtWordToGuess.setText(String.valueOf(wordWorkCopy));
				triedLetters.add(workChar);
			} else {
				triedLetters.add(workChar);
				triesLeft--;
			}
			checkWirOrLose();
		} else {
			Toast.makeText(getActivity(), "You already tried this letter",  Toast.LENGTH_LONG).show();
		}
	}
	
	public void checkWirOrLose() {
		Log.i(TAG, "Tries left:" + triesLeft + " Letters to guess: " + lettersToGuess);
		if(gameToPlay.getWholeWordGuessed() == null) {
			gameToPlay.setWholeWordGuessed(false);
		}
		if(triesLeft == 0) {
			gameToPlay.setResult(false);
			
		} else if( lettersToGuess == 0) {
			gameToPlay.setResult(true);
			
		}
		if(gameToPlay.getResult() != null) {
			new NNAsyncTask() {
				
				@Override
				public boolean onPostLoad() {
					// TODO Auto-generated method stub
					if(gameToPlay.getResult()) {
						Toast.makeText(getActivity(), "You won",  Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getActivity(), "You lose",  Toast.LENGTH_LONG).show();
					}
					getFragmentManager().popBackStack();
					return false;
				}
				
				@Override
				public boolean onLoad() {
					// TODO Auto-generated method stub
					Manager.getServiceClient().saveGame(gameToPlay);
					UserDetailsFragment fg = (UserDetailsFragment) getFragmentManager().findFragmentByTag("user_detail");
					fg.refreshUser();
					return false;
				}
			}.execute();
		} else {
			updateScreenData();
		}
		
	}
	
	public void setWordToGuess() {
		
		if(word == null) {
			word = wordToGuess.getWord().toCharArray();
			wordWorkCopy = maskWord(wordToGuess.getWord().toCharArray());
		} 
		
		txtWordToGuess.setText(String.valueOf(wordWorkCopy));
		txtWordDescription.setText(getResources().getString(R.string.word_description) + wordToGuess.getDescription());

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
	
	private void setUpGame() {
		for(CategoryDTO category : categoryList) {
			wordsList.addAll(category.getWordList());
		}
		if(wordToGuess == null) {
			wordToGuess = getRandomWord();
		}
		gameToPlay = new GameDTO();
		gameToPlay.setUserId(user.getId());
		gameToPlay.setWordID(wordToGuess.getId());

	}
	
	private void tryToGuessWholeWord() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter your word:");
        final EditText et = new EditText(getActivity());
        builder.setView(et);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	String text = et.getText().toString().toLowerCase();
                    	String wordToGuessToLowerCase = wordToGuess.getWord().toLowerCase();
                    	Log.i(TAG, "Text: " + text + " wordToGuess:" + wordToGuessToLowerCase);
                    	if(wordToGuessToLowerCase.equals(text)) {
                    		lettersToGuess = 0;
                    		gameToPlay.setWholeWordGuessed(true);
                    	} else {
                    		triesLeft = 0;
                    	}
                    	checkWirOrLose();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		inflater.inflate(R.menu.hangman_menu, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.guessWholeWord:
			tryToGuessWholeWord();
		default:
			return false;
		}
	}
}
