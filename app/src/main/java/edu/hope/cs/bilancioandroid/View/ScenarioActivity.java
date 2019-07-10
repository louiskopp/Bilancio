package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DecimalFormat;

import edu.hope.cs.bilancioandroid.Controller.NavigationViewHelper;
import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.R;

public class ScenarioActivity extends AppCompatActivity {

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
                    Intent WalletIntent = new Intent(getApplicationContext(),Savings.class);
                    startActivity(WalletIntent);
                    finish();
                    break;

                case R.id.navigation_savings:
                    Intent savingsIntent = new Intent(getApplicationContext(),Savings.class);
                    startActivity(savingsIntent);
                    finish();
                    break;

                case R.id.navigation_scenarios:

                    break;

            }
            return false;
        }
    };

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;
    ConstraintLayout constraintLayout;
    TextView scenarioInfo;
    PopupWindow popupWindow;
    boolean mAddition = false;
    boolean mSubtract = false;
    boolean mMultiplication = false;
    boolean mDivision = false;
    Float Value1;
    Float Value2;
    boolean operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        setTheme(R.style.TeachingMode);
        setContentView(R.layout.activity_scenario);
        int size = prefs.getInt("Text Size", 18);
        constraintLayout = (ConstraintLayout) findViewById(R.id.scenarioPage_layout);
        context = getApplicationContext();

        BottomNavigationView navi = findViewById(R.id.navigation2);

            NavigationViewHelper.disableShiftMode(navi);
            MenuItem item1 = navi.getMenu().getItem(3).setChecked(true);
            navi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        TextView settingsBtn = (TextView) findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(ScenarioActivity.this, Settings.class);
                editor.putString("Last Page", "Scenarios");
                editor.apply();
                startActivity(settingsIntent);
            }
        });

        TextView header = (TextView) findViewById(R.id.scenarioHeader);
        header.setTextSize(size);

        TextView homeworkTitle =(TextView) findViewById(R.id.homeworkTitle);
        homeworkTitle.setTextSize(size);

        TextView customTitle =(TextView) findViewById(R.id.customTitle);
        customTitle.setTextSize(size);

        TextView prebuiltTitle =(TextView) findViewById(R.id.prebuiltTitle);
        prebuiltTitle.setTextSize(size);

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

        TextView homework = (TextView) findViewById(R.id.homework_start);
        homework.setTextSize(size);
        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Homework.class);
                startActivity(intent);
            }
        });
        TextView custom = (TextView) findViewById(R.id.custom_start);
        custom.setTextSize(size);
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Custom.class);
                startActivity(intent);
            }
        });
        TextView prebuild = (TextView) findViewById(R.id.prebuilt_start);
        prebuild.setTextSize(size);
        prebuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Prebuilt.class);
                startActivity(intent);
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

        findViewById(R.id.scenario_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.scenario_layout), Gravity.NO_GRAVITY,p.x,p.y-250);
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
            public void onDismiss() {
            }
        });

        // Set an elevation value for popup window
        // Call requires API level 21
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }

        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0, 400);
    }

}
