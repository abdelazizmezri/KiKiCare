package com.yaapps.kikicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
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
    boolean textInputFirstNameControle, textInputLasNameControle, textInputEmailControle, textInputPasswordControle, textInputVerifPasswordControle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textInputFirstName = findViewById(R.id.textInputFirstName);
        textInputLasName = findViewById(R.id.textInputLasName);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        textInputVerifPassword = findViewById(R.id.textInputVerifPassword);

        //textInputFirstNameControle
        textInputFirstName.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (textInputFirstName.getEditText().getText().toString().isEmpty()) {
                        textInputFirstName.setError("First Name is empty");
                        textInputFirstNameControle = false;
                    }
                }
            }
        });
        textInputFirstName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!textInputFirstName.getEditText().getText().toString().isEmpty()) {
                    textInputFirstName.setErrorEnabled(false);
                    if (ControleSaisie.isUsername(textInputFirstName.getEditText().getText().toString())) {
                        textInputFirstName.setErrorEnabled(false);
                        textInputFirstNameControle = true;
                    } else {
                        textInputFirstName.setError("First Name is invalid");
                        textInputFirstNameControle = false;
                    }
                } else {
                    textInputFirstName.setError("First Name is empty");
                    textInputFirstNameControle = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //textInputLasNameControle
        textInputLasName.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (textInputLasName.getEditText().getText().toString().isEmpty()) {
                        textInputLasName.setError("Last Name is empty");
                        textInputLasNameControle = false;
                    }
                }
            }
        });
        textInputLasName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!textInputLasName.getEditText().getText().toString().isEmpty()) {
                    textInputLasName.setErrorEnabled(false);
                    if (ControleSaisie.isUsername(textInputLasName.getEditText().getText().toString())) {
                        textInputLasName.setErrorEnabled(false);
                        textInputLasNameControle = true;
                    } else {
                        textInputLasName.setError("Last Name is invalid");
                        textInputLasNameControle = false;
                    }
                } else {
                    textInputLasName.setError("Last Name is empty");
                    textInputLasNameControle = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //textInputEmailControle
        textInputEmail.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (!textInputEmail.getEditText().getText().toString().isEmpty()) {
                        textInputEmail.setErrorEnabled(false);
                        if (ControleSaisie.validEmail(textInputEmail.getEditText().getText().toString())) {
                            textInputEmail.setErrorEnabled(false);
                            textInputEmailControle = true;
                        } else {
                            textInputEmail.setError("Email is invalid");
                            textInputEmailControle = false;
                        }
                    } else {
                        textInputEmail.setError("Email is empty");
                        textInputEmailControle = false;
                    }
                }
            }
        });

        //textInputPasswordControle
        textInputPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (textInputPassword.getEditText().getText().toString().isEmpty()) {
                        textInputPassword.setError("Password is empty");
                        textInputVerifPassword.getEditText().setEnabled(false);
                        textInputPasswordControle = false;
                    }
                }
            }
        });
        textInputPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!textInputPassword.getEditText().getText().toString().isEmpty()) {
                    textInputPassword.setErrorEnabled(false);
                    if (textInputPassword.getEditText().getText().toString().length() > 7) {
                        textInputVerifPassword.getEditText().setEnabled(true);
                        textInputPasswordControle = true;
                    } else {
                        textInputPassword.setError("Password length must be more than 8");
                        textInputVerifPassword.getEditText().setEnabled(false);
                        textInputPasswordControle = false;
                    }
                } else {
                    textInputPassword.setError("Password is empty");
                    textInputVerifPassword.getEditText().setEnabled(false);
                    textInputPasswordControle = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //textInputVerifPasswordControle
        textInputVerifPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String password = textInputPassword.getEditText().getText().toString();
                    String verifPassword = textInputVerifPassword.getEditText().getText().toString();
                    if (password.equals(verifPassword)) {
                        textInputVerifPassword.setErrorEnabled(false);
                        textInputVerifPasswordControle = true;
                    } else {
                        textInputVerifPassword.setError("Password is invalid");
                        textInputVerifPasswordControle = false;
                    }
                }
            }
        });
        textInputVerifPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = textInputPassword.getEditText().getText().toString();
                String verifPassword = textInputVerifPassword.getEditText().getText().toString();
                if (password.equals(verifPassword)) {
                    textInputVerifPassword.setErrorEnabled(false);
                    textInputVerifPasswordControle = true;
                } else {
                    textInputVerifPassword.setError("Password is invalid");
                    textInputVerifPasswordControle = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Button Register
        cirRegisterButton = findViewById(R.id.cirRegisterButton);
        cirRegisterButton.setOnClickListener(v -> {
            cirRegisterButton.startAnimation();
            if(textInputFirstNameControle && textInputLasNameControle && textInputEmailControle && textInputPasswordControle && textInputVerifPasswordControle) {
                cirRegisterButton.setError(null);
                String email = textInputEmail.getEditText().getText().toString();
                String firstName = textInputFirstName.getEditText().getText().toString();
                String lastName = textInputLasName.getEditText().getText().toString();
                String password = textInputPassword.getEditText().getText().toString();
                //Register
                User user = new User(email, firstName, lastName, password, "null", "EMAIL");
                register(user, cirRegisterButton, v);
            }else{
                cirRegisterButton.revertAnimation();
                cirRegisterButton.setError("");
                Toast.makeText(RegisterActivity.this, "Formular uncomplet", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    private void register(User user, CircularProgressButton cirRegisterButton, View v){
        queue = Volley.newRequestQueue(RegisterActivity.this);
        final String url = "http://10.0.2.2:1225/getUser?email=" + user.getEmail();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        if(response.isEmpty()){
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
                                            Toast.makeText(RegisterActivity.this, user.getEmail() + " est ajouté avec succés", Toast.LENGTH_LONG).show();
                                            onLoginClick(v);
                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // error
                                            cirRegisterButton.revertAnimation();
                                            cirRegisterButton.setError("");
                                            Toast.makeText(RegisterActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );
                            queue.add(postRequest);
                        }else{
                            cirRegisterButton.revertAnimation();
                            cirRegisterButton.setError("");
                            Toast.makeText(RegisterActivity.this, "User existes", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        cirRegisterButton.revertAnimation();
                        cirRegisterButton.setError("");
                        Toast.makeText(RegisterActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(postRequest);
    }


}
