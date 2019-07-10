package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Model.Reminder;
import edu.hope.cs.bilancioandroid.R;

public class ReminderAdapter extends BaseAdapter {

    private ArrayList<Reminder> reminders;
    private Context context;
    private LayoutInflater inflater;
    SharedPreferences prefs;

    public ReminderAdapter(Context context, ArrayList<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(reminders==null){return 0;}
        else {
            return reminders.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(reminders==null){return null;}
        else {
            return reminders.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public TextView titleView;
        public TextView typeView;
        public TextView information;
        public LinearLayout constraintLayout;
        int ref;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName), context.MODE_PRIVATE);
        int size = prefs.getInt("Text Size", 20);
        // Inflate the list_item.xml file if convertView is null
        if (convertView == null) {
            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(size == 22) {
                convertView = inflater.inflate(R.layout.reminder_item_large, null);
            } else {
                convertView = inflater.inflate(R.layout.reminder_item, null);
            }
            holder.titleView = (TextView) convertView.findViewById(R.id.title_reminder);
            holder.titleView.setTextSize(size);

            holder.typeView = (TextView) convertView.findViewById(R.id.type_reminder);
            holder.typeView.setTextSize(size);

            holder.information = (TextView) convertView.findViewById(R.id.info_reminders);
            holder.information.setTextSize(size);
            holder.constraintLayout = (LinearLayout) convertView.findViewById(R.id.reminder_item_id);
            convertView.setTag(holder);
        }

        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;
        holder.titleView.setText(reminders.get(position).getTitle());
        holder.typeView.setText(reminders.get(position).getType());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ReminderActivity) {
                    ((ReminderActivity) context).goToEditReminder(reminders.get(position));
                }
            }
        });
        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                a_builder.setMessage("Would you like to delete this reminder?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(context instanceof ReminderActivity) {
                                    ((ReminderActivity) context).deleteReminder(reminders.get(position));
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
                alert.setTitle("Delete Reminder");
                alert.show();
                return true;
            }
        });
        holder.information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ReminderActivity) {
                    ((ReminderActivity) context).buttonPopUp();
                }
            }
        });
        holder.information.bringToFront();

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
