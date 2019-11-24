package com.yaapps.kikicare.UI.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputLayout;
import com.yaapps.kikicare.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AnimalRegisterFragment extends Fragment {
    View view;
    EditText birth,name,race;
    Spinner type,sexe,size;
    TextInputLayout inputName,inputRace,inputBirth;
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.register_animal_fragment, container, false);

        name= view.findViewById(R.id.editTextName);
        race= view.findViewById(R.id.editTextRace);
        birth= view.findViewById(R.id.editTextAge);
        inputName= view.findViewById(R.id.textInputName);
        inputBirth= view.findViewById(R.id.textInputAge);
        inputRace= view.findViewById(R.id.textInputRace);
        Spinner type=view.findViewById(R.id.spinnerType);
        Spinner sexe=view.findViewById(R.id.spinnerSexe);
        Spinner size=view.findViewById(R.id.spinnerSize);
        Button save=view.findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validator())
                {
                    //TODO 1 Call Animal service To add in database

                displayFragment(new ProfileFragment());
                }
            }
        });
        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }
        };
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        return view;
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        birth.setText(sdf.format(myCalendar.getTime()));
    }
    protected void displayFragment(Fragment fragment) {
        FragmentManager ft = getFragmentManager();
        Fragment myFragment = ft.findFragmentByTag("REGISTER");
        if (myFragment != null && myFragment.isVisible()) {
            ft.beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                    android.R.animator.fade_out)
                    .hide(myFragment)
                    .replace(R.id.containerList,fragment,"PROFILE")
                    .commit();
        }
    }

    public boolean validator(){

        if (name.getText().toString().length() == 0
                || race.getText().toString().length() == 0
        || birth.getText().toString().length()==0){
           inputName.setError("Name must  not be empty");
            inputRace.setError("Race must  not be empty");
            inputBirth.setError("Birth Date must  not be empty");
            return false;
        }



        //TODO 2 Check Animal existance in database



        return true;
    }
}


