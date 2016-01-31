package io.vehiclehistory.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.util.Collection;

import javax.inject.Inject;

import io.vehiclehistory.R;
import io.vehiclehistory.SaveSearchDelegate;
import io.vehiclehistory.VehicleHistoryApp;
import io.vehiclehistory.activity.MainActivity;
import io.vehiclehistory.activity.VehicleDataActivity;
import io.vehiclehistory.api.model.VehicleInput;
import io.vehiclehistory.api.model.VehicleResponse;
import io.vehiclehistory.data.api.caller.GetVehicleHistoryCaller;
import io.vehiclehistory.data.api.view.VehicleHistoryMvpView;
import io.vehiclehistory.injection.component.ApplicationComponent;
import io.vehiclehistory.validation.Issue;
import io.vehiclehistory.validation.VehicleInputValidator;
import io.vehiclehistory.validation.VehicleValidationException;

public class FindVehicleFragment extends Fragment implements VehicleHistoryMvpView {

    public static final int PICK_DATE_REQ_CODE = 101;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final int ANIMATOR_BUTTON = 0;
    private static final int ANIMATOR_PROGRESS = 1;

    private static final String DATE_PICKER_TAG = "datePickerTag";

    private final Handler handler = new Handler();

    private Button findVehicleButton;
    private ViewAnimator findVehicleAnimator;

    private TextInputLayout plateInputLayout;
    private TextInputLayout vinInputLayout;
    private TextInputLayout registrationDateInputLayout;

    private View pickDateButton;

    @Inject
    protected GetVehicleHistoryCaller getVehicleHistoryCaller;

    private ApplicationComponent component;

    public ApplicationComponent component() {

        if (component == null) {
            component = VehicleHistoryApp.get(getActivity()).component();
        }

        return component;
    }


    public static FindVehicleFragment newInstance(int sectionNumber) {
        FindVehicleFragment fragment = new FindVehicleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FindVehicleFragment() {
        //nop
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_vehicle, container, false);

        component().inject(this);
        bindViews(rootView);
        setupPickDateButton();
        setupSearchButton();

        return rootView;
    }

    private void bindViews(View rootView) {
        getVehicleHistoryCaller.attachView(this);
        findVehicleButton = (Button) rootView.findViewById(R.id.find_vehicle_button);
        findVehicleAnimator = (ViewAnimator) rootView.findViewById(R.id.find_vehicle_animator);

        plateInputLayout = (TextInputLayout) rootView.findViewById(R.id.plate_edit_text);
        vinInputLayout = (TextInputLayout) rootView.findViewById(R.id.vin_edit_text);
        registrationDateInputLayout = (TextInputLayout) rootView.findViewById(R.id.registration_edit_text);

        pickDateButton = rootView.findViewById(R.id.pick_date_button);
    }

    private void setupPickDateButton() {
        pickDateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                registrationDateInputLayout.setError(null);

                FragmentManager fm = getActivity().getFragmentManager();
                DatePickerFragment datePickerFragment = new DatePickerFragment();

                datePickerFragment.setTargetFragment(FindVehicleFragment.this, PICK_DATE_REQ_CODE);

                datePickerFragment.show(fm, DATE_PICKER_TAG);
            }
        });
    }

    private void setupSearchButton() {
        findVehicleButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clearErrors();
                validateAndPerformSearch();
            }

        });
    }

    private void clearErrors() {
        plateInputLayout.setError(null);
        vinInputLayout.setError(null);
        registrationDateInputLayout.setError(null);
    }

    private void validateAndPerformSearch() {
        try {
            getVehicleHistoryCaller.getVehicleHistory(getValidatedInput());
        } catch (VehicleValidationException e) {
            handleValidationIssues(e.getIssues());
        }
    }

    private void handleValidationIssues(Collection<Issue> issues) {
        for (Issue issue : issues) {
            switch (issue.getField()) {
                case PLATE:
                    plateInputLayout.setError(issue.getDetailMessage());
                    break;
                case VIN:
                    vinInputLayout.setError(issue.getDetailMessage());
                    break;
                case FIRST_REGISTRATION_DATE:
                    registrationDateInputLayout.setError(issue.getDetailMessage());
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_DATE_REQ_CODE && resultCode == Activity.RESULT_OK) {
            String date = data.getStringExtra(DatePickerFragment.EXTRA_KEY_DATE_STRING);
            registrationDateInputLayout.getEditText().setText(date);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onExceptionUi(String message) {
        Context context = getActivity();

        if (context != null) {
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            setButtonAnimator(ANIMATOR_BUTTON);
            setUiLocked(false);
        }
    }

    private VehicleInput getValidatedInput() throws VehicleValidationException {
        VehicleInput input = new VehicleInput();

        String plate = plateInputLayout.getEditText().getText().toString();
        String vin = vinInputLayout.getEditText().getText().toString();
        String firstRegDate = registrationDateInputLayout.getEditText().getText().toString();

        VehicleInputValidator validator = new VehicleInputValidator(getActivity());
        validator.validate(plate, vin, firstRegDate);

        input.setPlate(plate);
        input.setVin(vin);
        input.setFirstRegistrationDate(firstRegDate);

        return input;
    }

    private void setUiLocked(final boolean locked) {
        this.handler.post(new Runnable() {

            @Override
            public void run() {
                plateInputLayout.setEnabled(!locked);
                vinInputLayout.setEnabled(!locked);
                registrationDateInputLayout.setEnabled(!locked);
            }
        });
    }

    private void setButtonAnimator(final int childPosition) {
        this.handler.post(new Runnable() {

            @Override
            public void run() {
                findVehicleAnimator.setDisplayedChild(childPosition);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onGetVehicleHistoryFinished(VehicleInput request, VehicleResponse response) {
        new SaveSearchDelegate(getActivity().getApplicationContext()).saveSearch(request, response);
        Intent i = new Intent(getActivity(), VehicleDataActivity.class);
        i.putExtra(VehicleDataActivity.EXTRA_VEHICLE_RESPONSE_KEY, response);

        getActivity().startActivity(i);
    }

    @Override
    public void onErrorResponse(String message) {
        Context applicationContext = getActivity().getApplicationContext();

        if (applicationContext != null) {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.connection_error);
            }
            onExceptionUi(message);
        }
    }

    @Override
    public void onNoConnectionError() {
        onErrorResponse(getString(R.string.connection_error));
    }

    @Override
    public void onRetryError() {
        onErrorResponse(getString(R.string.connection_error));
    }

    @Override
    public void unableToGetTokenError() {
        onErrorResponse(getString(R.string.connection_error));
    }

    @Override
    public void startedLoadingData() {
        setUiLocked(true);
        setButtonAnimator(ANIMATOR_PROGRESS);
    }

    @Override
    public void finishedLoadingData() {
        setButtonAnimator(ANIMATOR_BUTTON);
        setUiLocked(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getVehicleHistoryCaller.detachView();
    }

}
