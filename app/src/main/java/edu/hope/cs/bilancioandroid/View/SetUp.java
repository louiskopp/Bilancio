package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Controller.CustomEditText;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.R;

public class SetUp extends AppCompatActivity {
    SharedPreferences prefs;

    private ListView list;
    private boolean done = false;
    private ArrayList<Double> moneyNum = new ArrayList<Double>();
    private ArrayList<Double> percentNum = new ArrayList<Double>();
    private ArrayList<String> descriptions = new ArrayList<String>() {{
        add("This is your fund of extra money");
        add("Clothes");
        add("Classes, tuition, books, school supplies");
        add("Fun things to do with friends and eating out");
        add("Food and grocery purchases");
        add("Cleaning supplies, batteries, light bulbs");
        add("Hygiene related items");
        add("Phone");
        add("Cost of apartment");
        add("Bus tickets, gas, car insurance, car repairs");
        add("Water, heat/ electricity, trash, recycle, and cable/internet");
    }};
    private ArrayList<String> items = new ArrayList<String>() {{
        add("Extra Money");
        add("Clothing");
        add("Education");
        add("Entertainment");
        add("Food");
        add("Home Care");
        add("Personal Care");
        add("Phone");
        add("Rent");
        add("Transportation");
        add("Utilities");
    }};

    private ArrayList<Integer> drawables = new ArrayList<Integer>(){{
        add(R.drawable.savings_black);
        add(R.drawable.clothing_black);
        add(R.drawable.education_black);
        add(R.drawable.shopping_black);
        add(R.drawable.groceries_black);
        add(R.drawable.home_black);
        add(R.drawable.medical_black);
        add(R.drawable.phone_black);
        add(R.drawable.rent_black);
        add(R.drawable.auto_care_black);
        add(R.drawable.utilities_black);
    }};
    private ArrayList<String> moneys = new ArrayList<String>() {{
        add("$0.00");
        add("$0.00");
        add("$0.00");
        add("$0.00");
        add("$0.00");
        add("$0.00");
        add("$0.00");
        add("$0.00");
        add("$0.00");
        add("$0.00");
        add("$0.00");
    }};
    private ArrayList<String> percents = new ArrayList<String>() {{
        add("0.00%");
        add("0.00%");
        add("0.00%");
        add("0.00%");
        add("0.00%");
        add("0.00%");
        add("0.00%");
        add("0.00%");
        add("0.00%");
        add("0.00%");
        add("0.00%");
    }};
    private String entry;
    private double money;
    private static final String DATABASE_NAME = "budget_db";
    private AppDatabase db;
    private ItemAdapter itemAdapter;
    private Double whole;
    private CustomEditText text;
    private String badEnrty = "";
    private String entry1;
    private boolean focus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        list = (ListView) findViewById(R.id.list_view);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        itemAdapter = new ItemAdapter(this, items, moneys, percents);
        list.setAdapter(itemAdapter);
        TextView goToMain = (TextView) findViewById(R.id.go_to_main);
        text = (CustomEditText) findViewById(R.id.budget);
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(focus) {
                    wasEntered(text.getText().toString());
                    focus = false;
                }
                else{
                    focus = true;
                }
            }
        });
        entry = text.getText().toString().trim();
        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!done&&!(badEnrty.equals(""))){
                    wasEntered(badEnrty);
                }
                else if (entry.equals("")) {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(SetUp.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Please enter a budget amount so the amounts can be set.").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("No Amount Entered!");
                    alert.show();
                }
                else {
                    Thread clear = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.budgetDao().deleteAll();
                        }});
                    clear.start();
                    try {
                        clear.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread go = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < percentNum.size(); i++) {
                                int n = (int) System.currentTimeMillis();
                                Budget budget = new Budget(n, items.get(i), moneyNum.get(i), descriptions.get(i));
                                budget.setAllotted(moneyNum.get(i));
                                budget.setTransactions("");
                                budget.setImage(drawables.get(i));
                                addBudget(db, budget);
                            }
                        }
                    });
                    go.start();
                    try {
                        go.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent nextIntent = new Intent(getApplicationContext(), Overview.class);
                    startActivity(nextIntent);
                }
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    badEnrty = s.toString();
            }
        });


        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE|| actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    entry1 = text.getText().toString().trim();
                    if (entry1.equals("")) {
                    } else {
                        wasEntered(entry1);
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
    }

    public boolean wasEntered(String entry){
        done = true;
        this.entry = entry;
        money = Double.parseDouble(entry.replaceAll("[^\\d.]", ""));
        DecimalFormat df = new DecimalFormat("0.00");
        entry = df.format(money);
        entry = "$" + entry;
            fillBudgets();
            TextView moneyAmount = (TextView) findViewById(R.id.moneyAmount);
            moneyAmount.setText(entry);
            text.setText(entry);
            prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Budget", entry);
            editor.apply();
        return true;
    }


    public void fillBudgets() {
        if (money != 0) {
            if(list.getAdapter().getItem(0)=="0.00%") {
                percents = new ArrayList<String>() {{
                    add("9.00%");
                    add("4.00%");
                    add("8.00%");
                    add("5.00%");
                    add("14.00%");
                    add("4.00%");
                    add("6.00%");
                    add("6.00%");
                    add("24.00%");
                    add("12.00%");
                    add("8.00%");
                }};
            }
            else{
                percents = itemAdapter.getPercent();
            }
            for(int i=0;i<percents.size();i++){
                String now = percents.get(i).replaceAll("[^\\d.]", "");
                Double next = Double.parseDouble(now);
                if(percentNum.size()>i) {
                    percentNum.remove(i);
                }
                percentNum.add(i,next);
            }

            whole = 0.0;
            for(Double num: percentNum){
                whole+=num;
            }

            if(whole <= 100) {

                for (int i = 0; i < percentNum.size(); i++) {
                    Double hold = money * percentNum.get(i) / 100;
                    if(moneyNum.size()>i) {
                        moneyNum.remove(i);
                    }
                    moneyNum.add(i,hold);
                }

                for (int i = 0; i < moneyNum.size(); i++) {
                    Double put = moneyNum.get(i);
                    DecimalFormat df = new DecimalFormat("0.00");
                    String hold = df.format(put);
                    hold = "$" + hold;
                    if(moneys.size()>i) {
                        moneys.remove(i);
                    }
                    moneys.add(i,hold);

                }
                itemAdapter = new ItemAdapter(this, items, moneys, percents);
                list.setAdapter(itemAdapter);
            }
            else{
                final AlertDialog.Builder a_builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                a_builder.setMessage("Please change another if you wish to keep this percentage").setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("The percentages add to over 100%!");
                alert.show();
            }
        }
    }

    private static Budget addBudget(final AppDatabase db, Budget budget){
        db.budgetDao().insertAll(budget);
        return budget;
    }

    public void resetPercents(){
        moneys = itemAdapter.getMoney();

        for(int i=0;i<moneys.size();i++){
            String now = moneys.get(i).replaceAll("[^\\d.]", "");
            Double next = Double.parseDouble(now);
            if(moneyNum.size()>i) {
                moneyNum.remove(i);
            }
            moneyNum.add(i,next);
        }

        whole = 0.0;
        for(Double num: percentNum){
            whole+=num;
        }

        if(whole <= money) {

            for (int i = 0; i < moneyNum.size(); i++) {
                Double hold = moneyNum.get(i)/money*100;
                if(percentNum.size()>i) {
                    percentNum.remove(i);
                }
                percentNum.add(i,hold);
            }

            for (int i = 0; i < percentNum.size(); i++) {
                Double put = percentNum.get(i);
                DecimalFormat df = new DecimalFormat("0.00");
                String hold = df.format(put);
                hold = hold+"%";
                if(percents.size()>i) {
                    percents.remove(i);
                }
                percents.add(i,hold);

            }
            itemAdapter = new ItemAdapter(this, items, moneys, percents);
            list.setAdapter(itemAdapter);
        }
    }
}


