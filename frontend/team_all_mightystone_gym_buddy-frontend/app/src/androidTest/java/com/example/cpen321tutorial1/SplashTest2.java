package com.example.cpen321tutorial1;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SplashTest2 {

    @Rule
    public ActivityScenarioRule<Splash> mActivityScenarioRule =
            new ActivityScenarioRule<>(Splash.class);

    @Test
    public void splashTest2() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.getStarted), withText("Get Started"),
                        childAtPosition(
                                allOf(withId(R.id.MainActivity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction id = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.sign_in_button),
                                        childAtPosition(
                                                withId(R.id.LoginPage),
                                                1)),
                                0),
                        isDisplayed()));
        id.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.navigation_friends), withContentDescription("Friends"),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.find_new_friends), withText("Find New Friends"),
                        childAtPosition(
                                allOf(withId(R.id.top_bar),
                                        childAtPosition(
                                                withId(R.id.Friends),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.AddFriend), withText("Add Friend"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.my_friends), withText("My Friends"),
                        childAtPosition(
                                allOf(withId(R.id.top_bar),
                                        childAtPosition(
                                                withId(R.id.PossibleFriends),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
