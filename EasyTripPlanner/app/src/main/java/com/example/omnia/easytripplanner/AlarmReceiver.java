package com.example.omnia.easytripplanner;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;
import com.example.omnia.easytripplanner.database.dto.Status;
import com.example.omnia.easytripplanner.database.dto.TripDTO;

public class AlarmReceiver extends BroadcastReceiver {
    double lat1;
    double long1;
    double lat2;
    double long2;
    String notification;
    int notificationID;
    TripDTO tripDTO;

    @Override
    public void onReceive(Context context, Intent intent) {
        // using service class
        notification = intent.getStringExtra("notification");
        notificationID = intent.getIntExtra("tripId", 0);
        long1 = intent.getDoubleExtra("long1", 0);
        lat1 = intent.getDoubleExtra("lat1", 0);
        long2 = intent.getDoubleExtra("long2", 0);
        lat2 = intent.getDoubleExtra("lat2", 0);
        Intent i = new Intent(context, RingtonePlayingService.class);
        i.putExtra("tripId", notificationID);
        i.putExtra("notification", notification);
        i.putExtra("long1", long1);
        i.putExtra("long2", long2);
        i.putExtra("lat1", lat1);
        i.putExtra("lat2", lat2);
        context.startService(i);
        Intent alertIntent = new Intent();
        alertIntent.putExtra("tripId", notificationID);
        alertIntent.putExtra("notification", notification);
        alertIntent.putExtra("long1", long1);
        alertIntent.putExtra("long2", long2);
        alertIntent.putExtra("lat1", lat1);
        alertIntent.putExtra("lat2", lat2);
        alertIntent.setClassName("com.example.omnia.easytripplanner", "com.example.omnia.easytripplanner.AlertActivity");
        alertIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alertIntent);
    }

    public void createNotification(final Context context) {

        // notification characteristics

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Trip Advisor")
                .setMessage("Do you want to start your trip now?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tripDTO.setTrip_status(Status.DONE);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + lat1 + "," + long1 + "&daddr=" + lat2 + "," + long2));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        Intent dismissIntent = new Intent(context, RingtonePlayingService.class);
                        dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);
                        PendingIntent pendingIntent = PendingIntent.getService(context, notificationID, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        context.startService(dismissIntent);

                    }
                })
                .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent1 = new Intent(context, MainActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(context, notificationID, intent1,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
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
                                context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = builder.build();
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(notificationID, notification);

                        Intent dismissIntent = new Intent(context, RingtonePlayingService.class);
                        dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);
                        PendingIntent pendingIntent = PendingIntent.getService(context, notificationID, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        context.startService(dismissIntent);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tripDTO.setTrip_status(Status.CANCELLED);
                        Intent dismissIntent = new Intent(context, RingtonePlayingService.class);
                        dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);
                        PendingIntent pendingIntent = PendingIntent.getService(context, notificationID, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        context.startService(dismissIntent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);


        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();

    }
}
