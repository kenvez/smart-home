package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;

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
                    startDeviceSimulation();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    stopDeviceSimulation();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '3' -> {
                    listSimulatedDevices();
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

        System.out.println("[1] Start device simulation             ");
        System.out.println("[2] Stop device simulation              ");
        System.out.println("[3] List simulated devices              ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void startDeviceSimulation() {

    }

    private void stopDeviceSimulation() {

    }

    private void listSimulatedDevices() {

    }
}
