package com.smarthome.cli;

import com.smarthome.cli.utils.*;
import com.smarthome.cli.menu.*;

import java.util.Scanner;

public class CLI {
    private final Scanner scanner;
    private final HouseMenu houseMenu;
    private final RoomMenu roomMenu;
    private final DeviceMenu deviceMenu;

    public CLI() {
        this.scanner = new Scanner(System.in);
        this.houseMenu = new HouseMenu(scanner);
        this.roomMenu = new RoomMenu(scanner);
        this.deviceMenu = new DeviceMenu(scanner);
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
                case '4' -> System.out.println("Rules not yet implemented!");
                case '5' -> System.out.println("Notifications not yet implemented!");
                case '6' -> System.out.println("Simulate devices not yet implemented!");
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
