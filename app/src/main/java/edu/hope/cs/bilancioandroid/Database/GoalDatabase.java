package edu.hope.cs.bilancioandroid.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import edu.hope.cs.bilancioandroid.Model.Goal;

@Database(entities = {Goal.class}, version = 1)
public abstract class GoalDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();
}