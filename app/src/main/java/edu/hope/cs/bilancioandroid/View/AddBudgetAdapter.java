package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class AddBudgetAdapter extends BaseAdapter {

    ArrayList<Transaction> transactions;
    String fromString;
    private LayoutInflater inflater;
    Context context;
    Converters converters;
    SharedPreferences prefs;

    public AddBudgetAdapter(Context context, String fromString){
        this.context = context;
        prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        this.fromString = fromString;
        transactions = converters.fromString(fromString);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        public TextView amount;
        public TextView date;
        public Button button;
        int ref;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final int size = prefs.getInt("Text Size", 20);
        // Inflate the list_item.xml file if convertView is null
        if(convertView==null){
            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.transaction_item_new, null);
            holder.amount = (TextView) convertView.findViewById(R.id.amount_new_transaction);
            holder.amount.setTextSize(size);
            holder.date = (TextView) convertView.findViewById(R.id.date_new_transaction);
            holder.date.setTextSize(size);
            holder.button = (Button) convertView.findViewById(R.id.delete_new_transaction);
            convertView.setTag(holder);
        }

        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.date.setText(transactions.get(position).getDate());
        double more = transactions.get(position).getAmount();
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(-more);
        entry = "$"+entry;
        holder.amount.setText(entry);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof AddNewBudget){
                    ((AddNewBudget) context).deleteTransaction(transactions.get(position));
                }
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
