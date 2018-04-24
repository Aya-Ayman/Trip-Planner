package com.example.omnia.easytripplanner;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.example.omnia.easytripplanner.database.dao.daoimpl.NoteDAOImpl;
import com.example.omnia.easytripplanner.database.dao.daoimpl.ProfileDAOImpl;
import com.example.omnia.easytripplanner.database.dao.daoimpl.TripDAOImpl;
import com.example.omnia.easytripplanner.database.dto.NoteDTO;
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

public class DetailsActivity extends AppCompatActivity {
    /*  Permission request code to draw over other apps  */
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    Double long1 = 0.0d;
    Double lat1;
    Double long2 = 0.0d;
    Double lat2;
    Button Cancel;
    TextView status;
    ListView list;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    ArrayList<NoteDTO> noteDTOS;
    EditText name;
    Button Edit;
    Button Delete;
    Button Start;
    String TAG = " asdasdasdadsasdasd";
    Double currentLong;
    Double currentLat;
    LatLng latLang1;
    LatLng latLang2;
    public String placeName2;
    public String placeDestination2;
    Calendar myCalendar = Calendar.getInstance();
    Integer type;
    TextView trip_type;
    Calendar currentCalendar = Calendar.getInstance();
    EditText alarmDate;
    EditText alarmClock;
    AlarmManager alarmManager;
    AlarmManager alarmManagerRound;
    PendingIntent pendingIntent;
    PendingIntent pendingIntentRound;
    LocationManager mgr;
    public int years;
    public int months;
    public int days;
    public int hours = 2;
    public int minutes;
    String lastDate;
    long milliSecond;
    NavigatonDrawer m;
    TripDTO ex;
    int clockFlag = 0;
    int nullplacetestflage = 0;
    String email;
    ProfileDTO profileDTO;
    ProfileDAOImpl profileDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        alarmClock = findViewById(R.id.clockEdit);
        alarmDate = (EditText) findViewById(R.id.dateEdit);
        Start = findViewById(R.id.Start);
        Edit = findViewById(R.id.btn_edit);
        Delete = findViewById(R.id.btn_delete);
        Cancel = findViewById(R.id.Cancel);
        name = findViewById(R.id.details_name);
        status = findViewById(R.id.details_status);
        trip_type = findViewById(R.id.trip_type);
        ex = (TripDTO) getIntent().getSerializableExtra("trip");
        lastDate = "" + ex.getTrip_millisecond();
        milliSecond = (new Double(ex.getTrip_millisecond())).longValue();
        myCalendar.setTimeInMillis(System.currentTimeMillis());
        currentCalendar.setTimeInMillis(System.currentTimeMillis());
        profileDTO = new ProfileDTO();
        profileDAO = new ProfileDAOImpl(DetailsActivity.this);
        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(DetailsActivity.this);
        email = preferencesManager.getEmail();
        profileDTO = profileDAO.getProfileByEmail(email);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //    int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, (float) 0.0f, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLong = location.getLongitude();
                currentLat = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

        //notes
        NoteDAOImpl noteDAO = new NoteDAOImpl(DetailsActivity.this);
        //static id must be replaced
        noteDTOS = (ArrayList<NoteDTO>) noteDAO.getAllTripNotes(ex.getTrip_id());
        list = (ListView) findViewById(R.id.listview);
        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.item, arrayList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dialogUpdate(arrayList.get(position), position);
            }
        });

        for (NoteDTO noteDTO : noteDTOS) {
            arrayList.add(noteDTO.getNote_description());
            Log.i(TAG, "Note " + String.valueOf(noteDTO.getNote_id()));
        }

        // set my Calendar date
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                years = year;
                months = monthOfYear;
                days = dayOfMonth;
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                milliSecond = myCalendar.getTimeInMillis();
            }
        };

        // when alarmDate editText is clicked
        alarmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DetailsActivity.this, date, currentCalendar
                        .get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH),
                        currentCalendar.get(Calendar.DAY_OF_MONTH)).show();
                if (clockFlag == 0) {
                    alarmClock.setText("");
                    clockFlag = 1;
                }
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
                mTimePicker = new TimePickerDialog(DetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        alarmClock.setText(selectedHour + ":" + selectedMinute);
                        minutes = selectedMinute;
                        hours = selectedHour;
                        myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute - 1);
                        myCalendar.set(Calendar.SECOND, 59);
                        milliSecond = myCalendar.getTimeInMillis();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                if (clockFlag == 0) {
                    alarmDate.setText("");
                    clockFlag = 1;
                }
            }
        });

        /////////////////////////// auto complete  ////////////////////////////////////
        final PlaceAutocompleteFragment autocompleteFragment1 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment1.getView().setBackgroundColor(Color.WHITE);
        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                latLang1 = place.getLatLng();
                placeName2 = (String) place.getName();
                long1 = currentLong;
                if (long1 == null || long1 == 0.0)
                    long1 = latLang1.longitude;
                lat1 = currentLat;
                if (lat1 == null)
                    lat1 = latLang1.latitude;
                nullplacetestflage = 1;
                if (placeName2 == null) {

                    Toast.makeText(DetailsActivity.this, "null place", Toast.LENGTH_LONG).show();
                }
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
                latLang2 = place.getLatLng();
                placeDestination2 = (String) place.getName();
                long2 = latLang2.longitude;
                lat2 = latLang2.latitude;

                if (placeDestination2 == null) {

                    Toast.makeText(DetailsActivity.this, "null place", Toast.LENGTH_LONG).show();
                }
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


        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (long1 == 0.0 || long1 == null)
                    long1 = ex.getTrip_start_point_longitude();
                if (lat1 == null)
                    lat1 = ex.getTrip_start_point_latitude();
                if (long2 == 0.0 || long2 == null)
                    long2 = ex.getTrip_end_point_longitude();
                if (lat2 == null)
                    lat2 = ex.getTrip_end_point_latitude();

                alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                Intent myIntent = new Intent(DetailsActivity.this,
                        AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        DetailsActivity.this, ex.getTrip_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();

                ///////// set status to done //////////
                TripDAOImpl tripDAO = new TripDAOImpl(DetailsActivity.this);
                ex.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.DONE);
                tripDAO.updateTrip(ex);


                Log.i(TAG, long1 + " " + lat1 + " " + long2 + " " + lat2);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + lat1 + "," + long1 + "&daddr=" + lat2 + "," + long2));
                startActivity(intent);

                //Check if the application has draw over other apps permission or not?
                //This permission is by default available for API<23. But for API > 23
                //you have to ask for the permission in runtime.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(DetailsActivity.this)) {
                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent widgetIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(widgetIntent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                } else {
                    //If permission is granted start floating widget service
                    startFloatingWidgetService();
                }

            }
        });

        type = ex.getTrip_rounded();
        if (type == 1) {
            trip_type.setText("Round");
        } else {
            trip_type.setText("Single");
        }
        name.setText(ex.getTrip_name());
        autocompleteFragment2.setText(ex.getTrip_end_point());
        autocompleteFragment1.setText(ex.getTrip_start_point());
        alarmClock.setText(ex.getTrip_time());
        alarmDate.setText(ex.getTrip_date());
        status.setText(com.example.omnia.easytripplanner.database.dto.Status.UPCOMING);

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ss = Edit.getText().toString();
                if (ss.equals("Edit")) {
                    name.setEnabled(true);
                    Edit.setText("Save");
                } else {
                    if (long1 == 0.0 || long1 == null)
                        long1 = ex.getTrip_start_point_longitude();
                    if (lat1 == null)
                        lat1 = ex.getTrip_start_point_latitude();
                    if (long2 == 0.0 || long2 == null)
                        long2 = ex.getTrip_end_point_longitude();
                    if (lat2 == null)
                        lat2 = ex.getTrip_end_point_latitude();

                    if (name.getText().toString().trim().length() == 0 || alarmDate.getText().toString().trim().length() == 0 || alarmClock.getText().toString().trim().length() == 0)

                    {
                        Toast.makeText(DetailsActivity.this, "missing fields !", Toast.LENGTH_LONG).show();

                        if (placeName2 == null) {
                            ex.setTrip_start_point(ex.getTrip_start_point());

                        } else {
                            ex.setTrip_start_point(placeName2);
                        }

                        if (placeDestination2 == null) {
                            ex.setTrip_end_point(ex.getTrip_end_point());


                        } else {
                            ex.setTrip_end_point(placeDestination2);
                        }
                    } else

                    {
                        if (myCalendar.compareTo(currentCalendar) < 0) {
//                            "cannot insert passed time"
                            Toast.makeText(DetailsActivity.this, "Cannot insert passed time", Toast.LENGTH_SHORT).show();
                        } else {

                            TripDAOImpl tripDAO = new TripDAOImpl(DetailsActivity.this);
                            ex.setTrip_name(name.getText().toString());
                            ex.setTrip_date(alarmDate.getText().toString());
                            ex.setTrip_time(alarmClock.getText().toString());
                            ex.setTrip_rounded(0);
                            ex.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.UPCOMING);
                            tripDAO.updateTrip(ex);
                            m.adapter.notifyDataSetChanged();
                            DebugDB.getAddressLog();

                            //alarm logic
                            alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                            Intent myIntent = new Intent(DetailsActivity.this, AlarmReceiver.class);
                            pendingIntent = PendingIntent.getBroadcast(
                                    DetailsActivity.this, ex.getTrip_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                            pendingIntent.cancel();
                            Intent alarmIntent = new Intent(DetailsActivity.this, AlarmReceiver.class);
                            alarmIntent.putExtra("notification", name.getText().toString());
                            alarmIntent.putExtra("tripId", ex.getTrip_id());
                            alarmIntent.putExtra("long1", long1);
                            alarmIntent.putExtra("long2", long2);
                            alarmIntent.putExtra("lat1", lat1);
                            alarmIntent.putExtra("lat2", lat2);
                            PendingIntent pi = PendingIntent.getBroadcast(DetailsActivity.this, ex.getTrip_id(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, milliSecond, pi);

                            finish();

                        }
                    }
                }
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ex.getTrip_rounded() == 1) {

                    TripDAOImpl tripDAO = new TripDAOImpl(DetailsActivity.this);
                    TripDTO tripDTO2 = new TripDTO();
                    tripDTO2 = tripDAO.getTripByName(ex.getTrip_name() + " back", profileDTO.getProfile_id());
                    if (tripDTO2.getTrip_id() != null) {
                        Log.i(TAG, "onClick:  " + tripDTO2.getTrip_id());
                        tripDTO2.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.CANCELLED);
                        tripDAO.updateTrip(tripDTO2);
                        alarmManagerRound = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                        Intent myIntentRound = new Intent(DetailsActivity.this,
                                AlarmReceiver.class);
                        pendingIntentRound = PendingIntent.getBroadcast(
                                DetailsActivity.this, tripDTO2.getTrip_id(), myIntentRound, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManagerRound.cancel(pendingIntentRound);
                        pendingIntentRound.cancel();
                    }
                    ex.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.CANCELLED);
                    tripDAO.updateTrip(ex);
                    alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                    Intent myIntent = new Intent(DetailsActivity.this,
                            AlarmReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(
                            DetailsActivity.this, ex.getTrip_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();

                    finish();

                } else {
                    TripDAOImpl tripDAO = new TripDAOImpl(DetailsActivity.this);
                    ex.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.CANCELLED);
                    ex.setTrip_rounded(0);
                    tripDAO.updateTrip(ex);
                    alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                    Intent myIntent = new Intent(DetailsActivity.this,
                            AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            DetailsActivity.this, ex.getTrip_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                    finish();
                }
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setTitle("Confirm delete");
                builder.setMessage("Are you sure you want to delete this trip");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ex.getTrip_rounded() == 1) {
                            TripDAOImpl tripDAO = new TripDAOImpl(DetailsActivity.this);
                            NoteDAOImpl noteDAO1 = new NoteDAOImpl(DetailsActivity.this);
                            TripDTO tripDTO2 = new TripDTO();
                            tripDTO2 = tripDAO.getTripByName(ex.getTrip_name() + " back", profileDTO.getProfile_id());
                            if (tripDTO2.getTrip_id() != null) {
                                noteDAO1.deleteAllNote(tripDTO2.getTrip_id());
                                tripDAO.deleteTrip(tripDTO2.getTrip_id());
                                alarmManagerRound = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                                Intent myIntentRound = new Intent(DetailsActivity.this,
                                        AlarmReceiver.class);
                                pendingIntentRound = PendingIntent.getBroadcast(
                                        DetailsActivity.this, tripDTO2.getTrip_id(), myIntentRound, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManagerRound.cancel(pendingIntentRound);
                                pendingIntentRound.cancel();
                            }
                            tripDAO.deleteTrip(ex.getTrip_id());
                            noteDAO1.deleteAllNote(ex.getTrip_id());
                            alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                            Intent myIntent = new Intent(DetailsActivity.this,
                                    AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    DetailsActivity.this, ex.getTrip_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                            pendingIntent.cancel();
                            finish();
                        } else {
                            TripDAOImpl tripDAO = new TripDAOImpl(DetailsActivity.this);
                            NoteDAOImpl noteDAO1 = new NoteDAOImpl(DetailsActivity.this);
                            noteDAO1.deleteAllNote(ex.getTrip_id());
                            tripDAO.deleteTrip(ex.getTrip_id());
                            alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                            Intent myIntent = new Intent(DetailsActivity.this,
                                    AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    DetailsActivity.this, ex.getTrip_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                            pendingIntent.cancel();
                            finish();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    // update alarmDate Text
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        alarmDate.setText(sdf.format(myCalendar.getTime()));
    }

    // notes methods

    public void addNote(View view) {
        dialogCreate();
    }

    /**
     * method that show dialog when user create new note
     */
    public void dialogCreate() {

        final View view1 = getLayoutInflater().inflate(R.layout.notes_dialog, null);
        final EditText descedit = (EditText) view1.findViewById(R.id.noteDescription);
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        builder.setView(view1)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = descedit.getText().toString();
                if (description.trim().length() != 0) {
                    // insert new note
                    NoteDTO noteDTO = new NoteDTO();
                    noteDTO.setNote_id(noteDTOS.size() + 1);
                    noteDTO.setNote_description(description);
                    noteDTO.setNote_trip_id(ex.getTrip_id());
                    NoteDAOImpl noteDAO = new NoteDAOImpl(DetailsActivity.this);
                    noteDTOS.add(noteDTO);
                    boolean testInsert = noteDAO.insertNote(noteDTO);
                    addToList(description);
                    alert.dismiss();
                } else {
                    descedit.setError("Please enter your notes");
                }
            }
        });
    }

    /**
     * method show dialog when user need to update note data and have delete button
     *
     * @param text     note that user choose
     * @param position of note in listview
     */
    public void dialogUpdate(String text, final int position) {

        final View view1 = getLayoutInflater().inflate(R.layout.notes_dialog, null);
        final EditText descedit = (EditText) view1.findViewById(R.id.noteDescription);
        descedit.setText(text);
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        builder.setView(view1)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = descedit.getText().toString();
                if (!description.trim().isEmpty()) {
                    // update note
                    NoteDAOImpl noteDAO = new NoteDAOImpl(DetailsActivity.this);
                    NoteDTO noteDTO = noteDTOS.get(position);

                    noteDTO.setNote_description(description);
                    Boolean r = noteDAO.updateNote(noteDTO);
                    noteDTOS.set(position, noteDTO);
                    arrayList.set(position, description);
                    adapter.notifyDataSetChanged();
                    alert.dismiss();
                } else {
                    descedit.setError("please enter your note");
                }
            }
        });
        alert.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // delete object and row form database
                String description = descedit.getText().toString();
                if (!description.trim().isEmpty()) {
                    NoteDAOImpl noteDAO = new NoteDAOImpl(DetailsActivity.this);
                    NoteDTO noteDTO = noteDTOS.get(position);
                    noteDAO.deleteNote(noteDTO.getNote_id(), noteDTO.getNote_trip_id());
                    noteDTOS.remove(position);
                    arrayList.remove(position);
                    adapter.notifyDataSetChanged();
                    alert.dismiss();
                } else {
                    descedit.setError("enter data");
                }
            }
        });
    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        NoteDAOImpl noteDAO = new NoteDAOImpl(DetailsActivity.this);
        Intent intent = new Intent(DetailsActivity.this, FloatingWidgetService.class);
        ArrayList<NoteDTO> noteList;
        ArrayList<String> noteListDesc = new ArrayList<>();
        noteList = (ArrayList<NoteDTO>) noteDAO.getAllTripNotes(ex.getTrip_id());
        for (NoteDTO desc : noteList) {
            String s = desc.getNote_description();
            noteListDesc.add(s);
        }
        intent.putExtra("noteList", noteListDesc);
        startService(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
                startFloatingWidgetService();
            else
                //Permission is not available then display toast
                Toast.makeText(this,
                        getResources().getString(R.string.draw_other_app_permission_denied),
                        Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addToList(String desc) {
        //list
        arrayList.add(desc);
        adapter.notifyDataSetChanged();
    }
}