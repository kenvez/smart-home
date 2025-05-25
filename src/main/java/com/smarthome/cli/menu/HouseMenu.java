package com.smarthome.cli.menu;

import com.smarthome.core.Main;
import com.smarthome.core.model.house.House;
import com.smarthome.cli.utils.*;
import com.smarthome.core.model.house.HouseType;

import java.util.*;

public class HouseMenu {
    private final Scanner scanner;

    public HouseMenu(Scanner scanner) {
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
                    houseSettings();
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

        if (Main.houses.stream()
                .anyMatch(house -> house.getName().equalsIgnoreCase(name))) {
            System.out.println("A house with this name already exists!");
            return;
        }

        System.out.print("Enter house latitude: ");
        double latitude = scanner.nextDouble();

        System.out.print("Enter house longitude: ");
        double longitude = scanner.nextDouble();

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

    private void houseSettings() {
        House selectedHouse = MenuUtils.selectHouse(scanner);

        if (selectedHouse == null) {
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n=========> House settings <=========\n");

        System.out.println("[1] Change house name                   ");
        System.out.println("[2] Change house latitude                ");
        System.out.println("[3] Change house longitude               ");
        System.out.println("[4] Change house type                    ");
        System.out.println("[b] Back to house menu                  \n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        switch (choice) {
            case '1' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=========> House settings <=========\n");

                System.out.print("Enter new house name: ");
                String newName = scanner.next();

                selectedHouse.setName(newName);

                System.out.println("\nHouse name changed successfully!");

                ScreenUtils.pressEnterToContinue(scanner);
            }
            case '2' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=========> House settings <=========\n");

                System.out.print("Enter new house latitude: ");
                double newLatitude = scanner.nextDouble();

                selectedHouse.setLatitude(newLatitude);

                System.out.println("\nHouse latitude changed successfully!");

                ScreenUtils.pressEnterToContinue(scanner);
            }
            case '3' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=========> House settings <=========\n");

                System.out.print("Enter new house longitude: ");
                double newLongitude = scanner.nextDouble();

                selectedHouse.setLongitude(newLongitude);

                System.out.println("\nHouse longitude changed successfully!");

                ScreenUtils.pressEnterToContinue(scanner);
            }
            case '4' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=========> House settings <=========\n");

                HouseType[] types = HouseType.values();

                for (int i = 0; i < types.length; i++) {
                    System.out.printf("[%d] %s%n", i + 1, types[i]);
                }

                System.out.print("\nEnter your choice: ");

                ScreenUtils.pressEnterToContinue(scanner);
            }
            case 'b' -> {
                System.out.println("\nGoing back to house menu.");
                ScreenUtils.pressEnterToContinue(scanner);
            }
            default -> {
                System.out.println("\nInvalid choice! Please try again.");
                ScreenUtils.pressEnterToContinue(scanner);
            }
        }
    }
}
