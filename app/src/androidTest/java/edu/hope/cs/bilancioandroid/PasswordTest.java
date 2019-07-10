package edu.hope.cs.bilancioandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import edu.hope.cs.bilancioandroid.View.MakeOrPresetBudgets;
import edu.hope.cs.bilancioandroid.View.Password;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;

public class PasswordTest {
    @Rule
    public ActivityTestRule<Password> passwordRule = new ActivityTestRule<>(Password.class);

    Context context = getInstrumentation().getTargetContext();
    SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();

    @Test
    public void testPasswordSave() {
        Intents.init();
        onView(withId(R.id.password)).perform(typeText("twitter1"), pressImeActionButton());
        onView(withId(R.id.confirmPassword)).perform(typeText("twitter1"),closeSoftKeyboard());
        onView(withId(R.id.go_to_budget)).perform(click());
        assertEquals("twitter1", prefs.getString("Password", ""));
        intended(hasComponent(MakeOrPresetBudgets.class.getName()));
        Intents.release();
    }

    @Test
    public void testNoPassword() {
        Intents.init();
        onView(withId(R.id.go_to_budget)).perform(click());
        assertEquals("", prefs.getString("Password", "Password"));
        intended(hasComponent(MakeOrPresetBudgets.class.getName()));
        Intents.release();
    }

    @Test
    public void testNoMatch() {
        Intents.init();
        editor.remove("Password");
        editor.apply();
        onView(withId(R.id.password)).perform(typeText("twitter1"), pressImeActionButton());
        onView(withId(R.id.confirmPassword)).perform(typeText("grandrapids"),closeSoftKeyboard());
        onView(withId(R.id.go_to_budget)).perform(click());
        assertEquals("nothing", prefs.getString("Password", "nothing"));
        onView(withText("Not a Match!")).check(matches(isDisplayed()));
        onView(withText("Go Back")).perform(click());
        onView(withText("Go Back")). check(doesNotExist());
        Intents.release();
    }

    @Test
    public void testShortPassword() {
        Intents.init();
        onView(withId(R.id.password)).perform(typeText("tweet"), pressImeActionButton());
        onView(withId(R.id.confirmPassword)).perform(typeText("tweet"),closeSoftKeyboard());
        onView(withId(R.id.go_to_budget)).perform(click());
        onView(withText("Password Not Long Enough!")).check(matches(isDisplayed()));
        Intents.release();
    }
}
