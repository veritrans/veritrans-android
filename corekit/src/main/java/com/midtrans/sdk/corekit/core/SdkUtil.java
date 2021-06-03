package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.SnapTransactionDetails;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.promo.Promo;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.params.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.GCIPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.KlikBcaPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.PromoDetails;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CustomerDetailRequest;
import com.midtrans.sdk.corekit.models.snap.payment.DanamonOnlinePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.QrisPaymentParameter;
import com.midtrans.sdk.corekit.models.snap.payment.ShopeePayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.ShopeePayQrisPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.UobEzpayPaymentRequest;
import com.midtrans.sdk.corekit.utilities.Installation;

import java.util.Collections;
import java.util.List;

/**
 * Created by ziahaqi on 18/06/2016.
 */
public class SdkUtil {
    private static final String UNIT_MINUTES = "minutes";

    /**
     * Get device identifier using SDK context.
     *
     * @return device identifier
     */
    public static String getDeviceId(Context context) {
        String deviceId = "UNKNOWN";
        try {
            deviceId = Installation.id(context);
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
        return deviceId;
    }

    public static TokenRequestModel getSnapTokenRequestModel(TransactionRequest transactionRequest) {

        SnapTransactionDetails details = new SnapTransactionDetails(transactionRequest.getOrderId(), transactionRequest.getAmount());

        // set currency
        details.setCurrency(transactionRequest.getCurrency());

        TokenRequestModel requestModel = new TokenRequestModel(details, transactionRequest.getItemDetails(),
                transactionRequest.getCustomerDetails(), transactionRequest.getCreditCard());
        requestModel.setExpiry(transactionRequest.getExpiry());


        // Set expiry if it's available
        if (transactionRequest.getExpiry() != null) {
            requestModel.setExpiry(transactionRequest.getExpiry());
        }

        if (transactionRequest.getCustomField1() != null && !TextUtils.isEmpty(transactionRequest.getCustomField1())) {
            requestModel.setCustomField1(transactionRequest.getCustomField1());
        }

        if (transactionRequest.getCustomField2() != null && !TextUtils.isEmpty(transactionRequest.getCustomField2())) {
            requestModel.setCustomField2(transactionRequest.getCustomField2());
        }

        if (transactionRequest.getCustomField3() != null && !TextUtils.isEmpty(transactionRequest.getCustomField3())) {
            requestModel.setCustomField3(transactionRequest.getCustomField3());
        }

        if (transactionRequest.getPermataVa() != null) {
            requestModel.setPermataVa(transactionRequest.getPermataVa());
        }

        if (transactionRequest.getBcaVa() != null) {
            requestModel.setBcaVa(transactionRequest.getBcaVa());
        }

        if (transactionRequest.getBniVa() != null) {
            requestModel.setBcaVa(transactionRequest.getBniVa());
        }

        if (transactionRequest.getEnabledPayments() != null
                && !transactionRequest.getEnabledPayments().isEmpty()) {
            requestModel.setEnabledPayments(transactionRequest.getEnabledPayments());
        }

        if (transactionRequest.getGopay() != null) {
            requestModel.setGopay(transactionRequest.getGopay());
        }

        if (transactionRequest.getShopeepay() != null) {
            requestModel.setShopeepay(transactionRequest.getShopeepay());
        }

        return requestModel;
    }

    public static CustomerDetailRequest initializePaymentDetails(Transaction transaction) {
        if (transaction != null) {
            CustomerDetails customerDetails = transaction.getCustomerDetails();
            if (customerDetails != null) {
                CustomerDetailRequest customerDetailRequest = new CustomerDetailRequest();
                customerDetailRequest.setFullName(customerDetails.getFirstName());
                customerDetailRequest.setPhone(customerDetails.getPhone());
                customerDetailRequest.setEmail(customerDetails.getEmail());

                return customerDetailRequest;
            }
        }

        return null;
    }

    public static CreditCardPaymentRequest getCreditCardPaymentRequest(CreditCardPaymentModel model, Transaction transaction) {

        CustomerDetailRequest customerDetailRequest = initializePaymentDetails(transaction);

        CreditCardPaymentParams paymentParams = new CreditCardPaymentParams(model.getCardToken(),
                model.isSavecard(), model.getMaskedCardNumber(), model.getInstallment());
        paymentParams.setPointRedeemed(model.getPointRedeemed());

        if (model.getBank() != null && model.getBank().equalsIgnoreCase(BankType.MANDIRI)) {
            paymentParams.setBank(model.getBank());
        }

        //for custom type adapter in gson, omit field point only
        // if the transaction is not from bank point page
        paymentParams.setFromBankPoint(model.isFromBankPoint());

        CreditCardPaymentRequest creditCardPaymentRequest = new CreditCardPaymentRequest(PaymentType.CREDIT_CARD, paymentParams, customerDetailRequest);

        //set promo is selected
        Promo promo = model.getPromoSelected();
        if (promo != null) {
            PromoDetails promoDetails = new PromoDetails(promo.getId(), promo.getDiscountedGrossAmount());
            creditCardPaymentRequest.setPromoDetails(promoDetails);
        }

        return creditCardPaymentRequest;
    }


    public static BankTransferPaymentRequest getBankTransferPaymentRequest(String email, String paymentType) {
        CustomerDetailRequest request = new CustomerDetailRequest();
        request.setEmail(email);
        BankTransferPaymentRequest paymentRequest = new BankTransferPaymentRequest(paymentType, request);
        return paymentRequest;
    }

    public static KlikBCAPaymentRequest getKlikBCAPaymentRequest(String userId, String paymentType) {

        KlikBCAPaymentRequest klikBCAPaymentRequest = new KlikBCAPaymentRequest(
                paymentType, new KlikBcaPaymentParams(userId));
        return klikBCAPaymentRequest;
    }


    public static GCIPaymentRequest getGCIPaymentRequest(String cardNumber, String password) {
        GCIPaymentParams paymentParams = new GCIPaymentParams(cardNumber, password);
        GCIPaymentRequest request = new GCIPaymentRequest(paymentParams, PaymentType.GCI);
        return request;
    }


    public static GoPayPaymentRequest getGoPayPaymentRequest() {
        return new GoPayPaymentRequest(PaymentType.GOPAY);
    }

    public static ShopeePayPaymentRequest getShopeePayPaymentRequest() {
        return new ShopeePayPaymentRequest(PaymentType.SHOPEEPAY);
    }

    public static ShopeePayQrisPaymentRequest getShopeePayQrisPaymentRequest() {
        List<String> acquirer = Collections.singletonList(QrisAcquirer.SHOPEEPAY);
        return new ShopeePayQrisPaymentRequest(PaymentType.QRIS, new QrisPaymentParameter(acquirer));
    }

    public static DanamonOnlinePaymentRequest getDanamonOnlinePaymentRequest() {
        return new DanamonOnlinePaymentRequest(PaymentType.DANAMON_ONLINE);
    }

    public static UobEzpayPaymentRequest getUobEzpayPaymentRequest() {
        return new UobEzpayPaymentRequest(PaymentType.UOB_EZPAY);
    }

    public static SnapServiceManager newSnapServiceManager(int requestTimeOut) {
        return new SnapServiceManager(MidtransRestAdapter.newSnapApiService(requestTimeOut));
    }

    public static MidtransServiceManager newMidtransServiceManager(int requestTimeOut) {
        return new MidtransServiceManager(MidtransRestAdapter.newMidtransApiService(requestTimeOut));
    }

    public static MerchantServiceManager newMerchantServiceManager(String merchantServerUrl, int requestTimeOut) {
        if (TextUtils.isEmpty(merchantServerUrl)) {
            return null;
        }

        return new MerchantServiceManager(MidtransRestAdapter.newMerchantApiService(merchantServerUrl, requestTimeOut));
    }

    public static MixpanelAnalyticsManager newMixpanelAnalyticsManager(String versionName, String deviceId, String merchantName, String flow, String deviceType, boolean isLogEnabled, Context context) {
        return new MixpanelAnalyticsManager(versionName, deviceId, merchantName, flow, deviceType, isLogEnabled, context);
    }
}
