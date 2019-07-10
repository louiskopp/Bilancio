package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.R;
import edu.hope.cs.bilancioandroid.Model.Transaction;

public class TransactionAdapter extends BaseAdapter {

    private Budget budget;
    private Context context;
    private LayoutInflater inflater;
    Converters converters;
    ArrayList<Transaction> transactions;
    SharedPreferences prefs;

    public TransactionAdapter(Context context, Budget budget) {
        this.context = context;
        this.budget  = budget;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String placeholder = budget.getTransactions();
        transactions = converters.fromString(placeholder);
        if(transactions==null){

        }
    }

    @Override
    public int getCount() {
        if(transactions==null){
            return 0;
        }
        return transactions.size();
    }

    @Override
    public Object getItem(int position) {
        if(transactions==null){
            return null;
        }
        return transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public TextView dateTextView;
        public TextView moneyTextView;
        public TextView storeTextView;
        int ref;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        int size = prefs.getInt("Text Size", 18);

        if (convertView == null) {
            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.transaction_item, null);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date_transactions);
            holder.dateTextView.setTextSize(size);

            holder.moneyTextView = (TextView) convertView.findViewById(R.id.money_transactions);
            holder.moneyTextView.setTextSize(size);

            holder.storeTextView = (TextView) convertView.findViewById(R.id.store_transactions);
            holder.storeTextView.setTextSize(size);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;
        holder.storeTextView.setText(transactions.get(position).getStore());
        Double left = transactions.get(position).getAmount();
        DecimalFormat df = new DecimalFormat("0.00");
        if(budget.getCategory().equals("Extra Money")){
            String entry = df.format(left);
            entry = "$" + entry;
            holder.moneyTextView.setText(entry);
        }
        else {
            String entry = df.format(-left);
            entry = "$" + entry;
            holder.moneyTextView.setText(entry);
        }
        String date = transactions.get(position).getDate();
        holder.dateTextView.setText(date);

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
