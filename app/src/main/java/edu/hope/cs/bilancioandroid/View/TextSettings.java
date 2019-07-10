package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.hope.cs.bilancioandroid.R;

public class TextSettings extends AppCompatActivity {
    SharedPreferences prefs;
    Bundle bundle;
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
        setContentView(R.layout.activity_text_settings);
        final TextView small = (TextView)findViewById(R.id.small);
        final TextView medium = (TextView)findViewById(R.id.medium);
        final TextView large = (TextView)findViewById(R.id.large);
        final TextView smallCheck = (TextView)findViewById(R.id.small2);
        final TextView mediumCheck = (TextView)findViewById(R.id.medium2);
        final TextView largeCheck = (TextView)findViewById(R.id.large2);

        if(prefs.getInt("Text Size", 18) == 18) {
            medium.setVisibility(View.GONE);
            mediumCheck.setVisibility(View.VISIBLE);
            smallCheck.setVisibility(View.GONE);
            largeCheck.setVisibility(View.GONE);
        } else if(prefs.getInt("Text Size", 18) == 14) {
            small.setVisibility(View.GONE);
            smallCheck.setVisibility(View.VISIBLE);
            mediumCheck.setVisibility(View.GONE);
            largeCheck.setVisibility(View.GONE);
        } else if(prefs.getInt("Text Size", 18) == 22) {
            large.setVisibility(View.GONE);
            largeCheck.setVisibility(View.VISIBLE);
            mediumCheck.setVisibility(View.GONE);
            smallCheck.setVisibility(View.GONE);
        }
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getInt("Text Size", 12) != 12) {
                    editor.remove("Text Size");
                }
                editor.putInt("Text Size", 14);
                editor.apply();
                small.setVisibility(View.GONE);
                smallCheck.setVisibility(View.VISIBLE);
                medium.setVisibility(View.VISIBLE);
                mediumCheck.setVisibility(View.GONE);
                large.setVisibility(View.VISIBLE);
                largeCheck.setVisibility(View.GONE);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getInt("Text Size", 12) != 12) {
                    editor.remove("Text Size");
                }
                editor.putInt("Text Size", 18);
                editor.apply();
                small.setVisibility(View.VISIBLE);
                smallCheck.setVisibility(View.GONE);
                medium.setVisibility(View.GONE);
                mediumCheck.setVisibility(View.VISIBLE);
                large.setVisibility(View.VISIBLE);
                largeCheck.setVisibility(View.GONE);
            }
        });

        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getInt("Text Size", 12) != 12) {
                    editor.remove("Text Size");
                }
                editor.putInt("Text Size", 22);
                editor.apply();
                small.setVisibility(View.VISIBLE);
                smallCheck.setVisibility(View.GONE);
                medium.setVisibility(View.VISIBLE);
                mediumCheck.setVisibility(View.GONE);
                large.setVisibility(View.GONE);
                largeCheck.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent settingsIntent = new Intent(TextSettings.this, Settings.class);
        startActivity(settingsIntent);
    }
}
