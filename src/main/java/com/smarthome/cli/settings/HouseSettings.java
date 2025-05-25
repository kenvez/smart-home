package com.smarthome.cli.settings;

import com.smarthome.cli.utils.MenuUtils;
import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.house.HouseType;

import java.util.Scanner;

public class HouseSettings {
    private final Scanner scanner;

    public HouseSettings(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
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

                scanner.nextLine();
            }
            case '2' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=========> House settings <=========\n");

                System.out.print("Enter new house latitude: ");
                double newLatitude = scanner.nextDouble();

                selectedHouse.setLatitude(newLatitude);

                System.out.println("\nHouse latitude changed successfully!");

                scanner.nextLine();
            }
            case '3' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=========> House settings <=========\n");

                System.out.print("Enter new house longitude: ");
                double newLongitude = scanner.nextDouble();

                selectedHouse.setLongitude(newLongitude);

                System.out.println("\nHouse longitude changed successfully!");

                scanner.nextLine();
            }
            case '4' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=========> House settings <=========\n");

                HouseType[] types = HouseType.values();

                for (int i = 0; i < types.length; i++) {
                    System.out.printf("[%d] %s%n", i + 1, types[i]);
                }

                System.out.print("\nEnter your choice: ");

                selectedHouse.setType(types[Integer.parseInt(scanner.next()) - 1]);

                scanner.nextLine();
            }
            case 'b' -> {
                System.out.println("\nGoing back to house menu.");

                scanner.nextLine();
            }
            default -> {
                System.out.println("\nInvalid choice! Please try again.");

                scanner.nextLine();
            }
        }
    }
}
