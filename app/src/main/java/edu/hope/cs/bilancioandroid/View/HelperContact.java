package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.hope.cs.bilancioandroid.R;

public class HelperContact extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

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
        setContentView(R.layout.activity_helper_contact);
        int size = prefs.getInt("Text Size", 18);
        TextView description = (TextView) findViewById(R.id.contactDescription);
        description.setTextSize(size);

        TextView nameTag = (TextView) findViewById(R.id.name);
        nameTag.setTextSize(size);

        TextView name = (TextView) findViewById(R.id.helperName);
        name.setTextSize(size);
        name.setText(prefs.getString("Name", ""));

        TextView phone = (TextView) findViewById(R.id.number);
        phone.setTextSize(size);

        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        phoneNumber.setTextSize(size);
        phoneNumber.setText(prefs.getString("Phone", ""));

        TextView goButton = (TextView) findViewById(R.id.changeContactBtn);
        goButton.setTextSize(size);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelperContact.this, Contacts.class));
            }
        });
    }
}
