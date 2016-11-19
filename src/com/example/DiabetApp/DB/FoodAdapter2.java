package com.example.DiabetApp.DB;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.DiabetApp.MainTwo;
import com.example.DiabetApp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by максим on 04.09.2016.
 */
public class FoodAdapter2 extends BaseExpandableListAdapter {


    private HashMap<String,TreeMap<String,ArrayList>>  foodList;
    private LayoutInflater inflater;
    private SparseArray<SparseBooleanArray> checkedPositions;
    ExpandableListView listView;
    static public volatile int groupInd;



    public FoodAdapter2(Context context, HashMap<String,TreeMap<String,ArrayList>>  foodList,ExpandableListView listView){
        this.foodList=foodList;
        inflater =LayoutInflater.from(context);
        checkedPositions = new SparseArray<SparseBooleanArray>();
        this.listView=listView;
    }


    @Override
    public int getGroupCount() {
        return foodList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        TreeMap []map2=new TreeMap [0];
        return (foodList.values().toArray(map2))[groupPosition].keySet().size();
    }

    @Override
    public Object getGroup(int i) {
        return foodList.keySet().toArray()[i];
    }

    @Override
    public Object getChild(int groupIndex, int childIndex) {
        TreeMap []map2=new TreeMap [0];


        return (foodList.values().toArray(map2))[groupIndex].keySet().toArray()[childIndex];

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
        TextView tv = (TextView) view.findViewById(R.id.it);
        String groupName =String.valueOf(foodList.keySet().toArray()[i]);
        tv.setText(groupName);


        AbsListView.LayoutParams p1 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,90);
        AbsListView.LayoutParams p2 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,70);
        if(groupName.length()>11){

            view.setLayoutParams(p1);
        }
        else{
            view.setLayoutParams(p2);
        }
        return view;
    }

    @Override
    public View getChildView(int groupIndex, int childIndex, boolean isLastChild, View view, ViewGroup parent) {
        if(view==null) {
            view = inflater.inflate(R.layout.simple_list_item_multiple_choice, parent, false);
        }
        TreeMap<String,ArrayList> tempMap= ((TreeMap<String,ArrayList>)foodList.values().toArray()[groupIndex]);
        String temp = (String) ((TreeMap<String,ArrayList>)foodList.values().toArray()[groupIndex]).keySet().toArray()[childIndex];
        Integer amount = (Integer) tempMap.get(temp).get(0);
        Integer calories = (Integer) tempMap.get(temp).get(1);

        CheckedTextView tv = (CheckedTextView) view.findViewById(R.id.textItem);
        tv.setText(String.valueOf(temp));
        tv.setTag(amount);

        TextView calloriesTV = (TextView)view.findViewById(R.id.hiddenCalloriesTV);
        calloriesTV.setText(calories.toString());
        calloriesTV.setTag(calories);

        tv.setChecked(false);

        boolean isChecked=false;
        if(checkedPositions.get(groupIndex)!=null){
             isChecked =  checkedPositions.get(groupIndex).get(childIndex);
            tv.setChecked(isChecked);
        }else{
              tv.setChecked(false);
        }
        /**here i should find the solution*/
        if( MainTwo.isCleared ){
            tv.setChecked(false);
            tv.setEnabled(true);
            checkedPositions.clear();
            notifyDataSetChanged();
            return view;
        }


        tv.setEnabled(!isChecked);

        notifyDataSetChanged();
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
        if(checkedChildPositionsArray==null) {
            checkedChildPositionsArray = new SparseBooleanArray();
            checkedChildPositionsArray.put(childPosition, true);
            checkedPositions.put(groupPosition, checkedChildPositionsArray);
        }else{
            boolean oldState  = checkedChildPositionsArray.get(childPosition);
            checkedChildPositionsArray.put(childPosition,!oldState);
        }

        notifyDataSetChanged();
    }

}
