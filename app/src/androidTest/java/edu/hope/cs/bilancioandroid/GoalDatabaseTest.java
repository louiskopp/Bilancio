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

import edu.hope.cs.bilancioandroid.Database.GoalDao;
import edu.hope.cs.bilancioandroid.Database.GoalDatabase;
import edu.hope.cs.bilancioandroid.Model.Goal;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class GoalDatabaseTest {

    private GoalDatabase gDb;
    private GoalDao goalDao;

    @Before
    public void createDb() {

        Context context = InstrumentationRegistry.getTargetContext();
        gDb = Room.inMemoryDatabaseBuilder(context, GoalDatabase.class).build();
        goalDao = gDb.goalDao();
    }

    @After
    public void closeDb() throws IOException {
        gDb.close();
    }

    @Test
    public void testInsert(){
        Goal goal = new Goal(5, "New Car", 200.0, 50.0);
        gDb.goalDao().insertAll(goal);
        Goal goal1 = gDb.goalDao().findByGoal("New Car");
        assertEquals(goal.getAmountSaved(), goal1.getAmountSaved());
        assertEquals(goal.getAmountTotal(), goal1.getAmountTotal());
        assertEquals(goal.getNameOfGoal(), goal1.getNameOfGoal());
        assertEquals(goal.getUid(), goal1.getUid());
    }
}
