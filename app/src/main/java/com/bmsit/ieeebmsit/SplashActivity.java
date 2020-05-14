package com.bmsit.ieeebmsit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class SplashActivity extends AppCompatActivity {

    TextView t1,t2;
    CircleImageView I;
    Animation txtanim,imganim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        t1=findViewById(R.id.bmsit);
        t2=findViewById(R.id.ieee);
        I=findViewById(R.id.ieeelogo);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startenteranimation();
            }
        },1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startexitanimation();
            }
        },3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        },5000);
    }

    private void startexitanimation() {

        t1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.textout));
        t2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.textout));
        I.startAnimation(AnimationUtils.loadAnimation(this,R.anim.image_animation));
        I.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);


    }

    private void startenteranimation() {

        I.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splashanime));
        t1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ieeeanimation));
        t2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.ieeeanimation));
        I.setVisibility(View.VISIBLE);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);




    }
}
