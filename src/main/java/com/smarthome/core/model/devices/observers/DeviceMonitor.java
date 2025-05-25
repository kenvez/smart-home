package com.smarthome.core.model.devices.observers;

import com.smarthome.core.model.devices.base.DeviceObserver;
import com.smarthome.core.model.devices.base.SmartDevice;

public class DeviceMonitor implements DeviceObserver {
    @Override
    public void update(SmartDevice device) {
        System.out.println("Device '" + device.getName() + "' changed status:" +
                " " + device.getStatus()
        );
    }
}
