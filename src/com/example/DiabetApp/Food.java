package com.example.DiabetApp;

import java.util.*;

public class Food {

    private String name;
    private List<String> list;

    public Food(String groupName,List<String> childList){
        this.name=groupName;
        list=childList;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
