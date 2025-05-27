package com.smarthome.cli.utils;

import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.room.Room;

// Using a record class as suggested by IntelliJ IDEA's code inspection
public record SelectionResult(Room room, SmartDevice device) {}