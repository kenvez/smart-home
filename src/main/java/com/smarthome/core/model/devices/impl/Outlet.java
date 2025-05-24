package com.smarthome.core.model.devices.impl;

import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SmartDevice;

public class Outlet extends SmartDevice {
    private boolean inUse;

    public Outlet(String name, DeviceStatus status) {
        super(name, status);
        this.inUse = false;

        if (status == DeviceStatus.ON) {
            setStatus(DeviceStatus.OFF);
        }
    }

    public boolean isInUse() {
        return this.inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;

        if (!inUse && isOn()) {
            turnOff();
        }
    }

    @Override
    public void turnOn() {
        if (inUse) {
            super.turnOn();
        } else {
            System.out.println("Cannot turn on the outlet, because it is not in use.");
        }
    }

    @Override
    public void simulate() {
        System.out.println("Outlet simulation!");
    }
}
