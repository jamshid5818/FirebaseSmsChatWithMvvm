package jx.lessons.firebaseSmsChatWithMvvm.data.cloudMessaging;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import jx.lessons.firebaseSmsChatWithMvvm.data.utils.MyNotificationManager;
import jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.MainActivity;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        notifyUser(remoteMessage.getFrom(), Objects.requireNonNull(remoteMessage.getNotification()).getBody());
    }
    public void notifyUser(String from, String notification){
        MyNotificationManager myNotificationManager = new MyNotificationManager(getApplicationContext());
        myNotificationManager.showNotification(from, notification,new Intent(getApplicationContext(), MainActivity.class));

    }
}