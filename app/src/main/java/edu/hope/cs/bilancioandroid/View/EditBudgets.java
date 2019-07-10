package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Controller.CustomEditText;
import edu.hope.cs.bilancioandroid.Controller.StickyService;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class EditBudgets extends AppCompatActivity {
    EditAdapter editAdapter;
    ListView listView;
    Context context;
    TextView difference;
    TextView totals;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    Double total = 0.0;
    SharedPreferences prefs;
    CustomEditText totalIncome;
    String income;
    Double diff;
    Converters converters;
    boolean focus = false;
    SharedPreferences.Editor editor;
    ConstraintLayout constraintLayout;
    private TextView scenarioInfo;
    private PopupWindow popupWindow;
    private boolean mDivision;
    private boolean mAddition;
    private boolean mSubtract;
    private boolean mMultiplication;
    private boolean operator;
    private Float Value1;
    private Float Value2;
    private TextView incomeText;
    private TextView budgetTotalText;
    private TextView budgetTotals;
    private TextView differenceText;
    private ArrayList<Budget> saveBudgets;
    private boolean wasSaved = false;
    int color;
    int green = 0;
    int yellow = 0;
    int red = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Budget> budgets;
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                    saveBudgets = budgets;
                }
                else {
                    budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                    saveBudgets = budgets;
                }
                for(Budget budget: budgets){
                    total+=budget.getAllotted();
                }
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int size = prefs.getInt("Text Size", 18);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
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
        if(prefs.getBoolean("Scenario", false)) {
            setContentView(R.layout.activity_edit_budgets_scenario);
            LinearLayout layout = findViewById(R.id.scenario_layout);
            TextView scenarioFinish = (TextView) findViewById(R.id.scenario_goto);
            scenarioFinish.setVisibility(View.INVISIBLE);

            scenarioInfo = (TextView) findViewById(R.id.scenario_show);
            scenarioInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openScenarioPopUp();
                }
            });


            TextView calculator = findViewById(R.id.calculator);
            calculator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calculatorPopUp();
                }
            });

        } else {
            setContentView(R.layout.activity_edit_budgets);
        }
        constraintLayout = (ConstraintLayout) findViewById(R.id.edit_layout);
        context = this;
        String newIncome = prefs.getString("current income", "");
        TextView toNewBudget = (TextView) findViewById(R.id.to_new_budget);
        toNewBudget.setTextSize(size);
        TextView save = (TextView) findViewById(R.id.save_edit_budgets);
        save.setTextSize(size);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diff<0){
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(EditBudgets.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Please make sure your total is green before you save!.").setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Your total is negative!");
                    alert.show();
                }
                else if(diff>0){
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(EditBudgets.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Please make sure all of your income is assigned to a budget so your difference is $0.00.").setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("You have money left over!");
                    alert.show();
                }
                else {
                    editor.remove("current income");
                    editor.remove("not yet saved");
                    editor.apply();
                    wasSaved = true;
                    Intent intent = new Intent(getApplicationContext(), Overview.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        toNewBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), AddNewBudget.class);
                editor.putString("current income", totalIncome.getText().toString());
                editor.apply();
                startActivity(settingsIntent);
            }
        });
        editAdapter = new EditAdapter(this);
        listView = (ListView) findViewById(R.id.edit_list);
        listView.setAdapter(editAdapter);
        difference = (TextView) findViewById(R.id.difference_edit);
        difference.setTextSize(size);
        incomeText = findViewById(R.id.textView30);
        incomeText.setTextSize(size);
        budgetTotals = findViewById(R.id.budget_totals_edit);
        budgetTotals.setTextSize(size);
        budgetTotalText = findViewById(R.id.textView28);
        budgetTotalText.setTextSize(size);
        differenceText = findViewById(R.id.textView29);
        differenceText.setTextSize(size);
        totals = (TextView) findViewById(R.id.budget_totals_edit);
        totals.setTextSize(size);
        totalIncome = (CustomEditText) findViewById(R.id.income_edit_budgets);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            totalIncome.setFocusedByDefault(false);
        }
        Intent stickyService = new Intent(this, StickyService.class);
        Gson gson = new Gson();
        String json = gson.toJson(saveBudgets);
        stickyService.putExtra("budgets", json);
        if(prefs.getString("not yet saved", "").equals("")){
            editor.putString("not yet saved", json);
            editor.apply();
        }
        else{
            Type listType = new TypeToken<ArrayList<Budget>>() {}.getType();
            String value = prefs.getString("not yet saved", "");
            saveBudgets =  new Gson().fromJson(value, listType);
        }
        startService(stickyService);
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(total);
        entry = "$" + entry;
        totals.setText(entry);
        if(!newIncome.equals("")){
            totalIncome.setText(newIncome);
        }
        else {
            totalIncome.setText(entry);
        }
        totalIncome.setTextSize(size);
        totalIncome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(focus) {
                    String placeholder = totalIncome.getText().toString().trim().replaceAll("[^\\d.]", "");
                    if (placeholder.equals("")) {
                        focus = false;
                    } else {
                        Double amountTotal = Double.parseDouble(placeholder);
                        DecimalFormat df = new DecimalFormat("0.00");
                        String entry = df.format(amountTotal);
                        entry = "$" + entry;
                        income = entry;
                        totalIncome.setText(entry);
                        focus = false;
                        Double money = Double.parseDouble(income.replaceAll("[^\\d.]", ""));
                        money = money-total;
                        //total = amountTotal;
                        diff = money;
                        entry = df.format(money);
                        entry = "$" + entry;
                        difference.setText(entry);
                        if(money<0){
                            difference.setTextColor(red);
                        }
                        else if(money>0){
                            difference.setTextColor(yellow);
                        }
                        else{
                            difference.setTextColor(green);
                        }
                    }
                }
                else{
                    focus = true;
                }
            }
        });
        totalIncome.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE|| actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String entry = totalIncome.getText().toString().trim();
                    entry = entry.replaceAll("[^\\d.]", "");
                    if (entry.equals("")) {
                    } else {
                        Double next = Double.parseDouble(entry);
                        DecimalFormat df = new DecimalFormat("0.00");
                        entry = df.format(next);
                        entry = "$" + entry;
                        income = entry;
                        totalIncome.setText(entry);
                        Double money = Double.parseDouble(income.replaceAll("[^\\d.]", ""));
                        money = money-total;
                        //total = next;
                        diff = money;
                        entry = df.format(money);
                        entry = "$" + entry;
                        difference.setText(entry);
                        if(money<0){
                            difference.setTextColor(red);
                        }
                        else if(money>0){
                            difference.setTextColor(yellow);
                        }
                        else{
                            difference.setTextColor(green);
                        }
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                totalIncome.clearFocus();
                return true;
            }
        });
        Double money = Double.parseDouble(totalIncome.getText().toString().replaceAll("[^\\d.]", ""));
        money = money-total;
        diff = money;
        entry = df.format(money);
        entry = "$" + entry;
        difference.setText(entry);
        if(money<0){
            difference.setTextColor(red);
        }
        else if(money>0){
            difference.setTextColor(yellow);
        }
        else{
            difference.setTextColor(green);
        }

    }

    public void restart(){
        Intent intent = new Intent(getApplicationContext(), EditBudgets.class);
        intent.putExtra("old income", income);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("You will lose all edits you have made if you do not press save.").setCancelable(false)
                .setPositiveButton("I am sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), AdvancedSettings.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Are you sure you want to exit Edit Budgets without Saving?");
        alert.show();
    }

    public void fillBudgets(final Budget oldBuddget, final double newAmt) {
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                Budget budget1;
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    budget1 = tDb.teachingModeDao().findByCategory(oldBuddget.getCategory());
                }
                else {
                    budget1 = db.budgetDao().findByCategory(oldBuddget.getCategory());
                }
                Budget placeholder = budget1;
                budget1.setAllotted(newAmt);
                String trans = budget1.getTransactions();
                ArrayList<Transaction> transactions = converters.fromString(trans);
                Double exp = 0.0;
                if(transactions==null){

                }
                else{
                    for(Transaction transaction: transactions){
                        exp+=transaction.getAmount();
                    }
                }
                budget1.setAmount(budget1.getAllotted()+exp);
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    tDb.teachingModeDao().delete(placeholder);
                    tDb.teachingModeDao().insertAll(budget1);
                }
                else {
                    db.budgetDao().delete(placeholder);
                    db.budgetDao().insertAll(budget1);
                }
                editAdapter = new EditAdapter(context);
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listView.setAdapter(editAdapter);
    }

    public void resetDiff() {
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                total=0.0;
                ArrayList<Budget> budgets;
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                }
                else {
                    budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                }
                for(Budget budget: budgets){
                    total+=budget.getAllotted();
                }
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(total);
        entry = "$" + entry;
        totals.setText(entry);
        Double money = Double.parseDouble(totalIncome.getText().toString().replaceAll("[^\\d.]", ""));
        money = money-total;
        diff = money;
        entry = df.format(money);
        entry = "$" + entry;
        difference.setText(entry);
        if(money<0){
            difference.setTextColor(red);
        }
        else if(money>0){
            difference.setTextColor(yellow);
        }
        else{
            difference.setTextColor(green);
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

        findViewById(R.id.edit_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.edit_layout), Gravity.NO_GRAVITY,p.x,p.y-250);
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
                        Float result = Value1/Value2;
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

        constraintLayout.setClickable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() { }
        });

        // Set an elevation value for popup window
        // Call requires API level 21
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }

        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0, 400);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.remove("current income");
        editor.remove("not yet saved");
        editor.apply();
        if(!wasSaved) {
            Thread go = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                        tDb.teachingModeDao().deleteAll();
                        for(Budget budget: saveBudgets){
                            tDb.teachingModeDao().insertAll(budget);
                        }
                    }
                    else{
                        db.budgetDao().deleteAll();
                        for(Budget budget: saveBudgets){
                            db.budgetDao().insertAll(budget);
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
        }
    }

    public void deleteBudgets(final Budget budget){
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                if (prefs.getString("Teaching Mode", "Off").equals("On")) {
                    Budget budget1 = tDb.teachingModeDao().findByCategory(budget.getCategory());
                    tDb.teachingModeDao().delete(budget1);
                    ArrayList<Budget> budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                    total = 0.0;
                    for(Budget budget: budgets){
                        total+=budget.getAllotted();
                    }
                } else {
                    Budget budget1 = db.budgetDao().findByCategory(budget.getCategory());
                    db.budgetDao().delete(budget1);
                    ArrayList<Budget> budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                    total = 0.0;
                    for(Budget budget: budgets){
                        total+=budget.getAllotted();
                    }
                }
                editAdapter = new EditAdapter(EditBudgets.this);
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Double money = Double.parseDouble(totalIncome.getText().toString().replaceAll("[^\\d.]", ""));
        money = money-total;
        diff = money;
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(money);
        entry = "$" + entry;
        difference.setText(entry);
        entry = df.format(total);
        totals.setText("$"+entry);
        if(money<0){
            difference.setTextColor(red);
        }
        else if(money>0){
            difference.setTextColor(yellow);
        }
        else{
            difference.setTextColor(green);
        }
        listView.setAdapter(editAdapter);
    }
}
