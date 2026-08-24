package com.example.drishti;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CCTVFeed extends AppCompatActivity {
    TextView projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cctvfeed);
        projectName=findViewById(R.id.projectName);
        String project=getIntent().getStringExtra("project");
        if(project!=null){
            projectName.setText(project);
        }
    }
}