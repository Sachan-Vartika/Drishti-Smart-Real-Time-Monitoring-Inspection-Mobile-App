package com.example.drishti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class RandomInspection extends AppCompatActivity {
    TextView projectName,location,type,risk,inspector;
    Button assignNow,startInspection;
    String selectedProject;
    String selectedDistrict;
    String selectedRisk;
    String selectedInspector;
    String[] projects={
            "ABC NGO",
            "Seva Institute",
            "Hope Foundation",
            "Sahyog Welfare Centre",
            "Jan Kalyan Institute"
    };
    String[] districts={
            "Ghaziabad",
            "Noida",
            "Delhi",
            "Meerut",
            "Lucknow"
    };
    String[] types={
            "Skill Development",
            "Welfare Services",
            "Rehabilitation",
            "Education",
            "Community Development"
    };
    String[] risks={
            "Low",
            "Medium",
            "High"
    };
    String[] inspectors={
            "Inspection Team A",
            "Inspection Team B",
            "Inspection Team C",
            "PMU Team 1"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_inspection);
        projectName=findViewById(R.id.projectName);
        location=findViewById(R.id.location);
        type=findViewById(R.id.type);
        risk=findViewById(R.id.risk);
        inspector=findViewById(R.id.inspector);
        assignNow=findViewById(R.id.assignNow);
        startInspection=findViewById(R.id.startInspection);
        startInspection.setEnabled(false);
        assignNow.setOnClickListener(v -> assignProject());
        startInspection.setOnClickListener(v -> {
            Intent i=new Intent(
                    RandomInspection.this,
                    StartInspection.class
            );
            i.putExtra("project",selectedProject);
            i.putExtra("district",selectedDistrict);
            i.putExtra("risk",selectedRisk);
            i.putExtra("inspector",selectedInspector);
            startActivity(i);
        });
    }
    void assignProject(){
        Random random=new Random();
        int index=random.nextInt(projects.length);
        selectedProject=projects[index];
        selectedDistrict=districts[index];
        selectedRisk=risks[random.nextInt(risks.length)];
        selectedInspector=inspectors[
                random.nextInt(inspectors.length)
                ];
        projectName.setText(selectedProject);
        location.setText(selectedDistrict);
        type.setText(types[index]);
        risk.setText("Risk Level: "+selectedRisk);
        inspector.setText("Assigned: "+selectedInspector);
        startInspection.setEnabled(true);
        assignNow.setText("REASSIGN");
    }
}