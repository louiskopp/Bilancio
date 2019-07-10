package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.hope.cs.bilancioandroid.Controller.MyDatePickerFragment;
import edu.hope.cs.bilancioandroid.Controller.NavigationViewHelper;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.GoalDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Goal;
import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.R;

public class Savings extends AppCompatActivity {

    private GoalDatabase gDb;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    private Context context;
    private Double savingsFromGoals = 0.0;
    private Double totalSavings = 0.0;
    private SavingsAdapter savingsAdapter;
    private ListView list;
    private ArrayList<Goal> goals = new ArrayList<Goal>();
    SharedPreferences prefs;
    Double newMoney;
    TextView scenarioInfo;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_overview:
                    Intent overviewIntent = new Intent(getApplicationContext(), Overview.class);
                    startActivity(overviewIntent);
                    finish();
                    break;

                case R.id.navigation_wallet:
                    Intent walletIntent = new Intent(getApplicationContext(), WalletActivity.class);
                    startActivity(walletIntent);
                    finish();
                    break;

                case R.id.navigation_savings:

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
                    Intent overviewIntent = new Intent(getApplicationContext(), Overview.class);
                    startActivity(overviewIntent);
                    finish();
                    break;

                case R.id.navigation_wallet:
                    Intent walletIntent = new Intent(getApplicationContext(), WalletActivity.class);
                    startActivity(walletIntent);
                    finish();
                    break;

                case R.id.navigation_savings:

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
    private SharedPreferences.Editor editor;
    private boolean re = false;
    boolean mAddition = false;
    boolean mSubtract = false;
    boolean mMultiplication = false;
    boolean mDivision = false;
    Float Value1;
    Float Value2;
    private Date date = new Date();
    boolean operator;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        if (prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_savings);
        totalSavings = Double.longBitsToDouble(prefs.getLong("total savings", 0));
        int size = prefs.getInt("Text Size", 20);
        newMoney = Double.longBitsToDouble(prefs.getLong("new money", 0));
        cons = (ConstraintLayout) findViewById(R.id.savingsLayout);
        context = this;
        gDb = Room.databaseBuilder(getApplicationContext(),
                GoalDatabase.class, "goal_db").build();
        db = Room.databaseBuilder(this,
                AppDatabase.class, DATABASE_NAME).build();
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
                if (type.equals("predictiveTrue") || type.equals("predictiveFalse") || type.equals("howMuchIsLeftTransaction")) {
                    Intent intent = new Intent(getApplicationContext(), Response.class);
                    startActivity(intent);
                } else {
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


        if (prefs.getBoolean("Scenario", false) != true) {
            layout.setVisibility(View.GONE);
            calculator.setVisibility(View.GONE);
            scenarioFinish.setVisibility(View.GONE);
            scenarioInfo.setVisibility(View.GONE);
        }

        Thread clear = new Thread(new Runnable() {
            @Override
            public void run() {
                goals = (ArrayList<Goal>) gDb.goalDao().getAll();
                if (!prefs.getBoolean("End", true)) {
                } else {
                    if (goals == null) {
                        totalSavings += newMoney;
                    } else {
                        int t = goals.size();
                        double forEach = newMoney / t;
                        for (Goal goal : goals) {
                            if (goal.getAmountTotal() - goal.getAmountSaved() > forEach) {
                                Goal placeholder = gDb.goalDao().findByGoal(goal.getNameOfGoal());
                                goal.setAmountSaved(goal.getAmountSaved() + forEach);
                                newMoney -= forEach;
                                gDb.goalDao().delete(placeholder);
                                gDb.goalDao().insertAll(goal);
                                savingsFromGoals += forEach;
                            } else if (goal.getAmountTotal() - goal.getAmountSaved() == forEach) {
                                if (prefs.getBoolean("later", false)) {
                                    savingsFromGoals += goal.getAmountTotal() - goal.getAmountSaved();
                                    Goal placeholder = gDb.goalDao().findByGoal(goal.getNameOfGoal());
                                    goal.setAmountSaved(goal.getAmountTotal());
                                    newMoney -= forEach;
                                    gDb.goalDao().delete(placeholder);
                                    gDb.goalDao().insertAll(goal);
                                } else {
                                    Budget budget = db.budgetDao().findByCategory("Extra Money");
                                    Budget placeholder = budget;
                                    Goal placegolder = gDb.goalDao().findByGoal(goal.getNameOfGoal());
                                    Double back = placegolder.getAmountTotal();
                                    gDb.goalDao().delete(placegolder);
                                    budget.setAmount(budget.getAmount() + back);
                                    db.budgetDao().delete(placeholder);
                                    db.budgetDao().insertAll(budget);
                                    newMoney -= forEach;
                                }
                            } else {
                                if (prefs.getBoolean("later", false)) {
                                    savingsFromGoals += goal.getAmountTotal() - goal.getAmountSaved();
                                    Goal placeholder = gDb.goalDao().findByGoal(goal.getNameOfGoal());
                                    Double more = goal.getAmountTotal() - goal.getAmountSaved();
                                    goal.setAmountSaved(goal.getAmountTotal());
                                    newMoney -= more;
                                    gDb.goalDao().delete(placeholder);
                                    gDb.goalDao().insertAll(goal);
                                } else {
                                    Budget budget = db.budgetDao().findByCategory("Extra Money");
                                    Budget placeholder = budget;
                                    Goal placegolder = gDb.goalDao().findByGoal(goal.getNameOfGoal());
                                    Double back = placegolder.getAmountTotal();
                                    Double more = -placegolder.getAmountSaved() + placegolder.getAmountTotal();
                                    gDb.goalDao().delete(placegolder);
                                    budget.setAmount(budget.getAmount() + back);
                                    db.budgetDao().delete(placeholder);
                                    db.budgetDao().insertAll(budget);
                                    newMoney -= more;
                                }
                            }
                        }
                        totalSavings += newMoney;
                        editor.putLong("total savings", Double.doubleToRawLongBits(totalSavings));
                        editor.apply();
                    }
                    editor.putBoolean("End", false);
                    editor.apply();
                    re = true;
                }

            }
        });
        clear.start();
        try {
            clear.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(final Goal goal: goals){
            Date end = new Date();
            if(goal!=null) {
                if (goal.getDate()!=null) {
                    String monthWord = goal.getDate().split(" ")[0];
                    Date date1 = null;
                    try {
                        date1 = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthWord);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date1);
                    int month1 = cal.get(Calendar.MONTH);
                    String month = Integer.toString(month1 + 1);
                    String year = goal.getDate().split(" ")[2];
                    String day = goal.getDate().split(" ")[1];
                    day = day.replaceAll("[^\\d.]", "");
                    String newDate = day + "/" + month + "/" + year;
                    try {
                        end = new SimpleDateFormat("dd/MM/yyyy").parse(newDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (end.compareTo(date) <= 0) {
                        final AlertDialog.Builder a_builder = new AlertDialog.Builder(Savings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        a_builder.setMessage("Would you like this goal to end now, or would you like to reset the end date for this goal?").setCancelable(false)
                                .setPositiveButton("Reset Date", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        MyDatePickerFragment newFragment = new MyDatePickerFragment(Savings.this, goal);
                                        newFragment.show(getSupportFragmentManager(), "date picker");
                                    }
                                })
                                .setNegativeButton("End Goal", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        deleteGoal(goal);
                                    }
                                });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Your goal named " + goal.getNameOfGoal() + " has reached its end date.");
                        alert.show();
                    }
                }
            }
        }
        if (re) {
            re = false;
            recreate();
        }
        if (!re) {
            for (Goal goal : goals) {
                savingsFromGoals += goal.getAmountSaved();

            }
        }
        list = (ListView) findViewById(R.id.list_of_goals);
        TextView emptyText = (TextView) findViewById(R.id.empty);
        emptyText.setTextSize(size);
        list.setEmptyView(emptyText);
        savingsAdapter = new SavingsAdapter(this, goals);
        list.setAdapter(savingsAdapter);
        if (savingsAdapter == null) {
            list.setVisibility(View.GONE);
        }
        BottomNavigationView navi = findViewById(R.id.navigation2);
        final BottomNavigationView nav = findViewById(R.id.navigation);

        if (prefs.getString("Teaching Mode", "Off").equals("On")) {
            nav.setVisibility(View.INVISIBLE);
            NavigationViewHelper.disableShiftMode(navi);
            MenuItem item1 = navi.getMenu().getItem(2).setChecked(true);
            navi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener2);
        } else {
            navi.setVisibility(View.GONE);
            NavigationViewHelper.disableShiftMode(nav);
            MenuItem item = nav.getMenu().getItem(2).setChecked(true);
            nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }
        TextView settingsBtn = (TextView) findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Savings.this, Settings.class);
                editor.putString("Last Page", "Savings");
                editor.apply();
                startActivity(settingsIntent);
            }
        });

        TextView savingsView = (TextView) findViewById(R.id.savingsView);
        savingsView.setTextSize(size);

        TextView totalS = (TextView) findViewById(R.id.savingsAmt);
        totalS.setTextSize(size);

        TextView goalsView = (TextView) findViewById(R.id.goalsView);
        goalsView.setTextSize(size);

        TextView totalG = (TextView) findViewById(R.id.goalsAmt);
        totalG.setTextSize(size);

        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(totalSavings);
        entry = "$" + entry;
        totalS.setText(entry);
        entry = df.format(savingsFromGoals);
        entry = "$" + entry;
        totalG.setText(entry);

        final Button anchor = findViewById(R.id.anchor);
        anchor.setVisibility(View.INVISIBLE);
        final Context wrapper = new ContextThemeWrapper(this, R.style.popupMenu);
        Button addGoalBtn = (Button) findViewById(R.id.addGoalButton);
        addGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(wrapper, anchor, Gravity.CENTER_HORIZONTAL);
                popup.inflate(R.menu.savings_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_goal_item:
                                Intent nextIntent = new Intent(getApplicationContext(), EditGoal.class);
                                startActivity(nextIntent);
                                return true;

                            case R.id.cancelButton:
                                popup.dismiss();
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(Savings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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

    public void goToEditGoal(Goal goal) {
        Intent intent = new Intent(getApplicationContext(), EditGoal.class);
        intent.putExtra("title", goal.getNameOfGoal());
        intent.putExtra("saved", goal.getAmountSaved());
        intent.putExtra("total", goal.getAmountTotal());
        intent.putExtra("date", goal.getDate());
        startActivity(intent);
    }

    public void finishedPopUp(final Goal goal) {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(Savings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("One or more of your goals have been met.").setCancelable(false)
                .setPositiveButton("Use Goal Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Thread go = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Budget budget = db.budgetDao().findByCategory("Extra Money");
                                Budget placeholder = budget;
                                Goal placegolder = gDb.goalDao().findByGoal(goal.getNameOfGoal());
                                Double back = placegolder.getAmountTotal();
                                gDb.goalDao().delete(placegolder);
                                budget.setAmount(budget.getAmount() + back);
                                db.budgetDao().delete(placeholder);
                                db.budgetDao().insertAll(budget);
                                savingsFromGoals -= back;
                            }
                        });
                        go.start();
                        try {
                            go.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        secondPopUp();
                    }
                })
                .setNegativeButton("Use Goal Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Goal met!");
        alert.show();

    }

    private void secondPopUp() {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(Savings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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

    private void openScenarioPopUp() {
        Point p = new Point();
        int[] location = new int[2];
        scenarioInfo.getLocationOnScreen(location);
        p.x = location[0];
        p.y = location[1];
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.info_popup, null);
        TextView textView = (TextView) customView.findViewById(R.id.tv);
        Scenario message = new Scenario(prefs.getString("ID", ""));
        textView.setText(message.getFullStr());
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(
                customView,
                800,
                300
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }

        findViewById(R.id.savingsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.savingsLayout), Gravity.NO_GRAVITY, p.x, p.y - 250);
    }

    public void calculatorPopUp() {
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
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "0");
                operator = false;
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "1");
                operator = false;
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "2");
                operator = false;
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "3");
                operator = false;
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "4");
                operator = false;
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "5");
                operator = false;
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "6");
                operator = false;
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "7");
                operator = false;
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "8");
                operator = false;
            }
        });

        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + "9");
                operator = false;
            }
        });

        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator) {
                    ed1.getText().clear();
                }
                ed1.setText(ed1.getText() + ".");
                operator = false;
            }
        });

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
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
                if (ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
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
                if (ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
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
                if (ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
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
                    if (ed1.getText().toString().equals("") || ed1.getText().toString().equals(".")) {
                        ed1.setText("0.0");
                    }
                    Value2 = Float.parseFloat(ed1.getText() + "");

                    if (mAddition == true) {

                        Float result = Value1 + Value2;
                        DecimalFormat format = new DecimalFormat("0.00");
                        ed1.setText(format.format(result));

                        mAddition = false;
                    }


                    if (mSubtract == true) {
                        Float result = Value1 - Value2;
                        DecimalFormat format = new DecimalFormat("0.00");
                        ed1.setText(format.format(result));

                        mSubtract = false;
                    }

                    if (mMultiplication == true) {
                        Float result = Value1 * Value2;
                        DecimalFormat format = new DecimalFormat("0.00");
                        ed1.setText(format.format(result));

                        mMultiplication = false;
                    }

                    if (mDivision == true) {
                        Float result = Value1 / Value2;
                        DecimalFormat format = new DecimalFormat("0.00");
                        ed1.setText(format.format(result));

                        mDivision = false;
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
            }
        });

        // Set an elevation value for popup window
        // Call requires API level 21
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }

        popupWindow.showAtLocation(cons, Gravity.CENTER, 0, 400);
    }

    public void deleteGoal(final Goal g) {
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                gDb.goalDao().delete(g);
                Budget budget = db.budgetDao().findByCategory("Extra Money");
                Budget exMoney = budget;
                budget.setAmount(budget.getAmount() + g.getAmountSaved());
                db.budgetDao().delete(exMoney);
                db.budgetDao().insertAll(budget);
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recreate();
    }

    public void setDateOfGoal(final Goal g, final String d){
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                Goal oldGoal = gDb.goalDao().findByGoal(g.getNameOfGoal());
                Goal newGoal = oldGoal;
                newGoal.setDate(d);
                gDb.goalDao().delete(oldGoal);
                gDb.goalDao().insertAll(newGoal);
                goals = (ArrayList<Goal>) gDb.goalDao().getAll();
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        savingsAdapter = new SavingsAdapter(Savings.this, goals);
        list.setAdapter(savingsAdapter);



    }

}
