package com.example.omnia.easytripplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.omnia.easytripplanner.database.Converter;
import com.example.omnia.easytripplanner.database.dao.daoimpl.ProfileDAOImpl;
import com.example.omnia.easytripplanner.database.dto.ProfileDTO;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Log_in extends AppCompatActivity {

    private static final String TAG = "Log_in";
    EditText emailEdit;
    EditText passwordEdit;
    LoginButton loginButton;
    CallbackManager callbackManager;
    ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailEdit = (EditText) findViewById(R.id.email_login);
        passwordEdit = (EditText) findViewById(R.id.password_login);
        //facebook api
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("omnia", "onsucesse");
                mdialog = new ProgressDialog(Log_in.this);
                mdialog.setMessage("retrieve data");
                mdialog.show();
                // get user profile data
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mdialog.dismiss();
                        getfacebookdata(object);
                    }
                });
                //graph api
                Bundle parameter = new Bundle();
                parameter.putString("fields", "id,name,email");
                request.setParameters(parameter);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Log_in.this, " login canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // check for network access
        boolean states = isNetworkAvailable();
        if (states == true) {
            // internet is avilable
        } else {
            // no internet connection
            Toast.makeText(Log_in.this, "no  internet", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * method that save user data to sharedpreferences
     *
     * @param jsonObject jason object that contains user facebook profile information
     */
    public void getfacebookdata(JSONObject jsonObject) {
        try {
            String userName = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            // facebook cant give acess to user password so instead i will use id
            String id = jsonObject.getString("id");
            final ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setProfile_name(userName);
            profileDTO.setProfile_email(email);
            profileDTO.setProfile_password(id);

            // insert to database
            final ProfileDAOImpl profileDAO = new ProfileDAOImpl(Log_in.this);
            profileDAO.insertProfile(profileDTO);
            // get facebook image
            final URL url = new URL("https://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=normal");
            new AsyncTask<Void, Void, Void>() {
                Bitmap bitmap;

                @Override
                protected Void doInBackground(Void... params) {
                    Looper.prepare();
                    try {
                        bitmap = Glide.
                                with(Log_in.this).
                                load(url).
                                asBitmap().
                                into(-1, -1).
                                get();
                        profileDTO.setProfile_picture(Converter.convertBitmapToByte(bitmap));
                    } catch (final ExecutionException e) {
                        Log.e(TAG, e.getMessage());
                    } catch (final InterruptedException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    return null;
                }


            }.execute();


            SharedPreferencesManager shm = new SharedPreferencesManager(this);
            shm.saveUserDataToSharedPreferences(email, id, "facebook");
            Intent intent = new Intent(this, NavigatonDrawer.class);
            intent.putExtra("profileEmail", email);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LogIn(View view) {

        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        // check if user enter a correct data
        if (email.trim().length() == 0) {
            emailEdit.setError("Email is required");
        } else if (password.trim().length() == 0) {
            passwordEdit.setError("Password is required");
        } else {
            // check if user data is correct or not
            ProfileDAOImpl profileDAO = new ProfileDAOImpl(this);
            ProfileDTO profileDTO = profileDAO.getProfileByEmailAndPassword(email, password);
            if (profileDTO.getProfile_id() == null) {
                // the user enter wrong data show message error
                Toast.makeText(this, "you enter incorrect data", Toast.LENGTH_LONG).show();
            } else {//user exist go to home page
                SharedPreferencesManager shm = new SharedPreferencesManager(this);
                shm.saveUserDataToSharedPreferences(email, password, "applogin");
                Intent intent = new Intent(this, NavigatonDrawer.class);
                intent.putExtra("profileEmail", email);
                finish();
                startActivity(intent);
            }
        }
    }

    /**
     * @param view link to login activity
     */
    public void loginLink(View view) {
        Intent intent = new Intent(this, Sign_up.class);
        startActivity(intent);
        finish();
    }

    // check network states
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
