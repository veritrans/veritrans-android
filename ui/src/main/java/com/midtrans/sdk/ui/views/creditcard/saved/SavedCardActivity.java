package com.midtrans.sdk.ui.views.creditcard.saved;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseItemDetailsActivity;
import com.midtrans.sdk.ui.adapters.SavedCardsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.models.SavedCard;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.views.creditcard.details.CreditCardDetailsActivity;
import com.midtrans.sdk.ui.views.status.PaymentStatusActivity;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by rakawm on 3/29/17.
 */

public class SavedCardActivity extends BaseItemDetailsActivity implements SavedCardView {
    private static final int CARD_DETAILS_REQUEST_CODE = 1024;
    private static final int MAX_RETRY = 3;

    private int retry = 1;

    private RecyclerView cardsContainer;
    private FancyButton addCardButton;

    private MidtransUi midtransUi;
    private SavedCardPresenter presenter;
    private SavedCardsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_card);
        initMidtransUi();
        initPresenter();
        initViews();
        initThemes();
        initAdapter();
        initCardsContainer();
        initCards();
        initAddCardButton();
    }

    private void initPresenter() {
        presenter = new SavedCardPresenter(this);
        presenter.setSavedCardView(this);
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initViews() {
        cardsContainer = (RecyclerView) findViewById(R.id.container_saved_card);
        addCardButton = (FancyButton) findViewById(R.id.btn_add_card);
    }

    private void initThemes() {
        // Set color theme for scan card button
        setBorderColor(addCardButton);
        setTextColor(addCardButton);
        setIconColorFilter(addCardButton);
    }

    private void initAdapter() {
        adapter = new SavedCardsAdapter();
    }

    private void initCardsContainer() {
        cardsContainer.setLayoutManager(new LinearLayoutManager(this));
        cardsContainer.setAdapter(adapter);
    }

    private void initCards() {
        if (presenter.getSavedCards() != null
                && !presenter.getSavedCards().isEmpty()) {
            adapter.setData(presenter.getSavedCards());
            adapter.notifyDataSetChanged();
            adapter.setListener(new SavedCardsAdapter.SavedCardAdapterEventListener() {
                @Override
                public void onItemClick(int position) {
                    SavedCard savedToken = adapter.getItem(position);
                    if (savedToken.getSavedToken().tokenType.equalsIgnoreCase(SavedToken.ONE_CLICK)) {
                        showProgressDialog(getString(R.string.processing_payment));
                        presenter.startPayment(savedToken);
                    } else {
                        startCardDetails(savedToken.getSavedToken());
                    }
                }
            });
            adapter.setDeleteCardListener(new SavedCardsAdapter.DeleteCardListener() {
                @Override
                public void onItemDelete(int position) {
                    if (position != RecyclerView.NO_POSITION) {
                        SavedCard savedToken = adapter.getItem(position);
                        showDeleteConfirmation(savedToken.getSavedToken());
                    }
                }
            });
        } else {
            startCardDetails();
        }
    }

    private void showDeleteConfirmation(final SavedToken savedToken) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.card_delete_message)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        deleteCard(savedToken);
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
        changeDialogButtonColor(alertDialog);
    }

    private void changeDialogButtonColor(AlertDialog alertDialog) {
        if (alertDialog.isShowing()
                && midtransUi != null
                && midtransUi.getColorTheme() != null
                && midtransUi.getColorTheme().getPrimaryDarkColor() != 0) {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            positiveButton.setTextColor(midtransUi.getColorTheme().getPrimaryDarkColor());
            negativeButton.setTextColor(midtransUi.getColorTheme().getPrimaryDarkColor());
        }
    }

    private void deleteCard(SavedToken savedToken) {
        showProgressDialog(getString(R.string.processing_delete));
        presenter.deleteCard(savedToken);
    }

    private void startCardDetails(SavedToken savedToken) {
        Intent intent = new Intent(this, CreditCardDetailsActivity.class);
        intent.putExtra(CreditCardDetailsActivity.EXTRA_CARD_DETAILS, savedToken);
        startActivityForResult(intent, CARD_DETAILS_REQUEST_CODE);
    }

    private void startCardDetails() {
        Intent intent = new Intent(this, CreditCardDetailsActivity.class);
        startActivityForResult(intent, CARD_DETAILS_REQUEST_CODE);
    }

    private void initAddCardButton() {
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCardDetails();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CARD_DETAILS_REQUEST_CODE) {
                PaymentResult result = (PaymentResult) data.getSerializableExtra(Constants.PAYMENT_RESULT);
                Logger.d(TAG, "onActivityResult(): response:" + result);
                if (result != null) {
                    finishPayment(resultCode, result);
                }
            }
        } else if (resultCode == RESULT_FIRST_USER) {
            if (requestCode == CARD_DETAILS_REQUEST_CODE) {
                SavedToken savedToken = (SavedToken) data.getSerializableExtra(CreditCardDetailsActivity.EXTRA_DELETED_CARD_DETAILS);
                deleteCardFromAdapter(savedToken);
            }
        }
    }

    private void deleteCardFromAdapter(SavedToken savedToken) {
        if (savedToken != null) {
            adapter.deleteCard(savedToken.maskedCard);
            if (adapter.getItemCount() == 0) {
                // Notify transaction activity to show blank card details
                setResult(RESULT_FIRST_USER);
                finish();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void finishPayment(int resultCode, PaymentResult<CreditCardPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onDeleteCardSuccess(SavedToken savedToken) {
        hideProgressDialog();
        deleteCardFromAdapter(savedToken);
    }

    @Override
    public void onDeleteCardFailure(String errorMessage) {
        hideProgressDialog();
        Toast.makeText(this, getString(R.string.error_delete_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreditCardPaymentSuccess(CreditCardPaymentResponse response) {
        hideProgressDialog();
        if (midtransUi.getCustomSetting().isShowPaymentStatus()) {
            startPaymentResult(new PaymentResult<>(response));
        } else {
            finishPayment(RESULT_OK, new PaymentResult<>(response));
        }
    }

    @Override
    public void onCreditCardPaymentFailure(CreditCardPaymentResponse response) {
        hideProgressDialog();
        if (retry < MAX_RETRY) {
            UiUtils.showToast(this, getString(R.string.message_payment_failed));
        } else {
            if (presenter.isPaymentStatusEnabled()) {
                startPaymentResult(presenter.getPaymentResult());
            } else {
                finishPayment(RESULT_OK, presenter.getPaymentResult());
            }
        }
    }

    @Override
    public void onCreditCardPaymentError(String errorMessage) {
        hideProgressDialog();
        if (retry < MAX_RETRY) {
            UiUtils.showToast(this, getString(R.string.message_payment_failed));
        } else {
            if (presenter.isPaymentStatusEnabled()) {
                startPaymentResult(presenter.getPaymentResult());
            } else {
                finishPayment(RESULT_OK, presenter.getPaymentResult());
            }
        }
    }

    private void showProgressDialog(String message) {
        UiUtils.showProgressDialog(this, message, false);
    }

    private void hideProgressDialog() {
        UiUtils.hideProgressDialog();
    }

    private void startPaymentResult(PaymentResult<CreditCardPaymentResponse> paymentResult) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }
}
