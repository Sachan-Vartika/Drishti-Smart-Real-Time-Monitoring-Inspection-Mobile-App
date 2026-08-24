package com.example.drishti;

import static android.content.Intent.getIntent;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class StartInspection extends AppCompatActivity {
    Button getLocation,capturePhoto,submitInspection;
    TextView locationText;
    ImageView photoPreview;
    FusedLocationProviderClient locationClient;
    final int LOCATION_REQUEST=100;
    final int CAMERA_REQUEST=101;
    FirebaseFirestore db;
    String inspectionId;
    String imageName;
    Uri imageUri;
    double latitude=0;
    double longitude=0;
    float accuracy=0;
    boolean locationCaptured=false;
    void submitInspection(){
        EditText projectName=findViewById(R.id.projectName);
        EditText remarks=findViewById(R.id.remarks);
        CheckBox checkFunctioning=findViewById(R.id.checkFunctioning);
        CheckBox checkStaff=findViewById(R.id.checkStaff);
        CheckBox checkBeneficiary=findViewById(R.id.checkBeneficiary);
        CheckBox checkRecords=findViewById(R.id.checkRecords);
        CheckBox checkInfrastructure=findViewById(R.id.checkInfrastructure);
        String project=projectName.getText().toString().trim();
        String remark=remarks.getText().toString().trim();
        String district=getIntent().getStringExtra("district");
        String risk=getIntent().getStringExtra("risk");
        String inspector=getIntent().getStringExtra("inspector");
        if(project.isEmpty()){
            projectName.setError("Enter project name");
            projectName.requestFocus();
            return;
        }
        if(!checkFunctioning.isChecked() ||
                !checkStaff.isChecked() ||
                !checkBeneficiary.isChecked() ||
                !checkRecords.isChecked() ||
                !checkInfrastructure.isChecked()){
            Toast.makeText(
                    this,
                    "Complete all inspection checks",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        if(!locationCaptured){
            Toast.makeText(
                    this,
                    "Capture GPS location first",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        if(imageUri==null){
            Toast.makeText(
                    this,
                    "Capture inspection photo first",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        if(inspectionId==null){
            inspectionId="INS-"+new SimpleDateFormat(
                    "yyyyMMdd-HHmmss",
                    Locale.getDefault()
            ).format(new Date());
            imageName=inspectionId+".jpg";
        }
        HashMap<String,Object> data=new HashMap<>();
        data.put("inspectionId",inspectionId);
        data.put("project",project);
        data.put("remarks",remark);
        data.put(
                "functioning",
                checkFunctioning.isChecked()
        );
        data.put(
                "staffPresent",
                checkStaff.isChecked()
        );
        data.put(
                "beneficiaryServices",
                checkBeneficiary.isChecked()
        );
        data.put(
                "recordsMaintained",
                checkRecords.isChecked()
        );
        data.put(
                "infrastructure",
                checkInfrastructure.isChecked()
        );
        data.put("latitude",latitude);
        data.put("longitude",longitude);
        data.put("accuracy",accuracy);
        data.put("imageName",imageName);
        data.put(
                "timestamp",
                System.currentTimeMillis()
        );
        data.put("district",district);
        data.put("riskLevel",risk);
        data.put("assignedInspector",inspector);
        db.collection("inspections")
                .document(inspectionId)
                .set(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(
                            this,
                            "Inspection Submitted Successfully",
                            Toast.LENGTH_LONG
                    ).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            this,
                            "Submission failed: "+
                                    e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_inspection);
        String project=getIntent().getStringExtra("project");
        String district=getIntent().getStringExtra("district");
        String risk=getIntent().getStringExtra("risk");
        String inspector=getIntent().getStringExtra("inspector");
        EditText projectName=findViewById(R.id.projectName);
        if(project!=null){
            projectName.setText(project);
            projectName.setEnabled(false);
        }
        getLocation=findViewById(R.id.getLocation);
        locationText=findViewById(R.id.locationText);
        capturePhoto=findViewById(R.id.capturePhoto);
        photoPreview=findViewById(R.id.photoPreview);
        locationClient= LocationServices.getFusedLocationProviderClient(this);
        getLocation.setOnClickListener(v -> getLocation());
        capturePhoto.setOnClickListener(v -> openCamera());
        submitInspection=findViewById(R.id.submitInspection);
        submitInspection.setOnClickListener(v -> submitInspection());
        db=FirebaseFirestore.getInstance();
    }
    void getLocation(){
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_REQUEST
            );
            return;
        }
        locationText.setText("Getting location...");
        CancellationTokenSource token=
                new CancellationTokenSource();
        locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                token.getToken()
        ).addOnSuccessListener(location -> {
            if(location!=null){
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                accuracy=location.getAccuracy();
                locationCaptured=true;
                locationText.setText(
                        "GPS Location Captured\n"+
                                "Latitude: "+latitude+"\n"+
                                "Longitude: "+longitude+
                                "\nAccuracy: "+accuracy+" meters"
                );
                Toast.makeText(
                        this,
                        "GPS Location Captured",
                        Toast.LENGTH_SHORT
                ).show();
            }else{
                locationCaptured=false;
                locationText.setText(
                        "Unable to get location"
                );
                Toast.makeText(
                        this,
                        "Please enable GPS and try again",
                        Toast.LENGTH_LONG
                ).show();
            }
        }).addOnFailureListener(e -> {
            locationCaptured=false;
            locationText.setText(
                    "Location error: "+e.getMessage()
            );
        });
    }
    void openCamera(){
        if(checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST
            );
            return;
        }
        inspectionId="INS-"+new SimpleDateFormat(
                "yyyyMMdd-HHmmss",
                Locale.getDefault()
        ).format(new Date());
        imageName=inspectionId+".jpg";
        ContentValues values=new ContentValues();
        values.put(
                MediaStore.Images.Media.DISPLAY_NAME,
                imageName
        );
        values.put(
                MediaStore.Images.Media.MIME_TYPE,
                "image/jpeg"
        );
        values.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES+
                        "/DRISHTI"
        );
        imageUri=getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        );
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(
                MediaStore.EXTRA_OUTPUT,
                imageUri
        );
        startActivityForResult(i,CAMERA_REQUEST);
    }
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data){
        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );
        if(requestCode==CAMERA_REQUEST &&
                resultCode==RESULT_OK &&
                imageUri!=null){
            photoPreview.setImageURI(imageUri);
            Toast.makeText(
                    this,
                    "Evidence photo captured",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults){
        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );
        if(requestCode==LOCATION_REQUEST){
            if(grantResults.length>0 &&
                    grantResults[0]==
                            PackageManager.PERMISSION_GRANTED){
                getLocation();
            }else{
                Toast.makeText(
                        this,
                        "Location permission is required",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
        if(requestCode==CAMERA_REQUEST){
            if(grantResults.length>0 &&
                    grantResults[0]==
                            PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else{
                Toast.makeText(
                        this,
                        "Camera permission is required",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}