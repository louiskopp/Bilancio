package edu.hope.cs.bilancioandroid.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Database.TeachingModeDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class OverallBudgetAdapter extends BaseAdapter {

    private ArrayList<Budget> budgets;
    ArrayList<Transaction> full = new ArrayList<Transaction>();
    private Context context;
    private LayoutInflater inflater;
    private Converters converters;
    private AppDatabase db;
    private static final String DATABASE_NAME = "budget_db";
    TeachingModeDatabase tDb;
    private static final String TEACHING = "teaching_db";
    SharedPreferences prefs;
    int color;
    private int drawable;
    private Resources res;

    public OverallBudgetAdapter(Context context, ArrayList<Transaction> full) {
        this.context = context;
        res = context.getResources();
        prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        db = Room.databaseBuilder(context,
                AppDatabase.class, DATABASE_NAME).build();
        tDb = Room.databaseBuilder(context,
                TeachingModeDatabase.class, TEACHING).build();
        this.budgets = budgets;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.full = full;
    }

    @Override
    public int getCount() {
        if(full==null){return 0;}
        else {
            return full.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(full==null){return null;}
        else {
            return full.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public TextView category;
        public TextView dateStr;
        public TextView moneyAmount;
        public TextView store;
        public TextView icon;
        public ConstraintLayout constraintLayout;
        int ref;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final int size = prefs.getInt("Text Size", 18);
        final ViewHolder holder;
        // Inflate the list_item.xml file if convertView is null
        if (convertView == null) {
            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.overall_budget_item, null);
            holder.category = (TextView) convertView.findViewById(R.id.money_overall_budgets);
            holder.category.setTextSize(size);
            holder.dateStr = (TextView) convertView.findViewById(R.id.store_overall_budgets);
            holder.dateStr.setTextSize(size);
            holder.moneyAmount = (TextView) convertView.findViewById(R.id.category_overall_budgets);
            holder.moneyAmount.setTextSize(size);
            holder.store = (TextView) convertView.findViewById(R.id.date_overall_budgets);
            holder.store.setTextSize(size);
            holder.icon = (TextView) convertView.findViewById(R.id.overall_budget_icons);
            holder.icon.setTextSize(size);
            holder.constraintLayout = (ConstraintLayout) convertView.findViewById(R.id.overall_item);
            convertView.setTag(holder);
        }

        else{
            holder = (ViewHolder) convertView.getTag();
        }
        if (prefs.getString("Teaching Mode", "").equals("On")) {
            color = R.style.TeachingMode;
        } else {
            color = prefs.getInt("Theme", R.style.White);
        }
        int[] positive = new int[] {
                Color.GREEN
        };
        int[] regular = new int[] {
                Color.YELLOW
        };
        int[] negative = new int[] {
                Color.RED
        };
        int green = 0;
        int yellow = 0;
        int red = 0;
        switch(color){
            case(R.style.BlueTheme):
                positive = new int[] {
                        Color.parseColor("#A3FFA3")
                };
                green = Color.parseColor("#A3FFA3");
                regular = new int[] {
                        Color.parseColor("#ffe863")
                };
                yellow = Color.parseColor("#ffe863");
                negative = new int[] {
                        Color.parseColor("#ff8282")
                };
                red = Color.parseColor("#ff8282");
                break;
            case(R.style.RedTheme):
                positive = new int[] {
                        Color.parseColor("#94ff2e")
                };
                green = Color.parseColor("#94ff2e");
                regular = new int[] {
                        Color.parseColor("#ffde00")
                };
                yellow =  Color.parseColor("#ffde00");
                negative = new int[] {
                        Color.parseColor("#ff8aba")
                };
                red = Color.parseColor("#ff8aba");
                break;
            case(R.style.GrayTheme):
                positive = new int[] {
                        Color.parseColor("#1c851c")
                };
                green = Color.parseColor("#1c851c");
                regular = new int[] {
                        Color.parseColor("#ffe812")
                };
                yellow = Color.parseColor("#ffe812");
                negative = new int[] {
                        Color.parseColor("#cf3326")
                };
                red =  Color.parseColor("#cf3326");
                break;
            case(R.style.Black):
                positive = new int[] {
                        Color.parseColor("#018901")
                };
                green = Color.parseColor("#018901");
                regular = new int[] {
                        Color.parseColor("#fded3a")
                };
                yellow = Color.parseColor("#fded3a");
                negative = new int[] {
                        Color.parseColor("#ff0000")
                };
                red = Color.parseColor("#ff0000");
                break;
            case(R.style.Colorblind):
                positive = new int[] {
                        context.getResources().getColor(R.color.colorPrimaryDark)
                };
                green = context.getResources().getColor(R.color.colorPrimaryDark);
                regular = new int[] {
                        Color.parseColor("#ffa800")
                };
                yellow = Color.parseColor("#ffa800");
                negative = new int[] {
                        Color.parseColor("#ff0000")
                };
                red =  Color.parseColor("#ff0000");
                break;
            case(R.style.White):
                positive = new int[] {
                        Color.parseColor("#018901")
                };
                green =  Color.parseColor("#018901");
                regular = new int[] {
                        Color.parseColor("#ffe600")
                };
                yellow =  Color.parseColor("#ffe600");
                negative = new int[] {
                        Color.parseColor("#ff0000")
                };
                red = Color.parseColor("#ff0000");
                break;
            case(R.style.TeachingMode):
                positive = new int[] {
                        context.getResources().getColor(R.color.budgetTotalColor)
                };
                green = context.getResources().getColor(R.color.budgetTotalColor);
                regular = new int[] {
                        context.getResources().getColor(R.color.colorAccent1)
                };
                yellow = context.getResources().getColor(R.color.colorAccent1);
                negative = new int[] {
                        Color.parseColor("#ff0000")
                };
                red = Color.parseColor("#ff0000");
                break;
        }

        holder.ref = position;
        holder.category.setText(full.get(position).getCategory());
        holder.dateStr.setText(full.get(position).getDate());
        holder.store.setText(full.get(position).getStore());
        Double money = full.get(position).getAmount();
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = df.format(money);
        entry = "$" + entry;
        holder.moneyAmount.setText(entry);
        if (money < 0) {
            holder.moneyAmount.setTextColor(red);
        }
        else{
            holder.moneyAmount.setTextColor(green);
        }
        Thread go = new Thread(new Runnable() {
            @Override
            public void run() {
                if(prefs.getString("Teaching Mode", "Off").equals("On")) {
                    Budget budget = tDb.teachingModeDao().findByCategory(full.get(position).getCategory());
                    if (budget == null) {
                        Budget budget1 = tDb.teachingModeDao().findByCategory("Extra Money");
                        drawable = budget1.getImage();
                    } else {
                        drawable = budget.getImage();
                    }
                }
                else {
                    Budget budget = db.budgetDao().findByCategory(full.get(position).getCategory());
                    if (budget == null) {
                        Budget budget1 = db.budgetDao().findByCategory("Extra Money");
                        drawable = budget1.getImage();
                    } else {
                        drawable = budget.getImage();
                    }
                }
            }
        });
        go.start();
        try {
            go.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        holder.icon.setBackground(res.getDrawable(drawable));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
