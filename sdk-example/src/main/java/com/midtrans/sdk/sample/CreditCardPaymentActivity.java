package com.midtrans.sdk.sample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.sample.fragments.CardListPaymentFragment;
import com.midtrans.sdk.sample.fragments.NewCardPaymentFragment;

import java.util.ArrayList;
import java.util.List;

public class CreditCardPaymentActivity extends AppCompatActivity {
    ProgressDialog dialog;
    private boolean normalPayment;
    private ArrayList<SaveCardRequest> savedCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_credit_card_payment);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (MidtransSDK.getInstance().getTransactionRequest()
                .getCardClickType().equals(getString(R.string.card_click_type_none))) {
            this.normalPayment = true;
            NewCardPaymentFragment newCardPaymentFragment = NewCardPaymentFragment.newInstance(true);

            // setup transaction status fragment
            fragmentTransaction.replace(R.id.card_payment_container,
                    newCardPaymentFragment, NewCardPaymentFragment.TAG);
            fragmentTransaction.commit();
        }else{
            this.normalPayment = false;

            //when using builtin token storage (token storage on snap)
            if(MidtransSDK.getInstance().isEnableBuiltInTokenStorage()){
                initSaveCards(convertSavedToken(MidtransSDK.getInstance().getCreditCard().getSavedTokens()));
                initSavedCardLayout();
            } else{
                fetchSaveCardsFromMerchantServer();
            }
        }
    }

    private ArrayList<SaveCardRequest> convertSavedToken(List<SavedToken> savedTokens) {
        ArrayList<SaveCardRequest> cards = new ArrayList<>();
        if(savedTokens != null && !savedTokens.isEmpty()){
            for(SavedToken saved: savedTokens){
                cards.add(new SaveCardRequest(saved.getToken(), saved.getMaskedCard(), saved.getTokenType()));
            }
        }
        return cards;
    }

    private void fetchSaveCardsFromMerchantServer() {
        dialog.show();
        MidtransSDK.getInstance().getCards(MainActivity.SAMPLE_USER_ID, new GetCardCallback() {
            @Override
            public void onSuccess(ArrayList<SaveCardRequest> response) {
                dialog.dismiss();
                initSaveCards(response);
                initSavedCardLayout();
            }

            @Override
            public void onFailure(String reason) {
                Toast.makeText(CreditCardPaymentActivity.this, reason, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable error) {
                dialog.dismiss();
                Toast.makeText(CreditCardPaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initSaveCards(ArrayList<SaveCardRequest> savedCards) {
        Log.d("ccpaymentActivit", "savecard>isnull:" + savedCards);
        Log.d("ccpaymentActivit", "savecard>bultinsize:" + savedCards.size());

        this.savedCards.clear();
        this.savedCards.addAll(savedCards);
    }

    private void initSavedCardLayout(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CardListPaymentFragment fragment = CardListPaymentFragment.newInstance();
        transaction.replace(R.id.card_payment_container, fragment, CardListPaymentFragment.TAG);
        transaction.commit();
    }


    private void addFragmentTransaction(Fragment fragment, String tag){
        // setup transaction status fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.card_payment_container,
                fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void actionOnError(Throwable error) {
        // Handle generic error condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Unknown error: " + error.getMessage())
                .create();
        dialog.show();
    }

    private void saveCreditCard(ArrayList<SaveCardRequest> requests) {

        MidtransSDK.getInstance().saveCards(MainActivity.SAMPLE_USER_ID, requests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {

            }

            @Override
            public void onFailure(String reason) {
                Logger.i("savecard>failed");
                Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error) {
                actionOnError(error);
            }
        });
    }


    /**
     *
     * @param cardNumber
     * @param expMonth
     * @param expYear
     * @param cvv
     * @param saveCard
     */
    public void paymentNormal(String cardNumber, String expMonth, String expYear, String cvv, boolean saveCard){
        Log.d("paynormal", "cardnumber:" + cardNumber);
        Log.d("paynormal", "expmonth:" + expMonth);
        Log.d("paynormal", "expyears:" + expYear);
        Log.d("paynormal", "cvv:" + cvv);
        Log.d("paynormal", "grossamount:" + MidtransSDK.getInstance().getTransactionRequest().getAmount());
        CardTokenRequest cardTokenRequest = new CardTokenRequest(cardNumber, cvv,
                expMonth, expYear,
                MidtransSDK.getInstance().getClientKey());
        cardTokenRequest.setIsSaved(saveCard);
        cardTokenRequest.setSecure(MidtransSDK.getInstance().getTransactionRequest().isSecureCard());
        cardTokenRequest.setGrossAmount(MidtransSDK.getInstance().getTransactionRequest().getAmount());

        getCardToken(cardTokenRequest, saveCard);
    }

    /**
     *
     * @param maskedCard
     * @param savecard
     */
    public void paymentOneclick(String maskedCard, boolean savecard) {
        CreditCardPaymentModel model = new CreditCardPaymentModel(maskedCard);
        payTransaction(MidtransSDK.getInstance().readAuthenticationToken(), model, savecard);
    }


    /**
     *
     * @param cvv
     * @param savedTokenId
     */
    public void paymentTwoClick(String cvv, String savedTokenId, boolean saveCard) {
        CardTokenRequest cardTokenRequest = new CardTokenRequest();
        cardTokenRequest.setSavedTokenId(savedTokenId);
        cardTokenRequest.setCardCVV(cvv);
        cardTokenRequest.setTwoClick(true);
        cardTokenRequest.setSecure(MidtransSDK.getInstance().getTransactionRequest().isSecureCard());
        cardTokenRequest.setGrossAmount(MidtransSDK.getInstance().getTransactionRequest().getAmount());
        cardTokenRequest.setBank("");
        cardTokenRequest.setClientKey(MidtransSDK.getInstance().getClientKey());
        getCardToken(cardTokenRequest, saveCard);
    }

    private void getCardToken(CardTokenRequest cardTokenRequest, final boolean saveCard){
        dialog.show();
        MidtransSDK.getInstance().getCardToken(cardTokenRequest, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse response) {
                String tokenId = response.getTokenId();
                payTransaction(MidtransSDK.getInstance().readAuthenticationToken(), new CreditCardPaymentModel(tokenId, saveCard), saveCard);
            }

            @Override
            public void onFailure(TokenDetailsResponse response, String reason) {
                // Handle error when get token
                dialog.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(CreditCardPaymentActivity.this)
                        .setMessage(reason)
                        .create();
                dialog.show();
            }

            @Override
            public void onError(Throwable error) {
                actionOnError(error);
            }
        });
    }

    private void payTransaction(String authenticationToken, final CreditCardPaymentModel model, final boolean savecard){
        MidtransSDK.getInstance().paymentUsingCard(authenticationToken, model, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {

                // Handle success transaction
                dialog.dismiss();
                actionSaveCard(response, savecard);
                final AlertDialog dialog = new AlertDialog.Builder(CreditCardPaymentActivity.this)
                        .setMessage(response.getStatusMessage())
                        .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                        }).create();
                dialog.show();
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                // Handle failed transaction
                dialog.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(CreditCardPaymentActivity.this)
                        .setMessage(reason)
                        .create();
                dialog.show();
            }

            @Override
            public void onError(Throwable error) {
                actionOnError(error);
            }
        });
    }

    private void actionSaveCard(TransactionResponse response, boolean savecard) {
        if(!MidtransSDK.getInstance().isEnableBuiltInTokenStorage() && savecard){
            ArrayList<SaveCardRequest> requests = new ArrayList<>();

            String clickType;
            if(MidtransSDK.getInstance().getTransactionRequest().getCardClickType().equals(R.string.card_click_type_one_click)){
                //oneclick
                clickType = getString(R.string.saved_card_one_click);
            }else {
                //twoclick
                clickType = getString(R.string.saved_card_two_click);
            }

            requests.add(new SaveCardRequest(response.getSavedTokenId(),
                    response.getMaskedCard(), clickType));

            saveCreditCard(requests);
        }

    }

    public void onNewCard() {
        NewCardPaymentFragment newCardPaymentFragment = NewCardPaymentFragment.newInstance(normalPayment);
        addFragmentTransaction(newCardPaymentFragment, NewCardPaymentFragment.TAG);
    }

    public ArrayList<SaveCardRequest> getSavedCards() {
        return savedCards;
    }
}
