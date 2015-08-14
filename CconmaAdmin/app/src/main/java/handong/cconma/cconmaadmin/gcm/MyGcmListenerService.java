package handong.cconma.cconmaadmin.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import handong.cconma.cconmaadmin.board.BoardViewActivity;
import handong.cconma.cconmaadmin.data.IntegratedSharedPreferences;
import handong.cconma.cconmaadmin.mainpage.MainActivity;
import handong.cconma.cconmaadmin.R;
import handong.cconma.cconmaadmin.mainpage.StartPage;

/**
 * Created by Young Bin Kim on 2015-07-09.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "debugging";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");
        boolean pushSetting;

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        IntegratedSharedPreferences pref = new IntegratedSharedPreferences(getApplicationContext());
        pushSetting = pref.getValue("PUSH", true);
        if (pushSetting) {
            sendNotification(message, title);
        }
    }

    private void sendNotification(String message, String title) {

        String[] messageSplit = message.split("\n\n");

        int point = messageSplit[2].indexOf('&');
        String board_info  = messageSplit[2].substring(point + 1);
        String[] boardSplit = board_info.split("&");

        String board_no = boardSplit[0].split("=")[1];
        String boardarticle_no = boardSplit[1].split("=")[1];

        Intent intent = new Intent(this, StartPage.class);
        intent.putExtra("board_no", board_no);
        intent.putExtra("boardarticle_no", boardarticle_no);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}

