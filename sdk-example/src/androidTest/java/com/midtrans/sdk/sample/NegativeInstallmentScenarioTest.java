package com.midtrans.sdk.sample;

import android.os.SystemClock;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.models.UserDetail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by rakawm on 1/23/17.
 */

@RunWith(AndroidJUnit4.class)
public class NegativeInstallmentScenarioTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    String cardNo, cardNo2, cardCVV, cardExpired;
    String cardNoExpected, cardNo2Expected, cardCVVExcpected, cardExpiredExpected;
    String email, emailExpected;
    private String fullName, phone;
    private String address, city, zipcode, country;

    @Before
    public void setup() {
        cardNo = "5411111111111115";
        cardNo2 = "4599207887122414";
        cardNoExpected = "5411 1111 1111 1115";
        cardNo2Expected = "4599 2078 8712 2414";
        cardCVV = "123";
        cardCVVExcpected = "123";
        cardExpired = "0120";
        cardExpiredExpected = "01 / 20";

        fullName = "Raka Westu Mogandhi";
        email = "westumogandhi@gmail.com";
        emailExpected = "westumogandhi@gmail.com";
        phone = "082140518011";
        address = "Jalan Wirajaya 312";
        city = "Yogyakarta";
        zipcode = "55198";
        country = "Indonesia";

        runPrologue();
    }

    public void runPrologue() {
        if (LocalDataHandler.readObject("user_details", UserDetail.class) == null) {
            // Go to credit card
            onView(withId(R.id.show_ui_flow)).perform(scrollTo(), click());

            // Fill consumer name
            onView(withId(R.id.et_full_name)).perform(clearText(), typeText(fullName), closeSoftKeyboard());
            onView(withId(R.id.et_email)).perform(clearText(), typeText(email), closeSoftKeyboard());
            onView(withId(R.id.et_phone)).perform(clearText(), typeText(phone), closeSoftKeyboard());

            // Click next button
            onView(withId(R.id.btn_next)).perform(click());

            // Fill consumer details
            onView(withId(R.id.et_address)).perform(clearText(), typeText(address), closeSoftKeyboard());
            onView(withId(R.id.et_city)).perform(clearText(), typeText(city), closeSoftKeyboard());
            onView(withId(R.id.et_zipcode)).perform(clearText(), typeText(zipcode), closeSoftKeyboard());
            onView(withId(R.id.et_country)).perform(clearText(), typeText(country), closeSoftKeyboard());
            onView(withText("Indonesia"))
                    .inRoot(isPlatformPopup())
                    .perform(click());

            // Click next button
            onView(withId(R.id.btn_next)).perform(click());
            // Load transaction details
            SystemClock.sleep(10000);
            onView(isRoot()).perform(ViewActions.pressBack());
            SystemClock.sleep(200);
        }
    }

    @Test
    public void creditCardNotApplicableOfferFlowTest() {
        // Initializing credit card payment
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(cardNo), closeSoftKeyboard()).check(matches(withText(cardNoExpected)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.layout_offer_status)).check(matches(isDisplayed()));
    }
}
