package com.smarthome.core.management;

import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.observers.DeviceLogger;
import com.smarthome.core.model.devices.observers.DeviceMonitor;
import com.smarthome.core.model.devices.observers.SecurityObserver;

public class DeviceManagement {
    private static DeviceManagement instance;
    private final DeviceMonitor monitor;
    private final DeviceLogger logger;
    private final SecurityObserver security;

    private DeviceManagement() {
        monitor = new DeviceMonitor();
        logger = new DeviceLogger();
        security = new SecurityObserver();
    }

    public static DeviceManagement getInstance() {
        if (instance == null) {
            instance = new DeviceManagement();
        }

        return instance;
    }

    public void registerDevice(SmartDevice device) {
        device.addObserver(monitor);
        device.addObserver(logger);
        device.addObserver(security);
    }

    public void unregisterDevice(SmartDevice device) {
        device.removeObserver(monitor);
        device.removeObserver(logger);
        device.removeObserver(security);
    }
}
