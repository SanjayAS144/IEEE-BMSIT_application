package com.bmsit.ieeebmsit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private EditText Profile;
    private Button statusbtn;
    private FirebaseUser currentUser;
    private DatabaseReference mdatabase;
    private ProgressDialog statusprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Profile=findViewById(R.id.status_update_1);
        statusbtn=findViewById(R.id.status_btn);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = currentUser.getUid();

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mtoolbar = findViewById(R.id.status_bar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String status_value =getIntent().getStringExtra("status_value");
        Profile.setText(status_value);
        statusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusprogress= new ProgressDialog(StatusActivity.this);

                statusprogress.setTitle("Saving Changes...");
                statusprogress.setMessage("Please wait while your Status is Updated");
                statusprogress.setCanceledOnTouchOutside(false);
                statusprogress.show();
                String updateprofile = Profile.getText().toString();


                mdatabase.child("status").setValue(updateprofile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            statusprogress.dismiss();
                        }
                        else{
                            statusprogress.hide();
                            Toast.makeText(StatusActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}
