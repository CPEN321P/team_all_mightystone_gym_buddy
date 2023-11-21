package M6Test;

import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.cpen321tutorial1.CalendarUtils.selectedDate;
import static com.example.cpen321tutorial1.Event.eventsForDate;
import static com.example.cpen321tutorial1.EventEdit.getTodaysDate;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.IBinder;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cpen321tutorial1.Account;
import com.example.cpen321tutorial1.ConnectionToBackend;
import com.example.cpen321tutorial1.Event;
import com.example.cpen321tutorial1.EventEdit;
import com.example.cpen321tutorial1.GlobalClass;
import com.example.cpen321tutorial1.Logo;
import com.example.cpen321tutorial1.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class BrowseUserandAddFriend {



    @Rule
    public ActivityScenarioRule<Logo> activityRule =
            new ActivityScenarioRule<Logo>(Logo.class);

    @Test
    public void A_CheckTheFriendsAndPossibleFriends(){
        Login();
        GoToPeople();
        GoToPossibleFriendsList();
        GoToFriendsList();
    }

    @Test
    public void B_CheckBlockUserNotInThePossibleFriends(){
        Login();
        CheckBlockList();
    }

    @Test
    public void C_CheckThePossibleFriendsProfile(){
        Login();
        GoToPeople();
        GoToPossibleFriendsList();
        GoToPersonalProfileOthers();
        CheckPersonalProfileOthers();
    }

    @Test
    public void D_AddFriendAndCheckSchedule(){
        Login();
        GoToPeople();
        GoToPossibleFriendsList();
        GoToPersonalProfileOthers();
        CheckPersonalProfileOthers();
        AddFriend();
        GoToFriendsList();
        GoToPersonalProfileFriends();
        CheckPersonalProfileFriends();
        CheckSchedule();
    }

    @Test
    public void E_BlockUserTest(){
        Login();
        GoToPeople();
        GoToPersonalProfileFriends();
        CheckPersonalProfileFriends();
        Block();
    }

    @Test
    public void F_CheckBlockStatusAndUnblockBlockUserTest(){
        Login();
        GoToPeople();
        CheckBlock();
        UnBlock();
        CheckUnblock();
    }


    public void Login(){
        ConnectionToBackend c = new ConnectionToBackend();
        Account thisAccount = c.getAccountInformation("libirdxz@gmail.com");
        myAccount = thisAccount;

        ArrayList<Event> TheEventsofThisAccount = c.getScheduleByUser(thisAccount.getUserId());
        GlobalClass.MyeventsList = TheEventsofThisAccount;
    }

    public void CheckBlockList(){
        ViewInteraction PersonalProfile = onView(
                allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar)),
                                4),
                        isDisplayed()));
        PersonalProfile.perform(click());
        onView(withId(R.id.PersonalProfile)).check(matches(isDisplayed()));

        ViewInteraction BlockedUsers = onView(
                allOf(withId(R.id.BlockList), withText("Blocked Users"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        BlockedUsers.perform(click());
        onView(withId(R.id.BlockedUsers)).check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Tommy"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(matches(withText("Tommy")));

        ViewInteraction Cancel = onView(
                allOf(withId(R.id.Cancel), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.button_bar),
                                        0),
                                0),
                        isDisplayed()));
        Cancel.perform(click());
        onView(withId(R.id.PersonalProfile)).check(matches(isDisplayed()));

        ViewInteraction Friends = onView(
                allOf(withId(R.id.navigation_friends), withContentDescription("Friends"),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withId(R.id.PersonalProfile),
                                                2)),
                                1),
                        isDisplayed()));
        Friends.perform(click());
        onView(withId(R.id.Friends)).check(matches(isDisplayed()));

        GoToPossibleFriendsList();
        textView.check(doesNotExist());
    }

    public void GoToPeople(){
        ViewInteraction FriendsList = onView(
                allOf(withId(R.id.navigation_friends), withContentDescription("Friends"),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        FriendsList.perform(click());
        onView(withId(R.id.Friends)).check(matches(isDisplayed()));
        //onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));
    }

    public void GoToPossibleFriendsList(){
        ViewInteraction PossiblePeopleList = onView(
                allOf(withId(R.id.find_new_friends), withText("Find New Friends"),
                        childAtPosition(
                                allOf(withId(R.id.top_bar),
                                        childAtPosition(
                                                withId(R.id.Friends),
                                                0)),
                                1),
                        isDisplayed()));
        PossiblePeopleList.perform(click());
        onView(withId(R.id.PossibleFriends)).check(matches(isDisplayed()));
    }

    public void GoToFriendsList(){
        ViewInteraction FriendsPage = onView(
                allOf(withId(R.id.my_friends), withText("My Friends"),
                        childAtPosition(
                                allOf(withId(R.id.top_bar),
                                        childAtPosition(
                                                withId(R.id.PossibleFriends),
                                                0)),
                                1),
                        isDisplayed()));
        FriendsPage.perform(click());
        onView(withId(R.id.Friends)).check(matches(isDisplayed()));
    }


    public void GoToPersonalProfileOthers(){

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview),
                        withParent(allOf(withId(R.id.PossibleFriends),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Sav"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(matches(withText("Sav")));

        textView.perform(click());

        onView(withId(R.id.PersonalProfileOthers)).check(matches(isDisplayed()));
    }

    public void AddFriend(){

        ViewInteraction AddFriend = onView(
                allOf(withId(R.id.AddFriend), withText("Add Friend"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        AddFriend.perform(click());
        onView(withId(R.id.PossibleFriends)).check(matches(isDisplayed()));
    }

    public void GoToPersonalProfileFriends(){
        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Sav"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(matches(withText("Sav")));

        textView.perform(click());
        onView(withId(R.id.PersonalProfileFriend)).check(matches(isDisplayed()));
    }

    public void CheckPersonalProfileFriends(){
        ViewInteraction Email = onView(
                allOf(withId(R.id.Email),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        Email.check(matches(not(withText("Only Able To Seem By Friend"))));

        ViewInteraction Age = onView(
                allOf(withId(R.id.Age),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        Age.check(matches(not(withText("Only Able To Seem By Friend"))));

        ViewInteraction Weight = onView(
                allOf(withId(R.id.Weight),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        Weight.check(matches(not(withText("Only Able To Seem By Friend"))));
    }

    public void CheckPersonalProfileOthers(){
        ViewInteraction Email = onView(
                allOf(withId(R.id.Email),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        Email.check(matches(withText("Only Able To Seem By Friend")));

        ViewInteraction Age = onView(
                allOf(withId(R.id.Age),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        Age.check(matches(withText("Only Able To Seem By Friend")));

        ViewInteraction Weight = onView(
                allOf(withId(R.id.Weight),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        Weight.check(matches(withText("Only Able To Seem By Friend")));
    }

    public void CheckSchedule(){
        ViewInteraction Schedule = onView(
                allOf(withId(R.id.FriendSchedule), withText("Schedule"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        Schedule.perform(click());
        onView(withId(R.id.ScheduleWeeklyFriends)).check(matches(isDisplayed()));

        ViewInteraction Cancel = onView(
                allOf(withId(R.id.Cancel), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        6),
                                0),
                        isDisplayed()));
        Cancel.perform(click());
        onView(withId(R.id.Friends)).check(matches(isDisplayed()));
    }

    public void Block(){
        ViewInteraction Block = onView(
                allOf(withId(R.id.Block), withText("Block"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        Block.perform(click());

        onView(withId(R.id.Friends)).check(matches(isDisplayed()));
    }

    public void CheckBlock(){

        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Sav"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(doesNotExist());

        ViewInteraction PossibleFriends = onView(
                allOf(withId(R.id.find_new_friends), withText("Find New Friends"),
                        childAtPosition(
                                allOf(withId(R.id.top_bar),
                                        childAtPosition(
                                                withId(R.id.Friends),
                                                0)),
                                1),
                        isDisplayed()));
        PossibleFriends.perform(click());
        onView(withId(R.id.PossibleFriends)).check(matches(isDisplayed()));
        textView.check(doesNotExist());

        ViewInteraction PersonalProfile = onView(
                allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withId(R.id.PossibleFriends),
                                                2)),
                                4),
                        isDisplayed()));
        PersonalProfile.perform(click());
        onView(withId(R.id.PersonalProfile)).check(matches(isDisplayed()));

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.BlockList), withText("Blocked Users"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        materialButton11.perform(click());
        onView(withId(R.id.BlockedUsers)).check(matches(isDisplayed()));

        textView.check(matches(isDisplayed()));
    }

    public void UnBlock(){
        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Sav"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));
        textView.perform(click());
        onView(withId(R.id.Unblock)).check(matches(isDisplayed()));

        ViewInteraction Confirm = onView(
                allOf(withId(R.id.Confirm), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        Confirm.perform(click());
        onView(withId(R.id.BlockedUsers)).check(matches(isDisplayed()));


    }

    public void CheckUnblock(){
        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Sav"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(doesNotExist());

        ViewInteraction Cancel = onView(
                allOf(withId(R.id.Cancel), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.button_bar),
                                        0),
                                0),
                        isDisplayed()));
        Cancel.perform(click());
        onView(withId(R.id.PersonalProfile)).check(matches(isDisplayed()));

        ViewInteraction Friends = onView(
                allOf(withId(R.id.navigation_friends), withContentDescription("Friends"),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withId(R.id.PersonalProfile),
                                                2)),
                                1),
                        isDisplayed()));
        Friends.perform(click());
        onView(withId(R.id.Friends)).check(matches(isDisplayed()));

        GoToPossibleFriendsList();
        textView.check(matches(isDisplayed()));
    }

    public static ViewAction waitFor(long delay){
        return new ViewAction() {
            @Override public Matcher<View> getConstraints(){
                return ViewMatchers.isRoot();
            }

            @Override public String getDescription(){
                return "wait for " + delay + "milliseconds";
            }

            @Override public void perform(UiController uiController, View view){
                uiController.loopMainThreadForAtLeast(delay);
            }

        };
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

    //Source: https://www.qaautomated.com/2016/01/how-to-test-toast-message-using-espresso.html
    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    return true;
                }
            }
            return false;
        }
    }

    //Source: https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}