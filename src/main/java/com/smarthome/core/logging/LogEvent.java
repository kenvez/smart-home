package com.smarthome.core.logging;

import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.room.Room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LogEvent {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime timestamp;
    private final String deviceId;
    private final String deviceType;
    private final String roomName;
    private final EventType eventType;
    private final String description;

    public LogEvent(SmartDevice device, Room room, EventType eventType, String description) {
        this.timestamp = LocalDateTime.now();
        this.deviceId = device.getId();
        this.deviceType = device.getClass().getSimpleName().toUpperCase();
        this.roomName = room != null ? room.getName() : "UNKNOWN";
        this.eventType = eventType;

        this.description = description.replace('\t', ' ');
    }

    public LogEvent(String deviceId, String deviceType, String roomName, EventType eventType, String description) {
        this.timestamp = LocalDateTime.now();
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.roomName = roomName;
        this.eventType = eventType;
        this.description = description.replace('\t', ' ');
    }

    public String formatLogEntry() {
        return String.join("\t",
                timestamp.format(TIMESTAMP_FORMATTER),
                deviceId,
                deviceType,
                roomName,
                eventType.toString(),
                description
        );
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
