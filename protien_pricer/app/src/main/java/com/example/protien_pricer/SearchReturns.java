package com.example.protien_pricer;

import java.util.ArrayList;

public class SearchReturns {

    // Search results list
    private ArrayList<FoodItem> items;
    public void setItems(ArrayList<FoodItem> i){
        this.items = i;
    }
    public ArrayList<FoodItem> getItems(){ return items; }

    // Specific item for calculation
    private FoodItem specific_item;
    public void setSpecific_item(FoodItem item){
        this.specific_item = item;
    }
    public FoodItem getSpecific_item() {
        return specific_item;
    }

    // Singleton
    private static SearchReturns instance;
    private SearchReturns(){}

    public static synchronized SearchReturns getInstance(){
        if(instance == null){
            instance = new SearchReturns();
        }
        return instance;
    }

    public void printItems(){
        for(int i = 0; i < items.size(); i++){
            FoodItem item = items.get(i);
            System.out.println("item - " + i + ": " + item.getServing());
        }
    }
}
