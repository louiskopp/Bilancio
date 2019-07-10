package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Model.Goal;
import edu.hope.cs.bilancioandroid.R;

public class SavingsAdapter extends BaseAdapter {

    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<Double> savingsTotals = new ArrayList<Double>();
    private ArrayList<Double> savingsSoFars = new ArrayList<Double>();
    private ArrayList<Goal> goals;
    private Context context;
    private LayoutInflater inflater;
    SharedPreferences prefs;
    private static final int FINISHED_GOAL = 0;
    private static final int NOT_FINISHED = 1;

    public SavingsAdapter(Context context, ArrayList<Goal> goals) {
        this.context = context;
        this.goals = goals;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return goals.size();
    }

    @Override
    public int getItemViewType(int position){
        if(goals.get(position).getAmountSaved()==goals.get(position).getAmountTotal()){
            return FINISHED_GOAL;
        }
        else{
            return NOT_FINISHED;
        }
    }

    @Override
    public Object getItem(int position) {
        return goals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public TextView titeTextView;
        public TextView toGoTextView;
        public ProgressBar progSaved;
        public TextView displayAmt;
        public ConstraintLayout relativeLayout;
        public Button button;
        int ref;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        int size = prefs.getInt("Text Size", 20);
        int type = getItemViewType(position);
        // Inflate the list_item.xml file if convertView is null
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case NOT_FINISHED:
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.savings_item, null);
                holder.titeTextView = (TextView) convertView.findViewById(R.id.savings_title);
                holder.titeTextView.setTextSize(size);
                holder.displayAmt = (TextView) convertView.findViewById(R.id.amount_saved_item);
                holder.displayAmt.setTextSize(size);
                holder.toGoTextView = (TextView) convertView.findViewById(R.id.to_go_savings);
                holder.toGoTextView.setTextSize(size);
                holder.progSaved = (ProgressBar) convertView.findViewById(R.id.progress_savings);
                holder.relativeLayout = (ConstraintLayout) convertView.findViewById(R.id.savings_item_each);
                    holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                            a_builder.setMessage("Would you like to delete this savings goal? If you delete this goal accidentally, you will have to remake the goal," +
                                    "but none of your savings will be assigned until the next budget cycle.").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(context instanceof Savings) {
                                                ((Savings) context).deleteGoal(goals.get(position));
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert = a_builder.create();
                            alert.setTitle("Delete Goal");
                            alert.show();
                            return true;
                        }
                    });
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(context instanceof Savings) {
                            ((Savings) context).goToEditGoal(goals.get(position));
                        }
                    }
                });
                    break;
                case FINISHED_GOAL:
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.finished_goal_item, null);
                    holder.titeTextView = (TextView) convertView.findViewById(R.id.category_finished);
                    holder.titeTextView.setTextSize(size);
                    holder.progSaved = (ProgressBar) convertView.findViewById(R.id.finished_prog_bar);
                    holder.displayAmt = (TextView) convertView.findViewById(R.id.how_much_done_saved);
                    holder.displayAmt.setTextSize(size);
                    holder.button = (Button) convertView.findViewById(R.id.finish_goal_button);
                    break;
            }
            convertView.setTag(holder);
        }

        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;
        Double t = 100-(goals.get(position).getAmountTotal()-goals.get(position).getAmountSaved())/goals.get(position).getAmountTotal()*100;
        int i = (int) Math.round(t);
        holder.progSaved.setProgress(i);
        holder.progSaved.setScaleY(4f);
        holder.titeTextView.setText(goals.get(position).getNameOfGoal());
        Double total = goals.get(position).getAmountTotal();
        Double saved = goals.get(position).getAmountSaved();
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(total-saved);
        String amt = df.format(saved);
        amt = "$"+amt;
        holder.displayAmt.setText(amt);
        holder.displayAmt.bringToFront();
        entry = "$"+entry;
        if(type==NOT_FINISHED) {
            holder.toGoTextView.setText(entry + " to go");
        }
        entry = df.format(saved);
        entry = "$"+entry;
        if(type==FINISHED_GOAL) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof Savings) {
                        ((Savings) context).finishedPopUp(goals.get(position));
                    }
                }
            });
        }
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
