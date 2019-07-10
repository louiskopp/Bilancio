package edu.hope.cs.bilancioandroid.View;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import edu.hope.cs.bilancioandroid.Controller.MyReceiver;
import edu.hope.cs.bilancioandroid.Controller.NavigationViewHelper;
import edu.hope.cs.bilancioandroid.Database.ReminderDatabase;
import edu.hope.cs.bilancioandroid.Model.Reminder;
import edu.hope.cs.bilancioandroid.R;


public class ReminderActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_overview:
                    Intent overviewIntent = new Intent(getApplicationContext(), Overview.class);
                    startActivity(overviewIntent);
                    finish();
                    break;
                case R.id.navigation_wallet:
                    Intent walletIntent = new Intent(getApplicationContext(), WalletActivity.class);
                    startActivity(walletIntent);
                    finish();
                    break;
                case R.id.navigation_savings:
                    Intent savingsIntent = new Intent(getApplicationContext(), Savings.class);
                    startActivity(savingsIntent);
                    finish();
                    break;

                case R.id.navigation_reminders:
                    break;
            }
            return false;
        }
    };


    private PopupWindow popupWindow;
    private ConstraintLayout cons;
    private ListView listView;
    private ReminderAdapter reminderAdapter;
    private ArrayList<Reminder> reminders;
    private ReminderDatabase db;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private View dialogView;
    private AlertDialog alertDialog;
    private int year, month, day, hour, minute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        if (prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_reminders);
        int size = prefs.getInt("Text Size", 20);
        db = Room.databaseBuilder(getApplicationContext(),
                ReminderDatabase.class, "reminder_db").build();

        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                reminders = (ArrayList<Reminder>) db.reminderDao().getAll();
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        alertDialog = new AlertDialog.Builder(this).create();
        listView = (ListView) findViewById(R.id.reminders_listview);
        reminderAdapter = new ReminderAdapter(this, reminders);
        listView.setAdapter(reminderAdapter);
        TextView textView = (TextView) findViewById(R.id.empty_reminders);
        textView.setTextSize(size);

        listView.setEmptyView(textView);
        if (reminderAdapter == null) {
            listView.setVisibility(View.GONE);
        }
        cons = (ConstraintLayout) findViewById(R.id.remindLayout);
        final BottomNavigationView nav = findViewById(R.id.navigation);
        NavigationViewHelper.disableShiftMode(nav);
        MenuItem item = nav.getMenu().getItem(3).setChecked(true);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TextView settingsBtn = (TextView) findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(ReminderActivity.this, Settings.class);
                editor.putString("Last Page", "Reminders");
                editor.apply();
                startActivity(settingsIntent);
            }
        });

        final Button anchor = findViewById(R.id.anchor);
        anchor.setVisibility(View.INVISIBLE);
        final Context wrapper = new ContextThemeWrapper(this, R.style.popupMenu);
        Button reminderBtn = (Button) findViewById(R.id.addReminders);
        reminderBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(wrapper, anchor, Gravity.CENTER_HORIZONTAL);
                popup.inflate(R.menu.reminders_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_reminder_item:
                                Intent nextIntent = new Intent(getApplicationContext(), AddReminder.class);
                                startActivity(nextIntent);
                                return true;

                            case R.id.cancelButton:
                                popup.dismiss();
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });


        if(prefs.getBoolean("reminder", true)){
            editor.putBoolean("reminder", false);
            editor.apply();
            welcomeActivityPopup();
        }

    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(ReminderActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAffinity();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Are you sure you want to exit?");
        alert.show();
    }

        public void buttonPopUp () {
            final AlertDialog.Builder a_builder = new AlertDialog.Builder(ReminderActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            a_builder.setMessage("Press on the reminder if you want to edit it.").setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alert = a_builder.create();
            alert.setTitle("Reminder Information");
            alert.show();

        }

        public void goToEditReminder (Reminder reminder){
            Intent nextIntent = new Intent(getApplicationContext(), AddReminder.class);
            nextIntent.putExtra("type", reminder.getType());
            nextIntent.putExtra("title", reminder.getTitle());
            nextIntent.putExtra("date", reminder.getDate());
            nextIntent.putExtra("how often", reminder.getHowOften());

            startActivity(nextIntent);
        }



    private void welcomeActivityPopup() {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(ReminderActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("You can select if and when you would like to have a daily reminder to enter your transactions.").setCancelable(false)
                .setPositiveButton("Set a Time", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("Daily", "On");
                        editor.putString("Notification", "On");
                        editor.apply();
                        Calendar c = Calendar.getInstance();
                        int currentHour = c.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = c.get(Calendar.MINUTE);
                        TimePickerDialog.OnTimeSetListener click = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int clockHour, int clockMin) {
                                hour = clockHour;
                                minute = clockMin;
                                setUpNotification();
                            }
                        };
                        final TimePickerDialog picker = new TimePickerDialog(ReminderActivity.this, R.style.timePicker, click, currentHour, currentMinute, false);
                        picker.setMessage("Please set the time for your reminder.");
                        picker.setCancelable(false);

                        picker.create();
                        picker.setTitle("Set Reminder Time");
                        picker.show();
                    }
                })
                .setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Would you like to set up daily reminders?");
        alert.show();
    }

    public void setUpNotification () {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent myIntent = new Intent(this, MyReceiver.class);
        myIntent.putExtra("type", "Transactions");
        myIntent.putExtra("title", "Transactions");
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, myIntent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);

    }

    public void deleteReminder(final Reminder r) {
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                db.reminderDao().delete(r);
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recreate();
}
}

