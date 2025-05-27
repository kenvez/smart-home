package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.EventLogger;
import com.smarthome.core.logging.EventType;
import com.smarthome.core.model.devices.base.*;

import java.util.Random;

public class DoorLock extends SmartDevice implements Switchable {
    private final Random random = new Random();

    private LockStatus lockStatus;
    private String pinCode;
    private boolean autoLockEnabled;
    private boolean isLocked;
    private int batteryLevel;

    public DoorLock(String name, DeviceStatus status) {
        super(name, status);
        this.lockStatus = LockStatus.LOCKED;
        this.pinCode = "1234";
        this.autoLockEnabled = false;
    }

    public LockStatus getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(LockStatus lockStatus) {
        this.lockStatus = lockStatus;
    }

    public boolean lock() {
        if (!isOn()) {
            System.out.println("Cannot lock door, device is off");
            return false;
        }

        setLockStatus(LockStatus.LOCKED);

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Door locked"
        );

        notifyObservers();

        return true;
    }

    public boolean unlock(String enteredPin) {
        if (!isOn()) {
            System.out.println("Cannot unlock door - device is OFF");
            return false;
        }

        if (!enteredPin.equals(pinCode)) {
            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.ERROR,
                    "Invalid PIN attempt"
            );

            return false;
        }

        setLockStatus(LockStatus.UNLOCKED);

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Door unlocked"
        );

        notifyObservers();

        return true;
    }

    public boolean changePinCode(String currentPin, String newPin) {
        if (!isOn()) {
            System.out.println("Cannot change PIN, device is off");
            return false;
        }

        if (!currentPin.equals(pinCode)) {
            return false;
        }

        if (!newPin.matches("\\d{4}")) {
            return false;
        }

        this.pinCode = newPin;

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SETTINGS_CHANGE,
                "PIN code changed"
        );

        notifyObservers();
        return true;
    }

    public void setAutoLock(boolean enabled) {
        this.autoLockEnabled = enabled;

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.SETTINGS_CHANGE,
                "Auto-lock " + (enabled ? "enabled" : "disabled")
        );

        notifyObservers();
    }

    public boolean isAutoLockEnabled() {
        return autoLockEnabled;
    }

    @Override
    public void turnOn() {
        setStatus(DeviceStatus.ON);

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Door lock turned ON"
        );
    }

    @Override
    public void turnOff() {
        setStatus(DeviceStatus.OFF);

        EventLogger.getInstance().logDeviceEvent(
                this,
                getParentRoom(),
                EventType.STATUS_CHANGE,
                "Door lock turned OFF"
        );
    }

    @Override
    public boolean isOn() {
        return getStatus() == DeviceStatus.ON;
    }

    @Override
    public void simulate() {
        boolean previousPowerState = isOn();
        boolean previousAutoLock = autoLockEnabled;
        int previousBattery = batteryLevel;
        LockStatus previousLockStatus = lockStatus;

        if (batteryLevel <= 0) {
            batteryLevel = 100;
        }

        if (!isOn()) {
            turnOn();

            System.out.println("Door lock " + getName() + " turned ON for simulation");
        }

        if (random.nextDouble() < 0.4) {
            if (lockStatus == LockStatus.LOCKED) {
                setLockStatus(LockStatus.UNLOCKED);

                System.out.println("Door lock " + getName() + " changed state from LOCKED to UNLOCKED");

                EventLogger.getInstance().logDeviceEvent(
                        this,
                        getParentRoom(),
                        EventType.SIMULATION,
                        "Simulated unlock with correct PIN"
                );
            } else {
                lock();

                System.out.println("Door lock " + getName() + " changed state from UNLOCKED to LOCKED");
            }
        }

        if (random.nextDouble() < 0.2) {
            setAutoLock(!autoLockEnabled);

            System.out.println("Door lock " + getName() + " auto-lock feature changed from " +
                    (previousAutoLock ? "enabled" : "disabled") + " to " +
                    (autoLockEnabled ? "enabled" : "disabled"));
        }

        int batteryUsage = random.nextInt(2) + 1; // Base usage
        if (previousLockStatus != lockStatus) {
            batteryUsage += 2;
        }

        batteryLevel = Math.max(0, batteryLevel - batteryUsage);

        if (Math.floor(previousBattery / 10.0) > Math.floor(batteryLevel / 10.0)) {
            System.out.println("Door lock " + getName() + " battery level: " + batteryLevel + "%");

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SIMULATION,
                    "Simulated battery drain: " + previousBattery + "% -> " + batteryLevel + "%"
            );
        }

        if (batteryLevel <= 15 && previousBattery > 15) {
            System.out.println("WARNING: Door lock " + getName() + " battery low (" + batteryLevel + "%)");

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.WARNING,
                    "Low battery alert: " + batteryLevel + "%"
            );
        }

        if (batteryLevel <= 5 && isOn()) {
            turnOff();
            System.out.println("Door lock " + getName() + " turned OFF due to critically low battery");

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.ERROR,
                    "Device powered off: Critical battery level (" + batteryLevel + "%)"
            );
        }

        if (random.nextDouble() < 0.05 && isOn()) {
            System.out.println("Door lock " + getName() + " detected unauthorized access attempt");

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.WARNING,
                    "Simulated unauthorized access attempt"
            );
        }

        if (autoLockEnabled && lockStatus == LockStatus.UNLOCKED && random.nextDouble() < 0.7) {
            lock();

            System.out.println("Door lock " + getName() + " auto-locked");
        }
    }
}