package com.yaapps.kikicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.yaapps.kikicare.Entity.User;
import com.yaapps.kikicare.UI.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    final int RC_SIGN_IN = 0;

    CallbackManager callbackManager;

    @SuppressLint("StaticFieldLeak")
    public static GoogleSignInClient mGoogleSignInClient;

    ImageView fb_ImageView;
    ImageView google_ImageView;

    CircularProgressButton bt_login;

    String txtname;
    String txtlastname;
    String txtemail;
    String imageurl;

    boolean textInputEmailControle, textInputPasswordControle;

    private PrefManager prefManager;

    RequestQueue queue;

    TextInputLayout textInputEmail;
    TextInputLayout textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        prefManager = new PrefManager(this);

        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);

        //textInputEmailControle
        Objects.requireNonNull(textInputEmail.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
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
        });

        //textInputPasswordControle
        Objects.requireNonNull(textInputPassword.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                if (!textInputPassword.getEditText().getText().toString().isEmpty()) {
                    textInputPassword.setErrorEnabled(false);
                    if (textInputPassword.getEditText().getText().toString().length() > 7) {
                        textInputPasswordControle = true;
                    } else {
                        textInputPassword.setError("Password length must be more than 8");
                        textInputPasswordControle = false;
                    }
                } else {
                    textInputPassword.setError("Password is empty");
                    textInputPasswordControle = false;
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
                    textInputPasswordControle = textInputPassword.getEditText().getText().toString().length() > 7;
                } else {
                    textInputPassword.setError("Password is empty");
                    textInputPasswordControle = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //login_email
        bt_login = findViewById(R.id.cirLoginButton);
        bt_login.setOnClickListener(v -> {
            if(textInputEmailControle && textInputPasswordControle) {
                bt_login.startAnimation();
                queue = Volley.newRequestQueue(LoginActivity.this);
                final String url = "http://10.0.2.2:1225/getUser?email=" + textInputEmail.getEditText().getText().toString().toLowerCase();
                StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            // response
                            if (!response.isEmpty()) {
                                try {
                                    JSONObject c = new JSONObject(response);
                                    User user = new User(
                                            c.getString("email"),
                                            c.getString("first_name"),
                                            c.getString("last_name"),
                                            c.getString("password"),
                                            c.getString("url_image"),
                                            c.getString("mode")
                                    );
                                    if (user.getMode().contentEquals("EMAIL")) {
                                        if (user.getPassword().contentEquals(textInputPassword.getEditText().getText())) {
                                            bt_login.revertAnimation();
                                            prefManager.setUser(user);
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        } else {
                                            bt_login.revertAnimation();
                                            bt_login.setError("");
                                            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        bt_login.revertAnimation();
                                        bt_login.setError("");
                                        Toast.makeText(LoginActivity.this, "Try to connect with " + user.getMode().toLowerCase(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (final JSONException e) {
                                    bt_login.revertAnimation();
                                    bt_login.setError("");
                                    Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                bt_login.revertAnimation();
                                bt_login.setError("");
                                Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                            }
                        },
                        error -> {
                            // error
                            bt_login.revertAnimation();
                            bt_login.setError("");
                            Toast.makeText(LoginActivity.this, "Connection error", Toast.LENGTH_LONG).show();
                        }
                );
                queue.add(postRequest);
            }
        });

        callbackManager = CallbackManager.Factory.create();

        //checkLoginStatus();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // google ImageView
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_ImageView = findViewById(R.id.google_ImageView);
        google_ImageView.setOnClickListener(view -> {
            if(new InternetDialog(LoginActivity.this).getInternetStatus())
                signInGoogle();
        });

        //facebook ImageView
        fb_ImageView = findViewById(R.id.fb_ImageView);
        fb_ImageView.setOnClickListener(v -> {
            if(new InternetDialog(this).getInternetStatus()){
                com.facebook.login.widget.LoginButton btn = new com.facebook.login.widget.LoginButton(LoginActivity.this);
                btn.setReadPermissions("email");
                btn.performClick();
                // Callback registration
                btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            assert account != null;
            txtemail = account.getEmail();
            txtname = account.getGivenName();
            txtlastname = account.getFamilyName();
            imageurl = Objects.requireNonNull(account.getPhotoUrl()).toString();
            queue = Volley.newRequestQueue(LoginActivity.this);
            final String url = "http://10.0.2.2:1225/getUser?email=" + txtemail.toLowerCase();
            final Response.ErrorListener connection_error = error -> {
                Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_LONG).show();
            };
            StringRequest postRequest = new StringRequest(Request.Method.GET, url, response -> {
                // response
                if (response.isEmpty()) {
                    String url1 = "http://10.0.2.2:1225/AddUser?first_name=" + txtname
                            + "&last_name=" + txtlastname
                            + "&email=" + txtemail.toLowerCase()
                            + "&url_image=" + imageurl
                            + "&mode=GMAIL";
                    StringRequest postRequest1 = new StringRequest(Request.Method.POST, url1,
                            response1 -> {
                                prefManager.setUser(new User(txtemail,txtname,txtlastname,"",imageurl,"GMAIL"));
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            },
                            connection_error
                    );
                    queue.add(postRequest1);
                } else {
                    String url1 = "http://10.0.2.2:1225/UpdateUser?first_name=" + txtname
                            + "&last_name=" + txtlastname
                            + "&email=" + txtemail
                            + "&password="
                            + "&url_image=" + imageurl;
                    StringRequest postRequest1 = new StringRequest(Request.Method.POST, url1,
                            response12 -> {
                                prefManager.setUser(new User(txtemail,txtname,txtlastname,"",imageurl,"GMAIL"));
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            },
                            connection_error
                    );
                    queue.add(postRequest1);
                }
            },
                    connection_error
            );
            queue.add(postRequest);
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, "Connection error", Toast.LENGTH_LONG).show();
        }
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null) {
                txtname = "";
                txtlastname = "";
                txtemail = "";
                imageurl = "";
                Toast.makeText(LoginActivity.this,"User Logged out",Toast.LENGTH_LONG).show();
            } else{
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken) {
        @SuppressLint("CheckResult") GraphRequest request = GraphRequest.newMeRequest(newAccessToken, (object, response) -> {
            try {
                String first_name = object.getString("first_name");
                String last_name = object.getString("last_name");
                String email = object.getString("email");
                String id = object.getString("id");
                String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";

                txtemail = email;
                txtname = first_name;
                txtlastname =  last_name;
                imageurl = image_url;

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();

                queue = Volley.newRequestQueue(LoginActivity.this);
                final String url = "http://10.0.2.2:1225/getUser?email=" + txtemail.toLowerCase();
                StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                        response1 -> {
                            // response
                            if(response1.isEmpty()){
                                String url1 = "http://10.0.2.2:1225/AddUser?first_name=" + txtname
                                        + "&last_name=" + txtlastname
                                        + "&email=" + txtemail.toLowerCase()
                                        + "&url_image=" + imageurl
                                        + "&mode=FACEBOOK";
                                StringRequest postRequest1 = new StringRequest(Request.Method.POST, url1,
                                        response11 -> {
                                            // response
                                            prefManager.setUser(new User(txtemail,txtname,txtlastname,"",imageurl,"FACEBOOK"));
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        },
                                        error -> {
                                            // error
                                            Toast.makeText(LoginActivity.this, "Connection error", Toast.LENGTH_LONG).show();
                                        }
                                );
                                queue.add(postRequest1);
                            }else{
                                String url1 = "http://10.0.2.2:1225/UpdateUser?first_name=" + txtname
                                        + "&last_name=" + txtlastname
                                        + "&email=" + txtemail
                                        + "&url_image=" + imageurl
                                        + "&mode=FACEBOOK";
                                StringRequest postRequest1 = new StringRequest(Request.Method.POST, url1,
                                        response112 -> {
                                            // response
                                            prefManager.setUser(new User(txtemail,txtname,txtlastname,"",imageurl,"FACEBOOK"));
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        },
                                        error -> {
                                            // error
                                            Toast.makeText(LoginActivity.this, "Connection error", Toast.LENGTH_LONG).show();
                                        }
                                );
                                queue.add(postRequest1);
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                        },
                        error -> {
                            // error
                            Toast.makeText(LoginActivity.this, "Connection error", Toast.LENGTH_LONG).show();
                        }
                );
                queue.add(postRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void checkLoginStatus() {
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
