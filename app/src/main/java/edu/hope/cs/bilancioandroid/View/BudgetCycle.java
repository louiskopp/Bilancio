package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import edu.hope.cs.bilancioandroid.R;

public class BudgetCycle extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String fromSettings;
    private ArrayAdapter<CharSequence> adapter;

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
        setContentView(R.layout.activity_budget_cycle);
        TextView message = findViewById(R.id.timeMsg);
        if(prefs.getInt("Text Size", 2) != 2) {
            message.setTextSize(size);
        }
        TextView current = (TextView) findViewById(R.id.currentTimeFrame);
        current.setTextSize(size);
        current.setVisibility(View.GONE);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.budgetToolbar);
        toolbar.setVisibility(View.GONE);
        TextView saveBtn = (TextView) findViewById(R.id.savingsBtn);
        saveBtn.setVisibility(View.GONE);
        final Spinner spinner = (Spinner) findViewById(R.id.select_time);
        int spinnerText;
        if(size == 14) {
            spinnerText = R.layout.spinner_small;
        } else if(size == 18) {
            spinnerText = R.layout.spinner_medium;
        } else {
            spinnerText = R.layout.spinner_large;
        }
        adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, spinnerText);
        adapter.setDropDownViewResource(spinnerText);
        spinner.setAdapter(adapter);
        TextView goToLogin = (TextView) findViewById(R.id.go_to_login);
        if(!prefs.getString("Last Page", "").equals("")) {
            current.setVisibility(View.VISIBLE);
            current.setText("Current Time Frame: " + prefs.getString("Cycle", "One Month"));
            toolbar.setVisibility(View.VISIBLE);
            goToLogin.setVisibility(View.GONE);
            saveBtn.setVisibility(View.VISIBLE);
            saveBtn.setTextSize(size);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int choice = spinner.getSelectedItemPosition();
                    String timeFrame = adapter.getItem(choice).toString();
                    editor.remove("Cycle");
                    editor.apply();
                    editor.putString("Cycle", timeFrame);
                    editor.apply();
                    startActivity(new Intent(BudgetCycle.this, Settings.class));
                }
            });
        }
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int choice = spinner.getSelectedItemPosition();
                String timeFrame = adapter.getItem(choice).toString();
                editor.putString("Cycle", timeFrame);
                editor.apply();
                    Intent nextIntent = new Intent(getApplicationContext(), Contacts.class);
                    startActivity(nextIntent);

            }
        });
    }

    @Override
    public void onBackPressed(){}
}
