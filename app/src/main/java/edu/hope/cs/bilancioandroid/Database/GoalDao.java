package edu.hope.cs.bilancioandroid.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.hope.cs.bilancioandroid.Model.Goal;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface GoalDao {

    @Insert(onConflict = REPLACE)
    void insert(Goal goal);

    @Query("SELECT * FROM goal")
    List<Goal> getAll();

    @Query("SELECT * FROM goal WHERE uid IN (:goalIds)")
    List<Goal> loadAllByIds(int[] goalIds);

    @Query("SELECT * FROM goal WHERE nameOfGoal LIKE :nameOfGoal"
            + " LIMIT 1")
    Goal findByGoal(String nameOfGoal);

    @Insert
    void insertAll(Goal... goals);

    @Delete
    void delete(Goal goal);

    @Query("DELETE FROM goal")
    void deleteAll();
}
