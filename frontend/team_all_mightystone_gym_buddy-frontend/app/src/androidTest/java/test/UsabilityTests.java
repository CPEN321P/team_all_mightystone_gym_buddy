package test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;

import static test.PerformanceTests.Login;
import static test.PerformanceTests.OpenAddEventPage;
import static test.PerformanceTests.OpenBlockList;
import static test.PerformanceTests.OpenChangePersonalProfile;
import static test.PerformanceTests.OpenFriendPersonalProfilePage;
import static test.PerformanceTests.OpenGymList;
import static test.PerformanceTests.OpenGymProfile;
import static test.PerformanceTests.OpenMonthlySchedule;
import static test.PerformanceTests.OpenOthersPersonalProfilePage;
import static test.PerformanceTests.OpenPeoplePage;
import static test.PerformanceTests.OpenPersonalProfile;
import static test.PerformanceTests.OpenPossibleFriendsPage;
import static test.PerformanceTests.OpenWeeklySchedule;

import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.example.cpen321tutorial1.Logo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class UsabilityTests {

    int NumberOfButtonPress;

    @Rule
    public ActivityScenarioRule<Logo> activityRule =
            new ActivityScenarioRule<Logo>(Logo.class);

    @Test
    public void A_OtherUsers_AddFriendOrBlock(){
        Login();
        NumberOfButtonPress = 0;

        OpenPeoplePage();
        NumberOfButtonPress++;
        OpenPossibleFriendsPage();
        NumberOfButtonPress++;
        OpenOthersPersonalProfilePage();
        NumberOfButtonPress++;
        //PressButton
        NumberOfButtonPress++;

        conclusion();
    }

    @Test
    public void B_Friends_CheckScheduleOrBlockOrChat(){
        Login();
        NumberOfButtonPress = 0;

        OpenPeoplePage();
        NumberOfButtonPress++;
        OpenFriendPersonalProfilePage();
        NumberOfButtonPress++;
        //PressButton
        NumberOfButtonPress++;

        conclusion();
    }

    @Test
    public void C_AddSchedule(){
        Login();
        NumberOfButtonPress = 0;

        OpenMonthlySchedule();
        NumberOfButtonPress++;
        OpenWeeklySchedule();
        NumberOfButtonPress++;
        OpenAddEventPage();
        NumberOfButtonPress++;
        //PressButtonofDone
        NumberOfButtonPress++;

        conclusion();
    }

    @Test
    public void D_SubscriptGym(){
        Login();
        NumberOfButtonPress = 0;

        OpenGymList();
        NumberOfButtonPress++;
        OpenGymProfile();
        NumberOfButtonPress++;
        //PressSubscribe
        NumberOfButtonPress++;

        conclusion();
    }

    @Test
    public void D_UnblockUser(){
        Login();
        NumberOfButtonPress = 0;

        OpenPersonalProfile();
        NumberOfButtonPress++;
        OpenBlockList();
        NumberOfButtonPress++;
        //PressOneOfTheUser
        NumberOfButtonPress++;
        //ConfirmUnblock
        NumberOfButtonPress++;

        conclusion();
    }

    @Test
    public void E_EditProfile(){
        Login();
        NumberOfButtonPress = 0;

        OpenPersonalProfile();
        NumberOfButtonPress++;
        OpenChangePersonalProfile();
        NumberOfButtonPress++;
        //PressDoneAfterEverythingIsDone
        NumberOfButtonPress++;

        conclusion();
    }

    public void conclusion(){
        if (NumberOfButtonPress > 4){
            ViewInteraction Error = onView(withContentDescription("ERROR"));
            Error.check(matches(isDisplayed()));
            //100% would be kick out
        }
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

    /*
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

     */

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