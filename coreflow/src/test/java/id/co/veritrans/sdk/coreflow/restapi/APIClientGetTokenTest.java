package id.co.veritrans.sdk.coreflow.restapi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import id.co.veritrans.sdk.coreflow.models.AuthModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ziahaqi on 25/06/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class APIClientGetTokenTest extends APIClientMainTest {
    private AuthModel mAuthModel;
    private String authtoken = "b9a10bcb99a1cede39d1f4f951ab2e16";

    @Override
    protected RetrofitMockClient initClientParams() throws Exception {
        return RestAPIMocUtilites.getClient(this.getClass().getClassLoader(),
                200, "authorized", "merchant_auth_ok.json");
    }


}
