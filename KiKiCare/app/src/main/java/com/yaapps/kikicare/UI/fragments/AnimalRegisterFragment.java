package com.yaapps.kikicare.UI.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.yaapps.kikicare.ControleSaisie;
import com.yaapps.kikicare.Entity.Animal;
import com.yaapps.kikicare.R;
import com.yaapps.kikicare.database.AppDataBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class AnimalRegisterFragment extends Fragment {
    View view;
    RequestQueue queue;
    private AppDataBase database ;
    //edit text
    EditText birthDate;
    //spinners
    Spinner type,sexe,size;

    final Calendar myCalendar = Calendar.getInstance();

    //TextInputs
    TextInputLayout inputName,inputRace,inputBirth;
    CircularProgressButton save;

    //Controls
    boolean inputNameControle, inputRaceControle, inputBirthControle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.register_animal_fragment, container, false);
        database = AppDataBase.getAppDatabase(getActivity());

        birthDate=view.findViewById(R.id.editTextAge);
        inputName= view.findViewById(R.id.textInputName);
        inputBirth= view.findViewById(R.id.textInputAge);
        inputRace= view.findViewById(R.id.textInputRace);
        Spinner type=view.findViewById(R.id.spinnerType);
        Spinner sexe=view.findViewById(R.id.spinnerSexe);
        Spinner size=view.findViewById(R.id.spinnerSize);
        save=view.findViewById(R.id.saveBtn);
        save.setOnClickListener(v -> {
            save.startAnimation();
            if(isConnected()) {
                if (inputNameControle && inputRaceControle) {
                    save.setError(null);
                    String name = inputName.getEditText().getText().toString();
                    String race = inputRace.getEditText().getText().toString();
                    Date birth = convertToDate(birthDate.getText().toString());
                    String typeText = type.getSelectedItem().toString();
                    String sizeText = size.getSelectedItem().toString();
                    String sexeText = sexe.getSelectedItem().toString();
                    int iduser=13; //getConnectedUser();

                    //Register
                    Animal animal = new Animal(name,typeText,race,"25/05/2018",sizeText,sexeText,"image",iduser);
                    register(animal, save, v);
                } else {
                    save.revertAnimation();
                    save.setError("");
                    Toast.makeText(getActivity(), "Incomplete Form", Toast.LENGTH_LONG).show();
                }
            }
            else{
                save.revertAnimation();
                save.setError("");
                Toast.makeText(getActivity(), "No Network Available!", Toast.LENGTH_LONG).show();
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
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });





        inputName.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (inputName.getEditText().getText().toString().isEmpty()) {
                        inputName.setError("Name is empty");
                        inputNameControle = false;
                    }
                }
            }
        });
        inputName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!inputName.getEditText().getText().toString().isEmpty()) {
                   inputName.setErrorEnabled(false);
                    if (ControleSaisie.isUsername(inputName.getEditText().getText().toString())) {
                        inputName.setErrorEnabled(false);
                       inputNameControle = true;
                    } else {
                        inputName.setError("Name is invalid");
                        inputNameControle = false;
                    }
                } else {
                    inputName.setError("Name is empty");
                    inputNameControle = false;
                }
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        inputRace.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (inputRace.getEditText().getText().toString().isEmpty()) {
                        inputRace.setError("Race is empty!");
                        inputRaceControle = false;
                    }
                }
            }
        });
        inputRace.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!inputRace.getEditText().getText().toString().isEmpty()) {
                    inputRace.setErrorEnabled(false);
                    if (ControleSaisie.isUsername(inputRace.getEditText().getText().toString())) {
                        inputRace.setErrorEnabled(false);
                        inputRaceControle = true;
                    } else {
                        inputName.setError("Race is invalid");
                        inputNameControle = false;
                    }
                } else {
                    inputRace.setError("Race is empty");
                    inputRaceControle = false;
                }
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        return view;
    }
    private void updateLabel() {
        String myFormat = "dd/MMM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        birthDate.setText(sdf.format(myCalendar.getTime()));
    }
    private Date convertToDate(String date) {
        SimpleDateFormat spf=new SimpleDateFormat("dd/MMM/yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }
    protected void displayFragment(Fragment fragment,Animal animal) {
        FragmentManager ft = getFragmentManager();
        Fragment myFragment = ft.findFragmentByTag("REGISTER");
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



    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    private void register(Animal animal, CircularProgressButton save, View v){
        queue = Volley.newRequestQueue(getActivity());
        final String url = "http://10.0.2.2:1225/getAnimal?name=" + animal.getName();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        if(response.isEmpty()){
                            String url = "http://10.0.2.2:1225/AddAnimal?id_user=" +animal.getIduser()
                                    + "&name=" + animal.getName()
                                    + "&sexe=" + animal.getSexe()
                                    + "&type=" + animal.getType()
                                   // + "&date_nais=" + animal.getBirth()
                                    + "&date_nais=" + animal.getBirthDate()
                                    + "&race=" + animal.getRace()
                                    + "&size=" + animal.getSize()
                                    + "&image=" +animal.getUrlImage();
                            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>()
                                    {
                                        @Override
                                        public void onResponse(String response) {
                                            // response
                                            save.revertAnimation();
                                            Toast.makeText(getActivity(), animal.getName() + " IS Added With Success", Toast.LENGTH_LONG).show();
                                            displayFragment(new ProfileFragment(),animal);
                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // error
                                            save.revertAnimation();
                                           save.setError("");
                                            Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );
                            queue.add(postRequest);
                        }else{
                            save.revertAnimation();
                            save.setError("");
                            Toast.makeText(getActivity(), "Animal existes", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        save.revertAnimation();
                        save.setError("");
                        Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(postRequest);
        database.animalDao().insertOne(animal);

        //finish();
    }

}


