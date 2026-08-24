package com.example.drishti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CCTV extends AppCompatActivity {
    Button connect1,connect2,connect3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cctv);
        connect1=findViewById(R.id.connect1);
        connect2=findViewById(R.id.connect2);
        connect3=findViewById(R.id.connect3);
        connect1.setOnClickListener(v -> openCamera("ABC NGO"));
        connect2.setOnClickListener(v -> openCamera("Seva Institute"));
        connect3.setOnClickListener(v -> openCamera("Hope Foundation"));
    }
    void openCamera(String project){
        Intent i=new Intent(
                CCTV.this,
                CCTVFeed.class
        );
        i.putExtra("project",project);
        startActivity(i);
    }
}