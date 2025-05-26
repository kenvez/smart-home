package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.Main;
import com.smarthome.core.management.DeviceManagement;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;

import java.util.*;
import java.util.stream.Collectors;

public class SystemStatusMenu {
    private final Scanner scanner;

    public SystemStatusMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        char choice = ' ';

        while (choice != 'b') {
            ScreenUtils.clearScreen();
            display();

            choice = scanner.next().charAt(0);

            scanner.nextLine();

            switch (choice) {
                case '1' -> {
                    displayOverallStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    displayHousesStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '3' -> {
                    displayRoomsStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '4' -> {
                    displayDevicesStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '5' -> {
                    displaySensorsStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '6' -> {
                    displayRulesStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case 'b' -> {
                    System.out.println("\nGoing back to main menu.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                default -> {
                    System.out.println("\nInvalid choice! Please try again.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
            }
        }
    }

    private void display() {
        System.out.println("\n=======> System status menu <=======\n");

        System.out.println("[1] Overall status             ");
        System.out.println("[2] Houses status              ");
        System.out.println("[3] Rooms status               ");
        System.out.println("[4] Devices status             ");
        System.out.println("[5] Sensors status             ");
        System.out.println("[6] Rules status               ");
        System.out.println("[b] Back to main menu                \n");

        System.out.print("Enter your choice: ");
    }

    private void displayOverallStatus() {
        ScreenUtils.clearScreen();

        System.out.println("\n=========> Overall status <=========\n");

        int totalHouses = Main.houses.size();
        int totalRooms = 0;
        int totalDevices = 0;
        int activeDevices = 0;

        for (House house : Main.houses) {
            totalRooms += house.getRooms().size();

            for (Room room : house.getRooms()) {
                totalDevices += room.getDevices().size();

                for (SmartDevice device : room.getDevices()) {
                    if (device.isOn()) {
                        activeDevices++;
                    }
                }
            }
        }

        System.out.printf("Houses: %d%n", totalHouses);
        System.out.printf("Rooms: %d%n", totalRooms);
        System.out.printf("Devices: %d (Active: %d)\n", totalDevices, activeDevices
        );

        if (totalHouses == 0) {
            System.out.println("\nSystem Status: No houses found in the system.");
        } else if (activeDevices == 0 && totalDevices > 0) {
            System.out.println("\nSystem Status: No active devices.");
        } else {
            System.out.println("\nSystem Status: Operational.");
        }
    }

    private void displayHousesStatus() {
        ScreenUtils.clearScreen();

        System.out.println("\n=========> Houses status <==========\n");

        if (Main.houses.isEmpty()) {
            System.out.println("No houses found in the system.");
            return;
        }

        for (House house : Main.houses) {
            System.out.printf("House: %s%n", house.getName());
            System.out.printf("Latitude: %f%n", house.getLatitude());
            System.out.printf("Longitude: %f%n", house.getLongitude());
            System.out.printf("Type: %s%n", house.getType());
            System.out.printf("Rooms: %d%n", house.getRooms().size());

            int devicesInHouse = 0;
            int activeDevicesInHouse = 0;

            for (Room room : house.getRooms()) {
                devicesInHouse += room.getDevices().size();

                for (SmartDevice device : room.getDevices()) {
                    if (device.isOn()) {
                        activeDevicesInHouse++;
                    }
                }
            }

            System.out.println("\nDevices: " + devicesInHouse);
            System.out.println("Active Devices: " + activeDevicesInHouse);
        }


    }

    private void displayRoomsStatus() {
        System.out.println("\n==========> Rooms status <==========\n");

        if (Main.houses.isEmpty()) {
            System.out.println("No houses found in the system.");
            return;
        }

        for (House house : Main.houses) {
            System.out.printf("House: %s%n", house.getName());

            if (house.getRooms().isEmpty()) {
                System.out.println("No rooms found in this house.");

                continue;
            }

            for (Room room : house.getRooms()) {
                System.out.println("\tRoom: " + room.getName());
                System.out.println("\tDevices: " + room.getDevices().size());

                int activeDevicesInRoom = 0;

                for (SmartDevice device : room.getDevices()) {
                    if (device.isOn()) {
                        activeDevicesInRoom++;
                    }
                }

                System.out.println("\tActive Devices: " + activeDevicesInRoom);
            }
        }
    }

    private void displayDevicesStatus() {
        ScreenUtils.clearScreen();

        System.out.println("\n=========> Devices status <=========\n");

        List<SmartDevice> allDevices = getAllDevices();

        if (allDevices.isEmpty()) {
            System.out.println("No devices found in the system.");

            return;
        }

        List<SmartDevice> onDevices = allDevices.stream()
                .filter(SmartDevice::isOn)
                .toList();

        List<SmartDevice> offDevices = allDevices.stream()
                .filter(device -> !device.isOn())
                .toList();

        System.out.println("Active Devices (" + onDevices.size() + "):");

        if (onDevices.isEmpty()) {
            System.out.println("No active devices.");
        } else {
            for (SmartDevice device : onDevices) {
                System.out.printf("- %s (%s): ON\n", device.getName(), device.getClass().getSimpleName());
            }
        }

        System.out.println("\nInactive Devices (" + offDevices.size() + "):");

        if (offDevices.isEmpty()) {
            System.out.println("No inactive devices.");
        } else {
            for (SmartDevice device : offDevices) {
                System.out.printf("- %s (%s): OFF\n", device.getName(), device.getClass().getSimpleName());
            }
        }


    }

    private void displaySensorsStatus() {
        ScreenUtils.clearScreen();

        System.out.println("\n=========> Sensors status <=========\n");

        System.out.println("Sensor status display is not yet implemented.");
        System.out.println("This feature will be available in a future update.");
    }

    private void displayRulesStatus() {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Rules status <==========\n");

        System.out.println("Rule status display is not yet implemented.");
        System.out.println("This feature will be available in a future update.");
    }

    private List<SmartDevice> getAllDevices() {
        List<SmartDevice> allDevices = new ArrayList<>();

        for (House house : Main.houses) {
            for (Room room : house.getRooms()) {
                allDevices.addAll(room.getDevices());
            }
        }

        return allDevices;
    }
}
