package com.smarthome.core.model.devices.impl;

import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SensorDevice;
import com.smarthome.core.model.devices.base.SensorStatus;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.logging.EventLogger;
import com.smarthome.core.logging.EventType;

import java.util.Random;

public class TemperatureSensor extends SmartDevice implements SensorDevice<Double>
{
    private Double currentTemperature;
    private SensorStatus sensorStatus;
    private final Random random = new Random();

    public TemperatureSensor(String name, DeviceStatus status) {
        super(name, status);
        this.sensorStatus = SensorStatus.ACTIVE;
        generateRandomTemperature();
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

    @Override
    public void simulate() {
        if (isOn()) {
            double deviation = random.nextDouble() * 2 - 1;
            currentTemperature = Math.min(40, Math.max(-40, currentTemperature + deviation));

            if (random.nextDouble() < 0.05) {
                SensorStatus[] statuses = SensorStatus.values();
                SensorStatus newStatus = statuses[random.nextInt(statuses.length)];
                setSensorStatus(newStatus);
            }

            if (getParentRoom() != null) {
                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.STATUS_CHANGE,
                        "Temperature reading: " + String.format("%.1f", currentTemperature) + "°C"
                );
            }
        }
    }

    private void generateRandomTemperature() {
        this.currentTemperature = random.nextDouble() * 80 - 40; // Range from -40 to 40
    }

    public SensorStatus getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(SensorStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Sensor status cannot be null");
        }

        SensorStatus oldStatus = this.sensorStatus;
        this.sensorStatus = status;

        if (oldStatus != status && getParentRoom() != null) {
            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.STATUS_CHANGE,
                    "Sensor status changed from " + oldStatus + " to " + status
            );
        }
    }

    public Double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setTemperature(Double temperature) throws IllegalArgumentException {
        if (temperature < -40 || temperature > 40) {
            throw new IllegalArgumentException("Temperature must be between -40 and 40 degrees Celsius");
        }
        this.currentTemperature = temperature;
    }

    @Override
    public void setStatus(DeviceStatus status) {
        super.setStatus(status);

        if (status == DeviceStatus.OFF) {
            setSensorStatus(SensorStatus.ACTIVE);
        }
    }
}
