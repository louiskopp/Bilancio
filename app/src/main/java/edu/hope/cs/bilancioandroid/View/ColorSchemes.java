package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.hope.cs.bilancioandroid.R;

public class ColorSchemes extends AppCompatActivity {
    Bundle bundle;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
            final AlertDialog.Builder a_builder1 = new AlertDialog.Builder(ColorSchemes.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            a_builder1.setMessage("Please disable Teaching Mode to change colors.").setCancelable(false)
                    .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(ColorSchemes.this, Settings.class));
                        }
                    });
            AlertDialog alert1 = a_builder1.create();
            alert1.setTitle("Can't change color scheme while in Teaching Mode");
            alert1.show();
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_color_schemes);
        bundle = getIntent().getExtras();
        int size = prefs.getInt("Text Size", 18);
        int theme = prefs.getInt("Theme", R.style.White);

        final TextView red = (TextView) findViewById(R.id.red);
        final TextView redCheck = (TextView) findViewById(R.id.red2);
        final TextView blue = (TextView) findViewById(R.id.blue);
        final TextView blueCheck = (TextView) findViewById(R.id.blue2);
        final TextView gray = (TextView) findViewById(R.id.gray);
        final TextView grayCheck = (TextView) findViewById(R.id.gray2);
        final TextView black = (TextView) findViewById(R.id.black);
        final TextView blackCheck = (TextView) findViewById(R.id.black2);
        final TextView colorBlind = (TextView) findViewById(R.id.colorblind);
        final TextView colorBlindCheck = (TextView) findViewById(R.id.colorblind2);
        final TextView white = (TextView) findViewById(R.id.white);
        final TextView whiteCheck = (TextView) findViewById(R.id.white2);

        //set which box is checked based on the theme

        if(theme == R.style.RedTheme) {
            setTheme(prefs.getInt("Theme", R.style.White));
            red.setVisibility(View.GONE);
            redCheck.setVisibility(View.VISIBLE);
            blueCheck.setVisibility(View.GONE);
            grayCheck.setVisibility(View.GONE);
            blackCheck.setVisibility(View.GONE);
            colorBlindCheck.setVisibility(View.GONE);
            whiteCheck.setVisibility(View.GONE);
        } else if(theme == R.style.BlueTheme) {
            setTheme(prefs.getInt("Theme", R.style.White));
            redCheck.setVisibility(View.GONE);
            blue.setVisibility(View.GONE);
            blueCheck.setVisibility(View.VISIBLE);
            grayCheck.setVisibility(View.GONE);
            blackCheck.setVisibility(View.GONE);
            colorBlindCheck.setVisibility(View.GONE);
            whiteCheck.setVisibility(View.GONE);
        } else if(theme == R.style.GrayTheme) {
            setTheme(prefs.getInt("Theme", R.style.White));
            redCheck.setVisibility(View.GONE);
            blueCheck.setVisibility(View.GONE);
            gray.setVisibility(View.GONE);
            grayCheck.setVisibility(View.VISIBLE);
            blackCheck.setVisibility(View.GONE);
            colorBlindCheck.setVisibility(View.GONE);
            whiteCheck.setVisibility(View.GONE);
        } else if(theme == R.style.Black) {
            setTheme(prefs.getInt("Theme", R.style.White));
            redCheck.setVisibility(View.GONE);
            blueCheck.setVisibility(View.GONE);
            grayCheck.setVisibility(View.GONE);
            black.setVisibility(View.GONE);
            blackCheck.setVisibility(View.VISIBLE);
            colorBlindCheck.setVisibility(View.GONE);
            whiteCheck.setVisibility(View.GONE);

        } else if(theme == R.style.Colorblind) {
            setTheme(prefs.getInt("Theme", R.style.White));
            redCheck.setVisibility(View.GONE);
            blueCheck.setVisibility(View.GONE);
            grayCheck.setVisibility(View.GONE);
            blackCheck.setVisibility(View.GONE);
            colorBlind.setVisibility(View.GONE);
            colorBlindCheck.setVisibility(View.VISIBLE);
            whiteCheck.setVisibility(View.GONE);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
            redCheck.setVisibility(View.GONE);
            blueCheck.setVisibility(View.GONE);
            grayCheck.setVisibility(View.GONE);
            blackCheck.setVisibility(View.GONE);
            colorBlindCheck.setVisibility(View.GONE);
            white.setVisibility(View.GONE);
            whiteCheck.setVisibility(View.VISIBLE);
        }


        red.setTextSize(size);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getInt("Theme", 5) != 5) {
                    editor.remove("Theme");
                }
                editor.putInt("Theme", R.style.RedTheme);
                editor.apply();
                setTheme(prefs.getInt("Theme", R.style.White));
                red.setVisibility(View.GONE);
                redCheck.setVisibility(View.VISIBLE);
                blue.setVisibility(View.VISIBLE);
                blueCheck.setVisibility(View.GONE);
                gray.setVisibility(View.VISIBLE);
                grayCheck.setVisibility(View.GONE);
                black.setVisibility(View.VISIBLE);
                blackCheck.setVisibility(View.GONE);
                colorBlind.setVisibility(View.VISIBLE);
                colorBlindCheck.setVisibility(View.GONE);
                white.setVisibility(View.VISIBLE);
                whiteCheck.setVisibility(View.GONE);
                recreate();
            }
        });


        redCheck.setTextSize(size);
        redCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        blue.setTextSize(size);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getInt("Theme", 5) != 5) {
                    editor.remove("Theme");
                }
                editor.putInt("Theme", R.style.BlueTheme);
                editor.apply();
                setTheme(prefs.getInt("Theme", R.style.White));
                red.setVisibility(View.VISIBLE);
                redCheck.setVisibility(View.GONE);
                blue.setVisibility(View.GONE);
                blueCheck.setVisibility(View.VISIBLE);
                gray.setVisibility(View.VISIBLE);
                grayCheck.setVisibility(View.GONE);
                black.setVisibility(View.VISIBLE);
                blackCheck.setVisibility(View.GONE);
                colorBlind.setVisibility(View.VISIBLE);
                colorBlindCheck.setVisibility(View.GONE);
                white.setVisibility(View.VISIBLE);
                whiteCheck.setVisibility(View.GONE);
                recreate();
            }
        });


        blueCheck.setTextSize(size);
        blueCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        gray.setTextSize(size);
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getInt("Theme", 5) != 5) {
                    editor.remove("Theme");
                }
                editor.putInt("Theme", R.style.GrayTheme);
                editor.apply();
                setTheme(prefs.getInt("Theme", R.style.White));
                red.setVisibility(View.VISIBLE);
                redCheck.setVisibility(View.GONE);
                blue.setVisibility(View.VISIBLE);
                blueCheck.setVisibility(View.GONE);
                gray.setVisibility(View.GONE);
                grayCheck.setVisibility(View.VISIBLE);
                black.setVisibility(View.VISIBLE);
                blackCheck.setVisibility(View.GONE);
                colorBlind.setVisibility(View.VISIBLE);
                colorBlindCheck.setVisibility(View.GONE);
                white.setVisibility(View.VISIBLE);
                whiteCheck.setVisibility(View.GONE);
                recreate();
            }
        });


        grayCheck.setTextSize(size);
        grayCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        black.setTextSize(size);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getInt("Theme", 5) != 5) {
                    editor.remove("Theme");
                }
                editor.putInt("Theme", R.style.Black);
                editor.apply();
                setTheme(prefs.getInt("Theme", R.style.White));
                red.setVisibility(View.VISIBLE);
                redCheck.setVisibility(View.GONE);
                blue.setVisibility(View.VISIBLE);
                blueCheck.setVisibility(View.GONE);
                gray.setVisibility(View.VISIBLE);
                grayCheck.setVisibility(View.GONE);
                black.setVisibility(View.GONE);
                blackCheck.setVisibility(View.VISIBLE);
                colorBlind.setVisibility(View.VISIBLE);
                colorBlindCheck.setVisibility(View.GONE);
                white.setVisibility(View.VISIBLE);
                whiteCheck.setVisibility(View.GONE);
                recreate();
            }
        });


        blackCheck.setTextSize(size);
        blackCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        colorBlind.setTextSize(size);
        colorBlind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getInt("Theme", 5) != 5) {
                    editor.remove("Theme");
                }
                editor.putInt("Theme", R.style.Colorblind);
                editor.apply();
                setTheme(prefs.getInt("Theme", R.style.White));
                red.setVisibility(View.VISIBLE);
                redCheck.setVisibility(View.GONE);
                blue.setVisibility(View.VISIBLE);
                blueCheck.setVisibility(View.GONE);
                gray.setVisibility(View.VISIBLE);
                grayCheck.setVisibility(View.GONE);
                black.setVisibility(View.VISIBLE);
                blackCheck.setVisibility(View.GONE);
                colorBlind.setVisibility(View.GONE);
                colorBlindCheck.setVisibility(View.VISIBLE);
                white.setVisibility(View.VISIBLE);
                whiteCheck.setVisibility(View.GONE);
                recreate();
            }
        });


        colorBlindCheck.setTextSize(size);
        colorBlindCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        white.setTextSize(size);
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getInt("Theme", 5) != 5) {
                    editor.remove("Theme");
                }
                editor.putInt("Theme", R.style.White);
                editor.apply();
                setTheme(prefs.getInt("Theme", R.style.White));
                red.setVisibility(View.VISIBLE);
                redCheck.setVisibility(View.GONE);
                blue.setVisibility(View.VISIBLE);
                blueCheck.setVisibility(View.GONE);
                gray.setVisibility(View.VISIBLE);
                grayCheck.setVisibility(View.GONE);
                black.setVisibility(View.VISIBLE);
                blackCheck.setVisibility(View.GONE);
                colorBlind.setVisibility(View.VISIBLE);
                colorBlindCheck.setVisibility(View.GONE);
                white.setVisibility(View.GONE);
                whiteCheck.setVisibility(View.VISIBLE);
                recreate();
            }
        });


        whiteCheck.setTextSize(size);
    }

    @Override
    public void onBackPressed() {
        bundle = getIntent().getExtras();
        String lastPage = bundle.getString("Last Page");
        Intent settingsIntent = new Intent(ColorSchemes.this, Settings.class);
        settingsIntent.putExtra("Last Page", lastPage);
        startActivity(settingsIntent);
    }

}
