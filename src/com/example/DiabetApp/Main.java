package com.example.DiabetApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.*;

public class Main extends Activity {

    GridLayout gridLayout;
    Button button;
    int buttonCount=0;
    ArrayList<Button> buttonArrayList = new ArrayList<Button>();
    String[] groups = new String[] {"Milk products","Bakery","Meat"};

    String[] milk_items = new String[] {"Cheese", "Butter", "Milk", "Yogurt"};
    String[] bakery = new String[] {"White bread", "Black Bread", "Muffin"};
    String[] meat = new String[] {"Beef", "Lamb", "Chicken", "Turkey","Rabbit","Pork"};

    ArrayList all = new ArrayList();

    // коллекция для групп
    ArrayList<Map<String, String>> groupData;

    // коллекция для элементов одной группы
    ArrayList<Map<String, String>> childDataItem;

    // общая коллекция для коллекций элементов
    ArrayList<ArrayList<Map<String, String>>> childData;
    // в итоге получится childData = ArrayList<childDataItem>
    ExpandableListView elvMain;

    Map<String,String>m;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        gridLayout= (GridLayout)findViewById(R.id.grid);


        groupData = new ArrayList<Map<String, String>>();

        for(String group:groups){
           m=new HashMap<String,String>();
            m.put("groupName",group);
            groupData.add(m);
        }

        all.add(milk_items);
        all.add(bakery);
        all.add(meat);


        String groupFrom[] = new String[] {"groupName"};
        int groupTo[] = new int[] {android.R.id.text1};

        childData=new ArrayList<ArrayList<Map<String, String>>>();

        //заполняем список "childData" елементами каждой группы !
        for(int k=0;k<groups.length;k++) {
            childDataItem = new ArrayList<Map<String, String>>();

            for (String item : (String[])all.get(k)) {
                m = new HashMap<String, String>();
                m.put("foodName", item);
                childDataItem.add(m);
            }

            childData.add(childDataItem);
        }

        String childFrom[] = new String[] {"foodName"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int childTo[] = new int[] {android.R.id.text1,android.R.id.text2};

        SimpleExpandableListAdapter listAdapter =  new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo
                );

        elvMain =(ExpandableListView)findViewById(R.id.expandableListView);
        elvMain.setAdapter(listAdapter);

        elvMain.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {

                if(view.isEnabled()){
                    String itemText = ((TextView)view).getText().toString();
                    button = (Button)getLayoutInflater().inflate(R.layout.item_button,null);
                    button.setText(itemText);
                    view.setEnabled(false);

                    gridLayout.addView(button);
                    return false;
                }
                else {
                    String itemText = ((TextView)view).getText().toString();
                    view.setEnabled(true);

                    gridLayout.removeView(findView(itemText));
                    return false;
                }

            }
        });
    }
    public View findView(String text){
        for(int i=0;i<gridLayout.getChildCount();i++){
            View v = gridLayout.getChildAt(i);
            if( ((Button)v).getText()==text){
                return v;
            }
        }
        return null;
    }


}
