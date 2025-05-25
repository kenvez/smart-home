package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.*;
import com.smarthome.core.model.devices.base.*;

public class Outlet extends SmartDevice {
    private boolean inUse;

    public Outlet(String name, DeviceStatus status) {
        super(name, status);
        this.inUse = false;

        if (status == DeviceStatus.ON) {
            setStatus(DeviceStatus.OFF);

            notifyObservers();
        }
    }

    public boolean isInUse() {
        return this.inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;

        notifyObservers();

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
        final double MAX_POWER = 1500.0;
        final double MIN_POWER = 5.0;
        double currentPowerDraw = 0.0;

        if (!isInUse()) {
            if (Math.random() < 0.05) {
                setInUse(true);
                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.STATUS_CHANGE,
                        "Device plugged into outlet"
                );
            }
            return;
        }

        if (isOn()) {
            double previousPower = currentPowerDraw;

            currentPowerDraw = MIN_POWER + (Math.random() * 200);

            if (Math.abs(currentPowerDraw - previousPower) > 50) {
                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.SIMULATION,
                        String.format("Power draw changed: %.1fW → %.1fW",
                                previousPower, currentPowerDraw)
                );
            }

            if (currentPowerDraw >= MAX_POWER * 0.95) {
                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.WARNING,
                        String.format("High power draw warning: %.1fW", currentPowerDraw)
                );

                if (Math.random() < 0.1) {
                    turnOff();
                    setInUse(false);

                    EventLogger.getInstance().logDeviceEvent(
                            this,
                            getParentRoom(),
                            EventType.ERROR,
                            "Overload protection triggered - outlet disabled"
                    );
                    return;
                }
            }

            if (Math.random() < 0.02) {
                setInUse(false);

                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.STATUS_CHANGE,
                        "Device unplugged from outlet"
                );
            }
        } else if (isInUse()) {
            currentPowerDraw = MIN_POWER;

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SIMULATION,
                    String.format("Standby power consumption: %.1fW", currentPowerDraw)
            );
        }
    }
}
