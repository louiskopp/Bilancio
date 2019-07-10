package edu.hope.cs.bilancioandroid.Controller;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class StickyService extends Service {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    private ArrayList<Budget> saveBudgets;

    public StickyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        Type listType = new TypeToken<ArrayList<Budget>>() {}.getType();
        String value = prefs.getString("not saved yet", "");
        saveBudgets =  new Gson().fromJson(value, listType);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        editor.remove("current income");
        editor.remove("not yet saved");
        editor.apply();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    if (saveBudgets != null) {
                    tDb.teachingModeDao().deleteAll();
                        for (Budget budget : saveBudgets) {
                            tDb.teachingModeDao().insertAll(budget);
                        }
                    }
                }
                else{
                    if (saveBudgets != null) {
                    db.budgetDao().deleteAll();
                    for(Budget budget: saveBudgets){
                        db.budgetDao().insertAll(budget);
                        }
                    }
                }
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("App Closed", "the stickyService has been activated");
    }
}
