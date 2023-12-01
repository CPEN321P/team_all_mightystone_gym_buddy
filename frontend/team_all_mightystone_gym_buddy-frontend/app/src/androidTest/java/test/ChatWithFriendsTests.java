package test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
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
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class ChatWithFriendsTests {

    public static String inputString;

    @Rule
    public ActivityScenarioRule<Logo> activityRule =
            new ActivityScenarioRule<Logo>(Logo.class);

    @Test
    public void A_CheckPersonalProfileFriendsAndCheckMessageButton(){
        Login();
        GoToPeople();
        GoToPersonalProfileFriends();
        CheckMessageButtonIsThere();
    }

    @Test
    public void B_CheckPersonalProfileAndCheckMessageButtonIsNotHere(){
        Login();
        GoToPeople();
        GoToPossibleFriendsList();
        GoToPersonalProfileOthers();
        CheckMessageButtonIsNotThere();
    }

    @Test
    public void C_SendTheMessageAndCheckit(){
        Login();
        GoToPeople();
        GoToPersonalProfileFriends();
        CheckMessageButtonIsThere();
        ClickMessageButton();
        SendMessageAndCheckMessage();
        CheckMessage();
    }

    @Test
    public void D_CheckSentMessageFromAnother(){
        LoginAsOthers();
        GoToPeople();
        GoToPersonalProfileFriendsFromAnother();
        CheckMessageButtonIsThere();
        ClickMessageButton();
        //CheckMessage();
        CheckMessageFromOthers();
    }

    public void Login(){
        ConnectionToBackend c = new ConnectionToBackend();
        Account thisAccount = c.getAccountInformation("libirdxz@gmail.com");
        myAccount = thisAccount;

        ArrayList<Event> TheEventsofThisAccount = c.getScheduleByUser(thisAccount.getUserId());
        GlobalClass.MyeventsList = TheEventsofThisAccount;
    }

    public void LoginAsOthers(){
        ConnectionToBackend c = new ConnectionToBackend();
        Account thisAccount = c.getAccountInformation("zhengxu3635@gmail.com");
        myAccount = thisAccount;

        ArrayList<Event> TheEventsofThisAccount = c.getScheduleByUser(thisAccount.getUserId());
        GlobalClass.MyeventsList = TheEventsofThisAccount;
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
        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Tyson Brown"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(matches(withText("Tyson Brown")));

        textView.perform(click());

        onView(withId(R.id.PersonalProfileOthers)).check(matches(isDisplayed()));
    }

    public void GoToPersonalProfileFriends(){
        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Tommy"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(matches(withText("Tommy")));

        textView.perform(click());
        onView(withId(R.id.PersonalProfileFriend)).check(matches(isDisplayed()));
    }

    public void GoToPersonalProfileFriendsFromAnother(){
        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Zheng Xu"),
                        withParent(withParent(withId(R.id.recyclerview))),
                        isDisplayed()));
        textView.check(matches(withText("Zheng Xu")));

        textView.perform(click());
        onView(withId(R.id.PersonalProfileFriend)).check(matches(isDisplayed()));
    }

    public void CheckMessageButtonIsThere(){
        ViewInteraction Message = onView(
                allOf(withId(R.id.Message), withText("Message")));
        Message.check(matches(isDisplayed()));
    }

    public void CheckMessageButtonIsNotThere(){
        ViewInteraction Message = onView(
                allOf(withId(R.id.Message), withText("Message")));
        Message.check(doesNotExist());
    }

    public void ClickMessageButton(){
        ViewInteraction Message = onView(
                allOf(withId(R.id.Message), withText("Message"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        Message.perform(click());
        onView(withId(R.id.Chat)).check(matches(isDisplayed()));
    }

    public void SendMessageAndCheckMessage(){
        LocalTime localTime = LocalTime.now();
        inputString = localTime.toString();

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.chat_text_input),
                        childAtPosition(
                                allOf(withId(R.id.bottom_layout),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(inputString), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.message_send_button),
                        childAtPosition(
                                allOf(withId(R.id.bottom_layout),
                                        childAtPosition(
                                                withId(R.id.Chat),
                                                2)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

    }

    public void CheckMessage(){
        ViewInteraction SentMessage = onView(
                allOf(withId(R.id.right_chat_text), withText(inputString),
                        withParent(allOf(withId(R.id.right_chat_layout),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class)))),
                        isDisplayed()));
        SentMessage.check(matches(withText(inputString)));
    }

    public void CheckMessageFromOthers(){
        ViewInteraction SentMessage = onView(
                allOf(withId(R.id.left_chat_text), withText(inputString),
                        withParent(allOf(withId(R.id.left_chat_layout),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class)))),
                        isDisplayed()));
        SentMessage.check(matches(withText(inputString)));
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