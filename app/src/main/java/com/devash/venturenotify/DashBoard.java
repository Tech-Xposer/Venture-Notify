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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
public class DashBoard extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirestoreRecyclerAdapter<ModelClass, NoteViewHolder> schmeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("SchemesDB").document("schemesDB").collection("schemes").orderBy("title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ModelClass> allschems =
                new FirestoreRecyclerOptions.Builder<ModelClass>()
                        .setQuery(query, ModelClass.class)
                        .build();
        schmeAdapter = new FirestoreRecyclerAdapter<ModelClass, NoteViewHolder>(allschems) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull ModelClass model) {
                holder.schemeTitle.setText(model.getTitle());
                holder.schemeContent.setText(model.getContent());
                holder.itemView.setOnClickListener(view -> {
                    String schemeID =schmeAdapter.getSnapshots().getSnapshot(position).getId();
                    Toast.makeText(DashBoard.this, ""+schemeID, Toast.LENGTH_SHORT).show();
                    DocumentReference dr = firestore.collection("SchemesDB").document("schemesDB").collection("schemes").document(schemeID);
                    Intent intent = new Intent(DashBoard.this, Viewscheme.class);
                    intent.putExtra("title",model.getTitle());
                    intent.putExtra("content",model.getContent());
                    intent.putExtra("schemeid",schemeID);
                    startActivity(intent);
                });
            }
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_schemes,parent, false );
                return new NoteViewHolder(view);
            }
        };
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager =new StaggeredGridLayoutManager(1    ,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter( schmeAdapter);
    }
    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        private final TextView schemeTitle;
        private final TextView schemeContent;
        LinearLayout myNote;
        public NoteViewHolder(@NonNull View itemview){
            super(itemview);
            schemeTitle = itemview.findViewById(R.id.title);
            schemeContent = itemview.findViewById(R.id.content);
            myNote = itemview.findViewById(R.id.note);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Logout");
        menu.add("Change Password");
        if(firebaseUser.getEmail().equals("asharma89505@gmail.com")){
            menu.add("AdminPage");
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Logout")){
            firebaseAuth.signOut();
            finish();
            Toast.makeText(this, "Signout Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashBoard.this,Login.class));
        }else if(item.getTitle().equals("AdminPage")){
            startActivity(new Intent(DashBoard.this, AdminBoard.class));
        } else if(item.getTitle().equals("Change Password")){
            startActivity(new Intent(DashBoard.this,Forgotpswd.class));
        }
        return super.onOptionsItemSelected(item);}
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
        builder.setMessage("Do uhh wanna Exit?");
        builder.setTitle("Alert");
        builder.setCancelable(false);
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> finish());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
