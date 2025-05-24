package com.smarthome.cli.menu;

import com.smarthome.cli.utils.*;

import java.util.*;

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
                    System.out.println("Going back to main menu.\n");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                default -> {
                    System.out.println("Invalid choice! Please try again.");
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

    }

    private void removeDevice() {

    }

    private void listDevices() {

    }
}
