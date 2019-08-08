package io.github.cjybyjk.systemupdater.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import io.github.cjybyjk.systemupdater.MainActivity;
import io.github.cjybyjk.systemupdater.R;

public class NotificationUtils {

    public static void createNotificationChannel(Context context) {
        Resources tResources = context.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, "update_found", tResources.getString(R.string.notification_channel_updates_found), NotificationManager.IMPORTANCE_DEFAULT);
            createNotificationChannel(context, "update_download", tResources.getString(R.string.notification_channel_updates_downloading), NotificationManager.IMPORTANCE_LOW);
            createNotificationChannel(context, "update_install", tResources.getString(R.string.notification_channel_updates_install), NotificationManager.IMPORTANCE_DEFAULT);
        }
    }

    @RequiresApi(api = 26)
    private static void createNotificationChannel(Context context,String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public static void showNotification(Context context, String channelId, int id, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = new Notification.Builder(context, channelId);
        } else {
            mBuilder = new Notification.Builder(context);
        }
        Intent tIntent = new Intent(context, MainActivity.class);
        PendingIntent tPendingIntent = PendingIntent.getActivity(context, 0, tIntent, 0);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setTicker(title)
                .setContentIntent(tPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setAutoCancel(true);
        notificationManager.notify(id, mBuilder.build());
    }

    public static void destroyNotification(Context context,int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

    public static void showProgressNotification(Context context, String channelId, int id, String title, String message,int max, int progress, boolean indeterminate) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, buildProgressNotification(context,channelId,title,message,max,progress,indeterminate));
    }

    public static Notification buildProgressNotification(Context context, String channelId, String title, String message, int max, int progress, boolean indeterminate) {
        Notification.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = new Notification.Builder(context, channelId);
        } else {
            mBuilder = new Notification.Builder(context);
        }
        Intent tIntent = new Intent(context, MainActivity.class);
        PendingIntent tPendingIntent = PendingIntent.getActivity(context, 0, tIntent, 0);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setContentIntent(tPendingIntent)
                .setTicker(title)
                .setProgress(max, progress, indeterminate)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setAutoCancel(false);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT + Notification.FLAG_ONLY_ALERT_ONCE;
        return notification;
    }
}
