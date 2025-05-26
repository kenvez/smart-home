package com.smarthome.core.model.devices.observers;

import com.smarthome.core.model.devices.base.DeviceObserver;
import com.smarthome.core.model.devices.base.SmartDevice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeviceLogger implements DeviceObserver {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void update(SmartDevice device) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("\n[" + timestamp + "] Device event: " + device.getName() + " - Status: " + device.getStatus());
    }
}
