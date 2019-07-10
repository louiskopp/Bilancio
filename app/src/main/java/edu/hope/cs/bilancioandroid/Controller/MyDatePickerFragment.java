package edu.hope.cs.bilancioandroid.Controller;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import edu.hope.cs.bilancioandroid.Model.Goal;
import edu.hope.cs.bilancioandroid.R;
import edu.hope.cs.bilancioandroid.View.AddIncome;
import edu.hope.cs.bilancioandroid.View.CanIAfford;
import edu.hope.cs.bilancioandroid.View.EditGoal;
import edu.hope.cs.bilancioandroid.View.Savings;

@SuppressLint("ValidFragment")
public class MyDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private String date;
    private Context context;
    private Goal goal;

    @SuppressLint("ValidFragment")
    public MyDatePickerFragment(Context context){
        this.context = context;
    }

    @SuppressLint("ValidFragment")
    public MyDatePickerFragment(Context context, Goal goal){
        this.context = context;
        this.goal = goal;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), R.style.datePicker,this, year, month, day);
    }

    @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    int monthInt = view.getMonth();
                    String monthStr = new DateFormatSymbols().getMonths()[monthInt];
                    date = monthStr+ " " +view.getDayOfMonth()+", "+view.getYear();

                    if(context instanceof AddIncome){
                        ((AddIncome)context).makeIncomeDate(date);
                    }

                    if(context instanceof EditGoal){
                        ((EditGoal)context).makeIncomeDate(date);
                    }

                    if(context instanceof Savings){
                        ((Savings)context).setDateOfGoal(goal, date);
                    }

                }

}
