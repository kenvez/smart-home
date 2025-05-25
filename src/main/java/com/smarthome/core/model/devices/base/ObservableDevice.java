package com.smarthome.core.model.devices.base;

public interface ObservableDevice {
    void addObserver(DeviceObserver observer);
    void removeObserver(DeviceObserver observer);
    void notifyObservers();
}
