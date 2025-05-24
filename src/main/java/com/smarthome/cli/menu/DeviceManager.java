package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.base.SmartDevice;

import java.util.Set;
import java.util.TreeSet;
import java.util.Scanner;

public class DeviceManager {
    private final Scanner scanner;
    private final RoomManager roomManager;
    private Set<SmartDevice> devices;

    public DeviceManager(Scanner scanner, RoomManager roomManager) {
        this.scanner = scanner;
        this.roomManager = roomManager;
        this.devices = new TreeSet<>((smartDeviceOne, smartDeviceTwo) -> smartDeviceOne.getName().compareToIgnoreCase(smartDeviceTwo.getName()));
    }

    public void manage() {
        char choice = ' ';

        while (choice != 'b') {
            ScreenUtils.clearScreen();
            displayMenu();

            choice = scanner.nextLine().charAt(0);

            switch (choice) {
                case '1' -> {
                    addDevice();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    removeDevice();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '3' -> {
                    listDevices();
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
        System.out.println("\n=======> Device management <========\n");

        System.out.println("[1] Add device                           ");
        System.out.println("[2] Remove device                        ");
        System.out.println("[3] List devices                         ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void selectRoom() {

    }

    private void addDevice() {

    }

    private void removeDevice() {

    }

    private void listDevices() {

    }
}
