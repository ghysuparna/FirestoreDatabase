package com.example.firestoredatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText name, classNo;

    Button savebtn ,choosebtn;
    Spinner subject;
    RecyclerView listOfImagesRecyclerView;

    private final int PICK_IMAGE_REQUEST = 71;

    String nameValue,classValue,subjectValue,idValue,downloadableValue;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    public ArrayList<String> downloadedUri = new ArrayList<String>();

    public FirebaseFirestore db;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeAllVariables();

        db =FirebaseFirestore.getInstance();
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }
    private void initializeAllVariables()
    {
        name=findViewById(R.id.name);
        classNo=findViewById(R.id.classno);
        subject=findViewById(R.id.subject);
        savebtn=findViewById(R.id.savebtn);
        choosebtn=findViewById(R.id.choosebtn);
        listOfImagesRecyclerView=findViewById(R.id.list_of_images_rv);
        layoutManager=new GridLayoutManager(this,2);
        listOfImagesRecyclerView.setLayoutManager(layoutManager);
    }
    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && null != data) {
            // Get the Image from data

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                Log.i("count", String.valueOf(count));
                int currentItem = 0;
                while (currentItem < count) {
                    imageUri = data.getClipData().getItemAt(currentItem).getUri();


                    Log.i("uri", imageUri.toString());
                    mArrayUri.add(imageUri);
                    listOfImagesRecyclerView.setAdapter(new ImageAdapter(this,mArrayUri));
                    currentItem = currentItem + 1;
                }
                Log.i("listsize", String.valueOf(mArrayUri.size()));
                Toast.makeText(this, ""+mArrayUri.size(), Toast.LENGTH_SHORT).show();
            } else if (data.getData() != null) {
                imageUri = data.getData();
                mArrayUri.add(imageUri);
                listOfImagesRecyclerView.setAdapter(new ImageAdapter(this,mArrayUri));

            }
        }

    }

    private void uploadImage() {

//        if (imageUri != null) {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();

//            StorageReference sto=storageReference.

            int uploads=0;
            final StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");
            for ( uploads = 0; uploads < mArrayUri.size(); uploads++) {
                Log.e("TAG", "uploadImage: "+uploads );
                Uri Image = mArrayUri.get(uploads);
                final StorageReference imagename = ImageFolder.child("image/" + Image.getLastPathSegment());

                imagename.putFile(mArrayUri.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri url) {
                                Log.e("TAG", "onSuccess: " + url);
                                saveImageUrl(url.toString());

                            }
                        });


                    }
                });

            }
//        if(uploads==mArrayUri.size())
//        {
//            Log.e("TAG", "onSuccess11111: "+downloadedUri.toString() );
//            savedata(downloadedUri);
//        }
//        else {}


    }
    private void  saveImageUrl( String url)
    {
        downloadedUri.add(url);
        if(downloadedUri.size()==mArrayUri.size())
        {
            savedata(downloadedUri);
        }
    }


    private void savedata(ArrayList<String> downloadedUri)
    {
        Log.e("TAG", "onSuccess222222: "+downloadedUri.toString() );

        nameValue=name.getText().toString().trim();
        classValue=classNo.getText().toString().trim();
        subjectValue=subject.getSelectedItem().toString();
        Map<String, Object> user = new HashMap<>();
        user.put("idValue",2);
        user.put("nameValue",nameValue);
        user.put("classValue",classValue);
        user.put("subjectValue",subjectValue);
        user.put("dateExample",(new Timestamp(new Date())));


        Map<String, String> imageUrl = new HashMap<>();

        Log.d("TAG", "DocumentSnapshot added with ID:1111111111111 "+ this.downloadedUri.size());

        for(int i = 0; i< this.downloadedUri.size(); i++)
        {

            Log.d("TAG", "DocumentSnapshot added with ID:1111111111111 "+ this.downloadedUri.size());
            imageUrl.put("image"+i, this.downloadedUri.get(i));
        }

        user.put("imageurl",imageUrl);

        saveUsersDataInCloud(user);


        name.getText().clear();
        classNo.getText().clear();
        subject.clearFocus();

    }

    private void  saveUsersDataInCloud( Map user)
    {
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
//                        saveImages(documentReference.getId());
                        Toast.makeText(MainActivity.this,"Data Uploaded Done ",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

    }


}
