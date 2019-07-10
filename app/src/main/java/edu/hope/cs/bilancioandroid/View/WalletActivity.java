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
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Controller.NavigationViewHelper;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class WalletActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_overview:
                    Intent overviewIntent = new Intent(getApplicationContext(),Overview.class);
                    startActivity(overviewIntent);
                    finish();
                    break;

                case R.id.navigation_wallet:

                    break;

                case R.id.navigation_savings:
                    Intent savingsIntent = new Intent(getApplicationContext(),Savings.class);
                    startActivity(savingsIntent);
                    finish();
                    break;

                case R.id.navigation_reminders:
                    Intent remindersIntent = new Intent(getApplicationContext(),ReminderActivity.class);
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


    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    private Double income = 0.0;
    private Double extraMoney = 0.0;
    private Double expenses = 0.0;
    private Double balance = 0.0;
    private String incomeStr, extraMoneyStr, expensesStr, balanceStr;
    private ArrayList<Budget> total = new ArrayList<Budget>();
    private Converters converter;
    private TextView incomeAmt;
    private TextView incomeView;
    private TextView extraMoneyAmt;
    private TextView extraMoneyView;
    private TextView expensesView;
    private TextView expensesAmt;
    private TextView balanceView;
    private TextView balanceAmt;
    private TextView summaryText;
    private Context context;
    private PopupWindow popupWindow;
    private ConstraintLayout constraintLayout;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Double extraAlloted = 0.0;
    TextView scenarioInfo;
    boolean mAddition = false;
    boolean mSubtract = false;
    boolean mMultiplication = false;
    boolean mDivision = false;
    Float Value1;
    Float Value2;
    boolean operator;
    Double all = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_wallet);
        int size = prefs.getInt("Text Size", 18);
        BottomNavigationView navi = findViewById(R.id.navigation2);
        final BottomNavigationView nav = findViewById(R.id.navigation);

        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            nav.setVisibility(View.INVISIBLE);
            NavigationViewHelper.disableShiftMode(navi);
            MenuItem item1 = navi.getMenu().getItem(1).setChecked(true);
            navi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener2);
        } else {
            navi.setVisibility(View.GONE);
            NavigationViewHelper.disableShiftMode(nav);
            MenuItem item = nav.getMenu().getItem(1).setChecked(true);
            nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }

        constraintLayout = (ConstraintLayout) findViewById(R.id.con) ;
        context = getApplicationContext();
        converter = new Converters();
        Toolbar toolbar = (Toolbar) findViewById(R.id.wallet_toolbar);
        setSupportActionBar(toolbar);

        summaryText = (TextView) findViewById(R.id.summaryText);
        summaryText.setTextSize(size);

        incomeAmt = (TextView) findViewById(R.id.incomeAmt);
        incomeAmt.setTextSize(size);

        incomeView = (TextView) findViewById(R.id.incomeText);
        incomeView.setTextSize(size);

        extraMoneyAmt = (TextView) findViewById(R.id.extra_moneyAmt);
        extraMoneyAmt.setTextSize(size);

        extraMoneyView = (TextView) findViewById(R.id.extraMoneyText);
        extraMoneyView.setTextSize(size);

        expensesView = (TextView) findViewById(R.id.expenseText);
        expensesView.setTextSize(size);

        expensesAmt = (TextView) findViewById(R.id.expensesAmt);
        expensesAmt.setTextSize(size);

        balanceView = (TextView) findViewById(R.id.balanceText);
        balanceView.setTextSize(size);

        balanceAmt = (TextView) findViewById(R.id.balanceAmt);
        balanceAmt.setTextSize(size);

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


        if(prefs.getBoolean("Scenario", false) != true) {
            layout.setVisibility(View.GONE);
            calculator.setVisibility(View.GONE);
            scenarioFinish.setVisibility(View.GONE);
            scenarioInfo.setVisibility(View.GONE);
        }

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(getApplicationContext(),
                TeachingModeDatabase.class, TEACHING).build();
        fillEntries();

        final Button anchor = findViewById(R.id.anchor);
        anchor.setVisibility(View.INVISIBLE);
        final Context wrapper = new ContextThemeWrapper(this, R.style.popupMenu);
        Button button = (Button) findViewById(R.id.addGoalButton);
        button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(wrapper, anchor, Gravity.CENTER_HORIZONTAL);
                    popup.inflate(R.menu.wallet_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.add_income_item:
                                    Intent nextIntent = new Intent(getApplicationContext(),AddIncome.class);
                                    nextIntent.putExtra("category", "Income");
                                    nextIntent.putExtra("Overview", "wallet");
                                    startActivity(nextIntent);
                                    return true;

                                case R.id.can_i_afford:
                                    Intent affordIntent = new Intent(getApplicationContext(), CanIAfford.class);
                                    affordIntent.putExtra("Overview", "Wallet");
                                    startActivity(affordIntent);
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

        TextView settingsBtn = (TextView) findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(WalletActivity.this, Settings.class);
                editor.putString("Last Page", "Wallet");
                editor.apply();
                startActivity(settingsIntent);
            }
        });

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

        findViewById(R.id.con).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.con), Gravity.NO_GRAVITY,p.x,p.y-250);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(WalletActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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
    
    public void fillEntries(){
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    total = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                    extraAlloted = tDb.teachingModeDao().findByCategory("Extra Money").getAllotted();
                }
                else {
                    total = (ArrayList<Budget>) db.budgetDao().getAll();
                    extraAlloted = db.budgetDao().findByCategory("Extra Money").getAllotted();
                }
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Budget budget: total){
            if(budget.getCategory().equals("Extra Money")){
                extraMoney = budget.getAmount();
            }
            balance+=budget.getAmount();
        }
        for(Budget budget: total){
            all+= budget.getAllotted();
            if(budget.getTransactions()!=null){
                String tr = budget.getTransactions();
                ArrayList<Transaction> tra = converter.fromString(tr);
                if(tra!=null) {
                    if(budget.getCategory().equals("Extra Money")){
                        continue;
                    }
                    else {
                        for (Transaction transaction1 : tra) {
                            expenses += transaction1.getAmount();
                        }
                    }
                }
            }
        }
        income = all-extraAlloted;

        DecimalFormat df = new DecimalFormat("0.00");
        String inc = df.format(income);
        String extra = df.format(extraMoney);
        if(expenses==0.0){
            String exp = df.format(expenses);
            exp = "$" + exp;
            expensesAmt.setText(exp);
        }
        else {
            String exp = df.format(-expenses);
            exp = "$" + exp;
            expensesAmt.setText(exp);
        }
        String bal = df.format(balance);
        inc = "$" + inc;
        extra = "$" + extra;
        bal = "$" + bal;
        incomeAmt.setText(inc);
        extraMoneyAmt.setText(extra);
        balanceAmt.setText(bal);

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
}
