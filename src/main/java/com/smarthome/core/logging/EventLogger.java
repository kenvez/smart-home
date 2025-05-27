package com.smarthome.core.logging;

import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.room.Room;

import java.io.*;

import java.time.format.*;
import java.time.LocalDate;

public class EventLogger {
    private static final String LOG_FILE_PREFIX = "smart_home_events_";
    private static final String LOG_FILE_EXTENSION = ".tsv";
    private static final String LOGS_DIRECTORY = "logs";
    private static EventLogger instance;

    private String currentLogFile;
    private int fileCounter = 1;

    private EventLogger() {
        File logsDir = new File(LOGS_DIRECTORY);

        if (!logsDir.exists()) {
            if (!logsDir.mkdir()) {
                System.out.println("\nFailed to create logs directory");
            }
        }

        generateNewLogFileName();

        initializeLogFile();
    }

    private void generateNewLogFileName() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = today.format(formatter);

        File file;
        fileCounter = 1;

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
                    System.out.println("\nFailed to create parent directories");
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {
                String headers = "TIMESTAMP\tDEVICE_ID\tDEVICE_TYPE" +
                        "\tROOM_NAME\tEVENT_TYPE\tDESCRIPTION";

                writer.write(headers);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("\nError initializing log file: " + e.getMessage());
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

    public void logRuleEvent(String ruleName, String ruleType, String roomName, EventType eventType, String description) {
        LogEvent event = new LogEvent(ruleName, ruleType, roomName, eventType, description);

        logEvent(event);
    }


    private void logEvent(LogEvent event) {
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
            System.err.println("\nError writing to log file: " + e.getMessage());
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
}
