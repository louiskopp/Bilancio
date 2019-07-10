package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.R;

public class AdvancedSettings extends AppCompatActivity {
    Bundle bundle;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private TextView scenarioInfo;
    private PopupWindow popupWindow;

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
        int size = prefs.getInt("Text Size", 18);
        setContentView(R.layout.activity_advanced_settings);
        bundle = getIntent().getExtras();
        final EditText passwordEntry = new EditText(this);
        passwordEntry.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEntry.setTextColor(Color.BLACK);
        if(!prefs.getString("Password", "").equals("") && prefs.getInt("Advanced", 0) != 1 && !prefs.getString("Teaching Mode", "Off").equals("On")) {
            final AlertDialog.Builder a_builder = new AlertDialog.Builder(AdvancedSettings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            a_builder.setMessage("Please enter your password to continue to the Advanced Settings").setCancelable(false)
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(passwordEntry.getText().toString().trim().equals(prefs.getString("Password", "Password"))) {
                                dialog.dismiss();
                                editor.putInt("Advanced", 1);
                                editor.apply();

                            } else {
                                dialog.dismiss();
                                incorrectAlert().show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Advanced Settings");
            alert.setView(passwordEntry);
            alert.show();
        }
        TextView resetPassword = (TextView) findViewById(R.id.resetPassword);
        resetPassword.setTextSize(size);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(AdvancedSettings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("You cannot reset your password in Teaching Mode. If you need to reset your password, please exit Teaching Mode.").setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Cannot reset password.");
                    alert.show();
                } else {
                    Intent nextIntent = new Intent(getApplicationContext(), Password.class);
                    startActivity(nextIntent);
                }
            }
        });
        TextView changeContact = (TextView) findViewById(R.id.changeContact);
        changeContact.setTextSize(size);
        changeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(),HelperContact.class);
                startActivity(nextIntent);
            }
        });
        TextView changeTimeFrame = (TextView) findViewById(R.id.changeTime);
        changeTimeFrame.setTextSize(size);
        changeTimeFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(),BudgetCycle.class);
                startActivity(nextIntent);
            }
        });
        TextView editBudgets = (TextView) findViewById(R.id.editBudgets);
        editBudgets.setTextSize(size);
        editBudgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(),EditBudgets.class);
                startActivity(nextIntent);
            }
        });
        TextView threshold = (TextView) findViewById(R.id.goto_threshold);
        threshold.setTextSize(size);
        threshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(),Threshold.class);
                startActivity(nextIntent);
            }
        });

        LinearLayout layout = findViewById(R.id.scenario_layout);
        TextView calculator = findViewById(R.id.calculator);
        TextView scenarioFinish = (TextView) findViewById(R.id.scenario_goto);
        calculator.setVisibility(View.INVISIBLE);
        scenarioFinish.setVisibility(View.INVISIBLE);

        scenarioInfo = (TextView) findViewById(R.id.scenario_show);
        scenarioInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScenarioPopUp();
            }
        });


        if(!prefs.getBoolean("Scenario", false)) {
            layout.setVisibility(View.GONE);
            scenarioInfo.setVisibility(View.GONE);
            calculator.setVisibility(View.GONE);
            scenarioFinish.setVisibility(View.GONE);
        }
    }

    //the popup that shows the scenario information
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

    //the back button always takes the user back to the Settings page
    @Override
    public void onBackPressed() {
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.remove("Advanced");
        editor.apply();
            Intent settingsIntent = new Intent(AdvancedSettings.this, Settings.class);
            startActivity(settingsIntent);
    }

    private AlertDialog incorrectAlert() {
        final EditText text = new EditText(this);
        text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedSettings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setMessage("Please enter your password to continue to the Advanced Settings").setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(text.getText().toString().trim().equals(prefs.getString("Password", "Password"))) {
                            dialog.dismiss();
                            editor.putInt("Advanced", 1);
                            editor.apply();
                        } else {
                            dialog.dismiss();
                            incorrectAlert2().show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Incorrect Password!");
        alert.setView(text);
        return alert;
    }

    //in order to have a continuous supply of alerts, two methods were made
    // that alternate back and forth
    private AlertDialog incorrectAlert2() {
        final EditText text = new EditText(this);
        text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedSettings.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setMessage("Please enter your password to continue to the Advanced Settings").setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(text.getText().toString().trim().equals(prefs.getString("Password", "Password"))) {
                            dialog.dismiss();
                            editor.putInt("Advanced", 1);
                            editor.apply();
                        } else {
                            dialog.dismiss();
                            incorrectAlert().show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Incorrect Password!");
        alert.setView(text);
        return alert;
    }
}
