package edu.hope.cs.bilancioandroid.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.hope.cs.bilancioandroid.Model.Reminder;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ReminderDao {

    @Insert(onConflict = REPLACE)
    void insert(Reminder reminder);

    @Query("SELECT * FROM reminder")
    List<Reminder> getAll();

    @Query("SELECT * FROM reminder WHERE uid IN (:reminderIds)")
    List<Reminder> loadAllByIds(int[] reminderIds);

    @Query("SELECT * FROM reminder WHERE title LIKE :title"
            + " LIMIT 1")
    Reminder findByTitle(String title);

    @Insert
    void insertAll(Reminder... reminders);

    @Delete
    void delete(Reminder reminder);

    @Query("DELETE FROM reminder")
    void deleteAll();
}
