package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.R;
import edu.hope.cs.bilancioandroid.Model.Transaction;

public class IndividualBudgetScreen extends AppCompatActivity {

    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    private TextView categoryIndividual;
    private TextView leftIndividual;
    private TextView descriptionIndividual;
    private TextView image;
    private ListView listView;
    private ArrayList<Budget> budgets;
    private Double totalFunds;
    private Double expenses = 0.0;
    private Converters converters;
    private Double howMuchLeft;
    private Budget budget;
    private String cat;
    private Resources res;
    private TransactionAdapter transactionAdapter;
    private Context context;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), context.MODE_PRIVATE);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_individual_budget_screen);
        context = getApplicationContext();
        res = getResources();
        cat = getIntent().getStringExtra("category");
        categoryIndividual = (TextView) findViewById(R.id.title_individual);
        leftIndividual = (TextView) findViewById(R.id.left_individual);
        descriptionIndividual = (TextView) findViewById(R.id.description_individual);
        image = (TextView) findViewById(R.id.image_individual);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                if(prefs.getString("Teaching Mode", "Off").equals("On")){
                    budget = tDb.teachingModeDao().findByCategory(cat);
                }
                else {
                    budget = db.budgetDao().findByCategory(cat);
                }
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        totalFunds = budget.getAmount();
            if(budget.getTransactions()!=null){
                String tr = budget.getTransactions();
                ArrayList<Transaction> tra = converters.fromString(tr);
                if(tra!=null) {
                    if(budget.getCategory().equals("Extra Money")){
                    }
                    else {
                        for (Transaction transaction1 : tra) {
                            expenses += transaction1.getAmount();
                        }
                    }
                }
            }
        howMuchLeft = totalFunds;
        DecimalFormat df = new DecimalFormat("0.00");
        if(howMuchLeft==0.0){
            if (budget.getHowMuchOver() == null) {
                leftIndividual.setText("$0 left");
            }
            else {
                leftIndividual.setText(budget.getHowMuchOver() + " over");
                leftIndividual.setTextColor(Color.RED);
            }
        }
        else {
            String inc = df.format(howMuchLeft);
            inc = "$" + inc;
            leftIndividual.setText(inc + " left");
        }
        categoryIndividual.setText("Category: " + cat);
        descriptionIndividual.setText(budget.getDescription());
        listView = (ListView) findViewById(R.id.listview_individual);
        transactionAdapter = new TransactionAdapter(this, budget);
        listView.setAdapter(transactionAdapter);
        image.setBackground(res.getDrawable(budget.getImage()));
        Button button = (Button) findViewById(R.id.add_new_transaction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(),AddIncome.class);
                nextIntent.putExtra("category", budget.getCategory());
                startActivity(nextIntent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(IndividualBudgetScreen.this, Overview.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        recreate();
    }
}
