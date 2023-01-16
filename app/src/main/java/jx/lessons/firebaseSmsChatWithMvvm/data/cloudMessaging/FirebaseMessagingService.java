package jx.lessons.firebaseSmsChatWithMvvm.data.cloudMessaging;

import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Objects;
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.MyNotificationManager;
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.SharedPref;
import jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.MainActivity;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public static final String TOKEN_BROADCAST = "myfcmtokenbroadcast";

    SharedPref shared = new SharedPref(getApplicationContext());
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        shared.setToken(token);
    }

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