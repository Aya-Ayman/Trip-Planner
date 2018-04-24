package com.example.omnia.easytripplanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.easytripplanner.database.Converter;
import com.example.omnia.easytripplanner.database.dao.daoimpl.ProfileDAOImpl;
import com.example.omnia.easytripplanner.database.dao.daoimpl.TripDAOImpl;
import com.example.omnia.easytripplanner.database.dto.ProfileDTO;
import com.example.omnia.easytripplanner.database.dto.TripDTO;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

public class NavigatonDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;

    ProfileDTO profileDTO;
    ImageView profilePic;
    TextView userName;
    TextView userEmail;
    String email;
    View header;
    private FloatingActionButton fab;
    public static MainAdapter adapter;
    public static List<TripDTO> trips = new ArrayList<TripDTO>();
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigaton_drawer);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(NavigatonDrawer.this)) {
                    Intent widgetIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(widgetIntent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                } else {
                    //If permission is granted start floating widget service
                }
                if (ContextCompat.checkSelfPermission(NavigatonDrawer.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NavigatonDrawer.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Intent i = new Intent(view.getContext(), AddTrip.class);
                        view.getContext().startActivity(i);

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(NavigatonDrawer.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1993);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.

                    }
                }
                // get current location
                if (ActivityCompat.checkSelfPermission(NavigatonDrawer.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NavigatonDrawer.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NavigatonDrawer.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1993);
                    return;
                }
                Intent i = new Intent(view.getContext(), AddTrip.class);
                view.getContext().startActivity(i);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(NavigatonDrawer.this);
        email = preferencesManager.getEmail();
        ProfileDAOImpl profileDAO = new ProfileDAOImpl(NavigatonDrawer.this);
        profileDTO = profileDAO.getProfileByEmail(email);
        getTrips();

        profilePic = (ImageView) header.findViewById(R.id.profilePic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(NavigatonDrawer.this, ProfileActivity.class);
                profileIntent.putExtra("profileEmail", email);
                startActivity(profileIntent);
            }
        });
        if (profileDTO.getProfile_picture() != null)
            profilePic.setImageBitmap(Converter.convertByteToBitmap(profileDTO.getProfile_picture()));
        userName = header.findViewById(R.id.userName);
        userName.setText(profileDTO.getProfile_name());
        userEmail = header.findViewById(R.id.userEmail);
        userEmail.setText(profileDTO.getProfile_email());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigaton_drawer, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(NavigatonDrawer.this, ProfileActivity.class);
            profileIntent.putExtra("profileEmail", email);
            startActivity(profileIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent;
            historyIntent = new Intent(NavigatonDrawer.this, History_Main.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent;
            aboutIntent = new Intent(NavigatonDrawer.this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.nav_share) {
            SharedPreferencesManager spm = new SharedPreferencesManager(NavigatonDrawer.this);
            String loginType = spm.getLoginType();
            if (loginType.equals("facebook")) {
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);
                Profile.setCurrentProfile(null);
            }
            spm.deleteSharedPreferenceData();
            Intent loginIntent = new Intent(NavigatonDrawer.this, Log_in.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginIntent.putExtra("EXIT", true);
            startActivity(loginIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        Intent intent = new Intent(NavigatonDrawer.this, FloatingWidgetService.class);
        ArrayList<String> noteList = new ArrayList<>();
        noteList.add("Note1");
        noteList.add("Note2");
        noteList.add("Note3");
        noteList.add("Note4");
        noteList.add("Note5");
        noteList.add("Note6");
        intent.putExtra("noteList", noteList);
        startService(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
            }
            //If permission granted start floating widget service

            else
                //Permission is not available then display toast
                Toast.makeText(this,
                        getResources().getString(R.string.draw_other_app_permission_denied),
                        Toast.LENGTH_SHORT).show();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getTrips() {
        new AsyncTask<Void, Void, List<TripDTO>>() {
            @Override
            protected List<TripDTO> doInBackground(Void... params) {
                TripDAOImpl tripDAO = new TripDAOImpl(NavigatonDrawer.this);
                trips = tripDAO.getAllUpcomingTrips(profileDTO.getProfile_id());

                return trips;
            }

            @Override
            protected void onPostExecute(List<TripDTO> list) {
                super.onPostExecute(list);

                adapter = new MainAdapter(NavigatonDrawer.this, trips);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
