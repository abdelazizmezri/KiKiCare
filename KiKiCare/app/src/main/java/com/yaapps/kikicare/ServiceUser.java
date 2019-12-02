package com.yaapps.kikicare;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yaapps.kikicare.Entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceUser {

    Context context;
    RequestQueue queue;

    public ServiceUser(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void addUser(User user){
        String url = "http://10.0.2.2:1225/AddUser?first_name=" + user.getFirstName()
                + "&last_name=" + user.getLastName()
                + "&email=" + user.getEmail()
                + "&password=" + user.getPassword()
                + "&url_image=" + user.getUrlImage()
                + "&mode=" + user.getMode();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.println(Log.INFO, "success", response);
                        //Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(postRequest);
    }

    public User getUser(String email){

        User user = null;
        final String url = "http://10.0.2.2:1225/getUser?email=" + email;

        HttpHandler sh = new HttpHandler();

        String jsonStr = sh.makeServiceCall(url);
        if (jsonStr != null) {
            try {
                    JSONObject c = new JSONObject(jsonStr);
                    user = new User(
                            c.getInt("id"),
                            c.getString("email"),
                            c.getString("first_name"),
                            c.getString("last_name"),
                            c.getString("password"),
                            c.getString("url_image"),
                            c.getString("mode")
                    );

            } catch (final JSONException e) {
                Log.println(Log.ERROR, "", "Json parsing error: " + e.getMessage());
            }
        }else {
            Log.println(Log.ERROR, "", "Couldn't get json from server.");
        }
        return user;
    }

    private static int i = 0;
    public int isFound(String email){

        Log.println(Log.INFO, "response", i+"");
        return i;
    }
}
