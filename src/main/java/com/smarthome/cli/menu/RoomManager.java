package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;
import com.smarthome.core.model.room.RoomType;

import java.util.*;

public class RoomManager {
    private final Scanner scanner;
    private final HouseManager houseManager;
    private House selectedHouse;

    public RoomManager(Scanner scanner, HouseManager houseManager) {
        this.scanner = scanner;
        this.houseManager = houseManager;
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

    private void addRoom() {
        House house = ScreenUtils.selectHouse(scanner, houseManager);

        if (house == null) return;

        this.selectedHouse = house;

        ScreenUtils.clearScreen();

        System.out.println("\n============> Add room <============\n");

        System.out.print("Enter room name: ");
        String name = scanner.nextLine();

        if (selectedHouse.getRooms().stream().anyMatch(room -> room.getName().equalsIgnoreCase(name))) {
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
                System.out.println("Invalid room type selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    private void removeRoom() {
        House house = ScreenUtils.selectHouse(scanner, houseManager);

        if (house == null) return;

        this.selectedHouse = house;

        ScreenUtils.clearScreen();

        System.out.println("\n==========> Remove Room <===========\n");

        if (selectedHouse.getRooms().isEmpty()) {
            System.out.println("No rooms found in the selected house!");
            return;
        }

        Set<Room> houseRooms = selectedHouse.getRooms();

        if (houseRooms.isEmpty()) {
            System.out.println("No rooms found in the selected house!");
            return;
        }

        System.out.println("Rooms in " + selectedHouse.getName() + ":\n");

        List<Room> roomsList = new ArrayList<>(houseRooms);
        int index = 1;

        for (Room room : roomsList) {
            System.out.printf("[%d] %s (Type: %s)%n",
                    index++,
                    room.getName(),
                    room.getType()
            );
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= roomsList.size()) {
                Room roomToRemove = roomsList.get(choice - 1);

                selectedHouse.removeRoom(roomsList.get(choice - 1));

                System.out.println("\nRoom '" + roomToRemove.getName() + "' removed successfully!");
            } else {
                System.out.println("Invalid room number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }

    }

    private void listRooms() {
        House house = ScreenUtils.selectHouse(scanner, houseManager);

        if (house == null) return;

        this.selectedHouse = house;

        ScreenUtils.clearScreen();

        System.out.println("\n==========> List Rooms <===========\n");

        Set<Room> houseRooms = selectedHouse.getRooms();

        if (houseRooms.isEmpty()) {
            System.out.println("No rooms found in the selected house!");
            return;
        }

        System.out.println("Rooms in " + selectedHouse.getName() + ":\n");

        List<Room> roomsList = new ArrayList<>(houseRooms);
        int index = 1;

        for (Room room : roomsList) {
            System.out.printf("[%d] %s (Type: %s)%n",
                    index++,
                    room.getName(),
                    room.getType()
            );
        }
    }

    public Set<Room> getRooms() {
        return selectedHouse.getRooms();
    }
}
