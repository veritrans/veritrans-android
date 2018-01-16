package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.models.BCABankTransfer;
import com.midtrans.sdk.corekit.models.BCAKlikPayDescriptionModel;
import com.midtrans.sdk.corekit.models.BCAKlikPayModel;
import com.midtrans.sdk.corekit.models.BankTransfer;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CIMBClickPayModel;
import com.midtrans.sdk.corekit.models.CardPaymentDetails;
import com.midtrans.sdk.corekit.models.CardTransfer;
import com.midtrans.sdk.corekit.models.CstoreEntity;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.DescriptionModel;
import com.midtrans.sdk.corekit.models.EpayBriTransfer;
import com.midtrans.sdk.corekit.models.IndomaretRequestModel;
import com.midtrans.sdk.corekit.models.IndosatDompetkuRequest;
import com.midtrans.sdk.corekit.models.KlikBCADescriptionModel;
import com.midtrans.sdk.corekit.models.KlikBCAModel;
import com.midtrans.sdk.corekit.models.MandiriBillPayTransferModel;
import com.midtrans.sdk.corekit.models.MandiriClickPayModel;
import com.midtrans.sdk.corekit.models.MandiriClickPayRequestModel;
import com.midtrans.sdk.corekit.models.MandiriECashModel;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.PermataBankTransfer;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.SnapTransactionDetails;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.TransactionDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.SnapPromo;
import com.midtrans.sdk.corekit.models.snap.params.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.GCIPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.KlikBcaPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.MandiriClickPayPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.PromoDetails;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CustomerDetailRequest;
import com.midtrans.sdk.corekit.models.snap.payment.DanamonOnlinePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayAuthorizationRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.MandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.utilities.Installation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ziahaqi on 18/06/2016.
 */
public class SdkUtil {
    private static final String UNIT_MINUTES = "minutes";

    /**
     * helper method to extract {@link MandiriBillPayTransferModel} from {@link
     * TransactionRequest}.
     *
     * @param request transaction request object
     * @return Transfer model object
     */
    protected static MandiriBillPayTransferModel getMandiriBillPayModel(TransactionRequest
                                                                                request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());
        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        MandiriBillPayTransferModel model =
                new MandiriBillPayTransferModel(request.getBillInfoModel(),
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(), request.getCustomerDetails());
        return model;
    }


    /**
     * helper method to extract {@link MandiriClickPayModel} from {@link TransactionRequest}.
     *
     * @param request transaction request object
     * @return Transfer model object
     */
    protected static MandiriClickPayRequestModel getMandiriClickPayRequestModel(TransactionRequest
                                                                                        request,
                                                                                MandiriClickPayModel mandiriClickPayModel) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        MandiriClickPayRequestModel model =
                new MandiriClickPayRequestModel(mandiriClickPayModel,
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(), request.getCustomerDetails());
        return model;
    }

    protected static KlikBCAModel getKlikBCAModel(TransactionRequest request, KlikBCADescriptionModel descriptionModel) {
        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(), request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        return new KlikBCAModel(
                descriptionModel,
                transactionDetails,
                request.getItemDetails(),
                request.getBillingAddressArrayList(),
                request.getShippingAddressArrayList(),
                request.getCustomerDetails()
        );
    }

    protected static BCAKlikPayModel getBCAKlikPayModel(TransactionRequest request,
                                                        BCAKlikPayDescriptionModel descriptionModel) {
        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(), request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        return new BCAKlikPayModel(
                descriptionModel,
                transactionDetails,
                request.getItemDetails(),
                request.getBillingAddressArrayList(),
                request.getShippingAddressArrayList(),
                request.getCustomerDetails()
        );
    }


    /**
     * helper method to extract {@link PermataBankTransfer} from {@link TransactionRequest}.
     *
     * @param request transaction request
     * @return Transfer model object
     */
    protected static PermataBankTransfer getPermataBankModel(TransactionRequest request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        // bank name
        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setBank(MidtransSDK.getInstance().getContext().getString(R.string.payment_permata));

        return new PermataBankTransfer(bankTransfer,
                transactionDetails, request.getItemDetails(),
                request.getBillingAddressArrayList(),
                request.getShippingAddressArrayList(),
                request.getCustomerDetails());

    }

    /**
     * helper method to extract {@link PermataBankTransfer} from {@link TransactionRequest}.
     *
     * @param request Transaction request
     * @return Transfer model object
     */
    protected static BCABankTransfer getBcaBankTransferRequest(TransactionRequest request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        // bank name
        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setBank(MidtransSDK.getInstance().getContext().getString(R.string.payment_bca));


        BCABankTransfer model =
                new BCABankTransfer(bankTransfer,
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;

    }


    /**
     * helper method to extract {@link IndomaretRequestModel} from {@link TransactionRequest}.
     *
     * @param request transaction request object
     * @return transfer model object
     */
    protected static IndomaretRequestModel getIndomaretRequestModel(TransactionRequest request,
                                                                    CstoreEntity cstoreEntity) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        IndomaretRequestModel model =
                new IndomaretRequestModel();

        model.setPaymentType(MidtransSDK.getInstance().getContext().getString(R.string.payment_indomaret));

        model.setItem_details(request.getItemDetails());
        model.setCustomerDetails(request.getCustomerDetails());
        model.setTransactionDetails(transactionDetails);
        model.setCstore(cstoreEntity);

        return model;

    }


    /**
     * helper method to extract {@link CIMBClickPayModel} from {@link TransactionRequest}.
     *
     * @param cimbDescription CIMB bank description
     * @param request         transaction request
     * @return transfer model object
     */

    protected static CIMBClickPayModel getCIMBClickPayModel(TransactionRequest request,
                                                            DescriptionModel cimbDescription) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        CIMBClickPayModel model =
                new CIMBClickPayModel(cimbDescription, transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;
    }

    /**
     * helper method to extract {@link MandiriECashModel} from {@link TransactionRequest}.
     *
     * @return Mandiri E Cash Model object
     */

    protected static MandiriECashModel getMandiriECashModel(TransactionRequest request,
                                                            DescriptionModel description) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        MandiriECashModel model =
                new MandiriECashModel(description, transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;
    }

    /**
     * helper method to extract {@link CardTransfer} from {@link TransactionRequest}.
     *
     * @param request            transaction request
     * @param cardPaymentDetails payment details
     * @return Card transfer model object
     */
    public static CardTransfer getCardTransferModel(TransactionRequest request,
                                                    CardPaymentDetails cardPaymentDetails) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        CardTransfer model =
                new CardTransfer(cardPaymentDetails,
                        transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;
    }


    /**
     * helper method to extract {@link EpayBriTransfer} from {@link TransactionRequest}.
     *
     * @param request transaction request object
     * @return E Pay BRI transfer model
     */
    protected static EpayBriTransfer getEpayBriBankModel(TransactionRequest request) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        if (request.isUiEnabled()) {
            //get user details only if using default ui.
            request = initializeUserInfo(request);
        }

        EpayBriTransfer model =
                new EpayBriTransfer(transactionDetails, request.getItemDetails(),
                        request.getBillingAddressArrayList(),
                        request.getShippingAddressArrayList(),
                        request.getCustomerDetails());
        return model;

    }

    /**
     * helper method to extract {@link IndosatDompetkuRequest} from {@link TransactionRequest}.
     *
     * @param request transaction request object
     * @return transfer model object
     */
    protected static IndosatDompetkuRequest getIndosatDompetkuRequestModel(TransactionRequest
                                                                                   request,
                                                                           String msisdn) {

        TransactionDetails transactionDetails = new TransactionDetails("" + request.getAmount(),
                request.getOrderId());

        //get user details only if using default ui.
        request = initializeUserInfo(request);

        IndosatDompetkuRequest model =
                new IndosatDompetkuRequest();

        model.setCustomerDetails(request.getCustomerDetails(), request
                .getShippingAddressArrayList(), request.getBillingAddressArrayList());
        model.setPaymentType(MidtransSDK.getInstance().getContext().getString(R.string.payment_indosat_dompetku));

        IndosatDompetkuRequest.IndosatDompetkuEntity entity = new IndosatDompetkuRequest
                .IndosatDompetkuEntity();
        entity.setMsisdn("" + msisdn);

        model.setIndosatDompetku(entity);
        model.setItemDetails(request.getItemDetails());
        model.setTransactionDetails(transactionDetails);

        return model;
    }

    /**
     * helper method to add {@link CustomerDetails} in {@link TransactionRequest}.
     *
     * @param transactionRequest transaction request
     * @return transactionRequest with  {@link CustomerDetails}.
     */
    protected static TransactionRequest initializeUserInfo(TransactionRequest transactionRequest) {
        transactionRequest = getUserDetails(transactionRequest);
        return transactionRequest;
    }

    /**
     * it extracts customer information from TransactionRequest.
     *
     * @param request instance of TransactionRequest
     * @return transaction request with {@link UserDetail}
     */
    static TransactionRequest getUserDetails(TransactionRequest request) {

        UserDetail userDetail = null;
        CustomerDetails mCustomerDetails = null;

        try {
            userDetail = LocalDataHandler.readObject(MidtransSDK.getInstance().getContext().getString(R.string.user_details), UserDetail.class);

            if (userDetail != null && !TextUtils.isEmpty(userDetail.getUserFullName())) {
                ArrayList<UserAddress> userAddresses = userDetail.getUserAddresses();
                if (userAddresses != null && !userAddresses.isEmpty()) {
                    Logger.i("Found " + userAddresses.size() + " user addresses.");
                    mCustomerDetails = new CustomerDetails();
                    mCustomerDetails.setPhone(userDetail.getPhoneNumber());
                    mCustomerDetails.setFirstName(userDetail.getUserFullName());
                    mCustomerDetails.setLastName(null);
                    mCustomerDetails.setEmail(userDetail.getEmail());
                    //added email in performTransaction()
                    request.setCustomerDetails(mCustomerDetails);

                    request = extractUserAddress(userDetail, userAddresses, request);
                }

            } else {
                Logger.e("User details not available.");
            }
        } catch (Exception ex) {
            Logger.e("Error while fetching user details : " + ex.getMessage());
        }

        return request;
    }

    static TransactionRequest extractUserAddress(UserDetail userDetail,
                                                 ArrayList<UserAddress> userAddresses,
                                                 TransactionRequest request) {

        ArrayList<BillingAddress> billingAddressArrayList = new ArrayList<>();
        ArrayList<ShippingAddress> shippingAddressArrayList = new ArrayList<>();

        for (int i = 0; i < userAddresses.size(); i++) {

            UserAddress userAddress = userAddresses.get(i);

            if (userAddress.getAddressType() == Constants.ADDRESS_TYPE_BOTH) {

                BillingAddress billingAddress = getBillingAddress(userDetail, userAddress);
                billingAddressArrayList.add(billingAddress);
                ShippingAddress shippingAddress = getShippingAddress(userDetail, userAddress);
                shippingAddressArrayList.add(shippingAddress);
            } else if (userAddress.getAddressType() == Constants.ADDRESS_TYPE_SHIPPING) {
                ShippingAddress shippingAddress = getShippingAddress(userDetail, userAddress);
                shippingAddressArrayList.add(shippingAddress);
            } else {
                BillingAddress billingAddress = getBillingAddress(userDetail, userAddress);
                billingAddressArrayList.add(billingAddress);
            }

        }

        request.setBillingAddressArrayList(billingAddressArrayList);
        request.setShippingAddressArrayList(shippingAddressArrayList);

        CustomerDetails customerDetails = request.getCustomerDetails();

        if (customerDetails != null) {
            if (billingAddressArrayList.size() > 0 && billingAddressArrayList.get(0) != null) {
                customerDetails.setBillingAddress(billingAddressArrayList.get(0));
            }

            if (shippingAddressArrayList.size() > 0 && shippingAddressArrayList.get(0) != null) {
                customerDetails.setShippingAddress(shippingAddressArrayList.get(0));
            }
            request.setCustomerDetails(customerDetails);
        }
        return request;
    }

    @NonNull
    private static BillingAddress getBillingAddress(UserDetail userDetail, UserAddress userAddress) {
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setCity(userAddress.getCity());
        billingAddress.setFirstName(userDetail.getUserFullName());
        billingAddress.setLastName("");
        billingAddress.setPhone(userDetail.getPhoneNumber());
        billingAddress.setCountryCode(userAddress.getCountry());
        billingAddress.setPostalCode(userAddress.getZipcode());
        billingAddress.setAddress(userAddress.getAddress());
        return billingAddress;
    }


    @NonNull
    private static ShippingAddress getShippingAddress(UserDetail userDetail, UserAddress userAddress) {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setCity(userAddress.getCity());
        shippingAddress.setFirstName(userDetail.getUserFullName());
        shippingAddress.setLastName("");
        shippingAddress.setPhone(userDetail.getPhoneNumber());
        shippingAddress.setCountryCode(userAddress.getCountry());
        shippingAddress.setPostalCode(userAddress.getZipcode());
        shippingAddress.setAddress(userAddress.getAddress());
        return shippingAddress;
    }

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

        if (transactionRequest.isUiEnabled()) {
            //get user details only if using default ui.
            transactionRequest = initializeUserInfo(transactionRequest);
        }

        SnapTransactionDetails details = new SnapTransactionDetails(transactionRequest.getOrderId(), (int) transactionRequest.getAmount());

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

        // Set promo is available
        if (transactionRequest.isPromoEnabled()) {
            SnapPromo promo = new SnapPromo();
            promo.setEnabled(true);

            if (transactionRequest.getPromoCodes() != null && !transactionRequest.getPromoCodes().isEmpty()) {
                promo.setAllowedPromoCodes(transactionRequest.getPromoCodes());
            }
            requestModel.setPromo(promo);
        }

        return requestModel;
    }

    public static CustomerDetailRequest initializePaymentDetails(TransactionRequest transactionRequest) {
        CustomerDetailRequest customerDetailRequest = new CustomerDetailRequest();
        customerDetailRequest.setFullName(transactionRequest.getCustomerDetails().getFirstName());
        customerDetailRequest.setPhone(transactionRequest.getCustomerDetails().getPhone());
        customerDetailRequest.setEmail(transactionRequest.getCustomerDetails().getEmail());
        return customerDetailRequest;
    }

    public static CreditCardPaymentRequest getCreditCardPaymentRequest(CreditCardPaymentModel model, TransactionRequest transactionRequest) {
        if (transactionRequest.isUiEnabled()) {
            // get user details only if using default ui
            transactionRequest = initializeUserInfo(transactionRequest);
        }

        CustomerDetailRequest customerDetailRequest = initializePaymentDetails(transactionRequest);
        CreditCardPaymentParams paymentParams = new CreditCardPaymentParams(model.getCardToken(),
                model.isSavecard(), model.getMaskedCardNumber(), model.getInstallment());
        paymentParams.setPointRedeemed(model.getPointRedeemed());

        if (model.getBank() != null && model.getBank().equalsIgnoreCase(BankType.MANDIRI)) {
            paymentParams.setBank(model.getBank());
        }

        //for custom type adapter in gson, omit field point only
        // if the transaction is not from bank point page
        paymentParams.setFromBankPoint(model.isFromBankPoint());

        return new CreditCardPaymentRequest(PaymentType.CREDIT_CARD, paymentParams, customerDetailRequest);
    }

    public static CreditCardPaymentRequest getCreditCardPaymentRequest(String discountToken, Long discountAmount, CreditCardPaymentModel model, TransactionRequest transactionRequest) {
        if (transactionRequest.isUiEnabled()) {
            // get user details only if using default ui
            transactionRequest = initializeUserInfo(transactionRequest);
        }

        CustomerDetailRequest customerDetailRequest = initializePaymentDetails(transactionRequest);
        CreditCardPaymentParams paymentParams = new CreditCardPaymentParams(model.getCardToken(),
                model.isSavecard(), model.getMaskedCardNumber(), model.getInstallment());
        CreditCardPaymentRequest paymentRequest = new CreditCardPaymentRequest(PaymentType.CREDIT_CARD, paymentParams, customerDetailRequest);

        //set the promo
        PromoDetails promoDetails = new PromoDetails(discountToken, discountAmount);
        paymentRequest.setPromoDetails(promoDetails);

        return paymentRequest;
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

    public static String getEmailAddress(TransactionRequest transactionRequest) {
        return transactionRequest.getCustomerDetails().getEmail();
    }


    public static GCIPaymentRequest getGCIPaymentRequest(String cardNumber, String password) {
        GCIPaymentParams paymentParams = new GCIPaymentParams(cardNumber, password);
        GCIPaymentRequest request = new GCIPaymentRequest(paymentParams, PaymentType.GCI);
        return request;
    }

    public static MandiriClickPayPaymentRequest getMandiriClickPaymentRequest(String mandiriCardNumber, String tokenResponse,
                                                                              String input3, String paymentType) {

        MandiriClickPayPaymentParams paymentParams = new MandiriClickPayPaymentParams(mandiriCardNumber, input3, tokenResponse);
        MandiriClickPayPaymentRequest request = new MandiriClickPayPaymentRequest(paymentType, paymentParams);

        return request;
    }

    public static GoPayPaymentRequest getGoPayPaymentRequest() {
        return new GoPayPaymentRequest(PaymentType.GOPAY);
    }

    public static GoPayAuthorizationRequest getGoPayAuthorizationRequest(String otp) {
        return new GoPayAuthorizationRequest(otp);
    }

    public static DanamonOnlinePaymentRequest getDanamonOnlinePaymentRequest() {
        return new DanamonOnlinePaymentRequest(PaymentType.DANAMON_ONLINE);
    }

    /**
     * Sorting payment method by priority (Ascending)
     */
    public static void sortPaymentMethodsByPriority(ArrayList<PaymentMethodsModel> paymentMethodsModels) {
        Collections.sort(paymentMethodsModels, new Comparator<PaymentMethodsModel>() {
            @Override
            public int compare(PaymentMethodsModel lhs, PaymentMethodsModel rhs) {
                return lhs.getPriority().compareTo(rhs.getPriority());
            }
        });
    }
}
