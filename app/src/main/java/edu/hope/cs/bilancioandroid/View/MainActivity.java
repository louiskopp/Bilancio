package edu.hope.cs.bilancioandroid.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import edu.hope.cs.bilancioandroid.R;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;

    private ImageView mImageView;
    private String cycle;
    private Date date = new Date();
    private boolean monday;
    private boolean tuesday;
    private boolean second;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        monday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
        tuesday = cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY;
        second = cal.get(Calendar.DAY_OF_MONTH) != 1;
        mImageView = (ImageView) findViewById(R.id.icon);
        mImageView.setImageResource(R.mipmap.iconfinal);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        cycle = prefs.getString("Cycle","");
        if(tuesday){
            editor.putBoolean("mondays", true);
            editor.apply();
        }
        if(second){
            editor.putBoolean("first", true);
            editor.apply();
        }
        Thread thread;
        if (prefs.contains("Name") &&
                prefs.contains("Phone") &&
                prefs.contains("Password") &&
                prefs.contains("Cycle")) {

            if (prefs.contains("Budget")) {
                if (!cycle.equals("")) {
                    if (cycle.equals("One Week")) {
                        if (monday && prefs.getBoolean("mondays", true)) {
                            editor.putBoolean("End", true);
                            editor.putBoolean("mondays", false);
                            editor.apply();
                            thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        sleep(2000);
                                        Intent intent = new Intent(getApplicationContext(), EndOfBudgetCycle.class);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                            return;
                        }

                    } else if (cycle.equals("Two Weeks")) {
                        if (monday && prefs.getBoolean("second monday", true) && prefs.getBoolean("mondays", true)) {
                            editor.putBoolean("mondays", false);
                            editor.putBoolean("second monday", false);
                            editor.putBoolean("End", true);
                            editor.apply();
                            thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        sleep(2000);
                                        Intent intent = new Intent(getApplicationContext(), EndOfBudgetCycle.class);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                            return;
                        } else if (monday) {
                            editor.putBoolean("mondays", false);
                            editor.putBoolean("second monday", true);
                            editor.apply();
                        }
                    } else {
                        boolean first = cal.get(Calendar.DAY_OF_MONTH) == 1;
                        if (first && prefs.getBoolean("first", true)) {
                            editor.putBoolean("first", false);
                            editor.putBoolean("End", true);
                            editor.apply();
                            thread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        sleep(2000);
                                        Intent intent = new Intent(getApplicationContext(), EndOfBudgetCycle.class);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                            return;
                        }
                    }
                }
                thread = new Thread() {
                   @Override
                   public void run() {
                        try {
                            sleep(2000);
                            Intent intent = new Intent(getApplicationContext(), Overview.class);
                            startActivity(intent);
                            finish();
                        } catch (InterruptedException e) {
                           e.printStackTrace();
                        }
                    }
                };
            } else {
                thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                            Intent intent = new Intent(getApplicationContext(), MakeOrPresetBudgets.class);
                            startActivity(intent);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                };
            }
        }
            else {
            editor.putBoolean("first", false);
            editor.putBoolean("mondays", false);
            editor.apply();
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            };
        }
        thread.start();
    }
    @Override
    public void onBackPressed() {

    }

}
