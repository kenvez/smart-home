package com.smarthome.core.model.devices.impl;

import com.smarthome.core.model.devices.base.*;
import com.smarthome.core.logging.*;

import java.util.Random;

public class TemperatureSensor extends SmartDevice implements SensorDevice<Double> {
    private SensorStatus sensorStatus;
    private double currentTemperature;
    private static final Random random = new Random();
    private double temperatureThreshold = 25.0;
    private int updateInterval = 60;

    public TemperatureSensor(String name, DeviceStatus status) {
        super(name, status);
        this.sensorStatus = SensorStatus.ACTIVE;
        this.currentTemperature = random.nextDouble() * 80 - 40;
    }

    @Override
    public Double readValue() {
        if (isOn() && sensorStatus == SensorStatus.ACTIVE) {
            return currentTemperature;
        } else {
            return null;
        }
    }

    @Override
    public String getUnit() {
        return "°C";
    }

    public SensorStatus getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(SensorStatus status) throws IllegalArgumentException {
        if (status == null) {
            throw new IllegalArgumentException("Sensor status cannot be null");
        }

        SensorStatus oldStatus = this.sensorStatus;

        this.sensorStatus = status;

        notifyObservers();

        if (oldStatus != status && getParentRoom() != null) {
            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.STATUS_CHANGE,
                    "Sensor status changed from " + oldStatus + " to " + status
            );
        }
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public double getTemperatureThreshold() {
        return temperatureThreshold;
    }

    public void setTemperatureThreshold(double threshold) throws IllegalArgumentException {
        if (threshold < -40 || threshold > 40) {
            throw new IllegalArgumentException("Temperature threshold must be between -40 and 40 degrees Celsius");
        }

        double oldThreshold = this.temperatureThreshold;
        this.temperatureThreshold = threshold;

        notifyObservers();

        if (oldThreshold != threshold && getParentRoom() != null) {
            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SETTINGS_CHANGE,
                    "Temperature threshold changed from " + oldThreshold + "°C to " + threshold + "°C"
            );
        }
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int interval) throws IllegalArgumentException {
        if (interval < 1) {
            throw new IllegalArgumentException("Update interval must be at least 1 second");
        }

        int oldInterval = this.updateInterval;
        this.updateInterval = interval;

        notifyObservers();

        if (oldInterval != interval && getParentRoom() != null) {
            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SETTINGS_CHANGE,
                    "Update interval changed from " + oldInterval + " seconds to " + interval + " seconds"
            );
        }
    }

    @Override
    public void setStatus(DeviceStatus status) {
        super.setStatus(status);

        if (status == DeviceStatus.OFF) {
            setSensorStatus(SensorStatus.ACTIVE);
        }
    }

    @Override
    public void simulate() {
        if (!isOn()) {
            turnOn();

            System.out.println("Temperature sensor " + getName() + " turned ON");

            EventLogger.getInstance().logDeviceEvent(
                             this,
                             getParentRoom(),
                             EventType.STATUS_CHANGE,
                             "Temperature sensor " + getName() + " turned ON during simulation"
                     );
        }

        double previousTemperature = currentTemperature;

        double change = (random.nextDouble() * 4.0) - 2.0;
        currentTemperature += change;

        double minTemperature = -10.0;
        double maxTemperature = 40.0;

        if (currentTemperature < minTemperature) {
            currentTemperature = minTemperature;
        } else if (currentTemperature > maxTemperature) {
            currentTemperature = maxTemperature;
        }

        currentTemperature = Math.round(currentTemperature * 10.0) / 10.0;

        System.out.println("Temperature sensor " + getName() + " reading changed from "
                + previousTemperature + "°C to " + currentTemperature + "°C");

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SIMULATION,
                "Simulated temperature: " + previousTemperature + "°C -> " + currentTemperature + "°C"
        );
    }
}
