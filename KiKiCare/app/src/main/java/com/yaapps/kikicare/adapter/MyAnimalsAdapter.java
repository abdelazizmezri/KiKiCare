package com.yaapps.kikicare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yaapps.kikicare.Entity.Animal;
import com.yaapps.kikicare.R;
import com.yaapps.kikicare.database.AppDataBase;

import java.util.ArrayList;
import java.util.List;

public class MyAnimalsAdapter extends RecyclerView.Adapter<MyAnimalsAdapter.WordViewHolder>  {
    private List<Animal> animals;
    private Context mContext;
    int id_user=13; //getConnectedUser();
    public MyAnimalsAdapter(Context mContext, List<Animal> animals)
    {
        this.mContext = mContext ;
        this.animals= animals;
    }
    @NonNull
    @Override
    public MyAnimalsAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.my_animals_row, parent, false);

        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAnimalsAdapter.WordViewHolder holder, int position) {
        final Animal singleItem = animals.get(position);

        holder.name.setText(singleItem.getName());
        holder.sexe.setText(singleItem.getSexe());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDataBase.getAppDatabase(mContext).animalDao().delete(singleItem);
                MyAnimalsAdapter.this.notifyChange(
                        AppDataBase.getAppDatabase(mContext).animalDao().findByUser(id_user));
            }
        });


        //holder.image.setBackgroundResource(singleItem.getImage());

    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        public final TextView sexe;
        public final ImageView image;
        public final Button delete;
        final MyAnimalsAdapter mAdapter;

        public WordViewHolder(@NonNull View itemView, MyAnimalsAdapter mAdapter) {
            super(itemView);

            this.name = itemView.findViewById(R.id.name);
            this.image= itemView.findViewById(R.id.image);
            this.sexe= itemView.findViewById(R.id.sexe);
            this.delete= itemView.findViewById(R.id.btn_delete);

            this.mAdapter = mAdapter;
        }

    }
    public void notifyChange(List<Animal> animals){
        this.animals = animals;
        this.notifyDataSetChanged();
    }

}
