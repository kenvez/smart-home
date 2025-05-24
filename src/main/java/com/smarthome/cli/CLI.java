package com.smarthome.cli;

import com.smarthome.cli.menu.*;
import com.smarthome.cli.utils.ScreenUtils;

import java.util.Scanner;

public class CLI {
    private final Scanner scanner;
    private final HouseManager houseManager;
    private final RoomManager roomManager;
    private final DeviceManager deviceManager;
    private final RuleManager ruleManager;
    private final SystemStatusManager systemStatusManager;


    public CLI() {
        this.scanner = new Scanner(System.in);
        this.houseManager = new HouseManager(scanner);
        this.roomManager = new RoomManager(scanner, houseManager);
        this.deviceManager = new DeviceManager(scanner, houseManager);
        this.ruleManager = new RuleManager(scanner, houseManager, deviceManager);
        this.systemStatusManager = new SystemStatusManager(scanner, houseManager, roomManager,
                deviceManager, ruleManager);

    }

    public void start() {
        char choice = ' ';

        while (choice != 'q') {
            ScreenUtils.clearScreen();
            displayMainMenu();

            choice = scanner.next().charAt(0);

            scanner.nextLine();

            switch (choice) {
                case '1' -> houseManager.manage();
                case '2' -> roomManager.manage();
                case '3' -> deviceManager.manage();
                case '4' -> ruleManager.manage();
                case '5' -> systemStatusManager.manage();
                case '6' -> System.out.println("Simulate devices not yet implemented!");
                case 'q' -> System.out.println("Quiting program!");

                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=========> Smart home CLI <=========\n");

        System.out.println("[1] Manage houses                       ");
        System.out.println("[2] Manage rooms                        ");
        System.out.println("[3] Manage devices                      ");
        System.out.println("[4] Manage rules                        ");
        System.out.println("[5] Display system status               ");
        System.out.println("[6] Simulate devices                    ");
        System.out.println("[q] Quit program                      \n");

        System.out.print("Enter your choice: ");
    }
}
