package com.smarthome.core.model.devices.base;

public interface Switchable {
    void turnOn();
    void turnOff();
    boolean isOn();
}
