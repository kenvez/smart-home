package com.smarthome.cli.settings;

import com.smarthome.cli.utils.MenuUtils;
import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;
import com.smarthome.core.model.room.RoomType;

import java.util.Scanner;

public class RoomSettings {
    private final Scanner scanner;

    public RoomSettings(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
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
