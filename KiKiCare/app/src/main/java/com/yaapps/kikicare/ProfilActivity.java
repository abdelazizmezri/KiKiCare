package com.yaapps.kikicare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yaapps.kikicare.Entity.User;

public class ProfilActivity extends AppCompatActivity {

    ImageView imageProfil;
    TextView nameProfil;
    TextView emailProfil;
    TextView firstNameUser;
    TextView lastNameUser;
    TextView emailUser;
    Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User user = new PrefManager(this).getUser();


    }
}
