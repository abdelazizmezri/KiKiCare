package com.yaapps.kikicare.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.yaapps.kikicare.R;

public class SplashScreenActivity extends AppCompatActivity {

    ConstraintLayout splash;



    Handler handler = new Handler();
    Runnable show = new Runnable() {
        @Override
        public void run() {
            splash.setVisibility(View.VISIBLE);
        }
    };

    Runnable hide = new Runnable() {
        @Override
        public void run() {
            splash.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splash = findViewById(R.id.splash);
        handler.postDelayed(show, 1000);
        handler.postDelayed(hide, 3000);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }

    @Override
    public void onBackPressed() {

    }
}
