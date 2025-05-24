package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;

import java.awt.desktop.ScreenSleepEvent;
import java.util.Scanner;

public class RuleManager {
    private final Scanner scanner;
    private final HouseManager houseManager;
    private final RoomManager roomManager;
    private final DeviceManager deviceManager;
    private SmartDevice selectedDevice;

    public RuleManager(Scanner scanner, HouseManager houseManager, RoomManager roomManager, DeviceManager deviceManager) {
        this.scanner = scanner;
        this.houseManager = houseManager;
        this.roomManager = roomManager;
        this.deviceManager = deviceManager;
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
                    addRule();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    removeRule();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '3' -> {
                    listRules();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case 'b' -> {
                    System.out.println("Going back to main menu...");
                    ScreenUtils.pressEnterToContinue(scanner);
                }

                default -> {
                    System.out.println("Invalid choice! Please try again.\n");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
            }
        }

    }

    private void displayMenu() {
        System.out.println("\n=========> Rule management <========\n");

        System.out.println("[1] Add rule                            ");
        System.out.println("[2] Remove rule                         ");
        System.out.println("[3] List rules                          ");
        System.out.println("[b] Back to main menu                   ");

        System.out.print("Enter your choice: ");
    }

    private void addRule() {
        House selectedHouse = ScreenUtils.selectHouse(scanner, houseManager);

        if (selectedHouse == null) return;

        Room selectedRoom = ScreenUtils.selectRoom(scanner, roomManager, houseManager);

        if (selectedRoom == null) return;

        SmartDevice selectedDevice = ScreenUtils.selectDevice(scanner, houseManager, roomManager, deviceManager);

        if (selectedDevice == null) return;

        ScreenUtils.clearScreen();

        System.out.println("\n============> Add rule <============\n");


    }

    private void removeRule() {
        House selectedHouse = ScreenUtils.selectHouse(scanner, houseManager);

        if (selectedHouse == null) return;

        Room selectedRoom = ScreenUtils.selectRoom(scanner, roomManager, houseManager);

        if (selectedRoom == null) return;

        SmartDevice selectedDevice = ScreenUtils.selectDevice(scanner, houseManager, roomManager, deviceManager);

        if (selectedDevice == null) return;

        ScreenUtils.clearScreen();

        System.out.println("\n=========> Rule management <========\n");

        if (selectedDevice.getRules().isEmpty()) {
            System.out.println("No rules found in the selected device!");
            return;
        }
    }

    private void listRules() {

    }
}
