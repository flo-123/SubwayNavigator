package helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;

import hci.glasgow.subwaynavigator.MyApp;
import hci.glasgow.subwaynavigator.NavigatorActivity;
import hci.glasgow.subwaynavigator.R;

/**
 * Created by Flo on 31/10/15.
 */
public abstract class NotificationManager {

    public static int notificationID = 0;

    public static void createNotification(Context context, String title, String text) {

        if(context == null) {
            context = MyApp.getContext();
        }

        Intent notificationIntent = new Intent(context, NavigatorActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                notificationIntent,0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setContentTitle(title)
                .setContentText(text).setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(notificationID, mNotifyBuilder.build());
        notificationID++;
    }
}
