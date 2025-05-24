package com.smarthome.cli.utils;

import com.smarthome.core.Main;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;

import java.util.*;

public class MenuUtils {
    public static void displayMainMenu() {
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

    public static void displayHousesList(Set<House> houses) {
        List<House> housesList = new ArrayList<>(houses);

        if (houses.isEmpty()) {
            System.out.println("No houses found! Please add a house first.");
            return;
        }

        int index = 1;

        for (House house : housesList) {
            System.out.printf("[%d] %s [%.6f, %.6f]%n",
                    index++,
                    house.getName(),
                    house.getLatitude(),
                    house.getLongitude()
            );
        }
    }

    public static List<Room> displayRoomsList(Set<Room> rooms) {
        List<Room> roomsList = new ArrayList<>(rooms);

        if (rooms.isEmpty()) {
            System.out.println("No rooms found in the selected house!");
            return roomsList;
        }

        int index = 1;
        for (Room room : roomsList) {
            System.out.printf("[%d] %s [Type: %s]%n",
                    index++,
                    room.getName(),
                    room.getType()
            );
        }

        return roomsList;
    }



    public static House selectHouse(Scanner scanner) {
        List<House> housesList = new ArrayList<>(Main.houses);

        System.out.println("\n==========> Select House <==========\n");

        // TODO implement exception

        if (Main.houses.isEmpty()) {
            System.out.println("No houses found! Please add a house first.");

            return null;
        }

        int index = 1;

        for (House house : housesList) {
            System.out.printf("[%d] %s [%.6f, %.6f]%n",
                    index++,
                    house.getName(),
                    house.getLatitude(),
                    house.getLongitude()
            );
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= housesList.size()) {
                House selectedHouse = housesList.get(choice - 1);

                System.out.println("Selected house: " + selectedHouse.getName());

                return selectedHouse;
            } else {
                System.out.println("\nInvalid house number!");

                ScreenUtils.pressEnterToContinue(scanner);

                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");

            ScreenUtils.pressEnterToContinue(scanner);

            return null;
        }
    }
}
