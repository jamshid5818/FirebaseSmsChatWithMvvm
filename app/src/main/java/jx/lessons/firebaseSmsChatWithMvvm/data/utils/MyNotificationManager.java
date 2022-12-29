package jx.lessons.firebaseSmsChatWithMvvm.data.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import jx.lessons.firebaseSmsChatWithMvvm.R;

public class MyNotificationManager {
    private Context ctx;
    public static final int NOTIFICATION_ID = 123;
    public MyNotificationManager(Context ctx){
        this.ctx = ctx;
    }
    public void showNotification(String from, String notification, Intent intent){
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(
                    ctx,
                    NOTIFICATION_ID,
                    intent,
                    PendingIntent.FLAG_MUTABLE
            );
        }else {
            pendingIntent = PendingIntent.getActivity(
                    ctx,
                    NOTIFICATION_ID,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        Notification mNotification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(from)
                .setContentText(notification)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.mipmap.ic_launcher))
                .build();
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,mNotification);
    }
}