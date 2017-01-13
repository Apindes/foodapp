package com.example.DiabetApp.DB;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.example.DiabetApp.MainTwo;
import com.example.DiabetApp.Product;
import com.example.DiabetApp.R;

import java.io.Serializable;
import java.util.ArrayList;


public class FoodAdapter4 extends BaseExpandableListAdapter implements Serializable{


    private  ArrayList<ArrayList<Product>>  foodList;
    private LayoutInflater inflater;
    private SparseArray<SparseBooleanArray> checkedPositions;
    private SparseArray<SparseBooleanArray> foundedPositions;
    ExpandableListView listView;
    static public volatile int groupInd;
    private SparseBooleanArray checkedChildPositionsArray;
    private SparseBooleanArray foundedChildPositionsArray;
    public  boolean isFounded=false;


    public FoodAdapter4(){}

    public FoodAdapter4(Context context, ArrayList<ArrayList<Product>> foodList, ExpandableListView listView){
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
        return foodList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return foodList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int i) {

        return foodList.get(i);
    }

    @Override
    public Object getChild(int groupIndex, int childIndex) {

        return foodList.get(groupIndex).get(childIndex).getName();

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

        }else if(i==6){
            view = inflater.inflate(R.layout.additional_expandable_list_item,viewGroup,false);
        }else {
            view = inflater.inflate(R.layout.simple_expandable_list_item_1, viewGroup, false);
        }

        TextView tv = (TextView) view.findViewById(R.id.it);
        tv.setTypeface(MainTwo.font);

        if(foodList.size()>0 && !foodList.get(i).isEmpty()){
            Product product = foodList.get(i).get(0);
            if (product != null) {
                String groupName = product.getGroupName();
                if (MainTwo.languageIsEng) {
                    groupName = foodList.get(i).get(0).getGroupName();

                }
                if (!MainTwo.languageIsEng) {

                    switch (groupName) {
                        case "dairy products":
                            groupName = "молочные продукты";
                            break;
                        case "bakery":
                            groupName = "выпечка";
                            break;
                        case "drinks":
                            groupName = "напитки";
                            break;
                        case "sweets":
                            groupName = "сладости";
                            break;
                        case "vegetables":
                            groupName = "овощи";
                            break;
                        case "fruits":
                            groupName = "фрукты";
                            break;
                        case "additional": {
                            groupName = "моя группа";
                            break;
                        }
                    }
                }
                tv.setText(groupName);


//                AbsListView.LayoutParams p1 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 90);
//                AbsListView.LayoutParams p2 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 70);
//                if (groupName.length() > 11) {
//
//                    view.setLayoutParams(p1);
//                } else {
//                    view.setLayoutParams(p2);
//                }
                notifyDataSetChanged();
            }
        }
        return view;
    }

    @Override
    public View getChildView(final int groupIndex, final int childIndex, boolean isLastChild, View view, ViewGroup parent) {
        if(view==null) {
            view = inflater.inflate(R.layout.simple_list_item_multiple_choice, parent, false);
        }
        Product product = foodList.get(groupIndex).get(childIndex);
        Integer calories = product.getCalloriess();

        CheckedTextView tv = (CheckedTextView) view.findViewById(R.id.textItem);
        tv.setTypeface(MainTwo.font);
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


        final int color_default  = Color.parseColor("#72000000");
        final int color_selected  = Color.parseColor("#72b3e473");
        if(!isFounded){
            view.setBackgroundColor(color_default);
        }
        if(foundedPositions.get(groupIndex)!=null){

            isFounded  = foundedPositions.get(groupIndex).get(childIndex);
            if(isFounded) {
                final View finalView = view;
                Thread t1 = new Thread(){
                    @Override
                    public void run() {
                       finalView.post(new Runnable() {
                           @Override
                           public void run() {
                               synchronized (MainTwo.class) {
                                   //hiding the keyboard of autoTV
                                   MainTwo.hideKeyboardOf(null);
                               }
                               synchronized (finalView) {
                                   try {
                                       finalView.setBackgroundColor(color_selected);
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }

                               }
                           }
                       });
                    }
                };
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        foundedPositions.get(groupIndex).put(childIndex, false);
                    }
                }.start();

//                view.setBackgroundColor(color_selected);
                isFounded=false;
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
            isFounded=false;
            tv.setChecked(false);
            tv.setEnabled(true);
            checkedPositions.clear();
            foundedPositions.clear();
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
        }
        notifyDataSetChanged(); notifyDataSetInvalidated();

    }

    public synchronized void setFoodList(ArrayList<ArrayList<Product>> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged(); notifyDataSetInvalidated();

    }

    public synchronized ArrayList<ArrayList<Product>> getFoodList() {
        return foodList;
    }

    public synchronized FoodAdapter4 getAdapter(){
        return this;
    }


}
