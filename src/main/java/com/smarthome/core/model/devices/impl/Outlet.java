package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.*;
import com.smarthome.core.model.devices.base.*;

import java.util.Random;

public class Outlet extends SmartDevice implements Switchable {
    private final Random random = new Random();

    private double powerConsumption;
    private boolean devicePluggedIn;
    private boolean inUse;

    public Outlet(String name, DeviceStatus status) {
        super(name, status);
        this.inUse = false;

        if (status == DeviceStatus.ON) {
            setStatus(DeviceStatus.OFF);

            notifyObservers();

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.STATUS_CHANGE,
                    "Outlet turned OFF"
            );
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

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SETTINGS_CHANGE,
                "In use changed to " + (inUse ? "ON" : "OFF")
        );
    }

    @Override
    public void turnOn() {
        if (inUse) {
            super.turnOn();

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.STATUS_CHANGE,
                    "Outlet turned ON"
            );
        } else {
            System.out.println("\nCannot turn on the outlet, because it is " +
                    "not in use.");

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.WARNING,
                    "Failed to turn on outlet, not in use"
            );
        }
    }


    @Override
    public void turnOff() {
        super.turnOff();

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Outlet turned OFF"
        );
    }

    @Override
    public boolean isOn() {
        return getStatus() == DeviceStatus.ON;
    }


    @Override
    public void simulate() {
        double previousPower = powerConsumption;

        if (!isInUse()) {
            setInUse(true);

            System.out.println("Outlet " + getName() + " set to in-use for simulation");
        }

        if (!isOn()) {
            turnOn();

            System.out.println("Outlet " + getName() + " turned ON for simulation");
        }

        if (random.nextDouble() < 0.2) {
            devicePluggedIn = !devicePluggedIn;
            System.out.println("Outlet " + getName() + " device plugged in: " + (devicePluggedIn ? "YES" : "NO"));

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SIMULATION,
                    "Simulated device " + (devicePluggedIn ? "plugged in" : "unplugged")
            );
        }

        if (isOn() && devicePluggedIn) {
            double newPowerConsumption = random.nextDouble() * 1500;
            newPowerConsumption = Math.round(newPowerConsumption * 10) / 10.0; // Round to 1 decimal

            powerConsumption = newPowerConsumption;

            System.out.println("Outlet " + getName() + " power consumption changed from " +
                    previousPower + " watts to " + powerConsumption + " watts");

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SIMULATION,
                    "Simulated power consumption: " + previousPower + " watts -> " + powerConsumption + " watts"
            );
        } else if (previousPower > 0) {
            powerConsumption = 0;

            System.out.println("Outlet " + getName() + " power consumption changed from " +
                    previousPower + " watts to 0 watts");

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SIMULATION,
                    "Simulated power consumption: " + previousPower + " watts -> 0 watts (device " +
                            (!isOn() ? "turned off" : "unplugged") + ")"
            );
        }

        if (random.nextDouble() < 0.4) {
            boolean currentState = isOn();

            if (currentState) {
                turnOff();
            } else {
                turnOn();
            }

            System.out.println("Outlet " + getName() + " power state changed to: " + (isOn() ? "ON" : "OFF"));
        }
    }
}
