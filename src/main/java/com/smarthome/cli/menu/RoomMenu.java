package com.smarthome.cli.menu;

import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.*;
import com.smarthome.cli.utils.*;

import java.util.*;

public class RoomMenu {
    private final Scanner scanner;

    public RoomMenu(Scanner scanner) {
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
                    addRoom();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    removeRoom();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '3' -> {
                    listRooms();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '4' -> {
                    roomSettings();
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
        System.out.println("\n===========> Room menu <============\n");

        System.out.println("[1] Add room                            ");
        System.out.println("[2] Remove room                         ");
        System.out.println("[3] List rooms                          ");
        System.out.println("[4] Room settings                       ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void addRoom() {
        House selectedHouse = MenuUtils.selectHouse(scanner);

        if (selectedHouse == null) {
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n============> Add room <============\n");

        System.out.print("Enter room name: ");

        String name = scanner.nextLine();

        if (selectedHouse.getRooms().stream()
                .anyMatch(room -> room.getName().equalsIgnoreCase(name))) {
            System.out.println("A room with this name already exists!");
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n============> Add room <============\n");

        RoomType[] types = RoomType.values();

        for (int i = 0; i < types.length; i++) {
            System.out.printf("[%d] %s%n", i + 1, types[i]);
        }

        System.out.print("\nEnter your choice: ");

        try {
            int typeChoice = Integer.parseInt(scanner.nextLine());

            if (typeChoice > 0 && typeChoice <= types.length) {
                RoomType selectedType = types[typeChoice - 1];
                Room room = new Room(name, selectedType);

                selectedHouse.addRoom(room);

                System.out.println("\nRoom '" + name + "' added successfully!");
            } else {
                System.out.println("\nInvalid room!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");
        }
    }

    private void removeRoom() {
        House selectedHouse = MenuUtils.selectHouse(scanner);

        if (selectedHouse == null) {
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n==========> Remove room <===========\n");

        List<Room> rooms = MenuUtils.displayRoomsList(selectedHouse.getRooms());

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= rooms.size()) {
                Room roomToRemove = rooms.get(choice - 1);

                selectedHouse.removeRoom(rooms.get(choice - 1));

                System.out.println("\nRoom '" + roomToRemove.getName() + "' removed successfully!");
            } else {
                System.out.println("\nInvalid room!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");
        }
    }

    private void listRooms() {
        House selectedHouse = MenuUtils.selectHouse(scanner);

        if (selectedHouse == null) {
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n===========> List rooms <===========\n");

        MenuUtils.displayRoomsList(selectedHouse.getRooms());
    }

    private void roomSettings() {
        House selectedHouse = MenuUtils.selectHouse(scanner);

        if (selectedHouse == null) {
            return;
        }

        Room selectedRoom = MenuUtils.selectRoom(scanner, selectedHouse);

        if (selectedRoom == null) {
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n=========> Room settings <==========\n");

        System.out.println("[1] Change room name                   ");
        System.out.println("[2] Change room type                   ");
        System.out.println("[b] Back to room menu                \n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        switch (choice) {
            case '1' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=========> Room settings <==========\n");

                System.out.print("Enter new room name: ");

                String newName = scanner.next();

                selectedRoom.setName(newName);

                System.out.println("\nRoom name changed successfully!");

                scanner.nextLine();
            }
            case '2' -> {
                ScreenUtils.clearScreen();

                System.out.println("\n=========> Room settings <==========\n");

                RoomType[] types = RoomType.values();

                for (int i = 0; i < types.length; i++) {
                    System.out.printf("[%d] %s%n", i + 1, types[i]);
                }

                System.out.print("\nEnter your choice: ");

                selectedRoom.setType(types[Integer.parseInt(scanner.next()) - 1]);

                scanner.nextLine();
            }
            case 'b' -> {
                System.out.println("\nGoing back to room menu.");

                scanner.nextLine();
            }
            default -> {
                System.out.println("\nInvalid choice! Please try again.");

                scanner.nextLine();
            }
        }
    }
}
