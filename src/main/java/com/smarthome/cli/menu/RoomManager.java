package com.smarthome.cli.menu;

import com.smarthome.cli.utils.MenuUtils;
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


    public Set<Room> getRooms(House house) {
        return house.getRooms();
    }
}
