package edu.hope.cs.bilancioandroid.Controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import java.text.DecimalFormat;

public class CustomEditText extends EditText {
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String entry = getText().toString();
            if (entry.equals("")) {
            } else {
                Double amountEntered = Double.parseDouble(entry.replaceAll("[^\\d.]", ""));
                DecimalFormat df = new DecimalFormat("0.00##");
                entry = df.format(amountEntered);
                entry = "$" + entry;
                setText(entry);
                return true;

            }
        }
        return false;
    }
}
