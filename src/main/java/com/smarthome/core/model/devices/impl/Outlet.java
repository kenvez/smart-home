package com.smarthome.core.model.devices.impl;

import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SmartDevice;

public class Outlet extends SmartDevice {
    private final boolean inUse;

    public Outlet(String name, DeviceStatus status) {
        super(name, status);
        this.inUse = false;
    }

    public boolean isInUse() {
        return this.inUse;
    }

    @Override
    public void simulate() {
        System.out.println("Outlet simulation!");
    }
}
