package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.Converters;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Transaction;
import edu.hope.cs.bilancioandroid.R;

public class OverViewAdapter extends BaseAdapter {

    private ArrayList<Budget> budgets;
    private Double expenses = 0.0;
    private Context context;
    private LayoutInflater inflater;
    Converters converters;
    private Resources res;
    private Double outOfExtra = 0.0;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    int progress;


    public OverViewAdapter(Context context, ArrayList<Budget> budgets, int progress) {
        this.context = context;
        this.budgets = budgets;
        res = context.getResources();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.progress = progress;
        for (Budget budget : budgets) {
            if (budget.getHowMuchOver() != null) {
                outOfExtra += Double.parseDouble(budget.getHowMuchOver().replaceAll("[^\\d.]", ""));
            }
        }
        prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName), context.MODE_PRIVATE);
        editor = prefs.edit();
        if (prefs.getString("Teaching Mode", "Off").equals("On")) {
            context.setTheme(R.style.TeachingMode);
        } else {
            context.setTheme(prefs.getInt("Theme", R.style.White));
        }
    }

    @Override
    public int getCount() {
        return budgets.size();
    }

    @Override
    public Object getItem(int position) {
        return budgets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public TextView categoryTextView;
        public TextView leftTextView;
        public ProgressBar progLeft;
        public TextView image;
        public TextView moneyTextView;
        public TextView colorblind;
        int ref;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int size = prefs.getInt("Text Size", 18);
        // Inflate the list_item.xml file if convertView is null
        if (convertView == null) {
            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (prefs.getInt("Theme", R.style.White) == R.style.Colorblind) {
                if(prefs.getInt("Text Size", 18) == 22) {
                    convertView = inflater.inflate(R.layout.overview_colorblind_item_large, null);
                    holder.colorblind = (TextView) convertView.findViewById(R.id.check_or_ex);
                } else if(prefs.getInt("Text Size", 18) == 22) {
                    convertView = inflater.inflate(R.layout.overview_colorblind_item_medium, null);
                    holder.colorblind = (TextView) convertView.findViewById(R.id.check_or_ex);
                } else {
                    convertView = inflater.inflate(R.layout.overview_colorblind_item_small, null);
                    holder.colorblind = (TextView) convertView.findViewById(R.id.check_or_ex);
                }
                holder.colorblind.setTextSize(size);
            } else {
                if(prefs.getInt("Text Size", 18) == 22) {
                    convertView = inflater.inflate(R.layout.overview_item_large, null);
                } else if(prefs.getInt("Text Size", 18) == 18) {
                    convertView = inflater.inflate(R.layout.overview_item_medium, null);
                } else {
                    convertView = inflater.inflate(R.layout.overview_item_small, null);
                }
            }
            holder.categoryTextView = (TextView) convertView.findViewById(R.id.category_overview);
            holder.categoryTextView.setTextSize(size);
            holder.leftTextView = (TextView) convertView.findViewById(R.id.how_much_left);
            holder.leftTextView.setTextSize(size);
            holder.progLeft = (ProgressBar) convertView.findViewById(R.id.overview_progbar);
            holder.image = (TextView) convertView.findViewById(R.id.overview_imageview);
            holder.moneyTextView = (TextView) convertView.findViewById(R.id.money_in_bar);
            holder.moneyTextView.setTextSize(size);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;
        holder.categoryTextView.setText(budgets.get(position).getCategory());
        holder.leftTextView.setTextColor(Color.BLACK);
        Double left = budgets.get(position).getAmount();
        DecimalFormat df = new DecimalFormat("0.00");
        int color = 0;
        if (prefs.getString("Teaching Mode", "").equals("On")) {
            color = R.style.TeachingMode;
        } else {
            color = prefs.getInt("Theme", R.style.White);
        }
        if (left == 0.0) {
            if (budgets.get(position).getHowMuchOver() == null) {
                holder.leftTextView.setText("$0 left");


                switch (color) {
                    case (R.style.BlueTheme):
                        holder.leftTextView.setTextColor(Color.parseColor("#ff8282"));
                        break;
                    case (R.style.RedTheme):
                        holder.leftTextView.setTextColor(Color.parseColor("#ff8aba"));
                        break;
                    case (R.style.GrayTheme):
                        holder.leftTextView.setTextColor(Color.parseColor("#cf3326"));
                        break;
                    case (R.style.Black):
                        holder.leftTextView.setTextColor(Color.RED);
                        break;
                    case (R.style.Colorblind):
                        holder.leftTextView.setTextColor(Color.RED);
                        break;
                    case (R.style.White):
                        holder.leftTextView.setTextColor(Color.RED);
                        break;
                    case (R.style.TeachingMode):
                        holder.leftTextView.setTextColor(Color.RED);
                        break;
                }
            } else {
                holder.leftTextView.setText(budgets.get(position).getHowMuchOver() + " over");
                if (prefs.getString("Teaching Mode", "").equals("On")) {
                    color = R.style.TeachingMode;
                } else {
                    color = prefs.getInt("Theme", R.style.White);
                }

                switch (color) {
                    case (R.style.BlueTheme):
                        holder.leftTextView.setTextColor(Color.parseColor("#ff8282"));
                        break;
                    case (R.style.RedTheme):
                        holder.leftTextView.setTextColor(Color.parseColor("#ff8aba"));
                        break;
                    case (R.style.GrayTheme):
                        holder.leftTextView.setTextColor(Color.parseColor("#cf3326"));
                        break;
                    case (R.style.Black):
                        holder.leftTextView.setTextColor(Color.RED);
                        break;
                    case (R.style.Colorblind):
                        holder.leftTextView.setTextColor(Color.RED);
                        break;
                    case (R.style.White):
                        holder.leftTextView.setTextColor(Color.RED);
                        break;
                    case (R.style.TeachingMode):
                        holder.leftTextView.setTextColor(Color.RED);
                        break;
                }
            }
        }else {
            String entry = df.format(left);
            entry = "$" + entry;
            holder.leftTextView.setText(entry + " left");
            if (prefs.getString("Teaching Mode", "").equals("On")) {
                color = R.style.TeachingMode;
            } else {
                color = prefs.getInt("Theme", R.style.White);
            }
        switch (color) {
            case (R.style.BlueTheme):
                holder.leftTextView.setTextColor(Color.WHITE);
                break;
            case (R.style.RedTheme):
                holder.leftTextView.setTextColor(context.getResources().getColor(R.color.colorAccent1));
                break;
            case (R.style.GrayTheme):
                holder.leftTextView.setTextColor(Color.WHITE);
                break;
            case (R.style.Black):
                holder.leftTextView.setTextColor(context.getResources().getColor(R.color.colorAccent4));
                break;
            case (R.style.Colorblind):
                holder.leftTextView.setTextColor(Color.BLUE);
                break;
            case (R.style.White):
                holder.leftTextView.setTextColor(Color.BLACK);
                break;
            case(R.style.TeachingMode):
                holder.leftTextView.setTextColor(Color.WHITE);
                break;
        }
        }

        if (budgets.get(position).getTransactions() != null) {
            String tr = budgets.get(position).getTransactions();
            ArrayList<Transaction> tra = converters.fromString(tr);
            if (tra != null) {
                if (budgets.get(position).getCategory().equals("Extra Money")) {
                } else {
                    for (Transaction transaction1 : tra) {
                        expenses += transaction1.getAmount();
                    }
                }
            }
        }
        if(holder.categoryTextView.getText().equals("Extra Money")){
            holder.moneyTextView.setText("");
        }
        else {
            if (expenses == 0.0) {
                String entry = df.format(expenses);
                entry = "$" + entry;
                holder.moneyTextView.setText(entry);
            } else {
                String entry = df.format(-expenses);
                entry = "$" + entry;
                holder.moneyTextView.setText(entry);
            }
        }
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}
        };
        int green = 0;
        int yellow = 0;
        int red = 0;
        int[] positive = new int[] {
                Color.GREEN
        };
        int[] regular = new int[] {
                Color.YELLOW
        };
        int[] negative = new int[] {
                Color.RED
        };
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

        ColorStateList good = new ColorStateList(states, positive);
        ColorStateList okay = new ColorStateList(states, regular);
        ColorStateList bad = new ColorStateList(states, negative);
        if(budgets.get(position).getCategory().equals("Extra Money")){
            Double n = (left)/(budgets.get(position).getAllotted())*100;
            int t = (int) Math.round(n);
            holder.progLeft.setProgress(t);
            if(budgets.get(position).getAmount()<budgets.get(position).getAllotted()-budgets.get(position).getAllotted()*.9){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.progLeft.setProgressTintList(bad);
                }
                else {
                    Drawable progressDrawable = holder.progLeft.getProgressDrawable().mutate();
                    progressDrawable.setColorFilter(red, PorterDuff.Mode.SRC_IN);
                    holder.progLeft.setProgressDrawable(progressDrawable);
                }
                if(prefs.getInt("Theme", 0)==R.style.Colorblind){
                    holder.colorblind.setBackground(context.getResources().getDrawable(R.mipmap.red_x));
                }
            }
            else if(budgets.get(position).getAmount()<budgets.get(position).getAllotted()-budgets.get(position).getAllotted()*.7){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.progLeft.setProgressTintList(okay);
                }
                else {
                    Drawable progressDrawable = holder.progLeft.getProgressDrawable().mutate();
                    progressDrawable.setColorFilter(yellow, PorterDuff.Mode.SRC_IN);
                    holder.progLeft.setProgressDrawable(progressDrawable);
                }
                if(prefs.getInt("Theme", 0)==R.style.Colorblind){
                    holder.colorblind.setBackground(context.getResources().getDrawable(R.mipmap.caution));
                }
            }
            else{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.progLeft.setProgressTintList(good);
                }
                else {
                    Drawable progressDrawable = holder.progLeft.getProgressDrawable().mutate();
                    progressDrawable.setColorFilter(green, PorterDuff.Mode.SRC_IN);
                    holder.progLeft.setProgressDrawable(progressDrawable);
                }
                if(prefs.getInt("Theme", 0)==R.style.Colorblind){
                    holder.colorblind.setBackground(context.getResources().getDrawable(R.drawable.checkbox_blue));
                }
            }
        }
        else{
            Double n = 100-(left)/(budgets.get(position).getAllotted())*100;
            int t = (int) Math.round(n);
            holder.progLeft.setProgress(t);
            if(n>99.999){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.progLeft.setProgressTintList(bad);
                }
                else {
                    Drawable progressDrawable = holder.progLeft.getProgressDrawable().mutate();
                    progressDrawable.setColorFilter(red, PorterDuff.Mode.SRC_IN);
                    holder.progLeft.setProgressDrawable(progressDrawable);
                }
                if(prefs.getInt("Theme", 0)==R.style.Colorblind){
                    holder.colorblind.setBackground(context.getResources().getDrawable(R.mipmap.red_x));
                }

            }
            else if(n+2>progress&&n<99.999){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.progLeft.setProgressTintList(okay);
                }
                else {
                    Drawable progressDrawable = holder.progLeft.getProgressDrawable().mutate();
                    progressDrawable.setColorFilter(yellow, PorterDuff.Mode.SRC_IN);
                    holder.progLeft.setProgressDrawable(progressDrawable);
                }
                if(prefs.getInt("Theme", 0)==R.style.Colorblind){
                    holder.colorblind.setBackground(context.getResources().getDrawable(R.mipmap.caution));
                }
            }
            else {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.progLeft.setProgressTintList(good);
                }
                else {
                    Drawable progressDrawable = holder.progLeft.getProgressDrawable().mutate();
                    progressDrawable.setColorFilter(green, PorterDuff.Mode.SRC_IN);
                    holder.progLeft.setProgressDrawable(progressDrawable);
                }
                if(prefs.getInt("Theme", 0)==R.style.Colorblind){
                    holder.colorblind.setBackground(context.getResources().getDrawable(R.drawable.checkbox_blue));
                }
            }
        }
        if(size == 14) {
            holder.progLeft.setScaleY(5f);
        } else if (size == 18) {
            holder.progLeft.setScaleY(7f);
        } else {
            holder.progLeft.setScaleY(9f);
        }
        int f = budgets.get(position).getImage();
        holder.image.setBackground(res.getDrawable(f));
        expenses = 0.0;
        holder.moneyTextView.bringToFront();
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

