package com.example.drishti;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Button login;
    FirebaseAuth f;
    TextView reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        f=FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        reg=findViewById(R.id.register);
        login.setOnClickListener(v -> {
            String s1=email.getText().toString().trim();
            String s2=password.getText().toString();
            if(TextUtils.isEmpty(s1)){
                Toast.makeText(
                        MainActivity.this,
                        "Email can't be empty.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            if(TextUtils.isEmpty(s2)){
                Toast.makeText(
                        MainActivity.this,
                        "Please enter the password first.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            if(s2.length()<6){
                Toast.makeText(
                        MainActivity.this,
                        "Password must have at least 6 characters.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            if(!s1.contains("@")){
                Toast.makeText(
                        MainActivity.this,
                        "Email must be in correct format.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            f.signInWithEmailAndPassword(s1,s2)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(
                                    MainActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();
                            Intent i=new Intent(
                                    MainActivity.this,
                                    Dashboard.class
                            );
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(
                                    MainActivity.this,
                                    "Login Failed: "+
                                            task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });
        reg.setOnClickListener(v -> {
            Intent i=new Intent(
                    MainActivity.this,
                    Register.class
            );
            startActivity(i);
        });
    }
}