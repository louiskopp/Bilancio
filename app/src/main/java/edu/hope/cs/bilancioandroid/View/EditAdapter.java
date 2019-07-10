package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Controller.CustomPercent;
import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class EditAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Budget> budgets;
    private LayoutInflater inflater;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    SharedPreferences prefs;
    private boolean focus = false;
    private Converters converters;
    SharedPreferences.Editor editor;

    private Resources res;

    public EditAdapter(Context context) {
        this.context = context;
        res = context.getResources();
        prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        db = Room.databaseBuilder(context,
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(context,
                TeachingModeDatabase.class, TEACHING).build();
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    budgets = (ArrayList<Budget>) tDb.teachingModeDao().getAll();
                }
                else {
                    budgets = (ArrayList<Budget>) db.budgetDao().getAll();
                }
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        public TextView textview;
        public TextView button;
        public TextView imageView;
        public CustomPercent editText;
        int ref;

    }

    @Override
    public int getCount() {
        return budgets.size();
    }

    @Override
    public Budget getItem(int position) {
        return budgets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final int size = prefs.getInt("Text Size", 18);
        // Inflate the list_item.xml file if convertView is null
        if (convertView == null) {
            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.edit_item, null);
            holder.textview = (TextView) convertView.findViewById(R.id.category_edit_budget);
            holder.textview.setTextSize(size);
            holder.button = (TextView) convertView.findViewById(R.id.delete_budget);
            holder.button.setTextSize(size);
            holder.editText = (CustomPercent) convertView.findViewById(R.id.money_edit_budget);
            holder.editText.setTextSize(size);
            holder.imageView = (TextView) convertView.findViewById(R.id.icon_edit_budget);
            holder.imageView.setTextSize(size);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ref = position;
        holder.textview.setText(budgets.get(position).getCategory());
        Double money = budgets.get(position).getAllotted();
        DecimalFormat df = new DecimalFormat("0.00##");
        String inc = df.format(money);
        inc = "$" + inc;
        holder.editText.setText(inc);
        holder.editText.bringToFront();
        holder.imageView.setBackground(res.getDrawable(budgets.get(position).getImage()));
        holder.button.setBackground(res.getDrawable(R.drawable.minus_sign));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (budgets.get(position).getCategory().equals("Extra Money")) {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Extra Money is a very important part of your budget.").setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Extra Money cannot be deleted");
                    alert.show();
                } else {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Are you sure?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (context instanceof EditBudgets) {
                                        ((EditBudgets) context).deleteBudgets(budgets.get(position));
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("You are about to delete a budget category.");
                    alert.show();
                }
            }
        });
        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String entry = holder.editText.getText().toString().trim();
                    entry = entry.replaceAll("[^\\d.]", "");
                    if (entry.equals("")) {
                    } else {
                        final Double next = Double.parseDouble(entry);
                        //if (context instanceof EditBudgets) {
                        //  ((EditBudgets) context).fillBudgets(budgets.get(position), next);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Budget budget1 = budgets.get(position);
                                if (prefs.getString("Teaching Mode", "Off").equals("On")) {
                                    budget1 = tDb.teachingModeDao().findByCategory(budget1.getCategory());
                                } else {
                                    budget1 = db.budgetDao().findByCategory(budget1.getCategory());
                                }
                                Budget placeholder = budget1;
                                budget1.setAllotted(next);
                                String trans = budget1.getTransactions();
                                ArrayList<Transaction> transactions = converters.fromString(trans);
                                Double exp = 0.0;
                                if (transactions == null) {

                                } else {
                                    for (Transaction transaction : transactions) {
                                        exp += transaction.getAmount();
                                    }
                                }
                                budget1.setAmount(budget1.getAllotted() + exp);
                                if (prefs.getString("Teaching Mode", "Off").equals("On")) {
                                    tDb.teachingModeDao().delete(placeholder);
                                    tDb.teachingModeDao().insertAll(budget1);
                                } else {
                                    db.budgetDao().delete(placeholder);
                                    db.budgetDao().insertAll(budget1);
                                }
                            }
                        });
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (context instanceof EditBudgets) {
                            ((EditBudgets) context).resetDiff();
                        }
                    }
                }
            }
        });
        holder.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String entry = holder.editText.getText().toString().trim();
                    entry = entry.replaceAll("[^\\d.]", "");
                    if (entry.equals("")) {

                    } else {
                        Double next = Double.parseDouble(entry);
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        if (context instanceof EditBudgets) {
                            ((EditBudgets) context).fillBudgets(budgets.get(position), next);
                        }
                    }
                }
                return true;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Thread go = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (prefs.getString("Teaching Mode", "Off").equals("On")) {
                            Budget budget = tDb.teachingModeDao().findByCategory(budgets.get(position).getCategory());
                            tDb.teachingModeDao().delete(budget);
                        }
                        else{
                            Budget budget = db.budgetDao().findByCategory(budgets.get(position).getCategory());
                            db.budgetDao().delete(budget);
                        }
                    }
                });
                go.start();
                try {
                    go.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, AddNewBudget.class);
                Budget budget =  budgets.get(position);
                Gson gson = new Gson();
                String json = gson.toJson(budget);
                intent.putExtra("budget",json);
                editor.putString("edited budget",json);
                editor.apply();
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }
}
