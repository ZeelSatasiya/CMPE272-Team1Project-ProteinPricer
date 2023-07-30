package com.example.protien_pricer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SavedCalc extends AppCompatActivity {

    FoodItem specific_item;
    private double price;
    private double servings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_calc);

        setTitle("Greatest App Ever");

        // Set up fields
        TextView desc = findViewById(R.id.calc_desc);
        TextView brand = findViewById(R.id.calc_brand);
        TextView serv_size = findViewById(R.id.calc_serving);
        TextView protein = findViewById(R.id.protein);
        TextView fat = findViewById(R.id.fat);
        TextView carbs = findViewById(R.id.carbs);
        TextView kcal = findViewById(R.id.kcal);
        EditText price_input = findViewById(R.id.price);
        EditText servings_input = findViewById(R.id.servings);
        Button calculate = findViewById(R.id.calculate_button);
        Button home = findViewById(R.id.home_button_calc);
        Button delete = findViewById(R.id.delete_button);
        TextView ppd = findViewById(R.id.calc_result);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = "";
        if(user != null){
            userID = user.getUid();
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int index = extras.getInt("index");
            FoodItem item = FBDBProxy.getInstance().getItems().get(index);
            FBDBProxy.getInstance().setSpecific_item(item);
        }

        // Use this method anywhere in the code to access selected item
        specific_item = FBDBProxy.getInstance().getSpecific_item();

        // Populate fields
        desc.setText(specific_item.getDescription());
        brand.setText("Brand: " + specific_item.getBrand());
        serv_size.setText("Serving Size: " + specific_item.getServing());
        protein.setText(String.valueOf(specific_item.getProtein()) + "g Protein");
        fat.setText(String.valueOf(specific_item.getFat()) + "g fat");
        carbs.setText(String.valueOf(specific_item.getCarbs()) + "g carbs");
        kcal.setText(String.valueOf(specific_item.getCalories()) + "kcal");

        // Assign buttons
        calculate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Take user input
                price = Double.parseDouble(price_input.getText().toString());
                servings = Double.parseDouble(servings_input.getText().toString());

                ppd(); // Calculate

                // Display result
                ppd.setText(String.valueOf((int)(ppd())) + "g Protein per Dollar Spent");
            }
        });

        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent new_search = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(new_search);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodID = specific_item.getId();
                FBDBProxy.getInstance().delete(foodID);

                Intent sl = new Intent(getApplicationContext(), SavedListActivity.class);
                startActivity(sl);
            }
        });
    }

    /*
    public void addDataToFirebase(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child(specific_item.getId()).setValue(specific_item);
                Toast.makeText(getApplicationContext(), "added to list", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "failed to add", Toast.LENGTH_SHORT).show();
            }
        });
    }

     */

    private double ppd(){
        double protein = specific_item.getProtein();
        double quantity = this.servings;
        double price = this.price;

        if(specific_item.getBrand().equals("N/A")){
            quantity = 16;
        }

        double result = (protein * quantity) / price;
        return Math.round(result);
    }

}