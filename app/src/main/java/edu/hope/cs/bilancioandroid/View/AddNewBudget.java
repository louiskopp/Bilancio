package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class AddNewBudget extends AppCompatActivity {

    EditText category;
    String categoryStr;
    EditText description;
    String descriptionStr;
    TextView categoryText;
    TextView change;
    TextView descriptionText;
    TextView recentTransactions;
    TextView cancel;
    ImageView icon;
    Budget budget;
    Budget budgetFrom;
    private AppDatabase db;
    int image;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    Converters converters;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Budget oldBudget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        Type listType = new TypeToken<Budget>() {}.getType();
        String value = prefs.getString("edited budget", "");
        oldBudget =  new Gson().fromJson(value, listType);
        String newValue = getIntent().getStringExtra("budget");
        budgetFrom = new Gson().fromJson(newValue, listType);
        int size = prefs.getInt("Text Size", 18);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_add_new_budget);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        image = getIntent().getIntExtra("icon", R.drawable.savings_black);
        categoryText = findViewById(R.id.textView2);
        categoryText.setTextSize(size);
        category = (EditText) findViewById(R.id.edit_name_new);
        category.setTextSize(size);
        categoryStr = getIntent().getStringExtra("category");
        if(categoryStr!=null){
            category.setText(categoryStr);
        }
        category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                categoryStr = s.toString();
            }
        });
        descriptionText = findViewById(R.id.textView5);
        descriptionText.setTextSize(size);
        description = (EditText) findViewById(R.id.edit_description_new);
        description.setTextSize(size);
        descriptionStr = getIntent().getStringExtra("description");
        if(descriptionStr!=null){
            description.setText(descriptionStr);
        }
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                descriptionStr = s.toString();
            }
        });
        change = (TextView) findViewById(R.id.change_icon_new);
        change.setTextSize(size);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectIcon.class);
                if(categoryStr!=null){
                    intent.putExtra("category", categoryStr);
                }
                if(descriptionStr!=null){
                    intent.putExtra("description", descriptionStr);
                }
                startActivity(intent);
            }
        });
        icon = (ImageView) findViewById(R.id.select_new_icon);
        icon.setImageDrawable(getResources().getDrawable(image));
        TextView save = (TextView) findViewById(R.id.save_add_new_budget);
        save.setTextSize(size);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) System.currentTimeMillis();
                double amount = 0.0;
                budget = new Budget(id, categoryStr, amount, descriptionStr);
                //If a budget is being edited, retain the information
                if(oldBudget!=null){
                    budget.setUid(budgetFrom.getUid());
                    budget.setAmount(budgetFrom.getAmount());
                    budget.setAllotted(budgetFrom.getAllotted());
                    budget.setTransactions(budgetFrom.getTransactions());
                }
                budget.setImage(image);
                Thread go = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                            tDb.teachingModeDao().insertAll(budget);
                        }
                        else {
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
                editor.remove("edited budget");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), EditBudgets.class);
                startActivity(intent);
            }
        });
        recentTransactions = findViewById(R.id.textView39);
        recentTransactions.setTextSize(size);
        cancel = (TextView) findViewById(R.id.cancel_add_budget);
        cancel.setTextSize(size);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditBudgets.class);
                startActivity(intent);
            }
        });

        if(budgetFrom!=null){
            category.setText(budgetFrom.getCategory());
            categoryStr = budgetFrom.getCategory();
            description.setText(budgetFrom.getDescription());
            descriptionStr = budgetFrom.getDescription();
            icon.setImageResource(budgetFrom.getImage());
            image = budgetFrom.getImage();
            AddBudgetAdapter addBudgetAdapter = new AddBudgetAdapter(this, budgetFrom.getTransactions());
            ListView listView = (ListView) findViewById(R.id.edit_transactions);
            listView.setAdapter(addBudgetAdapter);
        }
    }

    //remove a transaction from the list of transactions for a budget
    public void deleteTransaction(final Transaction transaction1) {
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                String tra = oldBudget.getTransactions();
                ArrayList<Transaction> transactions = converters.fromString(tra);
                for(Transaction transaction: transactions){
                    if(transaction.getAmount()==transaction1.getAmount()&&transaction.getStore().equals(transaction1.getStore())&&transaction.getDate().equals(transaction1.getDate())){
                        oldBudget.setAmount(oldBudget.getAmount()-transaction.getAmount());
                        transactions.remove(transaction);
                        break;
                    }
                }
                tra = converters.fromTransactions(transactions);
                oldBudget.setTransactions(tra);
                budget = oldBudget;
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getApplicationContext(), AddNewBudget.class);
        Gson gson = new Gson();
        String json = gson.toJson(budget);
        intent.putExtra("budget",json);
        startActivity(intent);
        finish();
    }
}
