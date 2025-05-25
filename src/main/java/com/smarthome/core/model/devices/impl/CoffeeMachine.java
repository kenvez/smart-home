package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.*;
import com.smarthome.core.model.devices.base.*;

public class CoffeeMachine extends SmartDevice {
    private CoffeeType coffeeType;
    private CupSize cupSize;
    private int waterLevel;
    private boolean isBrewing;

    public CoffeeMachine(String name, DeviceStatus status) {
        super(name, status);
        this.coffeeType = CoffeeType.ESPRESSO;
        this.cupSize = CupSize.SMALL;
        this.waterLevel = 100;
        this.isBrewing = false;
    }

    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    public void setCoffeeType(CoffeeType coffeeType) {
        this.coffeeType = coffeeType;
        notifyObservers();

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Coffee type changed to " + coffeeType
        );
    }

    public CupSize getCupSize() {
        return cupSize;
    }

    public void setCupSize(CupSize cupSize) {
        this.cupSize = cupSize;
        notifyObservers();

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Cup size changed to " + cupSize
        );
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        if (waterLevel < 0 || waterLevel > 100) {
            throw new IllegalArgumentException("Water level must be between 0 and 100");
        }
        this.waterLevel = waterLevel;
        notifyObservers();
    }

    public boolean isBrewing() {
        return isBrewing;
    }

    public void brewCoffee() {
        if (!isOn()) {
            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.ERROR,
                    "Cannot brew coffee: Machine is turned off"
            );
            return;
        }

        if (isBrewing) {
            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.ERROR,
                    "Cannot brew coffee: Already brewing"
            );
            return;
        }

        if (waterLevel < 20) {
            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.ERROR,
                    "Cannot brew coffee: Not enough water"
            );
            return;
        }

        isBrewing = true;

        int waterUsed = 0;
        switch (cupSize) {
            case SMALL -> waterUsed = 10;
            case MEDIUM -> waterUsed = 20;
            case LARGE -> waterUsed = 30;
        }

        waterLevel = Math.max(0, waterLevel - waterUsed);

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Brewing " + coffeeType + " (" + cupSize + ")"
        );
    }

    public void finishBrewing() {
        if (isBrewing) {
            isBrewing = false;

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.STATUS_CHANGE,
                    coffeeType + " is ready!"
            );
        }
    }

    public void refill() {
        setWaterLevel(100);

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Coffee machine water has been refilled"
        );
    }

    @Override
    public void simulate() {
        if (!isOn()) {
            return;
        }

        if (isBrewing && Math.random() > 0.5) {
            finishBrewing();
        }

        if (Math.random() < 0.1) {
            setWaterLevel(Math.max(0, waterLevel - 1));
        }

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SIMULATION,
                String.format("Coffee Machine - Water: %d%%, Status: %s",
                        waterLevel, isBrewing ? "Brewing" : "Ready")
        );
    }
}
