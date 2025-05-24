package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.house.House;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.Scanner;

public class HouseManager {
    private final Scanner scanner;
    private final Set<House> houses;

    public HouseManager(Scanner scanner) {
        this.scanner = scanner;
        this.houses = new TreeSet<>((houseOne, houseTwo) -> houseOne.getName()
                .compareToIgnoreCase(houseTwo.getName()));
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

        System.out.println("\n===========> Add house <============\n");

        System.out.print("Enter house name: ");
        String name = scanner.next();

        if (houses.stream().anyMatch(h -> h.getName().equalsIgnoreCase(name))) {
            System.out.println("A house with this name already exists!");
            return;
        }

        System.out.print("Enter house latitude: ");
        double latitude = scanner.nextDouble();

        System.out.print("Enter house longitude: ");
        double longitude = scanner.nextDouble();

        House house = new House(name, latitude, longitude);

        houses.add(house);

        System.out.println("\nHouse added successfully!");
    }

    private void removeHouse() {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Remove house <==========\n");

        if (houses.isEmpty()) {
            System.out.println("No houses found!");
            return;
        }

        int index = 1;
        List<House> housesList = new ArrayList<>(houses);

        for (House house : housesList) {
            System.out.printf("[%d] %s (%.6f, %.6f)%n",
                    index++,
                    house.getName(),
                    house.getLatitude(),
                    house.getLongitude()
            );
        }

        System.out.print("[b] Back to main menu                 \n");

        System.out.print("\nEnter your choice: ");

        String choice = scanner.next();

        if (choice.equalsIgnoreCase("b")) {
            return;
        }

        try {
            int choiceIndex = Integer.parseInt(choice);

            if (choiceIndex > 0 && choiceIndex <= housesList.size()) {
                houses.remove(housesList.get(choiceIndex - 1));
                System.out.println("House removed successfully!");
            } else {
                System.out.println("Invalid house number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private void listHouses() {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> List houses <===========\n");

        if (houses.isEmpty()) {
            System.out.println("No houses found!");

            return;
        }

        int index = 1;

        for (House house : houses) {
            System.out.printf("[%d] %s (%.6f, %.6f)%n",
                    index++,
                    house.getName(),
                    house.getLatitude(),
                    house.getLongitude()
            );
        }
    }

    public Set<House> getHouses() {
        return houses;
    }
}
