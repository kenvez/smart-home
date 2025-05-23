package com.smarthome.core.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeviceLogEntry {
    private final LocalDateTime timestamp;
    private final String deviceId;
    private final String deviceType;
    private final String roomName;
    private final EventType eventType;
    private final String description;

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DeviceLogEntry(String deviceId, String deviceType, String roomName,
                          EventType eventType, String description) {
        this.timestamp = LocalDateTime.now();
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.roomName = roomName;
        this.eventType = eventType;
        this.description = sanitizeDescription(description);
    }

    private String sanitizeDescription(String description) {
        return description.replace("\t", " ");
    }

    public String toTsvLine() {
        return String.join("\t",
                timestamp.format(TIMESTAMP_FORMATTER),
                deviceId,
                deviceType,
                roomName,
                eventType.toString(),
                description
        );
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDeviceId() { return deviceId; }
    public String getDeviceType() { return deviceType; }
    public String getRoomName() { return roomName; }
    public EventType getEventType() { return eventType; }
    public String getDescription() { return description; }

}
