package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Random;

import edu.hope.cs.bilancioandroid.Controller.CustomEditText;
import edu.hope.cs.bilancioandroid.Model.Goal;
import edu.hope.cs.bilancioandroid.Database.GoalDatabase;
import edu.hope.cs.bilancioandroid.Controller.MyDatePickerFragment;
import edu.hope.cs.bilancioandroid.R;

public class EditGoal extends AppCompatActivity {

    private String nameOfGoal;
    private double amountTotal = 0.0;
    private double amountSaved;
    private String date;
    private Switch switchDate;
    private TextView dateView;
    private TextView textViewForDate;
    private boolean edit;
    private EditText editTitle;
    private CustomEditText editAmount;
    private GoalDatabase gDb;
    private String originalName;
    private boolean focus = false;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        int size = prefs.getInt("Text Size", 18);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_edit_goal);
        gDb = Room.databaseBuilder(getApplicationContext(),
                GoalDatabase.class, "goal_db").build();
        nameOfGoal = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        amountTotal = getIntent().getDoubleExtra("total",0.0);
        amountSaved = getIntent().getDoubleExtra("saved", 0.0);
        TextView pageTitle = findViewById(R.id.textView16);
        pageTitle.setTextSize(size);
        TextView wordTitle = findViewById(R.id.textView);
        wordTitle.setTextSize(size);
        TextView wordAmount = findViewById(R.id.textView11);
        wordAmount.setTextSize(size);
        TextView wordEndDate = findViewById(R.id.textView13);
        wordEndDate.setTextSize(size);
        dateView = (TextView) findViewById(R.id.new_date_for_goal);
        dateView.setTextSize(size);
        editTitle = (EditText) findViewById(R.id.change_title);
        editTitle.setTextSize(size);
        editAmount = (CustomEditText) findViewById(R.id.change_amount);
        editAmount.setTextSize(size);
        textViewForDate = (TextView) findViewById(R.id.dateText);
        textViewForDate.setTextSize(size);
        switchDate = (Switch) findViewById(R.id.date_switch);
        TextView textView = (TextView) findViewById(R.id.cancel_edit_goal);
        textView.setTextSize(size);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Savings.class);
                startActivity(intent);
            }
        });

        if(nameOfGoal==null){
            switchDate.setChecked(false);
            dateView.setAlpha(0.0f);
            textViewForDate.setAlpha(0.0f);
            edit = false;
        }
        else{
            editTitle.setText(nameOfGoal);
            originalName = nameOfGoal;
            DecimalFormat df = new DecimalFormat("0.00");
            String entry = df.format(amountTotal);
            entry = "$"+entry;
            editAmount.setText(entry);
            edit = true;
            switchDate.setChecked(true);
            if(date!=null){
                dateView.setText(date);
            }

        }

        switchDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(dateView.getAlpha()==0.0f){
                    dateView.setAlpha(1.0f);
                    textViewForDate.setAlpha(1.0f);
                }
                else{
                    dateView.setAlpha(0.0f);
                    textViewForDate.setAlpha(0.0f);
                }
            }
        });
        editAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(focus) {
                    String placeholder = editAmount.getText().toString().trim().replaceAll("[^\\d.]", "");
                    if (placeholder.equals("")) {
                        focus = false;
                    } else {
                        amountTotal = Double.parseDouble(placeholder);
                        DecimalFormat df = new DecimalFormat("0.00");
                        String entry = df.format(amountTotal);
                        entry = "$" + entry;
                        editAmount.setText(entry);
                        focus = false;
                    }
                }
                else{
                    focus = true;
                }
            }
        });
        editAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String placeholder = editAmount.getText().toString().trim().replaceAll("[^\\d.]", "");
                    if(placeholder.equals("")){}
                    else {
                        amountTotal = Double.parseDouble(placeholder);
                        DecimalFormat df = new DecimalFormat("0.00");
                        String entry = df.format(amountTotal);
                        entry = "$" + entry;
                        editAmount.setText(entry);
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        editTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    nameOfGoal = editTitle.getText().toString().trim();
                    if(editTitle.getText().equals("")){}
                    else {
                        editTitle.setText(nameOfGoal);
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nameOfGoal = s.toString();
            }
        });

        TextView newGoal = (TextView) findViewById(R.id.save_new_goal);
        newGoal.setTextSize(size);
        newGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountTotal==0.0||nameOfGoal==null){
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(EditGoal.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("You need to enter all of the information to create or edit this goal.").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Need More Info!");
                    alert.show();
                }
                else {
                    Thread go = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (edit) {
                                Goal goal = gDb.goalDao().findByGoal(originalName);
                                Goal placeholder = goal;
                                goal.setAmountTotal(amountTotal);
                                if(switchDate.isChecked()) {
                                    goal.setDate(date);
                                }
                                goal.setNameOfGoal(nameOfGoal);
                                gDb.goalDao().delete(placeholder);
                                gDb.goalDao().insertAll(goal);
                            } else {
                                Random random = new Random();
                                int n = random.nextInt(10000) + 1;
                                Goal goal = new Goal(n, nameOfGoal, amountTotal, 0.0);
                                if(switchDate.isChecked()) {
                                    goal.setDate(date);
                                }
                                gDb.goalDao().insertAll(goal);
                            }
                        }
                    });
                    go.start();
                    try {
                        go.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), Savings.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void showDatePicker(View v) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment(this);
        newFragment.show(getSupportFragmentManager(), "date picker");
    }

    public void makeIncomeDate(String theDate){
        date = theDate;
        TextView text = (TextView) findViewById(R.id.new_date_for_goal);
        text.setText(date);
    }


}
