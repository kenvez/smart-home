package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;
import com.smarthome.core.model.room.RoomType;

import java.util.Scanner;
import java.util.TreeSet;
import java.util.ArrayList;

public class RoomManager {
    private final Scanner scanner;
    private final HouseManager houseManager;
    private House selectedHouse;
    private TreeSet<Room> rooms;

    public RoomManager(Scanner scanner, HouseManager houseManager) {
        this.scanner = scanner;
        this.houseManager = houseManager;
        this.rooms = new TreeSet<>((roomOne, roomTwo) -> roomOne.getName().compareToIgnoreCase(roomTwo.getName()));
    }

    public void manage() {
        if (houseManager.getHouses().isEmpty()) {
            System.out.println("\nNo houses available. Please add a house first.");
            ScreenUtils.pressEnterToContinue(scanner);
            return;
        }

        if (!selectHouse()) {
            return;
        }


        char choice = ' ';

        while (choice != 'b') {
            ScreenUtils.clearScreen();
            displayMenu();

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
                case 'b' -> System.out.println("Going back to main menu...");

                default -> {
                    System.out.println("Invalid choice! Please try again.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
            }

        }
    }

    private void displayMenu() {
        System.out.println("\n========> Room management <=========\n");

        System.out.println("[1] Add room                            ");
        System.out.println("[2] Remove room                         ");
        System.out.println("[3] List rooms                          ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private boolean selectHouse() {
        ScreenUtils.clearScreen();
        System.out.println("\n=========> Select house <=========\n");

        int index = 1;
        var housesList = new ArrayList<>(houseManager.getHouses());

        for (House house : housesList) {
            System.out.printf("[%d] %s%n", index++, house.getName());
        }

        System.out.println("[b] Back to main menu\n");
        System.out.print("Select house number: ");

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("b")) {
            return false;
        }

        try {
            int houseIndex = Integer.parseInt(input) - 1;
            if (houseIndex >= 0 && houseIndex < housesList.size()) {
                selectedHouse = housesList.get(houseIndex);
                rooms = new TreeSet<>((r1, r2) -> r1.getName().compareToIgnoreCase(r2.getName()));
                rooms.addAll(selectedHouse.getRooms());
                return true;
            } else {
                System.out.println("\nInvalid house number!");
                ScreenUtils.pressEnterToContinue(scanner);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input! Please enter a number.");
            ScreenUtils.pressEnterToContinue(scanner);
            return false;
        }
    }


    private void addRoom() {
        ScreenUtils.clearScreen();

        System.out.println("\n============> Add room <============\n");

        System.out.print("Enter room name: ");
        String name = scanner.next();

        if (rooms.stream().anyMatch(r -> r.getName().equalsIgnoreCase(name))) {
            System.out.println("A room with this name already exists!");
            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n============> Add room <============\n");

        System.out.println("[1] Bedroom                             ");
        System.out.println("[2] Kitchen                             ");
        System.out.println("[3] Garage                              ");
        System.out.println("[4] Living room                         ");
        System.out.println("[5] Dining room                         ");
        System.out.println("[6] Bathroom                            ");

        System.out.print("\nChoose room type: ");

        int typeIndex = scanner.nextInt();
        RoomType type;

        switch (typeIndex) {
            case 1 -> type = RoomType.BEDROOM;
            case 2 -> type = RoomType.KITCHEN;
            case 3 -> type = RoomType.GARAGE;
            case 4 -> type = RoomType.LIVING_ROOM;
            case 5 -> type = RoomType.DINING_ROOM;
            case 6 -> type = RoomType.BATHROOM;

            default -> {
                System.out.println("Invalid room type! Please try again.");
                ScreenUtils.pressEnterToContinue(scanner);
                return;
            }

        }

        Room room = new Room(name, type);

        rooms.add(room);
        selectedHouse.addRoom(room);

        System.out.println("\nRoom added successfully!");
    }

    private void removeRoom() {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Remove room <===========\n");

        if (rooms.isEmpty()) {
            System.out.println("No rooms found!");
            return;
        }
    }

    private void listRooms() {
        ScreenUtils.clearScreen();

        System.out.println("\n===========> List rooms <===========\n");

        if (rooms.isEmpty()) {
            System.out.println("No rooms found!");
            return;
        }

        int index = 1;

        for (Room room : rooms) {
            System.out.printf("%d. %s (%s)",
                    index++,
                    room.getName(),
                    room.getType()
            );
        }
    }
}
