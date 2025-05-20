package com.smarthome.core.model.devices.base;

import java.util.UUID;

public abstract class SmartDevice implements Switchable {
    private final String id;
    private String name;
    private DeviceStatus status;

    public abstract void simulate();

    public SmartDevice(String name, DeviceStatus status) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Device name cannot be null or empty");
        }

        this.name = name;
    }

    public DeviceStatus getStatus() {
        return this.status;
    }

    public void setStatus(DeviceStatus status) throws IllegalArgumentException {
        if (status == null || status.toString().trim().isEmpty())
            throw new IllegalArgumentException("Device status cannot be null or empty");
        if (status != DeviceStatus.ON && status != DeviceStatus.OFF)
            throw new IllegalArgumentException("Unsupported device status: " + status);

        this.status = status;
    }

    @Override
    public void turnOn() {
        setStatus(DeviceStatus.ON);
    }

    @Override
    public void turnOff() {
        setStatus(DeviceStatus.OFF);
    }

    @Override
    public boolean isOn() {
        return this.status == DeviceStatus.ON;
    }
}
