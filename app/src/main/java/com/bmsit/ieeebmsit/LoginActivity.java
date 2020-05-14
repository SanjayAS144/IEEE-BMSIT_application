package com.bmsit.ieeebmsit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    Button mRegister;
    FirebaseAuth mAuth;
    private DatabaseReference muserdatabase;
    private FirebaseUser mCurrentuser;
    private ProgressDialog mprograssdiolog;
    Button login;
    EditText username,Password;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN=1230;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mRegister = (Button) findViewById(R.id.btRegister);
        mAuth = FirebaseAuth.getInstance();
        username=findViewById(R.id.tgmail);
        mprograssdiolog=new ProgressDialog(this);
        login=findViewById(R.id.btLogin);
        Password=findViewById(R.id.etPassword);
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regiintent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regiintent);
            }
        });






        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String musername=username.getText().toString();
                String mpassword=Password.getText().toString();


                if(TextUtils.isEmpty(musername) || TextUtils.isEmpty(mpassword)){
                    Toast.makeText(LoginActivity.this, "Enter the Correct Details", Toast.LENGTH_SHORT).show();

                }else
                {

                        mprograssdiolog.setTitle("Logging In...");
                        mprograssdiolog.setMessage("Please wait while we check your credentials");
                        mprograssdiolog.setCanceledOnTouchOutside(false);
                        mprograssdiolog.show();
                        loginuser(musername,mpassword);




                }

            }
        });





    }

    private void loginuser(String username, String mpassword) {


        mAuth.signInWithEmailAndPassword(username, mpassword)
                .addOnCompleteListener(  new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mprograssdiolog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent newintent=new Intent(LoginActivity.this,MainActivity.class);
                            newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(newintent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            mprograssdiolog.hide();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });


    }








  }
