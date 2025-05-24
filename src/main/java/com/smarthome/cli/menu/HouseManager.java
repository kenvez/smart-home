package com.smarthome.cli.menu;

import com.smarthome.core.model.house.House;
import com.smarthome.cli.utils.*;

import java.util.*;

public class HouseManager {
    private final Scanner scanner;
    private final Set<House> houses;

    public HouseManager(Scanner scanner) {
        this.scanner = scanner;
        this.houses = new HashSet<>();
    }

    public void manage() {
        char choice = ' ';

        while (choice != 'b') {
            ScreenUtils.clearScreen();
            displayMenu();

            choice = scanner.next().charAt(0);

            scanner.nextLine();

            switch (choice) {
                case '1' -> {
                    addHouse();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    removeHouse();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '3' -> {
                    listHouses();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case 'b' -> System.out.println("Going back to main menu...");

                default -> {
                    System.out.println("Invalid choice! Please try again.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
            }
        }
    }
}
