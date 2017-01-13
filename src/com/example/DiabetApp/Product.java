package com.example.DiabetApp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.DiabetApp.DB.FoodAdapter4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product {



    private String name;
    private int amount;
    private int callories;
    private String picURL;
    private String groupName;
    private String auxName;
    private String groupIndex;
    private static volatile ArrayList<ArrayList<Product>> total_list = new ArrayList<>();
    public static ArrayList<Product> list = new ArrayList<>();
    private static ArrayList<String> Stringlist = new ArrayList<>();
    public static Container container;
    private static final String ADDITIONAL_GROUP = "additional";

    private static HashMap<String, Integer> namesMap = new HashMap<>();
    private static ArrayList<String> groupsList = new ArrayList<>();
    private static HashMap<String, String> groupsMap = new HashMap<>();

    //This constructor add  product ONLY to the additional list and to the MYSQL database as well!
    public Product(String group, String name, String auxName, int amount, int callories, String url) {
        this.picURL = url;
        if (!Stringlist.contains(group)) {
            Stringlist.add(group);
            list = new ArrayList<>();
        }
        Product additionalProduct = new Product(name, auxName, amount, callories, group);
        list.add(additionalProduct);
        if(MainTwo.databaseDoesntContain(name)) {

            SQLiteDatabase db = MainTwo.dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("name", name);
            cv.put("aux_name", auxName);
            cv.put("amount", amount);
            cv.put("callories", callories);
//            cv.put("url",url);
            db.insert("products", null, cv);

        }
        MainTwo.adapter4.setFoodList(getProducts());
        if (!total_list.contains(list)) {
            total_list.add(list);
        }
    }

    public Product() {
    }

    //this constructor helps to initialize variables!
    public Product(String name, String auxName, int amount, int callories, String groupName) {
        this.name = name;
        this.amount = amount;
        this.callories = callories;
        this.groupName = groupName;
        this.auxName = auxName;

        if (!groupsList.contains(groupName)) {
            groupsList.add(groupName);
        }
        if (!groupsMap.containsKey(groupName)) {
            groupsMap.put(groupName, groupName);
        }

    }
    //This constructor add new products in  a static way.
    public Product(String group, String name, String auxName, int amount, int callories) {


        if (!Stringlist.contains(group)) {
            Stringlist.add(group);
            list = new ArrayList<>();
        }

        Product firstProduct = new Product(name, auxName, amount, callories, group);
        if(!list.contains(firstProduct))
           list.add(firstProduct);
        Log.d(MainTwo.LOG_TAG,"added product!");


        if (!total_list.contains(list)) {
            total_list.add(list);
        }


    }


    public String getPicURL() {
        return picURL;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getCalloriess() {
        return callories;
    }

    public static ArrayList<String> getGroupsList() {
        return groupsList;
    }


    public static synchronized ArrayList<ArrayList<Product>> getProducts() {

        myListComparator comparator = new myListComparator();
        int i = 0;
        while (i < total_list.size() && i != 7) {

            Collections.sort(total_list.get(i), comparator);
            i++;
        }
        return total_list;
    }


    public String getGroupName() {
        return groupName;
    }

    public String getAuxName() {
        return auxName;
    }

    public static  HashMap<String, Integer> getNames() {
        ArrayList<ArrayList<Product>> groups = getProducts();

        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Product> group = groups.get(i);
            if (group != null) {
                for (Product product : group) {
                    String name1 = product.getName();
                    String name2 = product.getAuxName();
                    namesMap.put(name1, i);
                    namesMap.put(name2, i);
                }
            }

        }
        return namesMap;
    }

    public static HashMap<String, Integer> setNamesMap(int operation, String nameProduct) {
        HashMap<String, Integer> map = getNames();
        int groupIndex = total_list.size();
        switch (operation) {
            case 1:
                map.put(nameProduct, groupIndex);
                break;
            case 0:
                map.remove(nameProduct);
                break;
        }
        return map;
    }


    public static void addProductInNewGroup(String name, String auxName, int amount, int callories,String url) {

        {
            new Product(ADDITIONAL_GROUP, name, auxName, amount,callories, url);
        }

    }


    public static Container getContainer() {
        if(container!=null )  Log.d("==DIABET_APPLICATION ==", "container is not empty");
        else Log.d("==DIABET_APPLICATION ==", "container is empty");
        return container;
    }

    static class Container implements Serializable {


        private FoodAdapter4 adapter4;
        public Container(FoodAdapter4 a ){
            adapter4=a;
        }

        public FoodAdapter4 getAdapter4() {
            return adapter4;
        }
    }

    static class myListComparator extends Product implements Comparator<Product> {


        @Override
        public int compare(Product t1, Product t2) {
            String name1 = t1.getName();
            String name2 = t2.getName();

            String replace = "";
            Pattern p = Pattern.compile("[\\s]");

            if ((name1.split(" ")).length > 1) {

                Matcher m1 = p.matcher(name1);
                name1 = m1.replaceAll(replace);
            }

            if ((name2.split(" ")).length > 1) {
                Matcher m2 = p.matcher(name2);
                name2 = m2.replaceAll(replace);
            }
            int k;

            for (int i = 0; i < name1.length(); i++) {
                for (k = i; k < name2.length(); k++) {
                    if (name1.charAt(i) >
                            name2.charAt(k)) {
                        return 1;
                    } else if (name1.charAt(i) < name2.charAt(k)) {
                        return -1;
                    } else if (name1.charAt(i) == name2.charAt(k)) {
                        if (name1.charAt(i) == name1.charAt(name1.length() - 1) && name1.length() < name2.length()) {
                            return 1;
                        } else if (name2.charAt(k) == name2.charAt(name2.length() - 1) && name2.length() < name1.length()) {
                            return -1;
                        }
                        i++;

                    }
                }
            }
            return 0;

        }


    }
}

