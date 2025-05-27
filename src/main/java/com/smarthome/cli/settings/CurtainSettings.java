package com.smarthome.cli.settings;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.impl.Curtain;

import java.util.Scanner;

public class CurtainSettings {
    private final Scanner scanner;
    private final DeviceSettings deviceSettings;

    public CurtainSettings(Scanner scanner, DeviceSettings deviceSettings) {
        this.scanner = scanner;
        this.deviceSettings = deviceSettings;
    }

    public void start(Curtain curtain) {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Curtain settings <========\n");

        System.out.println("[1] Change device name                  ");
        System.out.println("[2] Toggle device                       ");
        System.out.println("[3] Open fully                          ");
        System.out.println("[4] Close fully                         ");
        System.out.println("[5] Set openness percentage             ");
        System.out.println("[6] Toggle automatic mode               ");
        System.out.println("[b] Back to device menu               \n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        switch (choice) {
            case '1' -> deviceSettings.changeName(curtain);
            case '2' -> deviceSettings.toggleDevice(curtain);
            case '3' -> openCurtainFully(curtain);
            case '4' -> closeCurtainFully(curtain);
            case '5' -> setCurtainOpenness(curtain);
            case '6' -> toggleCurtainAutomatic(curtain);
            case 'b' -> System.out.println("\nGoing back to device menu.");
            default -> System.out.println("\nInvalid choice!");
        }
    }

    private void openCurtainFully(Curtain curtain) {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Curtain settings <========\n");

        if (!curtain.isOn()) {
            System.out.println("Cannot open curtain while device is OFF.");
            return;
        }

        curtain.openFully();

        System.out.println("\nCurtain opened fully (100%).");
    }

    private void closeCurtainFully(Curtain curtain) {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Curtain settings <========\n");

        if (!curtain.isOn()) {
            System.out.println("Cannot close curtain while device is OFF.");
            return;
        }

        curtain.closeFully();

        System.out.println("Curtain closed fully (0%).");
    }

    private void setCurtainOpenness(Curtain curtain) {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Curtain settings <========\n");

        if (!curtain.isOn()) {
            System.out.println("Cannot adjust curtain while device is OFF.");
            return;
        }

        System.out.print("Enter openness percentage (0-100%): ");

        try {
            int percentage = Integer.parseInt(scanner.nextLine());
            curtain.setOpennessPercentage(percentage);
            System.out.println("Curtain openness set to " + percentage + "%");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid value: " + e.getMessage());
        }
    }

    private void toggleCurtainAutomatic(Curtain curtain) {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Curtain settings <========\n");

        boolean newState = !curtain.isAutomatic();

        curtain.setAutomatic(newState);

        System.out.println("Automatic mode " + (newState ? "enabled" : "disabled") + ".");
    }
}
