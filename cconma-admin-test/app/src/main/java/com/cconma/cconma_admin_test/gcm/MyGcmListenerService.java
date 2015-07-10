package com.cconma.cconma_admin_test.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cconma.cconma_admin_test.Activity.MainActivity;
import com.cconma.cconma_test_actionbar.R;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Young Bin Kim on 2015-07-09.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "debugging";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message, title);
    }

    private void sendNotification(String message, String title) {
        Log.d(TAG, "SEND NOTIFICATION");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_noti_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
    /*@Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String message = data.getString("message");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

        sendNotification(title, message);
    }

    private void sendNotification(String title, String message) {

    }*/
}

