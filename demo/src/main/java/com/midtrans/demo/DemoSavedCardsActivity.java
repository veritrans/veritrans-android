package com.midtrans.demo;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.midtrans.demo.models.SavedCard;
import com.midtrans.demo.models.SavedCards;
import com.midtrans.demo.widgets.DemoButton;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 5/3/17.
 */

public class DemoSavedCardsActivity extends AppCompatActivity implements CardRegistrationCallback {

    private static final String SAVED_CARDS_TYPE = "saved.cards";
    private static final int REQUEST_REGISTER = 123;
    private static final String TAG = DemoSavedCardsActivity.class.getSimpleName();

    private RecyclerView listSavedCard;
    private DemoButton buttonDemo;
    private SavedCardsAdapter cardsAdapter;
    private List<SavedCard> savedCards = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_saved_cards);
        fetchSavedCards();
        bindViews();
        initToolbar();
        initThemes();
        initListView();
        initDemoButton();
        checkSavedCards();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initThemes() {
        String theme = DemoPreferenceHelper.getStringPreference(this, DemoConfigActivity.COLOR_THEME);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back);
        if (theme != null && !TextUtils.isEmpty(theme)) {
            switch (theme) {
                case DemoThemeConstants.BLUE_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonDemo.setBorderColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    buttonDemo.setIconColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    buttonDemo.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    break;
                case DemoThemeConstants.RED_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonDemo.setBorderColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    buttonDemo.setIconColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    buttonDemo.setTextColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonDemo.setBorderColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX));
                    buttonDemo.setIconColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX));
                    buttonDemo.setTextColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX));
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonDemo.setBorderColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX));
                    buttonDemo.setIconColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX));
                    buttonDemo.setTextColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX));
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonDemo.setBorderColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX));
                    buttonDemo.setIconColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX));
                    buttonDemo.setTextColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX));
                    break;
                default:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonDemo.setBorderColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    buttonDemo.setIconColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    buttonDemo.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    break;
            }
        } else {
            drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
            buttonDemo.setBorderColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
            buttonDemo.setIconColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
            buttonDemo.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
        }

        toolbar.setNavigationIcon(drawable);
    }

    private void checkSavedCards() {
        if (savedCards == null || savedCards.isEmpty()) {
            startCardRegisterActivity();
        }
    }

    private void fetchSavedCards() {

        try {
            SavedCards savedCards = LocalDataHandler.readObject(SAVED_CARDS_TYPE, SavedCards.class);
            List<SavedCard> loadedSaveCards = savedCards.savedCards;
            if (loadedSaveCards != null && !loadedSaveCards.isEmpty()) {
                this.savedCards.clear();
                this.savedCards.addAll(loadedSaveCards);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void bindViews() {
        listSavedCard = (RecyclerView) findViewById(R.id.rv_saved_cards);
        buttonDemo = (DemoButton) findViewById(R.id.btn_add_card);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
    }

    private void initListView() {
        listSavedCard.setHasFixedSize(true);
        listSavedCard.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cardsAdapter = new SavedCardsAdapter();
        cardsAdapter.setData(savedCards);

        cardsAdapter.setDeleteCardListener(new SavedCardsAdapter.DeleteCardListener() {
            @Override
            public void onItemDelete(int position) {
                actionOndelete(position);
            }
        });
        listSavedCard.setAdapter(cardsAdapter);
    }

    private void actionOndelete(int position) {
        SavedCard selectedCard = cardsAdapter.getItem(position);
        this.savedCards.remove(selectedCard);
        cardsAdapter.removeCard(selectedCard.getMaskedCard());
    }

    private void initDemoButton() {
        buttonDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCardRegisterActivity();
            }
        });
    }

    private void startCardRegisterActivity() {
        MidtransSDK.getInstance().UiCardRegistration(this, this);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }


    private void updateSavedCards(SavedCard savedCard) {
        savedCards.add(savedCard);
        cardsAdapter.setData(savedCards);

        LocalDataHandler.saveObject(SAVED_CARDS_TYPE, new SavedCards(savedCards));
    }

    @Override
    public void onSuccess(CardRegistrationResponse response) {
        Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show();
        String cardType = Utils.getCardType(response.getMaskedCard());
        SavedCard savedCard = new SavedCard(cardType, response.getSavedTokenId(), response.getTransactionId(), response.getMaskedCard());
        updateSavedCards(savedCard);
    }

    @Override
    public void onFailure(CardRegistrationResponse response, String reason) {
        Logger.d(TAG, "card registration:onFailure()");

    }

    @Override
    public void onError(Throwable error) {
        Logger.d(TAG, "card registration:error()");
    }
}
