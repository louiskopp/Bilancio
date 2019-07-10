package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

import edu.hope.cs.bilancioandroid.Controller.CustomEditText;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.R;

public class CreateScenario extends AppCompatActivity {

    TextView balance;
    CustomEditText amount;
    Spinner categories;
    String category;
    String id;
    String last;
    String time;
    String income;
    String upOrDown;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> disabledAdapter;
    String typeish;
    String type;
    Button beginning;
    Button middle;
    Button end;
    Button incomeBtn;
    Button expenseBtn;
    Button increaseBtn;
    Button decreaseBtn;
    Budget budget;
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TeachingMode);
        setContentView(R.layout.activity_create_scenario);
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        final SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        int size = prefs.getInt("Text Size", 18);
        typeish = prefs.getString("Type", "");
        TextView timeFrame = findViewById(R.id.textView38);
        timeFrame.setTextSize(size);
        TextView amountText = (TextView) findViewById(R.id.text_amount_create);
        amountText.setTextSize(size);
        TextView budgetText = (TextView) findViewById(R.id.text_budget_create);
        budgetText.setTextSize(size);
        final TextView balanceText = (TextView) findViewById(R.id.text_balance_create);
        balanceText.setTextSize(size);
        TextView incomeText = (TextView) findViewById(R.id.text_income_create);
        incomeText.setTextSize(size);
        TextView increase = (TextView) findViewById(R.id.text_increase_create);
        increase.setTextSize(size);
        TextView done = (TextView) findViewById(R.id.done_create);
        done.setTextSize(size);

        balance = (TextView) findViewById(R.id.balance_create);
        balance.setTextSize(size);
        beginning = (Button) findViewById(R.id.beginning);
        beginning.setTextSize(size);
        middle = (Button) findViewById(R.id.middle);
        middle.setTextSize(size);
        end = (Button) findViewById(R.id.end);
        end.setTextSize(size);
        incomeBtn = (Button) findViewById(R.id.incomeBtn);
        incomeBtn.setTextSize(size);
        expenseBtn = (Button) findViewById(R.id.expenseBtn);
        expenseBtn.setTextSize(size);
        increaseBtn = (Button) findViewById(R.id.increaseBtn);
        increaseBtn.setTextSize(size);
        decreaseBtn = (Button) findViewById(R.id.decreaseBtn);
        decreaseBtn.setTextSize(size);
        categories = (Spinner) findViewById(R.id.spinner_create);
        amount = (CustomEditText) findViewById(R.id.amount_edit_create);
        amount.setTextSize(size);
        int spinner;
        if(size == 14) {
            spinner = R.layout.spinner_small;
        } else if(size == 18) {
            spinner = R.layout.spinner_medium;
        } else {
            spinner = R.layout.spinner_large;
        }
        int disabled;
        if(size == 14) {
            disabled = R.layout.disabled_spinner_small;
        } else if(size == 18) {
            disabled = R.layout.disabled_spinner_medium;
        } else {
            disabled = R.layout.disabled_spinner_large;
        }
        //set the array of default budget categories
        String[] currentCategories = new String[10];
        currentCategories[0]="Clothing";
        currentCategories[1]="Education";
        currentCategories[2]="Entertainment";
        currentCategories[3]="Food";
        currentCategories[4]="Home Care";
        currentCategories[5]="Personal Care";
        currentCategories[6]="Phone";
        currentCategories[7]="Rent";
        currentCategories[8]="Transportation";
        currentCategories[9]="Utilities";
        adapter = new ArrayAdapter<String>(this,
                spinner, currentCategories);
        disabledAdapter = new ArrayAdapter<String>(this,
                disabled, currentCategories);
        adapter.setDropDownViewResource(spinner);
        disabledAdapter.setDropDownViewResource(disabled);
        beginning.setTextColor(Color.GRAY);
        beginning.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
        time = "0";
        beginning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginning.setTextColor(Color.GRAY);
                beginning.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
                time = "0";
                middle.setTextColor(getResources().getColor(R.color.colorAccent1));
                middle.setBackgroundColor(Color.GRAY);
                end.setTextColor(getResources().getColor(R.color.colorAccent1));
                end.setBackgroundColor(Color.GRAY);
                Thread go = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        budget = tDb.teachingModeDao().findByCategory(category);
                    }
                });
                go.start();
                try {
                    go.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double current = budget.getAmount();
                DecimalFormat df = new DecimalFormat("0.00");
                String entry = df.format(current);
                entry = "$" + entry + " left";
                balance.setText(entry);
            }
        });
        middle.setTextColor(getResources().getColor(R.color.colorAccent1));
        middle.setBackgroundColor(Color.GRAY);
        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                middle.setTextColor(Color.GRAY);
                middle.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
                time = "1";
                beginning.setTextColor(getResources().getColor(R.color.colorAccent1));
                beginning.setBackgroundColor(Color.GRAY);
                end.setTextColor(getResources().getColor(R.color.colorAccent1));
                end.setBackgroundColor(Color.GRAY);
                Thread go = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        budget = tDb.teachingModeDao().findByCategory(category);
                    }
                });
                go.start();
                try {
                    go.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double current = budget.getAmount();
                DecimalFormat df = new DecimalFormat("0.00");
                String entry = df.format(current/2);
                entry = "$" + entry + " left";
                balance.setText(entry);
            }
        });
        end.setTextColor(getResources().getColor(R.color.colorAccent1));
        end.setBackgroundColor(Color.GRAY);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end.setTextColor(Color.GRAY);
                end.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
                time = "2";
                middle.setTextColor(getResources().getColor(R.color.colorAccent1));
                middle.setBackgroundColor(Color.GRAY);
                beginning.setTextColor(getResources().getColor(R.color.colorAccent1));
                beginning.setBackgroundColor(Color.GRAY);
                Thread go = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        budget = tDb.teachingModeDao().findByCategory(category);
                    }
                });
                go.start();
                try {
                    go.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double current = budget.getAmount();
                DecimalFormat df = new DecimalFormat("0.00");
                String entry = df.format(current/8);
                entry = "$" + entry + " left";
                balance.setText(entry);
            }
        });
        incomeBtn.setTextColor(Color.GRAY);
        incomeBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
        income = "income";
        categories.setAdapter(disabledAdapter);
        categories.setEnabled(false);
        balance.setTextColor(Color.LTGRAY);
        incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeBtn.setTextColor(Color.GRAY);
                incomeBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
                income = "income";
                balance.setTextColor(Color.LTGRAY);
                categories.setEnabled(false);
                categories.setAdapter(disabledAdapter);
                expenseBtn.setTextColor(getResources().getColor(R.color.colorAccent1));
                expenseBtn.setBackgroundColor(Color.GRAY);
            }
        });
        expenseBtn.setTextColor(getResources().getColor(R.color.colorAccent1));
        expenseBtn.setBackgroundColor(Color.GRAY);
        expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseBtn.setTextColor(Color.GRAY);
                expenseBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
                income = "expense";
                balance.setTextColor(Color.WHITE);
                categories.setEnabled(true);
                categories.setAdapter(adapter);
                incomeBtn.setTextColor(getResources().getColor(R.color.colorAccent1));
                incomeBtn.setBackgroundColor(Color.GRAY);
            }
        });
        increaseBtn.setTextColor(Color.GRAY);
        increaseBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
        upOrDown = "increase";
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseBtn.setTextColor(Color.GRAY);
                increaseBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
                upOrDown = "increase";
                decreaseBtn.setTextColor(getResources().getColor(R.color.colorAccent1));
                decreaseBtn.setBackgroundColor(Color.GRAY);
            }
        });
        decreaseBtn.setTextColor(getResources().getColor(R.color.colorAccent1));
        decreaseBtn.setBackgroundColor(Color.GRAY);
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseBtn.setTextColor(Color.GRAY);
                decreaseBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent1));
                upOrDown = "decrease";
                increaseBtn.setTextColor(getResources().getColor(R.color.colorAccent1));
                increaseBtn.setBackgroundColor(Color.GRAY);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last = amount.getText().toString().replaceAll("[^\\d.]", "");
                if (last.equals("")) {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(CreateScenario.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Please enter an amount.").setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Empty Amount");
                    alert.show();
                } else {
                    //set the category number for the ID
                    last = last.replaceAll("\\.", "");
                    switch (category) {
                        case ("Clothing"):
                            category = "0";
                            break;
                        case ("Education"):
                            category = "1";
                            break;
                        case ("Entertainment"):
                            category = "2";
                            break;
                        case ("Food"):
                            category = "3";
                            break;
                        case ("Home Care"):
                            category = "4";
                            break;
                        case ("Personal Care"):
                            category = "5";
                            break;
                        case ("Phone"):
                            category = "6";
                            break;
                        case ("Rent"):
                            category = "7";
                            break;
                        case ("Transportation"):
                            category = "8";
                            break;
                        case ("Utilities"):
                            category = "9";
                            break;
                    }
                    //set up the end of the id based on how large the number is
                    Intent intent = new Intent(getApplicationContext(), Homework.class);
                    switch (last.length()) {
                        case (1):
                            last = "00000" + last;
                            break;
                        case (2):
                            last = "0000" + last;
                            break;
                        case (3):
                            last = "000" + last;
                            break;
                        case (4):
                            last = "00" + last;
                            break;
                        case (5):
                            last = "0" + last;
                            break;
                    }
                    if(type==null){
                        if(typeish.equals("Adjustment")){
                            if(income.equals("income")){
                                if(upOrDown.equals("increase")){
                                    type = "2";
                                }
                                else{
                                    type = "3";
                                }

                            }
                            else{
                                if(upOrDown.equals("increase")){
                                    type = "0";
                                }
                                else{
                                    type = "1";
                                }
                            }
                        }
                        else if(typeish.equals("Transaction")){
                            if(income.equals("income")){
                                type = "5";
                            }
                            else{
                                type = "4";
                            }
                        }
                    }
                    id = time + "." + category + "." + type + "." + last;
                    intent.putExtra("id", id);
                    if(type.equals("6")){
                        double entered = Double.valueOf(last)/100;
                        if(time.equals("2")){
                            entered = entered*8;
                        }
                        else if(time.equals("1")){
                            entered = entered*2;
                        }

                        if(entered>budget.getAmount()){
                            final AlertDialog.Builder a_builder = new AlertDialog.Builder(CreateScenario.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                            a_builder.setMessage("Please enter an amount that's less than or equal to the budgets balance.").setCancelable(false)
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            recreate();
                                        }
                                    });
                            AlertDialog alert = a_builder.create();
                            alert.setTitle("Amount is too high!");
                            alert.show();
                        }
                        else {
                            startActivity(intent);
                        }
                    }
                    else {
                        startActivity(intent);
                    }
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
                        Double amountEntered = Double.parseDouble(entry.replaceAll("[^\\d.]", ""));
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
        switch (typeish){
            case("Adjustment"):
                break;
            case("Prediction"):
                incomeText.setVisibility(View.GONE);
                increase.setVisibility(View.GONE);
                incomeBtn.setVisibility(View.GONE);
                decreaseBtn.setVisibility(View.GONE);
                increaseBtn.setVisibility(View.GONE);
                expenseBtn.setVisibility(View.GONE);
                categories.setEnabled(true);
                balance.setTextColor(Color.WHITE);
                categories.setAdapter(adapter);
                type="6";
                break;
            case("Transaction"):
                increase.setVisibility(View.GONE);
                decreaseBtn.setVisibility(View.GONE);
                increaseBtn.setVisibility(View.GONE);
                break;
            case("How Much"):
                incomeText.setVisibility(View.GONE);
                increase.setVisibility(View.GONE);
                incomeBtn.setVisibility(View.GONE);
                decreaseBtn.setVisibility(View.GONE);
                increaseBtn.setVisibility(View.GONE);
                expenseBtn.setVisibility(View.GONE);
                categories.setEnabled(true);
                balance.setTextColor(Color.WHITE);
                categories.setAdapter(adapter);
                type = "8";
                break;
        }
        int choice = categories.getSelectedItemPosition();
        if(categories.getAdapter()==disabledAdapter){
            category = disabledAdapter.getItem(choice);
        }
        else {
            category = adapter.getItem(choice);
        }
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                budget = tDb.teachingModeDao().findByCategory(category);
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double current = budget.getAmount();
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(current);
        entry = "$" + entry + " left";
        balance.setText(entry);
    }
}
