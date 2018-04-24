package com.example.omnia.easytripplanner;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.omnia.easytripplanner.database.dao.daoimpl.ProfileDAOImpl;
import com.example.omnia.easytripplanner.database.dao.daoimpl.TripDAOImpl;
import com.example.omnia.easytripplanner.database.dto.ProfileDTO;
import com.example.omnia.easytripplanner.database.dto.TripDTO;

import java.util.ArrayList;

public class AlertActivity extends AppCompatActivity {
    double lat1;
    double long1;
    double lat2;
    double long2;
    String notification = "";
    int notificationID;
    TripDAOImpl tripDAO;
    TripDTO tripDTO;
    String email;
    ProfileDTO profileDTO;
    ProfileDAOImpl profileDAO;
    /*  Permission request code to draw over other apps  */
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        AlertActivity.this.setFinishOnTouchOutside(false);
        tripDAO = new TripDAOImpl(AlertActivity.this);
        tripDTO = new TripDTO();
        profileDTO = new ProfileDTO();
        profileDAO = new ProfileDAOImpl(AlertActivity.this);
        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(AlertActivity.this);
        email = preferencesManager.getEmail();
        profileDTO = profileDAO.getProfileByEmail(email);

        notification = getIntent().getStringExtra("notification");
        notificationID = getIntent().getIntExtra("tripId", 0);
        long1 = getIntent().getDoubleExtra("long1", 0);
        lat1 = getIntent().getDoubleExtra("lat1", 0);
        long2 = getIntent().getDoubleExtra("long2", 0);
        lat2 = getIntent().getDoubleExtra("lat2", 0);
        Log.i("tttttttttttt", long1 + " " + long2 + " " + lat1 + " " + lat2);
        tripDTO = (TripDTO) tripDAO.getTripByName(notification, profileDTO.getProfile_id());

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AlertActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(AlertActivity.this);
        }
        builder.setTitle("Trip Planner")
                .setMessage("Do you want to start " + notification + " trip ?")
                .setPositiveButton("Start Trip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        tripDTO.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.DONE);
                        tripDAO.updateTrip(tripDTO);
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + lat1 + "," + long1 + "&daddr=" + lat2 + "," + long2));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Intent dismissIntent = new Intent(AlertActivity.this, RingtonePlayingService.class);
                        dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);
                        PendingIntent pendingIntent = PendingIntent.getService(AlertActivity.this, notificationID, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        startService(dismissIntent);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(AlertActivity.this)) {
                            //If the draw over permission is not available open the settings screen
                            //to grant the permission.
                            Intent widgetIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(widgetIntent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                        } else {
                            //If permission is granted start floating widget service
                            startFloatingWidgetService();
                        }

                        finish();

                    }
                })
                .setNeutralButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent1 = new Intent(AlertActivity.this, NavigatonDrawer.class);
                        PendingIntent pIntent = PendingIntent.getActivity(AlertActivity.this, notificationID, intent1,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            int notifyID = 1;
                            String CHANNEL_ID = "my_channel_01";// The id of the channel.
                            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                            Notification notification = new Notification.Builder(AlertActivity.this)
                                    .setContentTitle("TIME TO START YOUR TRIP")
                                    .setContentText("GO NOW")
                                    .setOngoing(true)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setChannelId(CHANNEL_ID)
                                    .build();
                            finish();
                        } else {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(AlertActivity.this)
                                    .setAutoCancel(true)
                                    .setNumber(notificationID)
                                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                                    .setContentTitle("TIME TO START " + notification)
                                    .setContentText("GO NOW")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setSubText("Tab to cancel the ringtone")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setOngoing(true)
                                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.FLAG_AUTO_CANCEL)
                                    .setContentIntent(pIntent);
                            if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(new long[0]);
                            NotificationManager notificationManager = (NotificationManager)
                                    AlertActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                            Notification notification = builder.build();
                            notification.flags |= Notification.FLAG_AUTO_CANCEL;
                            notificationManager.notify(notificationID, notification);
                        }

                        Intent dismissIntent = new Intent(AlertActivity.this, RingtonePlayingService.class);
                        dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);
                        PendingIntent pendingIntent = PendingIntent.getService(AlertActivity.this, notificationID, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        startService(dismissIntent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel Trip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tripDTO.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.CANCELLED);
                        tripDAO.updateTrip(tripDTO);
                        Intent dismissIntent = new Intent(AlertActivity.this, RingtonePlayingService.class);
                        dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);
                        PendingIntent pendingIntent = PendingIntent.getService(AlertActivity.this, notificationID, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        startService(dismissIntent);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();

    }


    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        Intent intent = new Intent(AlertActivity.this, FloatingWidgetService.class);
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
}