package com.example.DiabetApp;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

public class FoodAdapter extends BaseExpandableListAdapter {

    public static final int CHOICE_MODE_MULTIPLE = AbsListView.CHOICE_MODE_MULTIPLE;
    private List <Food> foodList;
    private LayoutInflater inflater;
    private SparseArray<SparseBooleanArray> checkedPositions;
    private int choiceMode = CHOICE_MODE_MULTIPLE;

    public FoodAdapter(Context context,List<Food> foodList){
        this.foodList=foodList;
        inflater =LayoutInflater.from(context);
        checkedPositions = new SparseArray<>();
    }
    public FoodAdapter(Context context,List<Food>foodList,int choiceMode){
        this(context,foodList);
        this.choiceMode=choiceMode;
    }

    @Override
    public int getGroupCount() {
        return foodList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return foodList.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int i) {
        return foodList.get(i);
    }

    @Override
    public Object getChild(int groupIndex, int childIndex) {
        return foodList.get(groupIndex).getList().get(childIndex);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int groupIndex, int childIndex) {
        return childIndex;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if(view==null) {
            view = inflater.inflate(R.layout.simple_expandable_list_item_1, viewGroup, false);
        }
        ((TextView)view.findViewById(R.id.it))
        .setText(foodList.get(i).getName());
        return view;
    }

    @Override
    public View getChildView(int groupIndex, int childIndex, boolean isLastChild, View view, ViewGroup parent) {

        if(view==null) {
            view = inflater.inflate(R.layout.simple_list_item_multiple_choice, parent, false);
        }
        ((CheckedTextView)view).setText(foodList.get(groupIndex).getList().get(childIndex));

        boolean isChecked=false;
        if(checkedPositions.get(groupIndex)!=null){
             isChecked = checkedPositions.get(groupIndex).get(childIndex);
            ((CheckedTextView)view).setChecked(isChecked);
        }else{
            ((CheckedTextView)view).setChecked(false);
        }
        view.setEnabled(!isChecked);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public SparseArray<SparseBooleanArray> getCheckedPositions() {
        return checkedPositions;
    }

    public void setClicked(int groupPosition,int childPosition){
        SparseBooleanArray checkedChildPositionsArray = checkedPositions.get(groupPosition);
        if(checkedChildPositionsArray==null){
            checkedChildPositionsArray=new SparseBooleanArray();
            checkedChildPositionsArray.put(childPosition,true);
            checkedPositions.put(groupPosition,checkedChildPositionsArray);
        }else{
            boolean oldState  = checkedChildPositionsArray.get(childPosition);
            checkedChildPositionsArray.put(childPosition,!oldState);
        }
        notifyDataSetChanged();
    }


}
