package com.example.drishti;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText name,email,password,confirmPassword;
    Spinner role;
    Button createAccount;
    TextView login;
    FirebaseAuth auth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirmPassword);
        role=findViewById(R.id.role);
        createAccount=findViewById(R.id.createAccount);
        login=findViewById(R.id.login);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        String[] roles={
                "Select Role",
                "PMU Team",
                "Inspection Team",
                "NGO / Institute"
        };
        ArrayAdapter<String> adapter=
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roles
                );
        role.setAdapter(adapter);
        createAccount.setOnClickListener(v -> registerUser());
        login.setOnClickListener(v -> {
            Intent i=new Intent(
                    Register.this,
                    MainActivity.class
            );
            startActivity(i);
            finish();
        });
    }
    void registerUser(){
        String n=name.getText().toString().trim();
        String e=email.getText().toString().trim();
        String p=password.getText().toString();
        String cp=confirmPassword.getText().toString();
        String r=role.getSelectedItem().toString();
        if(TextUtils.isEmpty(n)){
            name.setError("Enter your name");
            return;
        }
        if(TextUtils.isEmpty(e)){
            email.setError("Enter your email");
            return;
        }
        if(!e.contains("@")){
            email.setError("Enter a valid email");
            return;
        }
        if(TextUtils.isEmpty(p)){
            password.setError("Enter password");
            return;
        }
        if(p.length()<6){
            password.setError(
                    "Password must have at least 6 characters"
            );
            return;
        }
        if(!p.equals(cp)){
            confirmPassword.setError(
                    "Passwords do not match"
            );
            return;
        }
        if(r.equals("Select Role")){
            Toast.makeText(
                    this,
                    "Please select your role",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        createAccount.setEnabled(false);
        auth.createUserWithEmailAndPassword(e,p)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String uid=auth.getCurrentUser().getUid();
                        Map<String,Object> user=
                                new HashMap<>();
                        user.put("name",n);
                        user.put("email",e);
                        user.put("role",r);
                        user.put("status","Pending");
                        user.put(
                                "createdAt",
                                System.currentTimeMillis()
                        );
                        db.collection("users")
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(
                                            this,
                                            "Account created successfully",
                                            Toast.LENGTH_LONG
                                    ).show();
                                    Intent i=new Intent(
                                            Register.this,
                                            MainActivity.class
                                    );
                                    startActivity(i);
                                    finish();
                                })
                                .addOnFailureListener(error -> {
                                    createAccount.setEnabled(true);
                                    Toast.makeText(
                                            this,
                                            "Profile creation failed: "+
                                                    error.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                });
                    }else{
                        createAccount.setEnabled(true);
                        Toast.makeText(
                                this,
                                "Registration failed: "+
                                        task.getException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}