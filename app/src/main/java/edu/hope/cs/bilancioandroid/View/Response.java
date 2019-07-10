package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.text.DecimalFormat;

import edu.hope.cs.bilancioandroid.Controller.CustomEditText;
import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.R;

public class Response extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String id;
    Scenario scenario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TeachingMode);
        setContentView(R.layout.activity_response);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        int size = prefs.getInt("Text Size", 18);
        id = prefs.getString("ID", "");
        scenario = new Scenario(id);
        TextView message = findViewById(R.id.scenario_responce);
        message.setTextSize(size);
        TextView amountText = findViewById(R.id.amount);
        amountText.setTextSize(size);
        final CustomEditText amount = (CustomEditText) findViewById(R.id.enter_answer);
        amount.setTextSize(size);
        amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String entry = amount.getText().toString().trim();
                    if (entry.equals("")) {
                    } else {
                        double amountEntered = Double.parseDouble(entry.replaceAll("[^\\d.]", ""));
                        DecimalFormat df = new DecimalFormat("0.00");
                        entry = df.format(amountEntered);
                        entry = "$" + entry;
                        amount.setText(entry);
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;

                }
                else {
                    return false;
                }
            }
        });
        TextView answer = (TextView) findViewById(R.id.check_answer);
        answer.setTextSize(size);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().equals("")||amount.getText()==null){
                    String g = amount.getText().toString();
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(Response.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("You need to enter an answer in order to check your answer.").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Empty Answer");
                    alert.show();
                }
                else {
                    String g = amount.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), Results.class);
                    editor.putString("Answer", amount.getText().toString());
                    editor.apply();
                    startActivity(intent);
                }
            }
        });
        message.setText(scenario.getFullStr());
        TextView yes = (TextView) findViewById(R.id.yes_response);
        yes.setTextSize(size);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Results.class);
                editor.putString("Answer", "Yes");
                editor.apply();
                startActivity(intent);
            }
        });
        TextView no = (TextView) findViewById(R.id.no_response);
        no.setTextSize(size);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Results.class);
                editor.putString("Answer", "No");
                editor.apply();
                startActivity(intent);
            }
        });
        if(scenario.getType().equals("howMuchIsLeftTransaction")){
            yes.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
            findViewById(R.id.respView1).setVisibility(View.GONE);
            findViewById(R.id.respView2).setVisibility(View.GONE);
            findViewById(R.id.respView3).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.amount).setVisibility(View.GONE);
            amount.setVisibility(View.GONE);
            answer.setVisibility(View.GONE);
        }
    }
}
