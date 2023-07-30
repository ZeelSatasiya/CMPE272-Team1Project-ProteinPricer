package com.example.protien_pricer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FBDBProxy {

    FirebaseDatabase database;
    DatabaseReference ref;

    ArrayList<FoodItem> items = new ArrayList<FoodItem>();
    ArrayList<FoodItem> getItems(){return items;}

    private FoodItem specific_item;
    public void setSpecific_item(FoodItem item){
        this.specific_item = item;
    }
    public FoodItem getSpecific_item() {
        return specific_item;
    }

    // Singleton
    private static FBDBProxy instance;
    private FBDBProxy(){}
    public static synchronized FBDBProxy getInstance(){
        if(instance == null){
            instance = new FBDBProxy();
        }
        return instance;
    }

    public void setRef(String uid) {
        this.database = FirebaseDatabase.getInstance();
        this.ref = database.getReference("SavedList/" + uid);
    }

    public void buildItems(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //FoodItem item = snapshot.getValue(FoodItem.class);
                //System.out.println("item is: " + item.getDescription());

                resetItems();
                int i = 0;
                for (DataSnapshot s : snapshot.getChildren()){
                    String brand = s.child("brand").getValue(String.class);
                    double cal = s.child("calories").getValue(double.class);
                    double carbs = s.child("carbs").getValue(double.class);
                    String desc = s.child("description").getValue(String.class);
                    double fat = s.child("fat").getValue(double.class);
                    String id = s.child("id").getValue(String.class);
                    // int index = s.child("index").getValue(int.class);
                    double protein = s.child("protein").getValue(double.class);
                    //String serving = s.child("serving").getValue(String.class);
                    double size = s.child("size").getValue(double.class);
                    String unit = s.child("unit").getValue(String.class);

                    System.out.println(desc);
                    FoodItem item = new FoodItem();
                    item.msetBrand(brand);
                    item.msetCalories(cal);
                    item.msetCarbs(carbs);
                    item.msetDescription(desc);
                    item.msetFat(fat);
                    item.msetId(id);
                    item.setIndex(i);
                    item.msetProtein(protein);
                    item.msetSize(size);
                    item.msetUnit(unit);
                    item.msetServing();

                    // item.printAll();
                    items.add(item);

                    i++;
                }
                System.out.println("build complete");

                for(FoodItem it : getItems()){
                    //it.printAll();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public void delete(String foodID){
        ref.child(foodID).removeValue();
    }

    public void resetItems(){
        items.clear();
    }

}





