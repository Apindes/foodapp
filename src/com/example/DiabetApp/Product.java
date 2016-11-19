package com.example.DiabetApp;

import java.util.*;
import java.util.regex.*;

public class Product extends ArrayList<Product>{

    private String name;
    private int amount;
    private int callories;
    private String picURL;
    private String isGroup;
    private String auxName;


    private final int MILK_GROUP=1;
    private final int BAKERY_GROUP=2;
    private final int DRINKS_GROUP=3;
    private final int SWEETS_GROUP=4;

    private static ArrayList<Product> milkArrayList=new ArrayList<>();
    private static ArrayList<Product> bakeryArrayList=new ArrayList<>();
    private static ArrayList<Product> drinksArrayList=new ArrayList<>();
    private static ArrayList<Product> sweetsArrayList=new ArrayList<>();

    private static  ArrayList<Product> [] products = new ArrayList[4];
    private static HashMap<String,Integer> namesMap=new HashMap<>();

    public Product(){}

    public Product(String name,String auxName,int amount,int callories,String isGroup){
        this.name=name;
        this.amount=amount;
        this.callories=callories;
        this.isGroup=isGroup;
        this.auxName=auxName;

    }
    public Product(int group ,String name,String auxName,int amount,int callories){


        switch (group){
            case MILK_GROUP:Product firstProduct=new Product(name,auxName,amount,callories,"milk");
                milkArrayList.add(firstProduct);
                break;
            case BAKERY_GROUP:Product secProduct=new Product(name,auxName,amount,callories,"bakery");
                bakeryArrayList.add(secProduct);
                break;
            case DRINKS_GROUP:Product thirdProduct=new Product(name,auxName,amount,callories,"drinks");
                drinksArrayList.add(thirdProduct);
                break;
            case SWEETS_GROUP:Product fourthProduct=new Product(name,auxName,amount,callories,"sweets");
                sweetsArrayList.add(fourthProduct);
                break;
        }

    }


    public String getName() {
        return name;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCalloriess() {
        return callories;
    }



    public static ArrayList<Product> [] getProducts() {
        Collections.sort(milkArrayList,new myListComparator());
        Collections.sort(drinksArrayList,new myListComparator());
        Collections.sort(bakeryArrayList,new myListComparator());
        Collections.sort(sweetsArrayList,new myListComparator());
        products[0]=milkArrayList;
        products[1]=drinksArrayList;
        products[2]=bakeryArrayList;
        products[3]=sweetsArrayList;
        return products;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public String getAuxName() {
        return auxName;
    }

    public static HashMap<String,Integer> getNames(){
        ArrayList<Product>[] groups = Product.getProducts();

        for(int i=0;i<groups.length;i++){
            ArrayList<Product> products = groups[i];
            for(Product product:products){
                String name1 =product.getName();
                String name2 =product.getAuxName();
                namesMap.put(name1,i);namesMap.put(name2,i);
            }

        }
        return namesMap;
    }

    public static ArrayList<Product> getMilkArrayList() {
        return milkArrayList;
    }

    public static ArrayList<Product> getBakeryArrayList() {
        return bakeryArrayList;
    }

    public static ArrayList<Product> getSweetsArrayList() {
        return sweetsArrayList;
    }

    public static ArrayList<Product> getDrinksArrayList() {
        return drinksArrayList;
    }
}


class myListComparator  implements Comparator<Product>{


    @Override
    public int compare(Product t1, Product t2) {
        String name1 = t1.getName();
        String name2 = t2.getName();

        String replace="";
        Pattern p= Pattern.compile("[\\s]");

        if((name1.split(" ")).length>1){

            Matcher m1=p.matcher(name1);
            name1=m1.replaceAll(replace);
        }

        if((name2.split(" ")).length>1){
            Matcher m2=p.matcher(name2);
            name2=m2.replaceAll(replace);
        }
       int k;
       for(int i=0;i<name1.length();i++){
           for(k=i;k<name2.length();k++){
                if(name1.charAt(i)>name2.charAt(k)){
                    return 1;
                }else if(name1.charAt(i)<name2.charAt(k)){
                    return -1;
                }
                else if(name1.charAt(i)==name2.charAt(k)){
                   i++;
                }
            }
        }
        return 0;
    }




}
