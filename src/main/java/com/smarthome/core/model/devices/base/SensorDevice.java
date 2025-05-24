package com.smarthome.core.model.devices.base;

public interface SensorDevice<T> {
    T readValue();
    String getUnit();
}