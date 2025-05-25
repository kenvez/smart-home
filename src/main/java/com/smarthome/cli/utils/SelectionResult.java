package com.smarthome.cli.utils;

import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.room.Room;

public class SelectionResult {
    private final Room room;
    private final SmartDevice device;

    public SelectionResult(Room room, SmartDevice device) {
        this.room = room;
        this.device = device;
    }

    public Room getRoom() {
        return room;
    }

    public SmartDevice getDevice() {
        return device;
    }
}
