package com.example.mifirestore01.RemindTest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.mifirestore01.R;


import java.util.Date;

public class NotifierAlarm extends BroadcastReceiver {

    private AppDatabase appDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {

        appDatabase = AppDatabase.geAppdatabase(context.getApplicationContext());
        RoomDAO roomDAO = appDatabase.getRoomDAO();
        Reminders reminder = new Reminders();
        reminder.setMessage(intent.getStringExtra("Message"));
        reminder.setRemindDate(new Date(intent.getStringExtra("RemindDate")));
        reminder.setId(intent.getIntExtra("id",0));
        roomDAO.Delete(reminder);
        AppDatabase.destroyInstance();

        Intent service1 = new Intent(context, NotificationService.class);
        service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        ContextCompat.startForegroundService(context, service1 );


    }
}
