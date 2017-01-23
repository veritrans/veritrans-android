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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.clearElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webClick;
import static android.support.test.espresso.web.webdriver.DriverAtoms.webKeys;
import static com.midtrans.sdk.sample.EspressoTestsMatchers.withDrawable;

/**
 * Created by rakawm on 1/20/17.
 */
@RunWith(AndroidJUnit4.class)
public class PositiveInstallmentScenarioTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    String offlineCard, mandiriCard, bniCard, bcaCard, cardCVV, cardExpired;
    String offlineCardExpected, bniCardExpected, mandiriCardExpected, bcaCardExpected, cardCVVExcpected, cardExpiredExpected;
    String email, emailExpected;
    private String fullName, phone;
    private String address, city, zipcode, country;

    @Before
    public void setup() {
        offlineCard = "4811111111111114";
        mandiriCard = "4617006959746656";
        bniCard = "4105058689481467";
        bcaCard = "4773776057051650";
        offlineCardExpected = "4811 1111 1111 1114";
        mandiriCardExpected = "4617 0069 5974 6656";
        bniCardExpected = "4105 0586 8948 1467";
        bcaCardExpected = "4773 7760 5705 1650";
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
    public void creditCardOfflineInstallmentNormalFlowTest() {
        // Initializing credit card payment
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(offlineCard), closeSoftKeyboard()).check(matches(withText(offlineCardExpected)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.layout_installment)).check(matches(isDisplayed()));
        onView(withId(R.id.button_installment_increase)).perform(click());
        onView(withId(R.id.btn_pay_now)).perform(click());

        // Load charging request
        SystemClock.sleep(5000);
        onView(withId(R.id.text_title_payment_status)).check(matches(withText(R.string.payment_successful)));
    }

    @Test
    public void creditCardOfflineInstallment3DSFlowTest() {
        // Initializing credit card payment
        onView(withId(R.id.radio_secure)).perform(scrollTo(), click());
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(offlineCard), closeSoftKeyboard()).check(matches(withText(offlineCardExpected)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.layout_installment)).check(matches(isDisplayed()));
        onView(withId(R.id.button_installment_increase)).perform(click());

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
    public void creditCardMandiriInstallmentNormalFlowTest() {
        // Initializing credit card payment
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(mandiriCard), closeSoftKeyboard()).check(matches(withText(mandiriCardExpected)));
        onView(withId(R.id.bank_logo)).check(matches(withDrawable(R.drawable.mandiri)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.layout_installment)).check(matches(isDisplayed()));
        onView(withId(R.id.button_installment_increase)).perform(click());
        onView(withId(R.id.btn_pay_now)).perform(click());

        // Load charging request
        SystemClock.sleep(5000);
        onView(withId(R.id.text_title_payment_status)).check(matches(withText(R.string.payment_successful)));
    }

    @Test
    public void creditCardMandiriInstallment3DSFlowTest() {
        // Initializing credit card payment
        onView(withId(R.id.radio_secure)).perform(scrollTo(), click());
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(mandiriCard), closeSoftKeyboard()).check(matches(withText(mandiriCardExpected)));
        onView(withId(R.id.bank_logo)).check(matches(withDrawable(R.drawable.mandiri)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.layout_installment)).check(matches(isDisplayed()));
        onView(withId(R.id.button_installment_increase)).perform(click());

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
    public void creditCardBNIInstallmentNormalFlowTest() {
        // Initializing credit card payment
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(bniCard), closeSoftKeyboard()).check(matches(withText(bniCardExpected)));
        onView(withId(R.id.bank_logo)).check(matches(withDrawable(R.drawable.bni)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.layout_installment)).check(matches(isDisplayed()));
        onView(withId(R.id.button_installment_increase)).perform(click());
        onView(withId(R.id.btn_pay_now)).perform(click());

        // Load charging request
        SystemClock.sleep(5000);
        onView(withId(R.id.text_title_payment_status)).check(matches(withText(R.string.payment_successful)));
    }

    @Test
    public void creditCardBNIInstallment3DSFlowTest() {
        // Initializing credit card payment
        onView(withId(R.id.radio_secure)).perform(scrollTo(), click());
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(bniCard), closeSoftKeyboard()).check(matches(withText(bniCardExpected)));
        onView(withId(R.id.bank_logo)).check(matches(withDrawable(R.drawable.bni)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.layout_installment)).check(matches(isDisplayed()));
        onView(withId(R.id.button_installment_increase)).perform(click());

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
    public void creditCardBCAInstallment3DSFlowTest() {
        // Initializing credit card payment
        onView(withId(R.id.radio_secure)).perform(scrollTo(), click());
        onView(withId(R.id.radio_bca)).perform(scrollTo(), click());
        onView(ViewMatchers.withId(R.id.show_ui_flow)).perform(scrollTo(), click());
        // Load 3DS
        SystemClock.sleep(10000);
        onView(withId(R.id.rv_payment_methods)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Fill credit card data
        onView(withId(R.id.et_card_no)).perform(typeText(bcaCard), closeSoftKeyboard()).check(matches(withText(bcaCardExpected)));
        onView(withId(R.id.bank_logo)).check(matches(withDrawable(R.drawable.bca)));
        onView(withId(R.id.et_exp_date)).perform(typeText(cardExpired), closeSoftKeyboard()).check(matches(withText(cardExpiredExpected)));
        onView(withId(R.id.et_cvv)).perform(typeText(cardCVV), closeSoftKeyboard()).check(matches(withText(cardCVVExcpected)));

        onView(withId(R.id.layout_installment)).check(matches(isDisplayed()));
        onView(withId(R.id.button_installment_increase)).perform(click());

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
}
