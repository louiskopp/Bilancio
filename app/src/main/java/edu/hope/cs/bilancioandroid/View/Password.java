package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.hope.cs.bilancioandroid.R;

public class Password extends AppCompatActivity {
SharedPreferences prefs;
SharedPreferences.Editor editor;
private String fromSettings;
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
        setContentView(R.layout.activity_password);
        fromSettings = getIntent().getStringExtra("settings");
        TextView login = (TextView) findViewById(R.id.go_to_budget);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();
                if(pass.equals(confirmPass) && pass.equals("")) {
                    editor.putString("Password", pass);
                    editor.apply();
                    if(prefs.getString("Settings", "No").equals("Yes")){
                        Intent nextIntent = new Intent(getApplicationContext(), AdvancedSettings.class);
                        startActivity(nextIntent);
                    }
                    else {
                        Intent nextIntent = new Intent(getApplicationContext(), MakeOrPresetBudgets.class);
                        startActivity(nextIntent);
                    }
                } else if(!pass.equals(confirmPass)) {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(Password.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Your password and your confirmation are not the same. Please try again!").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Not a Match!");
                    alert.show();
                    password.getText().clear();
                    confirmPassword.getText().clear();
                }
                else if(!pass.matches("^[a-zA-Z0-9@\\\\#$%&*()_+\\]\\[';:?.,!^-]{8,}$")) {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(Password.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("You must enter a password that is at least 8 characters.").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Password Not Long Enough!");
                    alert.show();
                    password.getText().clear();
                    confirmPassword.getText().clear();
                }
                else {
                prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Password", pass);
                editor.apply();
                if(prefs.getString("Settings", "No").equals("Yes")){
                    editor.putInt("Advanced", 1);
                    editor.apply();
                    Intent nextIntent = new Intent(getApplicationContext(), AdvancedSettings.class);
                    startActivity(nextIntent);
                }
                else {
                    Intent nextIntent = new Intent(getApplicationContext(), MakeOrPresetBudgets.class);
                    startActivity(nextIntent);
                }
                }
            }
        });
    }
}
