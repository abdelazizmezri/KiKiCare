package com.yaapps.kikicare.UI.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.yaapps.kikicare.R;
import com.yaapps.kikicare.UI.AdoptionActivity;
import com.yaapps.kikicare.UI.MyAnimalsActivity;

public class DashboardFragment extends Fragment {
    View view;
    Button firstButton;
    CardView cardadoption;
    CardView cardanimal;
    CardView cardagenda;
    CardView cardfmarket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        cardadoption=view.findViewById(R.id.AdoptionCard);
        cardadoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdoptionActivity.class);
                startActivity(intent);

            }
        });
        cardanimal=view.findViewById(R.id.addCard);
        cardanimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MyAnimalsActivity.class);
                startActivity(intent);

            }
        });
// get the reference of Button

        return view;
    }


    public void showFragment(Fragment fragment){
        FragmentManager ft=getFragmentManager();
        Fragment myFragment = ft.findFragmentByTag("MY_FRAGMENT");
        if (myFragment != null && myFragment.isVisible()) {
            ft .beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                    android.R.animator.fade_out)
                    .hide(myFragment)
                    .show(fragment)
                    .commit();
        }
    }
}
