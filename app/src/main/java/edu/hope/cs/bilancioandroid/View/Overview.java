package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.hope.cs.bilancioandroid.Controller.NavigationViewHelper;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.GoalDatabase;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Goal;
import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;


public class Overview extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_overview:
                    break;

                case R.id.navigation_wallet:
                    Intent walletIntent = new Intent(getApplicationContext(), WalletActivity.class);
                    startActivity(walletIntent);
                    finish();
                    break;

                case R.id.navigation_savings:
                    Intent savingsIntent = new Intent(getApplicationContext(), Savings.class);
                    startActivity(savingsIntent);
                    finish();
                    break;

                case R.id.navigation_reminders:
                    Intent remindersIntent = new Intent(getApplicationContext(), ReminderActivity.class);
                    startActivity(remindersIntent);
                    finish();
                    break;

            }
            return false;
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener2
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_overview:
                    break;

                case R.id.navigation_wallet:
                    Intent walletIntent = new Intent(getApplicationContext(), WalletActivity.class);
                    startActivity(walletIntent);
                    finish();
                    break;

                case R.id.navigation_savings:
                    Intent savingsIntent = new Intent(getApplicationContext(), Savings.class);
                    startActivity(savingsIntent);
                    finish();
                    break;

                case R.id.navigation_scenarios:
                    Intent scenariosIntent = new Intent(getApplicationContext(), ScenarioActivity.class);
                    startActivity(scenariosIntent);
                    finish();
                    break;

            }
            return false;
        }
    };

    private PopupWindow popupWindow;
    private ConstraintLayout cons;
    private boolean fullGoal = false;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    private ArrayList<Budget> budgets;
    private ArrayList<Budget> teachingBudgets;
    private OverViewAdapter overViewAdapter;
    private Context context;
    private ListView listView;
    private TextView howMuchLeft;
    private ProgressBar totalProgress;
    private Double totalFunds = 0.0;
    private Double expenses = 0.0;
    private Converters converters;
    private TextView totalSpent;
    private TextView overviewBox;
    private GoalDatabase gDb;
    private String usePopUp;
    private ArrayList<Goal> goals;
    private ProgressBar todayBar;
    private String cycle;
    private Date date = new Date();
    private Calendar calendar = Calendar.getInstance();
    private View line;
    private TextView todayText;
    int progress;
    int color;
    TextView scenarioInfo;
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    boolean mAddition = false;
    boolean mSubtract = false;
    boolean mMultiplication = false;
    boolean mDivision = false;
    boolean operator = false;
    Float Value1;
    Float Value2;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private TextView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usePopUp = getIntent().getStringExtra("use popup");
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), context.MODE_PRIVATE);
        editor = prefs.edit();
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        int size = prefs.getInt("Text Size", 18);
        if(prefs.getInt("Theme", R.style.White) == R.style.Colorblind) {
            if (size == 18) {
                setContentView(R.layout.activity_overview_medium);
            } else if (size == 14) {
                setContentView(R.layout.activity_overview_small);
            } else {
                setContentView(R.layout.activity_overview_large);
            }

        } else {
            if (size == 18) {
                setContentView(R.layout.activity_overview_medium_no_check);
            } else if (size == 14) {
                setContentView(R.layout.activity_overview_small_no_check);
            } else {
                setContentView(R.layout.activity_overview_large_no_check);
            }
        }
        BottomNavigationView navi = findViewById(R.id.navigation2);
        final BottomNavigationView nav = findViewById(R.id.navigation);

        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            nav.setVisibility(View.INVISIBLE);
            NavigationViewHelper.disableShiftMode(navi);
            MenuItem item1 = navi.getMenu().getItem(0).setChecked(true);
            navi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener2);
        } else {
            navi.setVisibility(View.GONE);
            NavigationViewHelper.disableShiftMode(nav);
            MenuItem item = nav.getMenu().getItem(0).setChecked(true);
            nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }
        cycle = prefs.getString("Cycle","");
        todayBar = (ProgressBar) findViewById(R.id.today_bar);
        line = (View) findViewById(R.id.today_line);
        todayBar.setScaleY(4f);
        calendar.setTime(date);
        if(cycle.equals("One Week")){
            int q = calendar.get(Calendar.DAY_OF_WEEK);
            Double day = Double.valueOf(q);
            Double prog = day/7*100;
            progress = (int )Math.round(prog);
            todayBar.setProgress(progress);
        }
        else if(cycle.equals("Two Weeks")){
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            if(prefs.getBoolean("second monday", false)){
                day=day+7;
            }
            Double q = Double.valueOf(day);
            Double prog = q/14*100;
            progress = (int )Math.round(prog);
            todayBar.setProgress(progress);
        }
        else{
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            Double q = Double.valueOf(day);
            Double prog = q/31*100;
            progress = (int )Math.round(prog);
            todayBar.setProgress(progress);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        todayText = (TextView) findViewById(R.id.today_text);
        todayText.setTextSize(size);
        todayText.measure(0,0);
        int widthToday = todayText.getMeasuredWidth();
        todayText.setTranslationX(todayBar.getProgress()*width/100-(widthToday/2));
        line.setTranslationX(todayBar.getProgress()*width/100);
        todayBar.setAlpha(0.0f);

        LinearLayout layout = findViewById(R.id.scenario_layout);

        TextView calculator = findViewById(R.id.calculator);
        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorPopUp();
            }
        });

        TextView scenarioFinish = (TextView) findViewById(R.id.scenario_goto);
        scenarioFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = prefs.getString("ID", "");
                Scenario scenario = new Scenario(id);
                String type = scenario.getType();
                if(type.equals("predictiveTrue")||type.equals("predictiveFalse")||type.equals("howMuchIsLeftTransaction")){
                    Intent intent = new Intent(getApplicationContext(), Response.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), Results.class);
                    startActivity(intent);
                }
            }
        });
        scenarioInfo = (TextView) findViewById(R.id.scenario_show);
        scenarioInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScenarioPopUp();
            }
        });

        TextView goToOverallBudget = (TextView) findViewById(R.id.for_to_click);
        goToOverallBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OverallBudgetOverview.class);
                startActivity(intent);
            }
        });

        if(!prefs.getBoolean("Scenario", false)) {
            layout.setVisibility(View.GONE);
            calculator.setVisibility(View.GONE);
            scenarioFinish.setVisibility(View.GONE);
            scenarioInfo.setVisibility(View.GONE);
        }
        context = getApplicationContext();
        if (prefs.getInt("Advanced", 0) == 1) {
            editor.remove("Advanced");
            editor.apply();
        }
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        gDb = Room.databaseBuilder(getApplicationContext(),
                GoalDatabase.class, "goal_db").build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                Budget budget = db.budgetDao().findByCategory("Extra Money");
                for (Budget budget1 : budgets) {
                    if (budget1.getCategory().equals("Extra Money")) {
                        budgets.remove(budget1);
                        break;
                    }
                }
                budgets.add(0, budget);
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread teach = new Thread(new Runnable() {
            @Override
            public void run() {
                teachingBudgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                Budget budget = tDb.teachingModeDao().findByCategory("Extra Money");
                for (Budget budget1 : teachingBudgets) {
                    if (budget1.getCategory().equals("Extra Money")) {
                        teachingBudgets.remove(budget1);
                        break;
                    }
                }
                teachingBudgets.add(0, budget);
            }
        });
        teach.start();
        try {
            teach.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            budgets = teachingBudgets;
        }
        overViewAdapter = new OverViewAdapter(context, budgets, progress);
        listView = (ListView) findViewById(R.id.overview_list);
        listView.setAdapter(overViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), IndividualBudgetScreen.class);
                intent.putExtra("category", budgets.get(position).getCategory());
                startActivity(intent);
            }
        });
        cons = (ConstraintLayout) findViewById(R.id.cons);
        TextView settingsBtn = (TextView) findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Overview.this, Settings.class);
                editor.putString("Last Page", "Overview");
                editor.apply();
                startActivity(settingsIntent);
            }
        });
        overviewBox = findViewById(R.id.textView14);
        howMuchLeft = (TextView) findViewById(R.id.total_left_overview);
        overviewBox.setTextSize(size);
        howMuchLeft.setTextSize(size);

        for (Budget budget : budgets) {
            totalFunds += budget.getAmount();
            if (budget.getTransactions() != null) {
                String tr = budget.getTransactions();
                ArrayList<Transaction> tra = converters.fromString(tr);
                if (tra != null) {
                    if (budget.getCategory().equals("Extra Money")) {
                        continue;
                    } else {
                        for (Transaction transaction1 : tra) {
                            expenses += transaction1.getAmount();
                        }
                    }
                }
            }
        }
        Double muchLeft = totalFunds;
        DecimalFormat df = new DecimalFormat("0.00");
        totalSpent = (TextView) findViewById(R.id.money_in_bar);
        totalSpent.setTextSize(size);

        if(expenses==0.0){
            String exp = df.format(expenses);
            exp = "$" + exp;
            totalSpent.setText(exp);
        }
        else {
            String exp = df.format(-expenses);
            exp = "$" + exp;
            totalSpent.setText(exp);
        }

        String inc = df.format(muchLeft);
        inc = "$" + inc;
        howMuchLeft.setText(inc + " left");
        if(muchLeft==0.0){

        }
        totalProgress = (ProgressBar) findViewById(R.id.total_overview_prog);
        Double n = 100 - (totalFunds + expenses)/(totalFunds) * 100;
        int t = (int) Math.round(n);
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}
        };
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
                        context.getResources().getColor(R.color.colorPrimaryDark)
                };
                green = context.getResources().getColor(R.color.colorPrimaryDark);
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
                        context.getResources().getColor(R.color.budgetTotalColor)
                };
                green = context.getResources().getColor(R.color.budgetTotalColor);
                regular = new int[] {
                        context.getResources().getColor(R.color.colorAccent1)
                };
                yellow = context.getResources().getColor(R.color.colorAccent1);
                negative = new int[] {
                        Color.parseColor("#ff0000")
                };
                red = Color.parseColor("#ff0000");
                break;
        }

        ColorStateList good = new ColorStateList(states, positive);
        ColorStateList okay = new ColorStateList(states, regular);
        ColorStateList bad = new ColorStateList(states, negative);

        if(prefs.getInt("Theme", R.style.White) == R.style.Colorblind) {
            imageView = (TextView) findViewById(R.id.check_overview);
            imageView.setTextSize(size);
        }
        totalProgress.setProgress(t);
        if(n>99.999){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                totalProgress.setProgressTintList(bad);
            }
            else {
                Drawable progressDrawable = totalProgress.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(red, PorterDuff.Mode.SRC_IN);
                totalProgress.setProgressDrawable(progressDrawable);
            }
            if(prefs.getInt("Theme", 0)==R.style.Colorblind){
                imageView.setBackground(context.getResources().getDrawable(R.mipmap.red_x));
            }
        }
        else if(n>progress&&n<99.999){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                totalProgress.setProgressTintList(okay);
            }
            else {
                Drawable progressDrawable = totalProgress.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(yellow, PorterDuff.Mode.SRC_IN);
                totalProgress.setProgressDrawable(progressDrawable);
            }
            if(prefs.getInt("Theme", 0)==R.style.Colorblind){
                imageView.setBackground(context.getResources().getDrawable(R.mipmap.caution));
            }
        }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                totalProgress.setProgressTintList(good);
            }
            else {
                Drawable progressDrawable = totalProgress.getProgressDrawable().mutate();
                progressDrawable.setColorFilter(green, PorterDuff.Mode.SRC_IN);
                totalProgress.setProgressDrawable(progressDrawable);
            }
        }
        if (size == 14) {
            totalProgress.setScaleY(5f);
        } else if (size == 18) {
            totalProgress.setScaleY(7f);
        } else {
            totalProgress.setScaleY(9f);
        }
        final Button anchor = findViewById(R.id.anchor);
        anchor.setVisibility(View.INVISIBLE);
        final Context wrapper = new ContextThemeWrapper(this, R.style.popupMenu);
        final Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(wrapper, anchor, Gravity.CENTER_HORIZONTAL);
                popup.inflate(R.menu.overview_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_income_item:
                                Intent nextIntent = new Intent(getApplicationContext(), AddIncome.class);
                                nextIntent.putExtra("Overview", "overview");
                                nextIntent.putExtra("category", "Income");
                                startActivity(nextIntent);
                                return true;

                            case R.id.can_i_afford:
                                Intent affordIntent = new Intent(getApplicationContext(), CanIAfford.class);
                                affordIntent.putExtra("Overview", "overview");
                                startActivity(affordIntent);
                                return true;

                            case R.id.edit_income_item:
                                Intent expenseIntent = new Intent(getApplicationContext(), AddIncome.class);
                                expenseIntent.putExtra("Overview", "overview");
                                startActivity(expenseIntent);
                                return true;

                            case R.id.cancelButton:
                                return false;
                        }
                        return false;
                    }
                });
            popup.show();
            }
        });


        if (prefs.getString("Overview", "").equals("Off")) {
            totalProgress.setVisibility(View.GONE);
            howMuchLeft.setVisibility(View.GONE);
            overviewBox.setVisibility(View.INVISIBLE);
            totalSpent.setVisibility(View.GONE);
        } else {
            totalProgress.setVisibility(View.VISIBLE);
            howMuchLeft.setVisibility(View.VISIBLE);
            overviewBox.setVisibility(View.VISIBLE);
            totalSpent.setVisibility(View.VISIBLE);
        }
        if(!prefs.getString("Today", "").equals("On")){
            line.setVisibility(View.GONE);
            todayText.setVisibility(View.GONE);
        }
        else{
            line.setVisibility(View.VISIBLE);
            todayText.setVisibility(View.VISIBLE);
        }
        if(usePopUp!=null){
         openGoalPopUp();
        }
    }

    private void openScenarioPopUp() {
        Point p = new Point();
        int[] location = new int[2];
        scenarioInfo.getLocationOnScreen(location);
        p.x = location[0];
        p.y = location[1];
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.info_popup,null);
        TextView textView = (TextView) customView.findViewById(R.id.tv);
        Scenario message = new Scenario(prefs.getString("ID", ""));
        textView.setText(message.getFullStr());
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(
                customView,
                800,
                300
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }

        findViewById(R.id.cons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.cons), Gravity.NO_GRAVITY,p.x,p.y-250);
    }

    private void openGoalPopUp() {
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                goals = (ArrayList<Goal>) gDb.goalDao().getAll();
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int t = goals.size();
        Double v = Double.longBitsToDouble(prefs.getLong("new money", 0));
        v = v / t;
        for (Goal goal : goals) {
            if (goal.getAmountTotal() <= goal.getAmountSaved() + v) {
                fullGoal = true;
            }
        }
        if (!fullGoal) {
            final AlertDialog.Builder a_builder = new AlertDialog.Builder(Overview.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            a_builder.setMessage("A new budget cycle has begun.").setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Budgets have been reset");
            alert.show();
        } else {
            final AlertDialog.Builder a_builder = new AlertDialog.Builder(Overview.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            a_builder.setMessage("One or more of your goals have been met.").setCancelable(false)
                    .setPositiveButton("Use Goal Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            secondPopUp();
                        }
                    })
                    .setNegativeButton("Use Goal Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            editor.putBoolean("later", true);
                            editor.apply();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Goal met!");
            alert.show();

        }
    }

    private void secondPopUp() {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(Overview.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("Funds from your goal are now available in Extra Money.").setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Savings.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Your goal is in use");
        alert.show();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(Overview.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAffinity();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Are you sure you want to exit?");
        alert.show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        recreate();
    }

    public void calculatorPopUp () {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.calculator, null);
       Button btn_0 = (Button) customView.findViewById(R.id.btn_0);
       Button btn_1 = (Button) customView.findViewById(R.id.btn_1);
       Button btn_2 = (Button) customView.findViewById(R.id.btn_2);
       Button btn_3 = (Button) customView.findViewById(R.id.btn_3);
       Button btn_4 = (Button) customView.findViewById(R.id.btn_4);
       Button btn_5 = (Button) customView.findViewById(R.id.btn_5);
       Button btn_6 = (Button) customView.findViewById(R.id.btn_6);
       Button btn_7 = (Button) customView.findViewById(R.id.btn_7);
       Button btn_8 = (Button) customView.findViewById(R.id.btn_8);
       Button btn_9 = (Button) customView.findViewById(R.id.btn_9);
       Button btn_Add = (Button) customView.findViewById(R.id.btn_Add);
       Button btn_Div = (Button) customView.findViewById(R.id.btn_Div);
       Button btn_Sub = (Button) customView.findViewById(R.id.btn_Sub);
       Button btn_Mul = (Button) customView.findViewById(R.id.btn_Mul);
       Button btn_calc = (Button) customView.findViewById(R.id.btn_calc);
       Button btn_dec = (Button) customView.findViewById(R.id.btn_dec);
       Button btn_clear = (Button) customView.findViewById(R.id.btn_clear);
       final EditText ed1 = (EditText) customView.findViewById(R.id.edText1);


        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "0");
                operator = false;
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "1");
                operator = false;
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "2");
                operator = false;
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "3");
                operator = false;
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "4");
                operator = false;
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "5");
                operator = false;
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "6");
                operator = false;
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "7");
                operator = false;
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "8");
                operator = false;
            }
        });

        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "9");
                operator = false;
            }
        });

        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + ".");
                operator = false;
            }
        });

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
                    ed1.setText("0.0");
                }
                    Value1 = Float.parseFloat(ed1.getText() + "");
                    mAddition = true;
                    operator = true;
                    DecimalFormat format = new DecimalFormat("0.00");
                    ed1.setText(format.format(Value1));
                }
        });

        btn_Sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
                    ed1.setText("0.0");
                }
                    Value1 = Float.parseFloat(ed1.getText() + "");
                    mSubtract = true;
                    operator = true;
                    DecimalFormat format = new DecimalFormat("0.00");
                    ed1.setText(format.format(Value1));
            }
        });

        btn_Mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
                        ed1.setText("0.00");
                    }
                    Value1 = Float.parseFloat(ed1.getText() + "");
                    mMultiplication = true;
                    operator = true;
                    DecimalFormat format = new DecimalFormat("0.00");
                    ed1.setText(format.format(Value1));

            }
        });

        btn_Div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
                        ed1.setText("0.0");
                    }
                    Value1 = Float.parseFloat(ed1.getText() + "");
                    mDivision = true;
                    operator = true;
                    DecimalFormat format = new DecimalFormat("0.00");
                    ed1.setText(format.format(Value1));
            }
        });

        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ed1.getText().toString().equals("") || Value1 != null) {
                    if(ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
                        ed1.setText("0.0");
                    }
                    Value2 = Float.parseFloat(ed1.getText() + "");

                    if (mAddition == true) {

                        Float result = Value1 + Value2;
                        DecimalFormat format = new DecimalFormat("0.00");
                        ed1.setText(format.format(result));

                        mAddition = false;
                    } else if (mSubtract == true) {
                        Float result = Value1 - Value2;
                        DecimalFormat format = new DecimalFormat("0.00");
                        ed1.setText(format.format(result));

                        mSubtract = false;
                    } else if (mMultiplication == true) {
                        Float result = Value1 * Value2;
                        DecimalFormat format = new DecimalFormat("0.00");
                        ed1.setText(format.format(result));

                        mMultiplication = false;
                    } else if (mDivision == true) {
                        Float result = Value1/Value2;
                        DecimalFormat format = new DecimalFormat("0.00");
                        ed1.setText(format.format(result));

                        mDivision = false;
                    } else {
                        DecimalFormat format = new DecimalFormat("0.00");
                        ed1.setText(format.format(0));
                    }
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText("");
            }
        });

        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        cons.setClickable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                startActivity(new Intent(Overview.this, Overview.class));
            }
        });

        // Set an elevation value for popup window
        // Call requires API level 21
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }

        popupWindow.showAtLocation(cons, Gravity.CENTER, 0, 400);
    }

    }