package M6Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.cpen321tutorial1.CalendarUtils.selectedDate;
import static com.example.cpen321tutorial1.Event.eventsForDate;
import static com.example.cpen321tutorial1.EventEdit.getTodaysDate;
import static com.example.cpen321tutorial1.GlobalClass.TempPeopleList;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.IBinder;
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
import com.example.cpen321tutorial1.EventEdit;
import com.example.cpen321tutorial1.GlobalClass;
import com.example.cpen321tutorial1.Logo;
import com.example.cpen321tutorial1.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BrowseUserandAddFriend {



    @Rule
    public ActivityScenarioRule<Logo> activityRule =
            new ActivityScenarioRule<Logo>(Logo.class);


    @Test
    public void OpenListOfPeople(){

        ArrayList<Account> Temp = new ArrayList<>();

        GlobalClass.MyeventsList = new ArrayList<>();

        GlobalClass.TestTempPeopleList = 1;
        GlobalClass.myAccount = new Account("Zheng Xu", "Libirdxz@gmail.com", 22, 80, "Male", Temp, Temp);

        Account Tyson = new Account("Tyson", "thetysonbrown@gmail.com", 24, 77, "Male", Temp, Temp);
        Account Joy = new Account("Joy", "joyinhea@gmail.com", 12, 60, "Female", Temp, Temp);
        Account Sav = new Account("Sav", "savitoj2019@gmail.com", 20, 80, "Male", Temp, Temp);

        TempPeopleList.clear();
        TempPeopleList.add(Tyson);
        TempPeopleList.add(Joy);
        TempPeopleList.add(Sav);

        ViewInteraction FriendsList = onView(
                allOf(withId(R.id.navigation_friends),
                        childAtPosition(
                                allOf(withId(R.id.navigation_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        FriendsList.perform(click());
        onView(withId(R.id.Friends)).check(matches(isDisplayed()));

        ViewInteraction PossiblePeopleList = onView(
                allOf(withId(R.id.find_new_friends),
                        childAtPosition(
                                allOf(withId(R.id.top_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        PossiblePeopleList.perform(click());
        onView(withId(R.id.PossibleFriends)).check(matches(isDisplayed()));
    }

    @Test
    public void CheckPossibleFriendPage(){
        OpenListOfPeople();
        //ViewInteraction ThePossiblePeopleList = onView(withId(R.)
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