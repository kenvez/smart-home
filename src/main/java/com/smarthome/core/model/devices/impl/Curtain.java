package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.EventLogger;
import com.smarthome.core.logging.EventType;
import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.base.Switchable;

import java.util.Random;

public class Curtain extends SmartDevice implements Switchable {
    private final Random random = new Random();

    private int opennessPercentage;
    private boolean automatic;

    public Curtain(String name, DeviceStatus status) {
        super(name, status);
        this.opennessPercentage = 0;
        this.automatic = false;
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

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SETTINGS_CHANGE,
                "Curtain opened fully, current openness: " + opennessPercentage + "%"
        );
    }

    public void closeFully() {
        if (isOn()) {
            setOpennessPercentage(0);
        }

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SETTINGS_CHANGE,
                "Curtain closed fully, current openness: " + opennessPercentage + "%"
        );
    }

    @Override
    public void turnOn() {
        setStatus(DeviceStatus.ON);

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
        boolean previousPowerState = isOn();
        int previousOpennessPercentage = opennessPercentage;
        boolean previousAutomatic = automatic;

        if (!isOn()) {
            if (random.nextDouble() < 0.3) {
                turnOn();

                System.out.println("Curtain " + getName() + " turned ON");
            } else {
                System.out.println("Curtain " + getName() + " remains OFF");

                return;
            }
        }

        if (random.nextDouble() < 0.2) {
            boolean newAutomatic = !automatic;
            setAutomatic(newAutomatic);
            System.out.println("Curtain " + getName() + " automatic mode changed from " +
                    (previousAutomatic ? "ON" : "OFF") + " to " +
                    (newAutomatic ? "ON" : "OFF"));
        }

        if (isOn()) {
            int action = random.nextInt(3);
            int newOpennessPercentage;

            switch (action) {
                case 0 -> {
                    newOpennessPercentage = opennessPercentage + random.nextInt(30) + 10;
                    newOpennessPercentage = Math.min(100, newOpennessPercentage);
                }
                case 1 -> {
                    newOpennessPercentage = opennessPercentage - random.nextInt(30) - 10;
                    newOpennessPercentage = Math.max(0, newOpennessPercentage);
                }
                case 2 -> newOpennessPercentage = random.nextInt(101);
                default -> newOpennessPercentage = opennessPercentage;
            }

            if (newOpennessPercentage != previousOpennessPercentage) {
                opennessPercentage = newOpennessPercentage;

                System.out.println("Curtain " + getName() + " position changed from " +
                        previousOpennessPercentage + "% open to " + opennessPercentage + "% open");

                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.SIMULATION,
                        "Simulated openness change: " + previousOpennessPercentage + "% -> " + opennessPercentage + "%"
                );

                notifyObservers();
            }
        }

        if (isOn() && !previousPowerState && random.nextDouble() < 0.1) {
            turnOff();

            System.out.println("Curtain " + getName() + " turned OFF at the end of simulation");
        }
    }
}
