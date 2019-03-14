package com.midtrans.sample

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.midtrans.sdk.corekit.base.enums.Environment
import com.midtrans.sdk.uikit.MidtransKit
import com.midtrans.sdk.uikit.MidtransKitConfig
import com.midtrans.sdk.uikit.base.theme.CustomColorTheme
import io.fabric.sdk.android.Fabric

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initMidtransKit()
        initFabric()
    }

    private fun initMidtransKit() {
        // 3DS 1Click key >>> VT-client-F91kdUrnE5w8zCja and set custom field = one_click
        MidtransKit
            .builder(
                this,
                BuildConfig.CLIENT_KEY,
                BuildConfig.BASE_URL
            )
            .setEnvironment(if (BuildConfig.DEBUG) {
                Environment.SANDBOX
            } else {
                Environment.PRODUCTION
            })
            .setApiRequestTimeOut(60)
            .setLogEnabled(true)
            .setBuiltinStorageEnabled(false)
            .setMidtransKitConfig(
                MidtransKitConfig
                    .builder()
                    .setColorTheme(CustomColorTheme("#008577", "#00574B", "#D81B60"))
                    .build()
            )
            .build()
    }

    private fun initFabric() {
        Fabric.with(this, Crashlytics())
    }
}