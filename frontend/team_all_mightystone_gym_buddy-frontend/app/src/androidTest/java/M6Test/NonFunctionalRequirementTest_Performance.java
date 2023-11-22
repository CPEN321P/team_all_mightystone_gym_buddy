package M6Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.example.cpen321tutorial1.Account;
import com.example.cpen321tutorial1.ConnectionToBackend;
import com.example.cpen321tutorial1.Event;
import com.example.cpen321tutorial1.GlobalClass;
import com.example.cpen321tutorial1.Logo;
import com.example.cpen321tutorial1.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class NonFunctionalRequirementTest_Performance {

    LocalTime StartTime;

    LocalTime EndTime;

    long OperatingTime;

    @Rule
    public ActivityScenarioRule<Logo> activityRule =
            new ActivityScenarioRule<Logo>(Logo.class);

    @Test
    public void A_OpenFriendsPageTest() {
        Login();

        StartRecord();
        OpenPeoplePage();
        EndRecord();
    }

    @Test
    public void B_OpenPossibleFriendsPageTest(){
        Login();
        OpenPeoplePage();

        StartRecord();
        OpenPossibleFriendsPage();
        EndRecord();
    }

    @Test
    public void C_OpenFriendsPersonalProfilePageTest(){
        Login();
        OpenPeoplePage();

        StartRecord();
        OpenFriendPersonalProfilePage();
        EndRecord();
    }

    @Test
    public void D_OpenOthersPersonalProfilePageTest(){
        Login();
        OpenPeoplePage();
        OpenPossibleFriendsPage();

        StartRecord();
        OpenOthersPersonalProfilePage();
        EndRecord();
    }

    @Test
    public void E_OpenScheduleAndAddEventTest(){
        Login();

        StartRecord();
        OpenMonthlySchedule();
        EndRecord();

        StartRecord();
        OpenDailySchedule();
        EndRecord();

        StartRecord();
        OpenWeeklySchedule();
        EndRecord();

        StartRecord();
        OpenAddEventPage();
        EndRecord();
    }

    @Test
    public void F_OpenGymListAndGymProfile(){
        Login();

        StartRecord();
        OpenGymList();
        EndRecord();

        StartRecord();
        OpenGymProfile();
        EndRecord();
    }

    @Test
    public void G_OpenPersonalProfile(){
        Login();

        StartRecord();
        OpenPersonalProfile();
        EndRecord();
    }

    @Test
    public void H_OpenBlockedUsers(){
        Login();
        OpenPersonalProfile();

        StartRecord();
        OpenBlockList();
        EndRecord();
    }

    @Test
    public void I_OpenEditPersonalProfile(){
        Login();
        OpenPersonalProfile();

        StartRecord();
        OpenChangePersonalProfile();
        EndRecord();
    }

    @Test
    public void J_OpenMessage(){
        Login();
        OpenPeoplePage();

        StartRecord();
        OpenMessagePage();
        EndRecord();
    }

    @Test
    public void K_OpenChat(){
        Login();
        OpenPeoplePage();
        OpenFriendPersonalProfilePage();

        StartRecord();
        OpenChatPage();
        EndRecord();
    }

    public static void Login(){
        ConnectionToBackend c = new ConnectionToBackend();
        Account thisAccount = c.getAccountInformation("libirdxz@gmail.com");
        myAccount = thisAccount;

        ArrayList<Event> TheEventsofThisAccount = c.getScheduleByUser(thisAccount.getUserId());
        GlobalClass.MyeventsList = TheEventsofThisAccount;
    }

    public void StartRecord(){
        StartTime = LocalTime.now();
    }

    public void EndRecord(){
        EndTime = LocalTime.now();
        OperatingTime = ChronoUnit.MILLIS.between(StartTime, EndTime);

        if (OperatingTime > 1000){
            ViewInteraction Error = onView(withContentDescription("ERROR"));
            Error.check(matches(isDisplayed()));
        }
    }

    public static void OpenMessagePage(){
        ViewInteraction Messages = onView(
                allOf(withId(R.id.top_bar_messages), withContentDescription("Messages"),
                        childAtPosition(
                                allOf(withId(R.id.top_bar),
                                        childAtPosition(
                                                withId(R.id.Friends),
                                                0)),
                                2),
                        isDisplayed()));
        Messages.perform(click());
        onView(withId(R.id.Messages)).check(matches(isDisplayed()));
    }

    public static void OpenChatPage(){
        ViewInteraction Chat = onView(
                allOf(withId(R.id.Message), withText("Message"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        Chat.perform(click());
        onView(withId(R.id.Chat)).check(matches(isDisplayed()));
    }

    public static void OpenPersonalProfile(){
        ViewInteraction PersonalProfile = onView(
                allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                4),
                        isDisplayed()));
        PersonalProfile.perform(click());
        onView(withId(R.id.PersonalProfile)).check(matches(isDisplayed()));
    }

    public static void OpenBlockList(){
        ViewInteraction BlockList = onView(
                allOf(withId(R.id.BlockList), withText("Blocked Users"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        BlockList.perform(click());
        onView(withId(R.id.BlockedUsers)).check(matches(isDisplayed()));
    }

    public static void OpenChangePersonalProfile(){
        ViewInteraction ChangePersonalProfile = onView(
                allOf(withId(R.id.EditPersonalProfile), withText("Edit Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        ChangePersonalProfile.perform(click());
        onView(withId(R.id.PersonalProfileEdit)).check(matches(isDisplayed()));
    }

    public static void OpenGymList(){
        ViewInteraction GymList = onView(
                allOf(withId(R.id.navigation_gyms), withContentDescription("Gyms"),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                3),
                        isDisplayed()));
        GymList.perform(click());
        onView(withId(R.id.Gyms)).check(matches(isDisplayed()));
    }

    public static void OpenGymProfile(){
        ViewInteraction Monster = onView(
                allOf(withId(R.id.name), withText("Monster"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        Monster.perform(click());
        onView(withId(R.id.GymProfile)).check(matches(isDisplayed()));
    }

    public static void OpenMonthlySchedule(){
        ViewInteraction Schedule = onView(
                allOf(withId(R.id.navigation_schedule), withContentDescription("Schedule"),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                2),
                        isDisplayed()));
        Schedule.perform(click());
        onView(withId(R.id.ScheduleMonthly)).check(matches(isDisplayed()));
    }

    public static void OpenDailySchedule(){
        ViewInteraction ScheduleDaily = onView(
                allOf(withId(R.id.Daily), withText("Daily"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ScheduleMonthly),
                                        0),
                                1),
                        isDisplayed()));
        ScheduleDaily.perform(click());
        onView(withId(R.id.ScheduleDaily)).check(matches(isDisplayed()));
    }

    public static void OpenWeeklySchedule(){
        ViewInteraction ScheduleWeekly = onView(
                allOf(withId(R.id.Weekly), withText("Weekly "),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ScheduleDaily),
                                        0),
                                1),
                        isDisplayed()));
        ScheduleWeekly.perform(click());
        onView(withId(R.id.ScheduleWeekly)).check(matches(isDisplayed()));
    }

    public static void OpenAddEventPage(){
        ViewInteraction AddEvent = onView(
                Matchers.allOf(withId(R.id.AddEvent), withText("New Event"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        4),
                                0),
                        isDisplayed()));
        AddEvent.perform(click());
        onView(withId(R.id.EventEdit)).check(matches(isDisplayed()));
    }

    public static void OpenPeoplePage(){
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
    }

    public static void OpenPossibleFriendsPage(){
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

    public static void OpenFriendPersonalProfilePage(){
        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Tommy"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        textView.perform(click());
        onView(withId(R.id.PersonalProfileFriend)).check(matches(isDisplayed()));
    }

    public static void OpenOthersPersonalProfilePage(){
        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Sav"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        textView.perform(click());
        onView(withId(R.id.PersonalProfileOthers)).check(matches(isDisplayed()));
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