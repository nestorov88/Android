package com.melon.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hangmanclient.R;
import com.melon.dto.CategoryDTO;
import com.melon.dto.WordDTO;

public class ExpandableItemView extends RelativeLayout{

	private WordDTO word;
	private TextView txtWord;
	private CategoryDTO category;
	private int childPosition;
	private int groupPosition;
	
	public ExpandableItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	
	}
	
	
	@Override
	protected ContextMenuInfo getContextMenuInfo() {
		// TODO Auto-generated method stub
		return new ExpandableViewContextMenuInfo(word, childPosition, groupPosition);
	}


	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		Log.i("", "INFLATING");
        txtWord = (TextView) this.findViewById(R.id.txtWord);
        
        ImageView delete = (ImageView) this.findViewById(R.id.imgDelete);
        
        
		super.onFinishInflate();
	}

	public void setContent(WordDTO word,int childPosition, int groupPosition) {
		this.word = word;
		this.childPosition = childPosition;
		this.groupPosition = groupPosition;
		txtWord.setText(word.getWord());
	}
	
	 public static class ExpandableViewContextMenuInfo implements ContextMenu.ContextMenuInfo {  
		  
	        public WordDTO word;
	        public int childPosition;
	        public int groupPosition;
	        
	        public ExpandableViewContextMenuInfo(WordDTO word, int childPosition, int groupPosition) {  

	    		this.childPosition = childPosition;
	    		this.groupPosition = groupPosition;
	        	this.word = word;  
	        }  
 
	    }  
}
