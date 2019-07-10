package edu.hope.cs.bilancioandroid.Controller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import edu.hope.cs.bilancioandroid.Database.ReminderDatabase;
import edu.hope.cs.bilancioandroid.Model.Reminder;
import edu.hope.cs.bilancioandroid.R;
import edu.hope.cs.bilancioandroid.View.Overview;

public class Utils {

    public static NotificationManager mManager;
    private static Resources res;
    public static SharedPreferences prefs;
    private static ReminderDatabase db;
    private static Reminder reminder;


    @SuppressWarnings("static-access")
    public static void generateNotification(Context context, final String title, String text) {
        db = Room.databaseBuilder(context,
                ReminderDatabase.class, "reminder_db").build();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                reminder = db.reminderDao().findByTitle(title);
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName),Context.MODE_PRIVATE);
        res = context.getResources();
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context, context.getResources().getString(R.string.notificationChannelID));
        nb.setSmallIcon(R.mipmap.iconfinal);
        nb.setContentTitle(title);
        nb.setContentText(text);
        nb.setTicker("Take a look");

        nb.setAutoCancel(true);

        Intent resultIntent = new Intent(context, Overview.class);
        TaskStackBuilder TSB = TaskStackBuilder.create(context);
        TSB.addParentStack(Overview.class);
        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);

        // mId allows you to update the notification later on.

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.notificationChannelID), context.getString(R.string.notificationChannelName), NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        if(title.equals("Transactions")) {
            if(prefs.getString("Daily", "Off").equals("On")) {
                notificationManager.notify(11221, nb.build());
            }
        } else {

            if(reminder==null){

            }
            else if (prefs.getString("Notification", "Off").equals("On")) {
                notificationManager.notify(11221, nb.build());
            }
        }

    }

}
