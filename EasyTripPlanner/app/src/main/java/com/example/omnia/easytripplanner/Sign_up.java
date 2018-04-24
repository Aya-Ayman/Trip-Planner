package com.example.omnia.easytripplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omnia.easytripplanner.database.dao.daoimpl.ProfileDAOImpl;
import com.example.omnia.easytripplanner.database.dto.ProfileDTO;

import java.util.regex.Pattern;

public class Sign_up extends AppCompatActivity {

    EditText usernameEdit;
    EditText emailEdit;
    EditText passwordEdit;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEdit = (EditText) findViewById(R.id.username_signup);
        emailEdit = (EditText) findViewById(R.id.email_signup);
        passwordEdit = (EditText) findViewById(R.id.password_signup);

    }

    /**
     * method invoked when user enter sign up button
     *
     * @param view
     */
    public void signUp(View view) {

        String userName = usernameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        // check if user enter a correct data
        if (email.trim().length() == 0) {
            emailEdit.setError("Email is required");
        } else if (userName.trim().length() == 0) {
            usernameEdit.setError("Username is required");
        } else if (email.trim().length() > 0 && !checkEmail(email)) {
            emailEdit.setError("Invalid email");
        } else if (password.trim().length() == 0) {
            passwordEdit.setError("password is required");
        } else {
            // check if user exist or not
            ProfileDAOImpl profileDAO = new ProfileDAOImpl(this);
            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setProfile_name(userName);
            profileDTO.setProfile_email(email);
            profileDTO.setProfile_password(password);

            if (!profileDAO.isEmailExist(profileDTO.getProfile_email())) {
                profileDAO.insertProfile(profileDTO);
                //save user data to sharedPreferences
                SharedPreferencesManager shm = new SharedPreferencesManager(this);
                shm.saveUserDataToSharedPreferences(email, password, "applogin");
                Intent intent = new Intent(this, NavigatonDrawer.class);
                intent.putExtra("profileEmail", email);
                finish();
                startActivity(intent);

            } else {
                Toast.makeText(this, "User Exist", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * @param email that user enter
     * @return true if email match the pattern else false
     */
    public static boolean checkEmail(String email) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + ")+");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

}
