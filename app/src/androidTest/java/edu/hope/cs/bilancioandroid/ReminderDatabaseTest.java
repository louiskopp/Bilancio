package edu.hope.cs.bilancioandroid;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ArrayAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import edu.hope.cs.bilancioandroid.Database.ReminderDao;
import edu.hope.cs.bilancioandroid.Database.ReminderDatabase;
import edu.hope.cs.bilancioandroid.Model.Reminder;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ReminderDatabaseTest {

    private ReminderDao reminderDao;
    private ReminderDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, ReminderDatabase.class).build();
        reminderDao = db.reminderDao();
    }

   @After
    public void closeDb() throws IOException{
        db.close();
   }

   @Test
   public void testInsert(){
       Reminder reminder = new Reminder(5, "Pay Rent", "Bill", "July 12,2017");
       reminder.setHowOften("One Week");
       db.reminderDao().insertAll(reminder);
       Reminder reminder1 = db.reminderDao().findByTitle("Pay Rent");
       assertEquals(reminder.getDate(), reminder1.getDate());
       assertEquals(reminder.getTitle(), reminder1.getTitle());
       assertEquals(reminder.getType(), reminder1.getType());
       assertEquals(reminder.getUid(), reminder1.getUid());
       assertEquals(reminder.getHowOften(), reminder1.getHowOften());
       db.reminderDao().delete(reminder1);
       ArrayList<Reminder> reminders = (ArrayList<Reminder>) db.reminderDao().getAll();
       assertEquals(reminders.size(), 0);
   }
}
