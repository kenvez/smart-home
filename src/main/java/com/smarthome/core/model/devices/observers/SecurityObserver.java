package com.smarthome.core.model.devices.observers;

import com.smarthome.core.model.devices.base.DeviceObserver;
import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SmartDevice;

import java.time.LocalDateTime;

public class SecurityObserver implements DeviceObserver {
    @Override
    public void update(SmartDevice device) {
        if (device.getStatus() == DeviceStatus.ON &&
                LocalDateTime.now().getHour() == 23 ||
                LocalDateTime.now().getHour() < 5) {
            System.out.println("\nSECURITY ALERT: Device '" + device.getName() +
                    "' turned on during night hours!");
        }
    }

}
