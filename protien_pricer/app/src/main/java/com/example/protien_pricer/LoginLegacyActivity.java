package com.example.protien_pricer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LoginLegacyActivity extends AppCompatActivity {
    Button sign_in_button;
    Button skip_button;
    //Button other;
    private GoogleSignInClient client;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_legacy);

        setTitle("Log Into Greatest App Ever");

        sign_in_button = findViewById(R.id.button_sign_in);
        skip_button = findViewById(R.id.skip_button);
        //other = findViewById(R.id.other_button);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://protien-pricer-default-rtdb.firebaseio.com/");
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this,options);
        client.revokeAccess();

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = client.getSignInIntent();
                startActivityForResult(i, 123);

            }
        });
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });
        /*
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent other = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(other);
            }
        });

         */
    }

    @Override
    public void onStart() {

        super.onStart();

        if(auth.getCurrentUser() != null){
            System.out.println("User On Start: " + auth.getCurrentUser().getUid());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            System.out.println("================== User ID: " + user.getUid());
                            goToMain();
                        } else {
                            Toast.makeText(LoginLegacyActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            } catch (ApiException e) {
                e.printStackTrace();
                System.out.println("task get result not working");
            }
        }
    }

    public void goToMain(){
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
    }
}