package com.yaapps.kikicare.UI.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.yaapps.kikicare.R;
public class UpdateAnimalFragment extends Fragment {
    int id;
    EditText name,race,size,birth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        View rootView = inflater.inflate(R.layout.update_animal_fragment, container, false);
        name=rootView.findViewById(R.id.editTextName);
        race=rootView.findViewById(R.id.editTextRace);
        birth=rootView.findViewById(R.id.editTextAge);
        //size=rootView.findViewById(R.id.tv_birth);

checkAndFillData();

        return rootView;
    }
    private void checkAndFillData()
    {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("id",0);
            name.setText(bundle.getString("name"));
            race.setText(bundle.getString("race"));
            birth.setText(bundle.getString("birth"));
        }
    }
}
