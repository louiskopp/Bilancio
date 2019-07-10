package edu.hope.cs.bilancioandroid;

import org.junit.Test;

import edu.hope.cs.bilancioandroid.Model.Goal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class GoalTest {
    Goal goal = new Goal(1,"New Car", 3300.00, 0.00);

    @Test
    public void checkUID() {
        assertEquals(1, goal.getUid());
        goal.setUid(7);
        assertEquals(7, goal.getUid());
    }

    @Test
    public void checkName() {
        assertEquals("New Car", goal.getNameOfGoal());
        goal.setNameOfGoal("First Car");
        assertEquals("First Car", goal.getNameOfGoal());
    }

    @Test
    public void checkAmountTotal() {
        assertEquals(3300.00, goal.getAmountTotal(), .001);
        goal.setAmountTotal(3000.00);
        assertEquals(3000.00,goal.getAmountTotal(), .001);
    }

    @Test
    public void checkAmountSaved() {
        assertEquals(0.00, goal.getAmountSaved(), 0.001);
        goal.setAmountSaved(100.00);
        assertEquals(100.00, goal.getAmountSaved(), 0.001);
    }

    @Test
    public void checkDate() {
        assertNull(goal.getDate());
        goal.setDate("6/11/18");
        assertEquals("6/11/18", goal.getDate());
    }


}
