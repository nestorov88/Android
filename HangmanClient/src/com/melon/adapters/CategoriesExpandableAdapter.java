package com.melon.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hangmanclient.R;
import com.melon.dto.CategoryDTO;
import com.melon.dto.WordDTO;
import com.melon.utils.Manager;
import com.melon.utils.NNAsyncTask;
import com.melon.views.ExpandableItemView;

public class CategoriesExpandableAdapter extends BaseExpandableListAdapter{
	
	private Activity context;
    private HashMap<CategoryDTO, List<WordDTO>> wordCollections;
    private ArrayList<CategoryDTO> categories;
    
    private static final String TAG = CategoriesExpandableAdapter.class.getSimpleName();
 
    public CategoriesExpandableAdapter(Activity context, ArrayList<CategoryDTO> categories,
    		HashMap<CategoryDTO, List<WordDTO>> wordCollection) {
        this.context = context;
       
        this.wordCollections = wordCollection;
        this.categories = categories;
    }
 
    public Object getChild(int groupPosition, int childPosition) {
        return wordCollections.get(categories.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	WordDTO word = (WordDTO) getChild(groupPosition, childPosition);
        final String wordString = word.getWord() ;
        
        LayoutInflater inflater = context.getLayoutInflater();
        Log.i(TAG, "convertView:" + convertView );
        if (convertView == null) {
/*        	ExpandableItemView vw = (ExpandableItemView) inflater.inflate(R.layout.view_word_layout, null);
        	vw.setContent(word, (CategoryDTO)getGroup(groupPosition));
            convertView = vw;*/
        	
            convertView = inflater.inflate(R.layout.view_word_layout, null);
            ((ExpandableItemView)convertView).setContent(word, childPosition, groupPosition);
        }
 
        TextView item = (TextView) convertView.findViewById(R.id.txtWord);
 
        ImageView delete = (ImageView) convertView.findViewById(R.id.imgDelete);
/*        delete.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
            	deleteItem(groupPosition, childPosition);
            }
        });*/
 
        item.setText(wordString);

        
//        context.registerForContextMenu(convertView);
        return convertView;
     
    }
 
    public int getChildrenCount(int groupPosition) {
        return wordCollections.get(categories.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }
 
    public int getGroupCount() {
        return categories.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	CategoryDTO category = (CategoryDTO) getGroup(groupPosition);
        String categoryName = category.getName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.view_category_layout,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.txtCategory);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(categoryName);
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    
    
    public void deleteItem(final int groupPosition, final int childPosition) {
        List<WordDTO> child = wordCollections.get(categories.get(groupPosition));
        final WordDTO word = child.remove(childPosition);
       	Log.i(TAG, "Deleting word:" + word.getWord());
        notifyDataSetChanged();

        
        new NNAsyncTask() {
			
			@Override
			public boolean onPostLoad() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onLoad() {
				// TODO Auto-generated method stub
				Manager.getServiceClient().deleteWordById(word.getId());
				return false;
			}
		}.execute();
    }
}
