package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Controller.CustomPercent;
import edu.hope.cs.bilancioandroid.R;

public class ItemAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<String> items = new ArrayList();
    ArrayList<String> money = new ArrayList();
    ArrayList<String> percent = new ArrayList();
    Context context;
    boolean focus = false;

    public ItemAdapter(Context context, ArrayList<String> i,
                       ArrayList<String> m, ArrayList<String> p){
        this.context = context;
        items = i;
        money = m;
        percent = p;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return percent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //ViewHolder holder = null;
        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.budget_item, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.name);
            holder.editText1 = (CustomPercent) convertView.findViewById(R.id.percent);
            holder.editText2 = (CustomPercent) convertView.findViewById(R.id.money);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.ref = position;

        holder.textView1.setText(items.get(position));
        holder.editText1.setText(percent.get(position));
        holder.editText2.setText(money.get(position));
        holder.editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(focus) {
                    if (context instanceof SetUp) {
                        ((SetUp) context).fillBudgets();
                    }
                    focus = false;
                }
                else{
                    focus = true;
                }
            }
        });
        holder.editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE|| actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String entry = holder.editText1.getText().toString().trim();
                    if (entry.equals("")) {
                    } else {
                        entry = entry.replaceAll("[^\\d.]", "");
                        Double next = Double.parseDouble(entry);
                        DecimalFormat df = new DecimalFormat("0.00");
                        entry = df.format(next);
                        entry = entry + "%";
                        percent.remove(position);
                        percent.add(position, entry);
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (context instanceof SetUp) {
                            ((SetUp) context).fillBudgets();
                        }
                    }
                }
                return true;
            }
        });
        holder.editText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE|| actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String entry = holder.editText2.getText().toString().trim();
                    entry = entry.replaceAll("[^\\d.]", "");
                    if (entry.equals("")) {
                    } else {
                        Double next = Double.parseDouble(entry);
                        DecimalFormat df = new DecimalFormat("0.00");
                        entry = df.format(next);
                        entry = "$" + entry;
                        money.remove(position);
                        money.add(position, entry);
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (context instanceof SetUp) {
                            ((SetUp) context).resetPercents();
                        }
                    }
                }
                return true;
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView textView1;
        CustomPercent editText1;
        CustomPercent editText2;
        int ref;
    }

    public ArrayList<String> getMoney() {
        return money;
    }

    public ArrayList<String> getPercent() {
        return percent;
    }
}

