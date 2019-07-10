package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class OverallBudgetOverview extends AppCompatActivity {

    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    private ArrayList<Budget> budgets;
    private PopupWindow popupWindow;
    private ConstraintLayout constraintLayout;
    private Converters converters;
    private ArrayList<Transaction> full = new ArrayList<Transaction>();
    private OverallBudgetAdapter overallBudgetAdapter;
    private Context context;
    private ListView listView;
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
        setContentView(R.layout.activity_overall_budget_overview);
        context = getApplicationContext();
        constraintLayout = (ConstraintLayout) findViewById(R.id.overall_layout);
        listView = (ListView) findViewById(R.id.overall_listview);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                }
                else {
                    budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                }
                for(Budget budget: budgets){
                    String trans = budget.getTransactions();
                    ArrayList<Transaction> transactions = converters.fromString(trans);
                    if(transactions!=null) {
                        for (Transaction transaction : transactions) {
                            full.add(transaction);
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
        overallBudgetAdapter = new OverallBudgetAdapter(getApplicationContext(), full);
        TextView emptyText = (TextView) findViewById(R.id.empty_overall);
        listView.setEmptyView(emptyText);
        listView.setAdapter(overallBudgetAdapter);
        if(overallBudgetAdapter==null){
            listView.setVisibility(View.GONE);
        }
        TextView textView = (TextView) findViewById(R.id.sort_by);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPopUp();
            }
        });
    }

    private void sortByPopUp() {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.overall_budget_popup,null);
            TextView cancel = (TextView) customView.findViewById(R.id.cancel_overall_budget);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            TextView sortCategories = (TextView) customView.findViewById(R.id.by_category);
            sortCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Transaction> hold = full;
                    Collections.sort(hold, Transaction.compareCategories());
                    overallBudgetAdapter = new OverallBudgetAdapter(context, (ArrayList<Transaction>) hold);
                    listView.setAdapter(overallBudgetAdapter);
                }
            });
            TextView sortDate = (TextView) customView.findViewById(R.id.by_date);
            sortDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Transaction> hold = full;
                    Collections.sort(hold, Transaction.compareDate());
                    overallBudgetAdapter = new OverallBudgetAdapter(context, (ArrayList<Transaction>) hold);
                    listView.setAdapter(overallBudgetAdapter);
                }
            });
        TextView sortHigh = (TextView) customView.findViewById(R.id.by_high);
        sortHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Transaction> hold = full;
                Collections.sort(hold, Transaction.compareAmount());
                overallBudgetAdapter = new OverallBudgetAdapter(context, (ArrayList<Transaction>) hold);
                listView.setAdapter(overallBudgetAdapter);
            }
        });
        TextView sortLow = (TextView) customView.findViewById(R.id.by_low);
        sortLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Transaction> hold = full;
                Collections.sort(hold, Transaction.compareAmountLow());
                overallBudgetAdapter = new OverallBudgetAdapter(context, (ArrayList<Transaction>) hold);
                listView.setAdapter(overallBudgetAdapter);
            }
        });


            if(popupWindow!=null){
                popupWindow.dismiss();
            }
            popupWindow = new PopupWindow(
                    customView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            // Set an elevation value for popup window
            // Call requires API level 21
            if(Build.VERSION.SDK_INT>=21){
                popupWindow.setElevation(5.0f);
            }

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0,500);
        }

}
