package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.R;

public class IconAdapter extends BaseAdapter {

    ArrayList<Integer> icons = new ArrayList<Integer>(){{
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

    ArrayList<Integer> icons2 = new ArrayList<Integer>(){{
        add(R.drawable.eating_black);
        add(R.drawable.fitness_black);
        add(R.drawable.fuel_black);
        add(R.drawable.gifts_black);
        add(R.drawable.homecare_black);
        add(R.drawable.investments_black);
        add(R.drawable.dental_black);
        add(R.drawable.add_income_black);
        add(R.drawable.savings_icon_black);
        add(R.drawable.taxi_black);
        add(R.drawable.airplane_black);
    }};
    Context context;
    private LayoutInflater inflater;
    public IconAdapter(Context context){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        public ImageView imageView;
        public ImageView imageView2;
        int ref;

    }

    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Object getItem(int position) {
        return icons.get(position);
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
            convertView= inflater.inflate(R.layout.icon_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.one_icon);
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.two_icon);
            convertView.setTag(holder);

        }

        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(icons.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof SelectIcon){
                    ((SelectIcon) context).getIcon(icons.get(position));
                }
            }
        });
        holder.imageView2.setImageResource(icons2.get(position));
        holder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof SelectIcon){
                    ((SelectIcon) context).getIcon(icons2.get(position));
                }
            }
        });
        return convertView;
    }
}
