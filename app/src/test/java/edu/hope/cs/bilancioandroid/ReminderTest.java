package edu.hope.cs.bilancioandroid;

import org.junit.Test;

import edu.hope.cs.bilancioandroid.Model.Reminder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReminderTest {
    Reminder rem = new Reminder(1, "Record income", "Income", "6/23/18");
    Reminder rem2 = new Reminder(134, "Pay water bill", "Bill", "7/1/18");

    @Test
    public void checkUID() {
        assertEquals(1, rem.getUid());
        assertEquals(134, rem2.getUid());
        rem.setUid(134);
        assertTrue(rem.getUid() == rem2.getUid());
        rem2.setUid(52);
        assertFalse(rem.getUid() == rem2.getUid());
    }

    @Test
    public void checkTitle() {
        assertEquals("Record income", rem.getTitle());
        assertEquals("Pay water bill", rem2.getTitle());
        rem.setTitle("Pay water bill");
        assertTrue(rem.getTitle().equals(rem2.getTitle()));
    }

    @Test
    public void checkType() {
        assertEquals("Income", rem.getType());
        assertEquals("Bill", rem2.getType());
        rem.setType("Bill");
        assertTrue(rem.getType().equals(rem2.getType()));
    }

    @Test
    public void checkDate() {
        assertEquals("6/23/18", rem.getDate());
        assertEquals("7/1/18", rem2.getDate());
        rem.setDate("7/1/18");
        assertTrue(rem.getDate().equals(rem2.getDate()));
    }

    @Test
    public void checkHowOften() {
        assertEquals(null, rem.getHowOften());
        assertEquals(null, rem2.getHowOften());
        rem.setHowOften("Biweekly");
        assertEquals("Biweekly", rem.getHowOften());
    }
}
