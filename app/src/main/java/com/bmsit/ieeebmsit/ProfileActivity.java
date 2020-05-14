package com.bmsit.ieeebmsit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference muserdatabase;
    private FirebaseUser mCurrentuser;


    private CircleImageView mimage;
    TextView mprofile_name;
    TextView mstatus;
    Button changestatus,imagebtn,namebtn;
    ImageButton mimagebutton;
    private static final int gallery_pick=1;
    private ProgressDialog mprogressdialog;

    private StorageReference mstorageref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        mimage=(CircleImageView) findViewById(R.id.profile_image);
        mprofile_name=findViewById(R.id.Profilename);
        mstatus = findViewById(R.id.Status);
        changestatus=findViewById(R.id.statuschange);
        imagebtn=findViewById(R.id.imgchange);
        namebtn=findViewById(R.id.profcgange);
        mimagebutton=findViewById(R.id.backbtn);

        namebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=mprofile_name.getText().toString();
                Intent status_intent = new Intent(ProfileActivity.this,editprofileActivity.class);
                status_intent.putExtra("name",name);

                startActivity(status_intent);


            }
        });

        mimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryintent = new Intent();
                galleryintent.setType("image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryintent,"SELECT IMAGE"),gallery_pick);
            }
        });
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        mstorageref= FirebaseStorage.getInstance().getReference();

        String current_Uid = mCurrentuser.getUid();
        muserdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_Uid);
        changestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status_value=mstatus.getText().toString();
                Intent status_intent = new Intent(ProfileActivity.this,StatusActivity.class);
                status_intent.putExtra("status_value",status_value);
                startActivity(status_intent);
            }
        });



        muserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mprofile_name.setText(name);
                mstatus.setText(status);
                Picasso.get().load(image).into(mimage);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallery_pick && resultCode == RESULT_OK){
            Uri imageuri =data.getData();

            CropImage.activity(imageuri)
                    .setAspectRatio(1,1)
                    .start(this);
            //Toast.makeText(SettingActivity.this, (CharSequence) imageuri, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mprogressdialog=new ProgressDialog(ProfileActivity.this);
                mprogressdialog.setTitle("Uploading Image....");
                mprogressdialog.setMessage("Please wait till the image is Uploaded");
                mprogressdialog.setCanceledOnTouchOutside(false);
                mprogressdialog.show();
                Uri resultUri = result.getUri();
                String current_userid = mCurrentuser.getUid();
                final StorageReference filepath = mstorageref.child("Profile_images").child(current_userid + ".jpg");
                filepath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            mprogressdialog.dismiss();
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();

                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            String downloadurl = task.getResult().toString();
                            muserdatabase.child("image").setValue(downloadurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mprogressdialog.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(100);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
