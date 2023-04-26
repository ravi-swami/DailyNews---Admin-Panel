package com.example.dailynews_adminpanel;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.TimeZone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button upload;
    ImageView imageView;
    FloatingActionButton browse, close;
    EditText editTitle, editContent;
    String date, title, content;
    Uri dbUri;
    ProgressBar progressBar;

    Model model;

    FirebaseFirestore db;
    FirebaseStorage storage;

    DateFormat dateFormat;
    Date dat;

    ActivityResultLauncher<String> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browse = findViewById(R.id.browse);
        close = findViewById(R.id.close);
        upload = findViewById(R.id.upload);
        imageView = findViewById(R.id.imageView);
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();



        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if(uri == null){
                            imageView.setImageResource(R.drawable.imgg);
                            imageView.setImageURI(null);
                        }
                        else{
                            imageView.setImageURI(uri);
                        }

                        dbUri = uri;
                    }
                });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                dat = new Date();

                upload.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                if(!(editTitle.getText().toString().isEmpty())){
                    date = dateFormat.format(dat);
                    title = editTitle.getText().toString();
                    content = editContent.getText().toString();


                    if(dbUri == null){
                        model = new Model(date, title, content,"null");
                        db.collection("News").add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MainActivity.this, "News Uploaded", Toast.LENGTH_SHORT).show();
                                upload.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Upload Failed!!", Toast.LENGTH_SHORT).show();
                                upload.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                    else{
                        final StorageReference reference = storage.getReference()
                                .child("Images").child(String.valueOf(System.currentTimeMillis()));

                        reference.putFile(dbUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        model = new Model(date, title, content, uri.toString());
                                        db.collection("News").add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(MainActivity.this, "News Uploaded", Toast.LENGTH_SHORT).show();
                                                upload.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "Upload Failed!!", Toast.LENGTH_SHORT).show();
                                                upload.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }


                }
                else{
                    Toast.makeText(MainActivity.this, "Please Fill!!", Toast.LENGTH_SHORT).show();
                    upload.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }



            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.imgg);
                imageView.setImageURI(null);
                dbUri = null;
            }
        });

    }
}