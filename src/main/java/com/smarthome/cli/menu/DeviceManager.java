package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.base.DeviceStatus;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.impl.Lightbulb;
import com.smarthome.core.model.devices.impl.Outlet;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;

import java.awt.Color;
import java.util.*;

public class DeviceManager {
    private final Scanner scanner;
    private final HouseManager houseManager;
    private Room selectedRoom;

    public DeviceManager(Scanner scanner, HouseManager houseManager) {
        this.scanner = scanner;
        this.houseManager = houseManager;
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
                    System.out.println("Going back to main menu...\n");
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
        System.out.println("\n=======> Device management <========\n");

        System.out.println("[1] Add device                           ");
        System.out.println("[2] Remove device                        ");
        System.out.println("[3] List devices                         ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void addDevice() {
        House selectedHouse = ScreenUtils.selectHouse(scanner, houseManager);

        if (selectedHouse == null) return;

        Room selectedRoom = ScreenUtils.selectRoom(scanner, selectedHouse);

        if (selectedRoom == null) return;


        ScreenUtils.clearScreen();

        System.out.println("\n===========> Add device <===========\n");

        System.out.print("Enter device name: ");
        String name = scanner.nextLine();

        if (selectedRoom.getDevices().stream().anyMatch(device -> device.getName().equalsIgnoreCase(name))) {
            System.out.println("A device with this name already exists!");
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n===========> Add device <===========\n");

        System.out.println("[1] Lightbulb");
        System.out.println("[2] Outlet");
        System.out.println("[b] Back to main menu");

        System.out.print("\nEnter your choice: ");

        char type = scanner.next().charAt(0);

        SmartDevice device = null;

        switch (type) {
            case '1' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=======> Lightbulb settings <=======\n");

                try {
                    Lightbulb lightbulb = new Lightbulb(name, DeviceStatus.OFF);
                    device = lightbulb;

                    double hue = lightbulb.getHue();
                    double saturation = lightbulb.getSaturation();
                    double value = lightbulb.getValue();

                    System.out.println("Enter color settings (or press Enter to use defaults).");

                    System.out.print("\nHue (0-359, default: 30): ");

                    String hueInput = scanner.nextLine();

                    if (!hueInput.isEmpty()) {
                        hue = Double.parseDouble(hueInput);

                        lightbulb.setHue(hue);
                    }

                    System.out.print("Saturation (0-1, default: 0.01): ");

                    String saturationInput = scanner.nextLine();

                    if (!saturationInput.isEmpty()) {
                        saturation = Double.parseDouble(saturationInput);

                        lightbulb.setSaturation(saturation);
                    }

                    System.out.print("Value (0-1, default: 0.98): ");

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
            case 'b' -> System.out.println("Going back to main menu...");

            default -> {
                System.out.println("Invalid device type selection!");
                return;
            }
        }

        selectedRoom.addDevice(device);
        System.out.println("Device '" + name + "' added to room '" + selectedRoom.getName() + "'!");
    }

    private void removeDevice() {
        House selectedHouse = ScreenUtils.selectHouse(scanner, houseManager);

        if (selectedHouse == null) return;

        Room selectedRoom = ScreenUtils.selectRoom(scanner, selectedHouse);

        if (selectedRoom == null) return;

        ScreenUtils.clearScreen();

        System.out.println("\n==========> Remove Device <===========\n");

        if (selectedRoom.getDevices().isEmpty()) {
            System.out.println("No devices found in the selected room!");
            return;
        }

        Set<SmartDevice> roomDevices = selectedRoom.getDevices();

        if (roomDevices.isEmpty()) {
            System.out.println("No devices found in the selected room!");
            return;
        }

        System.out.println("Devices in " + selectedRoom.getName() + ":\n");

        List<SmartDevice> devicesList = new ArrayList<>(roomDevices);
        int index = 1;

        for (SmartDevice device : devicesList) {
            System.out.printf("[%d] %s (Status: %s)%n",
                index++,
                device.getName(),
                device.getStatus()
            );
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= devicesList.size()) {
                SmartDevice deviceToRemove = devicesList.get(choice - 1);

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
        House selectedHouse  = ScreenUtils.selectHouse(scanner, houseManager);

        if (selectedHouse == null) {
            return;
        }

        Room selectedRoom = ScreenUtils.selectRoom(scanner, selectedHouse);

        if (selectedRoom == null) {
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n==========> List Devices <===========\n");

        Set<SmartDevice> roomDevices = selectedRoom.getDevices();

        if (roomDevices.isEmpty()) {
            System.out.println("No devices found in the selected room!");
            return;
        }

        System.out.println("Devices in " + selectedRoom.getName() + ":\n");

        List<SmartDevice> devicesList = new ArrayList<>(roomDevices);
        int index = 1;

        for (SmartDevice device : devicesList) {
            System.out.printf("[%d] %s (Status: %s)%n",
                index++,
                device.getName(),
                device.getStatus()
            );
        }
    }

    public Set<SmartDevice> getDevices() {
        Set<SmartDevice> allDevices = new HashSet<>();

        for (House house : houseManager.getHouses()) {
            for (Room room : house.getRooms()) {
                allDevices.addAll(room.getDevices());
            }
        }

        return allDevices;
    }
}
