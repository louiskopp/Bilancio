package edu.hope.cs.bilancioandroid;

import android.arch.persistence.room.Room;
import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import java.io.IOException;
import java.util.ArrayList;

import edu.hope.cs.bilancioandroid.Database.AppDatabase;
import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Database.BudgetDao;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DataBaseTest {

    private BudgetDao budgetDao;
    private AppDatabase db;


    @Before
    public void createDb() {

        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        budgetDao = db.budgetDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsertandFind() throws Exception {
        Budget budget = new Budget(5,"Rent", 500, "Where you live");
        budgetDao.insert(budget);
        Budget budget1 = db.budgetDao().findByCategory("Rent");
        assertEquals(budget.getCategory(), budget1.getCategory());
        assertEquals(budget.getAmount(), budget1.getAmount());
        assertEquals(budget.getDescription(), budget1.getDescription());
        assertEquals(budget.getUid(), budget1.getUid());
        db.budgetDao().deleteAll();
        ArrayList<Budget> budgets = (ArrayList<Budget>) db.budgetDao().getAll();
        assertEquals(budgets.size(), 0);
    }
}
