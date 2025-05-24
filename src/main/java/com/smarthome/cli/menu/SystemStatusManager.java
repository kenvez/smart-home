package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;
import com.smarthome.core.model.rules.Rule;

import java.util.Scanner;

public class SystemStatusManager {
    private final Scanner scanner;
    private final HouseManager houseManager;
    private final RoomManager roomManager;
    private final DeviceManager deviceManager;
    private final RuleManager ruleManager;

    public SystemStatusManager(Scanner scanner, HouseManager houseManager, RoomManager roomManager,
                               DeviceManager deviceManager, RuleManager ruleManager) {
        this.scanner = scanner;
        this.houseManager = houseManager;
        this.roomManager = roomManager;
        this.deviceManager = deviceManager;
        this.ruleManager = ruleManager;
    }

    public void manage() {
        char choice = ' ';

        while (choice != 'b') {
            ScreenUtils.clearScreen();
            displayMenu();

            choice = scanner.next().charAt(0);
            scanner.nextLine();

            switch (choice) {
                case '1' -> {
                    displayFullStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    displayHouseStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '3' -> {
                    displayDeviceStatistics();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '4' -> {
                    displayRuleStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case 'b' -> System.out.println("Going back to main menu...");
                default -> {
                    System.out.println("Invalid choice! Please try again.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=======> System Status Menu <======\n");
        System.out.println("[1] Display full system status");
        System.out.println("[2] Display house status");
        System.out.println("[3] Display device statistics");
        System.out.println("[4] Display rule status");
        System.out.println("[b] Back to main menu\n");
        System.out.print("Enter your choice: ");
    }

    private void displayFullStatus() {
        ScreenUtils.clearScreen();
        System.out.println("\n=======> Full System Status <=======\n");

        System.out.println("Summary:");
        System.out.printf("Total Houses: %d%n", houseManager.getHouses().size());
        int totalRooms = houseManager.getHouses().stream()
                .mapToInt(house -> house.getRooms().size())
                .sum();
        System.out.printf("Total Rooms: %d%n", totalRooms);
        System.out.printf("Total Devices: %d%n", deviceManager.getDevices().size());
        System.out.printf("Total Rules: %d%n", ruleManager.getRules().size());

        System.out.println("\nDetailed Status:");

        for (House house : houseManager.getHouses()) {
            displayHouseDetails(house);
        }
    }

    private void displayHouseStatus() {
        House selectedHouse = ScreenUtils.selectHouse(scanner, houseManager);
        if (selectedHouse == null) return;

        ScreenUtils.clearScreen();
        System.out.printf("\n=======> Status for House: %s <=======\n%n", selectedHouse.getName());
        displayHouseDetails(selectedHouse);
    }

    private void displayHouseDetails(House house) {
        System.out.printf("\nHouse: %s%n", house.getName());
        System.out.printf("Location: (%.6f, %.6f)%n", house.getLatitude(), house.getLongitude());
        System.out.printf("Number of Rooms: %d%n", house.getRooms().size());

        // Display rooms and their devices
        for (Room room : house.getRooms()) {
            System.out.printf("\n  Room: %s (Type: %s)%n", room.getName(), room.getType());
            System.out.printf("  Devices in room: %d%n", room.getDevices().size());

            for (SmartDevice device : room.getDevices()) {
                System.out.printf("    - %s (%s): %s%n",
                        device.getName(),
                        device.getClass().getSimpleName(),
                        device.getStatus());
            }
        }
    }

    private void displayDeviceStatistics() {
        ScreenUtils.clearScreen();
        System.out.println("\n=======> Device Statistics <=======\n");

        int totalDevices = deviceManager.getDevices().size();
        long activeDevices = deviceManager.getDevices().stream()
                .filter(SmartDevice::isOn)
                .count();

        System.out.printf("Total Devices: %d%n", totalDevices);
        System.out.printf("Active Devices: %d%n", activeDevices);
        System.out.printf("Inactive Devices: %d%n", totalDevices - activeDevices);

        System.out.println("\nDevice Type Distribution:");
        deviceManager.getDevices().stream()
                .map(device -> device.getClass().getSimpleName())
                .distinct()
                .sorted()
                .forEach(type -> {
                    long count = deviceManager.getDevices().stream()
                            .filter(device -> device.getClass().getSimpleName().equals(type))
                            .count();
                    System.out.printf("  %s: %d%n", type, count);
                });
    }

    private void displayRuleStatus() {
        ScreenUtils.clearScreen();
        System.out.println("\n=======> Rule Status <=======\n");

        if (ruleManager.getRules().isEmpty()) {
            System.out.println("No rules defined in the system.");
            return;
        }

        System.out.printf("Total Rules: %d%n", ruleManager.getRules().size());
        System.out.println("\nRule Details:");

        for (Rule<?, ?> rule : ruleManager.getRules()) {
            System.out.printf("\nRule: %s%n", rule.getName());
            System.out.printf("  Condition Device: %s (%s)%n",
                    rule.getConditionDevice().getName(),
                    rule.getConditionDevice().getStatus());
            System.out.printf("  Action Device: %s (%s)%n",
                    rule.getActionDevice().getName(),
                    rule.getActionDevice().getStatus());
            System.out.printf("  Current Condition Status: %s%n",
                    rule.checkCondition() ? "Met" : "Not Met");
        }
    }
}
