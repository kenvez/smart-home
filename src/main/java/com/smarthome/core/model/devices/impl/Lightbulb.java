package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.*;
import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.base.Switchable;


import java.awt.Color;
import java.util.Random;

public class Lightbulb extends SmartDevice implements Switchable {
    private final Random random = new Random();

    private double hue;
    private double saturation;
    private double value;

    public Lightbulb(String name, DeviceStatus status) {
        super(name, status);
        this.hue = 30.0;
        this.saturation = 0.01;
        this.value = 0.98;
    }

    public double getHue() {
        return this.hue;
    }

    public void setHue(double hue) throws IllegalArgumentException {
        if (hue < 0.0 || hue >= 360.0)
            throw new IllegalArgumentException("Hue must be in [0, 360): " + hue);

        this.hue = hue;

        notifyObservers();

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SETTINGS_CHANGE,
                "Hue set to " + hue + "%"
        );
    }

    public double getSaturation() {
        return this.saturation;
    }

    public void setSaturation(double saturation) throws IllegalArgumentException {
        if (saturation < 0.0 || saturation > 1.0)
            throw new IllegalArgumentException("Saturation must be in [0, 1]: " + saturation);

        this.saturation = saturation;

        notifyObservers();

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SETTINGS_CHANGE,
                "Saturation set to " + saturation + "%"
        );
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) throws IllegalArgumentException {
        if (value < 0.0 || value > 1.0)
            throw new IllegalArgumentException("Value must be in [0, 1]: " + value);

        this.value = value;

        notifyObservers();

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SETTINGS_CHANGE,
                "Value set to " + value * 100 + "%"
        );
    }

    public Color getRGBColor() {
        double h = this.hue;
        double s = this.saturation;
        double v = this.value;

        double c = v * s;
        double x = c * (1 - Math.abs((h / 60) % 2 - 1));
        double m = v - c;

        double rPrime;
        double gPrime;
        double bPrime;

        if (h < 60) {
            rPrime = c;
            gPrime = x;
            bPrime = 0;
        } else if (h < 120) {
            rPrime = x;
            gPrime = c;
            bPrime = 0;
        } else if (h < 180) {
            rPrime = 0;
            gPrime = c;
            bPrime = x;
        } else if (h < 240) {
            rPrime = 0;
            gPrime = x;
            bPrime = c;
        } else if (h < 300) {
            rPrime = x;
            gPrime = 0;
            bPrime = c;
        } else {
            rPrime = c;
            gPrime = 0;
            bPrime = x;
        }

        int r = (int) Math.round((rPrime + m) * 255);
        int g = (int) Math.round((gPrime + m) * 255);
        int b = (int) Math.round((bPrime + m) * 255);

        return new Color(r, g, b);
    }

    @Override
    public void turnOn() {
        setStatus(DeviceStatus.ON);

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Lightbulb turned ON"
        );
    }

    @Override
    public void turnOff() {
        setStatus(DeviceStatus.OFF);

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Lightbulb turned OFF"
        );
    }

    @Override
    public boolean isOn() {
        return getStatus() == DeviceStatus.ON;
    }


    @Override
    public void simulate() {
        double previousHue = hue;
        double previousSaturation = saturation;
        double previousValue = value;

        if (!isOn()) {
            if (random.nextBoolean()) {
                turnOn();

                System.out.println("Light bulb " + getName() + " turned ON");
            } else {
                System.out.println("Light bulb " + getName() + " remains OFF");

                return;
            }
        } else {
            if (random.nextDouble() < 0.3) {
                turnOff();
                System.out.println("Light bulb " + getName() + " turned OFF");

                return;
            }
        }

        if (isOn()) {
            if (random.nextBoolean()) {
                try {
                    double newValue = Math.round(random.nextDouble() * 100) / 100.0;

                    if (Math.abs(newValue - value) > 0.01) {
                        double oldValue = value;
                        setValue(newValue);

                        System.out.println("Light bulb " + getName() + " " +
                                "value changed from " +
                                String.format("%.0f", oldValue * 100) + "% to " +
                                String.format("%.0f", newValue * 100) + "%");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error setting light bulb value: " + e.getMessage());
                }
            }

            if (random.nextDouble() < 0.3) {
                try {
                    double newHue = random.nextDouble() * 359;

                    if (Math.abs(newHue - hue) > 5) {
                        double oldHue = hue;
                        setHue(newHue);

                        System.out.println("Light bulb " + getName() + " color hue changed from " +
                                String.format("%.1f", oldHue) + " to " +
                                String.format("%.1f", newHue));

                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error setting light bulb hue: " + e.getMessage());
                }
            }

            if (random.nextDouble() < 0.2) {
                try {
                    double newSaturation = Math.round(random.nextDouble() * 100) / 100.0;

                    if (Math.abs(newSaturation - saturation) > 0.05) {
                        double oldSaturation = saturation;
                        setSaturation(newSaturation);

                        System.out.println("Light bulb " + getName() + " color saturation changed from " +
                                String.format("%.2f", oldSaturation) + " to " +
                                String.format("%.2f", newSaturation));
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error setting light bulb saturation: " + e.getMessage());
                }
            }

            if (previousHue != hue || previousSaturation != saturation || previousValue != value) {
                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.SIMULATION,
                        String.format(
                                "Simulated color change: HSV(%.1f, %.2f, %.2f) -> HSV(%.1f, %.2f, %.2f)",
                                previousHue, previousSaturation, previousValue,
                                hue, saturation, value
                        )
                );
            }
        }
    }
}
