package com.yaapps.kikicare.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaapps.kikicare.Entity.Animal;
import com.yaapps.kikicare.R;
import com.yaapps.kikicare.UI.MyAnimalsActivity;
import com.yaapps.kikicare.UI.fragments.ProfileFragment;

import java.util.ArrayList;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.WordViewHolder>  {
    private final ArrayList<Animal> animals;
    private Context mContext;
    public AnimalAdapter(Context mContext, ArrayList<Animal> animals)
    {
        this.mContext = mContext ;
        this.animals= animals;
    }
    @NonNull
    @Override
    public AnimalAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.animal_row, parent, false);

        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalAdapter.WordViewHolder holder, int position) {
        final Animal singleItem = animals.get(position);

        holder.name.setText(singleItem.getName());
        holder.sexe.setText(singleItem.getSexe());

        holder.image.setBackgroundResource(singleItem.getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            displayFragment(new ProfileFragment(),singleItem);
            }

        });

    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        public final TextView sexe;
        public final ImageView image;
        final AnimalAdapter mAdapter;

        public WordViewHolder(@NonNull View itemView, AnimalAdapter mAdapter) {
            super(itemView);

            this.name = itemView.findViewById(R.id.name);
            this.image= itemView.findViewById(R.id.image);
            this.sexe= itemView.findViewById(R.id.sexe);

            this.mAdapter = mAdapter;
        }

    }
    protected void displayFragment(Fragment fragment, Animal animal) {
        FragmentManager ft= ((AppCompatActivity) mContext).getSupportFragmentManager();
        Fragment myFragment = ft.findFragmentByTag("LIST");
        Bundle bundle = new Bundle();
        bundle.putInt("id",animal.getId());
        bundle.putString("name",animal.getName());
        bundle.putString("race",animal.getRace());
        bundle.putString("birth",animal.getBirthDate());
        bundle.putString("sexe",animal.getSexe());
        bundle.putString("type",animal.getType());
        fragment.setArguments(bundle);
        if (myFragment != null && myFragment.isVisible()) {
            ft.beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                    android.R.animator.fade_out)
                    .hide(myFragment)
                    .replace(R.id.containerList,fragment,"PROFILE")
                    .commit();
        }
    }

}
