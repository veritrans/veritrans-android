package com.midtrans.sdk.uikit.view.method.creditcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.view.method.creditcard.adapter.SavedCreditCardAdapter;
import com.midtrans.sdk.uikit.view.method.creditcard.details.CreditCardDetailsActivity;
import com.midtrans.sdk.uikit.widget.FancyButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SavedCreditCardListActivity extends BasePaymentActivity implements SavedCreditCardListContract {

    private SavedCreditCardListPresenter presenter;

    private RecyclerView listCard;
    private FancyButton buttonAddCard;

    private SavedCreditCardAdapter adapter;

    private boolean isAlreadyGotResponse = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_credit_card_list);
        initToolbarAndView();
        initProperties();
        initActionButton();
        initCardsContainer();
        initSavedCards();
    }

    @Override
    protected void initToolbarAndView() {
        super.initToolbarAndView();
        buttonAddCard = findViewById(R.id.btn_add_card);
        listCard = findViewById(R.id.container_saved_card);
    }

    private void initActionButton() {
        setTitle(getString(R.string.saved_card));
        buttonAddCard.setOnClickListener(v -> showCardDetailPage(null));
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
        setTextColor(buttonAddCard);
        setIconColorFilter(buttonAddCard);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new SavedCreditCardListPresenter(this, this, paymentInfoResponse);
        adapter = new SavedCreditCardAdapter(new SavedCreditCardAdapter.SavedCardListener() {
            @Override
            public void onItemClick(int position) {
                SaveCardRequest savedCard = adapter.getDataAt(position);
                showCardDetailPage(savedCard);
            }

            @Override
            public void onItemPromo(int position) {

            }

            @Override
            public void onItemDelete(int position) {
                SaveCardRequest savedCard = adapter.getDataAt(position);
                showDeleteCardConfirmationDialog(savedCard);
            }
        });
    }

    private void initCardsContainer() {
        listCard.setLayoutManager(new LinearLayoutManager(this));
        listCard.setAdapter(adapter);
    }

    private void initSavedCards() {
        if (presenter.isSavedCardEnabled()) {
            if (presenter.isSavedCardsAvailable()) {
                MessageHelper.showToast(this,"MAKE SERVER SNAP");
                setSavedCards(presenter.getSavedCards());
            } else if (!presenter.isBuiltInTokenStorage()) {
                MessageHelper.showToast(this,"MAKE SERVER SENDIRI");
                fetchSavedCard();
            } else {
                MessageHelper.showToast(this,"GAK ADA DATA CC");
                showCardDetailPage(null);
            }
        } else {
            MessageHelper.showToast(this,"HARUS ONEKLIK ATO TWOKLIK");
            showCardDetailPage(null);
        }
    }

    private void setSavedCards(List<SaveCardRequest> savedCards) {
        adapter.setData(savedCards);
    }

    public void fetchSavedCard() {
        showProgressLayout();
        presenter.fetchSavedCards();
    }

    private void showDeleteCardConfirmationDialog(final SaveCardRequest request) {
        if (isActivityRunning()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.card_delete_message)
                    .setPositiveButton(R.string.text_yes, (dialogInterface, j) -> {
                        dialogInterface.dismiss();
                        showProgressLayout();
                        presenter.deleteSavedCard(paymentInfoResponse.getToken(), request);
                    })
                    .setNegativeButton(R.string.text_no, (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            alertDialog.show();
        }
    }

    private void showCardDetailPage(SaveCardRequest savedCard) {
        /*Intent intent = new Intent(this, CreditCardDetailsActivity.class);
        intent.putExtra(CreditCardDetailsActivity.EXTRA_SAVED_CARD, savedCard);
        //pass deep link flag to credit card detail page
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        intent.putExtra(CreditCardDetailsActivity.USE_DEEP_LINK, isFirstPage);
        startActivityForResult(intent, Constants.INTENT_CARD_DETAILS);*/
    }

    public String getBankByBin(String cardBin) {
        return presenter.getBankByCardBin(cardBin);
    }

    private void updateSavedCardsInstance(String maskedCardNumber) {
        presenter.updateSavedCardsInstance(maskedCardNumber);
        if (presenter.isSavedCardsAvailable()) {
            setSavedCards(presenter.getSavedCards());
        } else {
            showCardDetailPage(null);
        }
    }

    @Override
    public void onNullInstanceSdk() {
        setResult(Constants.RESULT_SDK_NOT_AVAILABLE);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_WEBVIEW_PAYMENT && resultCode == Activity.RESULT_OK) {
            finishPayment(RESULT_OK, null);
        }
    }

    @Override
    public void onGetSavedCardsSuccess(List<SaveCardRequest> savedCards) {
        hideProgressLayout();
        if (!savedCards.isEmpty()) {
            setSavedCards(savedCards);
        } else {
            showCardDetailPage(null);
        }
    }

    @Override
    public void onGetSavedCardTokenFailure() {
        hideProgressLayout();
        showCardDetailPage(null);
    }

    @Override
    public void onDeleteCardSuccess(String maskedCard) {
        hideProgressLayout();
        updateSavedCardsInstance(maskedCard);
    }

    @Override
    public void onDeleteCardFailure() {
        hideProgressLayout();
    }
}