package io.vehiclehistory.api.model;

import java.io.Serializable;

public class VehicleType implements Serializable {
    private CarType type;
    private CarKind kind;

    public CarType getType() {
        return type;
    }

    public CarKind getKind() {
        return kind;
    }
}
