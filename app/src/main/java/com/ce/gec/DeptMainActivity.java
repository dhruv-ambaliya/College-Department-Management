package com.ce.gec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DeptMainActivity extends AppCompatActivity {
        ImageView uploadImage;
        Button saveButton;
        EditText uploadTopic, uploadDesc;
        String imageURL;
        Uri uri;
     String fileExtension;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dept_main);

            uploadImage = findViewById(R.id.uploadImage);
            uploadDesc = findViewById(R.id.uploadDesc);
            uploadTopic = findViewById(R.id.uploadTopic);
            saveButton = findViewById(R.id.saveButton);

            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK){
                                Intent data = result.getData();
                                uri = data.getData();
                                ContentResolver cR= getContentResolver();
                                MimeTypeMap mime = MimeTypeMap.getSingleton();
                                fileExtension = mime.getExtensionFromMimeType(cR.getType(uri));
                                uploadImage.setImageURI(uri);
                            } else {
                                Toast.makeText(DeptMainActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

            uploadImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent photoPicker = new Intent(Intent.ACTION_PICK);
                    photoPicker.setType("*/*");
                    String[] mimeTypes = {"image/*", "application/pdf"};
                    photoPicker.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    activityResultLauncher.launch(photoPicker);
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String val = uploadTopic.getText().toString();
                    if (val.isEmpty()) {
                        uploadTopic.setError("Username cannot be empty");
                    } else {
                        uploadTopic.setError(null);
                        saveData();

                    }}
            });
        }

        public void saveData(){

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images")
                    .child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(DeptMainActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();
                    uploadData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }

        public void uploadData(){

            String title = uploadTopic.getText().toString();
            String desc = uploadDesc.getText().toString();
//            Date time = Calendar.getInstance().getTime();
            DataClass dataClass = new DataClass(title, desc, imageURL,fileExtension, new Date().getTime());

//            Date now = Calendar.getInstance().getTime();
//            DataClass dataClass = new DataClass(title, desc, imageURL, time);

            //We are changing the child from title to currentDate,
            // because we will be updating title as well and it may affect child value.

            String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

            Date currentdate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");

            String formattedDate = dateFormat.format(currentdate);



            // Create a new node
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Departments").child(currentDate);
            FirebaseDatabase.getInstance().getReference("Departments").child(formattedDate).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                uploadImage.setImageResource(android.R.drawable.ic_menu_gallery);
                                uploadDesc.setText("");
                                uploadTopic.setText("");
                                Toast.makeText(DeptMainActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DeptMainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }