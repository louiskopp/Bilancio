package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class Homework extends AppCompatActivity {

    private Scenario scenario;
    private TextView scenarioId;
    private PopupWindow popupWindow;
    private ConstraintLayout relativeLayout;
    private String id;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Budget budget;
    private Budget placeholder;
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    private Converters converters;
    private ArrayList<Budget> budgets = new ArrayList<Budget>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TeachingMode);
        setContentView(R.layout.activity_homework);
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        Random rand = new Random();
        String fromPre = getIntent().getStringExtra("id");
        int time = rand.nextInt(2);
        int category = rand.nextInt(9);
        int type = rand.nextInt(9);
        int amount = rand.nextInt(99);
        amount = amount*100;
        if(type==6){
            amount = 1000;
        }
        if(type==7){
            amount= amount*100;
        }
        String amountStr = "0000"+ amount;
        id = time+"."+category+"."+type+"."+amountStr;
        if(fromPre==null) {
            scenario = new Scenario(id);
        }
        else{
            scenario = new Scenario(fromPre);
            id = fromPre;
        }
        String setBudgets = id.substring(0,1);
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                budget = tDb.teachingModeDao().findByCategory(scenario.getCategoryString());
                placeholder = budget;
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switch(setBudgets){
            case("0"):
                budget.setAmount(budget.getAllotted());
                break;
            case("1"):
                budget.setAmount(budget.getAllotted()/2);
                String tra = budget.getTransactions();
                ArrayList<Transaction> transactions = converters.fromString(tra);
                if(transactions==null){
                      transactions = new ArrayList<Transaction>();
                }
                transactions.add(new Transaction(-budget.getAllotted()/2,"", budget.getCategory(), "August 1, 1996"));
                tra = converters.fromTransactions(transactions);
                budget.setTransactions(tra);
                break;
            case("2"):
                budget.setAmount(budget.getAllotted()/8);
                String tra1 = budget.getTransactions();
                ArrayList<Transaction> transactions1 = converters.fromString(tra1);
                if(transactions1==null){
                    transactions1 = new ArrayList<Transaction>();
                }
                transactions1.add(new Transaction(-budget.getAllotted()/8*7,"", budget.getCategory(), "August 1, 1996"));
                tra1 = converters.fromTransactions(transactions1);
                budget.setTransactions(tra1);
                break;
        }
        Thread stop = new Thread(new Runnable() {
            @Override
            public void run() {
                tDb.teachingModeDao().delete(placeholder);
                tDb.teachingModeDao().insertAll(budget);
                budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
            }
        });
        stop.start();
        try {
            stop.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextView textView = (TextView) findViewById(R.id.homework_scenario);
        textView.setText(scenario.getFullStr());
        relativeLayout = (ConstraintLayout) findViewById(R.id.homework_layout);
        scenarioId = (TextView) findViewById(R.id.homework_id);
        scenarioId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInfo();
            }
        });

        TextView start = (TextView) findViewById(R.id.begin_homework);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("Scenario", true);
                editor.putString("ID", id);
                Gson gson = new Gson();
                String json = gson.toJson(budgets);
                editor.putString("teaching budgets", json);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), Overview.class);
                startActivity(intent);
            }
        });
    }

    public void displayInfo() {
        Point p = new Point();
        int[] location = new int[2];
        scenarioId.getLocationOnScreen(location);
        p.x = location[0];
        p.y = location[1];
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.info_popup,null);
        TextView textView = (TextView) customView.findViewById(R.id.tv);
        textView.setText("ID: " + id);
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(
                customView,
                400,
                300
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY,p.x,p.y-250);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Homework.this, ScenarioActivity.class));
    }
}
