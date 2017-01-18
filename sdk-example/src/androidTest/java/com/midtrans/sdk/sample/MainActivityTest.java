package com.midtrans.sdk.sample;

import android.os.SystemClock;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.web.webdriver.Locator;
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
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.clearElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webKeys;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by ziahaqi on 7/27/16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    String cardNo, cardCVV, cardExpired;
    String cardNoExpected, cardCVVExcpected, cardExpiredExpected;
    String email, emailExpected;
    private String fullName, phone;
    private String address, city, zipcode, country;

    @Before
    public void setup() {
        cardNo = "4811111111111114";
        cardNoExpected = "4811 1111 1111 1114";
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
    public void creditCardNormalFlowTest() {
        // Initializing credit card payment
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(cardNo), closeSoftKeyboard()).check(matches(withText(cardNoExpected)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.btn_pay_now)).perform(click());

        // Load charging request
        SystemClock.sleep(5000);
        onView(withId(R.id.text_title_payment_status)).check(matches(withText(R.string.payment_successful)));
    }

    @Test
    public void creditCard3DSFlowTest() {
        // Initializing credit card payment
        onView(withId(R.id.radio_secure)).perform(scrollTo(), click());
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(cardNo), closeSoftKeyboard()).check(matches(withText(cardNoExpected)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.btn_pay_now)).perform(click());

        // Load 3DS
        SystemClock.sleep(10000);

        // Fill 3DS
        onWebView().forceJavascriptEnabled();
        // Check for webview and fill the code with default `112233` string
        onWebView()
                .withElement(findElement(Locator.ID, "PaRes"))
                .perform(clearElement())
                .perform(webKeys("112233"))
                .withElement(findElement(Locator.NAME, "ok"))
                .perform(webClick());

        // Load charging request
        SystemClock.sleep(5000);
        onView(withId(R.id.text_title_payment_status)).check(matches(withText(R.string.payment_successful)));
    }

    @Test
    public void bankTransferBCAFlowTest() {
        //Initializing bank transfer payment
        onView(withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        SystemClock.sleep(10000);

        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.rv_bank_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Check email address text
        onView(withId(R.id.et_email)).perform(clearText(), typeText(email), closeSoftKeyboard()).check(matches(withText(emailExpected)));
        onView(withId(R.id.btn_confirm_payment)).perform(click());

        // Wait for request
        SystemClock.sleep(5000);

        // Check for virtual account
        onView(withId(R.id.text_virtual_account_number)).check(matches(not(withText(""))));
        onView(withId(R.id.text_virtual_account_number)).check(matches(not(withText(R.string.virtual_account_number))));
        onView(withId(R.id.btn_confirm_payment)).perform(click());

        // Check transaction status
        onView(withId(R.id.text_title_payment_status)).check(matches(withText(R.string.payment_pending)));
    }

    @Test
    public void bankTransferMandiriFlowTest() {
        //Initializing bank transfer payment
        onView(withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        SystemClock.sleep(10000);

        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.rv_bank_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //Check email address text
        onView(withId(R.id.et_email)).perform(clearText(), typeText(email), closeSoftKeyboard()).check(matches(withText(emailExpected)));
        onView(withId(R.id.btn_confirm_payment)).perform(click());

        // Wait for request
        SystemClock.sleep(5000);

        // Check for virtual account
        onView(withId(R.id.text_company_code)).check(matches(not(withText(""))));
        onView(withId(R.id.text_company_code)).check(matches(not(withText(R.string.virtual_account_number))));
        onView(withId(R.id.text_bill_pay_code)).check(matches(not(withText(""))));
        onView(withId(R.id.text_bill_pay_code)).check(matches(not(withText(R.string.virtual_account_number))));
        onView(withId(R.id.btn_confirm_payment)).perform(click());

        // Check transaction status
        onView(withId(R.id.text_title_payment_status)).check(matches(withText(R.string.payment_pending)));
    }

    @Test
    public void bankTransferPermataFlowTest() {
        //Initializing bank transfer payment
        onView(withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        SystemClock.sleep(10000);

        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.rv_bank_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        //Check email address text
        onView(withId(R.id.et_email)).perform(clearText(), typeText(email), closeSoftKeyboard()).check(matches(withText(emailExpected)));
        onView(withId(R.id.btn_confirm_payment)).perform(click());

        // Wait for request
        SystemClock.sleep(5000);

        // Check for virtual account
        onView(withId(R.id.text_virtual_account_number)).check(matches(not(withText(""))));
        onView(withId(R.id.text_virtual_account_number)).check(matches(not(withText(R.string.virtual_account_number))));
        onView(withId(R.id.btn_confirm_payment)).perform(click());

        // Check transaction status
        onView(withId(R.id.text_title_payment_status)).check(matches(withText(R.string.payment_pending)));
    }

    @Test
    public void bankTransferOtherBankFlowTest() {
        //Initializing bank transfer payment
        onView(withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        SystemClock.sleep(10000);

        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.rv_bank_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        //Check email address text
        onView(withId(R.id.et_email)).perform(clearText(), typeText(email), closeSoftKeyboard()).check(matches(withText(emailExpected)));
        onView(withId(R.id.btn_confirm_payment)).perform(click());

        // Wait for request
        SystemClock.sleep(5000);

        // Check for virtual account
        onView(withId(R.id.text_virtual_account_number)).check(matches(not(withText(""))));
        onView(withId(R.id.text_virtual_account_number)).check(matches(not(withText(R.string.virtual_account_number))));
        onView(withId(R.id.btn_confirm_payment)).perform(click());

        // Check transaction status
        onView(withId(R.id.text_title_payment_status)).check(matches(withText(R.string.payment_pending)));
    }
}
