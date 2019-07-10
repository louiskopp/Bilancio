package edu.hope.cs.bilancioandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import edu.hope.cs.bilancioandroid.View.Contacts;
import edu.hope.cs.bilancioandroid.View.Password;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;

public class ContactsTestNoOutsideAccess {
    @Rule
    public ActivityTestRule<Contacts> contactsRule = new ActivityTestRule<>(Contacts.class);

    Context context = getInstrumentation().getTargetContext();
    SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();

    @Test
    public void testForContactInput() {
        Intents.init();
        onView(withId(R.id.contactsName)).perform(typeText("Fred"), pressImeActionButton());
        onView(withId(R.id.contactPhone)).perform(typeText("2319677465"),closeSoftKeyboard());
        onView(withId(R.id.go_to_budget)).perform(click());
        assertEquals("Fred", prefs.getString("Name", ""));
        assertEquals("2319677465", prefs.getString("Phone", ""));
        intended(hasComponent(Password.class.getName()));
        Intents.release();
        }

    @Test
    public void testNoInfoEntered() {
        Intents.init();
        onView(withId(R.id.go_to_budget)).perform(click());
        onView(withText("No contact found!")).check(matches(isDisplayed()));
        Intents.release();
    }

    @Test
    public void testBadNumberEntered() {
        Intents.init();
        onView(withId(R.id.contactsName)).perform(typeText("Fred"), pressImeActionButton());
        onView(withId(R.id.contactPhone)).perform(typeText("23177465"),closeSoftKeyboard());
        onView(withId(R.id.go_to_budget)).perform(click());
        onView(withText("Not a real phone number!")).check(matches(isDisplayed()));
        Intents.release();
    }

    @Test
    public void testNoNameEntered() {
        Intents.init();
        onView(withId(R.id.contactPhone)).perform(typeText("23177465"),closeSoftKeyboard());
        onView(withId(R.id.go_to_budget)).perform(click());
        onView(withText("No contact found!")).check(matches(isDisplayed()));
        Intents.release();
    }

    @Test
    public void testNoNumberEntered() {
        Intents.init();
        onView(withId(R.id.contactsName)).perform(typeText("Fred"),closeSoftKeyboard());
        onView(withId(R.id.go_to_budget)).perform(click());
        onView(withText("No contact found!")).check(matches(isDisplayed()));
        Intents.release();
    }

}
