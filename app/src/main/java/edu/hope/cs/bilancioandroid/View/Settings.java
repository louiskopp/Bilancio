package edu.hope.cs.bilancioandroid.View;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.hope.cs.bilancioandroid.Controller.MyReceiver;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.R;

public class Settings extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Switch budgetBarSwitch;
    Switch enableRemSwitch;
    Switch dailyRemSwitch;
    Switch todaySwitch;
    Switch teachModeSwitch;
    Bundle bundle;
    AppDatabase db;
    private int hour;
    private int minutes;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    private PopupWindow popupWindow;
    private TextView scenarioInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putString("Settings", "Yes");
        editor.apply();
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_settings);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        final String lastPage = prefs.getString("Last Page", "Last Page");
        int size = prefs.getInt("Text Size", 18);

        TextView colorScheme = findViewById(R.id.colorScheme);
        colorScheme.setTextSize(size);
        colorScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent colorIntent = new Intent(Settings.this,ColorSchemes.class);
                colorIntent.putExtra("Last Page", lastPage);
                startActivity(colorIntent);
            }
        });

        final TextView textSettings = findViewById(R.id.textSettings);
            textSettings.setTextSize(size);
        textSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent textIntent = new Intent(Settings.this,TextSettings.class);
                textIntent.putExtra("Last Page", lastPage);
                startActivity(textIntent);
            }
        });

        budgetBarSwitch = findViewById(R.id.viewBudgetSwitch);
            budgetBarSwitch.setTextSize(size);
        if(prefs.getString("Overview", "").equals("Off")) {
            budgetBarSwitch.setChecked(false);
        } else {
            budgetBarSwitch.setChecked(true);
        }
        budgetBarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                budgetBarChecked();
            }
        });

        todaySwitch = findViewById(R.id.viewTodaySwitch);
            todaySwitch.setTextSize(size);
        if(prefs.getString("Today", "").equals("On")) {
            todaySwitch.setChecked(true);
        } else {
            todaySwitch.setChecked(false);
        }
        todaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                todayChecked();
            }
        });

        enableRemSwitch = findViewById(R.id.enableRemSwitch);
            enableRemSwitch.setTextSize(size);
        if(prefs.getString("Notification", "").equals("On")) {
            enableRemSwitch.setChecked(true);
        } else {
            enableRemSwitch.setChecked(false);
        }
        enableRemSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                customNotifications();
            }
        });

        dailyRemSwitch  = findViewById(R.id.dailyRemSwitch);
            dailyRemSwitch.setTextSize(size);
        if(prefs.getString("Daily", "").equals("On")) {
            dailyRemSwitch.setChecked(true);
        } else {
            dailyRemSwitch.setChecked(false);
        }
        dailyRemSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dailyReminders();
            }
        });

        TextView advancedSettings = findViewById(R.id.advancedSettings);
            advancedSettings.setTextSize(size);
        advancedSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adSettings = new Intent(Settings.this,AdvancedSettings.class);
                startActivity(adSettings);
            }
        });

        teachModeSwitch = findViewById(R.id.teachingModeSwitch);
            teachModeSwitch.setTextSize(size);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            teachModeSwitch.setChecked(true);
        } else {
            teachModeSwitch.setChecked(false);
        }
        teachModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                teachingModeOn();
                recreate();
            }
        });

        TextView reset = findViewById(R.id.resetDefaults);
        View view = findViewById(R.id.view12);
        View view2 = findViewById(R.id.view13);
        TextView teachSettings = findViewById(R.id.teachingSettings);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                reset.setTextSize(size);
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(prefs.getBoolean("Scenario", false)){
                        final AlertDialog.Builder a_builder = new AlertDialog.Builder(Settings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        a_builder.setMessage("Please finish the Scenario first.").setCancelable(false)
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("You can't reset during a Scenario!");
                        alert.show();
                    }
                    else {
                        final AlertDialog.Builder a_builder = new AlertDialog.Builder(Settings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        a_builder.setMessage("Set all balances to zero and categories to default?").setCancelable(false)
                                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Thread make = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ArrayList<Budget> budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                                                double all = 0.0;
                                                tDb.teachingModeDao().deleteAll();
                                                for (Budget budget : budgets) {
                                                    budget.setAmount(budget.getAllotted());
                                                    all += budget.getAllotted();
                                                    budget.setTransactions("");
                                                }
                                                DecimalFormat df = new DecimalFormat("0.00##");
                                                String entry = df.format(all);
                                                entry = "$" + entry;
                                                editor.putString("Budget", entry);
                                                for (Budget budget : budgets) {
                                                    tDb.teachingModeDao().insertAll(budget);
                                                }
                                                Budget need = tDb.teachingModeDao().findByCategory("Clothing");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Clothing", 200, "Clothes");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.clothing_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                                need = tDb.teachingModeDao().findByCategory("Education");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Education", 200, "Classes, tuition, books, school supplies");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.education_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                                need = tDb.teachingModeDao().findByCategory("Entertainment");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Entertainment", 200, "Fun things to do with friends and eating out");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.shopping_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                                need = tDb.teachingModeDao().findByCategory("Food");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Food", 200, "Food and grocery purchases");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.groceries_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                                need = tDb.teachingModeDao().findByCategory("Home Care");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Home Care", 200, "Cleaning supplies, batteries, light bulbs");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.home_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                                need = tDb.teachingModeDao().findByCategory("Personal Care");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Personal Care", 200, "Hygiene related items");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.medical_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                                need = tDb.teachingModeDao().findByCategory("Phone");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Phone", 200, "Phone");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.phone_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                                need = tDb.teachingModeDao().findByCategory("Rent");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Rent", 200, "Cost of apartment");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.rent_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                                need = tDb.teachingModeDao().findByCategory("Transportation");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Transportation", 200, "us tickets, gas, car insurance, car repairs");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.auto_care_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                                need = tDb.teachingModeDao().findByCategory("Utilities");
                                                if(need==null){
                                                    int n = (int) System.currentTimeMillis();
                                                    need = new Budget(n, "Utilities", 200, "Water, heat/ electricity, trash, recycle, and cable/internet");
                                                    need.setTransactions("");
                                                    need.setAllotted(200);
                                                    need.setImage(R.drawable.utilities_black);
                                                    tDb.teachingModeDao().insertAll(need);
                                                }
                                            }
                                        });
                                        make.start();
                                        try {
                                            make.join();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Reset to Defaults");
                        alert.show();

                    }
                }
            });
        } else {
            reset.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            teachSettings.setVisibility(View.GONE);
        }

        LinearLayout layout = findViewById(R.id.scenario_layout);
        TextView calculator = findViewById(R.id.calculator);
        TextView scenarioFinish = (TextView) findViewById(R.id.scenario_goto);
        calculator.setVisibility(View.INVISIBLE);
        scenarioFinish.setVisibility(View.INVISIBLE);

        scenarioInfo = (TextView) findViewById(R.id.scenario_show);
        scenarioInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScenarioPopUp();
            }
        });


        if(!prefs.getBoolean("Scenario", false)) {
            layout.setVisibility(View.GONE);
            scenarioInfo.setVisibility(View.GONE);
            calculator.setVisibility(View.GONE);
            scenarioFinish.setVisibility(View.GONE);
        }


    }

    private void todayChecked() {
        if(todaySwitch.isChecked()) {
            editor.putString("Today", "On");
            editor.apply();
        } else if(!todaySwitch.isChecked() && prefs.getString("Today", "").equals("On")) {
            editor.remove("Today");
            editor.apply();
        }
    }

    public void budgetBarChecked() {
        if(!budgetBarSwitch.isChecked()) {
            editor.putString("Overview", "Off");
            editor.apply();
        } else if(budgetBarSwitch.isChecked() && prefs.getString("Overview", "").equals("Off")) {
            editor.remove("Overview");
            editor.apply();
        }
    }
    public void customNotifications() {
        {
            if(enableRemSwitch.isChecked()) {
                editor.putString("Notification", "On");
                editor.apply();
            } else if(!enableRemSwitch.isChecked() && prefs.getString("Notification", "").equals("On")) {
                editor.remove("Notification");
                editor.apply();
            }
        }
    }

    public void dailyReminders() {
            if(dailyRemSwitch.isChecked()) {
                editor.putString("Daily", "On");
                editor.apply();
                Calendar c = Calendar.getInstance();
                int currentHour = c.get(Calendar.HOUR_OF_DAY);
                int currentMinute = c.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener click = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int clockHour, int clockMin) {
                        hour = clockHour;
                        minutes = clockMin;
                        setUpNotification();
                    }
                };
                final TimePickerDialog picker = new TimePickerDialog(this, R.style.timePicker, click, currentHour, currentMinute, false);
                picker.setMessage("Please set the time for your reminder.");
                picker.setCancelable(false);

                picker.create();
                picker.setTitle("Set Reminder Time");
                picker.show();
            } else if(!dailyRemSwitch.isChecked() && prefs.getString("Daily", "").equals("On")) {
                editor.remove("Daily");
                editor.apply();
            }
    }

    public void teachingModeOn() {
        if(teachModeSwitch.isChecked()) {
            editor.putString("Teaching Mode", "On");
            editor.apply();
            Thread go = new Thread(new Runnable() {
                @Override
                public void run() {
                    tDb.teachingModeDao().deleteAll();
                    ArrayList<Budget> budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                    for(Budget budget:budgets){
                        int n = (int) System.currentTimeMillis();
                        budget.setUid(n);
                        tDb.teachingModeDao().insertAll(budget);
                    }
                    Budget need = tDb.teachingModeDao().findByCategory("Clothing");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Clothing", 200, "Clothes");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.clothing_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                    need = tDb.teachingModeDao().findByCategory("Education");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Education", 200, "Classes, tuition, books, school supplies");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.education_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                    need = tDb.teachingModeDao().findByCategory("Entertainment");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Entertainment", 200, "Fun things to do with friends and eating out");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.shopping_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                    need = tDb.teachingModeDao().findByCategory("Food");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Food", 200, "Food and grocery purchases");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.groceries_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                    need = tDb.teachingModeDao().findByCategory("Home Care");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Home Care", 200, "Cleaning supplies, batteries, light bulbs");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.home_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                    need = tDb.teachingModeDao().findByCategory("Personal Care");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Personal Care", 200, "Hygiene related items");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.medical_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                    need = tDb.teachingModeDao().findByCategory("Phone");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Phone", 200, "Phone");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.phone_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                    need = tDb.teachingModeDao().findByCategory("Rent");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Rent", 200, "Cost of apartment");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.rent_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                    need = tDb.teachingModeDao().findByCategory("Transportation");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Transportation", 200, "us tickets, gas, car insurance, car repairs");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.auto_care_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                    need = tDb.teachingModeDao().findByCategory("Utilities");
                    if(need==null){
                        int n = (int) System.currentTimeMillis();
                        need = new Budget(n, "Utilities", 200, "Water, heat/ electricity, trash, recycle, and cable/internet");
                        need.setTransactions("");
                        need.setAllotted(200);
                        need.setImage(R.drawable.utilities_black);
                        tDb.teachingModeDao().insertAll(need);
                    }
                }
            });
            go.start();
            try {
                go.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setTheme(R.style.TeachingMode);
        } else if(!teachModeSwitch.isChecked() && prefs.getString("Teaching Mode", "").equals("On")) {
            editor.remove("Teaching Mode");
            editor.remove("Scenario");
            editor.remove("ID");
            editor.apply();
        }
    }

    private void openScenarioPopUp() {
        Point p = new Point();
        int[] location = new int[2];
        scenarioInfo.getLocationOnScreen(location);
        p.x = location[0];
        p.y = location[1];
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.info_popup,null);
        TextView textView = (TextView) customView.findViewById(R.id.tv);
        Scenario message = new Scenario(prefs.getString("ID", ""));
        textView.setText(message.getFullStr());
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(
                customView,
                800,
                300
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }

        findViewById(R.id.cons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.cons), Gravity.NO_GRAVITY,p.x,p.y-250);
    }

    @Override
    public void onBackPressed() {

            String last = prefs.getString("Last Page", "Last Page");
            if (last.equals("Overview")) {
                startActivity(new Intent(Settings.this, Overview.class));
            } else if (last.equals("Savings")) {
                startActivity(new Intent(Settings.this, Savings.class));
            } else if (last.equals("Wallet")) {
                startActivity(new Intent(Settings.this, WalletActivity.class));
            } else if (last.equals("Reminders") && prefs.getString("Teaching Mode", "").equals("On")) {
                startActivity(new Intent(Settings.this, ScenarioActivity.class));
            } else if(last.equals("Reminders") && !prefs.getString("Teaching Mode", "").equals("On")) {
                startActivity(new Intent(Settings.this, ReminderActivity.class));
            } else if(last.equals("Scenarios") && !prefs.getString("Teaching Mode", "").equals("On")) {
                startActivity(new Intent(Settings.this, ReminderActivity.class));
            }else if(last.equals("Scenarios") && prefs.getString("Teaching Mode", "").equals("On")) {
                startActivity(new Intent(Settings.this, ScenarioActivity.class));
            } else {
            super.onBackPressed();
        }
    }

    public void setUpNotification () {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        Intent myIntent = new Intent(this, MyReceiver.class);
        myIntent.putExtra("type", "Transactions");
        myIntent.putExtra("title", "Transactions");
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, myIntent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);

    }

}
