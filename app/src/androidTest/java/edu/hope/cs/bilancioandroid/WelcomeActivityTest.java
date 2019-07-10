package edu.hope.cs.bilancioandroid;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.hope.cs.bilancioandroid.View.BudgetCycle;
import edu.hope.cs.bilancioandroid.View.WelcomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WelcomeActivityTest {

    @Rule
    public ActivityTestRule<WelcomeActivity> rule = new ActivityTestRule<>(WelcomeActivity.class);

    @Test
    public void verifyWelcomeActivity() {
        Intents.init();
        onView(withId(R.id.welcomeMsg)).check(matches(withText("Welcome to Bilancio, an easy way to manage your budgets!")));
        onView(withId(R.id.setupButton)).check(matches(withText("Begin Setup")));
        onView(withId(R.id.setupButton)).perform(click());
        intended(hasComponent(BudgetCycle.class.getName()));
    }
}
