package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.house.House;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HouseManager {
    private final Scanner scanner;
    private final List<House> houses;

    public HouseManager(Scanner scanner) {
        this.scanner = scanner;
        this.houses = new ArrayList<>();
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

    private void displayMenu() {
        System.out.println("\n========> House management <========\n");

        System.out.println("[1] Add house                           ");
        System.out.println("[2] Remove house                        ");
        System.out.println("[3] List houses                         ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void addHouse() {
        ScreenUtils.clearScreen();

        System.out.print("Enter house name: ");
        String name = scanner.next();

        System.out.print("Enter house latitude: ");
        double latitude = scanner.nextDouble();

        System.out.print("Enter house longitude: ");
        double longitude = scanner.nextDouble();

        House house = new House(name, latitude, longitude);

        houses.add(house);

        System.out.println("\nHouse added successfully!");
    }

    private void removeHouse() {
        if (houses.isEmpty()) {
            System.out.println("No houses found!");
            return;
        }

        listHouses();

        System.out.print("Enter the number of the house to remove: ");

        int index = scanner.nextInt() - 1;

        if (index >= 0 && index < houses.size()) {
            houses.remove(index);
            System.out.println("House removed successfully!");
        } else {
            System.out.println("Invalid house number!");
        }
    }

    private void listHouses() {
        if (houses.isEmpty()) {
            System.out.println("\nNo houses found!");
            return;
        }

        System.out.println("\nList of houses:");

        for (int i = 0; i < houses.size(); i++) {
            House house = houses.get(i);

            System.out.printf("%d. %s (%.6f, %.6f)%n",
                    i + 1,
                    house.getName(),
                    house.getLatitude(),
                    house.getLongitude()
            );

        }
    }
}
