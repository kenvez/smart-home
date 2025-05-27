package com.smarthome.cli.settings;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.impl.Lightbulb;

import java.util.Scanner;

public class LightbulbSettings{
    private final Scanner scanner;
    private final DeviceSettings deviceSettings;

    public LightbulbSettings(Scanner scanner, DeviceSettings deviceSettings) {
        this.scanner = scanner;
        this.deviceSettings = deviceSettings;
    }

    public void start(Lightbulb lightbulb) {
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
            case '1' -> deviceSettings.changeName(lightbulb);
            case '2' -> deviceSettings.toggleDevice(lightbulb);
            case '3' -> colorSettings(lightbulb);
            case 'b' -> System.out.println("\nGoing back to main menu.");
            default -> System.out.println("\nInvalid choice!");
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
}
