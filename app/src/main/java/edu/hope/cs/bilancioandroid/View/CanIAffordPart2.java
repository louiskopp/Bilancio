package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.GoalDatabase;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Goal;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class CanIAffordPart2 extends AppCompatActivity {
    private Date date;
    private String dateString;
    private String category;
    private String location;
    private Double amountEntered;
    private Double amountLeftInCurrentBudget;
    private String posivite;
    private String negative;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    private PopupWindow popupWindow;
    private Context context;
    private String titleOfGoal;
    private ConstraintLayout constraintLayout;
    private GoalDatabase gDb;
    private Converters converters;
    private String needGo;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), context.MODE_PRIVATE);
        editor = prefs.edit();
        int size = prefs.getInt("Text Size", 18);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_can_iafford_part2);
        context = getApplicationContext();
        constraintLayout = (ConstraintLayout) findViewById(R.id.goal_pop_up) ;
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        gDb = Room.databaseBuilder(getApplicationContext(),
                GoalDatabase.class, "goal_db").build();
        dateString = getIntent().getStringExtra("date");
        category = getIntent().getStringExtra("category");
        location = getIntent().getStringExtra("location");
        needGo = getIntent().getStringExtra("Overview");
        String placeholder = getIntent().getStringExtra("amountEntered");
        amountEntered = Double.parseDouble(placeholder);
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                Budget budget;
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    budget = tDb.teachingModeDao().findByCategory(category);
                }
                else {
                    budget = db.budgetDao().findByCategory(category);
                }
                amountLeftInCurrentBudget = budget.getAmount()-amountEntered;
                if(amountLeftInCurrentBudget>=0){
                    DecimalFormat df = new DecimalFormat("0.00##");
                    String entry = df.format(amountLeftInCurrentBudget);
                    entry = "$"+entry;
                    posivite = entry;
                }
                else{
                    DecimalFormat df = new DecimalFormat("0.00##");
                    String entry = df.format(Math.abs(amountLeftInCurrentBudget));
                    entry = "$"+entry;
                    negative = entry;
                }
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextView doNot = (TextView) findViewById(R.id.dont_buy);
        doNot.setTextSize(size);
        doNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(needGo.equals("overview")){
                    Intent nextIntent = new Intent(getApplicationContext(), Overview.class);
                    startActivity(nextIntent);
                }
                else {
                    Intent nextIntent = new Intent(getApplicationContext(), WalletActivity.class);
                    startActivity(nextIntent);
                }
            }
        });
        TextView header = findViewById(R.id.can_i_affod);
        header.setTextSize(size);
        TextView buyIt = (TextView) findViewById(R.id.buy);
        buyIt.setTextSize(size);
        TextView wordCost = findViewById(R.id.two_amount_entered_place);
        wordCost.setTextSize(size);
        TextView wordCategory = findViewById(R.id.two_category_place);
        wordCategory.setTextSize(size);
        TextView wordDate = findViewById(R.id.two_date_place);
        wordDate.setTextSize(size);
        TextView wordLocation = findViewById(R.id.two_location_place);
        wordLocation.setTextSize(size);
        TextView transDets = findViewById(R.id.t);
        transDets.setTextSize(size);
        TextView costTextView = (TextView) findViewById(R.id._cost);
        costTextView.setTextSize(size);
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(amountEntered);
        entry = "$"+entry;
        costTextView.setText(entry);
        TextView categoryTextView = (TextView) findViewById(R.id._category);
        categoryTextView.setTextSize(size);
        categoryTextView.setText(category);
        TextView dateTextView = (TextView) findViewById(R.id._date);
        dateTextView.setTextSize(size);
        dateTextView.setText(dateString);
        TextView locationTextView = (TextView) findViewById(R.id._location);
        locationTextView.setTextSize(size);
        locationTextView.setText(location);
        TextView belowTextView = (TextView) findViewById(R.id.message_about_money);
        belowTextView.setTextSize(size);
        TextView yesOrNo = (TextView) findViewById(R.id.yes_or_no);
        if(amountLeftInCurrentBudget>=0){
            yesOrNo.setText("YES");
            yesOrNo.setTextColor(Color.rgb(50,205,50));
            belowTextView.setText("Go ahead! You will still have " + posivite + " left in your " + category + " budget if you buy this.");
            buyIt.setText("BUY IT");
            buyIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread go = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Budget budget;
                            if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                                budget = tDb.teachingModeDao().findByCategory(category);
                            }
                            else {
                                budget = db.budgetDao().findByCategory(category);
                            }
                            Budget placeHolder = budget;
                            budget.setAmount(amountLeftInCurrentBudget);
                            Transaction transaction = new Transaction(-amountEntered, location, category, dateString);
                            String str = budget.getTransactions();
                            ArrayList<Transaction> transactionArrayList = converters.fromString(str);
                            if(transactionArrayList==null){
                                transactionArrayList = new ArrayList<Transaction>();
                            }
                            transactionArrayList.add(transaction);
                            str = converters.fromTransactions(transactionArrayList);
                            budget.setTransactions(str);
                            if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                                tDb.teachingModeDao().delete(placeHolder);
                                tDb.teachingModeDao().insertAll(budget);
                            }
                            else {
                                db.budgetDao().delete(placeHolder);
                                db.budgetDao().insertAll(budget);
                            }
                        }
                    });
                    go.start();
                    try {
                        go.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(needGo.equals("overview")){
                        Intent nextIntent = new Intent(getApplicationContext(), Overview.class);
                        startActivity(nextIntent);
                    }
                    else {
                        Intent nextIntent = new Intent(getApplicationContext(), WalletActivity.class);
                        startActivity(nextIntent);
                    }
                }
            });
        }
        else{
            buyIt.setText("MAKE A GOAL");
            yesOrNo.setText("NO");
            yesOrNo.setTextColor(Color.RED);
            belowTextView.setText("Danger!! You will go over budget for " + category + " by " + negative + " if you make this purchase!");
            buyIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activatePopUp();
                    Thread go = new Thread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    go.start();
                    try {
                        go.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private void activatePopUp() {
        final EditText goalBox = new EditText(this);
        goalBox.setTextColor(Color.BLACK);
        goalBox.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        AlertDialog.Builder a = new AlertDialog.Builder(CanIAffordPart2.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a.setMessage("Please enter a title for your new goal.").setCancelable(false)
                .setPositiveButton("Make goal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText textBox = new EditText(CanIAffordPart2.this);
                        textBox.setTextColor(Color.BLACK);
                        textBox.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                        titleOfGoal = goalBox.getText().toString();
                        if (titleOfGoal == null || titleOfGoal.trim().equals("")) {
                            final AlertDialog.Builder a_builder = new AlertDialog.Builder(CanIAffordPart2.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                            a_builder.setMessage("You need to enter a title for this goal.").setCancelable(false)
                                    .setPositiveButton("Make goal", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            titleOfGoal = textBox.getText().toString();
                                            if (titleOfGoal == null || titleOfGoal.trim().equals("")) {
                                                //if a title is not set the second time around, a default title is set
                                                titleOfGoal = "Goal from Can I afford? (" + location + ")";
                                            }
                                                Thread go = new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Random random = new Random();
                                                        int n = random.nextInt(10000) + 1;
                                                        addGoal(gDb, new Goal(n, titleOfGoal, amountEntered, 0.0));
                                                    }
                                                });
                                                go.start();
                                                try {
                                                    go.join();
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                Intent nextIntent = new Intent(getApplicationContext(), WalletActivity.class);
                                                startActivity(nextIntent);
                                            }
                                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alert = a_builder.create();
                            alert.setTitle("No title found!");
                            alert.setView(textBox);
                            alert.show();
                        } else {
                            Thread go = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Random random = new Random();
                                    int n = random.nextInt(10000) + 1;
                                    addGoal(gDb, new Goal(n, titleOfGoal, amountEntered, 0.0));
                                }
                            });
                            go.start();
                            try {
                                go.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent nextIntent = new Intent(getApplicationContext(), WalletActivity.class);
                            startActivity(nextIntent);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog d = a.create();
                d.setView(goalBox);
                d.setTitle("Add Goal");
                d.show();
    }

    public Goal addGoal(final GoalDatabase gDb, Goal goal){
        gDb.goalDao().insertAll(goal);
        return goal;
    }
}
