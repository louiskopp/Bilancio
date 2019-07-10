package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.hope.cs.bilancioandroid.R;

public class Custom extends AppCompatActivity {

    String adjustmentText = "Make an adjustment to your budget allotted amounts.";
    String predictionText = "Can you afford a transaction?";
    String transactionText = "Record a transaction.";
    String howMuchText = "How much will be left in a budget after a transaction?";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TeachingMode);
        setContentView(R.layout.activity_custom);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        int size = prefs.getInt("Text Size", 18);
        String type = prefs.getString("Type", "");
        final TextView adjustment = (TextView) findViewById(R.id.adjustment);
        final TextView adjustment2 = (TextView) findViewById(R.id.adjustment2);
        final TextView prediction = (TextView) findViewById(R.id.prediction);
        final TextView prediction2 = (TextView) findViewById(R.id.prediction2);
        final TextView transaction = (TextView) findViewById(R.id.transaction);
        final TextView transaction2 = (TextView) findViewById(R.id.transaction2);
        final TextView howMuch = (TextView) findViewById(R.id.howMuch);
        final TextView howMuch2 = (TextView) findViewById(R.id.howMuch2);
        final TextView description = (TextView) findViewById(R.id.customDescription);
        final TextView next = (TextView) findViewById(R.id.next_custom);
        next.setVisibility(View.GONE);

            adjustment.setVisibility(View.VISIBLE);
            adjustment2.setVisibility(View.GONE);
            prediction.setVisibility(View.VISIBLE);
            prediction2.setVisibility(View.GONE);
            transaction.setVisibility(View.VISIBLE);
            transaction2.setVisibility(View.GONE);
            howMuch.setVisibility(View.VISIBLE);
            howMuch2.setVisibility(View.GONE);

        description.setTextSize(size);
        description.setText("Description:");
        TextView header = (TextView) findViewById(R.id.header);
        header.setTextSize(size);
        adjustment.setTextSize(size);
        adjustment2.setTextSize(size);
        adjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prefs.getString("Type", "").equals("")) {
                    editor.remove("Type");
                }
                editor.putString("Type", "Adjustment");
                editor.apply();
                adjustment.setVisibility(View.GONE);
                adjustment2.setVisibility(View.VISIBLE);
                prediction.setVisibility(View.VISIBLE);
                prediction2.setVisibility(View.GONE);
                transaction.setVisibility(View.VISIBLE);
                transaction2.setVisibility(View.GONE);
                howMuch.setVisibility(View.VISIBLE);
                howMuch2.setVisibility(View.GONE);
                description.setText("Description: " + adjustmentText);
                next.setVisibility(View.VISIBLE);
            }
        });

        prediction.setTextSize(size);
        prediction2.setTextSize(size);
        prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prefs.getString("Type", "").equals("")) {
                    editor.remove("Type");
                }
                editor.putString("Type", "Prediction");
                editor.apply();
                adjustment.setVisibility(View.VISIBLE);
                adjustment2.setVisibility(View.GONE);
                prediction.setVisibility(View.GONE);
                prediction2.setVisibility(View.VISIBLE);
                transaction.setVisibility(View.VISIBLE);
                transaction2.setVisibility(View.GONE);
                howMuch.setVisibility(View.VISIBLE);
                howMuch2.setVisibility(View.GONE);
                description.setText("Description: " + predictionText);
                next.setVisibility(View.VISIBLE);
            }
        });

        transaction.setTextSize(size);
        transaction2.setTextSize(size);
        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prefs.getString("Type", "").equals("")) {
                    editor.remove("Type");
                }
                editor.putString("Type", "Transaction");
                editor.apply();
                adjustment.setVisibility(View.VISIBLE);
                adjustment2.setVisibility(View.GONE);
                prediction.setVisibility(View.VISIBLE);
                prediction2.setVisibility(View.GONE);
                transaction.setVisibility(View.GONE);
                transaction2.setVisibility(View.VISIBLE);
                howMuch.setVisibility(View.VISIBLE);
                howMuch2.setVisibility(View.GONE);
                description.setText("Description: " + transactionText);
                next.setVisibility(View.VISIBLE);
            }
        });

        howMuch.setTextSize(size);
        howMuch2.setTextSize(size);
        howMuch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prefs.getString("Type", "").equals("")) {
                    editor.remove("Type");
                }
                editor.putString("Type", "How Much");
                editor.apply();
                adjustment.setVisibility(View.VISIBLE);
                adjustment2.setVisibility(View.GONE);
                prediction.setVisibility(View.VISIBLE);
                prediction2.setVisibility(View.GONE);
                transaction.setVisibility(View.VISIBLE);
                transaction2.setVisibility(View.GONE);
                howMuch.setVisibility(View.GONE);
                howMuch2.setVisibility(View.VISIBLE);
                description.setText("Description: " + howMuchText);
                next.setVisibility(View.VISIBLE);
            }
        });

        next.setTextSize(size);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Custom.this, CreateScenario.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Custom.this, ScenarioActivity.class));
    }
}
