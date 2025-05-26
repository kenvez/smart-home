package com.smarthome.core.model.devices.impl;

import com.smarthome.core.logging.EventLogger;
import com.smarthome.core.logging.EventType;
import com.smarthome.core.model.devices.base.*;

public class DoorLock extends SmartDevice implements Switchable {
    private LockStatus lockStatus;
    private String pinCode;
    private boolean autoLockEnabled;

    public DoorLock(String name, DeviceStatus status) {
        super(name, status);
        this.lockStatus = LockStatus.LOCKED;
        this.pinCode = "1234";
        this.autoLockEnabled = false;
    }

    public LockStatus getLockState() {
        return lockStatus;
    }

    public boolean isLocked() {
        return lockStatus == LockStatus.LOCKED;
    }

    public boolean lock() {
        if (!isOn()) {
            System.out.println("Cannot lock door, device is off");
            return false;
        }

        lockStatus = LockStatus.LOCKED;

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

        lockStatus = LockStatus.UNLOCKED;

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
        if (!isOn() || !autoLockEnabled || lockStatus == LockStatus.LOCKED) {
            return;
        }

        if (Math.random() < 0.1) {
            lock();
            EventLogger.getInstance().logDeviceEvent(
                    this,
                    getParentRoom(),
                    EventType.SIMULATION,
                    "Door auto-locked"
            );
        }
    }

}
