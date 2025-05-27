package com.smarthome.cli.menu;

import com.smarthome.cli.utils.MenuUtils;
import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.cli.utils.SelectionResult;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.room.Room;

import java.util.List;
import java.util.Scanner;

public class SimulateDevicesMenu {
    private final Scanner scanner;

    public SimulateDevicesMenu(Scanner scanner) {
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
                    simulateDevice();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    simulateAllDevices();
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
        System.out.println("\n=====> Simulate devices menu <======\n");

        System.out.println("[1] Simulate device                     ");
        System.out.println("[2] Simulate all devices                ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void simulateDevice() {
        SelectionResult selection = MenuUtils.performSelection(scanner, false);

        if (selection == null) {
            return;
        }

        Room selectedRoom = selection.room();

        ScreenUtils.clearScreen();

        System.out.println("\n========> Simulate device <=========\n");

        if (selectedRoom.getDevices().isEmpty()) {
            System.out.println("No devices found in the selected room!");
            return;
        }

        List<SmartDevice> devices = MenuUtils.displayDevicesList(selectedRoom.getDevices());

        System.out.print("\nSelect device to simulate: ");

        try {
            int deviceChoice = Integer.parseInt(scanner.nextLine());

            if (deviceChoice > 0 && deviceChoice <= devices.size()) {
                SmartDevice selectedDevice = devices.get(deviceChoice - 1);

                System.out.println("\nSimulating device: " + selectedDevice.getName());

                selectedDevice.simulate();
            } else {
                System.out.println("Invalid device number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private void simulateAllDevices() {
        SelectionResult selection = MenuUtils.performSelection(scanner, false);

        if (selection == null) {
            return;
        }

        Room selectedRoom = selection.room();

        ScreenUtils.clearScreen();

        System.out.println("\n======> Simulate all devices <======\n");

        if (selectedRoom.getDevices().isEmpty()) {
            System.out.println("\nNo devices found in the selected room!");
        } else {
            System.out.println("\nSimulating all devices in " + selectedRoom.getName() + ":");

            for (SmartDevice device : selectedRoom.getDevices()) {
                System.out.println("\n=== " + device.getName() + " ===");
                device.simulate();
            }
        }
    }
}
