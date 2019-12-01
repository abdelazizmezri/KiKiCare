package com.yaapps.kikicare.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.yaapps.kikicare.R;
import com.yaapps.kikicare.UI.fragments.AnimalListFragment;

public class MyAnimalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animals);
        displayFragment(new AnimalListFragment());

    }
    protected void displayFragment(Fragment fragment) {
        FragmentManager ft = getSupportFragmentManager();
                ft.beginTransaction()
                .replace(R.id.containerList,fragment,"LIST")
                .commit();
    }

}
