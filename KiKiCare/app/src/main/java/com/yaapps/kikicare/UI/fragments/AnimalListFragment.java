package com.yaapps.kikicare.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yaapps.kikicare.R;

public class AnimalListFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.animal_list_fragment, container, false);
        FloatingActionButton fab=view.findViewById(R.id.fab_1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               displayFragment(new AnimalRegisterFragment());
            }
        });

// get the reference of Button

        return view;
    }
    protected void displayFragment(Fragment fragment) {
        FragmentManager ft = getFragmentManager();
        Fragment myFragment = ft.findFragmentByTag("LIST");
        if (myFragment != null && myFragment.isVisible()) {
            ft.beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                    android.R.animator.fade_out)
                    .hide(myFragment)
                    .replace(R.id.containerList,fragment,"REGISTER")
                    .commit();
        }
    }
}
