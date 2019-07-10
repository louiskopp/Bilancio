package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.R;

public class EndOfBudgetCycle extends AppCompatActivity {

    String goodStart = "You have ";
    String goodEnd = " remaining at the end of this budget cycle. Pressing continue will transfer your remaining balance to savings or distribute it amongst any active goals. Any remaining funds from previous goals were returned to savings.";
    String bad = "You used all of your money during this budget cycle. Any remaining funds from previous goals were returned to your savings.";
    TextView continueForward;
    TextView message;
    TextView opener;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    Double left = 0.0;
    ArrayList<Budget> budgets;
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
        setContentView(R.layout.activity_end_of_budget_cycle);
        opener = findViewById(R.id.textView9);
        opener.setTextSize(size);
        continueForward = (TextView) findViewById(R.id.continue_cycle);
        continueForward.setTextSize(size);
        message = (TextView) findViewById(R.id.message_new_cycle);
        message.setTextSize(size);
        db = Room.databaseBuilder(this,
                AppDatabase.class, DATABASE_NAME).build();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                db.budgetDao().deleteAll();
                for(Budget budget: budgets){
                    left+= budget.getAmount();
                    budget.setAmount(budget.getAllotted());
                    budget.setTransactions("");
                    db.budgetDao().insertAll(budget);
                }
                editor.putLong("new money", Double.doubleToRawLongBits(left));
                editor.putBoolean("End", true);
                editor.apply();
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(left);
        entry = "$" + entry;
        int color;
        if (prefs.getString("Teaching Mode", "").equals("On")) {
            color = R.style.TeachingMode;
        } else {
            color = prefs.getInt("Theme", R.style.White);
        }
        int[] positive = new int[] {
                Color.GREEN
        };
        int[] regular = new int[] {
                Color.YELLOW
        };
        int[] negative = new int[] {
                Color.RED
        };
        int green = 0;
        int yellow = 0;
        int red = 0;
        switch(color){
            case(R.style.BlueTheme):
                positive = new int[] {
                        Color.parseColor("#A3FFA3")
                };
                green = Color.parseColor("#A3FFA3");
                regular = new int[] {
                        Color.parseColor("#ffe863")
                };
                yellow = Color.parseColor("#ffe863");
                negative = new int[] {
                        Color.parseColor("#ff8282")
                };
                red = Color.parseColor("#ff8282");
                break;
            case(R.style.RedTheme):
                positive = new int[] {
                        Color.parseColor("#94ff2e")
                };
                green = Color.parseColor("#94ff2e");
                regular = new int[] {
                        Color.parseColor("#ffde00")
                };
                yellow =  Color.parseColor("#ffde00");
                negative = new int[] {
                        Color.parseColor("#ff8aba")
                };
                red = Color.parseColor("#ff8aba");
                break;
            case(R.style.GrayTheme):
                positive = new int[] {
                        Color.parseColor("#1c851c")
                };
                green = Color.parseColor("#1c851c");
                regular = new int[] {
                        Color.parseColor("#ffe812")
                };
                yellow = Color.parseColor("#ffe812");
                negative = new int[] {
                        Color.parseColor("#cf3326")
                };
                red =  Color.parseColor("#cf3326");
                break;
            case(R.style.Black):
                positive = new int[] {
                        Color.parseColor("#018901")
                };
                green = Color.parseColor("#018901");
                regular = new int[] {
                        Color.parseColor("#fded3a")
                };
                yellow = Color.parseColor("#fded3a");
                negative = new int[] {
                        Color.parseColor("#ff0000")
                };
                red = Color.parseColor("#ff0000");
                break;
            case(R.style.Colorblind):
                positive = new int[] {
                        getResources().getColor(R.color.colorPrimaryDark)
                };
                green = getResources().getColor(R.color.colorPrimaryDark);
                regular = new int[] {
                        Color.parseColor("#ffa800")
                };
                yellow = Color.parseColor("#ffa800");
                negative = new int[] {
                        Color.parseColor("#ff0000")
                };
                red =  Color.parseColor("#ff0000");
                break;
            case(R.style.White):
                positive = new int[] {
                        Color.parseColor("#018901")
                };
                green =  Color.parseColor("#018901");
                regular = new int[] {
                        Color.parseColor("#ffe600")
                };
                yellow =  Color.parseColor("#ffe600");
                negative = new int[] {
                        Color.parseColor("#ff0000")
                };
                red = Color.parseColor("#ff0000");
                break;
            case(R.style.TeachingMode):
                positive = new int[] {
                        getResources().getColor(R.color.budgetTotalColor)
                };
                green = getResources().getColor(R.color.budgetTotalColor);
                regular = new int[] {
                        getResources().getColor(R.color.colorAccent1)
                };
                yellow = getResources().getColor(R.color.colorAccent1);
                negative = new int[] {
                        Color.parseColor("#ff0000")
                };
                red = Color.parseColor("#ff0000");
                break;
        }

        if(left>0){
            message.setText(goodStart+entry+goodEnd);
            message.setTextColor(green);
        }
        else {
            message.setText(bad);
            message.setTextColor(red);
        }
        continueForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Overview.class);
                intent.putExtra("use popup", "yes");
                startActivity(intent);
            }
        });
    }
}
