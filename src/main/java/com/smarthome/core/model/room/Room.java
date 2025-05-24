package com.smarthome.core.model.room;

import com.smarthome.core.logging.EventLogger;
import com.smarthome.core.logging.EventType;
import com.smarthome.core.model.devices.base.SmartDevice;

import java.util.TreeSet;
import java.util.Set;

public class Room {
    private String name;
    private final RoomType type;
    private final Set<SmartDevice> devices;

    public Room(String name, RoomType type) {
        this.name = name;
        this.type = type;
        this.devices = new TreeSet<>((SmartDevice smartDeviceOne, SmartDevice smartDeviceTwo) -> smartDeviceOne.getName().compareToIgnoreCase(smartDeviceTwo.getName()));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomType getType() {
        return this.type;
    }

    public void addDevice(SmartDevice device) {
        this.devices.add(device);

        device.setParentRoom(this);

        EventLogger.getInstance().logDeviceEvent(
                device,
                this,
                EventType.DEVICE_ADDED,
                "Device '" + device.getName() + "' added to room '" + this.getName() + "'"
        );

    }

    public Set<SmartDevice> getDevices() {
        return this.devices;
    }

    public void removeDevice(SmartDevice device) {
        if (this.devices.remove(device)) {
            EventLogger.getInstance().logDeviceEvent(
                    device,
                    this,
                    EventType.DEVICE_REMOVED,
                    "Device '" + device.getName() + "' removed from room '" + this.getName() + "'"
            );
        }

    }
}
