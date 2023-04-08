package com.devash.venturenotify;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminBoard extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    FloatingActionButton newScheme;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirestoreRecyclerAdapter<ModelClass, SchemeiewHolder> schmeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_board);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Toast.makeText(this, "Welome to Admin page", Toast.LENGTH_SHORT).show();
        newScheme=findViewById(R.id.newScheme);
        newScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminBoard.this, CreateScheme.class));
            }
        });
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("SchemesDB").document("schemesDB").collection("schemes").orderBy("title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ModelClass> allschems =
                new FirestoreRecyclerOptions.Builder<ModelClass>()
                        .setQuery(query, ModelClass.class)
                        .build();
        schmeAdapter = new FirestoreRecyclerAdapter<ModelClass, SchemeiewHolder>(allschems) {
            @Override
            protected void onBindViewHolder(@NonNull SchemeiewHolder holder, int position, @NonNull ModelClass model) {
                holder.schemeTitle.setText(model.getTitle());
                holder.schemeContent.setText(model.getContent());
                holder.itemView.setOnClickListener(view -> {
                    String schemeID =schmeAdapter.getSnapshots().getSnapshot(position).getId();
                    Toast.makeText(AdminBoard.this, ""+schemeID, Toast.LENGTH_SHORT).show();
                    DocumentReference dr = firestore.collection("SchemesDB").document("schemesDB").collection("schemes").document(schemeID);
                    Intent intent = new Intent(AdminBoard.this, Viewscheme.class);
                    intent.putExtra("title",model.getTitle());
                    intent.putExtra("content",model.getContent());
                    intent.putExtra("schemeid",schemeID);
                    startActivity(intent);
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminBoard.this);
                        builder.setMessage("Do you wanna Delete?");
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                            String schemeID =schmeAdapter.getSnapshots().getSnapshot(position).getId();
                            Toast.makeText(AdminBoard.this, ""+schemeID, Toast.LENGTH_SHORT).show();
                            DocumentReference dr = firestore.collection("SchemesDB").document("schemesDB").collection("schemes").document(schemeID);
                            dr.delete().addOnSuccessListener(unused -> Toast.makeText(AdminBoard.this, "Deleted Successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(AdminBoard.this, "Failed to Delete "+e, Toast.LENGTH_SHORT).show());
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        return false;
                    }
                });

            }
            @NonNull
            @Override
            public SchemeiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_schemes,parent, false );
                return new SchemeiewHolder(view);
            }
        };
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager =new StaggeredGridLayoutManager(1    ,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter( schmeAdapter);
    }

    public static class SchemeiewHolder extends RecyclerView.ViewHolder{
        private final TextView schemeTitle;
        private final TextView schemeContent;
        LinearLayout myScheme;

        public SchemeiewHolder(@NonNull View itemview){
            super(itemview);
            schemeTitle = itemview.findViewById(R.id.title);
            schemeContent = itemview.findViewById(R.id.content);
            myScheme = itemview.findViewById(R.id.note);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Logout");
        menu.add("Change Password");
        menu.add("New Scheme");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Logout")){
            firebaseAuth.signOut();
            finish();
            Toast.makeText(this, "Signout Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminBoard.this,Login.class));
        }
        else if(item.getTitle().equals("New Scheme")){
             startActivity(new Intent(AdminBoard.this, CreateScheme.class));
        }
        else if(item.getTitle().equals("Change Password")){
            startActivity(new Intent(AdminBoard.this,Forgotpswd.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        schmeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (schmeAdapter != null) {
            schmeAdapter.startListening();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminBoard.this);
        builder.setMessage("Do uhh wanna Exit?");
        builder.setTitle("Alert");
        builder.setCancelable(false);
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> finish());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}