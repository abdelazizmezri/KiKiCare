package com.yaapps.kikicare.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.yaapps.kikicare.Entity.Animal;
import com.yaapps.kikicare.R;
import com.yaapps.kikicare.adapter.AnimalAdapter;

import java.util.ArrayList;

public class AdoptionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Animal> animals = new ArrayList<Animal>();
    private AnimalAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption);
        recyclerView =findViewById(R.id.recycler_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        fillData();

        mAdapter = new AnimalAdapter(this,animals);

        recyclerView.setAdapter(mAdapter);

    }
    public void fillData(){
        animals.add(new Animal(1,"SnowBall","Male",R.drawable.cat1));
        animals.add(new Animal(1,"Soni","Male",R.drawable.dog));
        animals.add(new Animal(1,"Dariya","Female",R.drawable.cat3));

    }
}


