package com.devash.venturenotify;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
public class Viewscheme extends AppCompatActivity {
    TextView title, content;
    String schemeId, Title, Content;
    FloatingActionButton delete, edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewscheme);
        delete=findViewById(R.id.delete);
        edit=findViewById(R.id.editScheme);
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("asharma89505@gmail.com")){
            delete.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
        }
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        schemeId=intent.getStringExtra("schemeid");
        Title=intent.getStringExtra("title");
        Content = intent.getStringExtra("content");
        title.setText(Title);
        content.setText(Content);
        content.setMovementMethod(new ScrollingMovementMethod());
        delete.setOnClickListener(v -> {
            DocumentReference dr = firestore.collection("SchemesDB").document("schemesDB").collection("schemes").document(schemeId);
            AlertDialog.Builder builder = new AlertDialog.Builder(Viewscheme.this);
            builder.setMessage("Do uhh wanna Delete?");
            builder.setTitle("Alert");
            builder.setCancelable(false);
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dr.delete().addOnSuccessListener(unused -> Toast.makeText(Viewscheme.this, "Deleted Successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(Viewscheme.this, "Failed to Delete "+e, Toast.LENGTH_SHORT).show());
                dialog.dismiss();
                finish();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Viewscheme.this, Editscheme.class);
                intent.putExtra("title",Title);
                intent.putExtra("content",Content);
                intent.putExtra("schemeid",schemeId);
                startActivity(intent);
            }
        });
    }
}