package com.devash.venturenotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Editscheme extends AppCompatActivity {
    Intent editScheme;
    EditText title,content;
    FloatingActionButton save;
    String Title,Content, schemeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editscheme);
        editScheme = getIntent();
        title= findViewById(R.id.title);
        content = findViewById(R.id.content);
        save=findViewById(R.id.save);
        Title=(editScheme.getStringExtra("title"));
        Content=(editScheme.getStringExtra("content"));
        schemeId=(editScheme.getStringExtra("schemeid"));
        title.setText(Title);
        content.setText(Content);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Title.isEmpty() || !Content.isEmpty()){

                    DocumentReference dr = FirebaseFirestore.getInstance().collection("SchemesDB").document("schemesDB").collection("schemes").document(schemeId);
                    Map<String, Object> note = new HashMap<>();
                    note.put("title",title.getText().toString());
                    note.put("content",content.getText().toString());
                    dr.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Editscheme.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Editscheme.this, "Failed Exception"+e , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(Editscheme.this, "Enter Details!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}