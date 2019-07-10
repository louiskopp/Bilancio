package edu.hope.cs.bilancioandroid.Controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import java.text.DecimalFormat;

public class CustomPercent extends EditText {
    public CustomPercent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPercent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }
}
