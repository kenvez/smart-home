package com.smarthome.cli.menu;

import com.smarthome.cli.settings.HouseSettings;
import com.smarthome.core.Main;
import com.smarthome.core.model.house.House;
import com.smarthome.cli.utils.*;
import com.smarthome.core.model.house.HouseType;

import java.util.*;

public class HouseMenu {
    private final Scanner scanner;
    private final HouseSettings houseSettings;

    public HouseMenu(Scanner scanner) {
        this.scanner = scanner;
        this.houseSettings = new HouseSettings(scanner);
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
                case '4' -> {
                    houseSettings.start();
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
        System.out.println("\n===========> House menu <===========\n");

        System.out.println("[1] Add house                           ");
        System.out.println("[2] Remove house                        ");
        System.out.println("[3] List houses                         ");
        System.out.println("[4] House settings                      ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void addHouse() {
        ScreenUtils.clearScreen();

        System.out.println("\n===========> Add House <============\n");

        System.out.print("Enter house name: ");

        String name = scanner.next();

        scanner.nextLine();

        if (Main.houses.stream()
                .anyMatch(house -> house.getName().equalsIgnoreCase(name))) {
            System.out.println("A house with this name already exists!");
            return;
        }


        System.out.print("Enter house latitude: ");

        double latitude;
        double longitude;

        try {
            String latitudeStr = scanner.next().replace(',', '.');

            scanner.nextLine();

            latitude = Double.parseDouble(latitudeStr);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid house latitude! Please enter a valid number.");
            return;
        }

        try {
            String longitudeStr = scanner.next().replace(',', '.');

            scanner.nextLine();

            longitude = Double.parseDouble(longitudeStr);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid house latitude! Please enter a valid number.");
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n===========> Add House <============\n");

        HouseType[] types = HouseType.values();

        for (int i = 0; i < types.length; i++) {
            System.out.printf("[%d] %s%n", i + 1, types[i]);
        }

        System.out.print("\nEnter your choice: ");

        try {
            int typeChoice = Integer.parseInt(scanner.next());

            if (typeChoice > 0 && typeChoice <= types.length) {
                HouseType selectedType = types[typeChoice - 1];
                House house = new House(name, latitude, longitude, selectedType);

                Main.houses.add(house);

                System.out.println("\nHouse '" + name + "' added successfully!");
            } else {
                System.out.println("\nInvalid house!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");
        }
    }

    private void removeHouse() {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Remove house <==========\n");

        MenuUtils.displayHousesList(Main.houses);

        System.out.print("[b] Back to house menu                \n");

        System.out.print("\nEnter your choice: ");

        String choice = scanner.next();

        if (choice.equalsIgnoreCase("b")) {
            return;
        }

        try {
            List<House> housesList = new ArrayList<>(Main.houses);
            int choiceIndex = Integer.parseInt(choice);

            if (choiceIndex > 0 && choiceIndex <= housesList.size()) {
                Main.houses.remove(housesList.get(choiceIndex - 1));

                System.out.println("\nHouse removed successfully!");
            } else {
                System.out.println("\nInvalid house number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");
        }
    }

    private void listHouses() {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> List houses <===========\n");

        MenuUtils.displayHousesList(Main.houses);
    }
}
