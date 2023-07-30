package com.example.protien_pricer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import java.util.ArrayList;

import com.example.protien_pricer.SearchHandler.LocalBinder;

public class SearchResultsActivity extends AppCompatActivity {
    private ArrayList<FoodItem> items;
    private RecyclerView recyclerView;
    private RecyclerAdapter.RecyclerViewClickListener listener;

    // Service Binding
    SearchHandler searchHandler = new SearchHandler();
    boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Connect to Search Handler service
        Intent intentSH = new Intent(this, SearchHandler.class);
        bindService(intentSH, serviceConnection, Context.BIND_AUTO_CREATE);
        //System.out.println("2nd page connected: " + isConnected);


        recyclerView = findViewById(R.id.search_results);

        items = SearchReturns.getInstance().getItems();
        //System.out.println("2nd page items: " + items.toString());


        /*
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemViewAdapter(getApplicationContext(), items));

         */

        setAdapter();
        setTitle("Search Results");

        /*
        for(int i = 0; i < items.size(); i++){

            FoodItem item = items.get(i);

            // Then call the specific method to get specific field
            // For example:
            System.out.println("-------------------");
            System.out.println("index: " + item.getIndex());
            System.out.println("id: " + item.getId());
            System.out.println("desc: " + item.getDescription());
            System.out.println("brand: " + item.getBrand());
            System.out.println("serving: " + item.getServing());
            System.out.println("protein " + item.getProtein());
            System.out.println("carbs " + item.getCarbs());
            System.out.println("fat " + item.getFat());
            System.out.println("energy " + item.getCalories());
            System.out.println("===================");
        }

         */



    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalBinder binder = (LocalBinder) iBinder;
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnected = false;
        }
    };

    private void setAdapter(){
        setOnClickListener();
        RecyclerAdapter adapter = new RecyclerAdapter(items, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        /*
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerAdapter(items));

         */
    }

    private void setOnClickListener() {
        listener = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), Calculations.class);
                intent.putExtra("index", items.get(position).getIndex());
                startActivity(intent);
            }
        };
    }

}