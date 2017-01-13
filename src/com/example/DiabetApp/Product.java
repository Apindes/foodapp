package com.example.DiabetApp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
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
    private int groupIndex;
    private static volatile ArrayList<ArrayList<Product>> total_list = new ArrayList<>();
    public static ArrayList<Product> list = new ArrayList<>();
    private static ArrayList<Integer> intlist = new ArrayList<>();
    public static Container container;


    private final int MILK_GROUP = 1;
    private final int BAKERY_GROUP = 2;
    private final int DRINKS_GROUP = 3;
    private final int SWEETS_GROUP = 4;
    private final int VEGGIES_GROUP = 5;
    private final int FRUITS_GROUP = 6;
    private final int MEAT_GROUP = 7;
    public  final static int BASE_GROUPS_SIZE=6;

    private static final int ADDITIONAL_GROUP = 8;

    private static HashMap<String, Integer> namesMap = new HashMap<>();
    private static ArrayList<String> groupsList = new ArrayList<>();
    private static HashMap<String, Integer> groupsMap = new HashMap<>();

    //This constructor add this product to the additional list and to the MYSQL database as well!
    public Product(int group, String name, String auxName, int amount, int callories, String url) {
        this(group, name, auxName, amount, callories);
        this.picURL = url;
        if (!intlist.contains(group)) {
            intlist.add(group);
            list = new ArrayList<>();
        }
        Product additionalProduct = new Product(name, auxName, amount, callories, "additional", group);
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

    public Product(String name, String auxName, int amount, int callories, String groupName, int groupInt) {
        this.name = name;
        this.amount = amount;
        this.callories = callories;
        this.groupName = groupName;
        this.auxName = auxName;
        this.groupIndex = groupInt;

        if (!groupsList.contains(groupName)) {
            groupsList.add(groupName);
        }
        if (!groupsMap.containsKey(groupName)) {
            groupsMap.put(groupName, groupIndex);
        }

    }

    public Product(int group, String name, String auxName, int amount, int callories) {


        if (!intlist.contains(group)) {
            intlist.add(group);
            list = new ArrayList<>();
        }



        switch (group) {
            case MILK_GROUP:
                Product firstProduct = new Product(name, auxName, amount, callories, "dairy products", group);
                list.add(firstProduct);
                break;
            case BAKERY_GROUP:
                Product secProduct = new Product(name, auxName, amount, callories, "bakery", group);
                list.add(secProduct);
                break;
            case DRINKS_GROUP:
                Product thirdProduct = new Product(name, auxName, amount, callories, "drinks", group);
                list.add(thirdProduct);
                break;
            case SWEETS_GROUP:
                Product fourthProduct = new Product(name, auxName, amount, callories, "sweets", group);
                list.add(fourthProduct);
                break;
            case VEGGIES_GROUP:
                Product fifthProduct = new Product(name, auxName, amount, callories, "vegetables", group);
                list.add(fifthProduct);
                break;
//            case ADDITIONAL_GROUP:
//                Product sixthProduct = new Product(name, auxName, amount, callories, "additional", group);
//                list.add(sixthProduct);
//                break;

            case FRUITS_GROUP:
                Product eightProduct = new Product(name, auxName, amount, callories, "fruits", group);
                list.add(eightProduct);
                break;
            case MEAT_GROUP:
                Product meatProduct = new Product(name, auxName, amount, callories, "meat", group);
                list.add(meatProduct);
                break;
        }

        if (!total_list.contains(list)) {
            total_list.add(list);
        }


    }

    public Product(Parcel parcel) {

        total_list = parcel.readArrayList(Product.class.getClassLoader());

    }


    private boolean groupsContains(int group) {
        if (groupsMap.values().contains(group))
            return true;

        return false;
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
        while (i < total_list.size() && i != 6) {

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

