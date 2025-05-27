package com.smarthome.cli.settings;

import com.smarthome.core.logging.EventLogger;
import com.smarthome.core.logging.EventType;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.base.Switchable;
import com.smarthome.core.model.devices.impl.Curtain;
import com.smarthome.core.model.devices.impl.Lightbulb;
import com.smarthome.core.model.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class GroupControl {
    public static int applyToDevices(Room room, Predicate<SmartDevice> filter,
                                     Consumer<SmartDevice> action, String actionDescription) {
        if (room == null) return 0;

        List<SmartDevice> matchingDevices = room.getDevices().stream()
                .filter(filter)
                .toList();

        for (SmartDevice device : matchingDevices) {
            action.accept(device);

            EventLogger.getInstance().logDeviceEvent(
                    device,
                    room,
                    EventType.GROUP_CONTROL,
                    "Group action: " + actionDescription
            );
        }

        return matchingDevices.size();
    }

    public static int turnOnAllDevices(Room room) {
        return applyToDevices(
                room,
                Objects::nonNull,
                device -> {
                    if (device instanceof Switchable switchable && !switchable.isOn()) {
                        switchable.turnOn();
                    }
                },
                "Turned ON"
        );
    }

    public static int turnOffAllDevices(Room room) {
        return applyToDevices(
                room,
                Objects::nonNull,
                device -> {
                    if (device instanceof Switchable switchable && switchable.isOn()) {
                        switchable.turnOff();
                    }
                },
                "Turned OFF"
        );
    }
    public static int setAllCurtains(Room room, int percentage) {
        return applyToDevices(
                room,
                device -> device instanceof Curtain && device.isOn(),
                device -> ((Curtain) device).setOpennessPercentage(percentage),
                "Set openness to " + percentage + "%"
        );
    }

    public static int setAllLightbulbs(Room room, double brightness) {
        return applyToDevices(
                room,
                device -> device instanceof Lightbulb && device.isOn(),
                device -> ((Lightbulb) device).setValue(brightness),
                "Set brightness to " + (int)(brightness * 100) + "%"
        );
    }

    public static <T extends SmartDevice> List<T> getDevicesOfType(Room room, Class<T> deviceClass) {
        if (room == null) return new ArrayList<>();

        return room.getDevices().stream()
                .filter(deviceClass::isInstance)
                .map(deviceClass::cast)
                .collect(Collectors.toList());
    }

}
