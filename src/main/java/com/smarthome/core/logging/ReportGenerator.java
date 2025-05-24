package com.smarthome.core.logging;

import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SensorDevice;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.impl.TemperatureSensor;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {
    private final EventLogger logger = EventLogger.getInstance();

    public String generateActiveDevicesReport(House house) {
        StringBuilder report = new StringBuilder();
        report.append("=== Active Devices Report ===\n");
        report.append("House: ").append(house.getName()).append("\n\n");

        List<Room> rooms = house.getRooms().stream().toList();

        Map<String, Long> deviceCountByRoom = rooms.stream()
                .flatMap(room -> room.getDevices().stream()
                        .filter(SmartDevice::isOn)
                        .map(device -> Map.entry(room.getName(), device)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.counting()
                ));

        report.append("Active Devices by Room:\n");
        deviceCountByRoom.forEach((roomName, count) ->
                report.append(roomName).append(": ").append(count).append(" active devices\n"));

        report.append("\nDetailed Active Devices:\n");
        rooms.forEach(room -> {
            List<SmartDevice> activeDevices = room.getDevices().stream()
                    .filter(SmartDevice::isOn)
                    .toList();

            if (!activeDevices.isEmpty()) {
                report.append(room.getName()).append(":\n");
                activeDevices.forEach(device ->
                        report.append("  - ").append(device.getName())
                                .append(" (").append(device.getClass().getSimpleName()).append(")\n"));
            }
        });

        return report.toString();
    }

    public String generateTemperatureReport(House house) {
        StringBuilder report = new StringBuilder();
        report.append("=== Temperature Report ===\n");
        report.append("House: ").append(house.getName()).append("\n\n");

        List<Room> rooms = house.getRooms().stream().toList();

        List<TemperatureSensor> tempSensors = rooms.stream()
                .flatMap(room -> room.getDevices().stream())
                .filter(device -> device instanceof TemperatureSensor && device.isOn())
                .map(device -> (TemperatureSensor) device)
                .toList();

        if (tempSensors.isEmpty()) {
            report.append("No active temperature sensors found.\n");
            return report.toString();
        }

        DoubleSummaryStatistics tempStats = tempSensors.stream()
                .mapToDouble(sensor -> sensor.readValue())
                .summaryStatistics();

        report.append("Temperature Statistics:\n");
        report.append("  Average: ").append(String.format("%.1f°C\n", tempStats.getAverage()));
        report.append("  Minimum: ").append(String.format("%.1f°C\n", tempStats.getMin()));
        report.append("  Maximum: ").append(String.format("%.1f°C\n", tempStats.getMax()));
        report.append("  Number of sensors: ").append(tempStats.getCount()).append("\n\n");

        // Report by room
        report.append("Temperature by Room:\n");
        rooms.forEach(room -> {
            List<TemperatureSensor> roomSensors = room.getDevices().stream()
                    .filter(device -> device instanceof TemperatureSensor && device.isOn())
                    .map(device -> (TemperatureSensor) device)
                    .toList();

            if (!roomSensors.isEmpty()) {
                double avgTemp = roomSensors.stream()
                        .mapToDouble(sensor -> sensor.readValue())
                        .average()
                        .orElse(0.0);

                report.append("  ").append(room.getName())
                        .append(": ").append(String.format("%.1f°C\n", avgTemp));
            }
        });

        return report.toString();
    }

    public String generateEventActivityReport(LocalDateTime start, LocalDateTime end) {
        List<LogEvent> events = logger.getEventsBetween(start, end);

        StringBuilder report = new StringBuilder();
        report.append("=== Event Activity Report ===\n");
        report.append("Period: ").append(start).append(" to ").append(end).append("\n\n");

        Map<EventType, Long> eventsByType = events.stream()
                .collect(Collectors.groupingBy(
                        LogEvent::getEventType,
                        Collectors.counting()
                ));

        report.append("Events by Type:\n");
        eventsByType.forEach((type, count) ->
                report.append("  ").append(type).append(": ").append(count).append("\n"));

        Map<String, Long> eventsByRoom = events.stream()
                .collect(Collectors.groupingBy(
                        LogEvent::getRoomName,
                        Collectors.counting()
                ));

        report.append("\nEvents by Room:\n");
        eventsByRoom.forEach((room, count) ->
                report.append("  ").append(room).append(": ").append(count).append("\n"));

        Map<String, Long> eventsByDeviceType = events.stream()
                .collect(Collectors.groupingBy(
                        LogEvent::getDeviceType,
                        Collectors.counting()
                ));

        report.append("\nEvents by Device Type:\n");
        eventsByDeviceType.forEach((type, count) ->
                report.append("  ").append(type).append(": ").append(count).append("\n"));

        return report.toString();
    }

    public String generateSensorReadingsReport(House house) {
        StringBuilder report = new StringBuilder();
        report.append("=== Sensor Readings Report ===\n");
        report.append("House: ").append(house.getName()).append("\n\n");

        List<Room> rooms = house.getRooms().stream().toList();

        rooms.forEach(room -> {
            List<SmartDevice> sensors = room.getDevices().stream()
                    .filter(device -> device instanceof SensorDevice && device.isOn())
                    .toList();

            if (!sensors.isEmpty()) {
                report.append(room.getName()).append(":\n");

                sensors.forEach(device -> {
                    SensorDevice<?> sensor = (SensorDevice<?>) device;
                    report.append("  - ").append(device.getName())
                            .append(" (").append(device.getClass().getSimpleName()).append("): ")
                            .append(sensor.readValue())
                            .append(" ").append(sensor.getUnit()).append("\n");
                });
                report.append("\n");
            }
        });

        if (report.toString().equals("=== Sensor Readings Report ===\nHouse: " + house.getName() + "\n\n")) {
            report.append("No active sensors found.\n");
        }

        return report.toString();
    }
}

