package com.imbuegen.alumniapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 1500;
    private ImageView iv;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tv = findViewById(R.id.splash_screen_name);
        iv = findViewById(R.id.splash_screen_logo);


        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransitions);
        Animation bottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        Animation top = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        tv.startAnimation(bottom);
        iv.startAnimation(top);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIMEOUT);
    }
}
