package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.text.DecimalFormat;

import edu.hope.cs.bilancioandroid.Controller.CustomEditText;
import edu.hope.cs.bilancioandroid.R;

public class Threshold extends AppCompatActivity {
    TextView cancel;
    TextView save;
    CustomEditText amount;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    double thresholdAmt;
    boolean focus = false;

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
        setContentView(R.layout.activity_threshhold);
        cancel = (TextView) findViewById(R.id.cancel_threshhold);
        save = (TextView) findViewById(R.id.save_threshhold);
        amount = (CustomEditText) findViewById(R.id.enter_threshhold);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdvancedSettings.class);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putLong("threshold", Double.doubleToRawLongBits(thresholdAmt));
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), AdvancedSettings.class);
                startActivity(intent);
            }
        });

        amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String entry = amount.getText().toString().trim();
                    entry = entry.replaceAll("[^\\d.]", "");
                    if (entry.equals("")) {

                    } else {
                        thresholdAmt = Double.parseDouble(entry);
                        DecimalFormat df = new DecimalFormat("0.00");
                        entry = df.format(thresholdAmt);
                        entry = "$" + entry;
                        amount.setText(entry);
                    }
                }
                return false;
            }
        });
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (focus) {
                    String placeholder = amount.getText().toString().trim().replaceAll("[^\\d.]", "");
                    if (placeholder.equals("")) {
                        focus = false;
                    } else {
                        thresholdAmt = Double.parseDouble(placeholder);
                        DecimalFormat df = new DecimalFormat("0.00");
                        String entry = df.format(thresholdAmt);
                        entry = "$" + entry;
                        amount.setText(entry);
                        focus = false;


                    }
                }

            }

        });
    }
}
