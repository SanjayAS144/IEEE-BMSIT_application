package com.bmsit.ieeebmsit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_username,reg_email,reg_pass,reg_phonenum;
    private Button reg_registerbtn;
    private FirebaseAuth mAuth;
    private RelativeLayout rlayout;
    private Animation animation;
    private ProgressDialog mregprogress;
    private DatabaseReference mDatabase;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        reg_email=findViewById(R.id.etEmail);
        reg_pass=findViewById(R.id.etPassword);
        reg_username=findViewById(R.id.etUsername);
        reg_registerbtn=findViewById(R.id.btRegister);
        reg_phonenum = findViewById(R.id.etRePassword);
        mAuth = FirebaseAuth.getInstance();
        mregprogress= new ProgressDialog(this);

        rlayout     = findViewById(R.id.rlayout);
        animation   = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        rlayout.setAnimation(animation);
        reg_registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name=reg_username.getText().toString();
                String email=reg_email.getText().toString();
                String password=reg_pass.getText().toString();
                String phone_num=reg_phonenum.getText().toString();
                if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Enter The correct Data!", Toast.LENGTH_SHORT).show();
                }
                else{
                    mregprogress.setTitle("Creating Account");
                    mregprogress.setMessage("Please wait while we create your account");
                    mregprogress.setCanceledOnTouchOutside(false);
                    mregprogress.show();

                    register_user(user_name, email, password,phone_num);
                }




            }
        });



    }
    private void register_user(final String user_name, String email, String password,String phone_num) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            String token = FirebaseInstanceId.getInstance().getToken();

                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            String UID = current_user.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name",user_name);
                            userMap.put("phone number",phone_num);
                            userMap.put("status","I am a member of IEEE");
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                            userMap.put("token_id",token);
                            mDatabase.setValue(userMap).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){

                                    mregprogress.dismiss();
                                    Intent newintent=new Intent(RegisterActivity.this,MainActivity.class);
                                    newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(newintent);
                                    finish();

                                }
                                else{
                                    Toast.makeText(RegisterActivity.this, task1.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });



                            /*
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();


                            */

                        } else {
                            mregprogress.hide();
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


}
