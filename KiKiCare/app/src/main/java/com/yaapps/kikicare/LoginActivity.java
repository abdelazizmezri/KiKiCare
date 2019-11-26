package com.yaapps.kikicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.yaapps.kikicare.UI.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    final int RC_SIGN_IN = 0;

    CallbackManager callbackManager;

    GoogleSignInClient mGoogleSignInClient;

    ImageView fb_ImageView;
    ImageView google_ImageView;

    CircularProgressButton bt_login;

    String txtname;
    String txtlastname;
    String txtemail;
    String imageurl;

    boolean textInputEmailControle, textInputPasswordControle;


    RequestQueue queue;

    TextInputLayout textInputEmail;
    TextInputLayout textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);

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

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //login_email
        bt_login = findViewById(R.id.cirLoginButton);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_login.startAnimation();
                if(textInputEmailControle && textInputPasswordControle){
                    queue = Volley.newRequestQueue(LoginActivity.this);
                    final String url = "http://192.168.137.1:1225/getUser?email=" + textInputEmail.getEditText().getText();
                    StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    if (!response.isEmpty()) {
                                        try {
                                            JSONObject c = new JSONObject(response);
                                            User user = new User(
                                                    c.getInt("id"),
                                                    c.getString("email"),
                                                    c.getString("first_name"),
                                                    c.getString("last_name"),
                                                    c.getString("password"),
                                                    c.getString("url_image"),
                                                    c.getString("mode")
                                            );
                                            if(user.getPassword().contentEquals(textInputPassword.getEditText().getText())){
                                                bt_login.revertAnimation();
                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                            }else{
                                                bt_login.revertAnimation();
                                                bt_login.setError("");
                                                Toast.makeText(LoginActivity.this, "Mot de passe incorrecte", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (final JSONException e) {
                                            bt_login.revertAnimation();
                                            bt_login.setError("");
                                            Toast.makeText(LoginActivity.this, "Email invalid", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        bt_login.revertAnimation();
                                        bt_login.setError("");
                                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    bt_login.revertAnimation();
                                    bt_login.setError("");
                                    Toast.makeText(LoginActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                    queue.add(postRequest);
                }else{
                    bt_login.revertAnimation();
                    bt_login.setError("");
                    Toast.makeText(LoginActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                }
            }
        });

        callbackManager = CallbackManager.Factory.create();

        //checkLoginStatus();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // google ImageView
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_ImageView = findViewById(R.id.google_ImageView);
        google_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected())
                    signInGoogle();
            }
        });

        //facebook ImageView
        fb_ImageView = findViewById(R.id.fb_ImageView);
        fb_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
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
            txtemail = account.getEmail();
            txtname = account.getGivenName();
            txtlastname =  account.getFamilyName();
            imageurl = account.getPhotoUrl().toString();
            queue = Volley.newRequestQueue(LoginActivity.this);
            final String url = "http://192.168.137.1:1225/getUser?email=" + txtemail;
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            if(response.isEmpty()){
                                String url = "http://192.168.137.1:1225/AddUser?first_name=" + txtname
                                        + "&last_name=" + txtlastname
                                        + "&email=" + txtemail
                                        + "&url_image=" + imageurl
                                        + "&mode=GMAIL";
                                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>()
                                        {
                                            @Override
                                            public void onResponse(String response) {
                                                // response
                                                Toast.makeText(LoginActivity.this, txtemail + " est ajouté avec succés", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                            }
                                        },
                                        new Response.ErrorListener()
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // error
                                                Toast.makeText(LoginActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                );
                                queue.add(postRequest);
                            }else{
                                String url = "http://192.168.137.1:1225/UpdateUser?first_name=" + txtname
                                        + "&last_name=" + txtlastname
                                        + "&email=" + txtemail
                                        + "&url_image=" + imageurl
                                        + "&mode=GMAIL";
                                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>()
                                        {
                                            @Override
                                            public void onResponse(String response) {
                                                // response
                                                Toast.makeText(LoginActivity.this, txtemail + " est ajouté avec succés", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                            }
                                        },
                                        new Response.ErrorListener()
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // error
                                                Toast.makeText(LoginActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                );
                                queue.add(postRequest);
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast.makeText(LoginActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                        }
                    }
            );
            queue.add(postRequest);
            Log.println(Log.INFO,"user",txtemail + " " + txtname + " " + txtlastname + " " + imageurl);
        } catch (ApiException e) {

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
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
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

                    //Glide.with(LoginActivity.this).load(image_url).into(circleImageView);

                    queue = Volley.newRequestQueue(LoginActivity.this);
                    final String url = "http://192.168.137.1:1225/getUser?email=" + txtemail;
                    StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    if(response.isEmpty()){
                                        String url = "http://192.168.137.1:1225/AddUser?first_name=" + txtname
                                                + "&last_name=" + txtlastname
                                                + "&email=" + txtemail
                                                + "&url_image=" + imageurl
                                                + "&mode=GMAIL";
                                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>()
                                                {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        // response
                                                        Toast.makeText(LoginActivity.this, txtemail + " est ajouté avec succés", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                    }
                                                },
                                                new Response.ErrorListener()
                                                {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        // error
                                                        Toast.makeText(LoginActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                        );
                                        queue.add(postRequest);
                                    }else{
                                        String url = "http://192.168.137.1:1225/UpdateUser?first_name=" + txtname
                                                + "&last_name=" + txtlastname
                                                + "&email=" + txtemail
                                                + "&url_image=" + imageurl
                                                + "&mode=FACEBOOK";
                                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>()
                                                {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        // response
                                                        Toast.makeText(LoginActivity.this, txtemail + " est ajouté avec succés", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                    }
                                                },
                                                new Response.ErrorListener()
                                                {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        // error
                                                        Toast.makeText(LoginActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                        );
                                        queue.add(postRequest);
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    }
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Toast.makeText(LoginActivity.this, "Problème de connexion", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                    queue.add(postRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOutGoogle(){
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //signout avec success
            }
        });
    }

    private boolean isConnected() {
        return new InternetDialog(this).getInternetStatus();
    }
}
