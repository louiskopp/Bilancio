package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.R;

public class ManualSetUp extends AppCompatActivity {

    private ListView list;
    private Context context;
    private PopupWindow popupWindow;
    private ConstraintLayout relativeLayout;
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
    private ArrayList<String> info = new ArrayList<String>() {{
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
    private AppDatabase db;
    private boolean anyMoney = false;
    private boolean hasProperBudgets = true;
    private ItemAdapterManual itemAdapterManual;
    private ArrayList<Budget> budgets;
    private static final String DATABASE_NAME = "budget_db";
    private Double total=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_set_up);
        relativeLayout = findViewById(R.id.rl);
        context = getApplicationContext();
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        list = (ListView) findViewById(R.id.manual_list);
        itemAdapterManual = new ItemAdapterManual(this, info);
        budgets = itemAdapterManual.getMoney();
        list.setAdapter(itemAdapterManual);
        TextView goToMain = (TextView) findViewById(R.id.go_to_main);
        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        for(Budget budget:budgets) {
                            int n = (int) System.currentTimeMillis();
                            budget.setUid(n);
                            if (budget.getAllotted()==0.0) {
                                continue;
                            } else {
                                anyMoney=true;
                                addBudget(db, budget);
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
                if(!anyMoney){
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(ManualSetUp.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Please enter in some amounts to create your budget.").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("No Amounts Entered!");
                    alert.show();
                }
                else if(!hasProperBudgets) {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(ManualSetUp.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("You must have categories in addition than Extra Money in your budget.").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("You need more budget categories!");
                    alert.show();

                } else {
                    DecimalFormat df = new DecimalFormat("0.00");
                    String entry = df.format(total);
                    entry = "$" + entry;
                        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.channelName),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("Budget",entry);
                        editor.apply();
                        Intent nextIntent = new Intent(getApplicationContext(), Overview.class);
                        startActivity(nextIntent);
                    }
                }
        });
    }


    private static Budget addBudget(final AppDatabase db, Budget budget){
        db.budgetDao().insertAll(budget);
        return budget;
    }

    public void displayInfo(Button button, int[] location) {
        Point p = new Point();
        p.x = location[0];
        p.y = location[1];
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.info_popup,null);
        TextView textView = (TextView) customView.findViewById(R.id.tv);
        textView.setText(button.getContentDescription());
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

       popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY,p.x-150,p.y);
    }
}
