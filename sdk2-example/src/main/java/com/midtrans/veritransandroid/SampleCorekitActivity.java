package com.midtrans.veritransandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.merchant.Address;
import com.midtrans.sdk.core.models.merchant.CheckoutOrderDetails;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenResponse;
import com.midtrans.sdk.core.models.merchant.CreditCard;
import com.midtrans.sdk.core.models.merchant.CustomerDetails;
import com.midtrans.sdk.core.models.merchant.ItemDetails;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentParams;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;
import com.midtrans.sdk.core.utils.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SampleCorekitActivity extends AppCompatActivity {
    EditText cardNumber, cardCvv, cardExpiryMonth, cardExpiryYear,
            klikBcaUserId, mandiriClickpayCardNumber, mandiriClickpayTokenResponse,
            telkomselCashToken, indosatPhone, giftCardNumber, giftCardPin;
    Button getToken, checkout, payNowCC, payNowBcaVa,
            payNowMandiriBill, payNowPermataVa, payNowOtherVa,
            payNowBcaKlikPay, payNowKlikBca, payNowEpayBri, payNowCimbClicks,
            payNowMandiriEcash, payNowMandiriClickpay, payNowTelkomselCash,
            payNowIndosatDompetku, payNowXlTunai, payNowIndomaret,
            payNowKioson, payNowGiftCard, btnUiSdk;
    TextView cardToken, checkoutToken;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardNumber = (EditText) findViewById(R.id.card_number);
        cardCvv = (EditText) findViewById(R.id.card_cvv);
        cardExpiryMonth = (EditText) findViewById(R.id.card_expiry_month);
        cardExpiryYear = (EditText) findViewById(R.id.card_expiry_year);
        klikBcaUserId = (EditText) findViewById(R.id.klikbca_user_id);
        mandiriClickpayCardNumber = (EditText) findViewById(R.id.mandiri_clickpay_card);
        mandiriClickpayTokenResponse = (EditText) findViewById(R.id.mandiri_clickpay_token_response);
        telkomselCashToken = (EditText) findViewById(R.id.telkomsel_cash_token);
        indosatPhone = (EditText) findViewById(R.id.indosat_phone_number);
        giftCardNumber = (EditText) findViewById(R.id.gift_card_number);
        giftCardPin = (EditText) findViewById(R.id.gift_card_pin);

        cardToken = (TextView) findViewById(R.id.card_token);
        checkoutToken = (TextView) findViewById(R.id.checkout_token);

        getToken = (Button) findViewById(R.id.btn_get_token);
        checkout = (Button) findViewById(R.id.btn_checkout);

        payNowCC = (Button) findViewById(R.id.btn_pay_now_cc);
        payNowBcaVa = (Button) findViewById(R.id.btn_pay_now_bca_va);
        payNowMandiriBill = (Button) findViewById(R.id.btn_pay_now_mandiri_bill);
        payNowPermataVa = (Button) findViewById(R.id.btn_pay_now_permata_va);
        payNowOtherVa = (Button) findViewById(R.id.btn_pay_now_other_va);
        payNowBcaKlikPay = (Button) findViewById(R.id.btn_pay_now_bca_klikpay);
        payNowKlikBca = (Button) findViewById(R.id.btn_pay_now_klikbca);
        payNowEpayBri = (Button) findViewById(R.id.btn_pay_now_epay_bri);
        payNowCimbClicks = (Button) findViewById(R.id.btn_pay_now_cimb_clicks);
        payNowMandiriEcash = (Button) findViewById(R.id.btn_pay_now_mandiri_ecash);
        payNowMandiriClickpay = (Button) findViewById(R.id.btn_pay_now_mandiri_clickpay);
        payNowTelkomselCash = (Button) findViewById(R.id.btn_pay_now_telkomsel_cash);
        payNowIndosatDompetku = (Button) findViewById(R.id.btn_pay_now_indosat_dompetku);
        payNowXlTunai = (Button) findViewById(R.id.btn_pay_now_xl_tunai);
        payNowIndomaret = (Button) findViewById(R.id.btn_pay_now_indomaret);
        payNowKioson = (Button) findViewById(R.id.btn_pay_now_kioson);
        payNowGiftCard = (Button) findViewById(R.id.btn_pay_now_gift_card);
        btnUiSdk = (Button)findViewById(R.id.btn_uisdk);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.txt_loading));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        getToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardTokenRequest request = CardTokenRequest.newNormalCard(
                        cardNumber.getText().toString(),
                        cardCvv.getText().toString(),
                        cardExpiryMonth.getText().toString(),
                        cardExpiryYear.getText().toString()
                );
                progressDialog.show();
                MidtransCore.getInstance().getCardToken(request, new MidtransCoreCallback<CardTokenResponse>() {
                    @Override
                    public void onSuccess(CardTokenResponse object) {
                        progressDialog.dismiss();
                        cardToken.setText(object.tokenId);
                        Toast.makeText(SampleCorekitActivity.this, "Card Token: " + object.tokenId, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(CardTokenResponse object) {
                        progressDialog.dismiss();
                        Toast.makeText(SampleCorekitActivity.this, object.statusMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(SampleCorekitActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                MidtransCore.getInstance().checkout(MyApp.CHECKOUT_URL, initialiseCheckoutTokenRequest(), new MidtransCoreCallback<CheckoutTokenResponse>() {
                    @Override
                    public void onSuccess(CheckoutTokenResponse object) {
                        progressDialog.dismiss();
                        checkoutToken.setText(object.token);
                        Toast.makeText(SampleCorekitActivity.this, "Checkout Token: " + object.token, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(CheckoutTokenResponse object) {
                        progressDialog.dismiss();
                        Toast.makeText(SampleCorekitActivity.this, object.errorMessages.get(0), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(SampleCorekitActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        payNowCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())
                        && !TextUtils.isEmpty(cardToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingCreditCard(checkoutToken.getText().toString(), buildCreditCardPaymentParams(cardToken.getText().toString()), buildSnapCustomerDetails(), new MidtransCoreCallback<CreditCardPaymentResponse>() {
                        @Override
                        public void onSuccess(CreditCardPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with CC success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(CreditCardPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token and card token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowBcaVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingBcaBankTransfer(checkoutToken.getText().toString(), buildSnapCustomerDetails(), new MidtransCoreCallback<BcaBankTransferPaymentResponse>() {
                        @Override
                        public void onSuccess(BcaBankTransferPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with BCA VA pending", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(BcaBankTransferPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with BCA VA failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with BCA VA failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowMandiriBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingMandiriBankTransfer(checkoutToken.getText().toString(), buildSnapCustomerDetails(), new MidtransCoreCallback<MandiriBankTransferPaymentResponse>() {
                        @Override
                        public void onSuccess(MandiriBankTransferPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Mandiri Bill pending", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(MandiriBankTransferPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Mandiri Bill failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Mandiri Bill failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowPermataVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingPermataBankTransfer(checkoutToken.getText().toString(), buildSnapCustomerDetails(), new MidtransCoreCallback<PermataBankTransferPaymentResponse>() {
                        @Override
                        public void onSuccess(PermataBankTransferPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Permata VA pending", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(PermataBankTransferPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Permata VA failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Permata VA failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowOtherVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingOtherBankTransfer(checkoutToken.getText().toString(), buildSnapCustomerDetails(), new MidtransCoreCallback<OtherBankTransferPaymentResponse>() {
                        @Override
                        public void onSuccess(OtherBankTransferPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Other VA pending", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(OtherBankTransferPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Other VA failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Other VA failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowBcaKlikPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingBcaKlikpay(checkoutToken.getText().toString(), new MidtransCoreCallback<BcaKlikpayPaymentResponse>() {
                        @Override
                        public void onSuccess(BcaKlikpayPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with BCA KlikPay pending. Redirect URL: " + object.redirectUrl, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(BcaKlikpayPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with BCA KlikPay failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with BCA KlikPay failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowKlikBca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())
                        && !TextUtils.isEmpty(klikBcaUserId.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingKlikBca(checkoutToken.getText().toString(), klikBcaUserId.getText().toString(), new MidtransCoreCallback<KlikBcaPaymentResponse>() {
                        @Override
                        public void onSuccess(KlikBcaPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with KlikBCA pending.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(KlikBcaPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with KlikBCA failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with KlikBCA failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token and fill klikBCA user id first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowEpayBri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingEpayBri(checkoutToken.getText().toString(), new MidtransCoreCallback<EpayBriPaymentResponse>() {
                        @Override
                        public void onSuccess(EpayBriPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with E-Pay BRI pending. Redirect URL: " + object.redirectUrl, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(EpayBriPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with E-Pay BRI failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with E-Pay BRI failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowCimbClicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingCimbClicks(checkoutToken.getText().toString(), new MidtransCoreCallback<CimbClicksPaymentResponse>() {
                        @Override
                        public void onSuccess(CimbClicksPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with CIMB Clicks pending. Redirect URL: " + object.redirectUrl, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(CimbClicksPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with CIMB Clicks failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with CIMB Clicks failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowMandiriEcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingMandiriECash(checkoutToken.getText().toString(), new MidtransCoreCallback<MandiriECashPaymentResponse>() {
                        @Override
                        public void onSuccess(MandiriECashPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Mandiri E-Cash pending. Redirect URL: " + object.redirectUrl, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(MandiriECashPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Mandiri E-Cash failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Mandiri E-Cash failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowMandiriClickpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    if (!TextUtils.isEmpty(mandiriClickpayCardNumber.getText().toString())) {
                        if (!TextUtils.isEmpty(mandiriClickpayTokenResponse.getText().toString())) {
                            // Start payment
                            progressDialog.show();
                            MandiriClickpayPaymentParams paymentParams = new MandiriClickpayPaymentParams(
                                    mandiriClickpayCardNumber.getText().toString(),
                                    String.valueOf(Utilities.generateRandomNumber()),
                                    mandiriClickpayTokenResponse.getText().toString()
                            );
                            MidtransCore.getInstance().paymentUsingMandiriClickpay(checkoutToken.getText().toString(), paymentParams, new MidtransCoreCallback<MandiriClickpayPaymentResponse>() {
                                @Override
                                public void onSuccess(MandiriClickpayPaymentResponse object) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SampleCorekitActivity.this, "Payment with Mandiri Clickpay success.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(MandiriClickpayPaymentResponse object) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SampleCorekitActivity.this, "Payment with Mandiri Clickpay failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SampleCorekitActivity.this, "Payment with Mandiri Clickpay failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SampleCorekitActivity.this, "You must fill mandiri token response.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SampleCorekitActivity.this, "You must get fill mandiri clickpay number.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowTelkomselCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString()) && !TextUtils.isEmpty(telkomselCashToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingTelkomselCash(checkoutToken.getText().toString(), telkomselCashToken.getText().toString(), new MidtransCoreCallback<TelkomselCashPaymentResponse>() {
                        @Override
                        public void onSuccess(TelkomselCashPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Telkomsel Cash success.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(TelkomselCashPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Telkomsel Cash failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Telkomsel Cash failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first and fill token.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowIndosatDompetku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start payment
                progressDialog.show();

                if (!TextUtils.isEmpty(checkoutToken.getText().toString())
                        && !TextUtils.isEmpty(indosatPhone.getText().toString())) {
                    MidtransCore.getInstance().paymentUsingIndosatDompetku(checkoutToken.getText().toString(), indosatPhone.getText().toString(), new MidtransCoreCallback<IndosatDompetkuPaymentResponse>() {
                        @Override
                        public void onSuccess(IndosatDompetkuPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Indosat Dompetku success.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(IndosatDompetkuPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Indosat Dompetku failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Indosat Dompetku failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first and fill phone number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowXlTunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start Payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingXlTunai(checkoutToken.getText().toString(), new MidtransCoreCallback<XlTunaiPaymentResponse>() {
                        @Override
                        public void onSuccess(XlTunaiPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(
                                    SampleCorekitActivity.this,
                                    String.format("Payment with XL Tunai pending. ID: %s, Merchant ID: %s", object.xlTunaiOrderId, object.xlTunaiMerchantId),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(XlTunaiPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with XL Tunai failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with XL Tunai failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowIndomaret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start Payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingIndomaret(checkoutToken.getText().toString(), new MidtransCoreCallback<IndomaretPaymentResponse>() {
                        @Override
                        public void onSuccess(IndomaretPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(
                                    SampleCorekitActivity.this,
                                    String.format("Payment with Indomaret pending. ID: %s", object.paymentCode),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(IndomaretPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Indomaret failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Indomaret failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowKioson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    // Start payment
                    progressDialog.show();
                    MidtransCore.getInstance().paymentUsingKioson(checkoutToken.getText().toString(), new MidtransCoreCallback<KiosonPaymentResponse>() {
                        @Override
                        public void onSuccess(KiosonPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(
                                    SampleCorekitActivity.this,
                                    String.format("Payment with Kioson pending. ID: %s", object.paymentCode),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(KiosonPaymentResponse object) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Kioson failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(SampleCorekitActivity.this, "Payment with Kioson failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payNowGiftCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(checkoutToken.getText().toString())) {
                    if (!TextUtils.isEmpty(giftCardNumber.getText().toString())) {
                        if (!TextUtils.isEmpty(giftCardPin.getText().toString())) {
                            MidtransCore.getInstance().paymentUsingGiftCard(checkoutToken.getText().toString(), giftCardNumber.getText().toString(), giftCardPin.getText().toString(), new MidtransCoreCallback<GiftCardPaymentResponse>() {
                                @Override
                                public void onSuccess(GiftCardPaymentResponse object) {
                                    progressDialog.dismiss();
                                    Toast.makeText(
                                            SampleCorekitActivity.this,
                                            String.format("Payment with Gift Card success. Approval: %s", object.approvalCode),
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(GiftCardPaymentResponse object) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SampleCorekitActivity.this, "Payment with Gift Card failed, " + object.statusMessage, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SampleCorekitActivity.this, "Payment with Gift Card failed, " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SampleCorekitActivity.this, "You must fill gift card pin number.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SampleCorekitActivity.this, "You must fill gift card number.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SampleCorekitActivity.this, "You must get checkout token first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private CheckoutTokenRequest initialiseCheckoutTokenRequest() {
        // Setup order details
        CheckoutOrderDetails checkoutOrderDetails = new CheckoutOrderDetails(UUID.randomUUID().toString(), 200000);
        // Setup customer details
        CustomerDetails customerDetails = new CustomerDetails(
                "Raka", "Mogandhi", "rakamogandhi@hotmail.com", "082140518011",
                new Address("Raka", "Mogandhi", "Jalan Wirajaya 312", "Yogyakarta", "55283", "082140518011", "IDN"),
                new Address("Raka", "Mogandhi", "Jalan Wirajaya 312", "Yogyakarta", "55283", "082140518011", "IDN"));

        // Setup item details
        List<ItemDetails> itemDetailsList = new ArrayList<>();
        ItemDetails itemDetails = new ItemDetails("item1", 200000, 1, "VT SDK");
        itemDetailsList.add(itemDetails);

        //creditcard properties
        CreditCard creditCard = new CreditCard();
        creditCard.setSecure(false);
        return CheckoutTokenRequest.newCompleteCheckout("rakawm-test1", creditCard, itemDetailsList, customerDetails, checkoutOrderDetails, null, null);
    }

    private CreditCardPaymentParams buildCreditCardPaymentParams(String cardToken) {
        return CreditCardPaymentParams.newBasicPaymentParams(cardToken);
    }

    private SnapCustomerDetails buildSnapCustomerDetails() {
        return new SnapCustomerDetails("Raka Westu Mogandhi", "rakamogandhi@hotmail.com", "082140518011");
    }
}
