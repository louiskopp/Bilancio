package edu.hope.cs.bilancioandroid.View;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
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
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.hope.cs.bilancioandroid.Controller.CustomEditText;
import edu.hope.cs.bilancioandroid.Controller.MyDatePickerFragment;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

import static edu.hope.cs.bilancioandroid.View.CanIAfford.parseDate;

public class AddIncome extends AppCompatActivity {

    private Double amountEntered;
    private String dateString;
    private Date date;
    private String description;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    private Converters converters;
    private String needGo;
    private String whatIsBeingAdded;
    private ArrayAdapter<String> adapter;
    private String[] currentCategories;
    private TextView categoryView;
    private Spinner addIncomeSpinner;
    private PopupWindow popupWindow;
    private ConstraintLayout constraintLayout;
    private Budget newBudgetBeingUpdated;
    private Budget oldBudgetBeingUpdated;
    private boolean needPopup = false;
    private boolean needWorstPopup = false;
    private boolean fixArray = false;
    private boolean focus = false;
    private String number;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    CustomEditText amount;
    private double total = 0.0;
    private double threshold;
    Date today = new Date();
    String now;
    boolean inMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int monthInt = calendar.get(Calendar.MONTH);
        String monthStr = new DateFormatSymbols().getMonths()[monthInt];
        now = monthStr+ " " +calendar.get(Calendar.DAY_OF_MONTH)+", "+calendar.get(Calendar.YEAR);
        dateString = now;
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        int size = prefs.getInt("Text Size", 20);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
            inMode = true;
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_add_income);
        threshold = Double.longBitsToDouble(prefs.getLong("threshold", 0));
        number = prefs.getString("Phone", "");
        number = "tel:" + number;
        constraintLayout = findViewById(R.id.add_income_layout);
        needGo = getIntent().getStringExtra("Overview");
        if (needGo == null) {
            needGo = "";
        }
        whatIsBeingAdded = getIntent().getStringExtra("category");
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        TextView wordDesc = findViewById(R.id.income_description);
        wordDesc.setTextSize(size);
        TextView wordCategory = findViewById(R.id.textView8);
        wordCategory.setTextSize(size);
        TextView wordDate = findViewById(R.id.textView6);
        wordDate.setTextSize(size);
        TextView cancel = (TextView) findViewById(R.id.cancel_income);
        cancel.setTextSize(size);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        amount = (CustomEditText) findViewById(R.id.income_amount);
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (focus) {
                    String placeholder = amount.getText().toString().trim().replaceAll("[^\\d.]", "");
                    if (placeholder.equals("")) {
                        focus = false;
                    } else {
                        amountEntered = Double.parseDouble(placeholder);
                        DecimalFormat df = new DecimalFormat("0.00");
                        placeholder = df.format(amountEntered);
                        placeholder = "$" + placeholder;
                        amount.setText(placeholder);
                        focus = false;
                    }
                } else {
                    focus = true;
                }
            }
        });
        amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String entry = amount.getText().toString().trim();
                    if (entry.equals("")) {
                    } else {
                        amountEntered = Double.parseDouble(entry.replaceAll("[^\\d.]", ""));
                        DecimalFormat df = new DecimalFormat("0.00");
                        entry = df.format(amountEntered);
                        entry = "$" + entry;
                        amount.setText(entry);
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        final EditText desc = (EditText) findViewById(R.id.income_edit_description);
        desc.setTextSize(size);
        desc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    description = desc.getText().toString().trim();
                    if (description.equals("")) {
                    } else {
                        desc.setText(description);
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                description = s.toString();
            }
        });
        TextView save = (TextView) findViewById(R.id.save_income);
        save.setTextSize(size);
        categoryView = (TextView) findViewById(R.id.category_add_income_expense);
        categoryView.setTextSize(size);
        addIncomeSpinner = (Spinner) findViewById(R.id.select_possible_category);
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Budget> budgets;
                if(prefs.getString("Teaching Mode", "Off").equals("On")){
                    budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                }
                else {
                    budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                }
                if (budgets.size() != 0) {
                    currentCategories = new String[budgets.size() - 1];
                    for (int i = 0; i < budgets.size(); i++) {
                        if (budgets.get(i).getCategory().equals("Extra Money")) {
                            fixArray = true;
                        } else if (!fixArray) {
                            currentCategories[i] = budgets.get(i).getCategory();
                        } else {
                            currentCategories[i - 1] = budgets.get(i).getCategory();
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
        TextView text = (TextView) findViewById(R.id.income_date);
        text.setTextSize(size);
        text.setText(dateString);
        int spinner;
        if(size == 14) {
            spinner = R.layout.spinner_small;
        } else if(size == 18) {
            spinner = R.layout.spinner_medium;
        } else {
            spinner = R.layout.spinner_large;
        }
        adapter = new ArrayAdapter<String>(this,
                spinner, currentCategories);
        adapter.setDropDownViewResource(spinner);
        addIncomeSpinner.setAdapter(adapter);
        if ((whatIsBeingAdded != null)) {
            categoryView.setAlpha(1.0f);
            addIncomeSpinner.setVisibility(View.GONE);
            categoryView.setText(whatIsBeingAdded);
            if (whatIsBeingAdded.equals("Income") || whatIsBeingAdded.equals("Extra Money")) {
                desc.setHint("e.g. Paycheck, gift money");
                categoryView.setText("Income");
            }
        } else {
            categoryView.setAlpha(0.0f);
            addIncomeSpinner.setAlpha(1.0f);
        }

        //Edit the budgets to incorporate the amount added
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateString == null || description == null || amount.getText().toString().equals("")) {
                    final AlertDialog.Builder a_builder1 = new AlertDialog.Builder(AddIncome.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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
                } else {
                    Thread go = new Thread(new Runnable() {
                        Budget budget;

                        //Determine which database to use
                        @Override
                        public void run() {
                            if ((whatIsBeingAdded != null)) {
                                if (whatIsBeingAdded.equals("Income")) {
                                    if(prefs.getString("Teaching Mode", "Off").equals("On")){
                                        budget = tDb.teachingModeDao().findByCategory("Extra Money");
                                    }
                                    else {
                                        budget = db.budgetDao().findByCategory("Extra Money");
                                    }
                                } else {
                                    if(prefs.getString("Teaching Mode", "Off").equals("On")){
                                        budget = tDb.teachingModeDao().findByCategory(whatIsBeingAdded);
                                    }
                                    else {
                                        budget = db.budgetDao().findByCategory(whatIsBeingAdded);
                                    }
                                }
                            } else {
                                int choice = addIncomeSpinner.getSelectedItemPosition();
                                whatIsBeingAdded = adapter.getItem(choice);
                                if(prefs.getString("Teaching Mode", "Off").equals("On")){
                                    budget = tDb.teachingModeDao().findByCategory(whatIsBeingAdded);
                                }
                                else {
                                    budget = db.budgetDao().findByCategory(whatIsBeingAdded);
                                }
                            }
                            Budget placeHolder = budget;
                            String transactionStr = budget.getTransactions();
                            ArrayList<Transaction> transactionArrayList = converters.fromString(transactionStr);
                            if (transactionArrayList == null) {
                                transactionArrayList = new ArrayList<Transaction>();
                            }
                            if (budget.getCategory().equals("Extra Money")) {
                                transactionArrayList.add(new Transaction(amountEntered, description, whatIsBeingAdded, dateString));
                            } else {
                                transactionArrayList.add(new Transaction(-amountEntered, description, whatIsBeingAdded, dateString));
                            }
                            transactionStr = converters.fromTransactions(transactionArrayList);
                            budget.setTransactions(transactionStr);
                            if (budget.getCategory().equals("Extra Money")) {
                                budget.setAmount(budget.getAmount() + amountEntered);
                                if(prefs.getString("Teaching Mode", "Off").equals("On")){
                                    tDb.teachingModeDao().delete(placeHolder);
                                    tDb.teachingModeDao().insertAll(budget);
                                }
                                else {
                                    db.budgetDao().delete(placeHolder);
                                    db.budgetDao().insertAll(budget);
                                }
                            } else {
                                if(prefs.getString("Teaching Mode", "Off").equals("On")){
                                    if (budget.getAmount() - amountEntered >= 0) {
                                        budget.setAmount(budget.getAmount() - amountEntered);
                                        if(prefs.getString("Teaching Mode", "Off").equals("On")){
                                            tDb.teachingModeDao().delete(placeHolder);
                                            tDb.teachingModeDao().insertAll(budget);
                                        }
                                        else {
                                            db.budgetDao().delete(placeHolder);
                                            db.budgetDao().insertAll(budget);
                                        }
                                    }
                                    else if(tDb.teachingModeDao().findByCategory("Extra Money").getAmount() - (amountEntered - budget.getAmount()) >= 0 && inMode){
                                        newBudgetBeingUpdated = budget;
                                        oldBudgetBeingUpdated = placeHolder;
                                        needPopup = true;
                                    }
                                    else {
                                        needWorstPopup = true;
                                    }
                                }
                                else{
                                    if (budget.getAmount() - amountEntered >= 0) {
                                        budget.setAmount(budget.getAmount() - amountEntered);
                                        if(prefs.getString("Teaching Mode", "Off").equals("On")){
                                            tDb.teachingModeDao().delete(placeHolder);
                                            tDb.teachingModeDao().insertAll(budget);
                                        }
                                        else {
                                            db.budgetDao().delete(placeHolder);
                                            db.budgetDao().insertAll(budget);
                                        }
                                    } else if (db.budgetDao().findByCategory("Extra Money").getAmount() - (amountEntered - budget.getAmount()) >= 0 && !inMode) {
                                        newBudgetBeingUpdated = budget;
                                        oldBudgetBeingUpdated = placeHolder;
                                        needPopup = true;
                                    }
                                    else {
                                        needWorstPopup = true;
                                    }
                                }

                            }
                            ArrayList<Budget> budgets;
                            if(prefs.getString("Teaching Mode", "Off").equals("On")){
                                budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                            }
                            else {
                                budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                            }
                            for (Budget budget1 : budgets) {
                                total += budget1.getAmount();
                            }
                        }
                    });
                    go.start();
                    try {
                        go.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (needPopup) {
                        final AlertDialog.Builder a_builder = new AlertDialog.Builder(AddIncome.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        a_builder.setMessage("You'll have to take money from extra money fund.").setCancelable(false)
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Thread go = new Thread(new Runnable() {

                                            @Override
                                            public void run() {
                                                Budget extra;
                                                if (prefs.getString("Teaching Mode", "Off").equals("On")) {
                                                    extra = tDb.teachingModeDao().findByCategory("Extra Money");
                                                } else {
                                                    extra = db.budgetDao().findByCategory("Extra Money");
                                                }
                                                Budget secondPlaceholder = extra;
                                                Double howMuchExtra = amountEntered - newBudgetBeingUpdated.getAmount();
                                                extra.setAmount(extra.getAmount() - howMuchExtra);
                                                newBudgetBeingUpdated.setAmount(0.0);
                                                String tra = extra.getTransactions();
                                                ArrayList<Transaction> tra2 = converters.fromString(tra);
                                                if (tra2 == null) {
                                                    tra2 = new ArrayList<Transaction>();
                                                }
                                                tra2.add(new Transaction(-howMuchExtra, description, whatIsBeingAdded, dateString));
                                                tra = converters.fromTransactions(tra2);
                                                extra.setTransactions(tra);
                                                DecimalFormat df = new DecimalFormat("0.00");
                                                String entry = df.format(howMuchExtra);
                                                entry = "$" + entry;
                                                newBudgetBeingUpdated.setHowMuchOver(entry);
                                                if (prefs.getString("Teaching Mode", "Off").equals("On")) {
                                                    tDb.teachingModeDao().delete(oldBudgetBeingUpdated);
                                                    tDb.teachingModeDao().delete(secondPlaceholder);
                                                    tDb.teachingModeDao().insertAll(newBudgetBeingUpdated, extra);
                                                } else {
                                                    db.budgetDao().delete(oldBudgetBeingUpdated);
                                                    db.budgetDao().delete(secondPlaceholder);
                                                    db.budgetDao().insertAll(newBudgetBeingUpdated, extra);
                                                }
                                            }
                                        });
                                        go.start();
                                        try {
                                            go.join();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if (total < threshold) {
                                            final AlertDialog.Builder a_builder = new AlertDialog.Builder(AddIncome.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                                            a_builder.setMessage("This purchase has caused you to draw your budget beyond the set threshold. Would you like to call your contact?").setCancelable(false)
                                                    .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                                            } else {
                                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                                callIntent.setData(Uri.parse(number));
                                                                startActivity(callIntent);
                                                                finish();
                                                                return;
                                                            }
                                                        }
                                                    })
                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            finish();
                                                            return;
                                                        }
                                                    });
                                            AlertDialog alert = a_builder.create();
                                            alert.setTitle("Threshold Reached");
                                            alert.show();
                                            return;
                                        }
                                        if (needGo.equals("overview")) {
                                            Intent nextIntent = new Intent(getApplicationContext(), Overview.class);
                                            startActivity(nextIntent);
                                        } else if (needGo.equals("wallet")) {
                                            Intent nextIntent = new Intent(getApplicationContext(), WalletActivity.class);
                                            startActivity(nextIntent);
                                        } else {
                                            Intent nextIntent = new Intent(getApplicationContext(), IndividualBudgetScreen.class);
                                            nextIntent.putExtra("category", whatIsBeingAdded);
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
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("You dont have enough money in your budget!");
                        alert.show();
                        return;
                    }
                    if (needWorstPopup) {
                        final AlertDialog.Builder a_builder = new AlertDialog.Builder(AddIncome.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        a_builder.setMessage("You do not have enough money in this budget or extra money!").setCancelable(false)
                                .setPositiveButton("Call for Help", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                                            }
                                                            else {
                                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                                callIntent.setData(Uri.parse(number));
                                                                startActivity(callIntent);
                                                                finish();
                                                                return;
                                                            }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("PLEASE DO NOT BUY THIS");
                        alert.show();

                        return;
                    }
                    if (total < threshold) {
                        final AlertDialog.Builder a_builder = new AlertDialog.Builder(AddIncome.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        a_builder.setMessage("This purchase has caused you to draw your budget beyond the set threshold. Would you like to call your contact?").setCancelable(false)
                                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                        }
                                        else {
                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                            callIntent.setData(Uri.parse(number));
                                            startActivity(callIntent);
                                            finish();
                                            return;
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        finish();
                                        return;
                                    }
                                });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Threshold Reached");
                        alert.show();
                        return;
                    }
                    if (needGo.equals("overview")) {
                        Intent nextIntent = new Intent(getApplicationContext(), Overview.class);
                        startActivity(nextIntent);
                    }
                    else if(needGo.equals("wallet")){
                        Intent nextIntent = new Intent(getApplicationContext(), WalletActivity.class);
                        startActivity(nextIntent);
                    }
                    else {
                        Intent nextIntent = new Intent(getApplicationContext(), IndividualBudgetScreen.class);
                        nextIntent.putExtra("category", whatIsBeingAdded);
                        startActivity(nextIntent);
                    }
                }
            }
        });
    }

    public void showDatePicker(View v) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment(this);
        newFragment.show(getSupportFragmentManager(), "date picker");
    }
        public void makeIncomeDate(String theDate){
            dateString = theDate;
            this.date = parseDate(theDate);
            TextView text = (TextView) findViewById(R.id.income_date);
            text.setText(dateString);
        }

}
