package com.example.DiabetApp.DB;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.*;
import com.example.DiabetApp.MainTwo;
import com.example.DiabetApp.Product;
import com.example.DiabetApp.R;

import java.util.ArrayList;


public class FoodAdapter3 extends BaseExpandableListAdapter {


    private ArrayList<Product>[]  foodList;
    private LayoutInflater inflater;
    private SparseArray<SparseBooleanArray> checkedPositions;
    private SparseArray<SparseBooleanArray> foundedPositions;
    ExpandableListView listView;
    static public volatile int groupInd;
    private SparseBooleanArray checkedChildPositionsArray;
    private SparseBooleanArray foundedChildPositionsArray;
    public  boolean isFounded=false;
    Animation fadeIn = new AlphaAnimation(0, 1);


    public FoodAdapter3(Context context, ArrayList<Product> [] foodList, ExpandableListView listView){
        super();
        this.foodList=foodList;
        inflater =LayoutInflater.from(context);
        checkedPositions = new SparseArray<SparseBooleanArray>();
        foundedPositions = new SparseArray<>();
        this.listView=listView;
    }


    public boolean isFounded() {
        return isFounded;
    }

    @Override
    public int getGroupCount() {
        return foodList.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return foodList[groupPosition].size();
    }

    @Override
    public Object getGroup(int i) {

        return foodList[i];
    }

    @Override
    public Object getChild(int groupIndex, int childIndex) {

        return foodList[groupIndex].get(childIndex).getName();

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

        String groupName=foodList[i].get(0).getIsGroup();
        if(MainTwo.languageIsEng) {
            groupName =foodList[i].get(0).getIsGroup();

        }
        if(!MainTwo.languageIsEng){

            switch (groupName){
                case "milk": groupName="молоко";
                    break;
                case "bakery": groupName="выпечка";
                    break;
                case "drinks": groupName="напитки";
                    break;
                case "sweets":groupName="сладости";
                    break;

            }
        }
        tv.setText(groupName);

        AbsListView.LayoutParams p1 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,90);
        AbsListView.LayoutParams p2 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,70);
        if(groupName.length()>11){

            view.setLayoutParams(p1);
        }
        else{
            view.setLayoutParams(p2);
        }
        notifyDataSetChanged();
        return view;
    }

    @Override
    public View getChildView(int groupIndex, int childIndex, boolean isLastChild, View view, ViewGroup parent) {
        if(view==null) {
            view = inflater.inflate(R.layout.simple_list_item_multiple_choice, parent, false);
        }
        Product product = foodList[groupIndex].get(childIndex);
        Integer calories = product.getCalloriess();

        CheckedTextView tv = (CheckedTextView) view.findViewById(R.id.textItem);
        if(MainTwo.languageIsEng) {
            tv.setText(product.getName());

        }
        if(!MainTwo.languageIsEng){
            tv.setText(product.getAuxName());

        }
        tv.setTag(product);

        TextView calloriesTV = (TextView)view.findViewById(R.id.hiddenCalloriesTV);
        calloriesTV.setText(calories.toString());
        calloriesTV.setTag(calories);


        int color_default  = Color.parseColor("#72000000");
        int color_selected  = Color.parseColor("#72b3e473");

        if(foundedPositions.get(groupIndex)!=null){

            isFounded  = foundedPositions.get(groupIndex).get(childIndex);
            if(isFounded) {
                view.setBackgroundColor(color_selected);

            }else{
                isFounded=false;
            }

        }



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
            view.setBackgroundColor(color_default);
            fadeIn=null;
            isFounded=false;
            tv.setChecked(false);
            tv.setEnabled(true);
            checkedPositions.clear();
            foundedPositions.clear();
            notifyDataSetChanged();
            return view;
        }


        tv.setEnabled(!isChecked);

        if(!isFounded){
            view.setBackgroundColor(color_default);
        }

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
        foundedChildPositionsArray=null;
        checkedChildPositionsArray = checkedPositions.get(groupPosition);
        if(checkedChildPositionsArray==null) {
            checkedChildPositionsArray = new SparseBooleanArray();
            checkedChildPositionsArray.put(childPosition, true);
            checkedPositions.put(groupPosition, checkedChildPositionsArray);
        }else{
            boolean oldState  = checkedChildPositionsArray.get(childPosition);
            checkedChildPositionsArray.put(childPosition,!oldState);
        }

        notifyDataSetChanged(); notifyDataSetInvalidated();
    }

    public SparseArray<SparseBooleanArray> getFoundedPositions() {
        return foundedPositions;
    }

    public void setChildFounded(int groupPosition,int childPosition){
         foundedChildPositionsArray = foundedPositions.get(groupPosition);
        if(foundedChildPositionsArray==null){
            foundedChildPositionsArray = new SparseBooleanArray();
            foundedChildPositionsArray.put(childPosition,true);
            foundedPositions.put(groupPosition,foundedChildPositionsArray);


        }else{

            boolean oldState = foundedChildPositionsArray.get(childPosition);
            foundedChildPositionsArray.put(childPosition,!oldState);
//            foundedPositions.put(groupPosition,foundedChildPositionsArray);
        }
        notifyDataSetChanged();
//        else {
//
//            foundedChildPositionsArray=null;
//            notifyDataSetChanged();
//        }

    }

    public SparseBooleanArray getCheckedChildPositionsArray() {
        return checkedChildPositionsArray;
    }

    public void setFoundedChildPositionsArray(SparseBooleanArray foundedChildPositionsArray) {
        this.foundedChildPositionsArray = foundedChildPositionsArray;
    }
}
