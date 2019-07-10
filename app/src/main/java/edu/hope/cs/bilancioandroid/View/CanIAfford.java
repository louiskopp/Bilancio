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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.hope.cs.bilancioandroid.Controller.CustomEditText;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.R;

public class CanIAfford extends AppCompatActivity {

    private Date date;
    private String dateString;
    private String category;
    private String location;
    private Double amountEntered;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    private String[] currentCategories;
    private ArrayAdapter<String> adapter;
    private int choice;
    private Spinner spinner;
    private String needGo;
    private boolean fixArray = false;
    private CustomEditText amount;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Date today = new Date();
    String now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int monthInt = calendar.get(Calendar.MONTH);
        String monthStr = new DateFormatSymbols().getMonths()[monthInt];
        now = monthStr+ " " +calendar.get(Calendar.DAY_OF_MONTH)+", "+calendar.get(Calendar.YEAR);
        dateString = now;
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        final int size = prefs.getInt("Text Size", 18);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_can_iafford);
        TextView locationText = findViewById(R.id.textView10);
        locationText.setTextSize(size);
        TextView categoryText = findViewById(R.id.textView8);
        categoryText.setTextSize(size);
        TextView cancel = (TextView) findViewById(R.id.cancel_afford_one);
        cancel.setTextSize(size);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        needGo = getIntent().getStringExtra("Overview");
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Budget> budgets;
                if (prefs.getString("Teaching Mode", "Off").equals("On")) {
                    budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                }
                else {
                    budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                }
                currentCategories = new String[budgets.size()-1];
                for(int i=0; i<budgets.size();i++){
                    if(budgets.get(i).getCategory().equals("Extra Money")){
                        fixArray=true;
                    }
                    else if(!fixArray){
                        currentCategories[i] = budgets.get(i).getCategory();
                    }
                    else{
                        currentCategories[i-1] = budgets.get(i).getCategory();
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
        TextView dateText = findViewById(R.id.textView6);
        dateText.setTextSize(size);
        TextView text = (TextView) findViewById(R.id.date_afford);
        text.setTextSize(size);
        text.setText(now);
        int spinnerText;
        if(size == 14) {
            spinnerText = R.layout.spinner_small;
        } else if(size == 18) {
            spinnerText = R.layout.spinner_medium;
        } else {
            spinnerText = R.layout.spinner_large;
        }
        spinner = (Spinner) findViewById(R.id.select_time);
        adapter = new ArrayAdapter<String>(this,
                 spinnerText, currentCategories);
        adapter.setDropDownViewResource(spinnerText);
        spinner.setAdapter(adapter);
        TextView next = (TextView) findViewById(R.id.next_afford);
        next.setTextSize(size);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = spinner.getSelectedItemPosition();
                category = adapter.getItem(choice);
                if(dateString==null||category==null||location==null||amount.getText().toString().equals("")){
                    final AlertDialog.Builder a_builder1 = new AlertDialog.Builder(CanIAfford.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder1.setMessage("You need to fill all options in order to proceed!").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert1 = a_builder1.create();
                    alert1.setTitle("Need more Info!");
                    alert1.show();
                }
                else {
                    Intent nextIntent = new Intent(getApplicationContext(), CanIAffordPart2.class);
                    nextIntent.putExtra("date", dateString);
                    nextIntent.putExtra("category", category);
                    nextIntent.putExtra("location", location);
                    nextIntent.putExtra("Overview", needGo);
                    String placeholder = Double.toString(amountEntered);
                    nextIntent.putExtra("amountEntered", placeholder);
                    startActivity(nextIntent);
                }
            }
        });
        amount = (CustomEditText) findViewById(R.id.enter_amount);
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String entry = amount.getText().toString().trim();
                if(entry.equals("")){}
                else {
                    amountEntered = Double.parseDouble(entry.replaceAll("[^\\d.]", ""));
                    DecimalFormat df = new DecimalFormat("0.00");
                    entry = df.format(amountEntered);
                    entry = "$" + entry;
                    amount.setText(entry);
                }
            }
        });
        amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE|| actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String entry = amount.getText().toString().trim();
                    if(entry.equals("")){}
                    else {
                        amountEntered = Double.parseDouble(entry.replaceAll("[^\\d.]", ""));
                        DecimalFormat df = new DecimalFormat("0.00");
                        entry = df.format(amountEntered);
                        entry = "$" + entry;
                        amount.setText(entry);
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        final EditText loc = (EditText) findViewById(R.id.location_store_name);
        loc.setTextSize(size);
        loc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE|| actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    location = loc.getText().toString().trim();
                    if(location.equals("")){}
                    else {
                        loc.setText(location);
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        loc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                location = s.toString();
            }
        });
    }


    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
