package edu.hope.cs.bilancioandroid.View;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Controller.CustomPercent;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Scenario;
import edu.hope.cs.bilancioandroid.R;
import edu.hope.cs.bilancioandroid.View.ManualSetUp;

public class ItemAdapterManual extends BaseAdapter {

    private ArrayList<String> description = new ArrayList<String>();
    private ArrayList<Budget> budgets = new ArrayList<Budget>();
    private String[] items;
    private Context context;
    private Resources res;
    private LayoutInflater inflater;
    private int[] location = new int[2];
    private DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList<Integer> drawables = new ArrayList<Integer>(){{
        add(R.drawable.savings_black);
        add(R.drawable.clothing_black);
        add(R.drawable.education_black);
        add(R.drawable.shopping_black);
        add(R.drawable.groceries_black);
        add(R.drawable.home_black);
        add(R.drawable.medical_black);
        add(R.drawable.phone_black);
        add(R.drawable.rent_black);
        add(R.drawable.auto_care_black);
        add(R.drawable.utilities_black);
    }};
    private boolean focus = false;

    public ItemAdapterManual(Context context, ArrayList<String> itemList){
        this.context=context;
        description=itemList;
        res = context.getResources();
        items = res.getStringArray(R.array.budget_categories);
        for(int i=0; i<11; i++){
            Budget budget = new Budget(0, items[i], 0.0, description.get(i));
            budget.setAllotted(0.0);
            budget.setTransactions("");
            budget.setImage(drawables.get(i));
            budgets.add(budget);
                }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        public TextView textview;
        public Button button;
        public CustomPercent editText;
        int ref;

    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public String  getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
         // Inflate the list_item.xml file if convertView is null
        if(convertView==null){
            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.manual_budget_item, null);
            holder.textview= (TextView) convertView.findViewById(R.id.manual_name);
            holder.button= (Button) convertView.findViewById(R.id.information);
            holder.editText = (CustomPercent) convertView.findViewById(R.id.money_manual);
            convertView.setTag(holder);

        }

        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ref = position;
        holder.textview.setText(items[position]);
        holder.button.setContentDescription(description.get(position));
        holder.button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                holder.button.getLocationOnScreen(location);
                if(context instanceof ManualSetUp){
                    ((ManualSetUp)context).displayInfo(holder.button, location);
                }
            }
        });
        Double current = budgets.get(position).getAllotted();
        String entry = df.format(current);
        entry = "$" + entry;
        holder.editText.setText(entry);
        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(focus) {
                    String entry = holder.editText.getText().toString().trim();
                    entry = entry.replaceAll("[^\\d.]", "");
                    if (entry.equals("")) {
                    } else {
                        Double next = Double.parseDouble(entry);
                        budgets.get(position).setAllotted(next);
                        budgets.get(position).setAmount(next);
                        String now = df.format(next);
                        now = "$" + now;
                        holder.editText.setText(now);
                        focus = false;
                    }
                }
                else{
                    focus = true;
                }
            }
        });
        holder.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE|| actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String entry = holder.editText.getText().toString().trim();
                    entry = entry.replaceAll("[^\\d.]", "");
                    if (entry.equals("")) {
                    } else {
                        Double next = Double.parseDouble(entry);
                        budgets.get(position).setAllotted(next);
                        budgets.get(position).setAmount(next);
                        String now = df.format(next);
                        now = "$" + now;
                        holder.editText.setText(now);
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return true;
            }
        });
        return convertView;
    }

    public ArrayList<Budget> getMoney() {
        return budgets;
    }

}
