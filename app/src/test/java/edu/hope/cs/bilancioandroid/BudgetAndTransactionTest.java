package edu.hope.cs.bilancioandroid;

import org.junit.Test;

import edu.hope.cs.bilancioandroid.Model.Budget;
import edu.hope.cs.bilancioandroid.Model.Transaction;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BudgetAndTransactionTest {
    Transaction t1 = new Transaction(15.00, "Wesco", "Transportation", "June 6, 2018");
    Transaction t2 = new Transaction(21.45, "Petco", "Home Care", "July 7, 2018");

    @Test
    public void checkGetAmount() {
        assertEquals(15.00, t1.getAmount(), 0.001);
        assertEquals(21.45, t2.getAmount(), 0.001);
    }

    @Test
     public void checkGetStore() {
         assertEquals("Wesco", t1.getStore());
         assertEquals("Petco", t2.getStore());
     }

     @Test
     public void checkGetCategory() {
         assertEquals("Transportation", t1.getCategory());
         assertEquals("Home Care", t2.getCategory());
     }

     @Test
     public void checkGetDate() {
        assertEquals("June 6, 2018", t1.getDate());
        assertEquals("July 7, 2018", t2.getDate());
    }
    Budget budge = new Budget(5,"Clothing",85.00,"Money allotted to buy clothes.");
    Budget budge2 = new Budget(7, "Utilities", 150.00, "Water, heat/electricity, trash, recycle, and cable/internet");

    @Test
    public void checkUID() {
        assertEquals(5, budge.getUid());
        assertEquals(7, budge2.getUid());
        budge.setUid(7);
        budge2.setUid(9);
        assertEquals(7,budge.getUid());
        assertEquals(9, budge2.getUid());
    }

    @Test
    public void checkBudgetCategory() {
        assertEquals("Clothing", budge.getCategory());
        budge.setCategory("Utilities");
        assertEquals("Utilities", budge.getCategory());
        assertTrue(budge.getCategory().equals(budge2.getCategory()));
    }

    @Test
    public void checkBudgetAmount() {
        assertEquals(85.00, budge.getAmount(), 0.001);
        budge.setAmount(140.00);
        assertEquals(140.00, budge.getAmount(), 0.001);
        assertFalse(budge.getAmount() == budge2.getAmount());
    }
    @Test
    public void checkBudgetDescription() {
        assertEquals("Money allotted to buy clothes.", budge.getDescription());
        assertEquals("Water, heat/electricity, trash, recycle, and cable/internet", budge2.getDescription());
        budge2.setDescription("This is to test for the description change");
        assertFalse(budge2.getDescription().equals("Water, heat/electricity, trash, recycle, and cable/internet"));

    }



}