package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.*;
import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SmartDevice;


import java.awt.Color;
import java.time.LocalTime;

public class Lightbulb extends SmartDevice {
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

    public void setHue(double hue) throws IllegalArgumentException{
        if (hue < 0.0 || hue >= 360.0)
            throw new IllegalArgumentException("Hue must be in [0, 360): " + hue);

        this.hue = hue;

        notifyObservers();
    }

    public double getSaturation() {
        return this.saturation;
    }

    public void setSaturation(double saturation) throws IllegalArgumentException{
        if (saturation < 0.0 || saturation > 1.0)
            throw new IllegalArgumentException("Saturation must be in [0, 1]: " + saturation);

        this.saturation = saturation;

        notifyObservers();
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) throws IllegalArgumentException{
        if (value < 0.0 || value > 1.0)
            throw new IllegalArgumentException("Value must be in [0, 1]: " + value);

        this.value = value;

        notifyObservers();
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
    public void simulate() {
        if (!isOn()) {
            return;
        }

        double randomFluctuation = (Math.random() - 0.5) * 0.02;
        double newValue = Math.min(1.0, Math.max(0.0, getValue() + randomFluctuation));

        setValue(newValue);

        double hueShift = (Math.random() - 0.5) * 2.0;
        double newHue = (getHue() + hueShift) % 360.0;
        if (newHue < 0) {
            newHue += 360.0;
        }

        setHue(newHue);

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SIMULATION,
                String.format("Light state - Hue: %.1f, Saturation: %.2f, Brightness: %.2f%%",
                        getHue(),
                        getSaturation(),
                        getValue() * 100)
        );


        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();


        if (hour >= 18 && hour < 23) {
            if (getHue() > 45.0) {
                setHue(Math.max(30.0, getHue() - 1.0));
                setSaturation(Math.min(0.15, getSaturation() + 0.01));
            }
        } else if (hour >= 6 && hour < 18) {
            if (getHue() < 180.0) {
                setHue(Math.min(200.0, getHue() + 1.0));
                setSaturation(Math.max(0.05, getSaturation() - 0.01));
            }
        }

        if (hour >= 22 || hour < 6) {
            setValue(Math.min(0.5, getValue()));
        } else if (hour >= 8 && hour < 18) {
            setValue(Math.max(0.7, getValue()));
        }
    }
}
