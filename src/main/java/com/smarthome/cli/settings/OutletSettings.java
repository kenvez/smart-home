package com.smarthome.cli.settings;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.impl.Outlet;

import java.util.Scanner;

public class OutletSettings {
    private final Scanner scanner;
    private final DeviceSettings deviceSettings;

    public OutletSettings(Scanner scanner, DeviceSettings deviceSettings) {
        this.scanner = scanner;
        this.deviceSettings = deviceSettings;
    }

    public void start(Outlet outlet) {
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
            case '1' -> deviceSettings.changeName(outlet);
            case '2' -> deviceSettings.toggleDevice(outlet);
            case '3' -> toggleInUseStatus(outlet);
            case 'b' -> System.out.println("Going back to device menu...");
            default -> System.out.println("Invalid choice!");
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
}
