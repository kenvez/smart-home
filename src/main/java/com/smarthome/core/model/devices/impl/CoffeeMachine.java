package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.*;
import com.smarthome.core.model.devices.base.*;

import java.util.Random;

public class CoffeeMachine extends SmartDevice {
    private final Random random = new Random();

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

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SETTINGS_CHANGE,
                "Water level set to " + waterLevel + "%"
        );
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
            turnOn();

            System.out.println("Coffee machine " + getName() + " turned ON for simulation");
        }

        int previousWaterLevel = waterLevel;
        CoffeeType previousCoffeeType = coffeeType;
        CupSize previousCupSize = cupSize;

        if (random.nextDouble() < 0.3) {
            CoffeeType[] types = CoffeeType.values();
            CoffeeType newType = types[random.nextInt(types.length)];

            if (newType != coffeeType) {
                coffeeType = newType;
                System.out.println("Coffee machine " + getName() + " coffee type changed from " +
                        previousCoffeeType + " to " + coffeeType);

                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.SIMULATION,
                        "Simulated coffee type change: " + previousCoffeeType + " -> " + coffeeType
                );
            }
        }

        if (random.nextDouble() < 0.3) {
            CupSize[] sizes = CupSize.values();
            CupSize newSize = sizes[random.nextInt(sizes.length)];

            if (newSize != cupSize) {
                cupSize = newSize;
                System.out.println("Coffee machine " + getName() + " cup size changed from " +
                        previousCupSize + " to " + cupSize);

                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.SIMULATION,
                        "Simulated cup size change: " + previousCupSize + " -> " + cupSize
                );
            }
        }

        if (!isBrewing && random.nextDouble() < 0.4) {
            if (waterLevel >= 20) {
                int waterUsed = 0;

                switch (cupSize) {
                    case SMALL -> waterUsed = 10;
                    case MEDIUM -> waterUsed = 20;
                    case LARGE -> waterUsed = 30;
                }

                int newWaterLevel = Math.max(0, waterLevel - waterUsed);

                if (newWaterLevel != waterLevel) {
                    waterLevel = newWaterLevel;
                    isBrewing = true;

                    System.out.println("Coffee machine " + getName() + " started brewing " +
                            coffeeType + " (" + cupSize + ")");
                    System.out.println("Water level decreased from " + previousWaterLevel +
                            "% to " + waterLevel + "%");

                    EventLogger.getInstance().logDeviceEvent(
                            this,
                            getParentRoom(),
                            EventType.SIMULATION,
                            "Simulated brewing: " + coffeeType + " (" + cupSize + "), water level: " +
                                    previousWaterLevel + "% -> " + waterLevel + "%"
                    );
                }
            } else {
                System.out.println("Coffee machine " + getName() + " cannot brew: Not enough water");

                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.SIMULATION,
                        "Simulated brewing attempt failed: Not enough water (level: " + waterLevel + "%)"
                );
            }
        }
        else if (isBrewing && random.nextDouble() < 0.7) {
            isBrewing = false;
            System.out.println("Coffee machine " + getName() + " finished brewing");

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SIMULATION,
                    "Simulated brewing finished: " + coffeeType + " (" + cupSize + ")"
            );
        }

        if (waterLevel < 50 && random.nextDouble() < 0.2) {
            int oldLevel = waterLevel;
            waterLevel = 100;

            System.out.println("Coffee machine " + getName() + " water refilled from " +
                    oldLevel + "% to 100%");

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SIMULATION,
                    "Simulated water refill: " + oldLevel + "% -> 100%"
            );
        }

        if (waterLevel > 0 && random.nextDouble() < 0.1) {
            int waterLoss = random.nextInt(5) + 1;
            int newLevel = Math.max(0, waterLevel - waterLoss);

            if (newLevel != waterLevel) {
                int oldLevel = waterLevel;
                waterLevel = newLevel;

                System.out.println("Coffee machine " + getName() + " water level decreased from " +
                        oldLevel + "% to " + waterLevel + "% due to evaporation");

                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.SIMULATION,
                        "Simulated water evaporation: " + oldLevel + "% -> " + waterLevel + "%"
                );
            }
        }

        if (random.nextDouble() < 0.2) {
            if (isOn()) {
                turnOff();

                System.out.println("Coffee machine " + getName() + " turned OFF");
            } else {
                turnOn();

                System.out.println("Coffee machine " + getName() + " turned ON");
            }
        }
    }
}
