package com.melon.utils;
import android.os.AsyncTask;


public abstract class NNAsyncTask extends AsyncTask<String, Integer, Boolean>{

public abstract boolean onLoad();
public abstract boolean onPostLoad();


@Override
protected Boolean doInBackground(String... params) {
	// TODO Auto-generated method stub
	return onLoad();
}
@Override
protected void onPostExecute(Boolean result) {
	// TODO Auto-generated method stub
	super.onPostExecute(onPostLoad());
}
	
}
