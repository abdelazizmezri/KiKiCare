package com.yaapps.kikicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

public class SplashScreen extends AppCompatActivity {

    RelativeLayout splash;

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

        splash = (RelativeLayout) findViewById(R.id.splash);
        handler.postDelayed(show, 1000);
        handler.postDelayed(hide, 3000);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }
}
