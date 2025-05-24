package com.smarthome.core.model.devices.base;

import com.smarthome.core.logging.EventLogger;
import com.smarthome.core.logging.EventType;
import com.smarthome.core.model.room.Room;
import com.smarthome.core.model.rules.Rule;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public abstract class SmartDevice implements Switchable {
    private final String id;
    private final Set<Rule> rules;
    private String name;
    private DeviceStatus status;
    private Room parentRoom;

    public abstract void simulate();

    public SmartDevice(String name, DeviceStatus status) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.status = status;
        this.rules = new TreeSet<>((ruleOne, ruleTwo) -> ruleOne.getName().compareToIgnoreCase(ruleTwo.getName()));
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

        String oldName = this.name;
        this.name = name;

        EventLogger.getInstance().logDeviceEvent(this, parentRoom, EventType.STATUS_CHANGE, "Device name changed from '" + oldName + "' to '" + name + "'"
        );
    }

    public DeviceStatus getStatus() {
        return this.status;
    }

    public void setStatus(DeviceStatus status) throws IllegalArgumentException {
        if (status == null || status.toString().trim().isEmpty())
            throw new IllegalArgumentException("Device status cannot be null or empty");
        if (status != DeviceStatus.ON && status != DeviceStatus.OFF)
            throw new IllegalArgumentException("Unsupported device status: " + status);

        DeviceStatus oldStatus = this.status;
        this.status = status;

        if (oldStatus != status) {
            String description = "Device status changed from " + oldStatus + " to " + status;

            EventLogger.getInstance().logDeviceEvent(
                    this,
                    parentRoom,
                    EventType.STATUS_CHANGE,
                    description
            );
        }

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

    public void setParentRoom(Room room) {
        this.parentRoom = room;
    }

    public Room getParentRoom() {
        return parentRoom;
    }

    public Set<Rule> getRules() {
        return rules;
    }
}
