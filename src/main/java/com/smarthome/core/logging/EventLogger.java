package com.smarthome.core.logging;

import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.room.Room;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.ArrayList;

public class EventLogger {
    private static final String LOG_FILE_PREFIX = "smart_home_events_";
    private static final String LOG_FILE_EXTENSION = ".tsv";
    private static final String LOGS_DIRECTORY = "logs";
    private static EventLogger instance;
    private final List<LogEvent> eventCache = new ArrayList<>();
    private String currentLogFile;
    private int fileCounter = 1;

    private EventLogger() {
        File logsDir = new File(LOGS_DIRECTORY);
        if (!logsDir.exists()) {
            if (!logsDir.mkdir()) {
                System.err.println("Failed to create logs directory");
            }
        }

        generateNewLogFileName();

        initializeLogFile();
    }

    private void generateNewLogFileName() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = today.format(formatter);

        fileCounter = 1;
        File file;
        do {
            currentLogFile = LOGS_DIRECTORY + File.separator +
                    LOG_FILE_PREFIX + dateStr + "-" + fileCounter + LOG_FILE_EXTENSION;
            file = new File(currentLogFile);
            fileCounter++;
        } while (file.exists());

        fileCounter--;
    }

    private void initializeLogFile() {
        try {
            File logFile = new File(currentLogFile);

            File parentDir = logFile.getParentFile();
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    System.err.println("Failed to create parent directories");
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {
                String header = "TIMESTAMP\tDEVICE_ID\tDEVICE_TYPE\tROOM_NAME\tEVENT_TYPE\tDESCRIPTION";
                writer.write(header);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }

    public static EventLogger getInstance() {
        if (instance == null) {
            instance = new EventLogger();
        }
        return instance;
    }

    public void logDeviceEvent(SmartDevice device, Room room, EventType eventType, String description) {
        LogEvent event = new LogEvent(device, room, eventType, description);
        logEvent(event);
    }

    public void logCustomEvent(String deviceId, String deviceType, String roomName, EventType eventType, String description) {
        LogEvent event = new LogEvent(deviceId, deviceType, roomName, eventType, description);
        logEvent(event);
    }

    private void logEvent(LogEvent event) {
        eventCache.add(event);

        LocalDate eventDate = event.getTimestamp().toLocalDate();
        LocalDate currentFileDate = extractDateFromFilename(currentLogFile);

        if (!eventDate.equals(currentFileDate)) {
            generateNewLogFileName();
            initializeLogFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentLogFile, true))) {
            writer.write(event.formatLogEntry());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    private LocalDate extractDateFromFilename(String filename) {
        try {
            String dateStr = filename.substring(
                    filename.lastIndexOf(LOG_FILE_PREFIX) + LOG_FILE_PREFIX.length(),
                    filename.lastIndexOf("-" + fileCounter + LOG_FILE_EXTENSION)
            );
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    public List<LogEvent> getAllEvents() {
        return new ArrayList<>(eventCache);
    }

    public List<LogEvent> getEventsForDevice(String deviceId) {
        return eventCache.stream()
                .filter(event -> event.getDeviceId().equals(deviceId))
                .toList();
    }

    public List<LogEvent> getEventsForRoom(String roomName) {
        return eventCache.stream()
                .filter(event -> event.getRoomName().equals(roomName))
                .toList();
    }

    public List<LogEvent> getEventsByType(EventType eventType) {
        return eventCache.stream()
                .filter(event -> event.getEventType() == eventType)
                .toList();
    }

    public List<LogEvent> getEventsBetween(LocalDateTime start, LocalDateTime end) {
        return eventCache.stream()
                .filter(event -> !event.getTimestamp().isBefore(start) && !event.getTimestamp().isAfter(end))
                .toList();
    }

    public String getCurrentLogFilePath() {
        return currentLogFile;
    }
}
