package com.midtrans.sdk.uikit.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.midtrans.sdk.corekit.base.enums.BankType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.SavedToken;
import com.midtrans.sdk.corekit.core.api.snap.model.bins.BankBinsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.Promo;
import com.midtrans.sdk.corekit.core.api.snap.model.promo.PromoDetails;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.base.model.CreditCardPaymentModel;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static <T> T parseToModel(String jsonToParse, Class<T> classOfT) {
        Gson gson = new GsonBuilder().create();
        T model = gson.fromJson(jsonToParse, classOfT);
        return model;
    }

    /**
     * it will validate an given email-id.
     *
     * @param email email string
     * @return true if given email-id is valid else returns false
     */
    public static boolean isEmailValid(String email) {

        try {
            if (!TextUtils.isEmpty(email)) {
                Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
                if (pattern != null) {
                    Matcher matcher = pattern.matcher(email.trim());
                    return matcher.matches();
                }
            }
        } catch (RuntimeException e) {
            Logger.error(e.getMessage());
        }
        return false;
    }

    /**
     * Utility method which will help to close the keyboard.
     *
     * @param activity activity instance
     */
    public static void hideKeyboard(Activity activity) {
        try {
            Logger.info("hide keyboard");
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (RuntimeException e) {
            Logger.debug("hideKeyboard():" + e.getMessage());
        }
    }

    public static CreditCardPaymentParams convertToCreditCardPaymentParam(CreditCardPaymentModel model) {

        CreditCardPaymentParams paymentParams = new CreditCardPaymentParams(
                model.getCardToken(),
                model.isSavecard(),
                model.getMaskedCardNumber(),
                model.getInstallment()
        );
        if (model.getPointRedeemed() != 0) {
            paymentParams.setPointRedeemed(model.getPointRedeemed());
        }

        if (model.getBank() != null && model.getBank().equalsIgnoreCase(BankType.MANDIRI)) {
            paymentParams.setBank(model.getBank());
        }

        //for custom type adapter in gson, omit field point only
        // if the transaction is not from bank point page
        paymentParams.setFromBankPoint(model.isFromBankPoint());
        return paymentParams;
    }

    public static PromoDetails getPromoDetails(CreditCardPaymentModel model) {
        //set promo is selected
        Promo promo = model.getPromoSelected();
        if (promo != null) {
            return new PromoDetails(promo.getId(), promo.getDiscountedGrossAmount());
        }
        return null;
    }

    public static CustomerDetailPayRequest initializePaymentDetails(PaymentInfoResponse transaction) {
        if (transaction != null) {
            CustomerDetails customerDetails = transaction.getCustomerDetails();
            if (customerDetails != null) {
                CustomerDetailPayRequest customerDetailRequest = new CustomerDetailPayRequest();
                customerDetailRequest.setFullName(customerDetails.getFirstName() + customerDetails.getLastName());
                customerDetailRequest.setPhone(customerDetailRequest.getPhone());
                customerDetailRequest.setEmail(customerDetailRequest.getEmail());

                return customerDetailRequest;
            }
        }

        return null;
    }

    public static String getDeviceType(Activity activity) {
        String deviceType;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        if (diagonalInches >= 6.5) {
            deviceType = "TABLET";
        } else {
            deviceType = "PHONE";
        }

        return deviceType;
    }

    public static String formatDouble(double d) {
        String result = "0";
        try {
            result = d == (long) d ? String.format("%d", (long) d) : String.format("%s", d);

        } catch (RuntimeException e) {
            Logger.error("formatDouble():" + e.getMessage());
        }

        return result;
    }

    /**
     * It will generate random 5 digit number. It is used as input3 in mandiri click pay.
     *
     * @return random number
     */
    public static int generateRandomNumber() {
        int number;
        int high = 99999;
        int low = 10000;
        byte[] randomBytes = new byte[128];

        try {
            SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            secureRandomGenerator.nextBytes(randomBytes);
            number = secureRandomGenerator.nextInt(high - low) + low;
        } catch (NoSuchAlgorithmException e) {
            Logger.error("random number : " + e.getMessage());
            Random random = new Random();
            number = random.nextInt(high - low) + low;
        }
        return number;
    }


    /**
     * it will validate an given card number.
     *
     * @param ccNumber credit card number
     * @return true if given card number is valid else returns false.
     */
    public static boolean isValidCardNumber(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        boolean isvalid = (sum % 10 == 0);
        Logger.info("isValid:" + isvalid);
        return isvalid;
    }


    public static List<SaveCardRequest> filterMultipleSavedCard(List<SaveCardRequest> savedCards) {
        if (savedCards != null) {
            Collections.reverse(savedCards);
            Set<String> maskedCardSet = new HashSet<>();
            for (Iterator<SaveCardRequest> it = savedCards.iterator(); it.hasNext(); ) {
                if (!maskedCardSet.add(it.next().getMaskedCard())) {
                    it.remove();
                }
            }
        }
        return savedCards;
    }

    public static List<SaveCardRequest> convertSavedTokens(List<SavedToken> savedTokens) {
        List<SaveCardRequest> cards = new ArrayList<>();
        if (savedTokens != null && !savedTokens.isEmpty()) {
            for (SavedToken saved : savedTokens) {
                cards.add(new SaveCardRequest(saved.getToken(), saved.getMaskedCard(), saved.getTokenType()));
            }
        }
        return cards;
    }

    public static List<SavedToken> convertSavedCards(List<SaveCardRequest> savedCards) {
        List<SavedToken> cards = new ArrayList<>();
        if (savedCards != null && !savedCards.isEmpty()) {
            for (SaveCardRequest saved : savedCards) {
                SavedToken savedToken = new SavedToken();
                savedToken.setTokenType(saved.getType());
                savedToken.setMaskedCard(saved.getMaskedCard());
                savedToken.setToken(saved.getSavedTokenId());
                cards.add(savedToken);
            }
        }
        return cards;
    }

    public static ArrayList<BankBinsResponse> getBankBins(Context context) {
        ArrayList<BankBinsResponse> list = null;
        String data;
        try {
            InputStream is = context.getAssets().open("bank_bins.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            data = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            list = gson.fromJson(data, new TypeToken<ArrayList<BankBinsResponse>>() {
            }.getType());

        } catch (Exception e) {
            Logger.error(e.getMessage());
        }

        return list;
    }

    public static String getMaskedCardNumber(String maskedCard) {
        StringBuilder builder = new StringBuilder();
        String bulletMask = "●●●●●●";
        String newMaskedCard = maskedCard.replace("-", bulletMask);

        for (int i = 0; i < newMaskedCard.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                builder.append(' ');
                builder.append(newMaskedCard.charAt(i));
            } else {
                builder.append(newMaskedCard.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String getMaskedExpDate() {
        String bulletMask = "●●";
        String maskedDate = bulletMask + " / " + bulletMask;
        return maskedDate;
    }

    public static String getMaskedCardCvv() {
        String bulletMask = "●●●";

        return bulletMask;
    }
}