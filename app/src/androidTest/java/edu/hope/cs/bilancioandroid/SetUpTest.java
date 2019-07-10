package edu.hope.cs.bilancioandroid;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import edu.hope.cs.bilancioandroid.View.AddIncome;
import edu.hope.cs.bilancioandroid.View.SetUp;

import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;

import java.util.Set;

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
public class SetUpTest {

    private String number = "80";
    private String format = "$80.00";

    @Rule
    public ActivityTestRule<SetUp> mActivityRule = new ActivityTestRule<>(
            SetUp.class);


    @Test
    public void textEditText() {
        onView(withId(R.id.budget))
                .perform(typeText(number), pressImeActionButton());
        onView(withId(R.id.budget)).check(matches(withText(format)));

    }



}
