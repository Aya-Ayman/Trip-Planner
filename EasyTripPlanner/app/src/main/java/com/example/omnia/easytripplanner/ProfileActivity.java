package com.example.omnia.easytripplanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.omnia.easytripplanner.database.Converter;
import com.example.omnia.easytripplanner.database.dao.daoimpl.ProfileDAOImpl;
import com.example.omnia.easytripplanner.database.dto.ProfileDTO;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int REQUEST_CODE_PICTURE = 1;

    ImageView profile_picture;
    EditText profile_name;
    EditText profile_email;
    EditText profile_password;
    Button save_profile;

    //for profile email from intent
    String intent_profile_email;

    //for profile id
    int profile_id;

    //login type
    String loginType;

    //profile object to update
    ProfileDTO profileDTOToUpdate = new ProfileDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_picture = findViewById(R.id.profile_picture);
        profile_name = findViewById(R.id.profile_name);
        profile_email = findViewById(R.id.profile_email);
        profile_password = findViewById(R.id.profile_password);
        save_profile = findViewById(R.id.save_profile);

        //get the intent and get the profile email from it
        Intent intent = getIntent();

        intent_profile_email = intent.getStringExtra("profileEmail");

        ProfileDTO profileDTO;

        ProfileDAOImpl profileDAO = new ProfileDAOImpl(ProfileActivity.this);

        profileDTO = profileDAO.getProfileByEmail(intent_profile_email);

        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(ProfileActivity.this);

        loginType = preferencesManager.getLoginType();

        if (loginType.equals("facebook")) {
            profile_name.setEnabled(false);
            profile_email.setEnabled(false);
            profile_password.setEnabled(false);
            profile_picture.setEnabled(false);
        }
        //set all the edit text with the data from the profile object
        profile_name.setText(profileDTO.getProfile_name());
        profile_email.setText(profileDTO.getProfile_email());
        profile_password.setText(profileDTO.getProfile_password());
        if (profileDTO.getProfile_picture() != null)
            profile_picture.setImageBitmap(Converter.convertByteToBitmap(profileDTO.getProfile_picture()));

        //get the profile id
        profile_id = profileDTO.getProfile_id();

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileDTOToUpdate.setProfile_id(profile_id);
                profileDTOToUpdate.setProfile_name(profile_name.getText().toString());
                profileDTOToUpdate.setProfile_email(profile_email.getText().toString());
                profileDTOToUpdate.setProfile_password(profile_password.getText().toString());

                //update the profile
                ProfileDAOImpl profileDAO = new ProfileDAOImpl(ProfileActivity.this);

                boolean profile_updated = profileDAO.updateProfile(profileDTOToUpdate);

                if (profile_updated) {
                    Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Profile Don't Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String pickTitle = "Take or select a photo";
                Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
                startActivityForResult(chooserIntent, REQUEST_CODE_PICTURE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICTURE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profile_picture.setImageBitmap(bitmap);
                profileDTOToUpdate.setProfile_picture(Converter.convertBitmapToByte(bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
