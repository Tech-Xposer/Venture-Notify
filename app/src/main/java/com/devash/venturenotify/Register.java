package com.devash.venturenotify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    EditText email, password, cpassword;
    Button signup;
    String Password, Email,cPassword ;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        cpassword=findViewById(R.id.cpassword);
        signup = findViewById(R.id.register);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email=email.getText().toString().trim();
                Password = password.getText().toString().trim();
                cPassword = cpassword.getText().toString().trim();
                if(Email.isEmpty() || Password.isEmpty() || cPassword.isEmpty()){
                    Toast.makeText(Register.this, "Credentials Required!", Toast.LENGTH_SHORT).show();
                } else if (Email.length() < 8 ||  Password.length() < 8) {
                    Toast.makeText(Register.this, "Email and password should be of 8 chars long",Toast.LENGTH_SHORT).show();
                }
                else if( Password.equals(cPassword) ){
                    firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this, "Registration  Successful", Toast.LENGTH_SHORT).show();
                                SendEmail();
                            }
                            else{
                                Toast.makeText(Register.this, "Email Not Sent!"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this, "Password Not Match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendEmail() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Register.this, "Email Sent Successfully", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Register.this, Login.class));
                }
            });
        }else {
            Toast.makeText(this, "404!  Process Failed", Toast.LENGTH_SHORT).show();
        }
    }
}