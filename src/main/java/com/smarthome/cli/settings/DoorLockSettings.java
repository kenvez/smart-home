package com.smarthome.cli.settings;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.impl.DoorLock;

import java.util.Scanner;

public class DoorLockSettings {
    private final Scanner scanner;
    private final DeviceSettings deviceSettings;

    public DoorLockSettings(Scanner scanner, DeviceSettings deviceSettings) {
        this.scanner = scanner;
        this.deviceSettings = deviceSettings;
    }

    public void start(DoorLock doorLock) {
        ScreenUtils.clearScreen();

        System.out.println("\n=========> Door lock settings <=========\n");

        System.out.println("[1] Change device name                  ");
        System.out.println("[2] Toggle device                       ");
        System.out.println("[3] Lock door                           ");
        System.out.println("[4] Unlock door (PIN required)          ");
        System.out.println("[5] Change PIN code                     ");
        System.out.println("[6] Toggle auto-lock                    ");
        System.out.println("[b] Back to device menu               \n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        switch (choice) {
            case '1' -> deviceSettings.changeName(doorLock);
            case '2' -> deviceSettings.toggleDevice(doorLock);
            case '3' -> lockDoor(doorLock);
            case '4' -> unlockDoor(doorLock);
            case '5' -> changePinCode(doorLock);
            case '6' -> toggleAutoLock(doorLock);
            case 'b' -> System.out.println("\nGoing back to main menu.");
            default -> System.out.println("\nInvalid choice!");
        }
    }

    private void lockDoor(DoorLock doorLock) {
        if (doorLock.lock()) {
            System.out.println("Door locked successfully!");
        } else {
            System.out.println("Failed to lock the door. Make sure the device" +
                    " is on.");
        }
    }

    private void unlockDoor(DoorLock doorLock) {
        System.out.print("Enter PIN code: ");

        String pin = scanner.nextLine();

        if (doorLock.unlock(pin)) {
            System.out.println("Door unlocked successfully!");
        } else {
            System.out.println("Failed to unlock the door. Make sure the device" +
                    " is on.");
        }
    }

    private void changePinCode(DoorLock doorLock) {
        System.out.print("Enter current PIN code: ");

        String currentPin = scanner.nextLine();

        System.out.print("Enter new PIN code (4 digits): ");

        String newPin = scanner.nextLine();

        if (doorLock.changePinCode(currentPin, newPin)) {
            System.out.println("PIN code changed successfully!");
        } else {
            System.out.println("Failed to change the PIN code. Make sure the" +
                    " device is on and new PIN is 4 digits.");
        }
    }

    private void toggleAutoLock(DoorLock doorLock) {
        boolean newStatus = !doorLock.isAutoLockEnabled();

        doorLock.setAutoLock(newStatus);

        System.out.println("Auto-lock " + (newStatus ? "enabled" : "disabled") + ".");
    }
}
