package com.smarthome.cli.menu;

import com.smarthome.cli.utils.*;
import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.impl.Lightbulb;
import com.smarthome.core.model.devices.impl.Outlet;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DeviceMenu {
    private final Scanner scanner;

    public DeviceMenu(Scanner scanner) {
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
        System.out.println("\n==========> Device menu <===========\n");

        System.out.println("[1] Add device                          ");
        System.out.println("[2] Remove device                       ");
        System.out.println("[3] List devices                        ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void addDevice() {
        House selectedHouse = MenuUtils.selectHouse(scanner);

        if (selectedHouse == null) {
            return;
        }

        Room selectedRoom = MenuUtils.selectRoom(scanner, selectedHouse);

        if (selectedRoom == null) {
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n===========> Add device <===========\n");

        System.out.print("Enter device name: ");

        String name = scanner.nextLine();

        if (selectedRoom.getDevices().stream()
                .anyMatch(device -> device.getName().equalsIgnoreCase(name))) {
            System.out.println("A device with this name already exists!");
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n===========> Add device <===========\n");

        System.out.println("[1] Lightbulb                           ");
        System.out.println("[2] Outlet                              ");
        System.out.println("[b] Back to device menu");

        System.out.print("\nEnter your choice: ");

        char type = scanner.next().charAt(0);

        SmartDevice device = null;

        switch (type) {
            case '1' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=======> Lightbulb settings <=======\n");

                scanner.nextLine();

                try {
                    Lightbulb lightbulb = new Lightbulb(name, DeviceStatus.OFF);
                    device = lightbulb;

                    double hue = lightbulb.getHue();
                    double saturation = lightbulb.getSaturation();
                    double value = lightbulb.getValue();

                    System.out.println("Enter color settings (or press Enter to use defaults).");

                    System.out.print("\nHue [0-359, default: 30]: ");

                    String hueInput = scanner.nextLine();

                    if (!hueInput.isEmpty()) {
                        hue = Double.parseDouble(hueInput);

                        lightbulb.setHue(hue);
                    }

                    System.out.print("Saturation [0-1, default: 0.01]: ");

                    String saturationInput = scanner.nextLine();

                    if (!saturationInput.isEmpty()) {
                        saturation = Double.parseDouble(saturationInput);

                        lightbulb.setSaturation(saturation);
                    }

                    System.out.print("Value [0-1, default: 0.98]: ");

                    String valueInput = scanner.nextLine();

                    if (!valueInput.isEmpty()) {
                        value = Double.parseDouble(valueInput);

                        lightbulb.setValue(value);
                    }

                    Color rgbColor = lightbulb.getRGBColor();

                    ScreenUtils.clearScreen();

                    System.out.println("\n=======> Lightbulb settings <=======\n");

                    System.out.println("Lightbulb created with color:");

                    System.out.printf("\nHSV: (%.1f°, %.2f, %.2f)%n", hue, saturation, value);
                    System.out.printf("RGB: (%d, %d, %d)%n", rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input format! Using default values.");
                    device = new Lightbulb(name, DeviceStatus.OFF);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage() + " Using default values.");
                    device = new Lightbulb(name, DeviceStatus.OFF);
                }

                System.out.println("\nLightbulb created successfully!");
            }
            case '2' -> {
                device = new Outlet(name, DeviceStatus.OFF);

                System.out.println("\nOutlet created successfully!");
            }
            case 'b' -> System.out.println("Going back to device menu...");

            default -> {
                System.out.println("Invalid device type selection!");
                return;
            }
        }

        selectedRoom.addDevice(device);
        System.out.println("\nDevice '" + name + "' added to room '" + selectedRoom.getName() + "'!");
    }

    private void removeDevice() {
        House selectedHouse = MenuUtils.selectHouse(scanner);

        if (selectedHouse == null) {
            return;
        }

        Room selectedRoom = MenuUtils.selectRoom(scanner, selectedHouse);

        if (selectedRoom == null) {
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n=========> Remove device <==========\n");

        if (selectedRoom.getDevices().isEmpty()) {
            System.out.println("No devices found in the selected room!");
            return;
        }

        System.out.println("Devices in " + selectedRoom.getName() + ":\n");

        List<SmartDevice> devices=
                MenuUtils.displayDevicesList(selectedRoom.getDevices());

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= devices.size()) {
                SmartDevice deviceToRemove = devices.get(choice - 1);

                selectedRoom.removeDevice(deviceToRemove);

                System.out.println("\nDevice '" + deviceToRemove.getName() + "' removed successfully!");
            } else {
                System.out.println("Invalid device number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private void listDevices() {
        House selectedHouse = MenuUtils.selectHouse(scanner);

        if (selectedHouse == null) {
            return;
        }

        Room selectedRoom = MenuUtils.selectRoom(scanner, selectedHouse);

        if (selectedRoom == null) {
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n===========> List rooms <===========\n");

        List<SmartDevice> devices=
                MenuUtils.displayDevicesList(selectedRoom.getDevices());
    }
}
