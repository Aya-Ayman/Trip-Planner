package com.example.omnia.easytripplanner;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;

public class RingtonePlayingService extends Service {
    public RingtonePlayingService() {
    }

    private static final String TAG = RingtonePlayingService.class.getSimpleName();
    private static final String URI_BASE = RingtonePlayingService.class.getName() + ".";
    public static final String ACTION_DISMISS = URI_BASE + "ACTION_DISMISS";
    Timer t;
    static int numberOfRings;
    int tripId;
    String notification;
    private Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        numberOfRings++;
        notification = intent.getStringExtra("notification");
        tripId = intent.getIntExtra("tripId", 0);
        if (intent == null) {
            Log.d(TAG, "The intent is null.");
            return START_REDELIVER_INTENT;
        }

        String action = intent.getAction();

        if (ACTION_DISMISS.equals(action))
            dismissRingtone();
        else {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            ringtone = RingtoneManager.getRingtone(this, alarmUri);
            ringtone.play();
        }

        return START_NOT_STICKY;
    }

    public void dismissRingtone() {
        // stop the alarm rigntone
        Intent i = new Intent(this, RingtonePlayingService.class);
        stopService(i);

        // also dismiss the alarm to ring again or trigger again
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, tripId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        aManager.cancel(pendingIntent);

        // Canceling the current notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(notification, tripId);
    }

    @Override
    public void onDestroy() {
        ringtone.stop();
    }
}
