package com.yaapps.kikicare.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yaapps.kikicare.R;

public class FragmentWelcome extends Fragment {
    TextView firstname;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        firstname=rootView.findViewById(R.id.fname);


        return rootView;
    }
}
