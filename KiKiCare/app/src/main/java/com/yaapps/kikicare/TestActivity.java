package com.yaapps.kikicare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        String uri = new PrefManager(this).getUser().getUrlImage();
        Picasso.get().load(uri).into((ImageView) findViewById(R.id.imageView));
    }
}
