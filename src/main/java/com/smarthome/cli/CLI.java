package com.smarthome.cli;

import com.smarthome.cli.utils.*;
import com.smarthome.cli.menu.*;

import java.util.Scanner;

public class CLI {
    private final Scanner scanner;
    private final HouseMenu houseMenu;
    private final RoomMenu roomMenu;
    private final DeviceMenu deviceMenu;
    private final SensorMenu sensorMenu;
    private final RuleMenu ruleMenu;
    private final SystemStatusMenu systemStatusMenu;
    private final SimulateDevicesMenu simulateDevicesMenu;

    public CLI() {
        this.scanner = new Scanner(System.in);
        this.houseMenu = new HouseMenu(scanner);
        this.roomMenu = new RoomMenu(scanner);
        this.deviceMenu = new DeviceMenu(scanner);
        this.sensorMenu = new SensorMenu(scanner);
        this.ruleMenu = new RuleMenu(scanner);
        this.systemStatusMenu = new SystemStatusMenu(scanner);
        this.simulateDevicesMenu = new SimulateDevicesMenu(scanner);
    }

    public void start() {
        char choice = ' ';

        while (choice != 'q') {
            ScreenUtils.clearScreen();
            MenuUtils.displayMainMenu();

            choice = scanner.next().charAt(0);

            scanner.nextLine();

            switch (choice) {
                case '1' -> houseMenu.start();
                case '2' -> roomMenu.start();
                case '3' -> deviceMenu.start();
                case '4' -> sensorMenu.start();
                case '5' -> ruleMenu.start();
                case '6' -> systemStatusMenu.start();
                case '7' -> simulateDevicesMenu.start();
                case 'q' -> {
                    System.out.println("\nQuiting program.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                default -> {
                    System.out.println("\nInvalid choice! Please try again.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
            }
        }
    }
}
