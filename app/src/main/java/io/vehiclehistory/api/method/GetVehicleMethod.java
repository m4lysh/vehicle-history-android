package io.vehiclehistory.api.method;

import android.content.Context;

import io.vehiclehistory.R;
import io.vehiclehistory.api.consts.Settings;
import io.vehiclehistory.api.exception.VehicleHistoryApiException;
import io.vehiclehistory.api.model.VehicleInput;
import io.vehiclehistory.api.model.VehicleResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GetVehicleMethod extends Method<VehicleResponse> {

    private static final String BEARER_PREFIX = "Bearer ";

    private VehicleInput input;
    private String token;

    public GetVehicleMethod(VehicleInput input, String token, ResponseListener<VehicleResponse> listener, Context context) {
        super(listener, context);
        this.input = input;
        this.token = token;
    }

    @Override
    protected String getEndpoint() {
        return Settings.API_HOST;
    }

    @Override
    public void makeRequest() {
        apiService.getVehicle(input.getPlate(), input.getVin(), input.getFirstRegistrationDate(),
                new Callback<VehicleResponse>() {

            @Override
            public void success(VehicleResponse vehicleResponse, Response response) {
                listener.onSuccess(vehicleResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                switch (error.getKind()) {
                    case NETWORK:
                    case CONVERSION: {
                        listener.onConnectionError(context.getString(R.string.connection_error));
                        break;
                    }
                    case HTTP: {
                        VehicleHistoryApiException exception =
                                (VehicleHistoryApiException) error.getBodyAs(VehicleHistoryApiException.class);
                        listener.onApiError(exception);
                        break;
                    }

                }
            }
        });
    }

    @Override
    protected String prepareAuthorization() {
        return BEARER_PREFIX + token;
    }
}
