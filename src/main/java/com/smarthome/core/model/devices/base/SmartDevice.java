package com.smarthome.core.model.devices.base;

import java.util.UUID;

public abstract class SmartDevice implements Switchable {
    private final String id;
    private String name;

    public SmartDevice(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }


}
