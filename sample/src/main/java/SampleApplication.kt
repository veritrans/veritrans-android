import android.app.Application
import com.midtrans.sample.BuildConfig
import com.midtrans.sdk.corekit.base.enums.Environment
import com.midtrans.sdk.uikit.MidtransKit

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initMidtransKit()
    }

    private fun initMidtransKit() {
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
            .build()
    }
}