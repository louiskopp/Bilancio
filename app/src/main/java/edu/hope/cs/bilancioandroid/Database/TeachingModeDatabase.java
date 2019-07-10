package edu.hope.cs.bilancioandroid.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import edu.hope.cs.bilancioandroid.Model.Budget;

@Database(entities = {Budget.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class TeachingModeDatabase extends RoomDatabase {
    public abstract TeachingModeDao teachingModeDao();
}