package com.devash.venturenotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class    CreateScheme extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase db;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    EditText title, content;
    String Title, Content;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scheme);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        submit=findViewById(R.id.submit);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Title = title.getText().toString().trim();
                Content = content.getText().toString().trim();
                if(Title.isEmpty()||Content.isEmpty()){
                    Toast.makeText(CreateScheme.this, "Enter Both Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    DocumentReference dr = firestore.collection("SchemesDB").document("schemesDB").collection("schemes").document();
                    Map<String, Object> scheme = new HashMap<>();
                    scheme.put("title",Title);
                    scheme.put("content",Content);

                    dr.set(scheme).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreateScheme.this, "Created Successfully", Toast.LENGTH_SHORT).show();
                            FCMNotificationSender sender = new FCMNotificationSender("/topics/all",Title,Content,getApplicationContext(), CreateScheme.this);
                            sender.SendNotification();
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateScheme.this, "Failed! "+e , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        menu.add("Logout");
        menu.add("Change Password");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Logout")){
            firebaseAuth.signOut();
            finish();
            Toast.makeText(this, "Signout Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreateScheme.this,Login.class));
        }
        else if(item.getTitle().equals("Change Password")){
            startActivity(new Intent(CreateScheme.this,Forgotpswd.class));
        }
        return super.onOptionsItemSelected(item);
    }
}