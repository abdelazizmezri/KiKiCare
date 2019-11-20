package com.yaapps.kikicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class RegisterActivity extends AppCompatActivity {

    RequestQueue queue;
    TextInputLayout textInputFirstName, textInputLasName, textInputEmail, textInputPassword, textInputVerifPassword;
    CircularProgressButton cirRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        textInputFirstName = findViewById(R.id.textInputFirstName);
        textInputLasName = findViewById(R.id.textInputLasName);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        textInputVerifPassword = findViewById(R.id.textInputVerifPassword);
        cirRegisterButton = findViewById(R.id.cirRegisterButton);
        cirRegisterButton.setOnClickListener(v -> {
            cirRegisterButton.startAnimation();
            String email = textInputEmail.getEditText().getText().toString();
            String firstName = textInputFirstName.getEditText().getText().toString();
            String lastName = textInputLasName.getEditText().getText().toString();
            String password = textInputPassword.getEditText().getText().toString();
            String verifPassword = textInputVerifPassword.getEditText().getText().toString();
            User user = new User(email,firstName,lastName,password,"Image","EMAIL");
            //Log.println(Log.INFO,"user",user.toString());
            queue = Volley.newRequestQueue(RegisterActivity.this);
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
                            cirRegisterButton.revertAnimation();
                            Toast.makeText(RegisterActivity.this, email + "est ajouté avec succés", Toast.LENGTH_LONG).show();
                            onLoginClick(v);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            cirRegisterButton.revertAnimation();
                            Toast.makeText(RegisterActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                        }
                    }
            );
            queue.add(postRequest);
        });

    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }

}
