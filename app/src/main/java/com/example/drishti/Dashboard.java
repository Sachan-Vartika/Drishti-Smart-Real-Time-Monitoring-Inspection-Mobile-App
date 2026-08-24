package com.example.drishti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {
    Button randomInspection,startInspection,cctv,analytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        randomInspection=findViewById(R.id.randomInspection);
        startInspection=findViewById(R.id.startInspection);
        cctv=findViewById(R.id.cctv);
        analytics=findViewById(R.id.analytics);
        randomInspection.setOnClickListener(v -> {
            Intent i=new Intent(
                    Dashboard.this,
                    RandomInspection.class
            );
            startActivity(i);
        });
        startInspection.setOnClickListener(v -> {
            Intent i=new Intent(
                    Dashboard.this,
                    StartInspection.class
            );
            startActivity(i);
        });
        cctv.setOnClickListener(v -> {
            Intent i=new Intent(
                    Dashboard.this,
                    CCTV.class
            );
            startActivity(i);
        });
        analytics.setOnClickListener(v -> {
            Intent i=new Intent(
                    Dashboard.this,
                    Analytics.class
            );
            startActivity(i);
        });
    }
}