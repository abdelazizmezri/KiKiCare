package com.yaapps.kikicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
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
import com.google.android.material.textfield.TextInputEditText;
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

    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        //login_email
        bt_login = findViewById(R.id.cirLoginButton);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_login.startAnimation();
                try {
                    final String url = "http://10.0.2.2:1225/getUser?email=" + editTextEmail.getEditableText().toString();
                    HttpHandler sh = new HttpHandler();
                    String jsonStr = sh.makeServiceCall(url);
                    if (jsonStr != null) {
                        try {
                            JSONObject c = new JSONObject(jsonStr);
                            User user = new User(
                                    c.getInt("id"),
                                    c.getString("email"),
                                    c.getString("first_name"),
                                    c.getString("last_name"),
                                    c.getString("password"),
                                    c.getString("url_image"),
                                    c.getString("mode")
                            );
                            if(user.getPassword().equals(editTextPassword.getEditableText().toString())){
                                bt_login.revertAnimation();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }else{
                                bt_login.revertAnimation();
                                Toast.makeText(LoginActivity.this, "Mot de passe incorrecte", Toast.LENGTH_LONG).show();
                            }
                        } catch (final JSONException e) {
                            bt_login.revertAnimation();
                            Toast.makeText(LoginActivity.this, "Email invalid", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        bt_login.revertAnimation();
                        Toast.makeText(LoginActivity.this, "Couldn't get json from server.", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ex){
                    bt_login.revertAnimation();
                    Toast.makeText(LoginActivity.this, "error : " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //new ServiceUser(this).addUser(new User("abdelaziz.mezri@esprit.tn","abdelaziz","mezri","Azerty-94","image","email"));
        //Log.println(Log.INFO,"", (new ServiceUser(this).getUser("email")).toString());

        callbackManager = CallbackManager.Factory.create();

        checkLoginStatus();

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
                signInGoogle();
            }
        });

        //facebook ImageView
        fb_ImageView = findViewById(R.id.fb_ImageView);
        fb_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            // Signed in successfully, show authenticated UI.

            txtemail = account.getEmail();
            txtname = account.getGivenName();
            txtlastname =  account.getFamilyName();
            imageurl = account.getPhotoUrl().toString();
            Log.println(Log.INFO,"user",txtemail + " " + txtname + " " + txtlastname + " " + imageurl);
        } catch (ApiException e) {

        }
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
            if(currentAccessToken==null)
            {
                txtname = "";
                txtlastname = "";
                txtemail = "";
                imageurl = "";
                Toast.makeText(LoginActivity.this,"User Logged out",Toast.LENGTH_LONG).show();
            }
            else
                loadUserProfile(currentAccessToken);
        }
    };

    private void loadUserProfile(AccessToken newAccessToken)
    {
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

                    Log.println(Log.INFO,"user",txtemail + " " + txtname + " " + txtlastname);

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

    private void checkLoginStatus()
    {
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
}
