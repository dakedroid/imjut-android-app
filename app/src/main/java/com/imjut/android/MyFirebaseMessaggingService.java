package com.imjut.android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.imjut.android.Modelos.User;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Angel on 10/03/2018.
 */

public class MyFirebaseMessaggingService extends FirebaseMessagingService {

    private DatabaseReference mCurrentUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User mUser;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(mAuth.getCurrentUser() != null && remoteMessage.getData().size() > 0){
            Log.i("Notificaciones", "Mensaje Recibido");
            final String title = remoteMessage.getData().get("title");
            final String description = remoteMessage.getData().get("description");
            final String postimageurl = remoteMessage.getData().get("postimageurl");

            final String mCurrentEmailUser = mAuth.getCurrentUser().getEmail().replace(".",",");
            mCurrentUser = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(mCurrentEmailUser);

            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUser = dataSnapshot.getValue(User.class);
                    assert mUser != null;
                    if(mUser.isNoti_activadas()){
                        showNotification(title,description,postimageurl);
                        Log.i("Notificaciones", "Notificacion enviada");
                    }
                    mCurrentUser.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mCurrentUser.addValueEventListener(listener);


            //Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
            }
        }

    }

    public void showNotification(String title, String description, String postimageurl){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra("PATH", image);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Evento: " + title)
                .setSmallIcon(R.mipmap.imjut)
                .setContentText(description)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        int mNotificacionId = (int) System.currentTimeMillis();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(mNotificacionId /* ID of notification */, notificationBuilder.build());
    }
}


