package com.devash.venturenotify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText uname, pass;
    String passWord, userName;
    TextView signup,forgotpswd;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uname= findViewById(R.id.uid);
        pass=findViewById(R.id.password);
        login = findViewById(R.id.register);
        signup = findViewById(R.id.signup);
        forgotpswd=findViewById(R.id.resetpswd);
        firebaseAuth  = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(Login.this, DashBoard.class));
        }
        login.setOnClickListener(view -> {
            userName = uname.getText().toString().trim();
            passWord = pass.getText().toString().trim();
            if(userName.isEmpty() || passWord.isEmpty()){
                Toast.makeText(Login.this, "Please Enter Credentials", Toast.LENGTH_SHORT).show();
            }else{
                if(userName.length()<8 || passWord.length()<8){
                    Toast.makeText(Login.this, "Credentials should be valid", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.signInWithEmailAndPassword(userName, passWord).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            isUserVerified();
                        }
                        else {
                            Toast.makeText(Login.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        forgotpswd.setOnClickListener(view -> {
            userName = uname.getText().toString().trim();
            if(userName.isEmpty()){
                Toast.makeText(Login.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            }else{
                firebaseAuth.sendPasswordResetEmail(userName).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(Login.this, "Reset link sent successfully ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Login.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        signup.setOnClickListener(view -> startActivity(new Intent(Login.this, Register.class)));
    }

    private void isUserVerified() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified() && userName.equals("asharma89505@gmail.com") && passWord.equals("Dev.ash@73")){
            Toast.makeText(Login.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(Login.this, AdminBoard.class ));
        }
        else if(Objects.requireNonNull(firebaseUser).isEmailVerified()){
            Toast.makeText(Login.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(Login.this, DashBoard.class ));
        }
        else {
            Toast.makeText(Login.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Do uhh wanna Exit?");
        builder.setTitle("Alert");
        builder.setCancelable(false);

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
        builder.setPositiveButton("Yes", (dialog, which) -> Login.this.finish());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}