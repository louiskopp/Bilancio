package edu.hope.cs.bilancioandroid;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import edu.hope.cs.bilancioandroid.View.AddIncome;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddIncomeTest {

    private String number = "80";
    private String format = "$80.00";

    @Rule
    public ActivityTestRule<AddIncome> mActivityRule = new ActivityTestRule<>(
            AddIncome.class);

    @Test
    public void textEditText() {
       onView(withId(R.id.income_amount))
            .perform(typeText(number), pressImeActionButton());
       onView(withId(R.id.income_amount)).check(matches(withText(format)));
    }

}
