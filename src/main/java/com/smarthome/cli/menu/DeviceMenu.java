package com.smarthome.cli.menu;

import com.smarthome.core.management.DeviceManagement;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;
import com.smarthome.core.model.devices.base.*;
import com.smarthome.core.model.devices.impl.*;
import com.smarthome.cli.utils.*;

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
                case '4' -> {
                    deviceSettings();
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
        System.out.println("[4] Device settings                     ");
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

                    DeviceManagement.getInstance().registerDevice(device);

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

                DeviceManagement.getInstance().registerDevice(device);

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

                DeviceManagement.getInstance().unregisterDevice(deviceToRemove);
                selectedRoom.removeDevice(deviceToRemove);

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

        MenuUtils.displayDevicesList(selectedRoom.getDevices());
    }

    private void deviceSettings() {
        House selectedHouse = MenuUtils.selectHouse(scanner);

        if (selectedHouse == null) {
            return;
        }

        Room selectedRoom = MenuUtils.selectRoom(scanner, selectedHouse);

        if (selectedRoom == null) {
            return;
        }

        SmartDevice selectedDevice = MenuUtils.selectDevice(scanner,
                selectedRoom);

        if (selectedDevice == null) {
            return;
        }

        switch (selectedDevice) {
            case Lightbulb lightbulb -> handleLightbulbSettings(lightbulb);
            case Outlet outlet -> handleOutletSettings(outlet);
            case CoffeeMachine coffeeMachine -> handleCoffeeMachineSettings(coffeeMachine);
            default -> System.out.println("Invalid device type!");

        }
    }

    private void handleLightbulbSettings(Lightbulb lightbulb) {
        ScreenUtils.clearScreen();

        System.out.println("\n=======> Lightbulb settings <=======\n");

        System.out.println("[1] Change device name                  ");
        System.out.println("[2] Toggle device                       ");
        System.out.println("[3] Color settings                      ");
        System.out.println("[b] Back to device menu               \n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        switch (choice) {
            case '1' -> changeName(lightbulb);
            case '2' -> toggleDevice(lightbulb);
            case '3' -> colorSettings(lightbulb);
            case 'b' -> System.out.println("Going back to device menu...");
            default -> System.out.println("Invalid choice!");
        }
    }

    private void handleOutletSettings(Outlet outlet) {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Outlet settings <=========\n");

        System.out.println("[1] Change device name                  ");
        System.out.println("[2] Toggle device                       ");
        System.out.println("[3] Toggle in-use status                ");
        System.out.println("[4] Display current status              ");
        System.out.println("[b] Back to device menu               \n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        switch (choice) {
            case '1' -> changeName(outlet);
            case '2' -> toggleDevice(outlet);
            case '3' -> toggleInUseStatus(outlet);
            case 'b' -> System.out.println("Going back to device menu...");
            default -> System.out.println("Invalid choice!");
        }
    }

    private void handleCoffeeMachineSettings(CoffeeMachine coffeeMachine) {
        System.out.println("\n====> Coffee Machine Settings <=====\n");

        System.out.println("[1] Change device name                  ");
        System.out.println("[2] Toggle device                       ");
        System.out.println("[3] Select coffee type                  ");
        System.out.println("[4] Select cup size                     ");
        System.out.println("[5] Brew coffee                         ");
        System.out.println("[6] Refill water                        ");
        System.out.println("[b] Back to device menu                \n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        switch (choice) {
            case '1' -> changeName(coffeeMachine);
            case '2' -> toggleDevice(coffeeMachine);
            case '3' -> selectCoffeeType(coffeeMachine);
            case '4' -> selectCupSize(coffeeMachine);
            case '5' -> brewCoffee(coffeeMachine);
            case '6' -> refillWater(coffeeMachine);
            case 'b' -> System.out.println("Going back to device menu...");
            default -> System.out.println("Invalid choice!");
        }

    }

    private void changeName(SmartDevice device) {
        ScreenUtils.clearScreen();

        System.out.println("\n=======> Change device name <=======\n");

        System.out.print("Enter new device name: ");

        String newName = scanner.nextLine();

        device.setName(newName);

        System.out.printf("\n%s name changed successfully!\n",
                device.getName());
    }

    private void toggleDevice(SmartDevice device) {
        ScreenUtils.clearScreen();

        System.out.println("\n=========> Toggle device <==========\n");

        if (device.isOn()) {
            device.turnOff();
            System.out.println("\nDevice turned off!");
        } else {
            device.turnOn();
            System.out.println("\nDevice turned on!");
        }
    }

    private void colorSettings(Lightbulb lightbulb) {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Color settings <==========\n");

        try {
            System.out.printf("Current settings: Hue = %.1f, Saturation = %" +
                    ".2f, Value = %.2f%n", lightbulb.getHue(),
                    lightbulb.getSaturation(), lightbulb.getValue());

            System.out.print("\nEnter new hue(0-359, press Enter to skip): ");

            String hueInput = scanner.nextLine();

            if (!hueInput.isEmpty()) {
                double hue = Double.parseDouble(hueInput);

                lightbulb.setHue(hue);
            }

            System.out.print("Enter new saturation(0-1, press Enter to skip): ");

            String saturationInput = scanner.nextLine();

            if (!saturationInput.isEmpty()) {
                double saturation = Double.parseDouble(saturationInput);

                lightbulb.setSaturation(saturation);
            }

            System.out.print("Enter new value(0-1, press Enter to skip): ");

            String valueInput = scanner.nextLine();

            if (!valueInput.isEmpty()) {
                double value = Double.parseDouble(valueInput);

                lightbulb.setValue(value);
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid number format!");
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void toggleInUseStatus(Outlet outlet) {
        ScreenUtils.clearScreen();

        System.out.println("\n======> Toggle in-use status <======\n");

        System.out.println("Current in-use status: " + outlet.isInUse());
        System.out.print("\nToggle in-use status? (y/n): ");

        char choice = scanner.next().charAt(0);

        if (choice == 'y') {
            outlet.setInUse(!outlet.isInUse());

            System.out.println("\nIn-use status changed to: " + outlet.isInUse());
        }
    }

    private void selectCoffeeType(CoffeeMachine coffeeMachine) {
        ScreenUtils.clearScreen();

        System.out.println("\n=======> Select coffee type <========\n");

        CoffeeType[] types = CoffeeType.values();

        for (int i = 0; i < types.length; i++) {
            System.out.printf("[%d] %s%n", i + 1, types[i]);
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= types.length) {
                coffeeMachine.setCoffeeType(types[choice - 1]);

                System.out.println("\nCoffee type changed to: " + types[choice - 1]);
            } else {
                System.out.println("\nInvalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");
        }

    }

    private void selectCupSize(CoffeeMachine coffeeMachine) {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Select cup size <=========\n");

        CupSize[] sizes = CupSize.values();

        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("[%d] %s%n", i + 1, sizes[i]);
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= sizes.length) {
                coffeeMachine.setCupSize(sizes[choice - 1]);

                System.out.println("\nCup size changed to: " + sizes[choice - 1]);
            } else {
                System.out.println("\nInvalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");
        }

    }

    private void brewCoffee(CoffeeMachine coffeeMachine) {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Brew coffee <===========\n");

        System.out.printf("Current settings: %s (%s)%n",
                coffeeMachine.getCoffeeType(),
                coffeeMachine.getCupSize());
        System.out.printf("Water level: %d%%%n", coffeeMachine.getWaterLevel());

        System.out.print("\nStart brewing? (y/n): ");
        String input = scanner.nextLine().toLowerCase();

        if (input.equals("y")) {
            coffeeMachine.brewCoffee();
            if (coffeeMachine.isBrewing()) {
                System.out.println("\nStarted brewing coffee!");
            }
        }

    }

    private void refillWater(CoffeeMachine coffeeMachine) {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Refill water <==========\n");

        System.out.printf("Current water level: %d%%%n", coffeeMachine.getWaterLevel());

        System.out.print("\nRefill water to 100%? (y/n): ");
        String input = scanner.nextLine().toLowerCase();

        if (input.equals("y")) {
            coffeeMachine.refill();
            System.out.println("\nWater refilled to 100%");
        }

    }
}
