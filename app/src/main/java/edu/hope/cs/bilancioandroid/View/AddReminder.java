package edu.hope.cs.bilancioandroid.View;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import edu.hope.cs.bilancioandroid.Controller.MyReceiver;
import edu.hope.cs.bilancioandroid.Database.ReminderDatabase;
import edu.hope.cs.bilancioandroid.Model.Reminder;
import edu.hope.cs.bilancioandroid.R;

public class AddReminder extends AppCompatActivity {

    private String title;
    private Date remindMeOn;
    private String every;
    private String type;
    private String dateStr;
    private String howOften;
    private EditText editText;
    private TextView cancel;
    private TextView save;
    private ReminderDatabase db;
    private TextView remindMeEvery;
    private Spinner reminderSpinner;
    private TextView selectDateAndTime;
    private long time;
    private View dialogView;
    private AlertDialog alertDialog;
    private Switch reminderSwitch;
    private Button bill;
    private Button income;
    private boolean titleHelper = false;
    private boolean typeHelper = false;
    private PendingIntent pendingIntent;
    private int year, month, day, hour, minute;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Reminder reminder;
    Reminder placeholderReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getIntent().getIntExtra("pass", 0)!=0){
           noNotification();
        }
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        final int size = prefs.getInt("Text Size", 18);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_add_reminder);
        type = getIntent().getStringExtra("type");
        if(getIntent().getStringExtra("type")!=null){
            if(getIntent().getStringExtra("type").equals("Income")) {
                typeHelper = true;
            }
        }
        title = getIntent().getStringExtra("title");
        editText = (EditText) findViewById(R.id.enter_title_reminder_add);
        editText.setTextSize(size);
        if(getIntent().getStringExtra("title")!=null){
            titleHelper = true;
            editText.setText(getIntent().getStringExtra("title"));
        }
        else{editText.setText("");}
        time = getIntent().getLongExtra("time", 0);
        howOften = getIntent().getStringExtra("how often");
        dateStr = getIntent().getStringExtra("date");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                title = s.toString();
            }
        });
        bill = (Button) findViewById(R.id.reminder_bill);
        income = (Button) findViewById(R.id.reminder_income);
        if (typeHelper) {
            bill.setBackgroundColor(Color.WHITE);
            bill.setTextColor(Color.BLUE);
            income.setBackgroundColor(Color.BLUE);
            income.setTextColor(Color.WHITE);
            type = "Income";
        }
        else {
            bill.setBackgroundColor(Color.BLUE);
            bill.setTextColor(Color.WHITE);
            income.setBackgroundColor(Color.WHITE);
            income.setTextColor(Color.BLUE);
            type = "Bill";
        }
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill.setBackgroundColor(Color.BLUE);
                bill.setTextColor(Color.WHITE);
                income.setBackgroundColor(Color.WHITE);
                income.setTextColor(Color.BLUE);
                type = "Bill";
                if(titleHelper){
                    editText.setText(getIntent().getStringExtra("title"));
                }
                else {
                    editText.setText("");
                }
            }
        });
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income.setBackgroundColor(Color.BLUE);
                income.setTextColor(Color.WHITE);
                bill.setBackgroundColor(Color.WHITE);
                bill.setTextColor(Color.BLUE);
                type = "Income";
                editText.setText("Record Income");
            }
        });
        alertDialog = new AlertDialog.Builder(this).create();
        int spinnerText;
        if(size == 14) {
            spinnerText = R.layout.spinner_small;
        } else if(size == 18) {
            spinnerText = R.layout.spinner_medium;
        } else {
            spinnerText = R.layout.spinner_large;
        }
        reminderSpinner = (Spinner) findViewById(R.id.select_how_often);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminders_array, spinnerText);
        adapter.setDropDownViewResource(spinnerText);
        reminderSpinner.setAdapter(adapter);
        int t = reminderSpinner.getSelectedItemPosition();
        every = (String) adapter.getItem(t);
        db = Room.databaseBuilder(getApplicationContext(),
                ReminderDatabase.class, "reminder_db").build();


        TextView titleBox = (TextView) findViewById(R.id.textView19);
        titleBox.setTextSize(size);

        TextView wordTitle = findViewById(R.id.textView25);
        wordTitle.setTextSize(size);

        TextView remindMe = findViewById(R.id.textView23);
        remindMe.setTextSize(size);

        TextView repeat = findViewById(R.id.textView24);
        repeat.setTextSize(size);


        cancel = (TextView) findViewById(R.id.cancel_add_reminder);
        cancel.setTextSize(size);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(), ReminderActivity.class);
                startActivity(nextIntent);
            }
        });

        save = (TextView) findViewById(R.id.save_add_reminder);
        save.setTextSize(size);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread go = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(reminderSwitch.isChecked()){
                            int t = reminderSpinner.getSelectedItemPosition();
                            every = (String) adapter.getItem(t);
                        }
                        if(titleHelper){
                            reminder = db.reminderDao().findByTitle(getIntent().getStringExtra("title"));
                            placeholderReminder = reminder;
                            reminder.setDate(dateStr);
                            reminder.setTitle(title);
                            reminder.setType(type);
                            if (reminderSwitch.isChecked()) {
                                reminder.setHowOften(every);
                            }
                            db.reminderDao().delete(placeholderReminder);
                            placeholderReminder = reminder;
                            db.reminderDao().insertAll(reminder);

                        }
                        else {
                            int n =(int) System.currentTimeMillis();
                            reminder = new Reminder(n, title, type, dateStr);
                            if (reminderSwitch.isChecked()) {
                                reminder.setHowOften(every);
                            }
                            else{
                                reminder.setHowOften(null);
                            }
                            placeholderReminder = reminder;
                            db.reminderDao().insertAll(reminder);
                        }
                    }
                });
                go.start();
                try {
                    go.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setUpNotification(year, month, day, hour, minute);
                if (prefs.getString("Notification", "Off").equals("Off")) {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(AddReminder.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Would you like to turn them on?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   editor.putString("Notification", "On");
                                   editor.apply();
                                    Intent nextIntent = new Intent(getApplicationContext(), ReminderActivity.class);
                                    finish();
                                    startActivity(nextIntent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent nextIntent = new Intent(getApplicationContext(), ReminderActivity.class);
                                    finish();
                                    startActivity(nextIntent);
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Reminders are currently switched off!");
                    alert.show();
                }
                else {
                    Intent nextIntent = new Intent(getApplicationContext(), ReminderActivity.class);
                    finish();
                    startActivity(nextIntent);
                }

            }
        });

        reminderSwitch = (Switch) findViewById(R.id.reminder_switch);
        remindMeEvery = (TextView) findViewById(R.id.remind_me_every);
        remindMeEvery.setTextSize(size);
        remindMeEvery.setVisibility(View.GONE);
        reminderSpinner.setVisibility(View.GONE);
        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (reminderSwitch.isChecked()) {
                    remindMeEvery.setVisibility(View.VISIBLE);
                    reminderSpinner.setVisibility(View.VISIBLE);
                } else {
                    remindMeEvery.setVisibility(View.GONE);
                    reminderSpinner.setVisibility(View.GONE);
                }
            }
        });

        selectDateAndTime = (TextView) findViewById(R.id.select_date_and_time);
        selectDateAndTime.setTextSize(size);
        final DatePicker datePicker = new DatePicker(this);
        selectDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener click =  new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet (DatePicker view ,int yearPick, int monthPick, int dayPick){
                    day = dayPick;
                    month = monthPick;
                    year = yearPick;

                    timePicker();
                }
                };

                final DatePickerDialog datePick = new DatePickerDialog(AddReminder.this, R.style.datePicker, click, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePick.setMessage("Please set the time for your reminder.");
                datePick.setCancelable(false);
                datePick.create();
                datePick.setTitle("Set Reminder Time");
                datePick.show();
            }
        });

        if (title != null) {
            editText.setText(title);
        }

        if(dateStr!=null){
            selectDateAndTime.setText(dateStr);
        }

        if(howOften!=null){
            reminderSwitch.setChecked(true);
            //reminderSpinner.setPromptId(2);
        }
    }

    public void setUpNotification (int year, int month, int day, int hour, int minute){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);

        Intent myIntent=new Intent(getApplicationContext(),MyReceiver.class);
        myIntent.putExtra("type",type);
        myIntent.putExtra("title",title);
        int id=(int)System.currentTimeMillis();
        pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),id,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        if(reminderSwitch.isChecked()){
        int i=0;
        if(every.equals("Month")){
        i=28;
        }
        else if(every.equals("Day")){
        i=1;
        }
        else if(every.equals("Week")){
        i=7;
        }
        else if(every.equals("Two weeks")){
        i=14;
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*i,pendingIntent);
        }
        else{
        alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
        }
        }

    public void timePicker() {
        Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener click = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int clockHour, int clockMin) {
                hour = clockHour;
                minute = clockMin;
                setUpNotification(year, month, day, hour, minute);
                String monthStr = new DateFormatSymbols().getMonths()[month];
                int amorpm;
                String of;
                String minutes;
                if(minute<10){
                    minutes="0"+minute;
                }
                else{minutes = String.valueOf(minute);
                }
                if(hour>12){
                    amorpm=hour-12;
                    of = "PM";
                }
                else{amorpm=hour;
                    of="AM";
                }
                dateStr = monthStr+ " " +day+", "+year+" at " + amorpm+":"+minutes +" "+of;
                selectDateAndTime.setText(dateStr);
            }
        };
        final TimePickerDialog picker = new TimePickerDialog(this, R.style.timePicker, click, currentHour, currentMinute, false);
        picker.setMessage("Please set the time for your reminder.");
        picker.setCancelable(false);

        picker.create();
        picker.setTitle("Set Reminder Time");
        picker.show();
    }

    public void noNotification() {
        int newId = getIntent().getIntExtra("pass", 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), newId, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        finish();
    }
}


