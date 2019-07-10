package edu.hope.cs.bilancioandroid;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import edu.hope.cs.bilancioandroid.View.MakeOrPresetBudgets;
import edu.hope.cs.bilancioandroid.View.ManualSetUp;
import edu.hope.cs.bilancioandroid.View.SetUp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class MakeOrPresetBudgetsTest {
    @Rule
    public ActivityTestRule<MakeOrPresetBudgets> mpRule = new ActivityTestRule<>(MakeOrPresetBudgets.class);

    @Test
    public void testMakeOwnBudgets() {
        Intents.init();
        onView(withId(R.id.own)).perform(click());
        onView(withId(R.id.own2)).check(matches(isDisplayed()));
        onView(withId(R.id.create)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.go_to_budget), withText("Setup Budgets"))).perform(click());
        intended(hasComponent(ManualSetUp.class.getName()));
        Intents.release();
    }

    @Test
    public void testPresetBudgets() {
        Intents.init();
        onView(withId(R.id.create)).perform(click());
        onView(withId(R.id.create2)).check(matches(isDisplayed()));
        onView(withId(R.id.own)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.go_to_budget), withText("Setup Budgets"))).perform(click());
        intended(hasComponent(SetUp.class.getName()));
        Intents.release();
    }

    @Test
    public void testNeither() {
        Intents.init();
        onView(withId(R.id.own)).check(matches(isDisplayed()));
        onView(withId(R.id.create)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.go_to_budget), withText("Setup Budgets"))).perform(click());
        onView(withText("Option not selected!")).check(matches(isDisplayed()));
        onView(withText("Go Back")).perform(click());
        onView(withText("Option not selected!")).check(doesNotExist());
        Intents.release();
    }

    @Test
    public void testSwitch() {
        Intents.init();
        onView(withId(R.id.create)).perform(click());
        onView(withId(R.id.create2)).check(matches(isDisplayed()));
        onView(withId(R.id.own)).check(matches(isDisplayed()));
        onView(withId(R.id.own)).perform(click());
        onView(withId(R.id.own2)).check(matches(isDisplayed()));
        onView(withId(R.id.create)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.go_to_budget), withText("Setup Budgets"))).perform(click());
        intended(hasComponent(ManualSetUp.class.getName()));
        Intents.release();
    }
}
