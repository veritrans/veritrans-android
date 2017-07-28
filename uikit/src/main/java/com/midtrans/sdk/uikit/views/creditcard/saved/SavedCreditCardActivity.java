package com.midtrans.sdk.uikit.views.creditcard.saved;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.adapters.SavedCardsAdapter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.creditcard.details.CreditCardDetailsActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 7/21/17.
 */

public class SavedCreditCardActivity extends BasePaymentActivity implements SavedCreditCardView {
    private static final int CARD_DETAILS_REQUEST_CODE = 1024;
    private static final int MAX_RETRY = 3;

    private int retry = 1;

    private RecyclerView cardsContainer;
    private FancyButton addCardButton;
    private DefaultTextView textTitle;
    private LinearLayout progressContainer;
    private DefaultTextView progressBarMessage;
    private ImageView imageProgress;

    private SavedCreditCardPresenter presenter;
    private SavedCardsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_credit_card);
        initProperties();
        bindView();
        initProgressLayout();
        initTitle();
        initCardsContainer();
        initTheme();
        initActionButton();
        initSavedCards();
    }

    private void initProgressLayout() {
        Glide.with(this)
                .load(R.drawable.midtrans_loader)
                .asGif()
                .into(imageProgress);
    }

    private void initTitle() {
        textTitle.setText(getString(R.string.saved_card));
    }

    private void initCardsContainer() {
        cardsContainer.setLayoutManager(new LinearLayoutManager(this));
        cardsContainer.setAdapter(adapter);
    }

    private void initActionButton() {
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardDetailPage(null);
            }
        });
    }

    private void initTheme() {
        setBorderColor(addCardButton);
        setTextColor(addCardButton);
        setIconColorFilter(addCardButton);
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
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.card_delete_message)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        dialogInterface.dismiss();
                        showProgressLayout(true);
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


    private void bindView() {
        textTitle = (DefaultTextView) findViewById(R.id.text_page_title);
        addCardButton = (FancyButton) findViewById(R.id.btn_add_card);
        cardsContainer = (RecyclerView) findViewById(R.id.container_saved_card);
        progressContainer = (LinearLayout) findViewById(R.id.progress_container);
        progressBarMessage = (DefaultTextView) findViewById(R.id.progress_bar_message);
        imageProgress = (ImageView) findViewById(R.id.progress_bar_image);
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
        if (savedCard != null) {
            intent.putExtra(CreditCardDetailsActivity.EXTRA_SAVED_CARD, savedCard);
        }
        startActivityForResult(intent, UiKitConstants.INTENT_CARD_DETAILS);
    }


    public void fetchSavedCard() {
        showProgressLayout(true);
        presenter.fetchSavedCards();
    }

    private void showProgressLayout(boolean show) {
        if (show) {
            progressContainer.setVisibility(View.VISIBLE);
        } else {
            progressContainer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            TransactionResponse result = (TransactionResponse) data.getSerializableExtra(getString(R.string.transaction_response));
            if (result != null) {
                finishPayment(resultCode, data);
            }
        } else if (resultCode == UiKitConstants.INTENT_RESULT_DELETE_CARD) {
            String maskedCardNumber = data.getStringExtra(CreditCardDetailsActivity.EXTRA_DELETED_CARD_DETAILS);
            updateSavedCardsInstance(maskedCardNumber);
        } else {
            if (!presenter.isSavedCardEnabled() || !presenter.isSavedCardsAvailable()) {
                finish();
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

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onGetSavedCardsSuccess(List<SaveCardRequest> savedCards) {
        if (!savedCards.isEmpty()) {
            showProgressLayout(false);
            setSavedCards(savedCards);
        } else {
            showCardDetailPage(null);
        }
    }

    @Override
    public void onGetSavedCardTokenFailed() {
        showCardDetailPage(null);

    }

    @Override
    public void onCardDeletionSuccess(String maskedCard) {
        showProgressLayout(false);
        updateSavedCardsInstance(maskedCard);
    }

    @Override
    public void onCardDeletionFailed() {
        showProgressLayout(false);
        SdkUIFlowUtil.showToast(this, getString(R.string.error_delete_message));
    }

    public String getBankByBin(String cardBin) {
        return presenter.getBankByCardBin(cardBin);
    }

}
