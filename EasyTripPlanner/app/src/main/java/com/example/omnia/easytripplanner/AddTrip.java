package com.example.omnia.easytripplanner;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.example.omnia.easytripplanner.database.dao.daoimpl.ProfileDAOImpl;
import com.example.omnia.easytripplanner.database.dao.daoimpl.TripDAOImpl;
import com.example.omnia.easytripplanner.database.dto.ProfileDTO;
import com.example.omnia.easytripplanner.database.dto.TripDTO;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTrip extends AppCompatActivity {


    Double long1 = 0.0d;
    Double lat1;
    Double long2;
    Double lat2;
    String TAG = " asdasdasdadsasdasd";
    EditText alarmDateBack;
    EditText alarmClockBack;
    LatLng latLang1;
    LatLng latLang2;
    EditText name;
    Button add;
    Button cancel;
    String placeName;
    String placeDestination;
    Calendar myCalendar = Calendar.getInstance();
    Calendar currentCalendar = Calendar.getInstance();
    Calendar myCalendarRound = Calendar.getInstance();
    EditText alarmDate;
    EditText alarmClock;
    AlarmManager alarmManager;
    AlarmManager alarmManagerRound;
    PendingIntent pendingIntent;
    PendingIntent pendingIntentRound;
    RadioButton single;
    RadioButton round;
    int years;
    int months;
    int days;
    public int hours = 2;
    public int minutes;
    int years2;
    int months2;
    int days2;
    public int hours2 = 2;
    public int minutes2;
    NavigatonDrawer m;
    List<String> tripNames = new ArrayList<String>();
    String email;
    ProfileDTO profileDTO;
    ProfileDAOImpl profileDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        alarmClock = findViewById(R.id.editClock);
        alarmDate = (EditText) findViewById(R.id.editDate);
        alarmClockBack = findViewById(R.id.clockEdit_back);
        alarmDateBack = findViewById(R.id.dateEdit_back);
        name = (EditText) findViewById(R.id.name);
        single = findViewById(R.id.single_trip);
        round = findViewById(R.id.round_trip);
        add = (Button) findViewById(R.id.btn_add);
        cancel = (Button) findViewById(R.id.btn_cancel);
        myCalendar.setTimeInMillis(System.currentTimeMillis());
        currentCalendar.setTimeInMillis(System.currentTimeMillis());


        profileDTO = new ProfileDTO();
        profileDAO = new ProfileDAOImpl(AddTrip.this);
        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(AddTrip.this);
        email = preferencesManager.getEmail();
        profileDTO = profileDAO.getProfileByEmail(email);
        Log.i(TAG, "profile_id   " + profileDTO.getProfile_id());
        getTripNames();
        // set my Calendar date
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                years = year;
                months = monthOfYear;
                days = dayOfMonth;
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        // when alarmDate editText is clicked
        alarmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTrip.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // click listener on alarmClock EditText
        alarmClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTrip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        alarmClock.setText(selectedHour + ":" + selectedMinute);
                        minutes = selectedMinute;
                        hours = selectedHour;
                        myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute - 1);
                        myCalendar.set(Calendar.SECOND, 59);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        ////////////////////////////////// round picker ///////////////////////////////////////
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                years2 = year;
                months2 = monthOfYear;
                days2 = dayOfMonth;
                myCalendarRound.set(Calendar.YEAR, year);
                myCalendarRound.set(Calendar.MONTH, monthOfYear);
                myCalendarRound.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelRound();
            }
        };

        // when alarmDate editText is clicked
        alarmDateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTrip.this, date1, currentCalendar
                        .get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH),
                        currentCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // click listener on alarmClock EditText
        alarmClockBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime2 = Calendar.getInstance();
                int hour = mcurrentTime2.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime2.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker2;
                mTimePicker2 = new TimePickerDialog(AddTrip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        alarmClockBack.setText(selectedHour + ":" + selectedMinute);
                        minutes2 = selectedMinute;
                        hours2 = selectedHour;

                        myCalendarRound.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendarRound.set(Calendar.MINUTE, selectedMinute - 1);
                        myCalendarRound.set(Calendar.SECOND, 59);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker2.setTitle("Select Time");
                mTimePicker2.show();
            }
        });

        final PlaceAutocompleteFragment autocompleteFragment1 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment1.getView().setBackgroundColor(Color.WHITE);
        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                latLang1 = place.getLatLng();
                placeName = (String) place.getName();
                long1 = latLang1.longitude;
                lat1 = latLang1.latitude;
            }

            @Override
            public void onError(Status status) {

            }
        });
        final PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);
        autocompleteFragment2.getView().setBackgroundColor(Color.WHITE);
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                latLang2 = place.getLatLng();
                placeDestination = (String) place.getName();
                long2 = latLang2.longitude;
                lat2 = latLang2.latitude;

            }

            @Override
            public void onError(Status status) {

            }
        });

        AutocompleteFilter typeFilter1 = new AutocompleteFilter.Builder()
                .setCountry("EG")
                .build();
        autocompleteFragment1.setFilter(typeFilter1);
        AutocompleteFilter typeFilter2 = new AutocompleteFilter.Builder()
                .setCountry("EG")
                .build();
        autocompleteFragment2.setFilter(typeFilter2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String trip_name = name.getText().toString();

                String trip_start_point = placeName;
                String trip_end_point = placeDestination;

                if (trip_name.matches("") || alarmClock.getText().toString().matches("") || alarmDate.getText().toString().matches("") || long1 == 0.0 || long2 == 0.0 || (!single.isChecked() && !round.isChecked())) {
                    Toast.makeText(AddTrip.this, "missing fields !", Toast.LENGTH_LONG).show();

                } else if (myCalendar.compareTo(currentCalendar) <= 0) {

                    Toast.makeText(AddTrip.this, "cannot insert passed time", Toast.LENGTH_SHORT).show();
                } else if (tripNames.contains(name.getText().toString())) {
                    Toast.makeText(AddTrip.this, "Duplicated trip name !", Toast.LENGTH_LONG).show();

                } else {
                    if (single.isChecked()) {
                        TripDTO t;

                        TripDAOImpl tripDAO = new TripDAOImpl(AddTrip.this);
                        TripDTO tripDTO = new TripDTO();
                        tripDTO.setTrip_name(trip_name);
                        tripDTO.setTrip_start_point(trip_start_point);
                        tripDTO.setTrip_end_point(trip_end_point);
                        tripDTO.setTrip_start_point_latitude(lat1);
                        tripDTO.setTrip_start_point_longitude(long1);
                        tripDTO.setTrip_end_point_latitude(lat2);
                        tripDTO.setTrip_end_point_longitude(long2);
                        tripDTO.setTrip_date(alarmDate.getText().toString());
                        tripDTO.setTrip_time(alarmClock.getText().toString());
                        tripDTO.setTrip_rounded(0);
                        tripDTO.setProfile_id(profileDTO.getProfile_id());
                        tripDTO.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.UPCOMING);
                        tripDTO.setTrip_millisecond((double) myCalendar.getTimeInMillis());

                        //insert trip
                        tripDAO.insertTrip(tripDTO);
                        t = tripDAO.getTripByName(trip_name, profileDTO.getProfile_id());
                        DebugDB.getAddressLog();
                        int i = m.trips.size();
                        //alarm logic
                        alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                        Intent alarmIntent = new Intent(AddTrip.this, AlarmReceiver.class);
                        alarmIntent.putExtra("notification", name.getText().toString());
                        alarmIntent.putExtra("tripId", t.getTrip_id());
                        alarmIntent.putExtra("long1", long1);
                        alarmIntent.putExtra("long2", long2);
                        alarmIntent.putExtra("lat1", lat1);
                        alarmIntent.putExtra("lat2", lat2);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTrip.this, t.getTrip_id(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP
                                , myCalendar.getTimeInMillis(), pendingIntent);
                        m.trips.add(t);
                        m.adapter.notifyDataSetChanged();
                        finish();
                    }
                    if (round.isChecked()) {
                        if (myCalendarRound.compareTo(myCalendar) <= 0) {
                            Toast.makeText(AddTrip.this, "cannot return before going", Toast.LENGTH_SHORT).show();
                        } else {
                            TripDTO t;
                            TripDAOImpl tripDAO = new TripDAOImpl(AddTrip.this);
                            TripDTO tripDTO = new TripDTO();
                            tripDTO.setTrip_name(trip_name);
                            tripDTO.setTrip_start_point(trip_start_point);
                            tripDTO.setTrip_end_point(trip_end_point);
                            tripDTO.setTrip_start_point_latitude(lat1);
                            tripDTO.setTrip_start_point_longitude(long1);
                            tripDTO.setTrip_end_point_latitude(lat2);
                            tripDTO.setTrip_end_point_longitude(long2);
                            tripDTO.setTrip_date(alarmDate.getText().toString());
                            tripDTO.setTrip_time(alarmClock.getText().toString());
                            tripDTO.setTrip_rounded(1);
                            tripDTO.setProfile_id(profileDTO.getProfile_id());
                            tripDTO.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.UPCOMING);
                            tripDTO.setTrip_millisecond((double) myCalendar.getTimeInMillis());
                            tripDAO.insertTrip(tripDTO);
                            //insert trip
                            t = tripDAO.getTripByName(trip_name, profileDTO.getProfile_id());
                            Log.i(TAG, "onClick: " + t.getTrip_id());
                            DebugDB.getAddressLog();
                            int i = m.trips.size();

                            TripDTO tRound;
                            TripDTO tripDTO2 = new TripDTO();
                            tripDTO2.setTrip_name(trip_name + " back");
                            tripDTO2.setTrip_start_point(trip_end_point);
                            tripDTO2.setTrip_end_point(trip_start_point);
                            tripDTO2.setTrip_start_point_latitude(lat2);
                            tripDTO2.setTrip_start_point_longitude(long2);
                            tripDTO2.setTrip_end_point_latitude(lat1);
                            tripDTO2.setTrip_end_point_longitude(long1);
                            tripDTO2.setTrip_date(alarmDateBack.getText().toString());
                            tripDTO2.setTrip_time(alarmClockBack.getText().toString());
                            tripDTO2.setTrip_rounded(0);
                            tripDTO2.setProfile_id(profileDTO.getProfile_id());
                            tripDTO2.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.UPCOMING);
                            tripDTO2.setTrip_millisecond((double) myCalendarRound.getTimeInMillis());
                            tripDAO.insertTrip(tripDTO2);
                            tRound = tripDAO.getTripByName(trip_name + " back", profileDTO.getProfile_id());

                            //alarm logic
                            alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                            Intent alarmIntent = new Intent(AddTrip.this, AlarmReceiver.class);
                            alarmIntent.putExtra("notification", name.getText().toString() + " going");
                            alarmIntent.putExtra("tripId", t.getTrip_id());
                            alarmIntent.putExtra("long1", long1);
                            alarmIntent.putExtra("long2", long2);
                            alarmIntent.putExtra("lat1", lat1);
                            alarmIntent.putExtra("lat2", lat2);
                            pendingIntent = PendingIntent.getBroadcast(AddTrip.this, t.getTrip_id(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.set(AlarmManager.RTC_WAKEUP
                                    , myCalendar.getTimeInMillis(), pendingIntent);
                            alarmManagerRound = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                            Intent alarmIntentRound = new Intent(AddTrip.this, AlarmReceiver.class);
                            alarmIntentRound.putExtra("notification", name.getText().toString() + " back");
                            alarmIntentRound.putExtra("tripId", tRound.getTrip_id());
                            alarmIntentRound.putExtra("long1", long2);
                            alarmIntentRound.putExtra("long2", long1);
                            alarmIntentRound.putExtra("lat1", lat2);
                            alarmIntentRound.putExtra("lat2", lat1);
                            pendingIntentRound = PendingIntent.getBroadcast(AddTrip.this, tRound.getTrip_id(), alarmIntentRound, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManagerRound.set(AlarmManager.RTC_WAKEUP
                                    , myCalendarRound.getTimeInMillis(), pendingIntentRound);

                            m.trips.add(t);
                            m.adapter.notifyDataSetChanged();
                            finish();
                        }
                    }

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddTrip.this, "round is cliked", Toast.LENGTH_LONG).show();
                alarmClockBack.setVisibility(View.VISIBLE);
                alarmDateBack.setVisibility(View.VISIBLE);
            }
        });

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddTrip.this, "single is clicked", Toast.LENGTH_LONG).show();
                alarmClockBack.setVisibility(View.GONE);
                alarmDateBack.setVisibility(View.GONE);
            }
        });
    }

    // update alarmDate Text
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        alarmDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelRound() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        alarmDateBack.setText(sdf.format(myCalendarRound.getTime()));
    }

    private void getTripNames() {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                TripDAOImpl tripDAO = new TripDAOImpl(AddTrip.this);
                tripNames = tripDAO.getAllTripsNames();

                return tripNames;
            }

            @Override
            protected void onPostExecute(List<String> list) {
                super.onPostExecute(list);


            }
        }.execute();
    }

}

