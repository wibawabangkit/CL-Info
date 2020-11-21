package com.example.testkkp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation app_splash, btt;
    ImageView app_logo;
    TextView app_title;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getUsernameLocal();

        //load anim
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);

        app_logo = findViewById(R.id.app_logo);
        app_title = findViewById(R.id.app_title);

        app_logo.startAnimation(app_splash);
        app_title.startAnimation(btt);

    }

    private void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
        if (nik_key_new.isEmpty()) {
            //timer splash 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gogetStarted = new Intent(SplashScreen.this, GetStarted.class);
                    startActivity(gogetStarted);
                    finish();
                }
            }, 4000);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gotohome = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(gotohome);
                    finish();
                }
            }, 4000);
        }
    }
}