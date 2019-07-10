package edu.hope.cs.bilancioandroid.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.hope.cs.bilancioandroid.Model.Budget;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TeachingModeDao {

    @Insert(onConflict = REPLACE)
    void insert(Budget budget);

    @Query("SELECT * FROM budget")
    List<Budget> getAll();

    @Query("SELECT * FROM budget WHERE uid IN (:budgetIds)")
    List<Budget> loadAllByIds(int[] budgetIds);

    @Query("SELECT * FROM budget WHERE category LIKE :category"
            + " LIMIT 1")
    Budget findByCategory(String category);

    @Insert
    void insertAll(Budget... budgets);

    @Delete
    void delete(Budget budget);

    @Query("DELETE FROM budget")
    void deleteAll();
}

