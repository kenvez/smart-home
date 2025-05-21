package com.smarthome.core.model.room;

import com.smarthome.core.model.devices.base.SmartDevice;

import java.util.List;
import java.util.ArrayList;

public class Room {
    private String name;
    private RoomType type;
    private List<SmartDevice> devices;

    public Room(String name, RoomType type) {
        this.name = name;
        this.type = type;
        this.devices = new ArrayList<>();
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
}
