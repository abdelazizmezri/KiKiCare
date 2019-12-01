package com.yaapps.kikicare.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yaapps.kikicare.Entity.Animal;
import com.yaapps.kikicare.R;
import com.yaapps.kikicare.adapter.MyAnimalsAdapter;
import com.yaapps.kikicare.database.AppDataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimalListFragment extends Fragment {
    View view;
    RequestQueue queue;
    private AppDataBase database;
    private RecyclerView recyclerView;
    private List<Animal> animals = new ArrayList<Animal>();
    private MyAnimalsAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.animal_list_fragment, container, false);
        database = AppDataBase.getAppDatabase(getActivity().getApplicationContext());
        int iduser=13; //getConnectedUser();
        FloatingActionButton fab=view.findViewById(R.id.fab_1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               displayFragment(new AnimalRegisterFragment());
            }
        });
        recyclerView =view.findViewById(R.id.recycler_animal);

       // recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        animals=database.animalDao().findByUser(iduser);



        mAdapter = new MyAnimalsAdapter(getActivity(),animals);

        recyclerView.setAdapter(mAdapter);



        return view;
    }
//    public void fillData(){
//
//        queue = Volley.newRequestQueue(getActivity());
//        final String url = "http://10.0.2.2:1225/getAnimalsByUser?id_user="+13;//static user (connected user)
//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        if (!response.isEmpty()) {
//                            try {
//                                JSONArray c = new JSONArray(response);
//                                for (int i = 0; i < c.length(); i++) {
//
//                                    JSONObject item = c.getJSONObject(i);
//                                    Animal animal = new Animal(
//                                            item.getInt("id"),
//                                            item.getString("name"),
//                                            item.getString("sexe"),
//                                            item.getString("image")
//                                );
//                                animals.add(animal);}
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();
//
//                            } catch (final JSONException e) {
//                                System.out.println(e);
//
//                                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
//                            }
//                        }else {
//                            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Toast.makeText(getActivity(), "Problème de connexion", Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
//        queue.add(postRequest);
//    }



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
