package com.midtrans.sdk.ui.views.creditcard.saved;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.adapters.SavedCardsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.models.SavedCard;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.views.creditcard.details.CreditCardDetailsActivity;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Picasso;

/**
 * Created by rakawm on 3/29/17.
 */

public class SavedCardActivity extends BaseActivity implements SavedCardView {
    private static final int CARD_DETAILS_REQUEST_CODE = 1024;

    private RecyclerView itemDetails;
    private RecyclerView cardsContainer;
    private FancyButton addCardButton;

    private ProgressDialog progressDialog;
    private MidtransUi midtransUi;
    private SavedCardPresenter presenter;
    private SavedCardsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_card);
        initPresenter();
        initMidtransUi();
        initViews();
        initThemes();
        initItemDetails();
        initToolbar();
        initToolbarBackButton();
        initAdapter();
        initCardsContainer();
        initCards();
        initProgressDialog();
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
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        cardsContainer = (RecyclerView) findViewById(R.id.container_saved_card);
        addCardButton = (FancyButton) findViewById(R.id.btn_add_card);
    }

    private void initThemes() {
        // Set color theme for item details bar
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        // Set color theme for scan card button
        setBorderColor(addCardButton);
        setTextColor(addCardButton);
        setIconColorFilter(addCardButton);

        initThemeColor();
    }

    private void initItemDetails() {
        ItemDetailsAdapter itemDetailsAdapter = new ItemDetailsAdapter(new ItemDetailsAdapter.ItemDetailListener() {
            @Override
            public void onItemShown() {
                // Do nothing
            }
        }, presenter.createItemDetails(this));
        itemDetails.setLayoutManager(new LinearLayoutManager(this));
        itemDetails.setAdapter(itemDetailsAdapter);
    }

    private void initToolbar() {
        // Set title
        TextView titleText = (TextView) findViewById(R.id.page_title);
        titleText.setText(R.string.saved_card);

        // Set merchant logo
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        Picasso.with(this)
                .load(midtransUi.getTransaction().merchant.preference.logoUrl)
                .into(merchantLogo);

        initToolbarBackButton();
        // Set back button click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
                    startCardDetails(savedToken.getSavedToken());
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

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
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

    private void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }
}
