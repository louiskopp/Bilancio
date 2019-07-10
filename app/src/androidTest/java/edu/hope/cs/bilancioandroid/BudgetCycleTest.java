package edu.hope.cs.bilancioandroid;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.hope.cs.bilancioandroid.View.BudgetCycle;
import edu.hope.cs.bilancioandroid.View.Contacts;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class BudgetCycleTest {
    @Rule
    public ActivityTestRule<BudgetCycle> bcRule = new ActivityTestRule<>(BudgetCycle.class);

    @Test
    public void verifyTwoWeeks() {
        Intents.init();
        onView(withId(R.id.select_time)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Two Weeks"))).perform(click());
        onView(withId(R.id.select_time)).check(matches(withSpinnerText("Two Weeks")));
        onView(allOf(withId(R.id.go_to_login), withText("Next"))).perform(click());
        intended(hasComponent(Contacts.class.getName()));
        Intents.release();
    }

    @Test
    public void verifyOneMonth() {
        Intents.init();
        onView(withId(R.id.select_time)).check(matches(withSpinnerText("One Month")));
        onView(allOf(withId(R.id.go_to_login), withText("Next"))).perform(click());
        intended(hasComponent(Contacts.class.getName()));
        Intents.release();
    }

    @Test
    public void verifyOneWeek() {
        Intents.init();
        onView(withId(R.id.select_time)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("One Week"))).perform(click());
        onView(withId(R.id.select_time)).check(matches(withSpinnerText("One Week")));
        onView(allOf(withId(R.id.go_to_login), withText("Next"))).perform(click());
        intended(hasComponent(Contacts.class.getName()));
        Intents.release();
    }
}
