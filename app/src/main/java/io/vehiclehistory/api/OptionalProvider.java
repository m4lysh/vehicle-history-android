package io.vehiclehistory.api;

import android.content.Context;
import android.content.res.Resources;

import io.vehiclehistory.DateFormatter;
import io.vehiclehistory.R;
import io.vehiclehistory.api.model.CarKind;
import io.vehiclehistory.api.model.CarMake;
import io.vehiclehistory.api.model.CarType;
import io.vehiclehistory.api.model.Engine;
import io.vehiclehistory.api.model.FuelType;
import io.vehiclehistory.api.model.Inspection;
import io.vehiclehistory.api.model.InspectionStatus;
import io.vehiclehistory.api.model.Mileage;
import io.vehiclehistory.api.model.Name;
import io.vehiclehistory.api.model.Plate;
import io.vehiclehistory.api.model.Policy;
import io.vehiclehistory.api.model.PolicyStatus;
import io.vehiclehistory.api.model.Production;
import io.vehiclehistory.api.model.Registration;
import io.vehiclehistory.api.model.RegistrationStatus;
import io.vehiclehistory.api.model.Vehicle;
import io.vehiclehistory.api.model.VehicleType;

/**
 * Created by m4lysh on 2015-04-06.
 */
public class OptionalProvider {

    private Resources resources;

    public OptionalProvider(Context context) {
        this.resources = context.getResources();
    }

    public String getFirstVehicleRegistration(Vehicle vehicle) {
        Registration registration = vehicle.getRegistration();
        if (registration == null) {
            return resources.getString(R.string.unknown_first_vehicle_registration);
        }

        String firstRegistration = registration.getFirstAt();

        return firstRegistration != null
                ? new DateFormatter().formatDateFromApi(firstRegistration)
                : resources.getString(R.string.unknown_first_vehicle_registration);
    }

    public String getRegistrationStatus(Vehicle vehicle) {
        Registration registration = vehicle.getRegistration();
        if (registration == null) {
            return resources.getString(R.string.unknown_registration_status);
        }

        RegistrationStatus status = registration.getStatus();

        return status != null
                ? status.toString()
                : resources.getString(R.string.unknown_registration_status);
    }

    public String getModel(Vehicle vehicle) {
        Name name = vehicle.getName();
        if (name == null) {
            return resources.getString(R.string.unknown_model);
        }

        String model = name.getModel();

        return model != null
                ? model : resources.getString(R.string.unknown_model);
    }

    public String getMake(Vehicle vehicle) {
        Name name = vehicle.getName();
        if (name == null) {
            return resources.getString(R.string.unknown_make);
        }

        CarMake make = name.getMake();

        return make != null
                ? make.toString() : resources.getString(R.string.unknown_make);
    }

    public String getCubicCapacity(Vehicle vehicle) {
        Engine engine = vehicle.getEngine();
        if (engine == null) {
            return resources.getString(R.string.unknown_cubic_capacity);
        }

        String engineCc = engine.getCubicCapacity();

        return engineCc != null
                ? engineCc : resources.getString(R.string.unknown_cubic_capacity);
    }

    public String getFuelType(Vehicle vehicle) {
        Engine engine = vehicle.getEngine();
        if (engine == null) {
            return resources.getString(R.string.unknown_fuel_type);
        }

        FuelType fuelType = engine.getFuel();

        return fuelType != null
                ? fuelType.toString() : resources.getString(R.string.unknown_fuel_type);
    }

    public String getMileage(Vehicle vehicle) {
        Mileage mileage = vehicle.getMileage();
        if (mileage == null) {
            return resources.getString(R.string.unknown_mileage);
        }

        String fuelType = mileage.getValue();

        return fuelType != null
                ? fuelType : resources.getString(R.string.unknown_mileage);
    }

    public String hasInsurance(Vehicle vehicle) {
        Policy policy = vehicle.getPolicy();
        if (policy == null) {
            return resources.getString(R.string.unknown_policy_status);
        }

        PolicyStatus policyStatus = policy.getStatus();

        return policyStatus != null
                ? policyStatus.toString() : resources.getString(R.string.unknown_policy_status);
    }

    public String getInspectionStatus(Vehicle vehicle) {
        Inspection inspection = vehicle.getInspection();
        if (inspection == null) {
            return resources.getString(R.string.unknown_inspection_status);
        }

        InspectionStatus inspectionStatus = inspection.getStatus();

        return inspectionStatus != null
                ? inspectionStatus.toString() : resources.getString(R.string.unknown_inspection_status);
    }

    public String getVehicleType(Vehicle vehicle) {
        VehicleType type = vehicle.getType();
        if (type == null) {
            return resources.getString(R.string.unknown_vehicle_type);
        }

        CarType vehicleType = type.getType();

        return vehicleType != null
                ? vehicleType.toString() : resources.getString(R.string.unknown_vehicle_type);
    }

    public String getVehicleKind(Vehicle vehicle) {
        VehicleType type = vehicle.getType();
        if (type == null) {
            return resources.getString(R.string.unknown_vehicle_kind);
        }

        CarKind carKind = type.getKind();

        return carKind != null
                ? carKind.toString() : resources.getString(R.string.unknown_vehicle_kind);
    }

    public String getProductionYear(Vehicle vehicle) {
        Production production = vehicle.getProduction();
        if (production == null) {
            return resources.getString(R.string.unknown_production_year);
        }

        String year = production.getYear();

        return year != null
                ? year : resources.getString(R.string.unknown_production_year);
    }

    public String getPlate(Vehicle vehicle) {
        Plate plate = vehicle.getPlate();
        if (plate == null) {
            return resources.getString(R.string.unknown_plate);
        }

        String plateValue = plate.getValue();

        return plateValue != null ? plateValue : resources.getString(R.string.unknown_plate);
    }

    public String getVin(Vehicle vehicle) {
        String vin = vehicle.getVin();

        return vin != null ? vin : resources.getString(R.string.unknown_vin);
    }
}
