package M6Test;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static com.example.cpen321tutorial1.CalendarUtils.selectedDate;
import static com.example.cpen321tutorial1.Event.eventsForDate;
import static com.example.cpen321tutorial1.EventEdit.getTodaysDate;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
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
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManageTheScheduleTest {

    @Rule
    public ActivityScenarioRule<Logo> activityRule =
            new ActivityScenarioRule<Logo>(Logo.class);

    @Test
    public void OpenEventEditWeekly(){
        OpenSchedule();
        NewEventThroughWeekly();
    }

    @Test
    public void OpenEventEditDaily(){
        OpenSchedule();
        NewEventThroughDaily();
    }

    @Test
    public void AddOneEventAndRemoveIt(){
        OpenSchedule();
        NewEventThroughWeekly();
        AddEvent();
        CheckEventAddedOrNot();
        ClearLastEvent();
        CheckAfterClearLastEvent();
    }

    @Test
    public void AddOneEventWithWrongString(){
        OpenSchedule();
        NewEventThroughWeekly();
        AddEventWithWrongString();
    }

    @Test
    public void AddOneEventWithTimeConflict(){
        OpenSchedule();
        NewEventThroughWeekly();
        AddEvent();
        CheckEventAddedOrNot();
        AddEventWithTimeConflict();
        ClearLastEvent();
        CheckAfterClearLastEvent();
    }

    @Test
    public void FinalTest_NoMoreEventToRemove(){
        OpenSchedule();
        NewEventThroughWeekly();
        AddEvent();
        CheckEventAddedOrNot();
        ClearLastEvent();
        CheckAfterClearLastEvent();
        NoMoreEventToDelete();
    }


    public void OpenSchedule(){

        ConnectionToBackend c = new ConnectionToBackend();
        Account thisAccount = c.getAccountInformation("libirdxz@gmail.com");
        myAccount = thisAccount;
        Log.d("MTST", myAccount.getUserId());

        ArrayList<Event> TheEventsofThisAccount = c.getScheduleByUser(thisAccount.getUserId());
        GlobalClass.MyeventsList = TheEventsofThisAccount;

        ViewInteraction GoToSchedule = onView(
                Matchers.allOf(withId(R.id.navigation_schedule), withContentDescription("Schedule"),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.widget.RelativeLayout")),
                                                1)),
                                2),
                        isDisplayed()));
        GoToSchedule.perform(click());
        onView(withId(R.id.ScheduleMonthly)).check(matches(isDisplayed()));
    }

    public void NewEventThroughWeekly(){

        ViewInteraction WeeklySchedule = onView(
                Matchers.allOf(withId(R.id.Weekly), withText("Weekly"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        WeeklySchedule.perform(click());
        onView(withId(R.id.ScheduleWeekly)).check(matches(isDisplayed()));

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

    public void NewEventThroughDaily(){

        ViewInteraction DailySchedule = onView(
                Matchers.allOf(withId(R.id.Daily), withText("Daily"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ScheduleMonthly),
                                        0),
                                1),
                        isDisplayed()));
        DailySchedule.perform(click());
        onView(withId(R.id.ScheduleDaily)).check(matches(isDisplayed()));

        ViewInteraction AddEvent = onView(
                Matchers.allOf(withId(R.id.AddEvent), withText("New Event"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ScheduleDaily),
                                        3),
                                0),
                        isDisplayed()));
        AddEvent.perform(click());
        onView(withId(R.id.EventEdit)).check(matches(isDisplayed()));
    }

    public void AddEvent(){
        ViewInteraction AddEventName = onView(
                Matchers.allOf(withId(R.id.Event),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        AddEventName.perform(replaceText("Drink Water"), closeSoftKeyboard());
        AddEventName.check(matches(withText("Drink Water")));

        ViewInteraction AddDate = onView(
                Matchers.allOf(withId(R.id.Date),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.DateLayOut),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        EventEdit.dateButton.setText(getTodaysDate());
        AddDate.check(matches(withText(getTodaysDate())));

        ViewInteraction EditTime = onView(
                Matchers.allOf(withId(R.id.StartTime),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.StartTimeLayOut),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.widget.LinearLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        EventEdit.timeButton.setText("08:00");
        EditTime.check(matches(withText("08:00")));

        ViewInteraction NumberOfHour = onView(
                Matchers.allOf(withId(R.id.HowLong),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        0),
                                3),
                        isDisplayed()));
        NumberOfHour.perform(replaceText("3"), closeSoftKeyboard());
        NumberOfHour.check(matches(withText("3")));

        ViewInteraction Done = onView(
                Matchers.allOf(withId(R.id.Done), withText("DONE"),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.EventEdit),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        Done.perform(click());
        onView(withId(R.id.ScheduleWeekly)).check(matches(isDisplayed()));


    }

    public void AddEventWithWrongString(){

        ViewInteraction AddEventName = onView(
                Matchers.allOf(withId(R.id.Event),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        AddEventName.perform(replaceText("Drink Water"), closeSoftKeyboard());
        AddEventName.check(matches(withText("Drink Water")));

        ViewInteraction AddDate = onView(
                Matchers.allOf(withId(R.id.Date),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.DateLayOut),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        EventEdit.dateButton.setText(getTodaysDate());
        AddDate.check(matches(withText(getTodaysDate())));

        ViewInteraction EditTime = onView(
                Matchers.allOf(withId(R.id.StartTime),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.StartTimeLayOut),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.widget.LinearLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        EventEdit.timeButton.setText("08:00");
        EditTime.check(matches(withText("08:00")));

        ViewInteraction NumberOfHour = onView(
                Matchers.allOf(withId(R.id.HowLong),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        0),
                                3),
                        isDisplayed()));
        NumberOfHour.perform(replaceText("Three"), closeSoftKeyboard());
        NumberOfHour.check(matches(withText("Three")));

        ViewInteraction Done = onView(
                Matchers.allOf(withId(R.id.Done), withText("DONE"),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.EventEdit),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        Done.perform(click());

        //Check if toast is display
        onView(withText("Invalid Number Of Hours")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    public void AddEventWithTimeConflict(){
        //AddEvent();

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

        ViewInteraction AddEventName = onView(
                Matchers.allOf(withId(R.id.Event),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        AddEventName.perform(replaceText("Running"), closeSoftKeyboard());
        AddEventName.check(matches(withText("Running")));

        ViewInteraction AddDate = onView(
                Matchers.allOf(withId(R.id.Date),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.DateLayOut),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        EventEdit.dateButton.setText(getTodaysDate());
        AddDate.check(matches(withText(getTodaysDate())));

        ViewInteraction EditTime = onView(
                Matchers.allOf(withId(R.id.StartTime),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.StartTimeLayOut),
                                        childAtPosition(
                                                withClassName(Matchers.is("android.widget.LinearLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        EventEdit.timeButton.setText("09:00");
        EditTime.check(matches(withText("09:00")));

        ViewInteraction NumberOfHour = onView(
                Matchers.allOf(withId(R.id.HowLong),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.widget.LinearLayout")),
                                        0),
                                3),
                        isDisplayed()));
        NumberOfHour.perform(replaceText("2"), closeSoftKeyboard());
        NumberOfHour.check(matches(withText("2")));

        ViewInteraction Done = onView(
                Matchers.allOf(withId(R.id.Done), withText("DONE"),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.EventEdit),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        Done.perform(click());

        //Check if toast is display
        onView(withText("TimeConflict!")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        ViewInteraction CANCEL = onView(
                Matchers.allOf(withId(R.id.CancelAddEvent), withText("CANCEL"),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.EventEdit),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        CANCEL.perform(click());
    }

    public void CheckEventAddedOrNot(){

        DataInteraction EventListWeekly = onData(Matchers.anything())
                .inAdapterView(Matchers.allOf(withId(R.id.eventList),
                        childAtPosition(
                                withId(R.id.ScheduleWeekly),
                                5)))
                .atPosition(0);
        EventListWeekly.check(matches(isDisplayed()));

    }

    public void ClearLastEvent(){
        ViewInteraction ClearLastEvent = onView(
                Matchers.allOf(withId(R.id.ClearEvent), withText("Clear Last Event"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ScheduleWeekly),
                                        4),
                                1),
                        isDisplayed()));
        ClearLastEvent.perform(click());
    }

    public void CheckAfterClearLastEvent(){

        DataInteraction EventListWeekly = onData(Matchers.anything())
                .inAdapterView(Matchers.allOf(withId(R.id.eventList),
                        childAtPosition(
                                withId(R.id.ScheduleWeekly),
                                5)))
                .atPosition(0);

        int EventNumber = eventsForDate(selectedDate).size();
        if (EventNumber == 0){
            return;
        }
        else {
            EventListWeekly.check(matches(isDisplayed()));
        }
    }

    public void NoMoreEventToDelete(){

        ViewInteraction ClearLastEvent = onView(
                Matchers.allOf(withId(R.id.ClearEvent), withText("Clear Last Event"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ScheduleWeekly),
                                        4),
                                1),
                        isDisplayed()));
        ClearLastEvent.perform(click());

        //Check if toast is display
        onView(withText("No Event For Today!")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
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
}