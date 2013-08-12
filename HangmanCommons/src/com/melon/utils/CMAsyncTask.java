package com.melon.utils;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;



/**
 * Async task, that should be used in every place with "heavy" 
 * background loading of resources - network calls, file downloads etc.
 * It provide integrated capabilities for showing dialog during loading with specific content.
 *
 */
public abstract class CMAsyncTask extends AsyncTask<String, Void, Boolean> {
    
	private static final String TAG = "SmAsyncTask";
    private int retry = 0;
    private boolean showLoadingDialog = false; 
    //private Handler mHandler;
    private ProgressDialog dialog;
    private Context context;
    private long delayExecutionMillis = 0;
    private String dialogMessage = "";

    private Exception ex = null;
    
	/**
     * Default constructor creates load task with endless retry cycle.
     */
    public CMAsyncTask() {
    	initState(null, Integer.MAX_VALUE, false, 0);
    }


  /*  public SmAsyncTask(int retry) {
    	initState(null, retry, false, 0);
    }

    public SmAsyncTask(int retry, long delayExecutionMillis) {
    	initState(null, retry, false, delayExecutionMillis);
    }*/
    
    public CMAsyncTask(Context context, int retry, boolean showLoadingDialog) {
    	initState(context, retry, showLoadingDialog, 0);
    }

    public CMAsyncTask(Context context, int retry, boolean showLoadingDialog, String dialogMessage) {
    	this.dialogMessage = dialogMessage;
    	initState(context, retry, showLoadingDialog, 0);
    }

    public CMAsyncTask(Context context, int retry, boolean showLoadingDialog, long delayExecutionMillis) {
    	initState(context, retry, showLoadingDialog, delayExecutionMillis);
    }

    private void initState(Context context, int retry, boolean showLoadingDialog, long delayExecutionMillis) {
        this.context = context;
    	this.retry = retry;
        this.showLoadingDialog = false;
        this.delayExecutionMillis = delayExecutionMillis;
        //mHandler = new Handler(Looper.getMainLooper());
    }
    
    /**
     * Execute implemented functionality before load() and after
     * progress bar became visible. 
     * The method should be override only if there is specific needs.
     * 
     */
    public void onPreload() { }
    
    /**
     * Execute implemented functionality in background
     * @return boolean - true if the method loads with success
     */
    public abstract boolean load() throws Exception;
    
    /**
     * Execute implemented functionality after load() finished with 
     * success and progress bar is already removed. 
     * 
     * @param result - boolean returned by load()
     */
    public abstract void onPostload(Boolean result);
    
      
	protected final void onPreExecute() {
		  if (dialog != null) {
			  dialog.dismiss();
			  dialog = null;
		  }
		 
		  if (showLoadingDialog && context != null) {
	    	 dialog = new ProgressDialog(context);
	    	 dialog.setCancelable(false);
	    	 dialog.setCanceledOnTouchOutside(false);
	    	 dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
				}
			});
	    	 
	         dialog.setMessage(dialogMessage);
	         dialog.show();
		  }
		  
		  onPreload();
	}

	protected final Boolean doInBackground(final String... args) {

    	  if (delayExecutionMillis > 0) {
    		  try {
  				Thread.sleep(delayExecutionMillis);
  			} catch (InterruptedException e) {
  				Log.e("AsyncTask", "InterruptedException - " + e.getMessage());
  			}
    	  }

		  boolean loaded = false;
		  
		  while(!loaded && retry-- >= 0) {
	
			try {
				loaded = load();
			} catch (Exception e1) {
				Log.e("AsyncTask", "ServiceException - " + e1.getMessage());
				ex = e1;
				Log.e("AsyncTask", "Exception during loading async task - " + e1.getMessage(), e1);
				ex = new Exception("Exception during loading async task", e1);
			} 
		  }
    	
    	  return loaded;
	}

	protected final void onPostExecute(final Boolean result) {
/*	  mHandler.post(new Runnable() {
		@Override
		public void run() {
			onPostload(result);
	        
			if (dialog != null && dialog.isShowing()) {
	             dialog.dismiss();
	             dialog = null;
	        }
		}
	  });
*/
		
		onPostload(result);
		
		if (dialog != null && dialog.isShowing()) {
		     dialog.dismiss();
		     dialog = null;
		}
	}
  
	public void progress(){
		publishProgress(null);
	}
  	
    @Override
	public void onProgressUpdate(Void... values) {
    	super.onProgressUpdate(values);
    	if (dialog != null && showLoadingDialog) {
    		dialog.setMessage(dialogMessage);
            dialog.show();
    	}
	}


	public String getDialogMessage() {
		return dialogMessage;
	}


	public void setDialogMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
		progress();
	}
	
    public Exception getException() {
		return ex;
	}


	public ProgressDialog getDialog() {
		return dialog;
	}


	public void setDialog(ProgressDialog dialog) {
		this.dialog = dialog;
	}

	
}