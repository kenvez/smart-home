package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.EventLogger;
import com.smarthome.core.logging.EventType;
import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.base.Switchable;

public class Curtain extends SmartDevice implements Switchable {
    private int opennessPercentage;
    private boolean automatic;

    public Curtain(String name, DeviceStatus status) {
        super(name, status);
        this.opennessPercentage = 0;
        this.automatic = false;
    }

    public int getOpennessPercentage() {
        return this.opennessPercentage;
    }

    public void setOpennessPercentage(int percentage) throws IllegalArgumentException {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Openness percentage must be between 0 and 100: " + percentage);
        }

        if (!isOn()) {
            System.out.println("Cannot adjust curtains while device is OFF");
            return;
        }

        this.opennessPercentage = percentage;

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Curtain openness set to " + percentage + "%"
        );

        notifyObservers();
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;

        // Log the event
        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Curtain automatic mode set to " + (automatic ? "ON" : "OFF")
        );

        notifyObservers();
    }

    public void openFully() {
        if (isOn()) {
            setOpennessPercentage(100);
        }
    }

    public void closeFully() {
        if (isOn()) {
            setOpennessPercentage(0);
        }
    }

    public void toggle() {
        if (!isOn()) {
            return;
        }

        if (opennessPercentage == 100) {
            closeFully();
        } else {
            openFully();
        }
    }

    @Override
    public void turnOn() {
        setStatus(DeviceStatus.ON);

        // Log the event
        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Curtain turned ON"
        );
    }

    @Override
    public void turnOff() {
        setStatus(DeviceStatus.OFF);

        // Log the event
        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Curtain turned OFF"
        );
    }

    @Override
    public boolean isOn() {
        return getStatus() == DeviceStatus.ON;
    }

    @Override
    public void simulate() {
        if (!isOn() || !automatic) {
            return;
        }

        int hour = java.time.LocalTime.now().getHour();

        if (hour >= 7 && hour < 19) {
            if (opennessPercentage < 100) {
                setOpennessPercentage(Math.min(100, opennessPercentage + 10));
            }
        } else {
            if (opennessPercentage > 0) {
                setOpennessPercentage(Math.max(0, opennessPercentage - 10));
            }
        }

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SIMULATION,
                "Curtain automatic adjustment - current openness: " + opennessPercentage + "%"
        );
    }

}
