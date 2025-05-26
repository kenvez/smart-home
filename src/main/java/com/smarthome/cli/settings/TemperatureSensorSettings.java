package com.smarthome.cli.settings;

import com.smarthome.cli.utils.MenuUtils;
import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.cli.utils.SelectionResult;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.impl.TemperatureSensor;

import java.util.Scanner;

public class TemperatureSensorSettings {
    private final Scanner scanner;
    private final DeviceSettings deviceSettings;

    public TemperatureSensorSettings(Scanner scanner, DeviceSettings deviceSettings) {
        this.scanner = scanner;
        this.deviceSettings = deviceSettings;
    }

    public void start(TemperatureSensor temperatureSensor) {
        ScreenUtils.clearScreen();

        System.out.println("\n==> Temperature sensor settings <===\n");

        System.out.println("[1] Change device name                  ");
        System.out.println("[2] Toggle device                       ");
        System.out.println("[3] Set temperature threshold           ");
        System.out.println("[4] Set update interval                 ");
        System.out.println("[5] View current temperature            ");
        System.out.println("[b] Back to device menu               \n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        switch (choice) {
            case '1' -> deviceSettings.changeName(temperatureSensor);
            case '2' -> deviceSettings.toggleDevice(temperatureSensor);
            case '3' -> setTemperatureThreshold(temperatureSensor);
            case '4' -> setUpdateInterval(temperatureSensor);
            case '5' -> viewCurrentTemperature(temperatureSensor);
            case 'b' -> System.out.println("Going back to device menu...");
            default -> System.out.println("Invalid choice!");
        }

    }

    private void setTemperatureThreshold(TemperatureSensor temperatureSensor) {
        ScreenUtils.clearScreen();

        System.out.println("\n===> Set temperature threshold <====\n");

        System.out.println("Current threshold: " + temperatureSensor.getTemperatureThreshold() + "°C.");

        System.out.print("\nEnter new temperature threshold (°C): ");

        try {
            double threshold = Double.parseDouble(scanner.nextLine());

            temperatureSensor.setTemperatureThreshold(threshold);

            System.out.println("\nTemperature threshold updated to " + threshold + "°C.");
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void setUpdateInterval(TemperatureSensor temperatureSensor) {
        ScreenUtils.clearScreen();

        System.out.println("\n======> Set update interval <=======\n");

        System.out.println("Current update interval: " + temperatureSensor.getUpdateInterval() + " seconds.");

        System.out.print("\nEnter new update interval (seconds): ");

        try {
            int interval = Integer.parseInt(scanner.nextLine());

            temperatureSensor.setUpdateInterval(interval);

            System.out.println("\nUpdate interval set to " + interval + " " +
                    "seconds.");
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void viewCurrentTemperature(TemperatureSensor temperatureSensor) {
        ScreenUtils.clearScreen();

        System.out.println("\n=========> Smart home CLI <=========\n");

        System.out.printf("Device: %s\n", temperatureSensor.getName());
        System.out.printf("Device status: %s\n", temperatureSensor.isOn() ? "ON" : "OFF");
        System.out.printf("Sensor status: %s\n",
                temperatureSensor.getSensorStatus());
        System.out.printf("Current temperature: %.1f°C%n\n",
                temperatureSensor.getCurrentTemperature());
        System.out.printf("Temperature threshold: %s\n",
                temperatureSensor.getTemperatureThreshold());
        System.out.printf("Update interval: %s\n",
                temperatureSensor.getUpdateInterval());
    }
}
