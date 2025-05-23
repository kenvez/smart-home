package com.smarthome.core.logging;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;


public class DeviceLogger {
    private static final String LOG_FILE = "device_events.tsv";
    private static final String HEADER = "TIMESTAMP\tDEVICE_ID\tDEVICE_TYPE\tROOM_NAME\tEVENT_TYPE\tDESCRIPTION";
    private static DeviceLogger instance;

    private DeviceLogger() {
        initializeLogFile();
    }

    public static DeviceLogger getInstance() {
        if (instance == null) {
            instance = new DeviceLogger();
        }
        return instance;
    }

    private void initializeLogFile() {
        Path logFile = Paths.get(LOG_FILE);
        if (!Files.exists(logFile)) {
            try {
                Files.write(logFile, Collections.singletonList(HEADER),
                        StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize log file", e);
            }
        }
    }

    public void logEvent(DeviceLogEntry entry) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(LOG_FILE),
                StandardOpenOption.APPEND)) {
            writer.newLine();
            writer.write(entry.toTsvLine());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write log entry", e);
        }
    }

    // Metody do generowania raportów
    public Map<String, Long> getActiveDevicesCount() {
        return readLogEntries()
                .stream()
                .filter(entry -> entry.getEventType() == EventType.STATUS_CHANGE)
                .collect(Collectors.groupingBy(
                        DeviceLogEntry::getDeviceType,
                        Collectors.counting()
                ));
    }

    public Map<String, List<DeviceLogEntry>> getDeviceHistoryByRoom() {
        return readLogEntries()
                .stream()
                .collect(Collectors.groupingBy(
                        DeviceLogEntry::getRoomName
                ));
    }

    public List<DeviceLogEntry> getRecentEvents(int limit) {
        return readLogEntries()
                .stream()
                .sorted(Comparator.comparing(DeviceLogEntry::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<DeviceLogEntry> readLogEntries() {
        try {
            return Files.lines(Paths.get(LOG_FILE))
                    .skip(1) // Skip header
                    .map(this::parseLogEntry)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read log entries", e);
        }
    }

    private DeviceLogEntry parseLogEntry(String line) {
        try {
            String[] parts = line.split("\t");
            if (parts.length != 6) return null;

            return new DeviceLogEntry(
                    parts[1], // deviceId
                    parts[2], // deviceType
                    parts[3], // roomName
                    EventType.valueOf(parts[4]), // eventType
                    parts[5]  // description
            );
        } catch (Exception e) {
            return null;
        }
    }

}
