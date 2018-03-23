package com.midtrans.sdk.uikit.views.creditcard.saved;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.adapters.SavedCardsAdapter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.creditcard.details.CreditCardDetailsActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 7/21/17.
 */

public class SavedCreditCardActivity extends BasePaymentActivity implements SavedCreditCardView {

    private RecyclerView listCard;
    private FancyButton buttonAddCard;
    private SemiBoldTextView textTitle;

    private SavedCreditCardPresenter presenter;
    private SavedCardsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_credit_card);
        initProperties();
        initTitle();
        initCardsContainer();
        initTheme();
        initActionButton();
        initSavedCards();
    }

    private void initTitle() {
        textTitle.setText(getString(R.string.saved_card));
    }

    private void initCardsContainer() {
        listCard.setLayoutManager(new LinearLayoutManager(this));
        listCard.setAdapter(adapter);
    }

    private void initActionButton() {
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardDetailPage(null);
            }
        });
    }

    @Override
    public void initTheme() {
        setTextColor(buttonAddCard);
        setIconColorFilter(buttonAddCard);
    }

    private void initProperties() {
        presenter = new SavedCreditCardPresenter(this, this);
        adapter = new SavedCardsAdapter();

        adapter.setListener(new SavedCardsAdapter.SavedCardAdapterEventListener() {
            @Override
            public void onItemClick(int position) {
                SaveCardRequest savedCard = adapter.getItem(position);
                showCardDetailPage(savedCard);
            }
        });

        adapter.setDeleteCardListener(new SavedCardsAdapter.DeleteCardListener() {
            @Override
            public void onItemDelete(int position) {
                SaveCardRequest request = adapter.getItem(position);
                showDeleteCardConfirmationDialog(request);
            }
        });
    }

    private void showDeleteCardConfirmationDialog(final SaveCardRequest request) {
        if (isActivityRunning()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.card_delete_message)
                    .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            dialogInterface.dismiss();
                            showProgressLayout();
                            presenter.deleteSavedCard(request);
                        }
                    })
                    .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }


    @Override
    public void bindViews() {
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
        buttonAddCard = (FancyButton) findViewById(R.id.btn_add_card);
        listCard = (RecyclerView) findViewById(R.id.container_saved_card);
    }

    private void initSavedCards() {
        if (presenter.isSavedCardEnabled()) {
            if (presenter.isSavedCardsAvailable()) {
                setSavedCards(presenter.getSavedCards());
            } else if (!presenter.isBuiltInTokenStorage()) {
                fetchSavedCard();
            } else {
                showCardDetailPage(null);
            }
        } else {
            showCardDetailPage(null);
        }
    }

    private void setSavedCards(List<SaveCardRequest> savedCards) {
        adapter.setData(new ArrayList<>(savedCards));
    }

    private void showCardDetailPage(SaveCardRequest savedCard) {
        Intent intent = new Intent(this, CreditCardDetailsActivity.class);
        intent.putExtra(CreditCardDetailsActivity.EXTRA_SAVED_CARD, savedCard);
        //pass deep link flag to credit card detail page
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        intent.putExtra(CreditCardDetailsActivity.USE_DEEP_LINK, isFirstPage);
        startActivityForResult(intent, UiKitConstants.INTENT_CARD_DETAILS);
    }

    public void fetchSavedCard() {
        showProgressLayout();
        presenter.fetchSavedCards();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            TransactionResponse result = (TransactionResponse) data.getSerializableExtra(UiKitConstants.KEY_TRANSACTION_RESPONSE);
            if (result != null) {
                finishPayment(resultCode, data);
            }
        } else if (resultCode == UiKitConstants.INTENT_RESULT_DELETE_CARD) {
            String maskedCardNumber = data.getStringExtra(CreditCardDetailsActivity.EXTRA_DELETED_CARD_DETAILS);
            updateSavedCardsInstance(maskedCardNumber);
        } else {
            if (!presenter.isSavedCardEnabled() || !presenter.isSavedCardsAvailable()) {
                finish();
            } else {
                getMidtransSdk().resetPaymentDetails();
            }
        }
    }

    private void updateSavedCardsInstance(String maskedCardNumber) {
        presenter.updateSavedCardsInstance(maskedCardNumber);
        if (presenter.isSavedCardsAvailable()) {
            setSavedCards(presenter.getSavedCards());
        } else {
            showCardDetailPage(null);
        }
    }

    public String getBankByBin(String cardBin) {
        return presenter.getBankByCardBin(cardBin);
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onGetSavedCardsSuccess(List<SaveCardRequest> savedCards) {
        hideProgressLayout();

        if (isActivityRunning()) {
            if (!savedCards.isEmpty()) {
                setSavedCards(savedCards);
            } else {
                showCardDetailPage(null);
            }
        }
    }

    @Override
    public void onGetSavedCardTokenFailure() {
        if (isActivityRunning()) {
            showCardDetailPage(null);
        }
    }

    @Override
    public void onDeleteCardSuccess(String maskedCard) {
        hideProgressLayout();
        if (isActivityRunning()) {
            updateSavedCardsInstance(maskedCard);
        }
    }

    @Override
    public void onDeleteCardFailure() {
        hideProgressLayout();
        if (isActivityRunning()) {
            SdkUIFlowUtil.showToast(this, getString(R.string.error_delete_message));
        }
    }


}
