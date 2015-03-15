package pl.vehicle_history.api.method;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

import pl.vehicle_history.api.consts.Credentials;
import pl.vehicle_history.api.exception.VehicleHistoryApiException;
import pl.vehicle_history.api.model.Auth;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dudvar on 2015-03-13.
 */
public class AuthMethod extends Method<Auth> {

    private static final String TAG = AuthMethod.class.getSimpleName();

    public AuthMethod(ResponseListener<Auth> listener) {
        super(listener);
    }

    @Override
    public void makeRequest() {
        apiService.getToken(Credentials.LOGIN, Credentials.PASSWORD, Credentials.CLIENT, "password", new Callback<Auth>() {
            @Override
            public void success(Auth auth, Response response) {
                listener.onSuccess(auth);
            }

            @Override
            public void failure(RetrofitError error) {
                VehicleHistoryApiException exception = (VehicleHistoryApiException) error.getBodyAs(VehicleHistoryApiException.class);
                listener.onError(exception);
            }
        });
    }

    @Override
    protected String prepareAuthorization()     {
        // Prepare authorization data
        String credentialsToken = Credentials.CLIENT + ":" + Credentials.CLIENT_PASSWORD;

        byte[] bytes = new byte[0];
        try {
            bytes = credentialsToken.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException while preparing authorization data");
        }
        String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
        String authToken = "Basic" + " " + base64;
        return authToken;
    }

    private String getCredentials() {
        String credentials = "username=" + Credentials.LOGIN + "&password=" + Credentials.PASSWORD
                + "&cliend_id=" + Credentials.CLIENT + "&grant_type=password";
        return credentials;
    }
}
