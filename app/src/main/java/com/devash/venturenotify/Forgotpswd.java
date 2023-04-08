package com.devash.venturenotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Forgotpswd extends AppCompatActivity {
    EditText pswd, cpswd, opswd;
    String Password, cPassword,oPassword;
    Button submit;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpswd);
        pswd = findViewById(R.id.pswd);
        cpswd = findViewById(R.id.cpswd);
        opswd = findViewById(R.id.opswd);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Password = pswd.getText().toString().trim();
                cPassword = cpswd.getText().toString().trim();
                oPassword = opswd.getText().toString().trim();
                if(!Password.isEmpty() && !cPassword.isEmpty() && !oPassword.isEmpty()){
                    if(Password.equals(cPassword)){
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oPassword);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(Password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Forgotpswd.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(Forgotpswd.this, "Error! "+task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }else{
                        Toast.makeText(Forgotpswd.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Forgotpswd.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}