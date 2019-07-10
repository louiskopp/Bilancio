package edu.hope.cs.bilancioandroid.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import edu.hope.cs.bilancioandroid.Model.Reminder;

@Database(entities = {Reminder.class}, version = 1)
public abstract class ReminderDatabase extends RoomDatabase {
    public abstract ReminderDao reminderDao();
}