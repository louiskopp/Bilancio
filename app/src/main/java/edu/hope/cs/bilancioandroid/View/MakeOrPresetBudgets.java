package edu.hope.cs.bilancioandroid.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.hope.cs.bilancioandroid.R;

public class MakeOrPresetBudgets extends AppCompatActivity {
    private Button own;
    private Button create;
    private Button ownCheck;
    private Button createCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_or_preset_budgets);
        own = (Button) findViewById(R.id.own);
        create = (Button) findViewById(R.id.create);
        ownCheck = (Button) findViewById(R.id.own2);
        ownCheck.setVisibility(View.GONE);
        createCheck = (Button) findViewById(R.id.create2);
        createCheck.setVisibility(View.GONE);
        own.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.setVisibility(View.VISIBLE);
                createCheck.setVisibility(View.GONE);
                ownCheck.setVisibility(View.VISIBLE);
                own.setVisibility(View.GONE);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                own.setVisibility(View.VISIBLE);
                ownCheck.setVisibility(View.GONE);
                createCheck.setVisibility(View.VISIBLE);
                create.setVisibility(View.GONE);
            }
        });
        TextView createBudget = (TextView) findViewById(R.id.go_to_budget);
        createBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createCheck.getVisibility()==View.VISIBLE) {
                    Intent nextIntent = new Intent(getApplicationContext(), SetUp.class);
                    startActivity(nextIntent);
                }
                else if (ownCheck.getVisibility()==View.VISIBLE){
                    Intent nextIntent = new Intent(getApplicationContext(), ManualSetUp.class);
                    startActivity(nextIntent);
                }
                else{
                    final AlertDialog.Builder a_builder1 = new AlertDialog.Builder(MakeOrPresetBudgets.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder1.setMessage("Please select a budgeting option").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert1 = a_builder1.create();
                    alert1.setTitle("Option not selected!");
                    alert1.show();
                }
            }
        });
    }


}
