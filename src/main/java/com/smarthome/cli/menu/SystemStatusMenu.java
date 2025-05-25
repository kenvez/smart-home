package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;

import java.util.*;

public class SystemStatusMenu {
    private final Scanner scanner;

    public SystemStatusMenu(Scanner scanner) {
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
                    displayOverallStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    displayHousesStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '3' -> {
                    displayRoomsStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '4' -> {
                    displayDevicesStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '5' -> {
                    displaySensorsStatus();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '6' -> {
                    displayRulesStatus();
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
        System.out.println("\n=======> System status menu <=======\n");

        System.out.println("[1] Display overall status             ");
        System.out.println("[2] Display houses status              ");
        System.out.println("[3] Display rooms status               ");
        System.out.println("[4] Display devices status             ");
        System.out.println("[5] Display sensors status             ");
        System.out.println("[6] Display rules status               ");
        System.out.println("[b] Back to main menu                \n");

        System.out.print("Enter your choice: ");
    }

    private void displayOverallStatus() {
        System.out.println("\n=========> Overall status <=========\n");


    }

    private void displayHousesStatus() {
        System.out.println("\n=========> Houses status <==========\n");
    }

    private void displayRoomsStatus() {
        System.out.println("\n==========> Rooms status <==========\n");
    }

    private void displayDevicesStatus() {
        System.out.println("\n=========> Devices status <=========\n");
    }

    private void displaySensorsStatus() {
        System.out.println("\n=========> Sensors status <=========\n");
    }

    private void displayRulesStatus() {
        System.out.println("\n==========> Rules status <==========\n");
    }
}
